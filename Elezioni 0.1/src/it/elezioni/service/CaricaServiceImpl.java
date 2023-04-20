package it.elezioni.service;

import it.elezioni.data.dao.CaricaDao;
import it.elezioni.data.model.Carica;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CaricaServiceImpl implements CaricaService {
	
	@Autowired
    private CaricaDao caricaDao;

	@Transactional(value = "txConfigManager")
	public void deleteCaricaById(Integer idCarica) {
		caricaDao.deleteCaricaById(idCarica);
	}

	public List<Carica> selectCaricaByName(String titolo) {
		return caricaDao.selectCaricaByName(titolo);
	}

	public Integer insertCarica(Carica carica) {
		return caricaDao.insertCarica(carica);
	}

	@Override
	public Integer updateCarica(Carica carica) {
		return caricaDao.updateCarica(carica);
	}

	@Override
	public Carica selectCaricaById(Integer idCarica) {
		return caricaDao.selectCaricaById(idCarica);
	}

	

}
