package it.elezioni.controller.example;

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ExampleController {
	
	@Autowired
    Comparator<String> comparator;

	@RequestMapping(value = "/HelloWorld")
	public String home() {
		System.out.println("HomeController: Passing through...");
		//commento questo hardcoding perche ho messo nel servlet-context.xml il bean InternalResourceViewResolver
		//return "WEB-INF/views/home.jsp";
		return "/example/hello/home";
	}
	
	@RequestMapping(value = "/compare", method = RequestMethod.GET)
    public String compare(@RequestParam("input1") String input1,
    				@RequestParam("input2") String input2, Model model) {
 
        int result = comparator.compare(input1, input2);
        String inEnglish = (result < 0) ? "less than" : (result > 0 ? "greater than" : "equal to");
 
        String output = "According to our Comparator, '" + input1 + "' is " + inEnglish + "'" + input2 + "'";
 
        model.addAttribute("output", output);
        return "/example/hello/compareResult";
    }
}