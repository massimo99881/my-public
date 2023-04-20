package test.unimi.isa.ade;


import it.unimi.isa.exceptions.AdeException;
import it.unimi.isa.model.Sigma;
import it.unimi.isa.service.impl.AdeServiceImpl;
import it.unimi.isa.service.impl.IsoTriangularServiceImpl;
import it.unimi.isa.singleton.Singleton;
import it.unimi.isa.utils.Constants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.wolfram.jlink.MathLinkException;

@Component
public class AdeServiceImplTest {
	
	private static final Logger logger = Logger.getLogger(AdeServiceImplTest.class);
	
	@Autowired
	AdeServiceImpl adeServiceImpl;
	
	@Autowired
	IsoTriangularServiceImpl isoTriangularServiceImpl;
	
	@Autowired
	Singleton singleton;

	
	public static void main(String args[]) throws MathLinkException, AdeException {
		logger.info("######## INIZIO TEST AdeServiceImplTest ########");
		Constants.EXTRA_VALUES = 2;
		
		ApplicationContext context = new ClassPathXmlApplicationContext(Constants.CONFIG_PATH);
		AdeServiceImplTest adeServiceImplTest = context.getBean(test.unimi.isa.ade.AdeServiceImplTest.class);
		
//		adeServiceImplTest.secondTest();
//		adeServiceImplTest.thirdTest();
//		adeServiceImplTest.fourthTest();
//		adeServiceImplTest.sixthTest();
//		adeServiceImplTest.seventhTest();
//		adeServiceImplTest.eighthTest();
//		adeServiceImplTest.ninethTest();
//		adeServiceImplTest.tenthTest();
//		adeServiceImplTest.eleventhTest();
//		adeServiceImplTest.fakeTest();
//		adeServiceImplTest.anotherFakeTest();
//		adeServiceImplTest.aGame();
		adeServiceImplTest.nuovo();
//		adeServiceImplTest.nuovo2();
		
		logger.info("######## FINE TEST AdeServiceImplTest ########");
	}
	
	public String[] nuovo2() throws AdeException, MathLinkException {
		String result [] = new String [3];
		
		logger.info("Nuovo2: (3,1) scheme");
		
		Sigma sigma = new Sigma(3,1,"provanuova2");
		sigma.setFuncGenH(new String[]{"1","1+x","1+2x"},singleton.getKernelLink()); 
		sigma.setFuncGenV(new String[]{"(1 - x)^-1"},singleton.getKernelLink());
		adeServiceImpl.setSigma(sigma);
		String[][] m = adeServiceImpl.complete();
		adeServiceImpl.obtainRecurrenceAndGF(m);
		
		result[0] = adeServiceImpl.getDn();
		result[1] = adeServiceImpl.getFgoDn();
//	    assertTrue("{1, 1, 2, 3, 4, 5, 7, 10, 14, 19, 26}".equals(result[0]));
//	    assertTrue("1 + (x*(-1 - x - x^2 - x^3))/(-1 + x + x^4)".equals(result[1]));
		return result;
	}


	/**
	 * nuovo
	 * questo è l'esempio che usa le tabelle dei grafi che abbiamo studiato 
	 * per indep sets (!!! deve essere una matrice a ricorrenza lineare !!!)
	 * @param ml
	 * @return
	 * @throws AdeException
	 * @throws MathLinkException
	 */
	public String[] nuovo() throws AdeException, MathLinkException {
		String result [] = new String [3];
		
		logger.info("Nuovo (1,2) scheme");
		
		Sigma sigma = new Sigma(2,1,"provanuova");
		sigma.setFuncGenH(new String[]{"1","1+2x"},singleton.getKernelLink()); 
		sigma.setFuncGenV(new String[]{"(1 - x)^-1"},singleton.getKernelLink());
		adeServiceImpl.setSigma(sigma);
		String[][] m = adeServiceImpl.complete();
		adeServiceImpl.obtainRecurrenceAndGF(m);
		
		result[0] = adeServiceImpl.getDn();		
//	    assertTrue("{1, 1, 3, 5, 9, 17, 31, 57, 105}".equals(result[0]));
		return result;
	}
	
	public String[] aGame() throws AdeException, MathLinkException {
		String result [] = new String [3];
		
		logger.info("a game Delannoy (1,1) scheme");
 		
		Sigma sigma1 = new Sigma(1,1,"Delannoy1");
		sigma1.setFuncGenH(new String[]{"(1+2x)/(1-x^2)"},singleton.getKernelLink()); 
		sigma1.setFuncGenV(new String[]{"(1+2x)/(1-x^2)"},singleton.getKernelLink());
		adeServiceImpl.setSigma(sigma1);
		String[][] m = adeServiceImpl.complete(); 
		adeServiceImpl.obtainRecurrenceAndGF(m);
		
		//adesso cerco la matrice triangolarizzata
		//isodiagonale perchè la somma delle diagonali è la stessa..
		logger.info("Triangularization: isodiagonalize");
		Sigma sigma2 = new Sigma(1,1,"Test3");
		
		isoTriangularServiceImpl.setSigma(sigma2);
		isoTriangularServiceImpl.setDiagonal(adeServiceImpl.getD());
		isoTriangularServiceImpl.isodiagonalizza();
		
		isoTriangularServiceImpl.obtainFgoVx(sigma1, m);
		isoTriangularServiceImpl.obtainFgoRSn(sigma1, m);
		
		result[0] = adeServiceImpl.getDn();	
		
		//assertTrue("{1, 1, 3, 5, 9, 17, 31, 57, 105}".equals(result[0]));
		return result;
	}

	/**
	 * secondTest
	 * Qui abbiamo risolto il problema:
	 * KER = 0 + (1) *  d[n-0] + (1) *  d[n--1]
	 * @param ml
	 * @return
	 * @throws MathLinkException
	 * @throws AdeException
	 */
	public String[] secondTest() throws MathLinkException, AdeException {
		
		String[] result = new String[3];
		
		Sigma sigma = new Sigma(1,1,"Delannoy1");
		sigma.setFuncGenH(new String[]{"(1 - x)^-1"},singleton.getKernelLink()); 
		sigma.setFuncGenV(new String[]{"(1 - x)^-1"},singleton.getKernelLink());
		adeServiceImpl.setSigma(sigma);
		String[][] m = adeServiceImpl.complete(); 
		adeServiceImpl.obtainRecurrenceAndGF(m);
		
		result[0] = adeServiceImpl.getDn();	
		
		logger.info("Second Test : Delannoy (2,0) scheme");
		
		//1.definire la dimensione e i valori dello schema sigma
		sigma = new Sigma(2,0,"Delannoy2");
		//3.definire le funzioni generatrici per le condizioni al contorno
		sigma.setFuncGenH(new String[]{"(1 - x)^-1","(1 - x)^-1"},singleton.getKernelLink()); 
	
		adeServiceImpl.setSigma(sigma);
		String[][] m2 = adeServiceImpl.complete(); 		
		adeServiceImpl.obtainRecurrenceAndGF(m2);
		
		result[1] = adeServiceImpl.getDn();		
		return result;
		
		//assertTrue("{1, 2, 5, 12, 29, 70, 169, 408, 985}".equals(result[0]));
	    //assertTrue("{1, 2, 4, 7, 12, 20, 33, 54, 88}".equals(result[1]));
	}
	
	public String[] thirdTest() throws MathLinkException, AdeException {
		
		String[] result = new String[3];
		
		logger.info("Third Test: Delannoy (1,1) scheme");
		
		Sigma sigma = new Sigma(1,1,"Delannoy3");
		sigma.setFuncGenH(new String[]{Constants.GF_EXPONENTIAL},singleton.getKernelLink()); 
		sigma.setFuncGenV(new String[]{"(1 - x)^-1"},singleton.getKernelLink());
		adeServiceImpl.setSigma(sigma);
		String[][] m = adeServiceImpl.complete();
		adeServiceImpl.obtainRecurrenceAndGF(m);
		
		result[0] = adeServiceImpl.getDn();		
		//assertTrue("{1, 3, 8 + c, 19 + 5*c + c^2, 43 + 17*c + 6*c^2 + c^3, 94 + 49*c + 24*c^2 + 7*c^3 + c^4, 201 + 128*c + 79*c^2 + 32*c^3 + 8*c^4 + c^5, 423 + 314*c + 231*c^2 + 118*c^3 + 41*c^4 + 9*c^5 + c^6, 880 + 737*c + 624*c^2 + 381*c^3 + 167*c^4 + 51*c^5 + 10*c^6 + c^7}".equals(result[0]));
		return result;
	}
	
	
	
	public String[] fourthTest() throws MathLinkException, AdeException {
		
		String[] result = new String[3];
		
		logger.info("Fourth Test: Delannoy (1,2) scheme");
		
		Sigma sigma = new Sigma(1,2,"Delannoy4");
		sigma.setFuncGenH(new String[]{Constants.GF_FIBONACCI},singleton.getKernelLink()); 
		sigma.setFuncGenV(new String[]{"(1 - x)^-1", "(1 - x)^-1"},singleton.getKernelLink());
		adeServiceImpl.setSigma(sigma);
		String[][] m = adeServiceImpl.complete(); 
		adeServiceImpl.obtainRecurrenceAndGF(m);
		
		result[0] = adeServiceImpl.getDn();		
		//assertTrue("{1, 2, 4, 12 + Pi, 52 + 7*Pi, 250 + 39*Pi, 1238 + 207*Pi + Pi^2}".equals(result[0]));
		return result;
	}
	
	public String[] sixthTest() throws MathLinkException, AdeException {
		
		String[] result = new String[3];
		
		logger.info("Sixth Test: Delannoy (1,1) scheme");
		
		Sigma sigma = new Sigma(1,1,"Delannoy1");
		sigma.setFuncGenH(new String[]{"(1 - x)^-1"},singleton.getKernelLink()); 
		sigma.setFuncGenV(new String[]{"(1 - x)^-1"},singleton.getKernelLink());
		adeServiceImpl.setSigma(sigma);
		String[][] m = adeServiceImpl.complete(); 
		adeServiceImpl.obtainRecurrenceAndGF(m);
		
		result[0] = adeServiceImpl.getDn();	
		
		//assertTrue("{1, 2, 5, 12, 29, 70, 169, 408, 985}".equals(result[0]));
		return result;
		
	}
	
	public String[] seventhTest() throws MathLinkException, AdeException {
		
		String[] result = new String[3];
		
		logger.info("Seventh Test: Delannoy (1,1) scheme");
		
		Sigma sigma = new Sigma(1,1,"Delannoy1");
		sigma.setFuncGenH(new String[]{Constants.GF_FIBONACCI},singleton.getKernelLink()); 
		sigma.setFuncGenV(new String[]{Constants.GF_FIBONACCI},singleton.getKernelLink());
		adeServiceImpl.setSigma(sigma);
		String[][] m = adeServiceImpl.complete(); 
		adeServiceImpl.obtainRecurrenceAndGF(m);
		
		result[0] = adeServiceImpl.getDn();	
		//assertTrue("{1, 2, 7, 18, 47, 118, 293, 720, 1759}".equals(result[0]));
		return result;
	}
	
	public String[] eighthTest() throws MathLinkException, AdeException {
		
		String[] result = new String[3];
		
		logger.info("Eighth Test: Delannoy (1,1) scheme");
		
		Sigma sigma = new Sigma(1,1,"DelannoyEighth");
		sigma.setFuncGenH(new String[]{Constants.GF_EXPONENTIAL},singleton.getKernelLink()); 
		sigma.setFuncGenV(new String[]{"(1 - x)^-1"},singleton.getKernelLink());
		adeServiceImpl.setSigma(sigma);
		String[][] m = adeServiceImpl.complete(); 
		adeServiceImpl.obtainRecurrenceAndGF(m);
		
		result[0] = adeServiceImpl.getDn();	
		//assertTrue("{1, 3, 10, 33, 109, 360, 1189, 3927, 12970}".equals(result[0]));
		return result;
	}
	
	/**
	 * ninethTest
	 * is an empty method
	 * @param ml
	 * @return
	 * @throws MathLinkException
	 * @throws AdeException
	 */
	public String ninethTest() throws MathLinkException, AdeException {
		return "this is an empty method!";
		
	}
	
	public String[] tenthTest() throws MathLinkException, AdeException {
		
		String[] result = new String[3];
		
		logger.info("Tenth Test: Delannoy (1,1) scheme");
		
		Sigma sigma = new Sigma(1,1,"DelannoyEighth");
		sigma.setFuncGenH(new String[]{"(1 - x)^-1"},singleton.getKernelLink()); 
		sigma.setFuncGenV(new String[]{Constants.GF_EXPONENTIAL},singleton.getKernelLink());
		adeServiceImpl.setSigma(sigma);
		String[][] m = adeServiceImpl.complete(); 
		adeServiceImpl.obtainRecurrenceAndGF(m);
		
		result[0] = adeServiceImpl.getDn();	
		//assertTrue("{1, 3, 11, 39, 135, 459, 1543, 5151, 17123}".equals(result[0]));
		return result;
	}
	
	public String[] eleventhTest() throws MathLinkException, AdeException {		
		
		String[] result = new String[3];
		
		logger.info("Eleventh Test: Delannoy (1,1) scheme");
		
		Sigma sigma = new Sigma(1,1,"Delannoy1");
		sigma.setFuncGenH(new String[]{"(1 - x)^-1"},singleton.getKernelLink()); 
		sigma.setFuncGenV(new String[]{Constants.GF_FIBONACCI},singleton.getKernelLink());
		adeServiceImpl.setSigma(sigma);
		String[][] m = adeServiceImpl.complete(); 
		adeServiceImpl.obtainRecurrenceAndGF(m);
		
		result[0] = adeServiceImpl.getDn();		
		//assertTrue("{1, 2, 6, 15, 38, 94, 231, 564, 1372}".equals(result[0]));
		return result;
	}
	
	/**
	 * fakeTest
	 * 2. An example: h = 1, and v = 2
	 * @param ml
	 * @return
	 * @throws MathLinkException
	 * @throws AdeException
	 */
	public String[] fakeTest() throws MathLinkException, AdeException {
		
		String[] result = new String[8];
		
		logger.info("fakeTest: sample (1,2) scheme");
		Sigma sigma = new Sigma(1,2,"fakeSchema");
		adeServiceImpl.setSigma(sigma);
		String[][] m = adeServiceImpl.fakeComplete();
		adeServiceImpl.obtainRecurrenceAndGF(m);
		
		String d [] = adeServiceImpl.getD();
		result[0] = d[0];	
		result[1] = d[1];
		result[2] = d[2];
		result[3] = adeServiceImpl.getCBL();
		result[4] = adeServiceImpl.getCTR();
		result[5] = adeServiceImpl.getKER();
		result[6] = adeServiceImpl.getEBL();
		result[7] = adeServiceImpl.getETR();
		
//	    assertTrue("0  + m_0,0".equals(result[0]));
//	    assertTrue("0  + m_0,1 + m_1,0".equals(result[1]));
//	    assertTrue("0  + m_0,2 + m_1,1 + m_2,0".equals(result[2]));
//	    assertTrue("0 + m_6,0 + m_5,1".equals(result[3]));
//	    assertTrue("0 + m_0,6".equals(result[4]));
//	    assertTrue("0 + (0 + c_1,2) *  d_n-3 + (0 + c_1,1 + c_0,2) *  d_n-2 + (0 + c_1,0 + c_0,1) *  d_n-1".equals(result[5]));
//	    assertTrue("0 + c_1,0 * m_5,0 + c_1,0 * m_4,1 + c_0,1 * m_5,0 + c_1,1 * m_4,0".equals(result[6]));
//	    assertTrue("0 + c_0,1 * m_0,5 + c_0,2 * m_0,4".equals(result[7]));		
		
		return result;
	}
	
	/**
	 * anotherFakeTest
	 * 4. Isodiagonality, and the (1; 1) scheme
	 * @param ml
	 * @return
	 * @throws MathLinkException
	 * @throws AdeException
	 */
	public String[] anotherFakeTest() throws MathLinkException, AdeException {
		String[] result = new String[7];
		
		logger.info("anotherFakeTest: sample (1,1) scheme");
		Sigma sigma = new Sigma(1,1,"anotherFakeSchema");
		adeServiceImpl.setSigma(sigma);
		String[][] m = adeServiceImpl.fakeComplete();
		adeServiceImpl.obtainRecurrenceAndGF(m);

		String d [] = adeServiceImpl.getD();
		result[0] = d[0];	
		result[1] = d[1];
		result[2] = adeServiceImpl.getCBL();
		result[3] = adeServiceImpl.getCTR();
		result[4] = adeServiceImpl.getKER();
		result[5] = adeServiceImpl.getEBL();
		result[6] = adeServiceImpl.getETR();
		
//	    assertTrue("0  + m_0,0".equals(result[0]));
//	    assertTrue("0  + m_0,1 + m_1,0".equals(result[1]));
//	    assertTrue("0 + m_6,0".equals(result[2]));
//	    assertTrue("0 + m_0,6".equals(result[3]));
//	    assertTrue("0 + (0 + c_1,1) *  d_n-2 + (0 + c_1,0 + c_0,1) *  d_n-1".equals(result[4]));
//	    assertTrue("0 + c_1,0 * m_5,0".equals(result[5]));
//	    assertTrue("0 + c_0,1 * m_0,5".equals(result[6]));
		
		return result;
	}
}
