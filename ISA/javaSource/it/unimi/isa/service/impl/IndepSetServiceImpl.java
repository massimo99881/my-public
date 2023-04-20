package it.unimi.isa.service.impl;


import it.unimi.isa.beans.IndepMatrixBean;
import it.unimi.isa.model.graphs.Graph;
import it.unimi.isa.model.graphs.GraphFactory;
import it.unimi.isa.service.IndepSetService;
import it.unimi.isa.utils.Constants;
import it.unimi.isa.utils.ISAConsole;
import it.unimi.isa.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class IndepSetServiceImpl implements IndepSetService {
	
	private static final Logger logger = Logger.getLogger(IndepSetServiceImpl.class);
	
	private List<Integer> lastComputedKList;
	private List<Integer> rowSumList;
	private List<HashMap <Integer,List<Integer>>> independentMatrix;
	
	public IndepSetServiceImpl(){
	}
	
	public List<HashMap<Integer,List<Integer>>> forzaBruta(Graph g) throws Exception {
		
		this.lastComputedKList = new ArrayList<Integer>();
		this.rowSumList = new ArrayList<Integer>();
		this.independentMatrix = new ArrayList<HashMap<Integer,List<Integer>>>();
		
		int [] potenzaCamminoVerticale = g.getPotenzaCamminoVerticale();
		int [] potenzaCamminoOrizzontale = g.getPotenzaCamminoOrizzontale();
		int n = g.getN();
		int m = g.getM();
		String f = g.getF();
		
		if("igraph".equalsIgnoreCase(f)){
			independentMatrix = findIndepSetsIGraph(g,n);
		}
		else {
			if(Constants.ENABLE_CACHE_LOAD){
				logger.info("ENABLE_CACHE_LOAD = true");
				IndepMatrixBean indepMatrixBean = Utils.loadFromCache(n, m, f, potenzaCamminoVerticale, potenzaCamminoOrizzontale, g.getNomeFileCache(), Constants.PATH_TO_CACHE_INDEP);
				independentMatrix = indepMatrixBean.getIndependentMatrix();
				rowSumList = indepMatrixBean.getRowSumList();
				lastComputedKList = indepMatrixBean.getLastComputedKList();
				
				if(independentMatrix==null)
				{
					independentMatrix = findIndepSets(n,m,f,potenzaCamminoVerticale, potenzaCamminoOrizzontale);
					if(Constants.ENABLE_CACHE_SAVE){
						logger.info("ENABLE_CACHE_SAVE = true");
						Utils.saveInCache(n,m,f,potenzaCamminoVerticale, potenzaCamminoOrizzontale, g.getNomeFileCache(), Utils.stampaMatriceMath(independentMatrix), Constants.PATH_TO_CACHE_INDEP);
					}
					
				}
			}
			else {
				independentMatrix = findIndepSets(n,m,f,potenzaCamminoVerticale, potenzaCamminoOrizzontale);
				if(Constants.ENABLE_CACHE_SAVE){
					Utils.saveInCache(n,m,f,potenzaCamminoVerticale, potenzaCamminoOrizzontale, g.getNomeFileCache(), Utils.stampaMatriceMath(independentMatrix), Constants.PATH_TO_CACHE_INDEP);
				}
			}
		}
		
		return independentMatrix;
	}
	
	

	public List<HashMap <Integer,List<Integer>>> findIndepSets(int N, int M, String F, int[] potenzeCamminoVerticale, int[] potenzeCamminoOrizzontale) throws Exception{
		//ProgressSample progress = new ProgressSample();
		
		List<HashMap <Integer,List<Integer>>> listIndip = new ArrayList<HashMap<Integer,List<Integer>>>();
		lastComputedKList = new ArrayList<Integer>();
        rowSumList = new ArrayList<Integer>();
		
		int sum = 0;
		
		for (int n = 0; n <= N; n++) {
			boolean foundZero = false;
			int K = M*(n);
			
			Graph g = GraphFactory.getGraph(n, M, F, potenzeCamminoVerticale, potenzeCamminoOrizzontale);

			g.createIndipMatrix();
			
			List<Integer> vertexListQn = new ArrayList<Integer>();

			for (int i = 0; i < K; i++){
				vertexListQn.add(i);
			}
			HashMap <Integer,List<Integer>> hash = new HashMap<Integer,List<Integer>>();
			List<Integer> list = new ArrayList<Integer>();
			
			for (int k = 0; !foundZero; k++) {
				int numIndip = INDI(/*progress,*/ n,true, 0, k, vertexListQn, g);
				sum += numIndip;
				
				if(numIndip==0){
					foundZero = true;
					lastComputedKList.add(k-1);
					rowSumList.add(sum);
					sum = 0;
				}
				else{
					list.add(numIndip);
					hash.put(n,list);
					
					logger.info(g.getNome()+" n="+n+" k="+k+"..........");
					System.out.println(g.getNome()+" n="+n+" k="+k+"..........");
					Utils.stampaMatrice(listIndip);
					Utils.printHash2(hash);
				}
				
			}
			listIndip.add(hash);
			
		}
		
		this.setIndependentMatrix(listIndip);
		
		return listIndip;
	}
	

	public List<HashMap <Integer,List<Integer>>> findIndepSetsIGraph(Graph g, int n) {
		List<HashMap <Integer,List<Integer>>> listIndip = new ArrayList<HashMap<Integer,List<Integer>>>();
		int sum = 0;
		
		boolean foundZero = false;
		int K = g.V();//M*(n);
		
		g.createIndipMatrix();
		//if(Constants.DEBUG_ENABLED && n==Input.n){
			logger.info(g);
		//}
		List<Integer> vertexListQn = new ArrayList<Integer>();
//		int tot  = m;
//		if(Input.m>1){
//			tot = (m + 1) * (n);
//		}
//		else{
//			tot = (m * n);
//		}
		for (int i = 0; i < K; i++){
			vertexListQn.add(i);
		}
		HashMap <Integer,List<Integer>> hash = new HashMap<Integer,List<Integer>>();
		List<Integer> list = new ArrayList<Integer>();
		
		for (int k = 0; /*k <= K && */!foundZero; k++) {
			int numIndip = INDI_IGraph(true, 0, k, vertexListQn, g, n);
			sum += numIndip;
			
			if(numIndip==0){
				foundZero = true;
				lastComputedKList.add(k-1);
				rowSumList.add(sum);
				sum = 0;
			}
			else{
				list.add(numIndip);
				hash.put(0,list);
				
				logger.info("........................................................");
				logger.info("n="+n+" k="+k);
				Utils.stampaMatrice(listIndip);
				Utils.printHash2(hash);
				logger.info("........................................................");
			}
		}
		listIndip.add(hash);
		
		this.setIndependentMatrix(listIndip);
		
		return listIndip;
	}
	
	private int INDI_IGraph(boolean firstcall,int i,int k,List<Integer> list,Graph g, int n) {
		int numIndip = 0;
		List<Set<Integer>> res = new ArrayList<Set<Integer>>();
		getSubsets(list, k, 0, new HashSet<Integer>(), res);
		for (Set<Integer> set : res) {
			if(firstcall){
				int perc = (100 * i) / res.size();
				ISAConsole.setNotifylineText("calculating .. n="+n+",k=" + k + " - " + perc + "% - #subset=" + res.size());
				i++;
			}
				
			Integer[] setArr = set.toArray(new Integer[set.size()]);
			int t = setArr.length;
			if ((t == 0) || 
				(t == 1 && g.contains(setArr[0], setArr[0]) == 0) ||
				(t == 2 && g.contains(setArr[0], setArr[1]) == 0)
				) {
				numIndip++;
			}
			if (t >= 3) {			
				if (t == INDI_IGraph(false,i,k-1, new ArrayList<Integer>(set), g, n)){
					numIndip++;
				}					
				t--;				
			}
		}
		return numIndip;
	}

	private int INDI(int n, boolean firstcall,int i,int k,List<Integer> list,Graph g) {
		int numIndip = 0;
		List<Set<Integer>> res = new ArrayList<Set<Integer>>();
		getSubsets(list, k, 0, new HashSet<Integer>(), res);
		for (Set<Integer> set : res) {
			if(firstcall){
				int perc = (100 * i) / res.size();
				ISAConsole.setNotifylineText("calculating .. n="+n+",k=" + k + " - " + perc + "% - #subset=" + res.size());
				i++;
			}
				
			Integer[] setArr = set.toArray(new Integer[set.size()]);
			int t = setArr.length;
			if ((t == 0) || 
				(t == 1 && g.contains(setArr[0], setArr[0]) == 0) ||
				(t == 2 && g.contains(setArr[0], setArr[1]) == 0)
				) {
				numIndip++;
			}
			if (t >= 3) {			
				if (t == INDI(n,false,i,k-1, new ArrayList<Integer>(set), g)){
					numIndip++;
				}					
				t--;				
			}
		}
		return numIndip;
	}

	private void getSubsets(List<Integer> Qn, int k, int idx, 
			Set<Integer> current, List<Set<Integer>> solution) {
		
		// successful stop clause
		if (current.size() == k) {
			Set<Integer> a = new HashSet<Integer>(current); //current
			solution.add(a);
			return;
		}
		// unseccessful stop clause
		if (idx == Qn.size()){			
			return;
		}
			
		Integer x = Qn.get(idx);
		current.add(x);
		// "guess" x is in the subset
		getSubsets(Qn, k, idx + 1, current, solution);
		current.remove(x);
				
		// "guess" x is not in the subset
		getSubsets(Qn, k, idx + 1, current, solution);
	}

	public void setLastComputedKList(List<Integer> lastComputedKList) {
		this.lastComputedKList = lastComputedKList;
	}

	public List<Integer> getLastComputedKList() {
		return lastComputedKList;
	}

	public void setIndependentMatrix(List<HashMap <Integer,List<Integer>>> independentMatrix) {
		this.independentMatrix = independentMatrix;
	}

	public List<HashMap <Integer,List<Integer>>> getIndependentMatrix() {
		return independentMatrix;
	}

	public List<Integer> getRowSumList() {
		return rowSumList;
	}

	public void setRowSumList(List<Integer> rowSumList) {
		this.rowSumList = rowSumList;
	}
	
	
	
}