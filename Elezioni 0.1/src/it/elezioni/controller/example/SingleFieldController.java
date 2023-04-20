package it.elezioni.controller.example;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("thought")
public class SingleFieldController {

	@RequestMapping(value="/single-field")
	public ModelAndView singleFieldPage() {
		return new ModelAndView("example/single-field-page");
	}
	
	@RequestMapping(value="/remember")	
	public ModelAndView rememberThought(@RequestParam String thoughtParam) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("thought", thoughtParam);
		modelAndView.setViewName("example/single-field-page");
		return modelAndView;
	}
	
}
