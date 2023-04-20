package it.elezioni.service;

import it.elezioni.data.dao.VotazioneDao;
import it.elezioni.data.model.Votazione;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotazioneServiceImpl implements VotazioneService {

	@Autowired
    private VotazioneDao votazioneDao;
	
	private static final Logger LOGGER = Logger.getLogger(VotazioneServiceImpl.class);
	
	@Override
	public Integer insertVotazioneUtente(Votazione votazione) {
		return votazioneDao.insertVotazioneUtente(votazione);
	}

}
