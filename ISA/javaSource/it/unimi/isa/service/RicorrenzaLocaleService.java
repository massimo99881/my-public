package it.unimi.isa.service;

import java.util.HashMap;
import java.util.List;


public interface RicorrenzaLocaleService {

	public boolean findLocal(String fgoRsn, List<HashMap<Integer,List<Integer>>> indepList, List<Integer> kList);
}
