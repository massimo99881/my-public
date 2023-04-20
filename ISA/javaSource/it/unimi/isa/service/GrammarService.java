package it.unimi.isa.service;

import it.unimi.isa.exceptions.GrammarException;
import it.unimi.isa.exceptions.GraphException;
import it.unimi.isa.model.Bacchetta;
import it.unimi.isa.model.GrammarLanguage;
import it.unimi.isa.model.graphs.Graph;

import java.util.HashMap;
import java.util.List;

public interface GrammarService {

	public String[][] ottieneTransferMatrix(HashMap <String,List<String>> sistema);
	public HashMap <String,List<String>> ottieneSistemaLineareLinguaggio(HashMap <String,List<String>> sistemaProseguimento, Graph g);
	public String ottieneFgoGrammatica(HashMap<String, List<String>> hash);
	public String ottieniEspansioneInSerieFunzione(String fgo, String var);
	public String ottieniFunzioneGenGrammatica(String result);
	public String risolviSistemaLineareGrammatica(String math);
	public String creaSistemaLineareGrammatica(HashMap<String, List<String>> hash);
	public GrammarLanguage ottieneParoleConsentite(List<Bacchetta> alfabeto, Graph g) throws GraphException, GrammarException;
	public List<Bacchetta> ottieneLettereAlfabeto(Graph g) throws GraphException, GrammarException;
}
