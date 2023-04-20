package it.unimi.isa.service.impl;

import it.unimi.isa.model.graphs.Graph;
import it.unimi.isa.service.IndepOperationService;
import it.unimi.isa.utils.Utils;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class IndepOperationServiceImpl implements IndepOperationService {
	
	private static final Logger logger = Logger.getLogger(IndepOperationServiceImpl.class);

	/**
	 * Esegue il calcolo specificato dal parametro <operation> tra due tabelle di indipendenti
	 * @param firstIndep
	 * @param secondIndep
	 * @param operation
	 * @return
	 */
	public Integer[][] execute(Integer[][] firstIndep, Integer[][] secondIndep, String operation) {
		
		int row = firstIndep.length;
		int col = firstIndep[0].length;
		if(secondIndep.length>row){
			row = secondIndep.length;
		}
		if(secondIndep[0].length>col){
			col = secondIndep[0].length;
		}
		
		Integer[][] result = new Integer[row][col];
		Utils.init(result);
		
		for(int i=0;i<firstIndep.length;i++){
			for(int j=0;j<firstIndep[i].length;j++){
				Integer firstValue = checkExists(firstIndep,i,j);
				Integer secondValue = checkExists(secondIndep,i,j);
				
				if("+".equals(operation)){
					result[i][j] = firstValue + secondValue;
				}
				else if("-".equals(operation)){
					result[i][j] = firstValue - secondValue;
				}
			}
		}
		
		Utils.print(result);
		
		return result;
	}

	/**
	 * Controllo esistenza valore cella (i,j)
	 * @param table
	 * @param i
	 * @param j
	 * @return
	 */
	private Integer checkExists(Integer [][] table, int i, int j) {
		Integer value = new Integer(0);
		try{
			value = table[i][j] == null ? new Integer(0) : table[i][j];
		}
		catch(Exception e){
			value = new Integer(0);
		}
		
		return value;
	}

	/**
	 * 
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Integer[][] createIndependentTable(String [] type) throws Exception {
		logger.info("createIndependentTable ..");
		
		Graph g = Utils.ottieneGrafo(type, 1, 3);
		IndepSetServiceImpl tester = new IndepSetServiceImpl();
		List<HashMap<Integer,List<Integer>>> indepList = tester.forzaBruta(g);
		Integer[][] result = Utils.convertIndepHashMapListToMatrix(indepList); 
		
		return Utils.copyMatrix(result);
	}
}
