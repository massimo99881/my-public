package it.unimi.isa.model.graphs.grid;

import it.unimi.isa.exceptions.AdeException;
import it.unimi.isa.model.Sigma;
import it.unimi.isa.utils.Utils;

public class GridGraphZnk extends GridGraphImpl {
	
	public GridGraphZnk(int n, int m, int[] potenzaCamminoVerticale, int[] potenzaCamminoOrizzontale) {
		super(n, m);
		this.n = n;
		this.m = m;
		this.q = this.n - 1;
		this.r = this.m - 1;
		this.f = "Z";
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
	
		return this.latexScript = tmp + " " + tmp2 + "; \\end{tikzpicture}$$";
	}

	@Override
	public void createSchema() throws AdeException 
	{
		// TODO Auto-generated method stub
		
	}

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
			
		}

		this.initAdjacencyMatrix();
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
		if( "p_1_1 x z_n_1".equalsIgnoreCase(nome)){
			
			schema = new String [3][2];
			schema[2][1] = "{1}"; schema[2][0] = "{0}";
			schema[1][1] = "{0}"; schema[1][0] = "{1}";
			schema[0][1] = "{0}"; schema[0][0] = "{*}";
			
			sigma = new Sigma(schema.length-1, schema[0].length-1,schema);
			sigma.setFuncGenH(new String[]{"1","1+x"});
			sigma.setFuncGenV(new String[]{"(1-x)^-1"});
			
		}
		else if( "p_1_1 x z_n_2".equalsIgnoreCase(nome)){
			schema = new String [4][2];
			schema[3][1] = "{1}"; schema[3][0] = "{0}";
			schema[2][1] = "{0}"; schema[2][0] = "{0}";
			schema[1][1] = "{0}"; schema[1][0] = "{1}";
			schema[0][1] = "{0}"; schema[0][0] = "{*}";
			
			sigma = new Sigma(schema.length-1, schema[0].length-1,schema);
			sigma.setFuncGenH(new String[]{"1","1+x","1+2x"});
			sigma.setFuncGenV(new String[]{"(1-x)^-1"});
			
		}
		else if( "p_1_1 x z_n_3".equalsIgnoreCase(nome)){
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
		else if( "p_1_1 x z_n_e3".equalsIgnoreCase(nome)){
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
		if( "p_2_1 x z_n_1".equalsIgnoreCase(nome)){
			schema = new String [3][2];
			schema[2][1] = "{2}"; schema[2][0] = "{0}";
			schema[1][1] = "{0}"; schema[1][0] = "{1}";
			schema[0][1] = "{0}"; schema[0][0] = "{*}";
			
			sigma = new Sigma(schema.length-1, schema[0].length-1,schema);
			
			sigma.setFuncGenH(new String[]{"1","1+2x"});  //fgo per le righe
			sigma.setFuncGenV(new String[]{"(1-x)^-1"}); //fgo della colonna
		}
		else if( "p_2_1 x z_n_2".equalsIgnoreCase(nome)){
			schema = new String [4][2];
			schema[3][1] = "{1}"; schema[3][0] = "{0}";
			schema[2][1] = "{1}"; schema[2][0] = "{0}";
			schema[1][1] = "{0}"; schema[1][0] = "{1}";
			schema[0][1] = "{0}"; schema[0][0] = "{*}";
			
			sigma = new Sigma(schema.length-1, schema[0].length-1,schema);
			
			sigma.setFuncGenH(new String[]{"1","1+2x","1+4x"}); 
			sigma.setFuncGenV(new String[]{"(1-x)^-1"});
		}
		/**
		 * m=3
		 */
		else if( "p_3_1 x z_n_1".equalsIgnoreCase(nome)){
			schema = new String [4][4];
			schema[3][3] = "{-1}"; schema[3][2] = "{-1}"; schema[3][1] = "{0}"; schema[3][0] = "{0}";
			schema[2][3] = "{0}"; schema[2][2] = "{1}"; schema[2][1] = "{2}"; schema[2][0] = "{0}";
			schema[1][3] = "{0}"; schema[1][2] = "{0}"; schema[1][1] = "{1}"; schema[1][0] = "{1}";
			schema[0][3] = "{0}"; schema[0][2] = "{0}"; schema[0][1] = "{0}"; schema[0][0] = "{*}";
			
			sigma = new Sigma(schema.length-1, schema[0].length-1,schema);
			//TODO
//			sigma.setFuncGenH(new String[]{"1","1+3x+1x^2","1+6x+4x^2"}); 
//			sigma.setFuncGenV(new String[]{"(1-x)^-1","3x/(1-x)^2","(1+x+7x^2)/(1-x)^3"});
		}
		
		/**
		 * final check
		 */
		if(schema!=null){
			ricorrenzaLineare = true;
		}

		return ricorrenzaLineare;
	}

	@Override
	public String getSistemaLineareCircuiti() {
		// TODO Auto-generated method stub
		return null;
	}
}
