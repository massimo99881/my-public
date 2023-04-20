package it.elezioni.controller.impl;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import it.elezioni.controller.interfaces.PdaController;
import it.elezioni.data.model.Pda;
import it.elezioni.data.model.PdaClienteSede;
import it.elezioni.form.PdaForm;
import it.elezioni.service.PdaService;
import it.elezioni.util.Constants;
import it.elezioni.util.GenericError;
import it.elezioni.validator.PdaFormValidator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author mgalasi
 */
public class PdaControllerImpl implements PdaController {

    private static final Logger LOGGER = Logger.getLogger(PdaControllerImpl.class);
    @Autowired
    private PdaService pdaService;
   

    // Pre-carica la form Pda
    public String caricaPda(
    		Long idOffer, String token, String fromScript, 
    		PdaForm pdaForm, BindingResult result, Model model, Locale locale) {

        LOGGER.debug("caricaPda - parm : idOffer " + idOffer);
        LOGGER.debug("caricaPda - parm : token - " + token);
        try {

        if (fromScript == null) {
            fromScript = "false";
        }

        Pda pda = pdaService.getPdaById(idOffer);

        if (pda != null) {

            LOGGER.debug("caricaPda - pda.getIdOffer " + pda.getIdPda());
            LOGGER.debug("caricaPda - pda.getIdPda " + pda.getIdPda());
        }

        // Se non trovata crea Pda
        if (pda == null) {
            pda = new Pda();
            
        }

        pdaForm.setPda(pda);
        pdaForm.setPdaClienteSedeL(new PdaClienteSede());
        pdaForm.setPdaClienteSedeA(new PdaClienteSede());
       
       
        model.addAttribute("pdaForm", pdaForm);

        } catch (Exception e) {
            LOGGER.error(PdaController.class, e);
            model.addAttribute("errorMsg", new GenericError("Errore nel caricamento della PDA", Constants.GENERIC_ERROR));
            LOGGER.error("PDA: Errore nella connessione al Database");
            model.addAttribute("fromScript", pdaForm.getFromScript());
            this.formatDateForViewException(pdaForm);
            return URL_VIEW_PDA;
        }

        return URL_VIEW_PDA;

    }

    // Scrive/Aggiorna la Pda
    public String scriviAggiornaPda(Long idPda, PdaForm pdaForm, BindingResult result, Model model, HttpServletRequest request, Locale locale) {

        LOGGER.debug("scriviAggiornaPda - FORM: " + pdaForm.toString());

        LOGGER.debug("caricaPda - parm : token - " + pdaForm.getToken());
        LOGGER.debug("idPda da pdaForm.getPda().getIdPda(): " + pdaForm.getPda().getIdPda());
        LOGGER.debug("selectedValidate da pdaForm.getSelectedValidate(): " + pdaForm.getSelectedValidate());

        if (pdaForm.getFromScript() == null || pdaForm.getFromScript().equals("")) {
            pdaForm.setFromScript("false");
        }
        LOGGER.debug("scriviAggiornaPda - fromScript : " + pdaForm.getFromScript());

        //Se cliccato su non validato non esegue controlli
        if (pdaForm.getSelectedValidate() != null && pdaForm.getSelectedValidate().equalsIgnoreCase("true")) {

            PdaFormValidator validator = new PdaFormValidator();
            validator.validate(pdaForm, result);

            if (result.hasErrors()) {
               
            	
                return URL_VIEW_PDA;
            }
        }

        try {

            this.wrtUpdPdaCustomerData(pdaForm);

        } catch (DataAccessException eDA) {
            LOGGER.error(PdaController.class, eDA);
            model.addAttribute("errorMsg", new GenericError("Errore nella connessione al Database", Constants.GENERIC_ERROR));
            LOGGER.error("PDA: Errore nella connessione al Database");
          
            model.addAttribute("fromScript", pdaForm.getFromScript());
            this.formatDateForViewException(pdaForm);
            return URL_VIEW_PDA;
        } catch (Exception e) {
            LOGGER.error(PdaController.class, e);
            model.addAttribute("errorMsg", new GenericError("Errore nella scrittura della PDA", Constants.GENERIC_ERROR));
            LOGGER.error("PDA: Errore nella connessione al Database");
            
            model.addAttribute("fromScript", pdaForm.getFromScript());
            this.formatDateForViewException(pdaForm);
            return URL_VIEW_PDA;
        }

        //FARE EVENTUALI CONTROLLI SE VA SEMPRE MOSTRATA

        // String returnPage = "redirect:" + URL_ROOT + URL_CARICA_PDA_OFFERTA_MOBILE+"/"+pdaForm.getPda().getIdOffer()+"/"+pdaForm.getPda().getIdPda();
        // if (pdaForm.getFromScript() != null && !pdaForm.getFromScript().equals("")) {
        String returnPage = "forward:" + URL_ROOT + "";
        request.setAttribute("idPda", pdaForm.getPda().getIdPda());
        request.setAttribute("fromScript", pdaForm.getFromScript());
        request.setAttribute("token", pdaForm.getToken());

        //  }

        LOGGER.debug("forward path : " + returnPage);

        return returnPage;

    }

    

    private void wrtUpdPdaCustomerData(PdaForm pdaForm) throws Exception {

        // Dati tabella pda
        Pda pda = pdaForm.getPda();

        LOGGER.info("id pda : " + pda.getIdPda());

        boolean fromScript = false;
        if (pdaForm.getFromScript() != null && pdaForm.getFromScript().equalsIgnoreCase("true")) {
           fromScript = true;
        }
        boolean validata = false;
        if (pdaForm.getSelectedValidate() != null && pdaForm.getSelectedValidate().equalsIgnoreCase("true")) {
           validata = true;
        }

        if (pda.getData() != null && !pda.getData().equals("")) {
            pda.setData(this.formatDateForDb(pda.getData()));

        }

        // Dati tabella pda_cliente_sede
        List<PdaClienteSede> pdaCliSedList = new ArrayList();

        // Ref = L : DATI DELLA SEDE LEGALE
        PdaClienteSede pdaCliSedL = pdaForm.getPdaClienteSedeL();
        // PER ORA NO VALIDO QUANDO SI INTRODURRANNO LE COMBO O FORSE E' MEGLIO FARLO DECODIFICARE IN FASE DI GENERAZIONE XML 
        // String comunePdaCliSedL = pdaCliSedL.getComune();
        // pdaCliSedL.setComune(this.getDescComune(comunePdaCliSedL));
      
        pdaCliSedList.add(pdaCliSedL);

        // Ref = A : DATI DELLA SEDE DI ATTIVAZIONE (se selezionato)
        if (pdaForm.getSedeAttivazioneAltra() != null && pdaForm.getSedeAttivazioneAltra().equalsIgnoreCase("true")) {
            PdaClienteSede pdaCliSedA = pdaForm.getPdaClienteSedeA();
         
            pdaCliSedList.add(pdaCliSedA);
        }

        // Dati tabella pda_attore
      
        // Inserimento dati nelle tre tabelle
        if (pda.getIdPda() == null) {
            pdaService.insertPdaCustomerData(pda, pdaCliSedList);
        } else {
            pdaService.updatePdaCustomerData(pda, pdaCliSedList);
        }


    }

    private String formatDateForView(String dateInput) {

        String outputString = dateInput.substring(8, 10) + "-" + dateInput.substring(5, 7) + "-" + dateInput.substring(0, 4);

        LOGGER.debug("data formattata " + outputString);

        return outputString;

    }

    private String formatDateForDb(String dateInput) {


        String outputString = dateInput.substring(6, 10) + "-" + dateInput.substring(3, 5) + "-" + dateInput.substring(0, 2);

        LOGGER.info("data formattata per db " + outputString);

        return outputString;

    }

    private String formatToDbTime(String timeInput) {

        String outputString = timeInput.substring(0, 2) + timeInput.substring(3, 5) + "00";

        LOGGER.debug("time formattato per db " + outputString);

        return outputString;

    }

    

    private PdaForm formatDateForViewException(PdaForm pdaForm) {

        if (pdaForm.getPda().getData() != null && !pdaForm.getPda().getData().equals("")) {
                LOGGER.debug("dataPrima - "+pdaForm.getPda().getData());
                pdaForm.getPda().setData(this.formatDateForViewException(pdaForm.getPda().getData()));
        }

        return pdaForm;

    }

    private String formatDateForViewException(String dateInput) {

        String outputString = dateInput.substring(6, 8) + "-" + dateInput.substring(4, 6) + "-" + dateInput.substring(0, 4);
        return outputString;

    }

    public String refreshMappe() {
        //pdaConstantsService.refresh();
        return "refresh";
    }

}
