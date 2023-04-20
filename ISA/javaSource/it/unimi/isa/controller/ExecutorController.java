package it.unimi.isa.controller;

import it.unimi.isa.model.graphs.Graph;
import it.unimi.isa.model.graphs.external.IGraphImpl;
import it.unimi.isa.model.graphs.grid.GridGraphImpl;
import it.unimi.isa.service.impl.IndepOperationServiceImpl;
import it.unimi.isa.service.impl.IndepSetServiceImpl;
import it.unimi.isa.service.impl.IndepSetsFileServiceImpl;
import it.unimi.isa.service.impl.LaTexPrinterV1ServiceImpl;
import it.unimi.isa.service.impl.LaTexPrinterV2ServiceImpl;
import it.unimi.isa.service.impl.RicorrenzaLocaleServiceImpl;
import it.unimi.isa.singleton.Singleton;
import it.unimi.isa.utils.Constants;
import it.unimi.isa.utils.ISAConsole;
import it.unimi.isa.utils.Utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import test.unimi.isa.dyck.Dyck;

@Controller
@Scope("prototype")
public class ExecutorController implements Runnable {
	
	private static final Logger logger = Logger.getLogger(ExecutorController.class);

	@Autowired
	Singleton singleton;

	@Autowired
	ISAConsole isaConsole;

	@Autowired
	LaTexPrinterV2ServiceImpl laTexPrinterV2Impl;

	@Autowired
	LaTexPrinterV1ServiceImpl laTexPrinterV1Impl;

	@Autowired
	IndepSetServiceImpl indepSetServiceImpl;

	@Autowired
	IndepOperationServiceImpl indepOperationServiceImpl;

	@Autowired
	IndepSetsFileServiceImpl indepSetsFileServiceImpl;
	
	@Autowired
	RicorrenzaLocaleServiceImpl ricorrenzaLocaleServiceImpl;
	
	@Autowired
	Dyck dyckServiceImpl;

	private String currpath = Constants.PATH_TO_ISA;
	private String command1 = "";
	private String command2 = "";
	private int prompt = 1;
	private boolean entering_password = false;

	public String user = "user";

	public static GridGraphImpl iG = null;

	@Override
	public void run() {

		int current_prompt = this.prompt;
		ISAConsole.setNotifylineText("in progress ..");

		try {

			String[] result = null;
			if (current_prompt == 1) {
				result = command1.split(" ");
			} else if (current_prompt == 2) {
				result = command2.split(" ");
			}

			if ("".equalsIgnoreCase(result[0])) {
				isaConsole.setResultArea("", current_prompt);
			}

			/******************************
			 * command: new graph 5 {0,1} {1,2} {3,2} {1,4}
			 ******************************/
			else if (result.length > 2 && ("new".equalsIgnoreCase(result[0]) && "graph".equalsIgnoreCase(result[1]))) {
				iG = new IGraphImpl(Integer.parseInt(result[2]));
				for (int i = 3; i < result.length; i++) {
					String edge[] = Utils.getArrayFromString(result[i]);
					iG.addEdge(Integer.parseInt(edge[0]), Integer.parseInt(edge[1]));
				}
				isaConsole.setResultArea("\n" + iG.toString(), current_prompt);
			}
			/******************************
			 * command: open file.txt[/pdf..]
			 ******************************/
			else if ("clean".equalsIgnoreCase(result[0])) {
				if(Utils.pulisciCartellaDaFileTemporanei()){
					isaConsole.setResultArea("\nPulizia file completata con successo.", current_prompt);
				}
				else {
					isaConsole.setResultArea("\nErrore", current_prompt);
				}
			}
			/******************************
			 * command: open file.txt[/pdf..]
			 ******************************/
			else if ("open".equalsIgnoreCase(result[0])) {
				if (Desktop.isDesktopSupported()) {
					try {
						File myFile = new File(currpath + result[1]);
						Desktop.getDesktop().open(myFile);
					} catch (IOException ex) {
						// no application registered for PDFs
						isaConsole.setResultArea("\n no application registered to open this file",current_prompt);
					}
				}
			}
			/******************************
			 * command: used for test saved schema only
			 ******************************/
			else if ("schema".equalsIgnoreCase(result[0])) {
				isaConsole.setResultArea("\n" + Utils.stampaSchemaSalvati(ricorrenzaLocaleServiceImpl.getSavedSchemas()), current_prompt);

			}
			/******************************
			 * command: edit file.txt[/pdf..]
			 ******************************/
			else if ("edit".equalsIgnoreCase(result[0])) {
				if ("admin".equalsIgnoreCase(user)) {
					if (!Desktop.isDesktopSupported()) {
						isaConsole.setResultArea("\nno application registered to open this file",current_prompt);
					}

					Desktop desktop = Desktop.getDesktop();
					if (!desktop.isSupported(Desktop.Action.EDIT)) {
						isaConsole.setResultArea("\noperation not supported",current_prompt);
					}

					try {
						File myFile = new File(currpath + result[1]);
						desktop.edit(myFile);
					} catch (IOException e) {
						// Log an error
						isaConsole.setResultArea("\nerror occurred, see log files",	current_prompt);
					}
				} else {
					isaConsole.setResultArea("\nunauthorized user",current_prompt);
				}

			}
			/******************************
			 * command: su - admin
			 ******************************/
			else if ("su".equalsIgnoreCase(result[0])) {
				if ("-".equalsIgnoreCase(result[1]) && "admin".equalsIgnoreCase(result[2])) {
					isaConsole.setResultArea("enter password: ", current_prompt);
					entering_password = true;
				}
			} else if (Constants.USER_PWD.equalsIgnoreCase(result[0]) && entering_password) {
				entering_password = false;
				user = "admin";
			} else if ("exit".equalsIgnoreCase(result[0])) {
				user = "user";
			}
			/******************************
			 * command: cd
			 ******************************/
			else if ("cd".equalsIgnoreCase(result[0])) {
				currpath += result[1] + "//";
			}
			/******************************
			 * command: pwd
			 ******************************/
			else if ("pwd".equalsIgnoreCase(result[0])) {
				isaConsole.setResultArea("\n" + currpath, current_prompt);
			}
			/******************************
			 * command: ls
			 ******************************/
			else if ("ls".equalsIgnoreCase(result[0])) {
				listFiles(current_prompt);
			}
			/******************************
			 * command: clear
			 ******************************/
			else if ("clear".equalsIgnoreCase(result[0])) {
				isaConsole.clear(current_prompt);
			}
			/******************************
			 * command: help
			 ******************************/
			else if ("help".equalsIgnoreCase(result[0])) {
				showHelp(current_prompt);
			}
			/******************************
			 * command: quit
			 ******************************/
			else if ("quit".equalsIgnoreCase(result[0])	|| "q".equalsIgnoreCase(result[0])) {
				isaConsole.mainFrame.dispose();
			}
			/******************************
			 * command: dyck
			 ******************************/
			else if ("dyck".equalsIgnoreCase(result[0])) {
				String input = result[1];
				String output = "";
				
				output += dyckServiceImpl.printDyckPath(input);
				output += dyckServiceImpl.execute(input);
				output += dyckServiceImpl.printGraph(input);
				
				dyckServiceImpl.printLatex(output);
				
			}
			/******************************
			 * command: show
			 ******************************/
			else if ("show".equalsIgnoreCase(result[0])) {
				/******************************
				 * command: show graph
				 ******************************/
				if ("graphs".equalsIgnoreCase(result[1])) {
					String indent = "\n\t";
					isaConsole.setResultArea("", current_prompt);
					isaConsole.setResultArea("\n--Grid Graphs :\n",	current_prompt);
					isaConsole.setResultArea("In general we have: P_m_(h1) x P_n_(h2)\n",	current_prompt);
					isaConsole.setResultArea("Examples:\n", current_prompt);
					isaConsole.setResultArea("P_2_1 x P_5_1 "+ indent+ "this is a very sample one step power of walk grid graph\n",current_prompt);
					isaConsole.setResultArea("P_2_1 x P_5_3 "+ indent+ "now the orizontal power is of three step, but not of two (.. obviously of one)\n",current_prompt);
					isaConsole.setResultArea("P_2_1 x F_5_1 " + indent+ "where F have one diagonals for each step\n",current_prompt);
					isaConsole.setResultArea("P_2_1 x Z_6_1 " + indent+ "where Z have two diagonals for each step\n",current_prompt);
					isaConsole.setResultArea("P_2_1 x C_5_5 " + indent+ "C stand for Circuit\n", current_prompt);
					isaConsole.setResultArea("P_2_1 x M_5_5 " + indent+ "M stand for Moebius\n", current_prompt);
					isaConsole.setResultArea("P_2_1 x CF_5_5 "+ indent+ "CF is combination of Circuit and one diagonals for each step\n",current_prompt);
					isaConsole.setResultArea("P_2_1 x CZ_5_5 "+ indent+ "CZ is combination of Circuit and two diagonals for each step\n",current_prompt);
					isaConsole.setResultArea("P_2_1 x FT_5_2 "+ indent+ "FT is combination of F grid graphs and two step power of walk\n",current_prompt);
					isaConsole.setResultArea("P_2_1 x CTZ_5_5 "+ indent+ "CTZ is combination of Circuit, two step power of walk and two diagonals for each step\n",current_prompt);
					isaConsole.setResultArea("", current_prompt);
				}
			}
			/******************************
			 * command: print
			 ******************************/

			else if ("print".equalsIgnoreCase(result[0])) {

				checkPrinterOutput(result, current_prompt);

			}
			/******************************
			 * command: indep
			 ******************************/
			else if ("indep".equalsIgnoreCase(result[0])) {
				/******************************
				 * command: indep from <nomefile.txt>
				 ******************************/
				if ("from".equalsIgnoreCase(result[1])) {
					isaConsole.setResultArea("\ninizio elaborazione..",	current_prompt);
					indepSetsFileServiceImpl.calcola(result[2]);
					isaConsole.setResultArea("\n"+ indepSetsFileServiceImpl.getOutput(), current_prompt);
				} else {
					try {
						/******************************
						 * command: indep p_2_1 x p_6_1
						 ******************************/
						//String p1 = result[3];
						// getting graph
						isaConsole.setResultArea("\ncalcolo in corso..",current_prompt);
						Graph g = Utils.ottieneGrafo(result, 1, 3);
						List<HashMap<Integer,List<Integer>>> indepList = indepSetServiceImpl.forzaBruta(g);
						isaConsole.setResultArea("\n" + g.getNome(),current_prompt);
						isaConsole.setResultArea("\n"+ Utils.stampaMatrice(indepList),current_prompt);
					} catch (IndexOutOfBoundsException ex) {
						/******************************
						 * command: indep graph
						 ******************************/
						if(iG==null){
							isaConsole.setResultArea("\nCostruire prima un grafo. Es. new graph 4 {0,1} {0,2} {1,3} {2,3}" , current_prompt);
						}
						else{
							List<HashMap<Integer,List<Integer>>> indepList = indepSetServiceImpl.forzaBruta(iG);
							isaConsole.setResultArea("\n" + iG.getNome(),current_prompt);
							isaConsole.setResultArea("\n"+ Utils.stampaMatrice(indepList),current_prompt);
						}
						

					}
				}

			}
			/******************************
			 * command: calc
			 ******************************/
			else if ("calc".equalsIgnoreCase(result[0])) {
				operazioniTraMatriciIndipendenti(result, current_prompt);
			}
			/******************************
			 * not found
			 ******************************/
			else {
				logger.info("command not found!");
				isaConsole.setResultArea("\nComando non trovato, controllare la sintassi digitando \"help\".",current_prompt);
			}

			ISAConsole.setNotifylineText("process completed");

		} catch (Exception e) {
			// 
			e.printStackTrace();
			ISAConsole.setNotifylineText("An error occurred, see log files.");
		} finally {
			if (current_prompt == 1) {
				command1 = "";
				String text = singleton.getIsaConsole().commandArea.getText();
				singleton.getIsaConsole().commandArea.setText(text + "\n"+ user + "> ");
			} else if (current_prompt == 2) {
				command2 = "";
				String text = singleton.getIsaConsole().commandArea2.getText();
				singleton.getIsaConsole().commandArea2.setText(text + "\n"+ user + "> ");
			}

		}

	}

	/**
	 * Effettua calcolo tra valori di due tabelle degli indipendenti
	 * @param result
	 * @param current_prompt
	 * @throws Exception
	 */
	private void operazioniTraMatriciIndipendenti(String[] result, int current_prompt) throws Exception {
		// Esempi:
		// calc P_2_1 x P_5_1 - P_2_1 x P_5_2
		// calc P_2_1 x P_5_1 - P_2_1 x Z_6_1 (Z)
		// calc P_2_1 x P_5_1 - P_2_1 x C_5_5 (C)
		isaConsole.setResultArea("\ncalcolo in corso..", current_prompt);
		
		//Necessario disabilitare il caricamento dalla cache .. salva prima i valori correnti
		Boolean current_enable_cache_load = Constants.ENABLE_CACHE_LOAD;
		Boolean current_enable_cache_save = Constants.ENABLE_CACHE_SAVE;
		//disabilita cache
		Constants.ENABLE_CACHE_LOAD = false;
		Constants.ENABLE_CACHE_SAVE = false;

		Graph g = Utils.ottieneGrafo(result, 1, 3);
		List<HashMap<Integer,List<Integer>>> indepList = indepSetServiceImpl.forzaBruta(g);
		Integer[][] tabella1 = Utils.convertIndepHashMapListToMatrix(indepList);
		isaConsole.setResultArea("\nFirst independent set :", current_prompt);
		isaConsole.setResultArea("\n" + Utils.print(tabella1), current_prompt);

		String operation = result[4];

		g = Utils.ottieneGrafo(result, 5, 7);
		List<HashMap<Integer,List<Integer>>> indepList2 = indepSetServiceImpl.forzaBruta(g);
		Integer[][] tabella2 = Utils.convertIndepHashMapListToMatrix(indepList2);
		isaConsole.setResultArea("\nSecond independent set :", current_prompt);
		isaConsole.setResultArea("\n" + Utils.print(tabella2), current_prompt);

		isaConsole.setResultArea("\nOperation is: " + operation, current_prompt);

		isaConsole.setResultArea("\nResult: ", current_prompt);
		Integer[][] res = indepOperationServiceImpl.execute(Utils.copyMatrix(tabella1), Utils.copyMatrix(tabella2), operation);
		isaConsole.setResultArea("\n" + Utils.print(res), current_prompt);
		
		//ripristina valori gestione cache
		Constants.ENABLE_CACHE_LOAD = current_enable_cache_load;
		Constants.ENABLE_CACHE_SAVE = current_enable_cache_save;
	}

	/**
	 * MENU HELP
	 * @param current_prompt
	 */
	private void showHelp(int current_prompt) {
		String indent = "\n\t";
		isaConsole.setResultArea("\n***************************************************", current_prompt);
		isaConsole.setResultArea("\n                    ISA Console                    ", current_prompt);
		isaConsole.setResultArea("\n***************************************************", current_prompt);
		isaConsole.setResultArea("\nThis is a multithreaded application. To avoid waiting for the end of a processing \nuse different tab and digit your command:", current_prompt);
		isaConsole.setResultArea("\n\"quit\" or \"q\" " + indent + "exit\n", current_prompt);
		isaConsole.setResultArea("\"indep P_2_1 x P_5_1\" " + indent + "print independent sets table for a grid graph\n", current_prompt);
		isaConsole.setResultArea("\"indep P_2_2 x P_5_e3\" " + indent + "print independent sets table for a grid graph, \'e\' stand for equals, so there is not an inclusion\n", current_prompt);
		isaConsole.setResultArea("\"calc P_2_1 x P_5_1 - P_2_1 x P_5_2\" " + indent	+ "print result of an operation defined between two grid graph independent sets values \n", current_prompt);
		isaConsole.setResultArea("\"print graph P_2_2 x P_5_3\" " + indent + "print graphics of given grid graph\n", current_prompt);
		isaConsole.setResultArea("\"print all P_2_2 x P_5_2\" " + indent + "print all analysis of given grid graph\n", current_prompt);
		isaConsole.setResultArea("\"print test\" " + indent + "print pdf test document\n", current_prompt);
		isaConsole.setResultArea("\"show graphs\" " + indent + "show sintax for all graph families studied\n", current_prompt);
		isaConsole.setResultArea("\"new graph 5 {0,1} {1,2} {3,2} {1,4}\" "	+ indent + " create your favorite graph\n", current_prompt);
		isaConsole.setResultArea("\"pwd\" " + indent + "print current path location\n", current_prompt);
		isaConsole.setResultArea("\"clear\" " + indent + "clear the screen\n", current_prompt);
		isaConsole.setResultArea("\"ls\" " + indent	+ "show all file in current location path\n", current_prompt);
		isaConsole.setResultArea("\"cd <..>\" " + indent + "same as change directory\n", current_prompt);
		isaConsole.setResultArea("\"open <file.pdf>\" " + indent + "open specified pdf document\n", current_prompt);
		isaConsole.setResultArea("\"edit <file.txt>\" " + indent + "edit specified txt document\n", current_prompt);
		isaConsole.setResultArea("\"su - admin\" " + indent	+ "enter as administrator (require password)\n", current_prompt);
		isaConsole.setResultArea("\"exit\" " + indent + "logout from administrator access\n", current_prompt);
		isaConsole.setResultArea("\"new graph 4 {0,1} {0,2} {1,3} {2,3}\" "	+ indent + "create a new graph with 4 vertex and 4 edges\n", current_prompt);
		isaConsole.setResultArea("\"print graph\" " + indent + "print created graph\n", current_prompt);
		isaConsole.setResultArea("\"indep graph\" " + indent + "calculate independent sets of created graph\n", current_prompt);
		isaConsole.setResultArea("\"indep from graph2bis.txt\" " + indent + "calculate independent sets of a graph specified in a text file " + indent + "(classpath:\\it\\unimi\\isa\\graphs\\external)\n", current_prompt);
		isaConsole.setResultArea("\"dyck ++-++-+---\" " + indent + "calcola insiemi stabili del grafo descritto dal cammino di Dyck  \n", current_prompt);
	}

	/**
	 * Metodi di stampa PDF, al termine ripulisce cartelle di progetto.
	 * @param result
	 * @param currentPrompt
	 * @throws Exception 
	 */
	private void checkPrinterOutput(String[] result, int currentPrompt) throws Exception {
		/******************************
		 * command: print test
		 ******************************/
		if ("test".equalsIgnoreCase(result[1])) {
			isaConsole.setResultArea("\nL'operazione richiede un po' di tempo, stampa di test in corso.. " , currentPrompt);
			String message = laTexPrinterV1Impl.printDocument3();
			isaConsole.setResultArea("\n" + message, currentPrompt);
		}
		/******************************
		 * command: print graph P_2_1 x P_5_1
		 ******************************/
		else if ("graph".equalsIgnoreCase(result[1])) {
			
			try {
				//String p1 = result[2];
				// getting graph
				Graph g = Utils.ottieneGrafo(result, 2, 4);
				String message = laTexPrinterV2Impl.printGraphOnly(g);
				isaConsole.setResultArea("\n" + message, currentPrompt);
				
			} catch (IndexOutOfBoundsException ex) {
				// se viene sollevata una eccezione IndexOutOfBoundsException stiamo gestendo grafi iG
				if(iG==null){
					isaConsole.setResultArea("\nCostruire prima un grafo. Es. new graph 4 {0,1} {0,2} {1,3} {2,3}" , currentPrompt);
				}
				else{
					String message = laTexPrinterV2Impl.printGraphOnly(iG);
					isaConsole.setResultArea("\n" + message, currentPrompt);
				}
				
			}
		}
		/******************************
		 * command: print all P_2_1 x P_5_1
		 ******************************/
		else if ("all".equalsIgnoreCase(result[1])) {
			// getting graph
			String p1 = result[2];
			String p2 = result[4];
			String message = laTexPrinterV1Impl.printDocument3(p1, p2);
			isaConsole.setResultArea("\n" + message, currentPrompt);
		}
		
		Thread.sleep(2000);
		
		Utils.pulisciCartellaDaFileTemporanei();
	}

	/**
	 * Mostra contenuto di una cartella del progetto
	 * @param current_prompt
	 */
	private void listFiles(int current_prompt) {
		File folder = new File(this.currpath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName());
				isaConsole.setResultArea("\n-  " + user + "  " + listOfFiles[i].getName(), current_prompt);
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());
				isaConsole.setResultArea("\nd  " + user + "  " + listOfFiles[i].getName(), current_prompt);
			}
		}
	}

	/*
	 * Metodi di gestione comandi
	 */
	public void setCommand(String command) {
		if (prompt == 1) {
			this.command1 = command;
		} else if (prompt == 2) {
			this.command2 = command;
		}
	}

	public String getCommand() {
		if (prompt == 1) {
			return this.command1;
		} else if (prompt == 2) {
			return this.command2;
		}
		return null;
	}

	public void setPrompt(int prompt) {
		this.prompt = prompt;
	}

	public int getPrompt() {
		return prompt;
	}
}
