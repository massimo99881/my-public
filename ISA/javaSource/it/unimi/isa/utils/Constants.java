package it.unimi.isa.utils;

public class Constants {
	
	/**
	 * Spring Configuration 
	 */
	final public static String CONFIG_PATH = "classpath*:it/unimi/isa/application-config.xml";

	/**
	 * livello dei log
	 */
	final public static boolean DEBUG_ENABLED = true;
	/**
	 * imposta la dimensione di default della matrice 
	 */
	final public static int MATRIX_DIMENSION = 6; //10
	/**
	 * dimensione array dn
	 */
	final public static int ARRAY_ADE_DIMENSION = 6;
	/**
	 * f.g. Fibonacci
	 */
	final public static String GF_FIBONACCI = "(1 - x - x^2)^-1";
	final public static String GF_EXPONENTIAL = "(1 - 2x)^-1";
	
	/**
	 * messaggi di errore
	 */
	final public static String ERROR_SCHEMA_SETTINGS = "Valorizzare correttamente lo schema sigma. \nEsempio:\n Sigma sigma = new Sigma(2,0);\n sigma.setSchema(\"your_schema_name\"); \n .. \n public void setSchema(String type) throws AdeException { \n .. \n schema[2][0] = \"1\"; \n schema[1][0] = \"1\"; \n schema[0][0] = \"*\";\n ";
	/**
	 * errore funzione generatrice H
	 */
	final public static String ERROR_GEN_FUN_H_DEF = "Errore nella definizione delle funzioni generatrici per le condizioni al contorno H(x)";
	/**
	 * errore funzione generatrice V
	 */
	final public static String ERROR_GEN_FUN_V_DEF = "Errore nella definizione delle funzioni generatrici per le condizioni al contorno V(x)";
	final public static String ERROR_COEFF_NOT_FOUND = "Coefficienti non trovati. Obbligatorio definire almeno una funzione generatrice H(x) e V(x).\nEsempio\n sigma.setFuncGenH(new String[]{\"(1 - x)^-1\"}); //definisco una funzione gen. H_0(x)\n sigma.setFuncGenH(new String[]{\"(1 - x)^-1\",\"(1 - 2x - x^2)^-1\"}); //definisco due funzioni gen. H_0(x) e H_1(x) ";
	final public static String ERROR_EXECUTION_SEQUENCE = "Errore nella sequenza di esecuzione: necessario eseguire prima Ade.obtainRecurrence";
	
	final public static String ERROR_GRAPH_FAMILY_NOT_FOUND ="Famiglia di grafo non trovata";
	
	final public static String ERROR_FILE_OPENING ="Errore in lettura, file gia aperto.";
	final public static String ERROR_FILE_OPENING_PROG = "! I can't write on file ";
	
	/**
	 * Errori generici (non-gestiti)
	 */
	final public static String UNEXPECTED_ERROR_001 = "Errore nella costruzione della tabella. Error code: 001";
	
	/**
	 * extra values for check G.f. of d_n
	 */
	public static  int EXTRA_VALUES = 10;
	
	public static final String LETTERE_LINGUAGGIO = "ABCFGHILMNOPQRSVZKXY";
	
	/**
	 * CONFIGURAZIONI DI SISTEMA:
	 * - USERS
	 * - WIN 32 e 64 bit
	 * - MATHEMATICA VERSION
	 * - MIKTEX VERSION
	 */
	public static final String USER = "ISA"; 
//	public static final String USER = "Administrator.max-PC";
//	public static final String USER = "pc-massimo";
//	public static final String MATHEMATICA_VERSION = "10.2"; 
	public static final String MATHEMATICA_VERSION = "8.0";
//	public static final String bit64 = "x64\\";
	public static final String bit64 = "";
	
	public static final String JLINKDIR = "C:\\Program Files\\wolfram research\\mathematica\\"+MATHEMATICA_VERSION+"\\SystemFiles\\Links\\JLink";
	public static final String EXEC_KERNEL_LINK =  "-linkmode launch -linkname 'C:\\Program Files\\Wolfram Research\\Mathematica\\"+MATHEMATICA_VERSION+"\\MathKernel.exe'";
	
	/**
	 * Percorsi di progetto
	 */
	public static final String PATH_TO_PDF = "C:\\Users\\"+USER+"\\WolframWorkspaces\\ISA";
	public static final String PATH_TO_RESOURCES = "C:\\Users\\"+USER+"\\WolframWorkspaces\\ISA\\resources\\";
	public static final String PATH_TO_ISA = "C:\\Users\\"+USER+"\\WolframWorkspaces\\ISA\\";
	public static final String PATH_TO_TEX = "C:\\Users\\"+USER+"\\WolframWorkspaces\\ISA\\resources\\tex\\temp\\";
	/**
	 * Setting variabile ambiente latex
	 */
	public static final String LATEX_PATH = "latex";
	/**
	 * Oppure usando percorso assoluto
	 */
//	public static final String LATEX_PATH = "C:\\Program Files\\MiKTeX 2.9\\miktex\\bin\\"+bit64+"latex.exe";
//	public static final String LATEX_PATH = "C:\\Program Files (x86)\\MiKTeX 2.9\\miktex\\bin\\"+bit64+"latex.exe";
//	public static final String LATEX_PATH = "C:\\miktex\\miktex\\bin\\latex.exe";
	
	
	/************************************************************************************************************
	 * nome del file tex da generare
	 */
	public static final String TEX_FILE_NAME_RICORRENZE = "ricorrenze.tex";
	public static final String TEX_FILE_NAME_GRAPH = "graph.tex";
	public static String FILENAME_ALL2 = "sample-all2.tex";		
	public static final String PDF_FILE_NAME_GRAPH = "graph.pdf";
	public static final String PDF_FILE_NAME_RICORRENZE = "ricorrenze.pdf";
	
	/*
	 * PASSWORD GESTIONE FILE INTERNI DI PROGETTO (DA SHELL)
	 */
	public static final String USER_PWD = "pallone";
	
	/*
	 * TEMPLATE LATEX
	 */
	public static final String TEX_TEMPLATE_ALL2 = "template-all2.tex";
	public static final String TEX_TEMPLATE_GRAPH = "template-graph.tex";

	/**
	 * Abilita/Disabilita accesso server oeis
	 */
	public static final boolean SLOANE_BIJECTIONS_ENABLED = false;
	
	/**
	 * Carattere inclusione potenza del cammino
	 */
	public static final String CARATTERE_SPECIFICO_CAMMINO = "e";

	/**
	 * Gestione cache
	 */
	public static String PATH_TO_CACHE_INDEP = "resources\\cache\\indep\\";

	public static String PATH_TO_CACHE_TM = "resources\\cache\\tm\\";
	
	/**
	 * Se abilitato, questa variabile evita di far ricalcolare tabelle degli indipendenti con dimensione
	 * n già calcolata e legge il relativo file dalla cache
	 */
	public static boolean ENABLE_CACHE_LOAD = true;
	public static boolean ENABLE_CACHE_SAVE = true;

	/**
	 * controllo corrispondenza somme RS_n
	 */
	public static final boolean CHECK_EQUAL_SUMS = false;
}
