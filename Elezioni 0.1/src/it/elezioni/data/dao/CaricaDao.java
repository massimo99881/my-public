package it.elezioni.data.dao;

import it.elezioni.data.model.Carica;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface CaricaDao {

	public List<Carica> selectCaricaByName(@Param("titolo") String titolo);
	public void deleteCaricaById(@Param("idCarica") Integer idCarica);
	public Integer insertCarica(Carica carica);
	public Integer updateCarica(Carica carica);
	public Carica selectCaricaById(@Param("idCarica") Integer idCarica);
}
