package it.elezioni.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.elezioni.data.example.Carica;

@Component
public class CaricaFormValidator implements Validator {


	public boolean supports(Class<?> c) {
		return Carica.class.isAssignableFrom(c);
	}

	
	public void validate(Object command, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "descr", "carica.desc.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "durata", "carica.durata.empty");
		Carica caricaBean = (Carica)command;
		if(caricaBean!=null){
			if(caricaBean.getDurata()!=null){
				String durataString = Integer.toString(caricaBean.getDurata());
				if(!isNumber(durataString.trim()))
					errors.rejectValue("durata", "carica.durata.NAN");
			}
		}
		
	}

	private boolean isNumber(String str){
	    for (int i = 0; i < str.length(); i++) {
	        
	        //If we find a non-digit character we return false.
	        if (!Character.isDigit(str.charAt(i)))
	        return false;
	        }
	         
	        return true;
	}
}
