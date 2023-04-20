package test.unimi.isa.grammar;

import it.unimi.isa.service.impl.WilfServiceImpl;
import it.unimi.isa.utils.Constants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.wolfram.jlink.KernelLink;
import com.wolfram.jlink.MathLinkException;
import com.wolfram.jlink.MathLinkFactory;

@Component
public class WilfServiceImplTest {
	
	private static final Logger logger = Logger.getLogger(WilfServiceImplTest.class);
	
	@Autowired
	WilfServiceImpl wilfServiceImpl;

	public static void main(String args[]){
		logger.info("######## INIZIO TEST WilfServiceImplTest ########");
		
		final ApplicationContext context = new ClassPathXmlApplicationContext(Constants.CONFIG_PATH);
		final WilfServiceImplTest wilfServiceImplTest = context.getBean(WilfServiceImplTest.class);
    	
    	logger.info("Program started .. ");
    	
    	wilfServiceImplTest.test();
    	
    	//wilfServiceImplTest.testGnk(ml); //{3, 7, 17, 41, 99, 239, 577, 1393, 3363, 8119} 
    	
    	logger.info("######## FINE TEST WilfServiceImplTest ########");
	}
	
	private void test() {

    	/**
    	 * Test Gnk (m=2)
    	 */
    	String[][] xTM1 = { 
    			{"x", "x", "x"}, 
    			{"x", "0", "x"}, 
    			{"x", "x", "0"}
    		};
    	wilfServiceImpl.findRSnFromTM_v2(xTM1);
    	//Risultato: {3, 7, 17, 41, 99, 239, 577, 1393, 3363, 8119} 
    	
    	/**
    	 * Test Gnk (m=3)
    	 */
    	String[][] xTM2 = { 
    			{"x",	"x",	"x",	"x",	"x"},
    			{"x",	"0",	"x",	"x",	"0"},
    			{"x",	"x",	"0",	"x",	"x"},
    			{"x",	"x",	"x",	"0",	"0"},
    			{"x",	"0",	"x",	"0",	"0"}
    		};
    	wilfServiceImpl.findRSnFromTM_v2(xTM2);
        //Risultato: {5, 17, 63, 227, 827, 2999, 10897, 39561, 143677, 521721}
    	
    	/**
    	 * Test Gnk (m=5)
    	 */
    	String[][] xTM3 = { 
    			{"x",	"x",	"x",	"x",	"x",	"x",	"x",	"x",	"x",	"x",	"x",	"x",	"x"},
    			{"x",	"0",	"0",	"0",	"0",	"x",	"x",	"x",	"x",	"x",	"x",	"0",	"x"},
    			{"x",	"0",	"0",	"0",	"0",	"x",	"x",	"0",	"x",	"0",	"x",	"0",	"0"},
    			{"x",	"0",	"0",	"0",	"0",	"x",	"x",	"x",	"0",	"0",	"x",	"0",	"x"},
    			{"x",	"0",	"0",	"0",	"0",	"x",	"x",	"0",	"0",	"0",	"x",	"0",	"0"},
    			{"x",	"x",	"x",	"x",	"x",	"0",	"0",	"0",	"x",	"x",	"x",	"x",	"x"},
    			{"x",	"x",	"x",	"x",	"x",	"0",	"0",	"0",	"x",	"x",	"0",	"0",	"x"},
    			{"x",	"x",	"0",	"x",	"0",	"0",	"0",	"0",	"x",	"0",	"x",	"x",	"0"},
    			{"x",	"x",	"x",	"0",	"0",	"x",	"x",	"x",	"0",	"0",	"x",	"x",	"x"},
    			{"x",	"x",	"0",	"0",	"0",	"x",	"x",	"0",	"0",	"0",	"x",	"x",	"0"},
    			{"x",	"x",	"x",	"x",	"x",	"x",	"0",	"x",	"x",	"x",	"0",	"0",	"x"},
    			{"x",	"0",	"0",	"0",	"0",	"x",	"0",	"x",	"x",	"x",	"0",	"0",	"x"},
    			{"x",	"x",	"0",	"x",	"0",	"x",	"x",	"0",	"x",	"0",	"x",	"x",	"0"}
    		};
    	wilfServiceImpl.findRSnFromTM_v2(xTM3);
        //Risultato: {13, 99, 827, 6743, 55447, 454385, 3729091, 30584687, 250916131, 2058249165}
    	
    	
    	/**
    	 * Test Tnk (m=2)
    	 */
    	String[][] xTM4 = { 
    			{"x","0","0","x","x","0","0"},
    			{"x","0","0","0","x","0","0"},
    			{"x","0","0","x","0","0","0"},
    			{"0","x","0","0","0","0","x"},
    			{"0","0","x","0","0","x","0"},
    			{"0","x","0","0","0","0","0"},
    			{"0","0","x","0","0","0","0"}
    		};
    	wilfServiceImpl.findRSnFromTM_v2(xTM4);
        //Risultato: {7, 13, 27, 57, 117, 241, 499, 1031, 2129, 4399}
    	
    	/**
    	 * Test Tnk (m=3)
    	 */
    	String[][] xTM5 = { 
    			{"x",	"0",	"0",	"0",	"0",	"x",	"0",	"0",	"x",	"0",	"0",	"0",	"x",	"0",	"0",	"x",	"0"},
    			{"x",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"x",	"0",	"0",	"0",	"x",	"0",	"0",	"0",	"0"},
    			{"x",	"0",	"0",	"0",	"0",	"x",	"0",	"0",	"0",	"0",	"0",	"0",	"x",	"0",	"0",	"x",	"0"},
    			{"x",	"0",	"0",	"0",	"0",	"x",	"0",	"0",	"x",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0"},
    			{"x",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"x",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0"},
    			{"0",	"x",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"x",	"0",	"0",	"0",	"x",	"0",	"0",	"0"},
    			{"0",	"x",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"x",	"0",	"0",	"0"},
    			{"0",	"x",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"x",	"0",	"0",	"0",	"0",	"0",	"0",	"0"},
    			{"0",	"0",	"x",	"0",	"0",	"0",	"x",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"x",	"0",	"x"},
    			{"0",	"0",	"x",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"x",	"0",	"0"},
    			{"0",	"0",	"x",	"0",	"0",	"0",	"x",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0"},
    			{"0",	"0",	"x",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0"},
    			{"0",	"0",	"0",	"x",	"0",	"0",	"0",	"x",	"0",	"0",	"x",	"0",	"0",	"0",	"0",	"0",	"0"},
    			{"0",	"0",	"0",	"x",	"0",	"0",	"0",	"0",	"0",	"0",	"x",	"0",	"0",	"0",	"0",	"0",	"0"},
    			{"0",	"0",	"0",	"x",	"0",	"0",	"0",	"x",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0"},
    			{"0",	"0",	"0",	"0",	"x",	"0",	"0",	"0",	"0",	"0",	"0",	"x",	"0",	"0",	"0",	"0",	"0"},
    			{"0",	"0",	"0",	"0",	"x",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0",	"0"}
    		};
    	wilfServiceImpl.findRSnFromTM_v2(xTM5);
        //Risultato: {17, 43, 127, 381, 1099, 3197, 9367, 27317, 79631, 232485}
    	
    	/**
    	 * Test Znk (m=2)
    	 */
    	String[][] xTM6 = { 
    			{"x",	"x",	"x"},
    			{"x",	"0",	"0"},
    			{"x",	"0",	"0"}
    		};
    	wilfServiceImpl.findRSnFromTM_v2(xTM6);
        //Risultato: {3, 5, 11, 21, 43, 85, 171, 341, 683, 1365}
    	
    	/**
    	 * Test Znk (m=3)
    	 */
    	String[][] xTM7 = { 
    			{"x",	"x",	"x",	"x",	"x"},
    			{"x",	"0",	"0",	"x",	"0"},
    			{"x",	"0",	"0",	"0",	"0"},
    			{"x",	"x",	"0",	"0",	"0"},
    			{"x",	"0",	"0",	"0",	"0"}
    		};
    	wilfServiceImpl.findRSnFromTM_v2(xTM7);
        //Risultato: {5, 11, 35, 93, 269, 747, 2115, 5933, 16717, 47003}
    	
    	/**
    	 * Test Fnk (m=2)
    	 */
    	String[][] xTM8 = { 
    			{"x",	"x",	"x"},
    			{"x",	"0",	"0"},
    			{"x",	"x",	"0"}
    		};
    	wilfServiceImpl.findRSnFromTM_v2(xTM8);
        //Risultato: {3, 6, 13, 28, 60, 129, 277, 595, 1278, 2745}
    	
    	/**
    	 * Test Fnk (m=3)
    	 */
    	String[][] xTM9 = { 
    			{"x",	"x",	"x",	"x",	"x"},
    			{"x",	"0",	"0",	"x",	"0"},
    			{"x",	"x",	"0",	"0",	"0"},
    			{"x",	"x",	"x",	"0",	"0"},
    			{"x",	"0",	"0",	"0",	"0"}
    		};
    	wilfServiceImpl.findRSnFromTM_v2(xTM9);
        //Risultato: {5, 13, 42, 126, 387, 1180, 3606, 11012, 33636, 102733}
    	
	}

	/**
	 * Esempio di come ottenere la RSn di G_n,k
	 */
	@SuppressWarnings("unused")
	private void testGnk(){
		KernelLink ml = null;
		
		try {
			System.setProperty("com.wolfram.jlink.libdir", Constants.JLINKDIR);
			ml = MathLinkFactory.createKernelLink(Constants.EXEC_KERNEL_LINK);
			ml.discardAnswer();
			//supponiamo di avere il sistema lineare della grammatica e costruiamo la TM
			String xTM = 
				 "xTM = {"+
				 " {x, x, x},"+
				 " {x, 0, x},"+
				 " {x, x, 0}"+
				 "}";
			logger.info(xTM);
			logger.info(ml.evaluateToInputForm(xTM, 0));
			
			String Iden = 
				"Iden = {"+
				" {1, 0, 0},"+
				" {0, 1, 0},"+
				" {0, 0, 1}"+
				"}";
			logger.info(Iden);
			logger.info(ml.evaluateToInputForm(Iden, 0));
			
			String m = "m = Iden - xTM";
			logger.info(m);
			logger.info(ml.evaluateToInputForm(m, 0));
			
			String m2 = "m2 = Inverse[m]";
			logger.info(m2);
			logger.info(ml.evaluateToInputForm(m2, 0));
			
			String m8 = "m8 = Total[Total[m2]]";
			logger.info(m8);
			logger.info(ml.evaluateToInputForm(m8, 0));
			
			String f = "Simplify[m8]";		
			logger.info(f);
			logger.info(ml.evaluateToInputForm(f, 0));
			
			String RSn = "Series["+f+", {x, 0, 9}]";
			String coeffList = "CoefficientList["+RSn+", x]";
			String strResult = ml.evaluateToInputForm(coeffList, 0);	
			logger.info(RSn);
			logger.info(strResult);
			
		} catch (MathLinkException e) {
            logger.error("MathLinkException occurred: " + e.getMessage());
            
        } /*finally {
            ml.close();
        }*/ catch (Exception e) {
			e.printStackTrace();
			
		}
	}
}
