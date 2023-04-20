package test.unimi.isa.ade;

import it.unimi.isa.exceptions.AdeException;
import it.unimi.isa.model.Sigma;
import it.unimi.isa.service.impl.AdeServiceImpl;
import it.unimi.isa.service.impl.IsoTriangularServiceImpl;
import it.unimi.isa.singleton.Singleton;
import it.unimi.isa.utils.Constants;
import it.unimi.isa.utils.Utils;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.wolfram.jlink.KernelLink;
import com.wolfram.jlink.MathLinkException;
import com.wolfram.jlink.MathLinkFactory;

@Component
public class Subprog3 {
	
	private static final Logger logger = Logger.getLogger(Subprog3.class);
	
	@Autowired
	AdeServiceImpl adeServiceImpl;
	
	@Autowired
	IsoTriangularServiceImpl isoTriangularServiceImpl;
	
	@Autowired
	Singleton singleton;
	
	public static void main(String args[]){
		
		logger.info("######## INIZIO TEST Subprog3 ########");
		KernelLink ml = null;
        try {
        	ApplicationContext context = new ClassPathXmlApplicationContext(Constants.CONFIG_PATH);
        	Subprog3 subprog3 = context.getBean(test.unimi.isa.ade.Subprog3.class);
    		
        	
            System.setProperty("com.wolfram.jlink.libdir", Constants.JLINKDIR);
            ml = MathLinkFactory.createKernelLink(Constants.EXEC_KERNEL_LINK);
           	ml.discardAnswer();
        	
        	logger.info("Program started .. ");
            /**
	         * Triangularization ..
	         */
	        //subprog3.test1(ml); 
        	//subprog3.test2(ml);
	        
	        /**
	         * Esempio che fornisce i valori dell'n-cubo
	         */
        	subprog3.test4(ml); 
        	logger.info("######## FINE TEST Subprog3 ########");
	        
        } catch (MathLinkException e) {
            logger.error("MathLinkException occurred: " + e.getMessage());
        } catch (AdeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            ml.close();
        }
	}
	
	public void test1(KernelLink ml) throws MathLinkException, AdeException {
		
		logger.info("Test 1: Example1");
		
		Sigma sigma = new Sigma(1,1,"Example1");
		sigma.setFuncGenH(new String[]{"(1 - x)^-1"},singleton.getKernelLink()); 
		sigma.setFuncGenV(new String[]{"(1 - x)^-1"},singleton.getKernelLink());
		adeServiceImpl.setSigma(sigma);
		String[][] m = adeServiceImpl.complete(); 
		adeServiceImpl.obtainRecurrenceAndGF(m);
		
		logger.info("Test 1: Example2 ");
		
		sigma = new Sigma(1,1,"Example2");
		sigma.setFuncGenH(new String[]{"1"},singleton.getKernelLink()); 
		sigma.setFuncGenV(new String[]{"(1-(2*b-1)*x)/((1-x)*(1-2*b*x))"},singleton.getKernelLink());
		adeServiceImpl.setSigma(sigma);
		adeServiceImpl.complete(); 
	}
	
	@SuppressWarnings("unused")
	private void test2(KernelLink ml) throws MathLinkException, AdeException {
		logger.info("test 2: (1,1) scheme");
		
		Sigma sigma = new Sigma(1,1,"Test1");
		sigma.setFuncGenH(new String[]{"1"},singleton.getKernelLink()); 
		sigma.setFuncGenV(new String[]{"(1-x)^-1"},singleton.getKernelLink());
		
		adeServiceImpl.setSigma(sigma);
		adeServiceImpl.complete(); 
		
	}
	
	@SuppressWarnings("unused")
	private void test3(KernelLink ml) throws AdeException, MathLinkException {
		logger.info("test 3: ottengo il numero di facce dell'n-cubo");
		
		Sigma sigmaDelannoy = new Sigma(1,1,"Delannoy1");
		sigmaDelannoy.setFuncGenH(new String[]{"(1-x)^-1"},singleton.getKernelLink()); 
		sigmaDelannoy.setFuncGenV(new String[]{"(1-x)^-1"},singleton.getKernelLink());
		
		adeServiceImpl.setSigma(sigmaDelannoy);
		String[][] m = adeServiceImpl.complete(); 
		adeServiceImpl.obtainRecurrenceAndGF(m);
		
		//adesso cerco la matrice triangolarizzata
		//isodiagonale perchè la somma delle diagonali è la stessa..
		logger.info("Triangularization: isodiagonalize");
		Sigma sigma = new Sigma(1,1,"Test3");
		
		isoTriangularServiceImpl.setSigma(sigma);
		isoTriangularServiceImpl.setDiagonal(adeServiceImpl.getD());
		isoTriangularServiceImpl.isodiagonalizza();
		isoTriangularServiceImpl.obtainFgoVx(sigmaDelannoy, m);
		isoTriangularServiceImpl.obtainFgoRSn(sigmaDelannoy, m);
	}
	
	private void test4(KernelLink ml) throws AdeException, MathLinkException {
		logger.info("test 4");
		
		Sigma sigmaDelannoy = new Sigma(1,1,"03012016");
		sigmaDelannoy.setFuncGenH(new String[]{"1"},singleton.getKernelLink()); 
		//sigmaDelannoy.setFuncGenV(new String[]{"(1-x)^-1"},singleton.getKernelLink());
		sigmaDelannoy.setFuncGenV(new String[]{"(1-x)^-2"},singleton.getKernelLink());
		
		adeServiceImpl.setSigma(sigmaDelannoy);
		String[][] m = adeServiceImpl.complete(); 
		adeServiceImpl.obtainRecurrenceAndGF(m);
		
		
		
		
		
		logger.info("stampa matrice con moltiplicazione per k");
		Integer matriceInteriK[][] = new Integer[m.length][m[0].length];
		Integer rsnK[] = new Integer[m[0].length];
		Integer matriceInteri[][] = new Integer[m.length][m[0].length];
		Integer rsn[] = new Integer[m[0].length];
		
		for(int i=0;i<m.length;i++){
			rsnK[i] = 0;
			rsn[i] = 0;
			for(int k=0;k<m[i].length;k++){
				Integer valore = Integer.parseInt(m[i][k].trim());
				Integer valoreK = (k * Integer.parseInt(m[i][k].trim()));
				m[i][k] = String.valueOf(valoreK);
				matriceInteriK[i][k] = valoreK;
				rsnK[i] += valoreK;
				rsn[i] += valore;
				matriceInteri[i][k] = valore;
			}
		}
		
		logger.info("matriceInteri");
		Utils.print(matriceInteri);
		
		
		List<Integer> antidiagonaliList = adeServiceImpl.getAntidiagonals(matriceInteri);
		String antidiagList = Utils.getStringFromIntegerArray(antidiagonaliList);
        logger.info("AD : "+antidiagList);
		
		logger.info("la lista delle rsn");
		for(int y=0;y<rsn.length;y++){
			System.out.print(rsn[y]+",");
		}
		
		/////////////
		
		logger.info("matriceInteriK");
		Utils.print(matriceInteriK);
		
		
		antidiagonaliList = adeServiceImpl.getAntidiagonals(matriceInteriK);
		antidiagList = Utils.getStringFromIntegerArray(antidiagonaliList);
        logger.info("AD per  K: "+antidiagList);
		
		logger.info("la lista delle rsn per K");
		for(int y=0;y<rsnK.length;y++){
			System.out.print(rsnK[y]+",");
		}
	}
}
