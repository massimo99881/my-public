package it.elezioni.controller;

import it.elezioni.data.model.Utente;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("logoututente")
@SessionAttributes("utente")
public class LogoutUserController {

	@RequestMapping(method = RequestMethod.GET)
	public String caricaIndex(Map model,HttpSession session) {
		session.removeAttribute("utente");
		return "redirect:/";
	}
}
