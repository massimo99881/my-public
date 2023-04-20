package it.elezioni.data.model;

public class Votazione {

	private Integer idElezione;
	private Integer idUtente;
	private Boolean haVotato;
	
	public Integer getIdElezione() {
		return idElezione;
	}
	public void setIdElezione(Integer idElezione) {
		this.idElezione = idElezione;
	}
	public Integer getIdUtente() {
		return idUtente;
	}
	public void setIdUtente(Integer idUtente) {
		this.idUtente = idUtente;
	}
	public Boolean getHaVotato() {
		return haVotato;
	}
	public void setHaVotato(Boolean haVotato) {
		this.haVotato = haVotato;
	}
	
	
}
