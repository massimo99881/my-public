package it.unimi.isa.model;

import java.util.HashMap;
import java.util.List;

public class GrammarLanguage {

	List<String[][]> paroleConsentite;
	List<String[][]> paroleNonConsentite;
	HashMap <String,List<String>> sistemaProseguimento;
	
	public List<String[][]> getParoleConsentite() {
		return paroleConsentite;
	}
	public void setParoleConsentite(List<String[][]> paroleConsentite) {
		this.paroleConsentite = paroleConsentite;
	}
	public List<String[][]> getParoleNonConsentite() {
		return paroleNonConsentite;
	}
	public void setParoleNonConsentite(List<String[][]> paroleNonConsentite) {
		this.paroleNonConsentite = paroleNonConsentite;
	}
	public void setSistemaProseguimento(
			HashMap<String, List<String>> sistemaProseguimento) {
		this.sistemaProseguimento = sistemaProseguimento;
		
	}
	public HashMap<String, List<String>> getSistemaProseguimento() {
		return sistemaProseguimento;
	}
	
	
}
