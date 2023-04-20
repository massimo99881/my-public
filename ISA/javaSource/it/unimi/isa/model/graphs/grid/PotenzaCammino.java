package it.unimi.isa.model.graphs.grid;

import it.unimi.isa.model.graphs.Graph;


public class PotenzaCammino {
	
//	private static final Logger logger = Logger.getLogger(PotenzaCammino.class);

	public static String creaVerticale(int n, int m, int h){
		
		String edgeStyle = "";
		if(h>1){
			edgeStyle = "[bend right]";
		}
		
		String tmp2 = "";
		int [][] a = new int[n][m];
		for(int x = 0, v=1; x<a.length; x++){
			for(int i=0; i<a[x].length;i++){
				a[x][i]=v;
				v++;
			}
		}
		
		//Utils.print(a);
		
		for(int x = 0; x<a.length; x++){
			for(int i=h; i<a[x].length;i++){
				tmp2 += "\t("+a[x][i]+") edge "+edgeStyle+" node[above] {} ("+(a[x][i-h])+") \n"; //*
				//logger.info( "("+a[x][i]+") edge "+edgeStyle+" node[above] {} ("+(a[x][i-h])+")" ); //*
			}
		}
		return tmp2;
	}
	
	public static void creaVerticale(int n, int m, int h, Graph g){
		
		int [][] a = new int[n][m];
		for(int x = 0, v=0; x<a.length; x++){
			for(int i=0; i<a[x].length;i++){
				a[x][i]=v;
				v++;
			}
		}
		
		//logger.info("stampa mappatura vertici per cammino verticale");
		//Utils.print(a);
		
		for(int x = 0; x<a.length; x++){
			for(int i=h; i<a[x].length;i++){
				g.addEdge(a[x][i], (a[x][i-h]));
				//logger.info(a[x][i] + ", "+ (a[x][i-h]));
			}
		}
	}
	
	public static String creaOrizzontale(int n, int m, int h){
		
		String edgeStyle = "";
		if(h>1){
			edgeStyle = "[bend right]";
		}
		
		String tmp2 = "";
		int [][] a = new int[m][n];
		for(int x = 0, v=1; x<n; x++){
			for(int i=0; i<m;i++){
				a[i][x]=v;
				v++;
			}
		}
		
		//Utils.print(a);
		
		for(int x = 0; x<a.length; x++){
			for(int i=h; i<a[x].length;i++){
				tmp2 += "\t("+a[x][i]+") edge "+edgeStyle+" node[above] {} ("+(a[x][i-h])+") \n"; //*
				//logger.info( "("+a[x][i]+") edge "+edgeStyle+" node[above] {} ("+(a[x][i-h])+")" ); //*
			}
		}
		return tmp2;
	}
	
	public static void creaOrizzontale(int n, int m, int h, Graph g){
		
		int [][] a = new int[m][n];
		for(int x = 0, v=0; x<n; x++){
			for(int i=0; i<m;i++){
				a[i][x]=v;
				v++;
			}
		}
		
		//Utils.print(a);
		
		for(int x = 0; x<a.length; x++){
			for(int i=h; i<a[x].length;i++){
				g.addEdge(a[x][i], (a[x][i-h]));
				//logger.info(a[x][i] + ", "+ (a[x][i-h]));
			}
		}
	}
}
