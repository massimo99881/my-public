package it.unimi.isa.utils;


import it.unimi.isa.beans.IndepMatrixBean;
import it.unimi.isa.exceptions.GraphException;
import it.unimi.isa.model.SchemaObj;
import it.unimi.isa.model.Vertex;
import it.unimi.isa.model.graphs.Graph;
import it.unimi.isa.model.graphs.GraphFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class Utils {

	private static final Logger logger = Logger.getLogger(Utils.class);
	
	public static String stampaSchemaSalvati(List<SchemaObj> savedSchema){
		String result = "";
		int i=1;
		for(SchemaObj o: savedSchema){
			System.out.println("Schema n."+i+", occorrenze: "+o.getCount());
			print(o.getSchema());
			i++;
		}
		return result;
	}
	
	public static String[] getArrayFromString(String strResult) {
		//List<String> list = new ArrayList<String>(Arrays.asList(strResult.split(" , ")));
        strResult = strResult.substring(1, strResult.length()-1);
        String strArray[] = strResult.split(",");
        return strArray;
	}
	
	public static boolean matrixequals (int[][] m1, int[][] m2)
    {
       if (m1.length != m2.length) return false;
       for (int i = 0; i < m1.length; i++) {
         if (m1[i].length != m2[i].length) return false;
         for (int j = 0; j < m1[i].length; j++) {
           int b1 = m1[i][j];
           int b2 = m2[i][j];
           if (b1 != b2) return false;
         }
      }
      return true;
    }
	
	public static long[] getLongArrayFromString(String coeffList) {
		String coeffListArray [] = getArrayFromString(coeffList);
		
		long array[] = new long[coeffListArray.length];
		
		for(int i=0;i<array.length;i++){
			//System.out.println(coeffListArray[i]);
			array[i] = Long.parseLong(coeffListArray[i].trim());
		}
		
		return array;
	}
	
	public static int[] getIntArrayFromString(String coeffList) {
		String coeffListArray [] = getArrayFromString(coeffList);
		
		int array[] = new int[coeffListArray.length];
		
		for(int i=0;i<array.length;i++){
			//System.out.println(coeffListArray[i]);
			array[i] = Integer.parseInt(coeffListArray[i].trim());
		}
		
		return array;
	}
	
	public static long[] getLongAbsArrayFromString(String coeffList) {
		String coeffListArray [] = getArrayFromString(coeffList);
		
		long array[] = new long[coeffListArray.length];
		
		for(int i=0;i<array.length;i++){
			//System.out.println(coeffListArray[i]);
			array[i] = Math.abs(Long.parseLong(coeffListArray[i].trim()));
		}
		
		return array;
	}
	
	/**
	 * Utility print Integer matrix
	 * @param M
	 */
	public static String print(Integer[][] M){
		String result = "\n";
		for (int i = 0; i < M.length; i++) {
		    for (int j = 0; j < M[i].length; j++) {
		    	if(M[i][j]!=null){
		    		if(M[i][j].toString().length()==1)
		    			result += "    " + M[i][j];
		    		else if(M[i][j].toString().length()==2)
		    			result += "   " + M[i][j];
		    		else if(M[i][j].toString().length()==3)
		    			result += "  " + M[i][j];
		    		else if(M[i][j].toString().length()==4)
		    			result += " " + M[i][j];
		    		else
		    			result += " "+ M[i][j];
		    		
		    	}
		    }
		    result += "\n";
		}
		System.out.println(result);
		logger.info(result);
		return result;
	}
	
	/**
	 * check null or empty values
	 * @param field
	 * @return
	 */
	public static boolean isEmptyOrNull(String field){
		if(field==null || field.trim().length()==0)
			return true;
		return false;
	}
	
	public static void printArray(String[] a){
		String result = "";
		for(int i=0; i<a.length; i++){
			result += "d["+i+"]:= "+a[i]+" ; ";
		}
		logger.info(result);
	}

	@SuppressWarnings("unchecked")
	public void print(HashMap<String, Vertex> vertex) {
		String result = "\n ";
		Iterator entries = vertex.entrySet().iterator();
		while (entries.hasNext()) {
		  Entry thisEntry = (Entry) entries.next();
		  String key = (String) thisEntry.getKey();
		  result += key;
		  Vertex value = (Vertex) thisEntry.getValue();
		  result += " :: n="+value.getN() + " \n ";
		  // ...
		}
		logger.info(result);
	}
	
	@SuppressWarnings("unchecked")
	public static void printHash(HashMap <String,List<String>> hash) {
		String result = "\n";
		Iterator entries = hash.entrySet().iterator();
		while (entries.hasNext()) {
		  Entry thisEntry = (Entry) entries.next();
		  String key = (String) thisEntry.getKey();
		   result += key + "=";
		 List<String> list = (List<String>) thisEntry.getValue();
		 for(String s: list){
			 result += s + ",";
		 }
		 result += "\n";
		}
		logger.info(result);
	}

	public static String stampaMatrice(List<HashMap <Integer,List<Integer>>> listIndip) {
		String output = "";
		output += "\n";
		//output += "[stampaMatriceIndipendenza]";
		int n = 0;
		for (HashMap <Integer,List<Integer>> a : listIndip) 
		{
			output += n+"|";			
		    List<Integer> list = a.get(n);
		    for(Integer j : list)
		    {
		    	if ((j + "").length() >= 3)
		    		output += " " + j;
		    	else if ((j + "").length() == 2)
					output += "  " + j;
		    	else if ((j + "").length() < 2)
					output += "   " + j;
		    	else
		    		output += j;
		    }
		    output += "\n";
			n++;
		}
		
		logger.info(output);
		return output;
	}
	
	public static String stampaMatriceMath(List<HashMap <Integer,List<Integer>>> listIndip) {
		
		Integer [][]M = convertIndepHashMapListToMatrix(listIndip);
		String result = ""; //"{";
		for (int i = 0; i < M.length; i++) {
			result += "{";
		    for (int j = 0; j < M[i].length; j++) {
		    	if(M[i][j]!=null){
		    		if(M[i][j].toString().length()==1)
		    			result += "   " + M[i][j];
		    		else if(M[i][j].toString().length()==2)
		    			result += "  " + M[i][j];
		    		else if(M[i][j].toString().length()==3)
		    			result += " " + M[i][j];
		    		else
		    			result += M[i][j];
		    	}
		    	else{
		    		result += "   0";
		    	}
		    	
		    	if(j<M[i].length-1)
	    			result += ",";
		    }
		    result += "}"; //"\t}";
//		    if(i<M.length-1){
//		    	result += ",";
//		    }
		    result += "\n";
		}
//		result += "}";
		return result;
	}
	
	public static String stampaMatriceIGraph(List<HashMap <Integer,List<Integer>>> listIndip, int n) {
		String output = "";
		
		//cicla sempre e solo una volta
		
		for (HashMap <Integer,List<Integer>> a : listIndip) 
		{
			output += n+"|";			
		    List<Integer> list = a.get(0);
		    for(Integer j : list)
		    {
		    	if ((j + "").length() >= 3)
		    		output += " " + j;
		    	else if ((j + "").length() == 2)
					output += "  " + j;
		    	else if ((j + "").length() < 2)
					output += "   " + j;
		    	else 
		    		output += j;
		    }
		    output += "\n";
		}
		
		logger.info(output);
		return output;
	}
	
	public static void print(String[][] M){
		String result = "\n";
		for (int i = 0; i < M.length; i++) {
		    for (int j = 0; j < M[i].length; j++) {
		    	result += " :: " + M[i][j];
		    			    		
		    }
		    result += "\n";
		}
		logger.info(result);
	}
	
	public void print(String[][] M, boolean init){
		String result = "\n";
		for (int i = 0; i < M.length; i++) {
		    for (int j = 0; j < M[i].length; j++) {
		    	if(init && isEmptyOrNull(M[i][j])){
		    		M[i][j] = "@";
		    	}
		    	result += " :: " + M[i][j];
		    			    		
		    }
		    result += "\n";
		}
		logger.info(result);
	}
	
	public void init(String[][] M){
		String result = "\n";
		for (int i = 0; i < M.length; i++) {
		    for (int j = 0; j < M[i].length; j++) {
		    	M[i][j] = "@";
		    	result += " :: " + M[i][j];
		    			    		
		    }
		    result += "\n";
		}
		//logger.info(result);
	}
	
	public static String print(int[][] M){
		String result = "\n";
		for (int i = 0; i < M.length; i++) {
		    for (int j = 0; j < M[i].length; j++) {
		    	result += " :: " + M[i][j];
		    			    		
		    }
		    result += "\n";
		}
//		System.out.println(result);
		logger.info(result);
		return result;
	}

	public static String getStringFromIntegerArray(List<Integer> list) {
		String result = "{";
		int j = 0;
		int dim = list.size();
		for (Integer i : list){
			result += " "+ i ; 
			j++;
			if(j!=dim){
				result += ",";
			}
		}
		result += "}";
		return result;
	}
	
//	public static int findSubArr(int[] arr,int[] subarr)
//	{
//	    int lim=arr.length-subarr.length;
//
//	    for(int i=0;i<=lim;i++)
//	    {
//	        int[] tmpArr=Arrays.copyOfRange(arr,i,i+subarr.length);
//	        if(Arrays.equals(tmpArr,subarr))
//	            return i;   //returns starting index of sub array
//	    }
//	    return -1;//return -1 on finding no sub-array   
//	}

	public static boolean isPartOfExpansion(String expansion, String part) {
		String partCoeff [] = getArrayFromString(part);
		String partCoeffMerged = "";
		String expansionCoeff [] = getArrayFromString(expansion);
		String expansionCoeffMerged = "";
		for(int i=0;i<partCoeff.length;i++){
			partCoeffMerged += partCoeff[i].trim()+",";
		}
		for(int i=0;i<expansionCoeff.length;i++){
			expansionCoeffMerged += expansionCoeff[i].trim()+",";
		}
		return expansionCoeffMerged.contains(partCoeffMerged);
	}
	
	@SuppressWarnings("unchecked")
	public static List reverseList(List myList) {
	    List invertedList = new ArrayList();
	    for (int i = myList.size() - 1; i >= 0; i--) {
	        invertedList.add(myList.get(i));
	    }
	    return invertedList;
	}

	public static Integer[][] convertIndepHashMapListToMatrix(List<HashMap<Integer, List<Integer>>> listIndip) {
		int maxCols = 0;
		int c = 0;
		
		for (HashMap <Integer,List<Integer>> a : listIndip) 
		{
		    List<Integer> list = a.get(c);
		    int dim  = list.size();
		    if(dim>maxCols){
		    	maxCols = dim;
		    }
		    c++;
		}
		
		Integer[][] indip = new Integer[listIndip.size()][maxCols+1];
		
		c = 0;
		for (HashMap <Integer,List<Integer>> a : listIndip) 
		{
		    List<Integer> list = a.get(c);
		    int j = 0;
		    for(int v=0;v<maxCols+1;v++){
		    	Integer s = 0;
		    	if(v<list.size()){
		    		s = list.get(v);
		    	}		    	
		    	indip[c][j] = s;
		    	j++;
		    }
		    c++;
		}
		return indip;
	}
	
	/**
	 * Returns a pseudo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public int randInt(int min, int max) {

	    // NOTE: This will (intentionally) not run as written so that folks
	    // copy-pasting have to think about how to initialize their
	    // Random instance.  Initialization of the Random instance is outside
	    // the main scope of the question, but some decent options are to have
	    // a field that is initialized once and then re-used as needed or to
	    // use ThreadLocalRandom (if using at least Java 1.7).
	    //Random rand;
		Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}

	public static Integer[][] copyMatrix(Integer[][] matrix) {
		int dim = matrix.length;
		int dim2 = matrix[0].length;
		Integer[][] copy = new Integer [dim][dim2];
		for (int i = 0; i < dim; i++) {
		    for (int j = 0; j < dim2; j++) {
		    	if(matrix[i][j]==null){
		    		copy[i][j] = 0;
		    	}
		    	else{
		    		copy[i][j] = matrix[i][j];
		    	}
		    }
		}
		return copy;
	}

	public static void init(Integer[][] result) {
		for (int i = 0; i < result.length; i++) {
		    for (int j = 0; j < result[i].length; j++) {
		    	result[i][j] = 0;	
		    }
		}
	}

	public void print(int[] array) {
		String result = "\n";
		for(int i=0;i<array.length;i++){
			result += array[i] + ",";
		}
		logger.info(result);
	}
	
	public static Graph ottieneGrafo(String result[], int i1, int i2) throws GraphException {
		//String f = result[i1].substring(0,1);
		
		int n,m;
		Graph g = null;
		//CalcolaInsiemiIndipendenti tester = null;
		String P1 = result[i1];
		String P2 = result[i2];
		logger.info("p1:"+P1);
		logger.info("p2:"+P2);
		
		String ff = "";
		for(int i=0;i<result[i2].length();i++){
			if(!(String.valueOf(result[i2].charAt(i))).equals("_")){
				ff+=result[i2].charAt(i);
			}
			else{
				break;
			}
		}
		
		String[] p1 = P1.split("_");
		String[] p2 = P2.split("_");
		
		n = Integer.parseInt(p2[1]);
		m = Integer.parseInt(p1[1]);
						
		int [] potenzeCamminoVerticale = ottienePotenzaCammino(p1[2]);				
		int [] potenzeCamminoOrizzontale = ottienePotenzaCammino(p2[2]);
		g = GraphFactory.getGraph(n, m, ff, potenzeCamminoVerticale, potenzeCamminoOrizzontale);
        g.createIndipMatrix();	
		
//		if(f.equalsIgnoreCase("P") && ff.equalsIgnoreCase("P"))
//		{
//			String[] p1 = P1.split("_");
//			String[] p2 = P2.split("_");
//			
//			n = Integer.parseInt(p2[1]);
//			m = Integer.parseInt(p1[1]);
//							
//			int [] potenzeCamminoVerticale = ottienePotenzaCammino(p1[2]);				
//			int [] potenzeCamminoOrizzontale = ottienePotenzaCammino(p2[2]);
//			g = GraphFactory.getGraph(n, m, ff, potenzeCamminoVerticale, potenzeCamminoOrizzontale);
//	        g.createIndipMatrix();	
//	        
//	        
//		}
//		else if(!ff.equalsIgnoreCase("P"))
//		{
//			
//			String[] p1 = P1.split("_");
//			String[] p2 = P2.split("_");
//			
//			n = Integer.parseInt(p2[1]);
//			m = Integer.parseInt(p1[1]);
//			
//			//other (es. circuiti)		
//			int [] potenzeCamminoVerticale = ottienePotenzaCammino(p1[2]);				
//			int [] potenzeCamminoOrizzontale = ottienePotenzaCammino(p2[2]);
//			g = GraphFactory.getGraph(n, m, ff, potenzeCamminoVerticale, potenzeCamminoOrizzontale);
//			g.createIndipMatrix();			
//			
//		}  
		return g;
	}
	
	public static int[] ottienePotenzaCammino(String potenza) {
		int [] potenzaCammino = null;
		if(potenza.contains(Constants.CARATTERE_SPECIFICO_CAMMINO)){
			potenza = potenza.replace(Constants.CARATTERE_SPECIFICO_CAMMINO, "");
			potenzaCammino = new int[2];
			potenzaCammino[0] = 1;
			potenzaCammino[1] = Integer.parseInt(potenza);
		}
		else {
			int end = Integer.parseInt(potenza);
			potenzaCammino = new int[end]; 
			for(int i=1;i<=end;i++){
				potenzaCammino[i-1] = i;
			}
		}
		
		return potenzaCammino;
	}

	public static String print(long[] a) {
		String result = "";
		for(int i=0; i<a.length; i++){
			result += a[i]+", ";
		}
		logger.info(result);
		System.out.println(result);
		return result;
	}

	@SuppressWarnings("unchecked")
	public static void printHash2(HashMap<Integer, List<Integer>> hash) {
		String result = "\n";
		Iterator entries = hash.entrySet().iterator();
		while (entries.hasNext()) {
		  Entry thisEntry = (Entry) entries.next();
		  Integer key = (Integer) thisEntry.getKey();
		   result += key + "|";
		 List<Integer> list = (List<Integer>) thisEntry.getValue();
		 for(Integer j: list){
			if ((j + "").length() >= 3)
				 result += " " + j;
			else if ((j + "").length() == 2)
				result += "  " + j;
			else if ((j + "").length() < 2)
				result += "   " + j;
			else
				result += j;
			
		 }
		 result += "\n";
		}
		logger.info(result);
	}
	
	/**
	 * Stampa i nodi delle supergriglie
	 */
	public static String printNodes(int r, int q) 
	{
		
		int m = r;
		int n = q;
		
		String tmp = "$$\\scalefont{0.3}\\begin{tikzpicture}\n";
		
//		tmp += "\t\t [,>=stealth',shorten >=1pt,auto,node distance=2.cm,thick,main node/.style={circle,fill=blue!20,draw,font=\\sffamily},main node2/.style={circle,fill=green!20,draw,font=\\sffamily},main node3/.style={circle,fill=yellow!20,draw,font=\\sffamily}] \n";
		
//		tmp += "\t\t [,>=stealth',shorten >=1pt,auto,node distance=2.cm,thick,main node/.style={circle,draw,font=\\sffamily},main node2/.style={circle,fill=green!20,draw,font=\\sffamily},main node3/.style={circle,fill=yellow!20,draw,font=\\sffamily}] \n";
		
		tmp += "\t\t [,>=stealth',shorten >=1pt,auto,node distance=1.4cm,thick,main node/.style={circle,draw,font=\\sffamily\\small}] \n";
		
		int x = 1;
		
		for(int i=0;i<=n;i++)
		{
			if(i==0)
			{
				for (int c=1; c<=(m+1); c++, x++)
				{
					if(c==1)
					{
						//tmp += "\\node[main node] ("+x+") {"+x+"}; "; //* numerazione dall'alto verso il basso
						tmp += "\t\\node[main node] ("+x+") {"+(c+"."+(i+1))+"}; \n";
					}
					else 
					{
						//tmp += "\\node[main node] ("+x+") [below of="+(x-1)+"] {"+x+"}; "; //*
						tmp += "\t\\node[main node] ("+x+") [below of="+(x-1)+"] {"+(c+"."+(i+1))+"}; \n";
					}
				}	
			}
			else
			{
				for (int c=1; c<=(m+1); c++, x++)
				{					
					//tmp += "\\node[main node] ("+(x)+") [right of="+(x-(m+1))+"] {"+x+"}; "; //*
					tmp += "\t\\node[main node] ("+(x)+") [right of="+(x-(m+1))+"] {"+(c+"."+(i+1))+"}; \n";
				}
			}
		}
		
		return tmp;
	}
	
	public static boolean pulisciCartellaDaFileTemporanei() {
		boolean doneCorrectly = true;
		try {
			File folder = new File(Constants.PATH_TO_PDF);
			for (File f : folder.listFiles()) {
				//elimino i file con estensione .aux e .log dal direttorio principale
			    if (f.getName().endsWith(".aux") || f.getName().endsWith(".log")) {
			        f.delete(); // may fail mysteriously - returns boolean you may want to check
			    }
			    else if(f.getName().endsWith(".pdf")){
			    	f.renameTo(new File(Constants.PATH_TO_PDF+"\\resources\\pdf\\" + f.getName()));
			    }
			  //sposto i file pdf ottenuti nella cartella resources/pdf
			}
			//elimino cartella temporanea di dot2tex
			FileUtils.deleteDirectory(new File(Constants.PATH_TO_PDF+ "\\docgraphs"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			doneCorrectly = false;
		}
		return doneCorrectly;
	}
	
	public static IndepMatrixBean loadFromCache(int n, int m, String f, int[] potenzaCamminoVerticale, int[] potenzaCamminoOrizzontale, String nomeFileCache, String path) throws FileNotFoundException, UnsupportedEncodingException {
		
		IndepMatrixBean indepMatrixBean = new IndepMatrixBean();
		List<HashMap <Integer,List<Integer>>> listIndip = null;
		List<Integer> lastComputedKList = null;
		List<Integer> rowSumList = null;
		
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		
		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	  
		    	  String originalFilename = listOfFiles[i].getName();
		    	  int ind = originalFilename.indexOf(".");
		    	  String filename = originalFilename.substring(0,ind);
		    	  String result[] = filename.split(" ");
//		    	  String t = result[0].substring(0,1);
		    	  String P1 = result[0];
		  		  String P2 = result[2];
		  		  
		  		String ff = "";
				for(int ii=0;ii<result[2].length();ii++){
					if(!(String.valueOf(result[2].charAt(ii))).equals("_")){
						ff+=result[2].charAt(ii);
					}
					else{
						break;
					}
				}
				
				String[] p1 = P1.split("_");
				String[] p2 = P2.split("_");
				
				int n2 = Integer.parseInt(p2[1]);
				int m2 = Integer.parseInt(p1[1]);
								
				int [] potenzeCamminoVerticale = Utils.ottienePotenzaCammino(p1[2]);				
				int [] potenzeCamminoOrizzontale = Utils.ottienePotenzaCammino(p2[2]);
				
				boolean checkSameGraph = m==m2 && ff.equalsIgnoreCase(f) && Arrays.equals(potenzaCamminoVerticale, potenzeCamminoVerticale) && Arrays.equals(potenzaCamminoOrizzontale, potenzeCamminoOrizzontale);
				boolean checkCondition = n>n2 ;
				
				if(checkSameGraph && !checkCondition && path.equals(Constants.PATH_TO_CACHE_INDEP) && Constants.ENABLE_CACHE_LOAD)
				{
					System.out.println("loading from cache..");
					logger.info("loading from cache..");
					
			        Scanner file =null;
			        try 
			        {
			            file = new Scanner(new File(Constants.PATH_TO_CACHE_INDEP + originalFilename));
			        }
			        catch(FileNotFoundException e)
			        {
			            System.out.println("Could not open file " + filename);
			        }

			        listIndip = new ArrayList<HashMap<Integer,List<Integer>>>();
			        lastComputedKList = new ArrayList<Integer>();
			        rowSumList = new ArrayList<Integer>();
			        
			        int n_ = 0;
			        int sum_ = 0;
			        while ( file.hasNext() ){ 
			        	HashMap <Integer,List<Integer>> hash = new HashMap<Integer,List<Integer>>();
				        
			        	
			            String line = file.nextLine() ;
			            int[] array = Utils.getIntArrayFromString(line);
			            
							List<Integer> list = new ArrayList<Integer>();
							boolean foundZero = false;
							for(int b=0;!foundZero;b++){
								int numIndip = array[b];
								sum_ += numIndip;
								
								if(numIndip==0){
									foundZero = true;
									lastComputedKList.add(b-1);
									rowSumList.add(sum_);
									sum_ = 0;
								}
								else{
									list.add(array[b]);
								}
							}
							
						hash.put(n_,list);
						listIndip.add(hash);	
						n_++;
						
			        }
				}
				
		    	  
		      } else if (listOfFiles[i].isDirectory()) {
		        System.out.println("Directory " + listOfFiles[i].getName());
		      }
		    }
		    
		indepMatrixBean.setIndependentMatrix(listIndip);
		indepMatrixBean.setLastComputedKList(lastComputedKList);
		indepMatrixBean.setRowSumList(rowSumList);
		return indepMatrixBean;
	}
	
	public static void saveInCache(int n, int m, String f, int[] potenzaCamminoVerticale, int[] potenzaCamminoOrizzontale, String nomeFileCache, String output, String path) throws FileNotFoundException, UnsupportedEncodingException {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		boolean overwriteCachedFile = false;
		String fileToDelete = "";
		int numOfDifferentGraph = 0;
		
		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	  
		    	  String filename = listOfFiles[i].getName();
		    	  fileToDelete = filename;
		    	  int ind = filename.indexOf(".");
		    	  filename = filename.substring(0,ind);
		    	  String result[] = filename.split(" ");
//		    	  String t = result[0].substring(0,1);
		    	  String P1 = result[0];
		  		  String P2 = result[2];
		  		  
		  		String ff = "";
				for(int ii=0;ii<result[2].length();ii++){
					if(!(String.valueOf(result[2].charAt(ii))).equals("_")){
						ff+=result[2].charAt(ii);
					}
					else{
						break;
					}
				}
				
				String[] p1 = P1.split("_");
				String[] p2 = P2.split("_");
				
				int n2 = Integer.parseInt(p2[1]);
				int m2 = Integer.parseInt(p1[1]);
								
				int [] potenzeCamminoVerticale = Utils.ottienePotenzaCammino(p1[2]);				
				int [] potenzeCamminoOrizzontale = Utils.ottienePotenzaCammino(p2[2]);
				
				boolean checkSameGraph = m==m2 && ff.equalsIgnoreCase(f) && Arrays.equals(potenzaCamminoVerticale, potenzeCamminoVerticale) && Arrays.equals(potenzaCamminoOrizzontale, potenzeCamminoOrizzontale);
				boolean checkCondition = n>n2 ;
				
				if(path.equals(Constants.PATH_TO_CACHE_TM)){
					checkCondition = false;
				}
				
				if( !checkSameGraph )
				{
					numOfDifferentGraph ++;
				}
				
				else if( checkSameGraph && checkCondition )
				{
					overwriteCachedFile = true;
					break;
				}
		    	  
		      } else if (listOfFiles[i].isDirectory()) {
		        System.out.println("Directory " + listOfFiles[i].getName());
		      }
		    }
		    
		    if(numOfDifferentGraph==listOfFiles.length){
		    	//insert new file
		    	PrintWriter writer = new PrintWriter(path + nomeFileCache+".txt", "UTF-8");
				writer.println(output);
				writer.close();
		    }
		    
		    if(overwriteCachedFile){
		    	//delete old cached file
		    	File file = new File(path+fileToDelete);
	    		if(file.delete()){
	    			System.out.println(file.getName() + " is deleted!");
	    		}else{
	    			System.out.println("Delete operation is failed.");
	    		}
		    	//insert new file
		    	PrintWriter writer = new PrintWriter(path + nomeFileCache+".txt", "UTF-8");
				writer.println(output);
				writer.close();
		    }
		    
	}

	public static String[][] invertMatrix(String[][] schema2) {
		//inverto ordine delle righe delle schema originale
		String schema3[][] = new String[schema2.length][schema2[0].length];
		for(int i=schema2.length-1, j=0;i>=0;i--,j++){
			schema3[j] = schema2[i];
		}
		
		//inverto ordine delle colonne dello schema ottenuto al passo precedente
		String schema [][] = new String[schema2.length][schema2[0].length];
		for(int i=0;i<schema3.length;i++){
			String arr[] = schema3[i];
			for(int a=0,y=schema3[i].length-1;a<schema3[i].length;a++,y--){
				schema[i][a] = arr[y];
			}
		}
		return schema;
	}

}
