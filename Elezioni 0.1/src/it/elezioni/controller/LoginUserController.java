package it.elezioni.controller;


import it.elezioni.data.model.Utente;
import it.elezioni.form.ElezioneForm;
import it.elezioni.service.PdaService;
import it.elezioni.service.UtenteService;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("utente")
public class LoginUserController {
	
	@Autowired
    private UtenteService utenteService;
	

	@RequestMapping(value = "/loginutente",method = RequestMethod.GET)
	public String caricaIndex(Map model) {
		Utente loginUtente = new Utente();
		model.put("utente", loginUtente);
		return "menu";
	}
	
	@RequestMapping(value = "/esterno", method = RequestMethod.GET)
	public String loginEsterno(Map model,HttpSession session){
		//se è un utente registrato che accede come esterno..
		session.removeAttribute("utente");
		
		Utente guest = new Utente();
		model.put("guestUsr",guest);
		return "guest";
	}

	@RequestMapping(value = "/loginutente",method = RequestMethod.POST)
	public String loginUtente(@ModelAttribute Utente utente,BindingResult result, ModelMap model) { //Map
		
		utente = (Utente) model.get("utente"); 
		
		Utente u = utenteService.selectUtenteForLogin(utente);
		
		
		if (u!=null){
		
			Integer idUtente = u.getIdUtente();			
			Boolean isAdmin = u.getIsAdmin();
			Boolean attivo = u.getAttivo();		
			
			if (!attivo){
				return "/noActive";
			}
			
			if(idUtente!=null && isAdmin!=null){
				
				model.put("utente", u);
				
				if(isAdmin){
					model.addAttribute("elezioneForm", new ElezioneForm());
					return "/admin/home";
				}
				else {
					return "/user/home";
				}
			}
		}
		
		//return "redirect:loginutente";
		
		return "/loginFailed";
	}
}
