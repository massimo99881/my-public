package it.unimi.isa.service;

import it.unimi.isa.model.graphs.Graph;

import java.util.HashMap;
import java.util.List;

public interface IndepSetService {

	public List<HashMap<Integer,List<Integer>>> forzaBruta(Graph g) throws Exception;
	
}
