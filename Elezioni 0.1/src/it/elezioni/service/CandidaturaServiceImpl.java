package it.elezioni.service;

import it.elezioni.data.dao.CandidaturaDao;
import it.elezioni.data.dao.CaricaDao;
import it.elezioni.data.model.Candidatura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidaturaServiceImpl implements CandidaturaService {

	@Autowired
    private CandidaturaDao candidaturaDao;
	
	@Override
	public void insertCandidaturaUtente(Candidatura candidatura) {
		candidaturaDao.insertCandidaturaUtente(candidatura);
	}
}
