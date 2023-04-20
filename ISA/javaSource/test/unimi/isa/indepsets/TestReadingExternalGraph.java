package test.unimi.isa.indepsets;

import it.unimi.isa.model.graphs.external.IGraphImpl;
import it.unimi.isa.model.graphs.grid.GridGraphImpl;
import it.unimi.isa.utils.Utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestReadingExternalGraph {

	public static void main(String args[]) throws IOException{
		System.out.println("######## INIZIO TEST TestReadingExternalGraph ########");
		new TestReadingExternalGraph();
		System.out.println("######## FINE TEST TestReadingExternalGraph ########");
		//logger.info("######## INIZIO TEST RicorrenzaLocaleServiceImplTest ########");
	}
	
	private int n;
	
	 public static GridGraphImpl iG = null;
	
	public TestReadingExternalGraph() throws IOException{
	
		String basepath =  this.getClass().getClassLoader().getResource("").getPath();
		basepath += "test//unimi//isa//indepsets//";
		
		// Open the file
		FileInputStream fstream = new FileInputStream(basepath + "test1.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine;

		//Read File Line By Line
		int line = 0;
//		int n = -1;
		String graphdef = "";
		boolean memograph = false;
		while ((strLine = br.readLine()) != null)   {
		  // Print the content on the console
		  System.out.println (++line+": " + strLine);
		  if(strLine.startsWith("n=")){
			  //cattura valore corrente di n..
			  int index = strLine.indexOf("=");
			  String nuovariga = strLine.substring(index+1,strLine.length()-1);
			  System.out.println("nuova linea ["+nuovariga+"]");
			  n = Integer.parseInt(nuovariga);
			  if(memograph){
				  //ha finito di leggere il grafo quindi crealo
				  creaGrafo(graphdef);
				  memograph = false;
				  graphdef = "";
			  }
			  
		  }
		  if(strLine.startsWith("/*")){
			  if(memograph){
				  //ha finito di leggere il grafo quindi crealo
				  creaGrafo(graphdef);
				  memograph = false;
				  graphdef = "";
			  }
			  
		  }
		  if(strLine.startsWith("*/")){
			  memograph = false;
			  graphdef = "";
		  }
		  if(strLine.startsWith("new graph")){
			  memograph = true;
		  }
		  
		  if(memograph){
			  graphdef += strLine;
		  }
		  
		}

		//Close the input stream
		br.close();
	}

	private void creaGrafo(String command) {
		command = command.replace(";","");
		System.out.println("n = "+this.n+" creaGrafo: "+command);
		String [] result = command.split(" ");
		
			
		iG = new IGraphImpl(Integer.parseInt(result[2]));
		for(int i=3;i<result.length;i++){
			String edge[] = Utils.getArrayFromString(result[i]);
			iG.addEdge(Integer.parseInt(edge[0]), Integer.parseInt(edge[1]));
		}
		System.out.println("\n"+iG.toString());
	}
	
	
}
