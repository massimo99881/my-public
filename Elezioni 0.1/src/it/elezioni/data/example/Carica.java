package it.elezioni.data.example;

import java.io.Serializable;

public class Carica implements Serializable {

	private static final long serialVersionUID = -2380710592347964138L;
	
	private Integer idCarica;
	private String descr;
	private Integer durata;
	
	public Integer getIdCarica() {
		return idCarica;
	}
	public void setIdCarica(Integer idCarica) {
		this.idCarica = idCarica;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public Integer getDurata() {
		return durata;
	}
	public void setDurata(Integer durata) {
		this.durata = durata;
	}
	
	
}
