package it.unimi.isa.model.graphs;

import it.unimi.isa.exceptions.GraphException;
import it.unimi.isa.model.graphs.circuits.CircuitAnk;
import it.unimi.isa.model.graphs.circuits.CircuitCFnk;
import it.unimi.isa.model.graphs.circuits.CircuitCZnk;
import it.unimi.isa.model.graphs.circuits.CircuitCnk;
import it.unimi.isa.model.graphs.circuits.CircuitHFnk;
import it.unimi.isa.model.graphs.circuits.CircuitHZnk;
import it.unimi.isa.model.graphs.circuits.CircuitHnk;
import it.unimi.isa.model.graphs.circuits.CircuitMnk;
import it.unimi.isa.model.graphs.grid.GridGraphFnk;
import it.unimi.isa.model.graphs.grid.GridGraphPnk;
import it.unimi.isa.model.graphs.grid.GridGraphZnk;
import it.unimi.isa.utils.Constants;

public class GraphFactory {
	
	public static Graph getGraph(int n, int m, String f, int[] potenzaCamminoVerticale, int[] potenzaCamminoOrizzontale) throws GraphException
	{
		if ( f.equalsIgnoreCase("p") ){
	    	return new GridGraphPnk(n,m,potenzaCamminoVerticale,potenzaCamminoOrizzontale);
	    }
		else if ( f.equalsIgnoreCase("z") ){
	    	return new GridGraphZnk(n,m,potenzaCamminoVerticale,potenzaCamminoOrizzontale);
	    }
	    else if ( f.equalsIgnoreCase("f") ){
	    	return new GridGraphFnk(n,m,potenzaCamminoVerticale,potenzaCamminoOrizzontale);
	    }    
	  
//	    /**
//		 * circuiti
//		 */
	    else if ( f.equalsIgnoreCase("c") ){
	    	return new CircuitCnk(n,m,potenzaCamminoVerticale,potenzaCamminoOrizzontale);
	    }
	    
	    else if ( f.equalsIgnoreCase("h") ){
	    	return new CircuitHnk(n,m,potenzaCamminoVerticale,potenzaCamminoOrizzontale);
	    }
		
	    else if ( f.equalsIgnoreCase("m") ){
	    	return new CircuitMnk(n,m,potenzaCamminoVerticale,potenzaCamminoOrizzontale);
	    }
	   
	    else if ( f.equalsIgnoreCase("cz") ){
	    	return new CircuitCZnk(n,m,potenzaCamminoVerticale,potenzaCamminoOrizzontale);
	    }
		
	    else if ( f.equalsIgnoreCase("hz") ){
	    	return new CircuitHZnk(n,m,potenzaCamminoVerticale,potenzaCamminoOrizzontale);
	    }
		
	    else if ( f.equalsIgnoreCase("cf") ){
	    	return new CircuitCFnk(n,m,potenzaCamminoVerticale,potenzaCamminoOrizzontale);
	    }
		
	    else if ( f.equalsIgnoreCase("hf") ){
	    	return new CircuitHFnk(n,m,potenzaCamminoVerticale,potenzaCamminoOrizzontale);
	    }
		
	    else if ( f.equalsIgnoreCase("a") ){
	    	return new CircuitAnk(n,m);
	    }
		
		/**
		 * Igraph
		 */
	    else if(f.equalsIgnoreCase("igraph")){
	    	System.out.println("GridGraphFactory: igraph not supported here.");
	    	return null;
	    }
		
	    throw new GraphException(Constants.ERROR_GRAPH_FAMILY_NOT_FOUND);
	  }
}
