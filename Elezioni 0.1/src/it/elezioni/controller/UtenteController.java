package it.elezioni.controller;

import java.util.List;

import it.elezioni.data.model.Candidatura;
import it.elezioni.data.model.Elezione;
import it.elezioni.data.model.Utente;
import it.elezioni.data.model.Votazione;
import it.elezioni.service.CandidaturaService;
import it.elezioni.service.CaricaService;
import it.elezioni.service.UtenteService;
import it.elezioni.service.VotazioneService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"utente"})
public class UtenteController {

	private static final Logger LOGGER = Logger.getLogger(UtenteController.class);
	
	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	private CandidaturaService candidaturaService;
	
	@Autowired
	private VotazioneService votazioneService;
	
	@RequestMapping(value = "/candidatiListByIdElezione")
	public @ResponseBody List<Utente> elencoCandidatiByIdElezione(@RequestParam(required = true) Integer idElezione)
	{
		LOGGER.info("elencoCandidatiByIdElezione");
	
		List<Utente> utenteList = utenteService.elencoCandidatiByIdElezione(idElezione);
		return utenteList;
			
	}
	
	
	@RequestMapping(value = "/registraCandidaturaUtente", method = RequestMethod.POST)
	public @ResponseBody String registraCandidaturaUtente(@RequestParam(required = true) Integer idElezione, @ModelAttribute Utente utente)
	{
		LOGGER.info("registraCandidaturaUtente");
		try {
			Candidatura candidatura = new Candidatura();
			
			candidatura.setIdUtente(utente.getIdUtente());
			candidatura.setIdElezione(idElezione);
			candidaturaService.insertCandidaturaUtente(candidatura);
			return "candidatura registrata";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e.toString());
			return "<b style='color:red;'>"+e.toString()+"</b>";
		}	
	}
	
	
	@RequestMapping(value = "/verificaAzioniUtente")
	public @ResponseBody List<Utente> verificaAzioniUtente(@RequestParam(required = true) String titolo,@ModelAttribute Utente utente)
	{
		LOGGER.info("verificaAzioniUtente");
	
		List<Utente> utenteList = utenteService.selectAzioniUtenteById(utente.getIdUtente(),titolo);
		return utenteList;
			
	}
	
	@RequestMapping(value = "/registraVotoUtente", method = RequestMethod.POST)
	public @ResponseBody String registraVotoUtente(@RequestParam(required = true) Integer idElezione, @RequestParam(required = true) Boolean haVotato, @ModelAttribute Utente utente)
	{
		LOGGER.info("registraVotoUtente");
		
			try {
				Votazione votazione = new Votazione();
				votazione.setIdUtente(utente.getIdUtente());
				votazione.setIdElezione(idElezione);
				votazione.setHaVotato(haVotato);
				Integer n = votazioneService.insertVotazioneUtente(votazione);
				return "voto registrato";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LOGGER.error(e.toString());
				return e.toString();
			}
		
	}
	
	
	@RequestMapping(value = "/registraUtente")
	public @ResponseBody String registraUtente(Utente utente)
	{
		LOGGER.info("registraUtente");
	
		try {
			utenteService.insertUtente(utente);
			
		} catch (Exception e) {

			return e.toString();
		}
		
		return "<br/><br/> Utente registrato con successo! <br/>"+
					"Accedi al sistema elezioni con le tue credenziali. Potrai candidarti, oppure votare un candidato. ";
			
	}
	
	@RequestMapping(value = "/utentiMaiCandidati")
	public @ResponseBody List<Utente> utentiMaiCandidati()
	{
		LOGGER.info("utentiMaiCandidati");
	
		List<Utente> utenteList = utenteService.utentiMaiCandidati();
		return utenteList;
			
	}
	
}
