package it.elezioni.controller.example;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WelcomeController {
	
	@RequestMapping("/welcome")
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ModelAndView model = new ModelAndView("example/hello/WelcomePage");
		
		return model;
	}

	//private static Logger log = Logger.getLogger(WelcomeController.class);  
	   
	 @Autowired  
	 ReloadableResourceBundleMessageSource messageSource;  
	   
	 @RequestMapping("/reloadlang")  
	 public String showMainPage(){  
	  //log.info("=============== RELOAD LANGUAGE ===============");  
	  messageSource.clearCache();  
	  return "redirect:/";  
	 } 
	 
	 /*this helps u when u want change properties files without restart. 
	  * Only u need save your properties file and then call URL: "http://<your web project url>/reloadlang"*/
	
}