package it.elezioni.data.model;

import java.io.Serializable;

public class Utente implements Serializable {

	private static final long serialVersionUID = -8983741575983357272L;
	
	private Integer idUtente;
	private String nome;
	private String cognome;
	private Boolean attivo;
	private Boolean isAdmin;
	
	private String account;
	private String password;
	
	//
	
	private Integer idElezione;
	private Boolean voto;
	private Integer nrVoti;
	private Boolean giaCandidato;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getIdUtente() {
		return idUtente;
	}
	public void setIdUtente(Integer idUtente) {
		this.idUtente = idUtente;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public Boolean getAttivo() {
		return attivo;
	}
	public void setAttivo(Boolean attivo) {
		this.attivo = attivo;
	}
	public Boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public Integer getIdElezione() {
		return idElezione;
	}
	public void setIdElezione(Integer idElezione) {
		this.idElezione = idElezione;
	}
	public Boolean getVoto() {
		return voto;
	}
	public void setVoto(Boolean voto) {
		this.voto = voto;
	}
	public Integer getNrVoti() {
		return nrVoti;
	}
	public void setNrVoti(Integer nrVoti) {
		this.nrVoti = nrVoti;
	}
	public Boolean getGiaCandidato() {
		return giaCandidato;
	}
	public void setGiaCandidato(Boolean giaCandidato) {
		this.giaCandidato = giaCandidato;
	}
		
}
