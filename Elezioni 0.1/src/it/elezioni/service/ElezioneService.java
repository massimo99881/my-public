package it.elezioni.service;

import java.util.List;

import it.elezioni.data.model.Carica;
import it.elezioni.data.model.Elezione;
import it.elezioni.data.model.Utente;

public interface ElezioneService {

	public List<Elezione> selectCaricaJoinElezione(String titolo, String elezione);
	public List<Elezione> selectElezioniInCorsoUser(String type, Utente utente);
	public List<Elezione> selectElezioniInCorso();
	public List<Elezione> selectElezioniStorico();
	public Integer creaElezione(Elezione elezione);
	public void deleteElezioneById(Integer idElezione);
	public List<Elezione> elezioniSenzaCandidati();
	public void disattivaElezioneById(Integer idElezione);
	public void disattivazioneElezioniPerVotazioni();
}
