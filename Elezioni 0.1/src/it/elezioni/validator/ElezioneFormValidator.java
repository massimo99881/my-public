package it.elezioni.validator;

import it.elezioni.data.model.Elezione;
import it.elezioni.form.ElezioneForm;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ElezioneFormValidator implements Validator{

	@Override
	public boolean supports(Class c) {
		return Elezione.class.equals(c);
	}

	@Override
	public void validate(Object obj, Errors e) {
		
		ElezioneForm elezioneForm = (ElezioneForm) obj;
		ValidationUtils.rejectIfEmpty(e, "dataIni", "dataIni.empty");
		ValidationUtils.rejectIfEmpty(e, "dataFine", "dataFine.empty");
		ValidationUtils.rejectIfEmpty(e, "descr", "descr.empty");
		ValidationUtils.rejectIfEmpty(e, "titolo", "titolo.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "durata", "durata.empty");
		
		if (elezioneForm.getDurata()==null) 
			e.rejectValue("durata", "durata.empty");
		else{
			if (elezioneForm.getDurata() < 0) {
	            e.rejectValue("durata", "durata.negative_value");
	        } else if (elezioneForm.getDurata() > 7) {
	            e.rejectValue("durata", "durata.over");
	        }
		}
        
	}

}
