package it.unimi.isa.service.impl;

import it.unimi.isa.exceptions.AdeException;
import it.unimi.isa.model.Sigma;
import it.unimi.isa.service.AdeService;
import it.unimi.isa.singleton.Singleton;
import it.unimi.isa.utils.Constants;
import it.unimi.isa.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wolfram.jlink.KernelLink;
import com.wolfram.jlink.MathLinkException;



/**
 * @class Ade
 * @see Classe per il calcolo della relazione di ricorrenzza delle antidiagonali
 * @author Administrator
 * @category tdg
 */
@Service
public class AdeServiceImpl implements AdeService {
	
	private static final Logger logger = Logger.getLogger(AdeServiceImpl.class);
	
	@Autowired
	Singleton singleton;
	
	private KernelLink ml; //Oggetto MathLink
	private String[] d; //the sums of the anti-diagonals 
	private String dn; //the sums of the anti-diagonals in sample string format (used for tests)
	private int h = 0; //term used for calculate generating function of dn
	
	private int n; //righe della matrice M
	private int k; //colonne
	
	
	private Sigma sigma;
	
	private String CBL;
	private String CTR;
	private String KER;
	private String EBL;
	private String ETR;
	
	private String fgoDn;
	
	public AdeServiceImpl() {
	}

	/**
	 * Ottiene ricorrenza e funzione generatrice delle Antidiagonali
	 * dato lo schema e le condizioni al contorno
	 * @throws AdeException
	 */
	public void obtainRecurrenceAndGF(String[][] matrix) throws AdeException {
		Sigma s = this.sigma;
		String[] funcGenH = s.getFuncGenH();
		String[] funcGenV = s.getFuncGenV();
		String[][] m = matrix;
		String [][] c = s.getSchema();
		
		//TODO sistemare questo pezzo di codice che toglie le parentesi {,} dallo schema
		for(int i=0;i<c.length;i++){
			for(int j=0;j<c[i].length;j++){
				String tmp = c[i][j];
				c[i][j]=tmp.substring(1,tmp.length()-1);
			}
		}
		
		logger.info(c);
		
		int H = s.getH();
		int V = s.getV();
		if(Constants.DEBUG_ENABLED)
			logger.info("h+v= "+(H+V));
		d = new String[Constants.ARRAY_ADE_DIMENSION];
		
		String sum;
		this.h=0;
		
		for(int n=0; n<H+V; n++){
			sum = "0";
			for(int h=0; h<=(H+1) ; h++){
				for(int v=0; v<=(V+1); v++){
					if(h+v==n){						
						String expr = sum + " + " + m[h][v];
						sum = ml.evaluateToOutputForm(expr, 0); 
					}
				}
			}
			d[this.h]= ml.evaluateToInputForm("Expand["+sum+"]",0);
			this.h++;
		}
		
		//per n>= H+V .. [ d_n = CBL_n + CTR_n + KER_n - EBL_n - ETR_n ]
		
		int n = Constants.MATRIX_DIMENSION-1;
		
		CBL = "0";
		for(int j=0; j<V; j++){
			if(funcGenV[0]!=null){
				if(Constants.GF_FIBONACCI.equals(funcGenV[0])){
					CBL += " + FIB[n] ";
				}
				else if(Constants.GF_EXPONENTIAL.equals(funcGenV[0])){
					CBL += " + EXP[n] ";
				}
				else {
					CBL += " + " + m[(n-j)][j];
				}
			}	
			else{
				throw new AdeException(Constants.ERROR_GEN_FUN_V_DEF);
			}
			
		}
		if(Constants.DEBUG_ENABLED)
			logger.info("CBL = "+CBL);
		
		CTR = "0";
		for(int i=0; i<H; i++){
			if(funcGenH[0]!=null){
				if(Constants.GF_FIBONACCI.equals(funcGenH[0])){
					CTR += " + FIB[n] ";
				}
				else if(Constants.GF_EXPONENTIAL.equals(funcGenH[0])){
					CTR += " + EXP[n] ";
				}
				else {
					CTR += " + " + m[i][(n-i)];
				}
			}
			else{
				throw new AdeException(Constants.ERROR_GEN_FUN_H_DEF);
			}
			
		}
		if(Constants.DEBUG_ENABLED)
			logger.info("CTR = "+CTR);
		
		KER = "0";
		for(int t=0; t<(H+V); t++){
			//getting w..
				String w = "0";
				for(int i=0; i<(H+1); i++){
					for(int j=0; j<(V+1); j++){
						if(i+j==t){
							w += " + " + c[(H-i)][(V-j)];
						}
					}
				}
			//now calc current KER ..
			KER += " + (" +ml.evaluateToInputForm("Expand["+w+"]", 0) + ") * " + " d[n-" + (H+V-t)+ "]";
		}
		if(Constants.DEBUG_ENABLED)
			logger.info("KER = "+KER);
		
		EBL = "0";
		for(int i=0; i<V; i++){
			for(int j=0; j<=i; j++){
				for(int p=0; p<=H; p++){
					if(p!=0 || j!=0){
						if(funcGenV[0]!=null){
							if(Constants.GF_FIBONACCI.equals(funcGenV[0])){
								EBL += " + " + c[p][j] + " * FIB[n-" + (p-i) +"]";
							}
							else if(Constants.GF_EXPONENTIAL.equals(funcGenV[0])){
								EBL += " + " + c[p][j] + " * EXP[n-" + (p-i) +"] ";
							}
							else {
								EBL += " + " + c[p][j] + " * " + m[(n-p-i)][(i-j)];
							}
						}
						else {
							throw new AdeException(Constants.ERROR_GEN_FUN_V_DEF);
						}
					}
				}
			}
		}
		if(Constants.DEBUG_ENABLED)
			logger.info("EBL = "+EBL);
		
		ETR = "0";
		for(int j=0; j<H; j++){
			for(int i=0; i<=j; i++){
				for(int q=0; q<=V; q++){
					if(i!=0 || q!=0){
						if(funcGenH[0]!=null){				
							if(Constants.GF_FIBONACCI.equals(funcGenH[0])){
								ETR += " + " + c[i][q] + " * FIB[n-"+(q-j)+"]";
							}
							else if(Constants.GF_EXPONENTIAL.equals(funcGenH[0])){
								ETR += " + " + c[i][q] + " * EXP[n-"+(q-j)+"]";
							}
							else {
								ETR += " + " + c[i][q] + " * " + m[(j-i)][(n-q-j)];
							}
						}
						else{
							throw new AdeException(Constants.ERROR_GEN_FUN_H_DEF);
						}
						
					}					
				}
			}
		}
		if(Constants.DEBUG_ENABLED)
			logger.info("ETR = "+ETR);
		
		String expr = "("+CBL+") + ("+CTR+") + ("+KER+") - ("+EBL+") - ("+ETR+")";
		logger.info("expr = {\n" + expr + "\n}\n");
		String result = ml.evaluateToInputForm("Expand["+expr+"]", 0) ;
		
		logger.info("dn = {"+result+"}");
		
		
		//ottiene la sequenza di valori dn usando mathematica..
		String out = MathematicaServiceImpl.SCRIPT_FIBONACCI + " \n "+ MathematicaServiceImpl.SCRIPT_EXPONENTIAL + " Clear[d]\n";
		for(int i=0; i<this.h; i++){
			out += "d["+i+"]:="+d[i]+" ; \n";
		}
		out += "d[n_Integer] := "+result +";\n";
		out += "Expand[d/@ Range[0, "+(d.length + Constants.EXTRA_VALUES)+"]]";
		
		logger.info("------");
		if(Constants.DEBUG_ENABLED)
			logger.info(out);		
		
		result = ml.evaluateToInputForm(out , 0);
		logger.info(result);
		
		this.dn = result;
		
		d = Utils.getArrayFromString(result);
		//Utils.printArray(d); 
		
		fgoDn = obtainGeneratingFuncion(result);
		logger.info("G.f:\n " + fgoDn);
	}
	
	/**
	 * Ottiene la funzione generatrice delle antidiagonali
	 * @param sequence
	 * @return
	 * @throws AdeException
	 */
	public String obtainGeneratingFuncion(String sequence) throws AdeException {
		
		return ml.evaluateToInputForm( "FindGeneratingFunction[" + sequence + ",x]" , 0);
		/*
		if(this.h==0){
			throw new AdeException(Constants.ERROR_EXECUTION_SEQUENCE);
		}
		
		String fn = "f[n]:= 0 ";
		for(int i=0; i<this.h; i++){
			int j = i+1;
			fn += " + c["+j+"] * f[n-"+j+"]";
		}
		fn += " + A ";
		
		logger.info(fn);
		
		String Dh = "Dh[x]:=1-(0";
		for(int i=1; i<=this.h; i++){
			Dh += " + c["+i+"] * x^"+i+" ";
		}
		Dh += ")";
		
		logger.info(Dh);
		
		String Nh = "Nh[x]:=0";
		for(int i=0; i<this.h; i++){
			Nh += " + x^"+i+" * (";
			Nh += "c[0]*f["+i+"] - (0";
			for(int j=1; j<=i; j++){
				Nh += " + c["+j+"]*f["+(i-j)+"]";
			}
			Nh += ")";
			Nh += ")";
		}
		
		logger.info(Nh);
		
		String F = "F[x]:= ("+Nh+"*(1-x)+A*x^(h+1))/((1-x)*"+Dh+")";
		logger.info(F);
		*/
	}

	public void obtainFakeRecurrence() {
		Sigma s = this.sigma;
		String[][] m = new String[Constants.MATRIX_DIMENSION][Constants.MATRIX_DIMENSION];
		String [][] c = s.getSchema();
		int H = s.getH();
		int V = s.getV();
		
		d = new String[H+V];
		
		String sum;
		int x=0;
		
		for(int n=0; n<H+V; n++){
			sum = "0 ";
			for(int h=0; h<=(H+1) ; h++){
				for(int v=0; v<=(V+1); v++){
					if(h+v==n){						
						sum += " + " + m[h][v];
						//sum = ml.evaluateToOutputForm(expr, 0); 
					}
				}
			}
			d[x]= sum;
			x++;
		}
		
		Utils.printArray(d);
		
		//per n>= H+V .. [ d_n = CBL_n + CTR_n + KER_n - EBL_n - ETR_n ]
		
		int n = Constants.MATRIX_DIMENSION-1;
		
		CBL = "0";
		for(int j=0; j<V; j++){
			CBL += " + " + m[(n-j)][j];
		}
		logger.info("CBL = "+CBL);
		
		CTR = "0";
		for(int i=0; i<H; i++){
			CTR += " + " + m[i][(n-i)];
		}
		logger.info("CTR = "+CTR);
		
		KER = "0";
		for(int t=0; t<(H+V); t++){
			//getting w..
				String w = "0";
				for(int i=0; i<(H+1); i++){
					for(int j=0; j<(V+1); j++){
						if(i+j==t){
							w += " + " + c[(H-i)][(V-j)];
						}
					}
				}
			//now calc current KER ..
			KER += " + (" +w + ") * " + " d_n-" + (n-(H+2-V+2+t));
		}
		logger.info("KER = "+KER);
		
		EBL = "0";
		for(int i=0; i<V; i++){
			for(int j=0; j<=i; j++){
				for(int p=0; p<=H; p++){
					if(p!=0 || j!=0){
						EBL += " + " + c[p][j] + " * " + m[(n-p-i)][(i-j)];
					}
				}
			}
		}
		logger.info("EBL = "+EBL);
		
		ETR = "0";
		for(int j=0; j<H; j++){
			for(int i=0; i<=j; i++){
				for(int q=0; q<=V; q++){
					if(i!=0 || q!=0){
						ETR += " + " + c[i][q] + " * " + m[(j-i)][(n-q-j)];
					}					
				}
			}
		}
		logger.info("ETR = "+ETR);
		
	}

	public String[] getD() {
		return d;
	}
	public String getDn() {
		return dn;
	}

	public String getCBL() {
		return CBL;
	}

	public String getCTR() {
		return CTR;
	}

	public String getKER() {
		return KER;
	}

	public String getEBL() {
		return EBL;
	}

	public String getETR() {
		return ETR;
	}

	public String getFgoDn() {
		return fgoDn;
	}

	public List<Integer> getAntidiagonals(Integer [][] independentMatrix) {
		
		List<Integer> antidiagonals = new ArrayList<Integer>();
		
		for(int i = 0; i < independentMatrix.length; i++){
			
			int j=0;
			int sum = 0;
			for(int k = i; k >=0 ; k--){			
				if(j<independentMatrix[k].length && independentMatrix[k][j]!=null){
					Integer val = independentMatrix[k][j];
					sum += val;	
					j++;
				}	
				
			}
			antidiagonals.add(sum);
			
		}
		
		return antidiagonals;
	}

	public void setMl(KernelLink ml) {
		this.ml = ml;
	}

	
	/**
	 * Effettua il calcolo per trovare i valori centrali della matrice
	 * Presuppone che le condizioni al contorno siano state gia calcolate.
	 * @param sigma
	 * @throws MathLinkException
	 * @throws AdeException 
	 */
	public String[][] complete() throws MathLinkException, AdeException {
		
		this.ml = singleton.getKernelLink();
		this.n = Constants.MATRIX_DIMENSION;
		this.k = Constants.MATRIX_DIMENSION;
		String[][] m = new String[this.n][this.k];
		
		//recupera i valori di sigma e stampa la sua configurazione .. 
		String [][] c = sigma.getSchema();
		logger.info(sigma);
		int h = sigma.getH(); 
		int v = sigma.getV();
		String[][] coeffH = sigma.getCoeffArrayH();
		String[][] coeffV = sigma.getCoeffArrayV();
		
		if((coeffH==null && h>0) || (coeffV==null && v>0)){
			throw new AdeException(Constants.ERROR_COEFF_NOT_FOUND);
		}			
		
		try {
			//ottiene i valori al contorno:
			//aggiungo alla matrice le condizioni al contorno per le righe
			for(int i=0;i<h;i++){			
				m[i]=coeffH[i];
			}
			//aggiungo alla matrice le condizioni al contorno per le colonne
			for(int i=0;i<v;i++){
				for(int j=0;j<Constants.MATRIX_DIMENSION;j++){				
					m[j][i]=coeffV[i][j];
				}
			}	
			
			Utils.print(m);
			
			//completa la matrice con i valori "centrali" .. 		
			for(int i=h;i<n;i++){
				for(int j=v;j<k;j++){
					if(j>i && sigma.isTriangular()){
						m[i][j]="0";
						//Utils.print(M);
						continue;
						//break;
					}
					
					String result = "";

					for(int r = 0; r<c.length; r++){
						for(int s = 0; s < c[r].length; s++){
							if(r!=0 || s!=0){
								String [] sigma = Utils.getArrayFromString(c[r][s]);
								String expr = "(" + m[i-r][j-s]+") * " +sigma[0]+ " + ";
								String append = "(0";
								for(int cv=1; cv<sigma.length; cv++){
									append += " + "+sigma[cv];																
								}			
								append += ")";
								expr += append;
								if(Utils.isEmptyOrNull(result)){
									result = ml.evaluateToOutputForm(expr, 0);
								}
								else {
									result = "(" + result + ") + (" +
									ml.evaluateToOutputForm(expr, 0) + ") ";									
								}	
							}
							
						}
					}
					
					String total = ml.evaluateToInputForm("Expand["+result+"]",0);
					//logger.info("total = ["+total+"]");
					
					m[i][j]= total;
					
				}
			}
			//stampa la matrice ottenuta
			Utils.print(m);
			
			return m;
			
		} catch (Exception e) {
			throw new AdeException(Constants.UNEXPECTED_ERROR_001);
		}
	}
	
	public String [][] fakeComplete() {
		logger.info(sigma);
		String[][] m = new String[Constants.MATRIX_DIMENSION][Constants.MATRIX_DIMENSION];
		for(int i=0; i<Constants.MATRIX_DIMENSION; i++){
			for(int j=0; j<Constants.MATRIX_DIMENSION; j++){
				m[i][j] = "m_"+i+","+j;
			}
		}
		Utils.print(m);
		return m;
	}

	public void setSigma(Sigma sigma) {
		this.sigma = sigma;
	}
	
}
