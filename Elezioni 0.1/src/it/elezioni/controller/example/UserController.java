package it.elezioni.controller.example;



import it.elezioni.data.example.JsonResponse;
import it.elezioni.data.example.User;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class UserController {
	private List<User> userList = new ArrayList<User>(); 
	
	@RequestMapping(value="/AddUserValidation",method=RequestMethod.GET)
	public String showForm(){
		return "example/ajax/AddUserValidation";
	}
	
	@RequestMapping(value="/AddUserValidation",method=RequestMethod.POST)
	public @ResponseBody JsonResponse addUser(@ModelAttribute(value="user") User user, BindingResult result ){
		JsonResponse res = new JsonResponse();
		ValidationUtils.rejectIfEmpty(result, "name", "Name can not be empty.");
		ValidationUtils.rejectIfEmpty(result, "education", "Educatioan not be empty");
		if(!result.hasErrors()){
			userList.add(user);
			res.setStatus("SUCCESS");
			res.setResult(userList);
		}else{
			res.setStatus("FAIL");
			res.setResult(result.getAllErrors());
		}
		
		return res;
	}

}
