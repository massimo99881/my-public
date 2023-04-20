package it.elezioni.controller.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.validation.BindingResult;

import it.elezioni.data.example.LoginForm;

import java.util.Map;

@Controller
@RequestMapping("loginform")
@SessionAttributes("logged")
public class LoginController {
	
	@RequestMapping(method = RequestMethod.GET)
	public String showForm(Map model) {
		LoginForm loginForm = new LoginForm();
		model.put("loginForm", loginForm);
		return "example/loginform";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String processForm(@ModelAttribute LoginForm loginForm,
			BindingResult result, Map model) {
		String userName = "UserName";
		String password = "password";
		
		loginForm = (LoginForm) model.get("loginForm");
		if (!loginForm.getUserName().equals(userName)
				|| !loginForm.getPassword().equals(password)) {
			return "example/loginform";
		}
		
		loginForm.setLoggedIn(true);
		model.put("logged", loginForm);
		return "example/loginsuccess";
	}

}