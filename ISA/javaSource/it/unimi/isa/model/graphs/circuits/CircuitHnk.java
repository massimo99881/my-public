package it.unimi.isa.model.graphs.circuits;

import it.unimi.isa.exceptions.AdeException;
import it.unimi.isa.model.graphs.grid.PotenzaCammino;
import it.unimi.isa.utils.Utils;

import java.util.HashMap;
import java.util.List;

/**
 * I grafi H sono i quasi-circuiti.
 * La potenza del cammino non collega i nodi finali con quelli iniziali
 * L'unico arco è quello che collega il primo nodo con l'ultimo
 * @author Administrator
 *
 */
public class CircuitHnk extends CircuitGraphImpl {

	public CircuitHnk() {
	}
	
	public CircuitHnk(int n, int m, int[] potenzaCamminoVerticale, int[] potenzaCamminoOrizzontale) {
		super(n, m);
		this.n = n;
		this.m = m;
		this.q = this.n - 1;
		this.r = this.m - 1;
		this.f = "H";
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

	}

	@Override
	public boolean isCircuit() {
		return true;
	}

	@Override
	public String createForLatexCircuit() {
		String output =
			"\n\\begin{figure}[H]\n"+
			"\n\t\\centering\n\n"+
			"\t\t\\begin{dot2tex}[circo,tikz] \n"+
			"\t\t\tstrict graph { \n";
			
		for (int v = 0; v < this.V; v++) {
			for (int w : adj(v)) {
				output += "\t\t\t\t \""+vertexN.get(String.valueOf(v))+"\" -- \""+vertexN.get(String.valueOf(w))+"\"  ; \n"; //[ label = \""+s.toLowerCase()+"\" ]
			}
		}
		output += 
			"\t\t\t} \n"+			
			"\t\t\\end{dot2tex} \n\n"+
			"\\end{figure} \n\n";
		
		return output;
	}

	@Override
	public String getAutoma(int h,	HashMap<String, List<String>> sistemaProseguimento) {
		String output = "";
		if(this.m==1){
			if(h==1){
				//è lo stesso automa di p_1_1 x c_n_1
				output +=
				"Questo \\`e l'automa che riconosce (tutte e sole) le stringhe che corrispondono agli insiemi indipendenti di $C_n^{(1)}$: \n\n"+

				"$$\\begin{tikzpicture}[->,>=stealth',shorten >=1pt,auto,node distance=2.cm, \n"+
				"thick,main node/.style={circle,fill=blue!20,draw,font=\\sffamily},main node2/.style={circle,fill=green!20,draw,font=\\sffamily},main node3/.style={circle,fill=yellow!20,draw,font=\\sffamily}] \n\n"+

				"\t	\\node[initial,initial text=,state,accepting] (1) {S}; \n"+
				"\t	\\node[initial text=,state,accepting] (2) [right of=1] {$A$}; \n"+
				"\t	\\node[initial text=,state,accepting] (3) [ below of=1] {$B$};  \n"+
				"\t	\\node[initial text=,state,accepting] (4) [right of=2] {$E_1$}; \n"+
				"\t	\\node[initial text=,state,accepting] (5) [right of=4] {$E_2$}; \n"+
				"\t	\\node[initial text=,state,accepting] (6) [right of=3] {$F_1$}; \n"+
				"\t	\\node[initial text=,state] (7) [right of=6] {$F_{2}$}; \n"+
				"\t	\\node[initial text=,state,accepting] (8) [right of=7] {$G$}; \n\n"+
					
				"\t	\\path[every node/.style={font=\\sffamily\\small}] \n\n"+

				"\t	(1) edge[] node[above] {$e$} (2) \n"+
				"\t	(1) edge[] node[] {$a$} (3) \n"+
				"\t	(2) edge[loop above] node[left=1pt] {e} (2) \n"+
				"\t	(3) edge[] node[below] {$e$} (6) \n"+
				"\t	(6) edge[loop below] node[left=1pt] {e} (6) \n"+
				"\t	(5) edge[bend right] node[above] {$a$} (4) \n"+
				"\t	(5) edge[loop above] node[left=1pt] {e} (5) \n"+
				"\t	(2) edge[] node[] {$a$} (4) \n"+
				"\t	(4) edge[] node[] {$e$} (5) \n"+
				"\t	(6) edge[] node[above] {$a$} (7) \n"+
				"\t	(7) edge[] node[] {$e$} (8) \n"+
				"\t	(8) edge[bend left] node[below] {$a$} (7) \n"+
				"\t	(8) edge[loop above] node[left=1pt] {e} (8); \n\n"+
				
				"\\end{tikzpicture}$$ \n";
			}
			else if(h==2){
				//stampiamo l'automa di p_1_1 x h_n_2
				output += 
					"Ed ecco l'automa che riconosce (tutte e sole) le stringhe che corrispondono agli insiemi indipendenti di $H_2$: \n"+

					"$$\\begin{tikzpicture}[->,>=stealth',shorten >=1pt,auto,node distance=2.cm, \n"+
					"thick,main node/.style={circle,fill=blue!20,draw,font=\\sffamily},main node2/.style={circle,fill=green!20,draw,font=\\sffamily},main node3/.style={circle,fill=yellow!20,draw,font=\\sffamily}] \n"+

					"\t \\node[initial,initial text=,state,accepting] (1) {S}; \n"+
					"\t \\node[initial text=,state,accepting] (2) [above of=1] {$A$}; \n"+
					"\t \\node[initial text=,state,accepting] (3) [right of=1] {$B$};  \n"+
					"\t \\node[initial text=,state,accepting] (4) [right of=2] {$E_1$}; \n"+
					"\t \\node[initial text=,state,accepting] (5) [right of=4] {$F_1$}; \n"+
					"\t \\node[initial text=,state,accepting] (6) [right of=5] {$F_3$}; \n"+
					"\t \\node[initial text=,state,accepting] (7) [right of=6] {$G_1$}; \n"+
					"\t \\node[initial text=,state,accepting] (8) [right of=7] {$E_2$}; \n"+
					"\t \\node[initial text=,state,accepting] (9) [right of=8] {$E_3$}; \n"+

					"\t \\node[initial text=,state,accepting] (10) [right of=3] {$F_2$}; \n"+
					"\t \\node[initial text=,state,accepting] (11) [right of=10] {$F_{4}$}; \n"+
					"\t \\node[initial text=,state] (12) [right of=11] {$G_2$}; \n"+
					"\t \\node[initial text=,state,accepting] (13) [right of=12] {$E_4$}; \n"+
					"\t \\node[initial text=,state,accepting] (14) [right of=13] {$E_5$}; \n"+

					"\t \\path[every node/.style={font=\\sffamily\\small}] \n"+

					"\t (1) edge[] node[above] {$e$} (2) \n"+
					"\t (1) edge[] node[] {$a$} (3) \n"+
					"\t (2) edge[loop above] node[left=1pt] {e} (2) \n"+
					"\t (2) edge[] node[] {$a$} (4) \n"+
					"\t (4) edge[] node[] {$e$} (5) \n"+
					"\t (5) edge[] node[] {$e$} (6) \n"+
					"\t (6) edge[loop above] node[left=1pt] {e} (6) \n"+
					"\t (6) edge[] node[] {$a$} (7) \n"+
					"\t (7) edge[] node[] {$e$} (8) \n"+
					"\t (8) edge[] node[] {$e$} (9) \n"+
					"\t (9) edge[loop above] node[left=1pt] {e} (9) \n"+
					"\t (9) edge[bend right] node[above] {$a$} (7) \n"+
					"\t (3) edge[] node[] {$e$} (10) \n"+
					"\t (10) edge[] node[] {$e$} (11) \n"+
					"\t (11) edge[loop above] node[left=1pt] {e} (11) \n"+
					"\t (11) edge[] node[] {$a$} (12) \n"+
					"\t (12) edge[] node[] {$e$} (13) \n"+
					"\t (13) edge[] node[] {$e$} (14) \n"+
					"\t (14) edge[loop above] node[left=1pt] {e} (14) \n"+
					"\t (14) edge[bend left] node[above] {$a$} (12) \n"+
					"\t ; \n"+

					"\\end{tikzpicture}$$ \n";
			}
		}
		return output;
	}

	@Override
	public boolean hasRicorrenzaLineare() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getSistemaLineareCircuiti() {
		String nome = getNomeForSchema();
		String output = "";
		
		/**
		 * m=1
		 */
		if( "p_1_1 x h_n_1".equalsIgnoreCase(nome)){
			output += 
				"\\bigskip\n\n"+
				"\\noindent\\(\\pmb{\\text{eqs}=\\{}\\\\ \n" +
				"\\pmb{s\\text{==}t*a+t*b+1,}\\\\ \n" +
				"\\pmb{a==t*a+t*\\text{e1}+1,}\\\\ \n" +
				"\\pmb{\\text{e1}==t*\\text{e2}+1,}\\\\ \n" +
				"\\pmb{\\text{e2}==t*\\text{e1}+t*\\text{e2}+1,}\\\\ \n" +
				"\\pmb{b==t*\\text{f1}+1,}\\\\ \n" +
				"\\pmb{\\text{f1}==t*\\text{f1}+t*\\text{f2}+1,}\\\\ \n" +
				"\\pmb{\\text{f2}==t*g,}\\\\ \n" +
				"\\pmb{g==t*\\text{f2}+t*g+1}\\\\ \n" +
				"\\pmb{\\}}\\) \n\n\n" +

				"\\bigskip $$F(t)=\\frac{-1-t+t^3}{-1+t+t^2}$$ \n\n" +

				"\\bigskip $$1+2 t+3 t^2+4 t^3+7 t^4+11 t^5+18 t^6+29 t^7+47 t^8+76 t^9+O[t]^{10}$$ \n\n";
		}
		if("p_1_1 x h_n_2".equalsIgnoreCase(nome)){
			output += 
					"\\bigskip \\bigskip \\noindent\\(\\pmb{\\text{eqs}=\\{}\\\\ \n"+
					"\\pmb{s==t*a+t*b+1,}\\\\ \n"+
					"\\pmb{a==t*a+t*\\text{e1}+1,}\\\\ \n"+
					"\\pmb{\\text{e1}==t*\\text{f1}+1,}\\\\ \n"+
					"\\pmb{\\text{f1}==t*\\text{f3}+1,}\\\\ \n"+
					"\\pmb{\\text{f3}==t*\\text{f3}+t*\\text{g1}+1,}\\\\ \n"+
					"\\pmb{\\text{g1}==t*\\text{e2}+1,}\\\\ \n"+
					"\\pmb{\\text{e2}==t*\\text{e3}+1,}\\\\ \n"+
					"\\pmb{\\text{e3}==t*\\text{e3}+t*\\text{g1}+1,}\\\\ \n"+
					"\\pmb{b==t*\\text{f2}+1,}\\\\ \n"+
					"\\pmb{\\text{f2}==t*\\text{f4}+1,}\\\\ \n"+
					"\\pmb{\\text{f4}==t*\\text{f4}+t*\\text{g2}+1,}\\\\ \n"+
					"\\pmb{\\text{g2}==t*\\text{e4},}\\\\ \n"+
					"\\pmb{\\text{e4}==t*\\text{e5}+1,}\\\\ \n"+
					"\\pmb{\\text{e5}==t*\\text{e5}+t*\\text{g2}+1}\\\\ \n"+
					"\\pmb{\\}}\\) \n\n" +

				"\\bigskip \\bigskip $$F(t)=\\frac{-1-t-t^2+t^4}{-1+t+t^3}$$ \n\n"+

				"\\bigskip \\bigskip $$1+2 t+3 t^2+4 t^3+5 t^4+8 t^5+12 t^6+17 t^7+25 t^8+37 t^9+O[t]^{10}$$ \n\n";
		}
		
		
		return output;
	}
}
