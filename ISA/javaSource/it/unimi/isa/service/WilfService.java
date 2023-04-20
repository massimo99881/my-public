package it.unimi.isa.service;

import it.unimi.isa.model.graphs.Graph;

public interface WilfService {

	public String[] findRSnFromTM_v2(String[][] xTM);
	public String[] findRSnFromTM_v3(String[][] xTM, Graph g);
}
