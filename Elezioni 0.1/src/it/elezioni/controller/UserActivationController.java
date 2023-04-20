package it.elezioni.controller;

import java.util.List;

import it.elezioni.data.model.Utente;
import it.elezioni.service.UtenteService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller

public class UserActivationController {

	private static final Logger LOGGER = Logger.getLogger(UserActivationController.class);

	@Autowired
	private UtenteService utenteService;

	@RequestMapping(value = "/cambiaStatoUtente")
	 public @ResponseBody
	 void cambiaStatoUtente(@RequestParam(required = true) Integer idUtente, @RequestParam(required = true) Boolean stato)
	 {
		utenteService.updateStatoUtente(idUtente, stato);
		LOGGER.info("cambiaStatoUtente");
	 
	 }
	
	@RequestMapping(value = "/cercaUtenti")
	 public @ResponseBody
	 List<Utente> cercaUtenti(@RequestParam(required = true) String nome,@RequestParam(required = true) String cognome)
	 {
		LOGGER.info("cercaUtenti");

		List<Utente> utenteList = utenteService.selectUtenteByName(nome,cognome);
		return utenteList;
	 }
	
	
	@RequestMapping(value = "/visualizzaUtenti")
	 public @ResponseBody
	 List<Utente> visualizzaUtenti()
	 {
		LOGGER.info("visualizzaUtenti");

		List<Utente> utenteList = utenteService.selectAllUtente();
		return utenteList;
	 }

}
