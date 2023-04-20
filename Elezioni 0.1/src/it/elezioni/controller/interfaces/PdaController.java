package it.elezioni.controller.interfaces;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import it.elezioni.form.PdaForm;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author mgalasi
 */
@Controller
@RequestMapping(value = PdaController.URL_ROOT)
public interface PdaController {

    static final String URL_ROOT = "/pda";

    static final String URL_COMPILE = "/compile";

    static final String URL_CARICA_PDA = "/caricaPda";
    static final String URL_VIEW_PDA = "example/pda/pda";

    static final String URL_SCRIVI_AGGIORNA_PDA = "/scriviAggiornaPda";
   
    static final String URL_VIEW_PDA_PRE_CARICA = "pda/pdaPreCarica";

    static final String URL_REFRESH_MAPPE = "/refreshMap";

    public static final String URL_STATE_UPDATE = "/setStatusBackOffice";

    @RequestMapping(value = URL_CARICA_PDA)
    public String caricaPda(
    		@RequestParam(value = "iOffer",required = false)Long idOffer,
    		@RequestParam(value = "token",required = false)String token,
    		@RequestParam(value = "fromScript",required = false)String fromScript,
    		PdaForm pdaForm, 
    		BindingResult result, 
    		Model model,Locale locale);
    
    @RequestMapping(value = URL_SCRIVI_AGGIORNA_PDA, method = RequestMethod.POST)
    public String scriviAggiornaPda(
    		@RequestParam(value = "idPda",required = false) Long idPda, 
    		PdaForm pdaForm, BindingResult result, 
    		Model model,
    		HttpServletRequest request ,
    		Locale locale);

    @ResponseBody
    @RequestMapping(value = URL_REFRESH_MAPPE)
    public String refreshMappe();
}


