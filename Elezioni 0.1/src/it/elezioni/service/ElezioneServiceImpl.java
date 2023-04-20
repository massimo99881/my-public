package it.elezioni.service;

import it.elezioni.data.dao.ElezioneDao;
import it.elezioni.data.model.Elezione;
import it.elezioni.data.model.Utente;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElezioneServiceImpl implements ElezioneService {
	
	@Autowired
    private ElezioneDao elezioneDao;
	
	private static final Logger LOGGER = Logger.getLogger(ElezioneServiceImpl.class);

	@Override
	public List<Elezione> selectCaricaJoinElezione(String titolo, String elezione) {
		return elezioneDao.selectCaricaJoinElezione(titolo, elezione);
	}

	@Override
	public Integer creaElezione(Elezione elezione) {
		return elezioneDao.creaElezione(elezione);
	}

	@Override
	public List<Elezione> selectElezioniStorico() {
		return elezioneDao.selectElezioniStorico();
	}

	@Override
	public List<Elezione> selectElezioniInCorso() {
		return elezioneDao.selectElezioniInCorso();
	}
	
	@Override
	public List<Elezione> selectElezioniInCorsoUser(String type, Utente utente) {
		return elezioneDao.selectElezioniInCorsoUser(type, utente);
	}

	public void deleteElezioneById(Integer idElezione){
		elezioneDao.deleteElezioneById(idElezione);
	}

	public List<Elezione> elezioniSenzaCandidati(){
		return elezioneDao.elezioniSenzaCandidati();
	}
	
	public void disattivaElezioneById(Integer idElezione){
		elezioneDao.disattivaElezioneById(idElezione);
	}
	
	public void disattivazioneElezioniPerVotazioni(){
		elezioneDao.disattivazioneElezioniPerVotazioni();
	}
	
}
