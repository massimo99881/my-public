package it.elezioni.data.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import it.elezioni.data.model.Carica;
import it.elezioni.data.model.Elezione;
import it.elezioni.data.model.Utente;

public interface ElezioneDao {

	public List<Elezione> selectCaricaJoinElezione(@Param("titolo") String titolo,@Param("elezione") String elezione);
	public Integer creaElezione(Elezione elezione);
	public List<Elezione> selectElezioniInCorsoUser(@Param("type") String type,@Param("utente") Utente utente);
	public List<Elezione> selectElezioniInCorso();
	public List<Elezione> selectElezioniStorico();
	public void deleteElezioneById(@Param("idElezione") Integer idElezione);
	public List<Elezione> elezioniSenzaCandidati();
	public void disattivaElezioneById(@Param("idElezione") Integer idElezione);
	public void disattivazioneElezioniPerVotazioni();
}
