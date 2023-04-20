package it.unimi.isa.service.impl;

import it.unimi.isa.model.Sigma;
import it.unimi.isa.service.IsoTriangularService;
import it.unimi.isa.singleton.Singleton;
import it.unimi.isa.utils.Constants;
import it.unimi.isa.utils.Utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wolfram.jlink.KernelLink;

@Service
public class IsoTriangularServiceImpl implements IsoTriangularService {
	
	private static final Logger logger = Logger.getLogger(IsoTriangularServiceImpl.class);
	
	@Autowired
	Singleton singleton;
	
	private KernelLink ml;
	private String[] diagonal;
	private String fgoVx;
	private String fgoRSn;
	private Sigma sigma;
	
	public IsoTriangularServiceImpl(KernelLink ml, Sigma sigma, String[] d) {
		this.sigma = sigma;
		this.diagonal = d;
	}

	public IsoTriangularServiceImpl() {
	}

	public void isodiagonalizza() {
		ml = singleton.getKernelLink();
		String [][] c = sigma.getSchema();
		logger.info(sigma);
		
		String [][] m = new String [Constants.MATRIX_DIMENSION][Constants.MATRIX_DIMENSION];
		
		//inizializzo temporaneamente tutto a zero ..
		for(int i=0;i<Constants.MATRIX_DIMENSION;i++){
			for(int j=0;j<Constants.MATRIX_DIMENSION;j++){
				m[i][j] = "0";
			}
		}
		
		m[0][0] = this.diagonal[0];
		for(int i=1;i<Constants.MATRIX_DIMENSION;i++){
			for(int j=0;j<Constants.MATRIX_DIMENSION;j++){
				if(j==0){
					int di = Integer.parseInt(this.diagonal[i].trim());
					int sum = 0;
					//ottengo valore mancante diagonale..
					for(int a=i-1, b=j+1;a>=0;a--,b++){
						if(m[a][b]!=null){
							sum += Integer.parseInt(m[a][b]);
						}
					}
					int res = di - sum;
					m[i][j] = String.valueOf(res);
				}
				else if(j<=(i+1)){
					//questo blocco è usato allo stesso modo anche in Ade.java
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
		}
		Utils.print(m);
		
		
	}

	public void obtainFgoVx(Sigma sigma, String[][] m) {
		ml = singleton.getKernelLink();
		logger.info("obtainFgoVx ... ");
		String [][] c = sigma.getSchema();
//		Utils.print(c);
//		logger.info(c[0][0]);
//		logger.info(c[0][1]);
//		logger.info(c[1][0]);
//		logger.info(c[1][1]);
		String tmp = "f = ";
		tmp += "(((1-("+c[0][1]+"*x))*("+sigma.getFuncGenH()[0]+")) + ";
		tmp += "((1-("+c[1][0]+"*x))*("+sigma.getFuncGenV()[0]+")) - ";
		tmp += "("+m[0][0]+"))/(1-(("+c[0][1]+"+"+c[1][0]+")*x))";
		logger.info(tmp);
		String fgo = ml.evaluateToInputForm(tmp,0);
		logger.info("f = "+fgo);
		this.fgoVx = fgo;
		String series = "Series["+fgo+", {x, 0, 9}]";
		String coeffList = "CoefficientList["+series+", x]";
		String strResult = ml.evaluateToInputForm(coeffList, 0);
		logger.info(strResult);
	}
	
	public void obtainFgoRSn(Sigma sigma, String[][] m) {
		ml = singleton.getKernelLink();
		logger.info("obtainFgoRSn ... ");
		String [][] c = sigma.getSchema();
		String tmp = "f = ";
		tmp += "(((1-"+c[0][1]+"*x)*("+sigma.getFuncGenH()[0]+")) + ";
		tmp += "((1-"+c[1][0]+"*x)*("+sigma.getFuncGenV()[0]+")) - ";
		tmp += "("+m[0][0]+"))/(1-(("+c[1][1]+"+"+c[1][0]+"+"+c[0][1]+")*x))";
		logger.info(tmp);
		String fgo = ml.evaluateToInputForm(tmp,0);
		logger.info("f = "+fgo);
		this.fgoRSn = fgo;
		String series = "Series["+fgo+", {x, 0, 9}]";
		String coeffList = "CoefficientList["+series+", x]";
		String strResult = ml.evaluateToInputForm(coeffList, 0);
		logger.info(strResult);
	}
	
	public String getFgoVx() {
		return fgoVx;
	}

	public String getFgoRSn() {
		return fgoRSn;
	}

	public void setSigma(Sigma sigma) {
		this.sigma = sigma;
	}

	public void setDiagonal(String[] diagonal) {
		this.diagonal = diagonal;
	}

	
}
