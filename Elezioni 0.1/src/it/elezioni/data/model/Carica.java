package it.elezioni.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Carica implements Serializable {

	private static final long serialVersionUID = 753890282310672118L;
	
	private Integer idCarica;
	private String titolo;
	private Integer durata;
	private List<Elezione> elezioni = new ArrayList<Elezione>();
	
	
	//getters and setters
	
	public Integer getIdCarica() {
		return idCarica;
	}
	public void setIdCarica(Integer idCarica) {
		this.idCarica = idCarica;
	}
	
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public Integer getDurata() {
		return durata;
	}
	public void setDurata(Integer durata) {
		this.durata = durata;
	}
	public List<Elezione> getElezioni() {
		return elezioni;
	}
	public void setElezioni(List<Elezione> elezioni) {
		this.elezioni = elezioni;
	}
	
	
	
}
