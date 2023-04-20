package it.unimi.isa.model.graphs.grid;

import it.unimi.isa.exceptions.AdeException;
import it.unimi.isa.model.Sigma;
import it.unimi.isa.utils.Utils;

public class GridGraphPnk extends GridGraphImpl {

	public GridGraphPnk(int n, int m, int[] potenzaCamminoVerticale, int[] potenzaCamminoOrizzontale){
		super(n, m);
		this.n = n;
		this.m = m;
		this.f = "P";
		this.q = this.n - 1;
		this.r = this.m - 1;
		this.potenzaCamminoVerticale = potenzaCamminoVerticale;
        this.potenzaCamminoOrizzontale = potenzaCamminoOrizzontale;
	}

	@Override
	public String createForLatex() {
		String tmp = Utils.printNodes(this.r, this.q);
		String tmp2 = "\\path[every node/.style={font=\\sffamily\\small}] \n";
		for(int i=0;i<potenzaCamminoVerticale.length;i++){
			tmp2 += PotenzaCammino.creaVerticale(n, m, potenzaCamminoVerticale[i]);
		}
		for(int i=0;i<potenzaCamminoOrizzontale.length;i++){
			tmp2 += PotenzaCammino.creaOrizzontale(n, m, potenzaCamminoOrizzontale[i]);
		}
		return tmp + "\n" + tmp2 + "; \n\\end{tikzpicture}$$\n\n";
	}
		
	/**
	 * Codice che costruisce l'oggetto grafo
	 */	
	@Override
	public void createIndipMatrix() {
		for(int i=0;i<potenzaCamminoVerticale.length;i++){
			PotenzaCammino.creaVerticale(this.n, this.m, potenzaCamminoVerticale[i],this);
		}
		for(int i=0;i<potenzaCamminoOrizzontale.length;i++){
			PotenzaCammino.creaOrizzontale(this.n, this.m, potenzaCamminoOrizzontale[i],this);
		}
		this.initAdjacencyMatrix();
	}

	@Override
	public void createSchema() throws AdeException{
//		Matrix matrixExample = new Matrix(ml); 
//		//1.definire la dimensione dello schema sigma
//		Sigma sigma = new Sigma(2,1,ml);
//		//2.definire i valori dello schema sigma
//		sigma.setSchema("g1");
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
		return false;
	}

	@Override
	public String createForLatexCircuit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasRicorrenzaLineare() {
		
		ricorrenzaLineare = false;
		String nome = getNomeForSchema();
		String schema [][] = null;
		
		/**
		 * m=1
		 */
		if( "p_1_1 x p_n_1".equalsIgnoreCase(nome)){
			
			schema = new String [3][2];
			schema[2][1] = "{1}"; schema[2][0] = "{0}";
			schema[1][1] = "{0}"; schema[1][0] = "{1}";
			schema[0][1] = "{0}"; schema[0][0] = "{*}";
			
			sigma = new Sigma(schema.length-1, schema[0].length-1,schema);
			sigma.setFuncGenH(new String[]{"1","1+x"});
			sigma.setFuncGenV(new String[]{"(1-x)^-1"});
			
		}
		else if( "p_1_1 x p_n_2".equalsIgnoreCase(nome)){
			schema = new String [4][2];
			schema[3][1] = "{1}"; schema[3][0] = "{0}";
			schema[2][1] = "{0}"; schema[2][0] = "{0}";
			schema[1][1] = "{0}"; schema[1][0] = "{1}";
			schema[0][1] = "{0}"; schema[0][0] = "{*}";
			
			sigma = new Sigma(schema.length-1, schema[0].length-1,schema);
			sigma.setFuncGenH(new String[]{"1","1+x","1+2x"});
			sigma.setFuncGenV(new String[]{"(1-x)^-1"});
			
		}
		else if( "p_1_1 x p_n_3".equalsIgnoreCase(nome)){
			schema = new String [5][2];
			schema[4][1] = "{1}"; schema[4][0] = "{0}";
			schema[3][1] = "{0}"; schema[3][0] = "{0}";
			schema[2][1] = "{0}"; schema[2][0] = "{0}";
			schema[1][1] = "{0}"; schema[1][0] = "{1}";
			schema[0][1] = "{0}"; schema[0][0] = "{*}";
			
			sigma = new Sigma(schema.length-1, schema[0].length-1,schema);
			sigma.setFuncGenH(new String[]{"1","1+x","1+2x","1+3x"});
			sigma.setFuncGenV(new String[]{"(1-x)^-1"});
			
		}
		else if( "p_1_1 x p_n_e3".equalsIgnoreCase(nome)){
			schema = new String [5][2];
			schema[4][1] = "{1}"; schema[4][0] = "{0}";
			schema[3][1] = "{-1}"; schema[3][0] = "{0}";
			schema[2][1] = "{1}"; schema[2][0] = "{0}";
			schema[1][1] = "{0}"; schema[1][0] = "{1}";
			schema[0][1] = "{0}"; schema[0][0] = "{*}";
			
			sigma = new Sigma(schema.length-1, schema[0].length-1,schema);
			sigma.setFuncGenH(new String[]{"1","1+x","1+2x","1+3x+x^2"});
			sigma.setFuncGenV(new String[]{"(1-x)^-1"});
			
		}
		/**
		 * m=2
		 */
		else if( "p_2_1 x p_n_1".equalsIgnoreCase(nome)){
			schema = new String [3][2];
			schema[2][1] = "{1}"; schema[2][0] = "{0}";
			schema[1][1] = "{1}"; schema[1][0] = "{1}";
			schema[0][1] = "{0}"; schema[0][0] = "{*}";
			
			sigma = new Sigma(schema.length-1, schema[0].length-1,schema);
			sigma.setFuncGenH(new String[]{"1","1+2x"});
			sigma.setFuncGenV(new String[]{"(1-x)^-1"});
			
		}
		else if( "p_2_1 x p_n_2".equalsIgnoreCase(nome)){
			schema = new String [5][3];
			schema[4][2] = "{1}"; schema[4][1] = "{0}"; schema[4][0] = "{0}";
			schema[3][2] = "{1}"; schema[3][1] = "{1}"; schema[3][0] = "{0}";
			schema[2][2] = "{0}"; schema[2][1] = "{1}"; schema[2][0] = "{0}";
			schema[1][2] = "{0}"; schema[1][1] = "{0}"; schema[1][0] = "{1}";
			schema[0][2] = "{0}"; schema[0][1] = "{0}"; schema[0][0] = "{*}";
			
			sigma = new Sigma(schema.length-1, schema[0].length-1,schema);
			
			sigma.setFuncGenH(new String[]{"1","1+2x","1+4x+2x^2","1+6x+6x^2"});
			sigma.setFuncGenV(new String[]{"(1-x)^-1","2x/(1 - x)^2"});
			
		}
		
		/**
		 * final check
		 */
		if(schema!=null){
			ricorrenzaLineare = true;
			sigma.setTriangular(true);
		}
		
		
		return ricorrenzaLineare;
	}
	
	@Override
	public String getSistemaLineareCircuiti() {
		// TODO Auto-generated method stub
		return null;
	}
}
