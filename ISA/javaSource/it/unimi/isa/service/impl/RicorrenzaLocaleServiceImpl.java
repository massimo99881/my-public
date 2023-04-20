package it.unimi.isa.service.impl;

import it.unimi.isa.model.SchemaObj;
import it.unimi.isa.service.RicorrenzaLocaleService;
import it.unimi.isa.singleton.Singleton;
import it.unimi.isa.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wolfram.jlink.KernelLink;

@Service
public class RicorrenzaLocaleServiceImpl implements RicorrenzaLocaleService {
	
	private static final Logger logger = Logger.getLogger(RicorrenzaLocaleServiceImpl.class);
	
	@Autowired
	Singleton singleton;
	
	private Integer[][] indepMatrixRicorrenza;
	private List<SchemaObj> savedSchemas;
	
	private int schema [][] = null;	
	private Integer [][] independentMatrix = null;
	private int selectedRow = -1;

	public boolean findLocal(String fgoRsn, List<HashMap<Integer,List<Integer>>> indepList, List<Integer> kList) 
	{
		Integer[][] independentMatrix = Utils.convertIndepHashMapListToMatrix(indepList);
		
		boolean hasRicorrenzaLineare = false;
		logger.info("Cercando ricorrenza locale..");
		logger.info("Recupero denominatore da fgo: " + fgoRsn);
		
		KernelLink ml = singleton.getKernelLink();
		String denominatore = ml.evaluateToInputForm("den1 = Denominator["+fgoRsn+"]", 0);
		logger.info("denominatore: "+denominatore);
		String coeffListDenom = ml.evaluateToInputForm("CoefficientList[den1, x]", 0);
		
		//ottiene i coefficienti in valore assoluto
		long[] coeffList = Utils.getLongAbsArrayFromString(coeffListDenom);
		ArrayUtils.reverse(coeffList);
		Utils.print(coeffList);
		
		//posso gia dire che lo schema sara composto da # di righe
		//schema = new String[righe_schema+1][];
		
		//crea matrice riempiendo di zeri per valori nulli
		indepMatrixRicorrenza = Utils.copyMatrix(independentMatrix);
		logger.info("stampa indepMatrixRicorrenza");
		Utils.print(indepMatrixRicorrenza);	
		
		
		//lista che tiene conto delle somme delle caselle per ciascun tentativo
		//List<Integer> sums = new ArrayList<Integer>();
		//
		//savedSchema = new ArrayList<SchemaObj>();
		
		for(int i=1;i<independentMatrix.length-(coeffList.length); i++){
			
			logger.info("riga selezionata: "+i);
			
			calcola(i, kList, coeffList, independentMatrix);
			
		}
		
		return hasRicorrenzaLineare;
	}
	
	public void calcola(int selectedRow, List<Integer> kList, long[] coeffList, Integer[][] independentMatrix){
		logger.info("selectedRow = "+selectedRow+"; kList="+kList+"; coeffList="+coeffList);
		System.out.println("selectedRow = "+selectedRow+"; kList="+kList+"; coeffList="+coeffList);
		
		this.independentMatrix = independentMatrix;
		
		for(int i=0;i<independentMatrix.length;i++){
			for(int j=0;j<independentMatrix[i].length;j++){
				if(independentMatrix[i][j]==null)
					independentMatrix[i][j]=0;
			}
		}
		
		this.selectedRow = selectedRow;
		
		
//		for(int i=0;i<coeffList.length;i++){
//			int valorecoeff = (int) coeffList[i];
//			calcola2(lunghezza, valorecoeff, kList, i, coeffList);
//		}
		schema = new int[coeffList.length][kList.get(kList.size()-1)+1];
		for(int i=0;i<schema.length;i++)
			for(int j=0;j<schema[i].length;j++)
				schema[i][j]=0;
		System.out.println("schema inizializzato");
		Utils.print(schema);
		
		calcola2((int) coeffList[0], kList, 0, coeffList);
		
	}
	
	public void calcola2(int valorecoeff, List<Integer> kList, int I, long[] coeffList){
		String res = "";
		for(int i=0;i<I;i++){
			res += "\t";
		}
	  	  
		System.out.println(res + "coeff: "+valorecoeff);
		boolean negativeCoeff = false;
		
		if(valorecoeff==0){
			   List<Integer> list = new ArrayList<Integer>();
			   for(int i=0;i<schema[0].length;i++){
				   list.add(0);
			   }
			   ICombinatoricsVector<Integer> perm = Factory.createVector(list);
			   aggiornaSchema(perm,I,res,negativeCoeff);
			   if(I+1<coeffList.length){					        	
		    	  calcola2((int) coeffList[I+1], kList, I+1, coeffList);
		       } 
	    	   else{
	    		  verificaSchema(perm, kList, res);
	    	   }
			   System.out.println();
		   }
		else 
		{
		
			if(valorecoeff<0){
				negativeCoeff = true;
			    valorecoeff = 0-valorecoeff;
			}
			
			// Create an instance of the partition generator to generate all
			// possible partitions of valorecoeff
			Generator<Integer> gen2 = Factory.createPartitionGenerator(valorecoeff);

			// Print the partitions
		   for (ICombinatoricsVector<Integer> p : gen2) {
			  System.out.print(res + "partizione {size: "+p.getSize()+" - ");
			  System.out.print("vector: "+p.getVector()+"} ");
			  System.out.println();
			  
		  	  List<Integer> list = new ArrayList<Integer>();
		      list.addAll(p.getVector());
		      int lunghezza = kList.get(kList.size()-1);
		      for(int i=list.size()-1;i<lunghezza;i++){
		    	  list.add(0);
		      }
			      
			  // Create the initial vector
		      ICombinatoricsVector<Integer> initialVector = Factory.createVector(list);
		      // Create the generator
		      Generator<Integer> generator = Factory.createPermutationGenerator(initialVector);
					      
		      for (ICombinatoricsVector<Integer> perm : generator) {
		    	  System.out.println(res+""+perm);
		    	  aggiornaSchema(perm,I,res,negativeCoeff);
		    	  if(I+1<coeffList.length){					        	
		    		  calcola2((int) coeffList[I+1], kList, I+1, coeffList);
			      } 
		    	  else{
		    		  verificaSchema(perm, kList, res);
		    	  }
		      }
		      System.out.println();
		   }
		   
		}   
	}

	private void verificaSchema(ICombinatoricsVector<Integer> perm, List<Integer> kList, String res) {
		
		//calcola la somma di tutti i valori corrispondenti nella independent matrix
		int sum = 0;
		for(int i=0;i<schema.length-1;i++){
			for(int j=0;j<schema[i].length;j++){
				if(schema[i][j]!=0){
					sum += independentMatrix[selectedRow+i][j]*schema[i][j];
				}
				
			}
		}
		
		if(sum!=0){
			int j = -1;
			for(int i=0;i<perm.getSize();i++)
				if(perm.getValue(i)==1)
					j=i;
			
			int limit = kList.get(selectedRow+schema.length-1);
			
			if(j!=-1 ){ 
				
				boolean limite_superato = false;
				for(int a=0;a<schema.length && !limite_superato;a++){
					for(int b=0;b<schema[a].length && !limite_superato;b++){
						if(schema[a][b]!=0 && b>=limit){
							limite_superato = true;
							System.out.println(res+"limite superato a dx : b="+b+", limit: "+limit);
							
						}
//						if(a==schema.length-1 && schema[a][b]!=0 && b<(limit)){
//							limite_superato = true;
//							
//							System.out.println(res+"limite superato a sx : b="+b+", limit: "+limit);
//						}
					}
				}
				
				if(!limite_superato){
					int valore = independentMatrix[selectedRow+schema.length-1][j];
					if(valore == sum){
						System.out.println(res + "currentRow="+selectedRow+" RICORRENZA TROVATA!!!**************************************************************");
						
						//controlla se è gia presente una matrice uguale
						boolean equals = false;
						SchemaObj f = null;
						for(SchemaObj o : getSavedSchemas()){
							int matrice[][] = o.getSchema();
							equals = Utils.matrixequals(matrice, schema);
							if(equals){
								f = o;
								break;
							}
								
						}
						if(equals){
							//schema gia presente, incrementa contatore
							int count = f.getCount();
							f.setCount(++count);
						}
						else{
							//nuovo schema
							SchemaObj s = new SchemaObj();
							s.setCount(1);
							s.setSchema(schema);
							getSavedSchemas().add(s);
						}
						
					}
				}
			}
		}
	}

	private void aggiornaSchema(ICombinatoricsVector<Integer> perm, int I, String res, boolean negativeCoeff) {
		for(int j=0;j<schema[I].length;j++){
			if(perm.getValue(j)!=0 && negativeCoeff){
				schema[I][j] = 0-perm.getValue(j);
			}
			else 
				schema[I][j] = perm.getValue(j);
		}
		//stampa
		for(int i=0;i<schema.length;i++){
			System.out.print(res+ "");
			for(int j=0;j<schema[i].length;j++){
				System.out.print(schema[i][j]+", ");
			}
			System.out.println();
		}
	}


	public void setSavedSchemas(List<SchemaObj> savedSchemas) {
		this.savedSchemas = savedSchemas;
	}

	public List<SchemaObj> getSavedSchemas() {
		return savedSchemas;
	}

	public List<Integer> initKList(Integer[][] independentMatrix2) {
		List<Integer> kList = new ArrayList<Integer>();
		for(int i=0;i<independentMatrix2.length;i++){
			for(int j=0;j<independentMatrix2[i].length;j++){
				if(independentMatrix2[i][j]==0){
					kList.add(j-1);
					break;
				}
			}
		}
		return kList;
	}
}
