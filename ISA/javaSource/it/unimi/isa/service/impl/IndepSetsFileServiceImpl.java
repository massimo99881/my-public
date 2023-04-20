package it.unimi.isa.service.impl;

import it.unimi.isa.model.graphs.external.IGraphImpl;
import it.unimi.isa.model.graphs.grid.GridGraphImpl;
import it.unimi.isa.service.IndepSetsFileService;
import it.unimi.isa.utils.ISAConsole;
import it.unimi.isa.utils.Utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndepSetsFileServiceImpl implements IndepSetsFileService {

	private static final Logger logger = Logger.getLogger(IndepSetsFileServiceImpl.class);
	
	private String indep_output;
	
	@Autowired
	IndepSetServiceImpl indepSetServiceImpl;
	 
	/**
	 * Metodo per il calcolo degli insiemi indipendenti di un grafo definito esternamente in un file
	 * @param filename
	 * @throws Exception
	 */
	public void calcola(String filename) throws Exception {
		 
		 indep_output = "";
		 logger.info("Inizio procedura di calcolo indipedenti da grafo importato da file: "+filename);
		 
		 String basepath =  this.getClass().getClassLoader().getResource("").getPath();
			basepath += "it//unimi//isa//model//graphs//external//";
			
			// Open the file
			FileInputStream fstream = null;
			try {
				fstream = new FileInputStream(basepath + filename);
			} catch (FileNotFoundException e) {
				ISAConsole.setNotifylineText("Error: file not found!");
				logger.error(e);
				e.printStackTrace();
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;

			//Read File Line By Line
			int line = 0;
			
			String graphdef = "";
			boolean memograph = false;
			int n = -1;
			
			try {
				while ((strLine = br.readLine()) != null)   {
					
				  // Print the content on the console
				  System.out.println (++line+": " + strLine);
				  if(strLine.startsWith("n=")){
					  
					  //cattura valore corrente di n..
					  int index = strLine.indexOf("=");
					  int index2 = strLine.indexOf(";");
					  String nuovariga = strLine.substring(index+1,index2);
					  nuovariga = nuovariga.trim();
					  System.out.println("nuova linea ["+nuovariga+"]");
					  n = Integer.parseInt(nuovariga);
					  
					  if(memograph){
						  //ha finito di leggere il grafo quindi crealo
						  creaGrafoECalcola(graphdef,n);
						  memograph = false;
						  graphdef = "";
					  }
					  
				  }
				  if(strLine.startsWith("/*")){
					  if(memograph){
						  //ha finito di leggere il grafo quindi crealo
						  creaGrafoECalcola(graphdef,n);
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
			} catch (NumberFormatException e) {
				ISAConsole.setNotifylineText("Error reading file, see log for details");
				logger.error(e);
				e.printStackTrace();
			} catch (IOException e) {
				ISAConsole.setNotifylineText("Error reading file, please see log file for details.");
				logger.error(e);
				e.printStackTrace();
			}

			//Close the input stream
			try {
				br.close();
			} catch (IOException e) {
				ISAConsole.setNotifylineText("Error: file cannot be closed correctly");
				logger.error(e);
				e.printStackTrace();
			}
			
			logger.info("fine procedura di importazione grafo da file e calcolo indipendenti.");
	 }
	 
	 public void creaGrafoECalcola(String command, int n) throws Exception {
			command = command.replace(";","");
			System.out.println("n = "+n+" creaGrafo: "+command);
			String [] result = command.split(" ");
			
				
			GridGraphImpl iG = new IGraphImpl(Integer.parseInt(result[2]));
			for(int i=3;i<result.length;i++){
				String edge[] = Utils.getArrayFromString(result[i]);
				iG.addEdge(Integer.parseInt(edge[0]), Integer.parseInt(edge[1]));
			}
			System.out.println("\n"+iG.toString());
			iG.setN(n);
			List<HashMap<Integer,List<Integer>>> indepList = indepSetServiceImpl.forzaBruta(iG);
			indep_output += Utils.stampaMatriceIGraph(indepList,n);
	}
	 
	public String getOutput(){
		return this.indep_output;
	}
}
