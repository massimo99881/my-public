/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elezioni.validator;


import it.elezioni.form.PdaForm;
//import org.apache.commons.lang.StringUtils;


import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author jdinardo
 */
public class PdaFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

    public void validate(Object o, Errors errors) {

        PdaForm pdaForm = (PdaForm)o;
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pdaClienteSedeL.ragSoc", "required.generic");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pdaClienteSedeL.formaGiuridica", "required.generic");
        // Forma giuridica + ragione sociale + 1 non superiore a 100 caratteri
        int ragSocLength = pdaForm.getPdaClienteSedeL().getRagSoc().length();
        int totLenght  = 1 + ragSocLength;
        if(totLenght > 100) {
            errors.rejectValue("pdaClienteSedeL.ragSoc", "format.RagioneSocialeTroppoLunga");
        }

//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pdaClienteSedeL.parIva", "required.generic");
//        String piva = StringUtils.trim(pdaForm.getPdaClienteSedeL().getParIva());
//        String exp = "^[0-9]{11}$";
//        if ( StringUtils.length(piva) > 0 ){
//            if (!piva.matches(exp)) {
//               errors.rejectValue("pdaClienteSedeL.parIva", "format.piva");
//            }
//        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pdaClienteSedeL.indirizzo", "required.generic");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pdaClienteSedeL.nCivico", "required.generic");
        
        if (pdaForm.getIndirizzoSpedizioneAltro() != null && pdaForm.getIndirizzoSpedizioneAltro().equalsIgnoreCase("true")) {

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pda.spedIndirizzo", "required.generic");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pda.spedNCivico", "required.generic");
        }


        if(pdaForm.getSedeAttivazioneAltra() != null && pdaForm.getSedeAttivazioneAltra().equalsIgnoreCase("true")) {
            
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pdaClienteSedeA.ragSoc", "required.generic");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pdaClienteSedeA.indirizzo", "required.generic");
        }

    }

	
}
