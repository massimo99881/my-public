package it.unimi.isa.model.graphs.circuits;

import it.unimi.isa.exceptions.AdeException;
import it.unimi.isa.model.graphs.grid.PotenzaCammino;
import it.unimi.isa.utils.Utils;

import java.util.HashMap;
import java.util.List;

public class CircuitCnk extends CircuitGraphImpl {
	
	public CircuitCnk() {
	}
	
	public CircuitCnk(int n, int m, int[] potenzaCamminoVerticale, int[] potenzaCamminoOrizzontale) {
		super(n, m);
		this.n = n;
		this.m = m;
		this.q = this.n - 1;
		this.r = this.m - 1;
		this.f = "C";
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
		
		//P_2_1 x C_6_1
		for(int i=0;i<potenzaCamminoVerticale.length;i++){
			tmp2 += PotenzaCammino.creaVerticale(n, m, potenzaCamminoVerticale[i]);  //P_2_1
		}
		for(int i=0;i<potenzaCamminoOrizzontale.length;i++){
			tmp2 += PotenzaCamminoCircuito.creaOrizzontale(n, m, potenzaCamminoOrizzontale[i]); //C_6_1
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
	
	@Override
	public String createForLatexCircuit(){
//		String tmp = ""+
//		  "$$\\begin{tikzpicture}[,>=stealth',shorten >=1pt,auto,node distance=1.1 cm, \n"+
//          "thick,main node/.style={circle,draw,font=\\sffamily},main node2/.style={circle,fill=black!30,draw,font=\\sffamily},main node3/.style={circle,fill=yellow!20,draw,font=\\sffamily}] \n"+
//          "\t\\node[main node] (1) {1}; \n"+
//          "\t\\node[main node] (2) [above right of=1] {2}; \n"+
//          "\t\\node[main node] (3) [right of=2] {3}; \n"+
//          "\t\\node[main node] (4) [below right of=3] {4}; \n"+
//          "\t\\node[main node] (5) [below left of=4] {5}; \n"+
//          "\t\\node[main node] (6) [left of=5] {6}; \n"+
//          "\t\\path[every node/.style={font=\\sffamily\\small}] \n"+
//          "\t(1) edge [] node[above] {} (2) \n"+
//          "\t(1) edge [] node[above] {} (6) \n"+
//          "\t(2) edge [] node[above] {} (3) \n"+
//          "\t(3) edge [] node[above] {} (4) \n"+
//          "\t(4) edge [] node[above] {} (5) \n"+
//          "\t(5) edge [] node[above] {} (6); \n"+
//          "\\end{tikzpicture}$$ \n";
		
		
		String output =
			"\n\\begin{figure}[H]\n"+
			"\n\t\\centering\n"+
//			"\t\t\\begin{tikzpicture}[>=latex',scale=0.8]\n"+
			"\t\t\\begin{dot2tex}[circo,tikz] \n"+
			"\t\t\tstrict graph { \n"+
			
//			"\t\t d2tdocpreamble = \" \n"+
//			    "\t\t\t \\usetikzlibrary{arrows, automata, positioning} \n"+
//			    "\t\t\t \\tikzstyle{automaton}=[shorten >=1pt, pos=.4, >=stealth', initial text=] \n"+
//			    "\t\t\t \\tikzstyle{state}=[rectangle, rounded corners] \n"+
//			    "\t\t\t \\tikzstyle{accepting}=[accepting by arrow] \n"+
//			  "\t\t \" \n";
			
			"\t\t\t\tgraph[center=true, margin=0.2, nodesep=0.1, ranksep=0.3] \n"+
		    "\t\t\t\tnode[shape=circle, fontname=\"Courier-Bold\", fontsize=10, width=0.4, height=0.4, fixedsize=true] \n"+
		    "\t\t\t\tedge[dir=none, arrowsize=0.6, arrowhead=vee] \n\n"; //dir=none,
			
//			"\t\t\t node [style=filled, fontcolor=black, fillcolor=white]; \n"+
//			"\t\t\t edge [color=black, label=\"N\"]; \n";
		
//		for (int v = 0; v < this.V; v++) {
//			output += "\t\t\t \""+vertexN.get(String.valueOf(v))+"\" [shape=\"circle\"]; \n";
//		}
		for (int v = 0; v < this.V; v++) {
			
			for (int w : adj(v)) {
				//output += "\t\t\t\t "+v+" -- "+w+" ; \n"; //[ label = \""+s.toLowerCase()+"\" ]	
				output += "\t\t\t\t \""+vertexN.get(String.valueOf(v))+"\" -- \""+vertexN.get(String.valueOf(w))+"\"  ; \n"; //[ label = \""+s.toLowerCase()+"\" ]
			}
			
		}
		output += 
//			" \n\t\t\t\tlabel=\"this is your graph\"; \n"+
			"\t\t\t} \n"+			
			"\t\t\\end{dot2tex} \n"+
//			"\t\\end{tikzpicture} \n"+
			"\\end{figure} \n\n"+
			"";
		
		return output;
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
			PotenzaCamminoCircuito.creaOrizzontale(this.n, this.m, potenzaCamminoOrizzontale[i],this);
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
		
		//this.initAdjacencyMatrix();
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
	public String getAutoma(int h, HashMap<String, List<String>> sistemaProseguimento) {
		String output = "";
		if(this.m==1){
			if(h==1){
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
				output += 
				"L'automa che riconosce (tutte e sole) le stringhe che corrispondono agli insiemi indipendenti di $C_n^{(2)}$ diventa: \n\n"+

				"$$\\begin{tikzpicture}[->,>=stealth',shorten >=1pt,auto,node distance=2.cm, \n"+
				"thick,main node/.style={circle,fill=blue!20,draw,font=\\sffamily},main node2/.style={circle,fill=green!20,draw,font=\\sffamily},main node3/.style={circle,fill=yellow!20,draw,font=\\sffamily}] \n"+

				"\t \\node[initial,initial text=,state,accepting] (1) {S}; \n"+
				"\t \\node[initial text=,state,accepting] (2) [right of=1] {$G_1$}; \n"+
				"\t \\node[initial text=,state,accepting] (3) [right of=2] {$G_2$}; \n"+
				"\t \\node[initial text=,state,accepting] (4) [right of=3] {$G_3$}; \n"+
				"\t \\node[initial text=,state] (5) [right of=4] {$G_4$}; \n"+
				"\t \\node[initial text=,state] (6) [right of=5] {$G_5$}; \n"+

				"\t \\node[initial text=,state,accepting] (7) [below of=2] {$E_1$}; \n"+
				"\t \\node[initial text=,state,accepting] (8) [right of=7] {$E_2$}; \n"+
				"\t \\node[initial text=,state,accepting] (9) [right of=8] {$E_3$}; \n"+
				"\t \\node[initial text=,state] (10) [right of=9] {$E_4$}; \n"+
				"\t \\node[initial text=,state,accepting] (11) [right of=10] {$E_5$}; \n"+

				"\t \\node[initial text=,state,accepting] (12) [below of=8] {$F_1$}; \n"+
				"\t \\node[initial text=,state,accepting] (13) [right of=12] {$F_2$};  \n"+
				"\t \\node[initial text=,state,accepting] (14) [right of=13] {$F_3$}; \n"+
				"\t \\node[initial text=,state,accepting] (15) [right of=14] {$F_4$}; \n"+

				"\t \\path[every node/.style={font=\\sffamily\\small}] \n"+

				"\t (1) edge[] node[] {$a$} (2) \n"+
				"\t (2) edge[] node[] {$e$} (3) \n"+
				"\t (3) edge[] node[] {$e$} (4) \n"+
				"\t (4) edge[loop above] node[left=1pt] {e} (4) \n"+
				"\t (4) edge[] node[] {$a$} (5) \n"+
				"\t (5) edge[] node[] {$e$} (6) \n"+
				"\t (6) edge[bend right] node[above] {$e$} (4) \n"+

				"\t (7) edge[] node[] {$e$} (8) \n"+
				"\t (8) edge[] node[] {$e$} (9) \n"+
				"\t (9) edge[loop above] node[left=1pt] {e} (9) \n"+
				"\t (9) edge[] node[] {$a$} (10) \n"+
				"\t (10) edge[] node[] {$e$} (11) \n"+
				"\t (11) edge[bend right] node[above] {$a$} (9) \n"+

				"\t (12) edge[] node[] {$e$} (13) \n"+
				"\t (13) edge[loop above] node[left=1pt] {e} (13) \n"+
				"\t (13) edge[] node[] {$a$} (14) \n"+
				"\t (14) edge[] node[] {$e$} (15) \n"+
				"\t (15) edge[bend right] node[above] {$e$} (13) \n"+

				"\t (1) edge[bend right] node[above] {$e$} (12) \n"+
				"\t (12) edge[bend right] node[above] {$a$} (7) \n"+
				"\t ; \n"+

				"\\end{tikzpicture}$$ \n";
			}
			else if(h==3){
				output += 
				"L'automa che riconosce (tutte e sole) le stringhe che corrispondono agli insiemi indipendenti di $C_n^{(3)}$ diventa: \n\n"+

				"\n  \\begin{landscape}"+
				"\n  $$\\begin{tikzpicture}[->,>=stealth',shorten >=2pt,auto,node distance=2.2cm, semithick]  "+
				"\n   	\\tikzstyle{every state}=[fill=none,text=black]"+
				"\n  \\node[initial,state,accepting] (q0) at (0,0) {$S$};"+
				"\n  \\node[state,accepting] (q1) [right of=q0]	{$A_1$};"+
				"\n  \\node[state,accepting] (q2) [right of=q1]	{$A_2$};"+
				"\n  \\node[state,accepting] (q3) [right of=q2]	{$A_3$};"+
				"\n  \\node[state,accepting] (q4) [right of=q3]	{$A_4$};"+
				"\n  \\node[state,accepting] (q5) [right of=q4]	{$A_5$};"+
				"\n  \\node[state,accepting] (q6) [right of=q5]	{$A_6$};"+

				"\n  \\node[state,accepting] (p1) at (0,3) {$B_1$};"+
				"\n  \\node[state,accepting] (p2) [right of=p1]	{$B_2$};"+
				"\n  \\node[state,accepting] (p3) [right of=p2]	{$B_3$};"+
				"\n  \\node[state,accepting] (p4) [right of=p3]	{$B_4$};"+
				"\n  \\node[state] (p5) [right of=p4]	{$B_5$};"+
				"\n  \\node[state,accepting] (p6) [right of=p5]	{$B_6$};"+
				"\n  \\node[state,accepting] (p7) [right of=p6]	{$B_7$};"+

				"\n  \\node[state,accepting] (c1) at (0,6)	{$C_1$};"+
				"\n  \\node[state,accepting] (c2) [right of=c1]	{$C_2$};"+
				"\n  \\node[state,accepting] (c3) [right of=c2]	{$C_3$};"+
				"\n  \\node[state,accepting] (c4) [right of=c3]	{$C_4$};"+
				"\n  \\node[state] (c5) [right of=c4]	{$C_5$};"+
				"\n  \\node[state] (c6) [right of=c5]	{$C_6$};"+
				"\n  \\node[state,accepting] (c7) [right of=c6]	{$C_7$};"+

				"\n  \\node[state,accepting] (d1) at (0,9)	{$D_1$};"+
				"\n  \\node[state,accepting] (d2) [right of=d1]	{$D_2$};"+
				"\n  \\node[state,accepting] (d3) [right of=d2]	{$D_3$};"+
				"\n  \\node[state,accepting] (d4) [right of=d3]	{$D_4$};"+
				"\n  \\node[state] (d5) [right of=d4]	{$D_5$};"+
				"\n  \\node[state] (d6) [right of=d5]	{$D_6$};"+
				"\n  \\node[state] (d7) [right of=d6]	{$D_7$};"+


				"\n  \\path "+
				"\n  (q0) edge  [] node {$e$} (q1)"+
				"\n  (q1) edge [] node {$e$} (q2)"+
				"\n  (q2) edge [] node {$e$} (q3)"+
				"\n  (q3) edge  [loop] node {$e$} (q3)"+
				"\n  (q3) edge  [] node {$a$} (q4)"+
				"\n  (q4) edge  [] node {$e$} (q5)"+
				"\n  (q5) edge  [] node {$e$} (q6)"+
				"\n  (q6) edge  [bend left] node {$e$} (q3)"+

				"\n  (q1) edge  [out=180-10,in=180+10] node {$a$} (c1)"+
				"\n  (q2) edge  [] node {$a$} (p1)"+

				"\n  (p1) edge [] node {$e$} (p2)"+
				"\n  (p2) edge [] node {$e$} (p3)"+
				"\n  (p3) edge  [] node {$e$} (p4)"+
				"\n  (p4) edge  [loop] node {$e$} (p4)"+
				"\n  (p4) edge  [] node {$a$} (p5)"+
				"\n  (p5) edge  [] node {$e$} (p6)"+
				"\n  (p6) edge  [] node {$e$} (p7)"+
				"\n  (p7) edge  [bend left] node {$e$} (p4)"+

				"\n  (q0) edge  [out=180-10,in=180+10] node {$a$} (d1)"+

				"\n  (c1) edge [] node {$e$} (c2)"+
				"\n  (c2) edge [] node {$e$} (c3)"+
				"\n  (c3) edge  [] node {$e$} (c4)"+
				"\n  (c4) edge  [loop] node {$e$} (c4)"+
				"\n  (c4) edge  [] node {$a$} (c5)"+
				"\n  (c5) edge  [] node {$e$} (c6)"+
				"\n  (c6) edge  [] node {$e$} (c7)"+
				"\n  (c7) edge  [bend left] node {$e$} (c4)"+

				"\n  (d1) edge [] node {$e$} (d2)"+
				"\n  (d2) edge [] node {$e$} (d3)"+
				"\n  (d3) edge  [] node {$e$} (d4)"+
				"\n  (d4) edge  [loop] node {$e$} (43)"+
				"\n  (d4) edge  [] node {$a$} (d5)"+
				"\n  (d5) edge  [] node {$e$} (d6)"+
				"\n  (d6) edge  [] node {$e$} (d7)"+
				"\n  (d7) edge  [bend left] node {$e$} (d4);"+
				"\n   \\end{tikzpicture}"+
				"\n  $$"+
				"\n  \\end{landscape} \n\n";
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
		if( "p_1_1 x c_n_1".equalsIgnoreCase(nome)){
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
		if("p_1_1 x c_n_2".equalsIgnoreCase(nome)){
			output += 
				"\\bigskip \\bigskip  \\noindent\\(\\pmb{\\text{eqs}=\\{}\\\\ \n"+
				"\\pmb{s\\text{==}t*\\text{f1}+t*\\text{g1}+1,}\\\\ \n"+
				"\\pmb{\\text{f1}==t*\\text{f2}+t*\\text{e1}+1,}\\\\ \n"+
				"\\pmb{\\text{f2}==t*\\text{f2}+t*\\text{f3}+1,}\\\\ \n"+
				"\\pmb{\\text{f3}==t*\\text{f4}+1,}\\\\ \n"+
				"\\pmb{\\text{f4}==t*\\text{f2}+1,}\\\\ \n"+
				"\\pmb{\\text{e1}==t*\\text{e2}+1,}\\\\ \n"+
				"\\pmb{\\text{e2}==t*\\text{e3}+1,}\\\\ \n"+
				"\\pmb{\\text{e3}==t*\\text{e3}+t*\\text{e4}+1,}\\\\ \n"+
				"\\pmb{\\text{e4}==t*\\text{e5},}\\\\ \n"+
				"\\pmb{\\text{e5}==t*\\text{e3}+1,}\\\\ \n" +
				"\\pmb{\\text{g1}==t*\\text{g2}+1,}\\\\ \n"+
				"\\pmb{\\text{g2}==t*\\text{g3}+1,}\\\\ \n"+
				"\\pmb{\\text{g3}==t*\\text{g3}+t*\\text{g4}+1,}\\\\ \n"+
				"\\pmb{\\text{g4}==t*\\text{g5},}\\\\ \n"+
				"\\pmb{\\text{g5}==t*\\text{g3}}\\\\ \n"+
				"\\pmb{\\}}\\) \n\n"+

				"\\bigskip \\bigskip $$F(t)=\\frac{-1-t-t^2+t^4+2 t^5}{-1+t+t^3}$$ \n\n"+

				"\\bigskip \\bigskip $$1+2 t+3 t^2+4 t^3+5 t^4+6 t^5+10 t^6+15 t^7+21 t^8+31 t^9+O[t]^{10}$$ \n\n";
		}
		
		
		return output;
	}
}
