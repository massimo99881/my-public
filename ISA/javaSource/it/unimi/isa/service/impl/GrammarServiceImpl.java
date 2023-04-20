package it.unimi.isa.service.impl;


import it.unimi.isa.exceptions.GrammarException;
import it.unimi.isa.exceptions.GraphException;
import it.unimi.isa.model.Bacchetta;
import it.unimi.isa.model.GrammarLanguage;
import it.unimi.isa.model.Vertex;
import it.unimi.isa.model.graphs.Graph;
import it.unimi.isa.service.GrammarService;
import it.unimi.isa.singleton.Singleton;
import it.unimi.isa.utils.Constants;
import it.unimi.isa.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wolfram.jlink.KernelLink;

@Service
public class GrammarServiceImpl implements GrammarService {
	
	@Autowired
	Singleton singleton;
	
	private static final Logger logger = Logger.getLogger(GrammarServiceImpl.class);

	private String letteraFGO;
	
	public String getLetteraFGO() {
		return letteraFGO;
	}

	@SuppressWarnings("unchecked")
	public String[][] ottieneTransferMatrix(HashMap <String,List<String>> sistema) {
		logger.info("ottiene Transfer Matrix");
		
		//Inizializzo la TM
		logger.info("");
		logger.info("Ottengo la Transfer Matrix: ");
		logger.info("");
	    int dim = sistema.size();
	    String[][] xTM = new String[dim][dim];
	    for(int i=0;i<xTM.length;i++){
	    	for(int j=0;j<xTM[i].length;j++){
	    		xTM[i][j] = "0";
	    	}
	    }
	    
	    //1.creo indici per ciascuna parola consentita (potrebbe essere ottimizzato)
	    HashMap<String,Integer> hashmap = new HashMap<String,Integer>();
	    Integer indice = 0;
	    Iterator entries = sistema.entrySet().iterator();
		while (entries.hasNext()) {
		    Map.Entry entry = (Map.Entry) entries.next();
		    String key = (String)entry.getKey();
		    hashmap.put(key, indice++);
		}
		
		String tempString = "";
		Iterator entries2 = hashmap.entrySet().iterator();
		while (entries2.hasNext()) {
		    Map.Entry entry2 = (Map.Entry) entries2.next();
		    String key = (String)entry2.getKey();
		    Integer value = (Integer) entry2.getValue();
		    tempString += key + ":"+value;
		    tempString += "\n";
		}
		logger.info(tempString);
		
		//2.produco la TM
		Iterator entries3 = sistema.entrySet().iterator();
		while (entries3.hasNext()) {
		    Map.Entry entry3 = (Map.Entry) entries3.next();
		    String key = (String)entry3.getKey();
		    int row = hashmap.get(key);
		    
		    List<String> values = (ArrayList<String>) entry3.getValue();
		    
		    for(String s : values){
		    	int col = hashmap.get(s);
		    	xTM[row][col] = "x";
		    }
		   
		}
		
		Utils.print(xTM);
		
		return xTM;
	}

	/**
	 * L'algoritmo con l'ottimizzazione:

1-crea il sistema "proseguimento" in cui per ciascuna parola valida valuto le possibili scelte (un solo passo successivo)
2-postporre alle variabili dipendenti la parte sx (h-1 lettere) della variabile indipendente
	 * @param paroleConsentite
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap <String,List<String>> ottieneSistemaLineareLinguaggio(HashMap <String,List<String>> sistemaProseguimento, Graph g) {
		int h = g.findMaxPotenzaCamminoOrizzontale();
		
		logger.info("Sistema lineare");
		
		HashMap <String,List<String>> hash = new HashMap<String,List<String>>();
		
		Iterator entries = sistemaProseguimento.entrySet().iterator();
		while (entries.hasNext()) {
		  Entry thisEntry = (Entry) entries.next();
		  String key = (String) thisEntry.getKey();
		  //String sx = key.substring(0,h-1);		
		  String sx = key.substring(key.length()-(h-1));		 
		   
		 List<String> list = (List<String>) thisEntry.getValue();
		 List<String> dxlist = new ArrayList<String>();
		 for(String s: list){
			 String dx = sx + s;
			 //verifica che la parola sia valida confrontandola con le var.indip
			 boolean is_valid = false;
			 Iterator entries2 = sistemaProseguimento.entrySet().iterator();
			 while (entries2.hasNext()) {
				Entry thisEntry2 = (Entry) entries2.next();
				String key2 = (String) thisEntry2.getKey();
				if(key2.equalsIgnoreCase(dx)){
					is_valid = true;
					break;
				}
			 }
			 if(is_valid){
				dxlist.add(dx);
			 }			 
		 }
		 
		 hash.put(key, dxlist);	 
		 
		}
		
		return hash;
	}
	
	public HashMap <String,List<String>> ottieneSistemaLineareLinguaggioCircuiti(HashMap <String,List<String>> sistemaProseguimento, Graph g) {
		int h = g.findMaxPotenzaCamminoOrizzontale();
		
		logger.info("Sistema lineare");
		
		HashMap <String,List<String>> hash = new HashMap<String,List<String>>();
		
		if(h==1){
			Iterator entries = sistemaProseguimento.entrySet().iterator();
			while (entries.hasNext()) {
			  Entry thisEntry = (Entry) entries.next();
			  String key = (String) thisEntry.getKey();
			  //String sx = key.substring(0,h-1);		
			  String sx = key.substring(key.length()-(h-1));		 
			   
			 List<String> list = (List<String>) thisEntry.getValue();
			 List<String> dxlist = new ArrayList<String>();
			 for(String s: list){
				 String dx = sx + s;
				 //verifica che la parola sia valida confrontandola con le var.indip
				 boolean is_valid = false;
				 Iterator entries2 = sistemaProseguimento.entrySet().iterator();
				 while (entries2.hasNext()) {
					Entry thisEntry2 = (Entry) entries2.next();
					String key2 = (String) thisEntry2.getKey();
					if(dx.toLowerCase().startsWith(key2.toLowerCase())){
						is_valid = true;
						break;
					}
				 }
				 if(is_valid){
					dxlist.add(dx);
				 }			 
			 }
			 
			 hash.put(key, dxlist);	 
			 
			}
		}
		else{
			Iterator entries = sistemaProseguimento.entrySet().iterator();
			while (entries.hasNext()) {
			  Entry thisEntry = (Entry) entries.next();
			  String key = (String) thisEntry.getKey();
			  //String sx = key.substring(0,h-1);
			  String sx = "";
			  if(!key.contains("_") && key.length()==2){
				  sx = key.substring(key.length()-(h-1));
			  }
//			  else {
//				  sx = key.substring(key.length()-(h-1));
//			  }
			   
			 List<String> list = (List<String>) thisEntry.getValue();
			 List<String> dxlist = new ArrayList<String>();
			 for(String s: list){
				 String dx = sx + s;
				 //verifica che la parola sia valida confrontandola con le var.indip
				 boolean is_valid = false;
				 Iterator entries2 = sistemaProseguimento.entrySet().iterator();
				 while (entries2.hasNext()) {
					Entry thisEntry2 = (Entry) entries2.next();
					String key2 = (String) thisEntry2.getKey();
					if(dx.toLowerCase().startsWith(key2.toLowerCase())){
						is_valid = true;
						break;
					}
				 }
				 if(is_valid){
					dxlist.add(dx);
				 }			 
			 }
			 
			 hash.put(key, dxlist);	 
			 
			}
		}
		
		return hash;
	}
	
//	private Bacchetta findBacchettaFromAlfabeto(String lettera,List<Bacchetta> alfabeto){
//		Bacchetta x = null;
//		for(Bacchetta b : alfabeto){
//			if(lettera.equalsIgnoreCase(b.getLettera())){
//				x = b;
//			}
//		}
//		return x;
//	}
	
	public String ottieneFgoGrammatica(HashMap<String, List<String>> hash) {
		logger.info("ottiene fgo della grammatica");
		
		String mathscript = creaSistemaLineareGrammatica(hash);
		String result = risolviSistemaLineareGrammatica(mathscript);
		String fgo = ottieniFunzioneGenGrammatica(result);
		
//		String coeffList = ottieniEspansioneInSerieFunzione(fgo);
				
		return fgo;		
	}

	public String ottieniEspansioneInSerieFunzione(String fgo, String var) {
		logger.info("ottiene espansione in serie della fgo");
		KernelLink ml = singleton.getKernelLink();
		String series = "Series["+fgo+", {"+var+", 0, 9}]";
		String coeffList = "CoefficientList["+series+", "+var+"]";
		String strResult = ml.evaluateToInputForm(coeffList, 0);
		logger.info(strResult);
		return strResult;
	}

	public String ottieniFunzioneGenGrammatica(String result) {
		String math = "f = "+this.letteraFGO+" /. u";
		logger.info(math);
		KernelLink ml = singleton.getKernelLink();
		String fgo = ml.evaluateToInputForm(math, 0) ;
		logger.info("fgo grammatica = " + fgo);
		fgo = ml.evaluateToInputForm("Together[Simplify["+fgo+"]]", 0) ;
		fgo = fgo.replaceAll("\\*", "");
		logger.info("fgo grammatica (semplified)= " + fgo);
		return fgo;
	}
	
	public String ottieniFunzioneGenGrammaticaCircuiti(String result) {
		String math = "f = s /. u";
		logger.info(math);
		KernelLink ml = singleton.getKernelLink();
		String fgo = ml.evaluateToInputForm(math, 0) ;
		logger.info("fgo grammatica = " + fgo);
		fgo = ml.evaluateToInputForm("Together[Simplify["+fgo+"]]", 0) ;
		fgo = fgo.replaceAll("\\*", "");
		logger.info("fgo grammatica (semplified)= " + fgo);
		return fgo;
	}

	public String risolviSistemaLineareGrammatica(String math) {
		KernelLink ml = singleton.getKernelLink();
		String result = ml.evaluateToInputForm(math, 0) ;
		logger.info(result);
		return result;
	}

	@SuppressWarnings("unchecked")
	public String creaSistemaLineareGrammatica(HashMap<String, List<String>> hash) {
		// 
		
		String lettere = "";
		String math = "eqs = { ";
		Iterator entries = hash.entrySet().iterator();
		int i = 0;
		int k=1;
		
		int max = 0;
		while ( entries.hasNext()) {
		    Map.Entry entry = (Map.Entry) entries.next();
		    String key = (String)entry.getKey();
		    key = key.toLowerCase();
		    //
		    if("u".equalsIgnoreCase(key)){
		    	key = "j";
		    }
		    //
		    lettere += key;
		    List<String> values = (ArrayList<String>) entry.getValue();
		    
		    if(values.size()>max){
		    	max = values.size();
		    	
		    }
		    
		    math += key + " == ";
		    for(String s : values){
		    	s = s.toLowerCase();
		    	//
		    	if("u".equalsIgnoreCase(s)){
		    		s = "j";
		    	}
		    	//
		    	math += " x * " + s + " + ";
		    }
		    math += " 1 ";
		    if(++i < hash.size()){
		    	math += " , ";
		    	lettere += " , ";
		    }
		    k++;
		}
		math += " }; ";
		math += "u = FullSimplify@First@Solve[eqs,{"+lettere+"}, Reals] "; // // Column // TraditionalForm
		logger.info(math);
		return math;
	}
	
	@SuppressWarnings("unchecked")
	public String creaSistemaLineareGrammaticaCircuiti(HashMap<String, List<String>> hash) {
		// 
		
		String lettere = "";
		String math = "eqs = { ";
		Iterator entries = hash.entrySet().iterator();
		int i = 0;
		int k=1;
		
		int max = 0;
		while ( entries.hasNext()) {
		    Map.Entry entry = (Map.Entry) entries.next();
		    String key = (String)entry.getKey();
		    key = key.toLowerCase();
		    //
		    if("u".equalsIgnoreCase(key)){
		    	key = "j";
		    }
		    //
		    lettere += key;
		    List<String> values = (ArrayList<String>) entry.getValue();
		    
		    if(values.size()>max){
		    	max = values.size();
		    	
		    }
		    
		    math += key + " == ";
		    
		    int count1=0;
		    for(String s : values){
		    	s = s.toLowerCase();
		    	//
		    	if("u".equalsIgnoreCase(s)){
		    		s = "j";
		    	}
		    	//
		    	math += " x * " + s + " ";
		    	
		    	if(count1<values.size()-1)
		    		math += " + ";
		    		
		    	count1++;
		    }
		    
		    
		    boolean isStatoNonFinale = false;
			if(key.contains("_")){
		    	try{
		    		int numero = Integer.parseInt(key.substring(key.length()-1));
		    		isStatoNonFinale = true;
		    	}
		    	catch(NumberFormatException e){
		    		System.out.println("errore nel controllo stato non finale");
		    	}
			}
			if(!isStatoNonFinale)
				math += " + 1 ";
//			else
//				math += " ";
			
		    
		    if(++i < hash.size()){
		    	math += " , ";
		    	lettere += " , ";
		    }
		    k++;
		}
		math += " }; ";
		math += "u = FullSimplify@First@Solve[eqs,{"+lettere+"}, Reals] "; // // Column // TraditionalForm
		logger.info(math);
		return math;
	}

	public GrammarLanguage ottieneParoleConsentite(List<Bacchetta> alfabeto, Graph g) throws GraphException, GrammarException {

		logger.info("ottiene parole del linguaggio");
		
//		if(g.isCircuit()){
//			throw new GrammarException("Impossibile calcolare linguaggio di un circuito.");
//		}
		
		GrammarLanguage paroleLinguaggio = new GrammarLanguage();
		int h = g.findMaxPotenzaCamminoOrizzontale();
		
		this.letteraFGO = "";
		for(int i=0;i<h;i++){
			this.letteraFGO += "e";
		}
//		if(g.isCircuit())
//			h = 1;
		
		//logger.info(g);
		//g.initAndPrintAdjacencyMatrix4Wolfram();
		
		Bacchetta b2 = alfabeto.get(0);
		//lascia un margine alla prima riga
		int rows = b2.getNodiScelti().length+1;
		//lascia un margine alla prima colonna
		int cols = h+2;
		
		List<String[][]> paroleConsentite = new ArrayList<String [][]>();
		
		String m [][] = new String[rows][cols];
		for(int i=0;i<m.length;i++){
			for(int j=0;j<m[i].length;j++)
				m[i][j]="#";
		}
		
		HashMap <String,List<String>> sistemaProseguimento = new HashMap<String,List<String>>();
		recursion(sistemaProseguimento,paroleConsentite,alfabeto,m,0,h,g);
		
		paroleLinguaggio.setSistemaProseguimento(sistemaProseguimento);
		
		
		logger.info("****** parole consentite **********");
		for(String [][] s : paroleConsentite){
			Utils.print(s);
		}
		logger.info("****************");
		
		paroleLinguaggio.setParoleConsentite(paroleConsentite);
		
		return paroleLinguaggio;
	}

	private void recursion(HashMap <String,List<String>> hash,List<String[][]> paroleConsentite,List<Bacchetta> alfabeto,String m [][],int j,int h, Graph g){
		
		for(Bacchetta b2 : alfabeto){
			m=valorizza(b2,m,j);
			if(j<h){
				j++;
				recursion(hash,paroleConsentite,alfabeto,m,j,h,g);	
				j--;
			}
			else{
				//logger.info("step "+j+"..");
				//Utils.print(m);
//				String [][] parola = new String[m.length][m[0].length];
//				for(int aa=0; aa<m.length; aa++){
//					for(int bb=0; bb<m[aa].length; bb++){
//						parola[aa][bb] = m[aa][bb];
//					}
//				}
				//
				if(isNonAdjacentVertexSet(m,g)){
					//logger.info("Consentita!");
					String [][] parola = new String[m.length][m[0].length];
					String letteraProseguimento = "";
					String parolasx = "";
					for(int aa=0; aa<m.length; aa++){
						for(int bb=0; bb<m[aa].length; bb++){
							parola[aa][bb] = m[aa][bb];
							if(aa==0){
								if(bb==m[aa].length-1){
									letteraProseguimento = m[aa][bb];
								}
								else if(bb>0){
									parolasx += m[aa][bb];
								}
							}							
						}
					}
					paroleConsentite.add(parola);
					
					List<String> t = hash.get(parolasx);
					if(t==null){
						t = new ArrayList<String>();
					}
					t.add(letteraProseguimento);
					hash.put(parolasx, t);
					
				}
//				else{
//					logger.info("NON Consentita!");
//					paroleNonConsentite.add(parola);
//				}
				
			}
			//logger.info("passa alla bacchetta successiva!");
			
		}
		
	}
	
	private boolean isNonAdjacentVertexSet(String[][] m, Graph g) {
		
		HashMap<String,Vertex> vertex = g.getVertex();
		boolean accepted = true;
		
		//nb. la prima riga corrisponde alla lettera del linguaggio
		for(int i=1;i<m.length;i++){
			for(int j=1; j<m[i].length; j++){
				
				//controllo su tutti gli altri vertici che hanno "1"
				if("1".equals(m[i][j])){
					
					//recupero corrispondente numero di vertice
					String key = i+"."+j;
					
					//TODO potrebbe verificarsi una eccezione nel caso in cui n non sia abbastanza grande da contenere 
					//il primo passo massimo del cammino orizzontale 
					int v = vertex.get(key).getN();
					
					for(int a=1;a<m.length;a++){
						for(int b=1; b<m[a].length; b++){							
							String key2 = a+"."+b;
							
							//se trovo due nodi diversi che hanno 1..							
							if("1".equals(m[a][b]) && !key.equals(key2)){
							
								//recupero corrispondente numero di vertice	del secondo nodo scelto							
								int w = vertex.get(key2).getN();									
								//non devo controllare cappi! (lo stesso nodo)								
								Integer res = g.contains(v, w);								
								//logger.info("effettuo verifica adiacenza tra nodi ["+v+"] e ["+w+"], res = "+res);
								if(res.intValue()==1){
									accepted = false;
								}
								
							}	
						}
					}
				}
			}
		}
		
		return accepted;
	}
	
	private String [][] valorizza(Bacchetta b2,String m [][],int j){
		//valorizza i nodi della bacchetta nella matrice
		int [] nodiB2 = b2.getNodiScelti();	
		m[0][j+1] = b2.getLettera();
		for(int i=0;i<nodiB2.length;i++){
			m[i+1][j+1] = String.valueOf(nodiB2[i]);
		}
		
		return m;
	}
	
	
	public List<Bacchetta> ottieneLettereAlfabeto(Graph g) throws GraphException, GrammarException {
		
		logger.info("ottiene lettere alfabeto");
		
//		if(g.isCircuit()){
//			throw new GrammarException("Impossibile calcolare linguaggio di un circuito.");
//		}
		
		String alphabet = Constants.LETTERE_LINGUAGGIO;
		List<HashMap<Integer, List<Integer>>> bacchetteList = new ArrayList<HashMap<Integer, List<Integer>>>();
		int M = g.getM();
		int[] n = new int[M];
		int Nr[] = new int[M];
		for (int i = 0; i < Nr.length; i++) {
			Nr[i] = 1;
		}
		printPermutations(n, Nr, 0, bacchetteList, g);
		Utils.stampaMatrice(bacchetteList);
		
		List<Bacchetta> alfabeto = new ArrayList<Bacchetta>();
		
		int k = 0;
		for (HashMap <Integer,List<Integer>> a : bacchetteList) 
		{	
		    List<Integer> list = a.get(k);
		    Bacchetta bb = new Bacchetta();
		    int [] nodiscelti = new int[list.size()];
		    
		    int c=0;
		    for(Integer indice : list)
		    {
		    	nodiscelti[c++]=indice;
		    }
		    bb.setNodiScelti(nodiscelti);
			k++;
			
			//imposta le lettere della "convenzione" se possibile, oppure altre lettere
			String lettera = checkLetteraConvenzionale(list);
			if("".equals(lettera)){
				Random r = new Random();
				int ind = r.nextInt(alphabet.length());
				lettera = String.valueOf(alphabet.charAt(ind));
				alphabet=alphabet.replace(lettera,"");
			}
			
			bb.setLettera(lettera);
			bb.setIndexTM(k-1);
			alfabeto.add(bb);
		}
		
		for(Bacchetta b1 : alfabeto){
			logger.info(b1);
		}
		
		return alfabeto;
	}

	private String checkLetteraConvenzionale(List<Integer> list) {
		String lettera = "";
		int num_zeri = 0;
		for(Integer i : list){
			if(i.intValue()==0) num_zeri ++;
		}
		if(num_zeri==list.size()) lettera = "E";
		if(num_zeri==list.size()-1 && list.get(0).intValue()==1) lettera = "D";
		if(num_zeri==list.size()-1 && list.get(list.size()-1).intValue()==1) lettera = "U";
		return lettera;
	}

	private void printPermutations(int[] n, int[] Nr, int idx, List<HashMap<Integer, List<Integer>>> bacchetteList, Graph g) {
		if (idx == n.length) { // stop condition for the recursion [base clause]

			HashMap<Integer, List<Integer>> hash = new HashMap<Integer, List<Integer>>();
			List<Integer> list = new ArrayList<Integer>();
			boolean uniConsec = false;
			
			for (int i = 0; i < n.length; i++) {
				list.add(n[i]);
				if (n[i] == 1 && i < n.length - 1 && n[i + 1] == 1) {
					uniConsec = true;
				}
				
			}
			
			boolean adjacent = false;
			if(!uniConsec){
				for(int i=0; i<n.length;i++){
					for(int j=0;j<n.length;j++){
						if(i!=j && n[i]==1 && n[j]==1){ //non devo controllare cappi! (lo stesso nodo)															
							Integer res = g.contains(i, j);								
							//logger.info("effettuo verifica adiacenza tra nodi ["+v+"] e ["+w+"], res = "+res);
							int cammino = Math.abs(i-j);
							if(res.intValue()==1 && cammino>1){
								adjacent = true;
							}
						}
					}
				}
			}
			
			
			if (!uniConsec && !adjacent) {
				int k = bacchetteList.size();
				hash.put(k, list);
				bacchetteList.add(hash);
			}

			// logger.info(Arrays.toString(n));
			return;
		}
		for (int i = 0; i <= Nr[idx]; i++) {
			n[idx] = i;
			printPermutations(n, Nr, idx + 1, bacchetteList, g); // recursive invokation, for next elements
		}
	}

//	public HashMap<String, List<String>> ottieneSistemaLineareLinguaggioCircuiti(
//			HashMap<String, List<String>> sistemaProseguimento) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	
	public void creaSistemaProseguimentoCircuiti(HashMap<String, List<String>> sistemaProseguimento, List<Bacchetta> alfabeto, Graph g){
		int h = g.findMaxPotenzaCamminoOrizzontale();
		String parolaDiAggancioTraSistemi = "e";
		
    	//definisco lo stato iniziale S e da esso le possibili transizioni 
    	//(la transizione con 'e' manderà all'automa del caso non-circuito)
    	List<String> newlist = new ArrayList<String>();
    	for (Bacchetta bacchetta : alfabeto) {
    		String name = bacchetta.getLettera();
    		if(!"e".equalsIgnoreCase(name))
    			name += "_i";
    		else if(h>1){
    			for(int i=1;i<h;i++){
    				name += bacchetta.getLettera();
    			}
    			parolaDiAggancioTraSistemi = name;
    		}
    		newlist.add(name);
		}
    	sistemaProseguimento.put("S", newlist);
    	
    	//Per ciascuna transizione diversa da 'e' (o nel caso h>1, diversa da 'ee..' --> parolaDiAggancioTraSistemi), che esce dallo stato S ..
    	List<HashMap> listMap = new ArrayList<HashMap>();
    	
    	for(String element : newlist){
    		
    		if(!parolaDiAggancioTraSistemi.equalsIgnoreCase(element.toLowerCase())){ 
    			
    			creaClusterAutoma(alfabeto, element, listMap, g);
    		}
    	}
    	
    	//aggiungo alla mappa originale i nuovi elementi trovati (stati e transizioni)
    	for (HashMap hash : listMap) {
    		Iterator entries = hash.entrySet().iterator();
    		while (entries.hasNext()) {
    		  Entry thisEntry = (Entry) entries.next();
    		  String key = (String) thisEntry.getKey();
    		  
    		  List<String> list = (List<String>) thisEntry.getValue();
    		  sistemaProseguimento.put(key, list);
    		}
		}
	}

	private void creaClusterAutoma(List<Bacchetta> alfabeto, String element, List<HashMap> listMap, Graph g) {
		int i=0;
		int h = g.findMaxPotenzaCamminoOrizzontale();
		String alphabet = Constants.LETTERE_LINGUAGGIO;
		
		HashMap<String,List<String>> map1 = new HashMap<String,List<String>>();
		List<String> newElementlist = new ArrayList<String>();
		
		//cicla tutte le lettere dell'alfabeto..
		for(Bacchetta bacchetta : alfabeto){
			HashMap<String,List<String>> map2 = new HashMap<String,List<String>>();
			
			if(element.toLowerCase().equalsIgnoreCase(bacchetta.getLettera()+"_i")){
				//considera solo la lettera uguale a quella uscente dallo stato S
				i++;
				
//				for(int q = 1; q<=h; q++)
//				{
					String result[]=new String[2];
					List<String> temp = new ArrayList<String>();
					
					//in questo caso creo primo nodo E_{pedice_random} 
					result = costruisciStatoE(alphabet); alphabet = result[0];
					String nodoE_k = result[1]; //Es. E_k [ho creato un nodo E_{pedice_random}]
					newElementlist.add(nodoE_k); //Esempio: U_i -> e E_k [Ho collegato il nodo (current) al nuovo nodo E_{pedice_random}] 
					//--
					
					if(h==1){
						
						temp.add(nodoE_k); //Es. E_k -> e E_k [ho creato un cappio]
						
						int beginIndex = element.indexOf("_");
						String elemento_vietato = element.substring(0,beginIndex);
						temp.add(elemento_vietato+"_"+i); //Es. E_k -> e E_k | u U_f [aggiungo transizione verso lo stato non finale]
						
						map2.put(nodoE_k,temp); //collego la lista delle transizioni di E_k
						
						temp = new ArrayList<String>();
						temp.add(nodoE_k);
						map2.put(elemento_vietato+"_"+i, temp); //Es. U_f -> e E_k [collega lo stato non terminale con il precedente tramite la transizione 'e']
					}
					else if(h==2){
						
						result = costruisciStatoE(alphabet); alphabet = result[0];
						String nodoE_j = result[1]; //Es. E_j [ho creato un nodo E_{pedice_random}]
						temp.add(nodoE_j); //Es. E_k -> e E_j
						map2.put(nodoE_k,temp);
						//--
						
						temp = new ArrayList<String>();
						temp.add(nodoE_j); //Es. E_j -> e E_j [ho creato un cappio]
						
						int beginIndex = element.indexOf("_");
						String elemento_vietato = element.substring(0,beginIndex);
						temp.add(elemento_vietato+"_"+i); //Es. E_j -> e E_j | u U_f [aggiungo transizione verso lo stato non finale]
						map2.put(nodoE_j,temp); //collego la lista delle transizioni di E_j
						
						temp = new ArrayList<String>();
						temp.add(nodoE_j);
						map2.put(elemento_vietato+"_"+i, temp); //Es. U_f -> e E_j [collega lo stato non terminale con il precedente tramite la transizione 'e']
					}
					
//					//questo è il caso in cui h>1
//					if(h>2) {
//						String result[] = costruisciStatoE(alphabet);
//						alphabet = result[0];
//						nodoE = result[1];
//						List<String> temp = new ArrayList<String>();
//					}
//				}
				
				
				
				
			}
			//TODO fai solo se la lettera è DIVERSA a quella che esce dallo stato S
//			else{
//				i++;
//				newElementlist.add(lettera+"_k");
//				List<String> temp = new ArrayList<String>();
//				temp.add(lettera+"_k");
//				
//				//aggiungo transizione verso lo stato non finale ..
//				int beginIndex = element.indexOf("_");
//				String elemento_vietato = element.substring(0,beginIndex);
//				temp.add(elemento_vietato+"_"+i);
//				
//				map2.put(lettera+"_k",temp);
//			}
			
			listMap.add(map2);
		}
		map1.put(element,newElementlist);
		listMap.add(map1);
	
	}

	/**
	 * Costruisce un nuovo stato dell'automa di tipo E_{pedice_random}
	 * Poi aggiorna l'elenco delle lettere utilizzabili per "pedice_random"
	 */
	private String [] costruisciStatoE(String alphabet) {
		
		String [] result = new String[2];
		Random r = new Random();
		int ind = r.nextInt(alphabet.length());
		String pediceScelto = String.valueOf(alphabet.charAt(ind));
		alphabet=alphabet.replace(pediceScelto,"");
		pediceScelto = pediceScelto.toLowerCase();
		result[0] = alphabet;
		result[1] = "E_"+pediceScelto;
		return result;
	}
}
