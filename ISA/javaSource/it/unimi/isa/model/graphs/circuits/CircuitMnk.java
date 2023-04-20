package it.unimi.isa.model.graphs.circuits;

import it.unimi.isa.exceptions.AdeException;
import it.unimi.isa.model.graphs.grid.PotenzaCammino;
import it.unimi.isa.utils.Utils;

import java.util.HashMap;
import java.util.List;

/**
 * Moebius Graphs
 * @author Administrator
 *
 */
public class CircuitMnk extends CircuitGraphImpl {
	
	public CircuitMnk(int n, int m, int[] potenzaCamminoVerticale, int[] potenzaCamminoOrizzontale) {
		super(n, m);
		this.n = n;
		this.m = m;
		this.q = this.n - 1;
		this.r = this.m - 1;
		this.f = "M";
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
			tmp2 += "(1) edge [bend left] node[above] {} ("+(_n*_m)+")"; //*
			tmp2 += "("+_m+") edge [bend right] node[above] {} ("+((_n*_m)-(_m-1))+")"; //*
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
				n--;
				this.addEdge(0,n*(m+1)+m);
				this.addEdge(m,n*(m+1));
			}
		}
		
		/**############################################
		 *   ISOMORFISMO CON LA SEGUENTE COSTRUZIONE 
		 * ############################################
		 *  int g_n = n;
			if (n >= 0) {
				//G.addEdge(0, 1); //si parte da una barretta verticale
				for (int c=0; c<m; c++){
					this.addEdge(c, c+1);
				}
			}
			if (n >= 1) {
				for(int f=1;f<=n;f++){
					int next = (m+1)*f;
					for(int r=next;r<(next+m);r++){
						int new_node = r+1;
						this.addEdge(r, new_node); //aggiungi barretta verticale
						this.addEdge(r-(m+1), r); //collego i nodi precedenti
						if(new_node==(next+m)){
							this.addEdge(new_node-(m+1), new_node); //collego i nodi precedenti
						}
						
						//l'avevo nomimata famiglia "U"
						if(f==g_n){
							if(r==next){ //
								int t=r-((m+1)*g_n)+1;
								this.addEdge(r, t);
							}
							if(r!=next && r!=(next+m)){
								int t = r-((m+1)*g_n)+1;
								this.addEdge(r, t);
								t = r-((m+1)*g_n)-1;
								this.addEdge(r, t);
							}
							if(new_node==(next+m)){ //
								int t = new_node-((m+1)*g_n)-1;
								this.addEdge(new_node, t);
							}
						}
					}	
				}				
			}
			
		 */
		
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
