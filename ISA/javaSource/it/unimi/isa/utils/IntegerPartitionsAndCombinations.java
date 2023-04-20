package it.unimi.isa.utils;

import java.util.ArrayList;
import java.util.List;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

public class IntegerPartitionsAndCombinations {

	/**
	 * questo è il primo passo:
	 * riassume tutti i possibili modi di distribuire le partizioni di interi che compongono il coefficiente del denominatore
	 * preso in considerazione
	 * Per ciascuna combinazione ottenuta si dovra procedere ricorsivamente sui possibili modi di distribuire le partizioni
	 * degli interi che compongono il coefficiente successivo del denominatore.
	 * @param args
	 */
	public static void main(String args[]){
		//definisco la lunghezza della riga della casella del coefficiente preso in considerazione
		int lunghezza = 5;
		int valorecoeff = 3;
		System.out.println("numero di caselle presenti nella riga: "+lunghezza);
		System.out.println("numero da partizionare: "+valorecoeff);
		System.out.println();
		
		// Create an instance of the partition generator to generate all
		   // possible partitions of valorecoeff
		   Generator<Integer> gen2 = Factory.createPartitionGenerator(valorecoeff);

		   // Print the partitions
		   for (ICombinatoricsVector<Integer> p : gen2) {
			  System.out.print("partizione {size: "+p.getSize()+" - ");
			  System.out.print("vector: "+p.getVector()+"} ");
			  System.out.println();
			  
			  	  List<Integer> list = new ArrayList<Integer>();
			      list.addAll(p.getVector());
			      for(int i=list.size()-1;i<lunghezza;i++){
			    	  list.add(0);
			      }
			      
			      	
					   // Create the initial vector
					      ICombinatoricsVector<Integer> initialVector = Factory.createVector(list);
		
					      // Create the generator
					      Generator<Integer> generator = Factory.createPermutationGenerator(initialVector);
		
					      for (ICombinatoricsVector<Integer> perm : generator) {
					         System.out.println("\t"+perm);
					      }
			   
			   
		      //System.out.print(p);
		      System.out.println();
		   }
	}
	
	
}
