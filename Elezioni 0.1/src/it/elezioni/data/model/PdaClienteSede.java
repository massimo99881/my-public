package it.elezioni.data.model;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author mgalasi
 */
public class PdaClienteSede {

    private Long idSede;
    private String ragSoc;
    private String indirizzo;
    private String cap;
    private String comune;
    private String nCivico;
    private Long idPda;
    
    
	public Long getIdSede() {
		return idSede;
	}
	public void setIdSede(Long idSede) {
		this.idSede = idSede;
	}
	public String getRagSoc() {
		return ragSoc;
	}
	public void setRagSoc(String ragSoc) {
		this.ragSoc = ragSoc;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getCap() {
		return cap;
	}
	public void setCap(String cap) {
		this.cap = cap;
	}
	public String getComune() {
		return comune;
	}
	public void setComune(String comune) {
		this.comune = comune;
	}
	public String getnCivico() {
		return nCivico;
	}
	public void setnCivico(String nCivico) {
		this.nCivico = nCivico;
	}
	public Long getIdPda() {
		return idPda;
	}
	public void setIdPda(Long idPda) {
		this.idPda = idPda;
	}
   
    
}
