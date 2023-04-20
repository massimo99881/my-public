package it.unimi.isa.model.graphs;

import it.unimi.isa.exceptions.AdeException;
import it.unimi.isa.model.Sigma;
import it.unimi.isa.model.Vertex;

import java.util.HashMap;
import java.util.List;

public interface Graph {
	public void setN(int n);
	public int V();
	public int E();
	public String getF();
	public int getN();
	public int getM();
	public void addEdge(int v, int w);
	public Integer contains(Integer v, Integer w);
	public Iterable<Integer> adj(int v);
	public String toString();
	public void initAdjacencyMatrix();
	public void initAndPrintAdjacencyMatrix4Wolfram();
	public boolean hasRicorrenzaLineare();
	public String[][] getIndepMatrix();
	public void createSchema() throws AdeException;
	public HashMap<String, Vertex> getVertex() ;
	
	public void createIndipMatrix();
	public String createForLatex();
	public String getNome();
	public String getNomeFile();
	public Sigma getSigma();
	public int findMaxPotenzaCamminoOrizzontale();
	public int[] getPotenzaCamminoVerticale();
	public int[] getPotenzaCamminoOrizzontale();
	public boolean isCircuit();
	public String createForLatexCircuit();
	public HashMap<String, String> getVertexN();
	public String getAutoma(int h, HashMap<String, List<String>> sistemaProseguimento);
	public String getNomeFileCache();
	public String getSistemaLineareCircuiti();
	
}
