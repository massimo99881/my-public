package test.unimi.isa.dyck;

import it.unimi.isa.beans.PlaceholderBean;
import it.unimi.isa.service.impl.LaTexFormatterServiceImpl;
import it.unimi.isa.utils.Constants;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import de.nixosoft.jlr.JLRConverter;

/**
 *  NB. ha un limite: sequenze con al max 10 '+' (numeri di una sola cifra: da 0 a 9)
 
 For Input: ++-++-+---
 
 We have the following steps:
 
{#1}

{#1|#2}

{#1|2#3},{#1|2|#3}

{#1|2#3|#4},{#1|2#4|#3},{#1|2|#3|#4}

{#1|2#3|4},{#1|24|#3},{#1|2|#3|4}


{#1|2|#3|4|#5} 
{#1|2#5|#3|4}
{#1|2|#3|4#5},
{#1|24|#3|#5} 
{#1|24#5|#3} 
{#1|2#3|4|#5}, 
{#1|2#3|4#5}, 
 *
 */
@Service
public class Dyck {
	
	private static final Logger logger = Logger.getLogger(Dyck.class);
	
	@Autowired
	LaTexFormatterServiceImpl laTexFormatterServiceImpl;
	
	private static final String workingPath = "resources//tex";
	
	private static final char SLAVE_CHAR = '#';
	private static final String SLAVE = "#";
	private static final char UP = '+';
	private static final char DOWN = '-';

	public static void main(String args[]) throws InterruptedException{
		
		logger.info("######## INIZIO TEST dyckTest ########");
    	final ApplicationContext context = new ClassPathXmlApplicationContext(Constants.CONFIG_PATH);
		final Dyck dyckTest = context.getBean(Dyck.class);
		
		//String input = "++-++-+---"; //ok
		//String input = "++--++-+--";
		String input = "++-+--";
		//String input =   "+-+-+-+-";
		//String input = "++++----";
		//String input = "+++-+++---++----"; //UUUDUUUDDDUUDDDD //ok 
					  
		
		String output = "";
		output += dyckTest.printDyckPath(input);
		output += dyckTest.execute(input);
		
		output += dyckTest.printGraph(input);
		
		dyckTest.printLatex(output);
		logger.info("######## FINE TEST dyckTest ########");
	
	}
	
	public String printGraph(String input) {
		char[] charArray = input.toCharArray();

		HashMap<Integer,Integer[]> deltamap = new HashMap<Integer, Integer[]>();
		int cursor = 0;
		int curr_cursor = 0;
		Stack st = new Stack();
		logger.info("stack: " + st);
		
		
		for(int y=0;y<charArray.length;y++){
			if(charArray[y]==UP){
				cursor++;
				curr_cursor++;
				
				if(curr_cursor>cursor){
					cursor = curr_cursor;
				}
				
				st.push(new Integer(cursor));
				
				Integer [] delta = new Integer[2];
				delta[0] = y;
				deltamap.put(new Integer(cursor), delta);
				logger.info("letto UP, cursor: "+cursor);
			}
			else if(charArray[y]==DOWN){
				cursor = (Integer) st.pop(); 
				Integer [] delta = deltamap.get(cursor);
				delta[1] = y;
				deltamap.put(cursor, delta);
				logger.info("letto DOWN, cursor: "+cursor);
			}
		}
		
		Iterator it = deltamap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + printArray((Integer[]) pair.getValue()));
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
		
	    System.out.println();
	    
	    HashMap<Integer,List<Integer>> adjacentListGraph = new HashMap<Integer, List<Integer>>();
	    
	    it = deltamap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Integer cursor2 = (Integer) pair.getKey();
	        Integer [] delta = (Integer[]) pair.getValue();
	        int start = delta[0];
			int end = delta[1];
	        List<Integer> list = new ArrayList<Integer>();
	        cursor = 0;
	        int curr_cursor2 = 0;
	        
	        st = new Stack();			
			for(int y=0;y<charArray.length;y++){
				if(charArray[y]==UP){
					cursor++;
					curr_cursor2++;
					
					if(curr_cursor2>cursor){
						cursor = curr_cursor2;
					}
					
					st.push(new Integer(cursor));
					if( cursor2!=cursor && (start <= y) && (y <= end) ){
						list.add(cursor);
					}
				}
				else if(charArray[y]==DOWN){
					cursor = (Integer) st.pop(); 
				}
			}
			
			adjacentListGraph.put(cursor2, list);
	        
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	    
	    System.out.println("lista di adiacenza del grafo ");
	    it = adjacentListGraph.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Integer cursor2 = (Integer) pair.getKey();
	        List<Integer> list =  (List<Integer>) pair.getValue();
	        System.out.print(cursor2 + " = ");
	        for (Integer integer : list) {
				System.out.print(integer+",");
			}
	        System.out.println();
	    }
	    
	    //costruisce matrice di adiacenza..
	    int dim = adjacentListGraph.size();
	    Integer [][] adjacentMatrixGraph = new Integer [dim][dim]; 
	    it = adjacentListGraph.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Integer key = (Integer) pair.getKey(); 
	        key --;
	        List<Integer> list =  (List<Integer>) pair.getValue();
	        for (Integer integer : list) {
	        	integer--;
				adjacentMatrixGraph[key][integer] = 1;
				
			}
	    }
	    
	    for(int a=0;a<adjacentMatrixGraph.length;a++){
	    	for(int b=0;b<adjacentMatrixGraph[a].length;b++){
	    		if(adjacentMatrixGraph[a][b] == null){
	    			adjacentMatrixGraph[a][b] = 0;
	    		}
	    		System.out.print(adjacentMatrixGraph[a][b]+",");
	    	}
	    	System.out.println();
	    }
	    
	    //crea complemento del grafo
	    for(int i=0;i<adjacentMatrixGraph.length;i++)
	    	for(int j=0;j<adjacentMatrixGraph[i].length;j++){
	    		if(adjacentMatrixGraph[i][j]==0)
	    			adjacentMatrixGraph[i][j] = 1;
	    		else
	    			adjacentMatrixGraph[i][j] = 0;
	    	}
	    
	    //crea lista di adiacenza complemento
	    HashMap<Integer,List<Integer>> adjacentListGraphComplement = new HashMap<Integer, List<Integer>>();
	    for(int i=0;i<adjacentMatrixGraph.length;i++) {
	    	List<Integer> list = new ArrayList<Integer>();
	    	for(int j=0;j<adjacentMatrixGraph[i].length;j++){
	    		if(adjacentMatrixGraph[i][j]==1){
	    			if(		//controlla archi ripetuti..
	    					!isNotPresent(adjacentListGraphComplement,j+1,i+1) 
	    						&&
	    					//controlla archi invertiti nella lista del grafo origianle
	    					!isNotPresent(adjacentListGraph,j+1,i+1) 
	    						&& 
	    					//elimina i cappi ..
	    					(i+1)!=(j+1))
	    			{
	    				list.add(j+1);
	    			}
	    		}
	    	}
	    	adjacentListGraphComplement.put(i+1, list);
		}
	    
	    System.out.println("lista di adiacenza del grafo complementare : ");
	    it = adjacentListGraphComplement.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Integer cursor2 = (Integer) pair.getKey();
	        List<Integer> list =  (List<Integer>) pair.getValue();
	        System.out.print(cursor2 + " = ");
	        for (Integer integer : list) {
				System.out.print(integer+",");
			}
	        System.out.println();
	    }
	    
	    //costruisce disegno 
	    
	    String output = "";
	    
	    
	    output += "\\noindent Disegno del grafo:\\ \n";
	    output += disegnaGrafo(adjacentListGraph);
	    output += "\\begin{definition} Ogni partizione stabile di G è una copertura in cricche del complemento di G. \\end{definition} ";
	    output += "\\noindent Disegno del grafo complementare:\\ \n";
	    output += disegnaGrafo(adjacentListGraphComplement);
	    output += "\n \\begin{definition} Una copertura in cricche di H è una partizione dei vertici di H tale che ogni blocco sia una cricca di H.  \\end{definition}";
	    
	    
	    output += "\\noindent Sia B un blocco di SP(G), allora tutti i vertici di G che stanno in B sono a due a due disgiunti. \\ ";
	    output += "Quindi gli stessi vertici nel complemento di G saranno a due a due connessi e i vertici nel complemento di G saranno una cricca. \\  ";
	    output += "Pertanto ad ogni partizione stabile di G corrisponde una copertura in cricche del complemento di G.  \\ \n";
	    
		return output;
	}
	
	private boolean isNotPresent(HashMap<Integer, List<Integer>> adjacentListGraphComplement, int i,int j) {
		// verifica se esiste gia un collegamento con i vertici invertiti
		Iterator it = adjacentListGraphComplement.entrySet().iterator();
		boolean present = false;
	    while (it.hasNext() && !present) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Integer cursor2 = (Integer) pair.getKey();
	        List<Integer> list =  (List<Integer>) pair.getValue();
	        //System.out.print(cursor2 + " = ");
	        for (Integer integer : list) {
				//System.out.print(integer+",");
	        	if(cursor2==i && integer==j)
	        	{
	        		present = true;
	        		break;
	        	}
			}
	        //System.out.println();
	    }
		return present;
	}

	private String disegnaGrafo(HashMap<Integer,List<Integer>> adjacentListGraph){
		String output = "";
		output +=
			"\n\\begin{figure}[H]\n";
	    output+=
			"\t\\centering\n"+
			"\t\t\\begin{tikzpicture}[>=latex',scale=0.8]\n"+
			"\t\t\t\\begin{dot2tex}[tikz] \n"+
			"\t\t\tdigraph finite_state_machine { \n"+
			
			"\t\t\t\tgraph[rankdir=BT, center=true, margin=0.2, nodesep=0.1, ranksep=0.3] \n"+
		    "\t\t\t\tnode[shape=circle, fontname=\"Courier-Bold\", fontsize=10, width=0.4, height=0.4, fixedsize=true] \n"+
		    "\t\t\t\tedge[arrowsize=0.6, arrowhead=vee] \n\n"; //

		Iterator entries = adjacentListGraph.entrySet().iterator();
		
		while (entries.hasNext()) 
		{
		    Map.Entry entry = (Map.Entry) entries.next();
		    Integer vertex = (Integer)entry.getKey();
		
		    List<Integer> values = (ArrayList<Integer>) entry.getValue();
		    for(Integer s : values)
		    {		    	
		    	output += "\t\t\t\t"+ vertex + " -> " + s + " [dir=none]; \n"; //[ label = \""+transizione+"\" ]
		    }		    		   
		}
		output += 
//			" \n\t\t\t\tlabel=\"My Diagram\"; \n"+
			"\t\t\t} \n"+			
			"\t\t\\end{dot2tex} \n"+
			"\t\\end{tikzpicture} \n"+
			"\\end{figure} \n\n"+
			"";
		
		return output;
	}

	private String printArray(Integer [] array) {
		String output = "";
		for(int i=0;i<array.length;i++)
			output += array[i]+",";
		return output;
	}

	public String printDyckPath(String input) {
		
		String coordinates = "(0,0) ";
		String labels = "";
		char[] charArray = input.toCharArray();
		int i=0,j=0,z=0;
		for(char c : charArray){
			
			if(c=='+'){
				j++;
				z++;
				labels += " \\node[label={180:{"+z+"}},circle,fill,inner sep=2pt] at (axis cs:"+(i+1)+","+j+") {}; \n";
			}
			else if(c=='-'){
				j--;
			}
			i++;
			coordinates += "("+i+","+j+") ";
		}
		
		String output = ""+
		"\\begin{tikzpicture} \n"+
		"\\begin{axis}[axis y line=center, axis x line=middle,grid=major,xmin=0,ymin=0,ymax="+(z+1)+",grid style={dashed,gray!30},y=3.5cm/3,x=1.0cm]  \n"+
		"\\addplot coordinates"+
			"{ \n "+coordinates+" \n}; "+
			labels +
		"\\end{axis} \n"+
		"\\end{tikzpicture}\\\\ \n ";
		return output;
	}

	public String execute(String input) {
		String outputLatex = "";
		List<List<String>> stablePartitionsList = new ArrayList<List<String>>();
		List<String> partition = new ArrayList<String>();
		int curr_cursor = 0;
		int cursor = 0;
		
		Stack st = new Stack();
		
		logger.info("stack: " + st);
		
		char[] charArray = input.toCharArray();
		
		char c_prev = 0;
		for(char c : charArray) 
		{
			String stablePartition = "";
			if(c==UP){
				cursor++;
				
				if(cursor==4){
					logger.info("for debug");
				}
				
				curr_cursor++;
				
				if(curr_cursor>cursor){
					cursor = curr_cursor;
				}
				
				st.push(new Integer(cursor));
				
				logger.info("letto UP, cursor: "+cursor);
				outputLatex += "\\noindent letto UP, cursor: "+cursor + "\\\\ \n";
				
				if(c_prev != DOWN){
					//first time..
					if(stablePartitionsList.isEmpty()){
						stablePartition = SLAVE + cursor;
						partition.add(stablePartition);
						stablePartitionsList.add(partition);
					}
					else {
						//-->
						
						stablePartition = SLAVE + cursor;
						
						Iterator<List<String>> iter = stablePartitionsList.iterator();
						List<List<String>> stablePartitionsList_New = new ArrayList<List<String>>();
						
						while(iter.hasNext()){
							
							List<String> list = iter.next();
							
							//add the new vertex in a separated block
							List<String> clone = new ArrayList<String>(list);
							clone.add(stablePartition);
							stablePartitionsList_New.add(clone);
							
							int i=0;
							HashMap<Integer,String> map = new HashMap<Integer,String>();
							for(String s : list){
								if(!s.contains(SLAVE)){
									//map.put(i, s+""+stablePartition);
									clone = new ArrayList<String>(list);
									clone.set(i, s+""+stablePartition);
								    stablePartitionsList_New.add(clone);
								}
									
								i++;
							}
							
							//if there is a partition that have a block with no slave, than add new vertex to it
							Iterator it = map.entrySet().iterator();
						    while (it.hasNext()) {
						        Map.Entry pair = (Map.Entry)it.next();
						        Integer index = (Integer) pair.getKey();
						        String s = (String) pair.getValue();
						        list.set(index, s);
						        //it.remove(); // avoids a ConcurrentModificationException
						    }
						    
						    if(!map.isEmpty())
						    	stablePartitionsList_New.add(list);
						}
						
						stablePartitionsList = stablePartitionsList_New;
					}
				}
				else if (c_prev == DOWN){
					stablePartition = SLAVE + cursor;
					
					//when we have un UP after DOWN
					Iterator<List<String>> iter = stablePartitionsList.iterator();
					List<List<String>> stablePartitionsList_New = new ArrayList<List<String>>();
					
					while(iter.hasNext()){
						 
						List<String> list = iter.next();
						
						//add the new vertex in a separated block
						List<String> clone = new ArrayList<String>(list);
						clone.add(stablePartition);
						stablePartitionsList_New.add(clone);
						
						//for all current stable partitions check if there is a non-slave block, so add new vertex to it
						
						int i=0; 
						for(String s : list){
							if(!s.contains(SLAVE)){
								clone = new ArrayList<String>(list);
								clone.set(i, s+""+stablePartition);
								
							    stablePartitionsList_New.add(clone);
							}
							i++;
						}
					}
					
					stablePartitionsList = stablePartitionsList_New;
				}
				
			}
			
			else if(c==DOWN){
				Integer a = (Integer) st.pop();
				cursor = a; //<<-----
				logger.info("letto DOWN, cursor: "+cursor);
				outputLatex += "\\noindent letto DOWN, cursor: "+cursor+"\\\\ \n";

				Iterator<List<String>> iter = stablePartitionsList.iterator();
				List<List<String>> stablePartitionsList_New = new ArrayList<List<String>>();
				
				//foreach partitions..
				while(iter.hasNext()){
					List<String> list = iter.next();
					
					//foreach blocks (s) check if there is current cursor as slave, so set it as free
					int i=0;
					HashMap<Integer,String> map = new HashMap<Integer,String>();
					char d = (char) ('0' + cursor);
					
					for(String s : list){
						
						int res = s.indexOf(d);
						if(res>0 && s.charAt(res-1)==SLAVE_CHAR){
							s = s.substring(0,res-1) + s.substring(res);
							map.put(i, s);
						}
						i++;
					}
					
					Iterator it = map.entrySet().iterator();
				    while (it.hasNext()) {
				        Map.Entry pair = (Map.Entry)it.next();
				        Integer index = (Integer) pair.getKey();
				        String s = (String) pair.getValue();
				        list.set(index, s);
				        //it.remove(); // avoids a ConcurrentModificationException
				    }
					
					if(!map.isEmpty())
				    	stablePartitionsList_New.add(list);
				    
				}
				
				stablePartitionsList = stablePartitionsList_New;
			}
			
			outputLatex += print(stablePartitionsList);
			c_prev = c;
			
		}
		
		try {
			outputLatex += transformAsPolinomials(stablePartitionsList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return outputLatex;
	}
	
	private String transformAsPolinomials(List<List<String>> stablePartitionsList) throws Exception {
		String output = "";
		
		HashMap<String,Integer> polinomialsMap = new HashMap<String, Integer>();
		
		Iterator<List<String>> iter = stablePartitionsList.iterator();
		while(iter.hasNext()){
		    Iterator<String> siter = iter.next().iterator();
		    HashMap<Integer,Integer> memo = new HashMap<Integer,Integer>();
		    while(siter.hasNext()){
		         String s = siter.next();
		         System.out.print(s+",");
		         int dimBlocco = s.length();

		         if(!memo.isEmpty()){
		        	 Integer val = memo.get(dimBlocco);
		        	 if(val!=null){
		        		 memo.remove(dimBlocco);
		        		 val++;
		        		 
		        	 }
		        	 else{
		        		 val = 1;
		        	 }
		        	 
		        	 memo.put(dimBlocco, val);
		         }
		         else {
		        	 memo.put(dimBlocco,1);
		         }
		     }
		    System.out.println();
		    
		    //scrivo in forma di polinomio ..
		    
		    Iterator it = memo.entrySet().iterator();
		    int size = memo.size();
		    int i=0;
		    String polinomio  = "";
		    
		    while (it.hasNext()) {
		    	
		        Map.Entry pair = (Map.Entry)it.next();
		        Integer dimBlocco = (Integer) pair.getKey();
		        Integer quanti = (Integer) pair.getValue();
		        polinomio += "w_"+dimBlocco+"^"+quanti;
		        System.out.print("w_"+dimBlocco+"^"+quanti);
		        if(i<size-1) {
		        	polinomio += " ";
		        	System.out.print(" ");
		        }
		        
		        
		        //it.remove(); // avoids a ConcurrentModificationException
		        i++;
		        
		    }
		    
		    boolean checked = false;
	    	Iterator it2 = polinomialsMap.entrySet().iterator();
	    	 while (it2.hasNext() && !checked) {
			        Map.Entry pair = (Map.Entry)it2.next();
			        String polinomioCorrente = (String) pair.getKey();
			        Integer occorrenzeTrovate = (Integer) pair.getValue();
			        
			        if(polinomioCorrente.equals(polinomio)){
			        	polinomialsMap.remove(polinomioCorrente);
			        	polinomialsMap.put(polinomioCorrente, occorrenzeTrovate+1);
			        	checked = true;
			        }
			       
			 }
	    	 
	    	 if(!checked){
	    		 polinomialsMap.put(polinomio, 1);
	    	 }
		    
		    System.out.println();
		    
		}
		
		//stampa polinomialsMap
		Iterator it3 = polinomialsMap.entrySet().iterator();
		Integer totale = 0;
		output += "\n\n \\noindent Trasformazione in polinomio: \n\n $$";
		int j = 0;
   	    while (it3.hasNext() ) {
		        Map.Entry pair = (Map.Entry)it3.next();
		        String polinomioCorrente = (String) pair.getKey();
		        Integer occorrenzeTrovate = (Integer) pair.getValue();
		        totale += occorrenzeTrovate;
		        System.out.println(polinomioCorrente + " , occorrenze = "+occorrenzeTrovate);
		        if(occorrenzeTrovate!=1){
		        	output+= occorrenzeTrovate + " " + polinomioCorrente+ " ";
		        }
		        else {
		        	output+= " " + polinomioCorrente+ " ";
		        }
		        
		        if(j<polinomialsMap.size()-1){
		        	output += " + ";
		        }
		        j++;
		 }
   	    output += "$$ \n";
   	    
   	    if(totale!=stablePartitionsList.size()){
   	    	throw new Exception("Errore nel calcolo del polinomio");
   	    }
		
		return output;
		
		
	}

	private String print(List<List<String>> stablePartitionsList){
		String outputLatex = "\n \\noindent ";
		String output = "";
		Iterator<List<String>> iter = stablePartitionsList.iterator();
		while(iter.hasNext()){
			output += "{";
			outputLatex += "${";
		    Iterator<String> siter = iter.next().iterator();
		    while(siter.hasNext()){
		         String s = siter.next();
		         output += s+"|";
		         //latex only..
			         char[] charArray = s.toCharArray();
			         s = "";
			         boolean check = false;
			         for(char c : charArray){
			        	 if(c==SLAVE_CHAR){
			        		 s+= "\\textcolor{red}{ \\widehat{";
			        		 check = true;
			        	 }
			        	 else if(check){
			        		 s+=c+"} }";
			        		 check = false;
			        	 }
			        	 else{
			        		 s+=" "+c +" ";
			        	 }
			         }
			         
		         
		         outputLatex += s+" \\mid ";
		     }
		    output += "},\n";
		    outputLatex += "}$ \\\\ \n ";
		}
		
		logger.info(output);
		return outputLatex+ " \n\n";
	}
	
	public void printLatex(String output) throws InterruptedException {
		File workingDirectory = new File(workingPath);
        File template = new File(workingDirectory.getAbsolutePath() + "//templateDyck.tex");
        File tempDir = new File(workingDirectory.getAbsolutePath() + File.separator + "temp");
        if (!tempDir.isDirectory()) {
            tempDir.mkdir();
        }

        File invoice1 = new File(tempDir.getAbsolutePath() + File.separator + "testDyck.tex");
       
         try {
            JLRConverter converter = new JLRConverter(workingDirectory);
            
            PlaceholderBean pb = new PlaceholderBean();
            pb.setTitle("Dyck Test");
            
            converter.replace("title", "DyckTest");
            
            converter.replace("output1o1", output);
            
            if (!converter.parse(template, invoice1)) {
                logger.info(converter.getErrorMessage());
            }

            String line;
		      //Provare anche: Process p = Runtime.getRuntime().exec("\"C:\\Program Files\\MiKTeX 2.9\\miktex\\bin\\latex.exe\" --output-format=pdf \"C:\\Users\\Administrator.max-PC\\WolframWorkspaces\\ISA\\resources\\tex\\provaGraphviz.tex\" -shell-escape ");
		      Process p = Runtime.getRuntime().exec("\"" + Constants.LATEX_PATH + "\" --output-format=pdf \"" + Constants.PATH_TO_TEX + "testDyck.tex\" -shell-escape ");

		      /**
		       * #2373 lualatex: --output-directory doesn't work
		       * The option --output-directory does nothing with lualatex.
		       * http://sourceforge.net/p/miktex/bugs/2373/
		       */
		      
		      BufferedReader bri = new BufferedReader
		        (new InputStreamReader(p.getInputStream()));
		      BufferedReader bre = new BufferedReader
		        (new InputStreamReader(p.getErrorStream()));
		      while ((line = bri.readLine()) != null) {
		        logger.info(line);
		        if(line.startsWith(Constants.ERROR_FILE_OPENING_PROG)){
		        	logger.error(Constants.ERROR_FILE_OPENING);
		        }
		      }
		      bri.close();
		      while ((line = bre.readLine()) != null) {
		        logger.info(line);
		      }
		      bre.close();
		      p.waitFor();
		      logger.info("Done.");
		      
	    	  	if (Desktop.isDesktopSupported()) {
		    	  logger.info("isDesktopSupported..");
		    	    try {
		    	    	
		    	        File myFile = new File(Constants.PATH_TO_PDF+"\\" + "testDyck.pdf");
		    	        Desktop.getDesktop().open(myFile);
		    	        
		    	        logger.info("Stampa completata") ;
		    	        
		    	    } catch (IOException ex) {
		    	    	
		    	    	// no application registered for PDFs
		    	    	logger.error("ERROR: ", ex);
		    	        
		    	    }
		      }
		      else {
		    	  logger.info("nessuna applicazione supportata per aprire pdf");
		      }
            
	    	  	//JLROpener.open(pdfGen.getPDF());

        } catch (IOException ex) {
        	logger.error(ex.getMessage());
        }
	}
}

