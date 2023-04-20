package it.unimi.isa.model.graphs.circuits;

import it.unimi.isa.model.Sigma;
import it.unimi.isa.model.Vertex;
import it.unimi.isa.model.graphs.Graph;
import it.unimi.isa.model.graphs.grid.GridGraphImpl;
import it.unimi.isa.utils.Constants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;


public abstract class CircuitGraphImpl implements Graph {

	private static final Logger logger = Logger.getLogger(GridGraphImpl.class);
	
	//numero di vertici del grafo
	protected int V;
	//mappa per indicizzare i vertici del grafo
	protected HashMap<String,Vertex> vertex ;
	protected HashMap<String,String> vertexN ;
	public HashMap<String, String> getVertexN() {
		return vertexN;
	}

	//numero archi 
	private int E;
	//matrice adiacenza
	protected Integer[][] adj;
	//stringa di stampa del grafo su .tex
	public String latexScript;
	//tabella degli insiemi indipendenti
	public String [][] indepMatrix;
	//memorizza se ha ricorrenza 
	protected boolean ricorrenzaLineare = false;
	//schema della ricorrenza
	protected Sigma sigma;
	
	//initialized by constructor
	
	protected int n;
	protected int m;
	
	//derived properties
	//numero di quadrati del grid graph
	protected int q; 
	//numero di righe di quadrati del grid graph
	protected int r;
	//indica il nome della famiglia di appartenenza (Es. P,C,Z,F..)
	protected String f;
	
	//variabili per la configurazione del grafo griglia
	protected int [] potenzaCamminoVerticale;
	protected int maxPotenzaCamminoVerticale;
	protected String inclusioneCamminoVerticale;
	
	protected int [] potenzaCamminoOrizzontale;
	protected int maxPotenzaCamminoOrizzontale;
	protected String inclusioneCamminoOrizzontale;
	
	public CircuitGraphImpl() {
	}
	
	public CircuitGraphImpl(int n, int m) {
		this.V = n*m;
		if (this.V < 0){
			throw new RuntimeException("Number of vertices must be nonnegative");
		}
		this.vertex = new HashMap<String,Vertex>();
		this.vertexN = new HashMap<String,String>();
		int x = 0;
		for(int c=1;c<=n;c++){
			for(int r=1;r<=m;r++){
				this.vertex.put(r+"."+c, new Vertex(x,r,c));
				this.vertexN.put(String.valueOf(x),r+";"+c);
				x++;
			}			
		}
		//logger.info("Stampa numerazione vertici per disegno");
		//Utils.print(this.vertex);
		this.E = 0;
		this.adj = new Integer[V][V];
	}

	public CircuitGraphImpl(int v2) {
		this.V = v2;
		if (this.V < 0){
			throw new RuntimeException("Number of vertices must be nonnegative");
		}
		this.vertex = new HashMap<String,Vertex>();
		this.E = 0;
		this.adj = new Integer[V][V];
	}

	// number of vertices and edges
	public int V() {
		return V;
	}

	public int E() {
		return E;
	}

	// add undirected edge v-w
	public void addEdge(int v, int w) {
		if (adj[v][w] == null || adj[v][w] == 0)
			E++;
		adj[v][w] = 1;
		adj[w][v] = 1;
	}

	// does the graph contain the edge v-w?
	public Integer contains(Integer v, Integer w) {
		return adj[v][w];
	}

	// return list of neighbors of v
	public Iterable<Integer> adj(int v) {
		return new AdjIterator(v);
	}
	
	// string representation of Graph - takes quadratic time
	public String toString() {
		String NEWLINE = System.getProperty("line.separator");
		StringBuilder s = new StringBuilder();
		s.append(V + " vertex and " + E + " edges " + NEWLINE);
		for (int v = 0; v < V; v++) {
			s.append(v + ": ");
			for (int w : adj(v)) {
				s.append(w + " ");
			}
			s.append(NEWLINE);
		}
		return s.toString();
	}

	public void initAdjacencyMatrix() {
		//logger.info("inizializzazione valori matrice adiacenza");
		for (int row = 0; row < V; row++) {
			for (int col = 0; col < V; col++) {
				if (adj[row][col] == null)
					adj[row][col] = 0;
			}
		}
	}

	public void initAndPrintAdjacencyMatrix4Wolfram() {
		String tempString = "";
		
		tempString += "---------adiacency matrix 4 wolfram--------------------\n";
		tempString += "{ \n";
		for (int row = 0; row < V; row++) {
			tempString += "{ \n";
			for (int col = 0; col < V; col++) {
				if (adj[row][col] == null)
					adj[row][col] = 0;
				if(col==(V-1))
					tempString += adj[row][col];
				else
					tempString += adj[row][col] + ", ";
			}
			if(row==(V-1))
				tempString += "} \n";
			else
				tempString += "}, \n";
		}// end for
		tempString += "} \n";
		tempString += "-----------------------------\n";
		
		logger.info(tempString);
	}

	
	/**
	 * support iteration over graph vertices
	 * @author Administrator
	 *
	 */
	private class AdjIterator implements Iterator<Integer>, Iterable<Integer> {
		int v, w = 0;

		AdjIterator(int v) {
			this.v = v;
		}

		public Iterator<Integer> iterator() {
			return this;
		}

		public boolean hasNext() {
			while (w < V) {
				if (adj[v][w] != null && adj[v][w] == 1)
					return true;
				w++;
			}
			return false;
		}

		public Integer next() {
			if (hasNext()) {
				return w++;
			} else {
				throw new NoSuchElementException();
			}
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	protected String findInclusionePotenzaCammino(int[] potenzaCammino, int maxPotenzaCammino){
		String carattere = Constants.CARATTERE_SPECIFICO_CAMMINO;
		int maxPotenzaCamminoTemp = maxPotenzaCammino;
		int potenzaPresente = 0;
		while(maxPotenzaCamminoTemp>=0){
			for(int i=0;i<potenzaCammino.length;i++){
				if(potenzaCammino[i]==maxPotenzaCamminoTemp){
					potenzaPresente ++;
				}
			}
			maxPotenzaCamminoTemp -- ;
		}
		if(/*maxPotenzaCammino>2 && */potenzaPresente == maxPotenzaCammino )
		{
			carattere = "";
		}		
		return carattere;
	}

	protected int findMaxPotenzaCammino(int[] potenzaCammino) {
		int max = 0;
		for(int i=0;i<potenzaCammino.length;i++){
			if(potenzaCammino[i]>max){
				max = potenzaCammino[i];
			}
		}
		return max;
	}
	
	@Override
	public String getNome() {
		maxPotenzaCamminoVerticale = findMaxPotenzaCammino(potenzaCamminoVerticale);
        maxPotenzaCamminoOrizzontale = findMaxPotenzaCammino(potenzaCamminoOrizzontale);
        inclusioneCamminoVerticale = findInclusionePotenzaCammino(potenzaCamminoVerticale,maxPotenzaCamminoVerticale);
        inclusioneCamminoOrizzontale = findInclusionePotenzaCammino(potenzaCamminoOrizzontale,maxPotenzaCamminoOrizzontale);
		String nomeCamminoVerticale = "P_"+m+"^{("+inclusioneCamminoVerticale+" "+maxPotenzaCamminoVerticale+")}";
		String nomeCamminoOrizzontale = this.f + "_"+n+"^{("+inclusioneCamminoOrizzontale+" "+maxPotenzaCamminoOrizzontale+")}";
		return "Grafo $"+nomeCamminoVerticale+"\\times "+nomeCamminoOrizzontale+"$ ";
	}
	
	@Override
	public String getNomeFile(){
		maxPotenzaCamminoVerticale = findMaxPotenzaCammino(potenzaCamminoVerticale);
        maxPotenzaCamminoOrizzontale = findMaxPotenzaCammino(potenzaCamminoOrizzontale);
        inclusioneCamminoVerticale = findInclusionePotenzaCammino(potenzaCamminoVerticale,maxPotenzaCamminoVerticale);
        inclusioneCamminoOrizzontale = findInclusionePotenzaCammino(potenzaCamminoOrizzontale,maxPotenzaCamminoOrizzontale);
		return "Grafo_P_"+m+"^("+inclusioneCamminoVerticale+" "+maxPotenzaCamminoVerticale+")x"+this.f+"_"+n+"^("+inclusioneCamminoOrizzontale+" "+maxPotenzaCamminoOrizzontale+")";
	}
	
	@Override
	public String getNomeFileCache(){
		maxPotenzaCamminoVerticale = findMaxPotenzaCammino(potenzaCamminoVerticale);
        maxPotenzaCamminoOrizzontale = findMaxPotenzaCammino(potenzaCamminoOrizzontale);
        inclusioneCamminoVerticale = findInclusionePotenzaCammino(potenzaCamminoVerticale,maxPotenzaCamminoVerticale);
        inclusioneCamminoOrizzontale = findInclusionePotenzaCammino(potenzaCamminoOrizzontale,maxPotenzaCamminoOrizzontale);
		return "P_"+m+"_"+inclusioneCamminoVerticale+""+maxPotenzaCamminoVerticale+" x "+this.f+"_"+n+"_"+inclusioneCamminoOrizzontale+""+maxPotenzaCamminoOrizzontale+"";
	}
	
	@Override
	public int findMaxPotenzaCamminoOrizzontale(){
		return findMaxPotenzaCammino(potenzaCamminoOrizzontale);
	}
	
	@Override
	public HashMap<String, Vertex> getVertex() {
		return this.vertex;
	}
	
	public String[][] getIndepMatrix() {
		return indepMatrix;
	}
	
	@Override
	public String getF() {
		return this.f;
	}

	@Override
	public int getM() {
		return this.m;
	}

	@Override
	public int getN() {
		return this.n;
	}

	@Override
	public int[] getPotenzaCamminoVerticale() {
		return potenzaCamminoVerticale;
	}

	@Override
	public int[] getPotenzaCamminoOrizzontale() {
		return potenzaCamminoOrizzontale;
	}
	
	@Override
	public void setN(int n) {
		this.n = n;
		
	}
	
	@Override
	public Sigma getSigma(){
		return this.sigma;
	}
	
	public String getSistemaLineareCircuiti(){
		return null;
	}
	
	public String getNomeForSchema(){
		maxPotenzaCamminoVerticale = findMaxPotenzaCammino(potenzaCamminoVerticale);
        maxPotenzaCamminoOrizzontale = findMaxPotenzaCammino(potenzaCamminoOrizzontale);
        inclusioneCamminoVerticale = findInclusionePotenzaCammino(potenzaCamminoVerticale,maxPotenzaCamminoVerticale);
        inclusioneCamminoOrizzontale = findInclusionePotenzaCammino(potenzaCamminoOrizzontale,maxPotenzaCamminoOrizzontale);
		return "P_"+m+"_"+inclusioneCamminoVerticale+""+maxPotenzaCamminoVerticale+" x "+this.f+"_n_"+inclusioneCamminoOrizzontale+""+maxPotenzaCamminoOrizzontale+"";
	}
}
