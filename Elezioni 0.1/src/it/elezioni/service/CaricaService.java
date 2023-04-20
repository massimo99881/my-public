package it.elezioni.service;

import java.util.List;

import it.elezioni.data.model.Carica;

public interface CaricaService {

	public List<Carica> selectCaricaByName(String titolo);
	public void deleteCaricaById(Integer idCarica);
	public Integer insertCarica(Carica carica);
	public Integer updateCarica(Carica carica);
	public Carica selectCaricaById(Integer idCarica);
	
}
