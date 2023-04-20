package it.unimi.isa.model.graphs.external;

import it.unimi.isa.exceptions.AdeException;
import it.unimi.isa.model.Vertex;
import it.unimi.isa.model.graphs.grid.GridGraphImpl;

import java.util.HashMap;

public class IGraphImpl extends GridGraphImpl{
	
	private String f;
	private int n;
	private boolean circuit;
	
	public IGraphImpl(int v){
		super(v);
		this.setF("igraph");
	}

	@Override
	public String createForLatex() {
		String output =
			"\n\\begin{figure}\n"+ //"\n\\begin{figure}[H]\n"+
			"\t\\centering\n"+
			"\t\t\\begin{tikzpicture}[>=latex',scale=0.8]\n"+
			"\t\t\t\\begin{dot2tex}[tikz] \n"+
			"\t\t\tstrict graph { \n"+
			
			"\t\t\t\tgraph[rankdir=LR, center=true, margin=0.2, nodesep=0.1, ranksep=0.3] \n"+
		    "\t\t\t\tnode[shape=circle, fontname=\"Courier-Bold\", fontsize=10, width=0.4, height=0.4, fixedsize=true] \n"+
		    "\t\t\t\tedge[dir=none, arrowsize=0.6, arrowhead=vee] \n\n"; //dir=none,
		
		for (int v = 0; v < this.V; v++) {
			
			for (int w : adj(v)) {
				output += "\t\t\t\t "+v+" -- "+w+" ; \n"; //[ label = \""+s.toLowerCase()+"\" ]	
				
			}
			
		}		    	

		output += 
			" \n\t\t\t\tlabel=\"this is your graph\"; \n"+
			"\t\t\t} \n"+			
			"\t\t\\end{dot2tex} \n"+
			"\t\\end{tikzpicture} \n"+
			"\\end{figure} \n\n"+
			"";
		
		return output;
	}

	@Override
	public void createIndipMatrix() {
		this.initAdjacencyMatrix();
	}

	@Override
	public void createSchema() throws AdeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int findMaxPotenzaCamminoOrizzontale() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getF() {
		// TODO Auto-generated method stub
		return this.f;
	}

	@Override
	public int getM() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getN() {
		return this.n;
	}

	@Override
	public String getNome() {
		return "my graph";
	}

	@Override
	public String getNomeFile() {
		return "my_graph";
	}

	@Override
	public int[] getPotenzaCamminoOrizzontale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getPotenzaCamminoVerticale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Vertex> getVertex() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setF(String f) {
		this.f = f;
	}

	@Override
	public void setN(int n) {
		this.n = n;
	}

	@Override
	public boolean isCircuit() {
		return this.circuit;
	}

	@Override
	public String createForLatexCircuit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasRicorrenzaLineare() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSistemaLineareCircuiti() {
		// TODO Auto-generated method stub
		return null;
	}
}
