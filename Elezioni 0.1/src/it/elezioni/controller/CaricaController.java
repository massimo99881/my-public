package it.elezioni.controller;

import java.util.List;

import it.elezioni.data.model.Carica;
import it.elezioni.data.model.Elezione;
import it.elezioni.form.ElezioneForm;
import it.elezioni.service.CaricaService;
import it.elezioni.service.ElezioneService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CaricaController {

	private static final Logger LOGGER = Logger.getLogger(CaricaController.class);
	
	@Autowired
	private CaricaService caricaService;
	
	@Autowired
	private ElezioneService elezioneService;
	
	@RequestMapping(value = "/cercaCaricaById")
	public @ResponseBody Carica cercaCaricaById(@RequestParam(required = true) Integer idCarica)
	{
		LOGGER.info("cercaCaricaById");
	
		return caricaService.selectCaricaById(idCarica);
	
	}
	
	@RequestMapping(value = "/cercaCaricaAdmin")
	public @ResponseBody List<Elezione> cercaCarica(@RequestParam(required = true) String titolo,@RequestParam(required = true) String elezione)
	{
		LOGGER.info("cercaCarica");
	
		List<Elezione> elezioneList = elezioneService.selectCaricaJoinElezione(titolo, elezione);
		return elezioneList;
	
	}
	
	@RequestMapping(value = "/cercaCaricaSoloDescr")
	public @ResponseBody List<Carica> cercaCaricaDescr(@RequestParam(required = true) String titolo,ModelMap model)
	{
		LOGGER.info("cercaCarica");
		model.addAttribute("elezioneForm", new ElezioneForm());
		List<Carica> caricaList = caricaService.selectCaricaByName(titolo);
		return caricaList;
	}
	
	@RequestMapping(value = "/cancellaCarica")
	public @ResponseBody List<Carica> cancellaCarica(@RequestParam(required = true) Integer idCarica,@RequestParam(required = true) String titolo)
	{
		LOGGER.info("cancellaCarica");
		caricaService.deleteCaricaById(idCarica);
		List<Carica> caricaList = caricaService.selectCaricaByName(titolo);
		return caricaList;
	}
	
	@RequestMapping(value = "/creaNuovaCarica")
	public @ResponseBody String creaNuovaCarica(Carica carica)
	{
		LOGGER.info("creaNuovaCarica");
		String result;
		try{
			Integer n = caricaService.insertCarica(carica);
			result = "Nuova carica <b>"+carica.getTitolo()+"</b> inserita ,durata: <b>"+carica.getDurata()+" anni</b> " ;
		}
		catch(Exception e){
			result = "Si è verificato un errore";
			LOGGER.error(e);
		}
		
		return result;
	}
	
	
	@RequestMapping(value = "/modificaCarica")
	public @ResponseBody String modificaCarica( Carica carica)
	{
		LOGGER.info("modificaCarica");
		String result;
		try{
			int n = caricaService.updateCarica(carica);
			result = "carica modificata " ;
		}
		catch(Exception e){
			result = "Si è verificato un errore";
			LOGGER.error(e);
		}
		
		return result;
	}
	
}
