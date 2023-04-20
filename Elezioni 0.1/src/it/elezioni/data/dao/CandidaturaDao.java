package it.elezioni.data.dao;

import org.apache.ibatis.annotations.Param;

import it.elezioni.data.model.Candidatura;



public interface CandidaturaDao {

	public Integer insertCandidaturaUtente(Candidatura candidatura);
}
