package it.elezioni.data.model;

import java.io.Serializable;
import java.util.Date;

public class Elezione implements Serializable {

	private Integer idElezione;
	private Date dataCrea;
	private Date dataIni;
	private Date dataFine;
	private String descr;
	private Integer idCarica;
	private String stato;

	//no db schema
	private Date dataFineCarica;
	private Carica carica;
	private Integer totVoti;
	private Integer nrVotanti;
	private Integer candidati;
	private String nomeEletto;
	private String cognomeEletto;
	
	public Integer getIdElezione() {
		return idElezione;
	}
	public void setIdElezione(Integer idElezione) {
		this.idElezione = idElezione;
	}
	
	
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public Carica getCarica() {
		return carica;
	}
	public void setCarica(Carica carica) {
		this.carica = carica;
	}
	
	public Date getDataFineCarica() {
		return dataFineCarica;
	}
	public void setDataFineCarica(Date dataFineCarica) {
		this.dataFineCarica = dataFineCarica;
	}
	public Integer getIdCarica() {
		return idCarica;
	}
	public void setIdCarica(Integer idCarica) {
		this.idCarica = idCarica;
	}
	public Date getDataCrea() {
		return dataCrea;
	}
	public void setDataCrea(Date dataCrea) {
		this.dataCrea = dataCrea;
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
	public Integer getTotVoti() {
		return totVoti;
	}
	public void setTotVoti(Integer totVoti) {
		this.totVoti = totVoti;
	}
	public Integer getNrVotanti() {
		return nrVotanti;
	}
	public void setNrVotanti(Integer nrVotanti) {
		this.nrVotanti = nrVotanti;
	}
	public Integer getCandidati() {
		return candidati;
	}
	public void setCandidati(Integer candidati) {
		this.candidati = candidati;
	}
	public String getNomeEletto() {
		return nomeEletto;
	}
	public void setNomeEletto(String nomeEletto) {
		this.nomeEletto = nomeEletto;
	}
	public String getCognomeEletto() {
		return cognomeEletto;
	}
	public void setCognomeEletto(String cognomeEletto) {
		this.cognomeEletto = cognomeEletto;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	
	
	
}
