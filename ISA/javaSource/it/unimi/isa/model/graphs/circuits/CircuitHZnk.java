package it.unimi.isa.model.graphs.circuits;

import it.unimi.isa.exceptions.AdeException;
import it.unimi.isa.model.graphs.grid.PotenzaCammino;
import it.unimi.isa.utils.Utils;

import java.util.HashMap;
import java.util.List;

public class CircuitHZnk extends CircuitGraphImpl {

	public CircuitHZnk(int n, int m, int[] potenzaCamminoVerticale, int[] potenzaCamminoOrizzontale) {
		super(n, m);
		this.n = n;
		this.m = m;
		this.q = this.n - 1;
		this.r = this.m - 1;
		this.f = "HZ";
		this.potenzaCamminoVerticale = potenzaCamminoVerticale;
        this.potenzaCamminoOrizzontale = potenzaCamminoOrizzontale;
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
		
		for(int i=0;i<potenzaCamminoVerticale.length;i++){
			tmp2 += PotenzaCammino.creaVerticale(n, m, potenzaCamminoVerticale[i]);
		}
		for(int i=0;i<potenzaCamminoOrizzontale.length;i++){
			tmp2 += PotenzaCammino.creaOrizzontale(n, m, potenzaCamminoOrizzontale[i]);
		}
		
		if(_m==1){
			if(_n>2){
				tmp2 += "(1) edge [bend left] node[above] {} ("+((_n*_m)-(_m-1))+")"; //*
			}
			
		}
		else {
			int m = this.r;
			int i=1;
			for(int z=1;z<=_n;z++){
				int b=_m*z;
				for(; i<b;i++){
					
					if(z>1){						
						tmp2 += "("+(i)+") edge [] node[above] {} ("+(i-(m))+")"; //*
						tmp2 += "("+(i+1)+") edge [] node[above] {} ("+(i-(m+1))+")"; //*
					}
					
				}
				i++;
			}
			tmp2 += "(1) edge [bend left] node[above] {} ("+((_n*_m)-(_m-1))+")"; //*
			tmp2 += "("+_m+") edge [bend right] node[above] {} ("+(_n*_m)+")"; //*
		}
		return this.latexScript = tmp + " " + tmp2 + "; \\end{tikzpicture}$$";
	}
	
	/**
	 * Codice che costruisce l'oggetto grafo
	 */	
	@Override
	public void createIndipMatrix() {
		int m = this.m;
		int n = this.n;
		
		for(int i=0;i<potenzaCamminoVerticale.length;i++){
			PotenzaCammino.creaVerticale(this.n, this.m, potenzaCamminoVerticale[i],this);
		}
		for(int i=0;i<potenzaCamminoOrizzontale.length;i++){
			PotenzaCammino.creaOrizzontale(this.n, this.m, potenzaCamminoOrizzontale[i],this);
		}
		
		if(m==1){
			for(int i=0;i<n;i++){
				if(i==(n-1)){
					this.addEdge(0, i);
				}				
			}
		}
		else {
			m = this.r;
			if (n >= 2) {
				for(int f=1;f<=(n-1);f++){
					int next = (m+1)*f;
					for(int r=next;r<(next+m);r++){
						int new_node = r+1;
						if(r==next){ //
							int t=r-(m+1)+1;
							this.addEdge(r, t);
						}
						if(r!=next && r!=(next+m)){
							int t = r-(m+1)+1;
							this.addEdge(r, t);
							t = r-(m+1)-1;
							this.addEdge(r, t);
						}
						if(new_node==(next+m)){ //
							int t = new_node-(m+1)-1;
							this.addEdge(new_node, t);
						}
					}	
				}
				n--;
				this.addEdge(m,n*(m+1)+m);
				this.addEdge(0,n*(m+1));	
				
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
//		Matrix matrixExample = new Matrix(ml); 
//		//1.definire la dimensione dello schema sigma
//		Sigma sigma = new Sigma(2,1,ml);
//		//2.definire i valori dello schema sigma
//		sigma.setSchemaForGraph("c1");
//		//3.definire le funzioni generatrici per le condizioni al contorno
//		sigma.setFuncGenH(new String[]{"1","1+2x"}); 
//		sigma.setFuncGenV(new String[]{"(1 - x)^-1"});
//		matrixExample.setSigma(sigma);
//		this.indepMatrix = matrixExample.complete(); //Calcolo
//		
//		Ade ade = new Ade(matrixExample, ml);
//		ade.obtainRecurrenceAndGF();
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
}
