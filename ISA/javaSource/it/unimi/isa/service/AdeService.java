package it.unimi.isa.service;

import java.util.List;

import com.wolfram.jlink.MathLinkException;

import it.unimi.isa.exceptions.AdeException;

public interface AdeService {
	
	public String[][] complete() throws MathLinkException, AdeException;
	public void obtainRecurrenceAndGF(String[][] matrix) throws AdeException;
	public String obtainGeneratingFuncion(String sequence) throws AdeException;
	public String [][] fakeComplete();
	public void obtainFakeRecurrence();
	public List<Integer> getAntidiagonals(Integer [][] independentMatrix);
	
}
