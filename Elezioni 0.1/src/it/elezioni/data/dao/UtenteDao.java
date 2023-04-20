package it.elezioni.data.dao;

import java.util.List;

import it.elezioni.data.model.Utente;
import org.apache.ibatis.annotations.Param;

public interface UtenteDao {

	public Utente selectUtenteById(Integer idUtente);
	public List<Utente> selectAllUtente();
	public Utente selectUtenteForLogin(Utente utente);
	public void updateUtente(Utente utente);
	public Integer insertUtente(Utente utente);
	public void deleteUtenteById(Integer idUtente);
	public void updateStatoUtente(@Param("idUtente") Integer idUtente,@Param("stato") Boolean stato);
	public List<Utente> selectUtenteByName(@Param("nome") String nome, @Param("cognome") String cognome);
	public List<Utente> elencoCandidatiByIdElezione(Integer idElezione);
	public List<Utente> selectAzioniUtenteById(@Param("idUtente") Integer idUtente,@Param("type") String type);
	public List<Utente> controlloUtenteGiaCandidato(@Param("idUtente") Integer idUtente);
	public List<Utente> utentiMaiCandidati();
}
