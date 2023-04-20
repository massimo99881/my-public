package it.unimi.isa.model.graphs.circuits;

import it.unimi.isa.exceptions.AdeException;
import it.unimi.isa.utils.Utils;

import java.util.HashMap;
import java.util.List;

public class CircuitAnk extends CircuitGraphImpl {
	
	public CircuitAnk(){}
	
	public CircuitAnk(int n, int m) {
		super(n, m);
		this.n = n;
		this.m = m;
		this.q = this.n - 1;
		this.r = this.m - 1;
		this.f = "A";
	}
	
	@Override
	public String createForLatex() {
		/**
		 * Pezzo di codice che disegna i nodi
		 */
		String tmp = Utils.printNodes(this.r, this.q);
		/**
		 * pezzo di codice che collega i nodi e disegna gli archi
		 */
		String tmp2 = "\\path[every node/.style={font=\\sffamily\\small}] ";
		int _n = this.n;
		int _m = this.m;
		if(_m==1){
			for(int z=1;z<=_n;z++){
				if(z>1){
					tmp2 += "("+(z-1)+") edge [] node[above] {} ("+z+")"; //collego con il precedente
				}
				
			}
			
			for(int z=1;z<=_n;z++){
				for(int k=z;k<=_n;k++){
					if(k!=z && k!=(z-1) && k!=(z+1)){
						tmp2 += "("+(k)+") edge [bend right] node[above] {} ("+z+")"; //*
					}
				}
			}
		}
		else {
			int m = this.r;
			int n = this.q;
			int vertex_n = (m + 1) * (n + 1);
			if (n >= 0) {
				for(int i=0;i<vertex_n;i++){
					for(int j=0;j<vertex_n;j++){
						if(i!=j){
							tmp2 += "("+(i+1)+") edge [bend right] node[above] {} ("+(j+1)+")";
						}
					}
				}
				
			}
		}
		return this.latexScript = tmp + " " + tmp2 + "; \\end{tikzpicture}$$";
	}
	
	/**
	 * Codice che costruisce l'oggetto grafo
	 */
	@Override
	public void createIndipMatrix() {
		int vertex_n = (m) * (n);
		if (n >= 0) {
			for(int i=0;i<vertex_n;i++){
				for(int j=0;j<vertex_n;j++){
					if(i!=j){
						this.addEdge(i, j);
					}
				}
			}
			
		}
		initAdjacencyMatrixSenzaCappi();
	}
	
	private void initAdjacencyMatrixSenzaCappi() {
		
		for (int row = 0; row < V; row++) {
			for (int col = 0; col < V; col++) {
				if (this.adj[row][col] == null || row == col) /*decidiamo di non cosiderare eventuali cappi*/
					this.adj[row][col] = 0;
			}
		}
	}

	@Override
	public void createSchema() throws AdeException
	{
		// TODO Auto-generated method stub
		
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
	public int[] getPotenzaCamminoOrizzontale() {
		return null;
	}

	@Override
	public int[] getPotenzaCamminoVerticale() {
		return null;
	}
	@Override
	public void setN(int n) {
		this.n = n;
		
	}

	@Override
	public boolean isCircuit() {
		return true;
	}

	@Override
	public String createForLatexCircuit() {
		String output =
			"\n\\begin{figure}[H]\n"+
			"\n\t\\centering\n"+
			"\t\t\\begin{dot2tex}[circo,tikz] \n"+
			"\t\t\tstrict graph { \n";
			
		for (int v = 0; v < this.V; v++) {
			for (int w : adj(v)) {
				output += "\t\t\t\t \""+vertexN.get(String.valueOf(v))+"\" -- \""+vertexN.get(String.valueOf(w))+"\"  ; \n"; //[ label = \""+s.toLowerCase()+"\" ]
			}
		}
		output += 
			"\t\t\t} \n"+			
			"\t\t\\end{dot2tex} \n"+
			"\\end{figure} \n\n";
		
		return output;
	}

	@Override
	public String getAutoma(int h,
			HashMap<String, List<String>> sistemaProseguimento) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasRicorrenzaLineare() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getSistemaLineareCircuiti() {
		// TODO Auto-generated method stub
		return null;
	}
}
