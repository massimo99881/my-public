package it.unimi.isa.service;

import it.unimi.isa.model.Bacchetta;
import it.unimi.isa.model.graphs.Graph;

import java.util.HashMap;
import java.util.List;

public interface LaTexFormatterService {

	public String formatIndependentMatrix(List<HashMap <Integer,List<Integer>>> listIndip, Graph g);
	public String formatIndependentMatrix(String[][] m, Graph g);
	public String formatSistemaLineare(HashMap<String, List<String>> sistema, String letteraFgo);
	public String formatSistemaLineareAutoma(HashMap<String, List<String>> sistema, String letteraFgo, int h);
	public String formatSistemaLineareGrammatica(HashMap<String, List<String>> sistema, String letteraFgo);
	public String formatLettereAlfabeto(List<Bacchetta> alfabeto);
	public String formatFgo(String fgo, String letter, String var);
	public String formatEspansioneInSerie(String coeffList, String type);
	public String formatMultiEspansioneInSerie(HashMap<String, String> espansioniMap);
	public String formatTransferMatrix(String[][] xTM, HashMap<String, List<String>> sistema);
	public String formatProseguimentoSistema(HashMap<String, List<String>> sistema, String letteraFgo);
	public String getSloaneBijections(String sequenza);
	public String formatParagraph(String string);
	public String formatSubSection(String string);
	public String creaSpazio();
	public String formatProseguimentoSistemaCircuito(HashMap<String, List<String>> sistema);
	public String formatListaAdiacenza(Graph g);
	public String getAutoma2(HashMap<String, List<String>> sistema, String statoIniziale,int h,int m);
	public String getAutoma4(int h, HashMap<String,List<String>> sistemaProseguimento);
}
