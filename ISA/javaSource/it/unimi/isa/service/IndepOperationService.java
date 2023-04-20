package it.unimi.isa.service;

public interface IndepOperationService {

	public Integer[][] execute(Integer[][] firstIndep, Integer[][] secondIndep, String operation);
	public Integer[][] createIndependentTable(String [] type) throws Exception;
	
}
