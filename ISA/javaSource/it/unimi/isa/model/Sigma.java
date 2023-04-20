package it.unimi.isa.model;

import it.unimi.isa.exceptions.AdeException;
import it.unimi.isa.utils.Constants;
import it.unimi.isa.utils.Utils;

import org.apache.log4j.Logger;

import com.wolfram.jlink.KernelLink;

public class Sigma {
	
	private static final Logger logger = Logger.getLogger(Sigma.class);
	
	int h = 0;
	int v = 0;
	private String schema [][];
	private String[] funcGenH; //H0 è la funzione generatrice per la prima riga dello schema
	private String[] funcGenV; //V0 è la funzione generatrice per la prima colonna dello schema
	private String coeffArrayH[][];
	private String coeffArrayV[][];

	private boolean isTriangular = false;
	
	public void setTriangular(boolean isTriangular) {
		this.isTriangular = isTriangular;
	}

	public Sigma() {
	}
	
	public Sigma(int h, int v, String [][] schema){
		this.h=h;
		this.v=v;
		
		this.schema = schema;
		
		//condizioni al contorno sono date dalle funzioni generatrici:
		funcGenH = new String [h];
		funcGenV = new String [v];
		
		coeffArrayH = new String[h][v];
		coeffArrayV = new String[v][h];
	}
	
	public Sigma(int h, int v, String type) throws AdeException{
		this.h=h;
		this.v=v;
		
		schema = new String [h+1][v+1];
		
		//condizioni al contorno sono date dalle funzioni generatrici:
		funcGenH = new String [h];
		funcGenV = new String [v];
		
		coeffArrayH = new String[h][v];
		coeffArrayV = new String[v][h];
		
		setSchema(type);
	}
	
	public void setSchema(String type) throws AdeException {
		try {
			if("Test1".equals(type)){
				schema[1][1] = "{1,a}"; schema[1][0] = "{1,0}";
				schema[0][1] = "{0,0}"; schema[0][0] = "{*}";
				isTriangular = true; //TODO risolvere una questione importante .. 08082015
			}
			if("provanuova".equals(type) || "g1".equals(type) || "c1".equals(type)){
				schema[2][1] = "{1}"; schema[2][0] = "{0}";
				schema[1][1] = "{1}"; schema[1][0] = "{1}";
				schema[0][1] = "{0}"; schema[0][0] = "{*}";
				
			}
			if("provanuova2".equals(type)){
				schema[3][1] = "{1}"; schema[3][0] = "{0}";
				schema[2][1] = "{0}"; schema[2][0] = "{0}";
				schema[1][1] = "{0}"; schema[1][0] = "{1}";
				schema[0][1] = "{0}"; schema[0][0] = "{*}";
				
			}
			if("Test3".equals(type)){
				schema[1][1] = "{1}"; schema[1][0] = "{2}";
				schema[0][1] = "{0}"; schema[0][0] = "{*}";
				isTriangular = true;
			}
			if("03012016".equals(type)){
				schema[1][1] = "{2}"; schema[1][0] = "{-1}";
				schema[0][1] = "{0}"; schema[0][0] = "{*}";
				isTriangular = true;
			}
			if("Example1".equals(type)){
				schema[1][1] = "{1}"; schema[1][0] = "{b}";
				schema[0][1] = "{b}"; schema[0][0] = "{*}";
			}
			if("Example2".equals(type)){
				schema[1][1] = "{1}"; schema[1][0] = "{2b}";
				schema[0][1] = "{0}"; schema[0][0] = "{*}";
			}
			if("Delannoy1".equals(type)){
				schema[1][1] = "{1}"; schema[1][0] = "{1}";
				schema[0][1] = "{1}"; schema[0][0] = "{*}";
			}
			if("DelannoyEighth".equals(type)){
				schema[1][1] = "{1}"; schema[1][0] = "{1}";
				schema[0][1] = "{2}"; schema[0][0] = "{*}";
			}
			if("Delannoy2".equals(type)){
				schema[2][0] = "{1}";
				schema[1][0] = "{1}";
				schema[0][0] = "{*}";
			}
			if("Delannoy3".equals(type)){
				schema[1][1] = "{1}"; schema[1][0] = "{1}";
				schema[0][1] = "{c}"; schema[0][0] = "{*}";
			}
			if("Delannoy4".equals(type)){
				schema[1][2] = "{Pi}"; schema[1][1] = "{1}"; schema[1][0] = "{2}";
				schema[0][2] = "{-1}"; schema[0][1] = "{3}"; schema[0][0] = "{*}";
			}
			/*
			 * fake schema
			 */
			if("fakeSchema".equals(type)){
				schema[1][2] = "c_1,2"; schema[1][1] = "c_1,1"; schema[1][0] = "c_1,0";
				schema[0][2] = "c_0,2"; schema[0][1] = "c_0,1"; schema[0][0] = "*";
			}
			if("anotherFakeSchema".equals(type)){
				schema[1][1] = "c_1,1"; schema[1][0] = "c_1,0";
				schema[0][1] = "c_0,1"; schema[0][0] = "*";
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			throw new AdeException(Constants.ERROR_SCHEMA_SETTINGS);
		}
	}
	
	public String[][] getSchema() {
		return schema;
	}

	public void setSchema(String[][] schema) {
		this.schema = schema;
	}

	/**
	 * Nota. La stampa dello schema e al rovescio
	 */
	@Override
	public String toString() {
		String result = "";
		for (int i = schema.length-1; i >=0; i--) {
		    for (int j = schema[i].length-1; j >=0; j--) {
		    	if(schema[i][j]!=null){
		    		result += "  " + schema[i][j];
		    	}		    		
		    }
		    result += "\n";
		}
		result +="\n";
		return result;
	}

	public String[] getFuncGenH() {
		return funcGenH;
	}

	public void setFuncGenH(String[] funcGenH, KernelLink kernelLink) throws AdeException {
		if(funcGenH.length!=h){
			throw new AdeException(Constants.ERROR_GEN_FUN_H_DEF);
		}
		
		this.funcGenH = funcGenH;
		
		for(int i=0;i<h;i++){
			coeffArrayH[i] = getCoeffListGeneralizedFromMathematica(funcGenH[i],kernelLink);
		}
		
	}

	public String[] getFuncGenV() {
		return funcGenV;
	}

	public void setFuncGenV(String[] funcGenV, KernelLink kernelLink) throws AdeException {
		if(funcGenV.length!=v){
			throw new AdeException(Constants.ERROR_GEN_FUN_V_DEF);
		}
		this.funcGenV = funcGenV;
		for(int i=0;i<v;i++){
			coeffArrayV[i] = getCoeffListGeneralizedFromMathematica(funcGenV[i], kernelLink);
		}
		
	}
	
	private String[] getCoeffListGeneralizedFromMathematica(String funcGen, KernelLink kernelLink) {
		String series = "Series["+funcGen+", {x, 0, "+Constants.MATRIX_DIMENSION+"}]";
		String coeffList = "CoefficientList["+series+", x]";
		String strResult = kernelLink.evaluateToInputForm(coeffList, 0);		
		if(Constants.DEBUG_ENABLED){
			logger.info("expr .. ");
	        logger.info(strResult);
	        logger.info("Converto i coefficienti in array di interi");
		}        
        
        String [] a2 = Utils.getArrayFromString(strResult);
        
        if(a2.length<Constants.MATRIX_DIMENSION){
        	String a[] = new String [Constants.MATRIX_DIMENSION];
        	for(int i=0; i<a2.length; i++){
            	a[i] = a2[i];
            }
        	for(int i=a2.length; i<Constants.MATRIX_DIMENSION; i++){
        		a[i] = "0";
        	}
        	return a;
        }
        
        return a2;
		
	}

	public String[][] getCoeffArrayH() {
		return coeffArrayH;
	}

	public void setCoeffArrayH(String[][] coeffArrayH) {
		this.coeffArrayH = coeffArrayH;
	}

	public String[][] getCoeffArrayV() {
		return coeffArrayV;
	}

	public void setCoeffArrayV(String[][] coeffArrayV) {
		this.coeffArrayV = coeffArrayV;
	}

	public int getH() {
		return h;
	}

	public int getV() {
		return v;
	}

	public boolean isTriangular() {
		return this.isTriangular ;
	}

	public void setFuncGenH(String[] strings) {
		this.funcGenH = strings;
	}

	public void setFuncGenV(String[] strings) {
		this.funcGenV = strings;
	}

}
