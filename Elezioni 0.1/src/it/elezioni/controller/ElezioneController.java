package it.elezioni.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import it.elezioni.data.model.Carica;
import it.elezioni.data.model.Elezione;
import it.elezioni.data.model.Utente;
import it.elezioni.form.ElezioneForm;
import it.elezioni.service.CaricaService;
import it.elezioni.service.ElezioneService;
import it.elezioni.service.UtenteService;
import it.elezioni.validator.ElezioneFormValidator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"utente"})
public class ElezioneController {

	private static final Logger LOGGER = Logger.getLogger(ElezioneController.class);
	
	@Autowired
	private ElezioneService elezioneService;
	
	@Autowired
	private CaricaService caricaService;
	
	@Autowired
	private UtenteService utenteService;
	
	
	@InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
         SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
         dateFormat.setLenient(false);
         webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
     }
	
	@RequestMapping(value = "mostraElezioniInCorso")
	public @ResponseBody List<Elezione> mostraElezioniInCorso(@RequestParam(required = false) String type,@ModelAttribute Utente utente) throws Exception{
		
		LOGGER.info("mostraElezioniInCorso");
		List<Elezione> elezioneList = null;
		List<Utente> utenteList = null;
		Integer n = null;
		
		//controllo elezioni da disattivare
		if("PerVotare".equals(type)){
			elezioneService.disattivazioneElezioniPerVotazioni();
		}
		
		//TODO ottimizzare con una CREATE VIEW le query ..
		//verifica se utente è già candidato per una elezione in corso
		utenteList = utenteService.controlloUtenteGiaCandidato(utente.getIdUtente());
		n= utenteList.size();
		
		if(n!=null ){
			if(n==1){
				LOGGER.info("utente gia candidato a una elezione in corso");
				utente.setGiaCandidato(true);
				type += "GiaCandidato";
			}
			else if(n==0){
				LOGGER.info("utente NON è gia candidato a una elezione in corso");
				utente.setGiaCandidato(false);
			}
			else if(n>1){
				String error = "Error checking number of candidates: more then one candidate in result!";
				LOGGER.error(error);
				throw new Exception(error);				
			}
		}	
		
		LOGGER.info("type: "+type);
		
		//user views
		if("PerCandidarsiGiaCandidato".equals(type))
			elezioneList = elezioneService.selectElezioniInCorsoUser(type,utenteList.get(0));
		if("PerVotareGiaCandidato".equals(type))
			elezioneList = elezioneService.selectElezioniInCorsoUser(type,utenteList.get(0));
		if("PerCandidarsi".equals(type))
			elezioneList = elezioneService.selectElezioniInCorsoUser(type,null);
		if("PerVotare".equals(type))
			elezioneList = elezioneService.selectElezioniInCorsoUser(type,null);
		
		if(type==null || "".equals(type))
			//guest only
			elezioneList = elezioneService.selectElezioniInCorso();
		return elezioneList;
	}
	
	@RequestMapping(value = "mostraStoricoElezioniGuest")
	public @ResponseBody List<Elezione> mostraStoricoElezioniGuest(){
		LOGGER.info("mostraStoricoElezioniGuest");
		List<Elezione> elezioneList = elezioneService.selectElezioniStorico();
		return elezioneList;
	}
	
	
	@RequestMapping(value = "elezioniSenzaCandidati")
	public @ResponseBody List<Elezione> elezioniSenzaCandidati(){
		LOGGER.info("elezioniSenzaCandidati");
		List<Elezione> elezioneList = elezioneService.elezioniSenzaCandidati();
		return elezioneList;
	}
	
	@RequestMapping(value = "/cercaElezioneByTitolo")
	public @ResponseBody List<Elezione> cercaElezioneDescr(@RequestParam(required = true) String elezioneDescr)
	{
		LOGGER.info("cercaElezioneDescr");
		List<Elezione> elezioneList = elezioneService.selectCaricaJoinElezione("",elezioneDescr);
		for(Elezione e: elezioneList){
			LOGGER.info(e.getDataCrea()+";");
			LOGGER.info(e.getDataIni()+";");
			LOGGER.info(e.getDataFine()+";");
			LOGGER.info(e.getDataFineCarica()+";");
			LOGGER.info("--------------");
		}
		return elezioneList;
	}
	
	@RequestMapping(value = "/deleteElezioneById")
	public @ResponseBody void deleteElezioneById(@RequestParam(required = true) Integer idElezione)
	{
		LOGGER.info("deleteElezioneById");
		try {
			elezioneService.deleteElezioneById(idElezione);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error(e.toString());
		}
		
	}
	
	
	@RequestMapping(value = "/disattivaElezioneById")
	public @ResponseBody void disattivaElezioneById(@RequestParam(required = true) Integer idElezione)
	{
		LOGGER.info("disattivaElezioneById");
		try {
			elezioneService.disattivaElezioneById(idElezione);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error(e.toString());
		}
		
	}
	
	@RequestMapping(value = "/creaElezione")
	public @ResponseBody String creaElezione(Elezione elezione)
	{
		LOGGER.info("creaElezione");
		elezioneService.creaElezione(elezione);
		return "<br/>Elezione inserita. <a href='#' onclick='resetValueForRegistraElezione();'>Inserisci un'altra elezione.</a>";
	}
	
	@RequestMapping(value = "/creaElezioneConCarica",method = RequestMethod.POST)
	public @ResponseBody String creaElezioneConCarica(ElezioneForm elezioneForm, BindingResult result)
	{
		LOGGER.info("creaElezioneECarica");
		ElezioneFormValidator validator = new ElezioneFormValidator();
        validator.validate(elezioneForm, result);
		if (result.hasErrors()) {
			return "<font style='color:red;'>input invalido</font>";
		} 
//		else {
//			return "Done";
//		}
		
		Carica carica = new Carica();
		carica.setTitolo(elezioneForm.getTitolo());
		carica.setDurata(elezioneForm.getDurata());
		caricaService.insertCarica(carica);
		
		Integer idC = carica.getIdCarica();
		
		Elezione elezione = new Elezione();
		elezione.setDataIni(elezioneForm.getDataIni());
		elezione.setDataFine(elezioneForm.getDataFine());
		elezione.setDescr(elezioneForm.getDescr());
		elezione.setIdCarica(idC);
		elezioneService.creaElezione(elezione);
		return "<br/>Elezione inserita. <a href='#' onclick='resetValueForRegistraElezione();'>Inserisci un'altra elezione.</a>";
	}
	
	
}
