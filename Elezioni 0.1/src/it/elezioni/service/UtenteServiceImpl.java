package it.elezioni.service;

import java.util.List;

import it.elezioni.data.dao.PdaDao;
import it.elezioni.data.dao.UtenteDao;
import it.elezioni.data.model.Utente;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtenteServiceImpl implements UtenteService {
	
	@Autowired
    private UtenteDao utenteDao;
	
	private static final Logger LOGGER = Logger.getLogger(UtenteServiceImpl.class);
	
	@Override
	public Utente selectUtenteForLogin(Utente utente) {
		return utenteDao.selectUtenteForLogin(utente);
	}
	
	@Override
	public List<Utente> selectAllUtente() {
		return utenteDao.selectAllUtente();
	}

	@Override
	public Utente selectUtenteById(Integer idUtente) {
		return utenteDao.selectUtenteById(idUtente);
	}

	@Override
	public void updateUtente(Utente utente) {
		utenteDao.updateUtente(utente);
		
	}

	@Override
	public Integer insertUtente(Utente utente) {
		return utenteDao.insertUtente(utente);
		
	}

	@Override
	public void deleteUtenteById(Integer idUtente) {
		utenteDao.deleteUtenteById(idUtente);
		
	}

	@Override
	public void updateStatoUtente(Integer idUtente, Boolean stato) {
		utenteDao.updateStatoUtente(idUtente,stato); 
		
	}

	@Override
	public List<Utente> selectUtenteByName(String nome, String cognome) {
		return utenteDao.selectUtenteByName(nome, cognome);
	}

	@Override
	public List<Utente> elencoCandidatiByIdElezione(Integer idElezione) {
		return utenteDao.elencoCandidatiByIdElezione(idElezione);
	}

	@Override
	public List<Utente> selectAzioniUtenteById(Integer idUtente, String type) {
		return utenteDao.selectAzioniUtenteById(idUtente,type);
	}

	@Override
	public List<Utente> controlloUtenteGiaCandidato(Integer idUtente) {
		return utenteDao.controlloUtenteGiaCandidato(idUtente);
	}

	@Override
	public List<Utente> utentiMaiCandidati() {
		return utenteDao.utentiMaiCandidati();
	}

	

}
