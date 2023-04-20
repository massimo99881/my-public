package it.unimi.isa.service.impl;

import it.unimi.isa.model.graphs.Graph;
import it.unimi.isa.service.WilfService;
import it.unimi.isa.singleton.Singleton;
import it.unimi.isa.utils.Constants;
import it.unimi.isa.utils.Utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wolfram.jlink.KernelLink;
import com.wolfram.jlink.MathLinkException;
import com.wolfram.jlink.MathLinkFactory;

/*
 * Esempio di esecuzione dell'algoritmo di Wilf per ottenere 
 * la FGO della somma delle righe e quindi la successione dei 
 * valori dell'espansione in serie.
 */
@Service
public class WilfServiceImpl implements WilfService {
	
	@Autowired
	Singleton singleton;
	
	@Autowired
	MathematicaServiceImpl mathematicaServiceImpl;
	
	private static final Logger logger = Logger.getLogger(WilfServiceImpl.class);
	
//	public void findRSnFromTM(KernelLink ml, String xTM){
//		//in questo caso abbiamo la TM
//		
//		logger.info(xTM);
//		logger.info(ml.evaluateToInputForm(xTM, 0));
//		
//		String Iden = 
//			"Iden = {"+
//			" {1, 0, 0},"+
//			" {0, 1, 0},"+
//			" {0, 0, 1}"+
//			"}";
//		logger.info(Iden);
//		logger.info(ml.evaluateToInputForm(Iden, 0));
//		
//		String m = "m = Iden - xTM";
//		logger.info(m);
//		logger.info(ml.evaluateToInputForm(m, 0));
//		
//		String m2 = "m2 = Inverse[m]";
//		logger.info(m2);
//		logger.info(ml.evaluateToInputForm(m2, 0));
//		
//		String m8 = "m8 = Total[Total[m2]]";
//		logger.info(m8);
//		logger.info(ml.evaluateToInputForm(m8, 0));
//		
//		String f = "Simplify[m8]";		
//		logger.info(f);
//		logger.info(ml.evaluateToInputForm(f, 0));
//		
//		String RSn = "Series["+f+", {x, 0, 9}]";
//		String coeffList = "CoefficientList["+RSn+", x]";
//		String strResult = ml.evaluateToInputForm(coeffList, 0);	
//		logger.info(RSn);
//		logger.info(strResult);
//	}
	
	public String[] findRSnFromTM_v2(String[][] xTM){
		KernelLink ml = null;
		String [] result = new String[7];
		
		try {
            System.setProperty("com.wolfram.jlink.libdir", Constants.JLINKDIR);
            ml = MathLinkFactory.createKernelLink(Constants.EXEC_KERNEL_LINK);
           	ml.discardAnswer();
           	
          //in questo caso abbiamo la TM
    		
    		String formatted_xtm = mathematicaServiceImpl.formatTransferMatrix(xTM);
    		result[0] = formatted_xtm;
    		logger.info(formatted_xtm);
    		
    		logger.info(ml.evaluateToInputForm(formatted_xtm, 0));
    		
    		String fomatted_iden = mathematicaServiceImpl.formatIdentityTM(xTM.length);
    		result[1] = fomatted_iden;
    		String Iden = fomatted_iden;
    		
    		logger.info(Iden);
    		logger.info(ml.evaluateToInputForm(Iden, 0));
    		
    		String m = "m = Iden - xTM";
    		logger.info(m);
    		m = ml.evaluateToInputForm(m, 0);
    		result[2] = m;
    		logger.info(m);
    		
    		String m2 = "m2 = Inverse[m]";
    		logger.info(m2);
    		m2 = ml.evaluateToInputForm(m2, 0);
    		result[3] = m2;
    		logger.info(m2);
    		
    		String m8 = "m8 = Total[Total[m2]]";
    		logger.info(m8);
    		m8 = ml.evaluateToInputForm(m8, 0);
    		result[4] = m8;
    		logger.info(m8);
    		
    		String f = "Simplify[m8]";		
    		logger.info(f);
    		f = ml.evaluateToInputForm(f, 0);
    		result[5] = f;
    		logger.info(f);
    		
    		String RSn = "Series["+f+", {x, 0, 9}]";
    		String coeffList = "CoefficientList["+RSn+", x]";
    		String strResult = ml.evaluateToInputForm(coeffList, 0);	
    		logger.info(RSn);
    		result[6] = strResult;
    		logger.info(strResult);
    		
    		return result;
        	
        } catch (MathLinkException e) {
            logger.error("MathLinkException occurred: " + e.getMessage());
            return null;
        } /*finally {
            ml.close();
        }*/ catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public String[] findRSnFromTM_v3(String[][] xTM, Graph g){
		logger.info("cerca fgo della somma delle righe e stampa successione");
		KernelLink ml = null;
		String [] result = new String[10];
		
		String output = "";
		
		try {
			if(singleton==null){
				//se la classe viene utilizzata come test (viene cioè eseguito il main)
				//allora devo istanziare l'oggetto KernelLink senza usare l'injection di spring
				System.setProperty("com.wolfram.jlink.libdir", Constants.JLINKDIR);
	            ml = MathLinkFactory.createKernelLink(Constants.EXEC_KERNEL_LINK);
	           	ml.discardAnswer();
			}
			else {
				ml = singleton.getKernelLink();
			}
			
            //in questo caso abbiamo la TM
    		
    		String formatted_xtm = mathematicaServiceImpl.formatTransferMatrix(xTM);
    		result[0] = formatted_xtm;
    		logger.info(formatted_xtm);
    		output += formatted_xtm + "\n";
    		String tmp = ml.evaluateToInputForm(formatted_xtm, 0);
    		logger.info(tmp);
    		output += tmp + "\n";
    		String fomatted_iden = mathematicaServiceImpl.formatIdentityTM(xTM.length);
    		result[1] = fomatted_iden;
    		String Iden = fomatted_iden;
    		output += Iden + "\n";
    		logger.info(Iden);
    		tmp = ml.evaluateToInputForm(Iden, 0);
    		logger.info(tmp);
    		output += tmp +"\n";
    		String m = "m = Iden - xTM";
    		logger.info(m);
    		output += m + "\n";
    		m = ml.evaluateToInputForm(m, 0);
    		result[2] = m;
    		logger.info(m);
    		output += m + "\n";
    		String m2 = "m2 = Inverse[m]";
    		logger.info(m2);
    		output += m2 + "\n";
    		m2 = ml.evaluateToInputForm(m2, 0);
    		result[3] = m2;
    		logger.info(m2);
    		output += m2 + "\n";
    		String m8 = "m8 = Total[Total[m2]]";
    		logger.info(m8);
    		output += m8 + "\n";
    		m8 = ml.evaluateToInputForm(m8, 0);
    		result[4] = m8;
    		logger.info(m8);
    		output += m8 + "\n";
    		String f = "f = Together[Simplify[m8]]";		
    		logger.info(f);
    		output += f + "\n";
    		f = ml.evaluateToInputForm(f, 0);
    		f = f.replaceAll("\\*", "");
    		result[5] = f;
    		logger.info(f);
    		output += f + "\n";
    		//String f2 = "f2 = f ";
    		
    		String f2 = "f2 = 1 + x * f ";
    		
    		/*String f2 = "f2 = 1 + ";
    		int h = g.findMaxPotenzaCamminoOrizzontale();
    		for(int i=1;i<=h;i++){
    			f2 += "x^"+i+" ";
    			if(i<h){
    				f2 += "+ ";
    			}
    			else if(i==h){
    				f2 += "* f ";
    			}
    		}*/
    		result[6] = f2;	
    		logger.info(f2);
    		f = ml.evaluateToInputForm(f2, 0);
    		f = ml.evaluateToInputForm("Together[Simplify["+f+"]]", 0);
    		result[7] = f;
    		logger.info(f);
    		
    		String RSn = "Series["+f+", {x, 0, 9}]";
    		String coeffList = "CoefficientList["+RSn+", x]";
    		String strResult = ml.evaluateToInputForm(coeffList, 0);	
    		logger.info(RSn);
    		result[8] = strResult;
    		logger.info(strResult);
    		
    		RSn = "Series["+result[5]+", {x, 0, 9}]";
    		output += RSn + "\n";
    		output += ml.evaluateToInputForm(RSn, 0) + "\n";
    		coeffList = "CoefficientList["+RSn+", x]";
    		output += coeffList + "\n";
    		strResult = ml.evaluateToInputForm(coeffList, 0);	
    		logger.info(RSn);
    		result[9] = strResult;
    		logger.info(strResult);
    		output += strResult + "\n";
    		
    		Utils.saveInCache(g.getN(),g.getM(),g.getF(),g.getPotenzaCamminoVerticale(), g.getPotenzaCamminoOrizzontale(), g.getNomeFileCache(), output, Constants.PATH_TO_CACHE_TM);
    		
    		return result;
        	
        } catch (MathLinkException e) {
            logger.error("MathLinkException occurred: " + e.getMessage());
            return null;
        } /*finally {
            ml.close();
        }*/ catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
