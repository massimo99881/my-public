package it.elezioni.service;

import java.util.List;

import it.elezioni.data.model.Utente;

public interface UtenteService {

	public Utente selectUtenteById(Integer idUtente);
	public List<Utente> selectAllUtente();
	public Utente selectUtenteForLogin(Utente utente);
	public void updateUtente(Utente utente);
	public Integer insertUtente(Utente utente);
	public void deleteUtenteById(Integer idUtente);
	public void updateStatoUtente(Integer idUtente,Boolean stato);
	public List<Utente> selectUtenteByName(String nome, String cognome);
	public List<Utente> elencoCandidatiByIdElezione(Integer idElezione);
	public List<Utente> selectAzioniUtenteById(Integer idUtente,String type);
	public List<Utente> controlloUtenteGiaCandidato(Integer idUtente);
	public List<Utente> utentiMaiCandidati();
}
