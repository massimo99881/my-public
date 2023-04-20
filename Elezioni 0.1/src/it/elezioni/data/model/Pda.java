package it.elezioni.data.model;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.List;


public class Pda {

    private Long idPda;
    private Long idCliente;
    private String data;
    private String luogo;
    private String spedIndirizzo;
    private String spedNCivico;
    
	public Long getIdPda() {
		return idPda;
	}
	public void setIdPda(Long idPda) {
		this.idPda = idPda;
	}
	public Long getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getLuogo() {
		return luogo;
	}
	public void setLuogo(String luogo) {
		this.luogo = luogo;
	}
	public String getSpedIndirizzo() {
		return spedIndirizzo;
	}
	public void setSpedIndirizzo(String spedIndirizzo) {
		this.spedIndirizzo = spedIndirizzo;
	}
	public String getSpedNCivico() {
		return spedNCivico;
	}
	public void setSpedNCivico(String spedNCivico) {
		this.spedNCivico = spedNCivico;
	}
   
    
}
