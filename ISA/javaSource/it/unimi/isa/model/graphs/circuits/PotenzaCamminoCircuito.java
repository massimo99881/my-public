package it.unimi.isa.model.graphs.circuits;

import it.unimi.isa.model.graphs.Graph;
import it.unimi.isa.model.graphs.grid.GridGraphImpl;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class PotenzaCamminoCircuito {

	private static final Logger logger = Logger.getLogger(PotenzaCamminoCircuito.class);

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
			if(h==1){
				for(int i=h; i<a[x].length;i++){
					tmp2 += "\t("+a[x][i]+") edge "+edgeStyle+" node[above] {} ("+(a[x][i-h])+") \n"; //*
					//logger.info( "("+a[x][i]+") edge "+edgeStyle+" node[above] {} ("+(a[x][i-h])+")" ); //*
				}
			}
			else if(h>1){
				int dim = a[x].length;
				HashMap<Integer,Integer> memoEdges = new HashMap<Integer,Integer>();
				for(int i=0; i<dim;i++){
					int dest = i+h;
					if(dest>=dim){
						dest = dest-dim ;
					}
					//controllo che non ci siano archi duplicati..
					if(memoEdges.isEmpty() || 
							(memoEdges.get(a[x][i])==null && memoEdges.get(a[x][dest])==null) ||
							(memoEdges.get(a[x][i])!=null && memoEdges.get(a[x][i])!=a[x][dest] && memoEdges.get(a[x][dest])!=null && memoEdges.get(a[x][dest])!=a[x][i]) || 
							(memoEdges.get(a[x][i])==null && memoEdges.get(a[x][dest])!=null && memoEdges.get(a[x][dest])!=a[x][i])
						){
						tmp2 += "\t("+a[x][i]+") edge "+edgeStyle+" node[above] {} ("+(a[x][dest])+") \n"; //*
						logger.info( "("+a[x][i]+") edge "+edgeStyle+" node[above] {} ("+(a[x][dest])+")" ); //*
						memoEdges.put(a[x][i], a[x][dest]);
					}
						
				}
			}
			
		}
		
		return tmp2;
	}
	
	public static void creaVerticale(int n, int m, int h, GridGraphImpl g){
		
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
			if(h==1){
				for(int i=h; i<a[x].length;i++){
					g.addEdge(a[x][i], (a[x][i-h]));
					//logger.info(a[x][i] + ", "+ (a[x][i-h]));
				}
			}
			else if(h>1){
				int dim = a[x].length;
				HashMap<Integer,Integer> memoEdges = new HashMap<Integer,Integer>();
				for(int i=0; i<dim;i++){
					int dest = i+h;
					if(dest>=dim){
						dest = dest-dim ;
					}
					//controllo che non ci siano archi duplicati..
					if(memoEdges.isEmpty() || 
							(memoEdges.get(a[x][i])==null && memoEdges.get(a[x][dest])==null) ||
							(memoEdges.get(a[x][i])!=null && memoEdges.get(a[x][i])!=a[x][dest] && memoEdges.get(a[x][dest])!=null && memoEdges.get(a[x][dest])!=a[x][i]) || 
							(memoEdges.get(a[x][i])==null && memoEdges.get(a[x][dest])!=null && memoEdges.get(a[x][dest])!=a[x][i])
						){
						g.addEdge(a[x][i], (a[x][dest]));
						//logger.info(a[x][i] + ", "+ (a[x][i-h]));
						memoEdges.put(a[x][i], a[x][dest]);
					}
						
				}
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
			if(h==1){
				for(int i=h; i<a[x].length;i++){
					tmp2 += "\t("+a[x][i]+") edge "+edgeStyle+" node[above] {} ("+(a[x][i-h])+") \n"; //*
					//logger.info( "("+a[x][i]+") edge "+edgeStyle+" node[above] {} ("+(a[x][i-h])+")" ); //*
				}
			}
			else if(h>1){
				int dim = a[x].length;
				HashMap<Integer,Integer> memoEdges = new HashMap<Integer,Integer>();
				for(int i=0; i<dim;i++){
					int dest = i+h;
					if(dest>=dim){
						dest = dest-dim ;
					}
					//controllo che non ci siano archi duplicati..
					if(memoEdges.isEmpty() || 
							(memoEdges.get(a[x][i])==null && memoEdges.get(a[x][dest])==null) ||
							(memoEdges.get(a[x][i])!=null && memoEdges.get(a[x][i])!=a[x][dest] && memoEdges.get(a[x][dest])!=null && memoEdges.get(a[x][dest])!=a[x][i]) || 
							(memoEdges.get(a[x][i])==null && memoEdges.get(a[x][dest])!=null && memoEdges.get(a[x][dest])!=a[x][i])
						){
						tmp2 += "\t("+a[x][i]+") edge "+edgeStyle+" node[above] {} ("+(a[x][dest])+") \n"; //*
						logger.info( "("+a[x][i]+") edge "+edgeStyle+" node[above] {} ("+(a[x][dest])+")" ); //*
						memoEdges.put(a[x][i], a[x][dest]);
					}
						
				}
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
			if(h==1){
				for(int i=h; i<a[x].length;i++){
					g.addEdge(a[x][i], (a[x][i-h]));
					logger.info(a[x][i] + ", "+ (a[x][i-h]));
				}
			}
			else if(h>1 && a[x].length>1){ //TODO da replicare anche per verticale
				int dim = a[x].length;
				HashMap<Integer,Integer> memoEdges = new HashMap<Integer,Integer>();
				for(int i=0; i<dim;i++){
					int dest = i+h;
					if(dest>=dim){
						dest = dest-dim ;
					}
					//controllo che non ci siano archi duplicati..
					if(dest<a[x].length && //TODO da replicare anche per verticale
							( memoEdges.isEmpty() || 
								(memoEdges.get(a[x][i])==null && memoEdges.get(a[x][dest])==null) ||
								(memoEdges.get(a[x][i])!=null && memoEdges.get(a[x][i])!=a[x][dest] && memoEdges.get(a[x][dest])!=null && memoEdges.get(a[x][dest])!=a[x][i]) || 
								(memoEdges.get(a[x][i])==null && memoEdges.get(a[x][dest])!=null && memoEdges.get(a[x][dest])!=a[x][i])
							)
						){
						g.addEdge(a[x][i], (a[x][dest]));
						logger.info(a[x][i] + ", "+ (a[x][dest]));
						memoEdges.put(a[x][i], a[x][dest]);
					}
						
				}
			}
			
		}
	}
}
