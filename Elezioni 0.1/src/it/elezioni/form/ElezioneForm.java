package it.elezioni.form;

import java.io.Serializable;
import java.util.Date;


public class ElezioneForm implements Serializable {

	private Integer idElezione;
	private Date dataIni;
	private Date dataFine;
	private String descr;
	private Integer idCarica;
	private String titolo;
	private Integer durata;
	
	
	public Integer getIdElezione() {
		return idElezione;
	}
	public void setIdElezione(Integer idElezione) {
		this.idElezione = idElezione;
	}
	public Date getDataIni() {
		return dataIni;
	}
	public void setDataIni(Date dataIni) {
		this.dataIni = dataIni;
	}
	public Date getDataFine() {
		return dataFine;
	}
	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
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
	
	
	
}
