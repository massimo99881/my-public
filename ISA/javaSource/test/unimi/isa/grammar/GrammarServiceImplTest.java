package test.unimi.isa.grammar;

import it.unimi.isa.exceptions.GrammarException;
import it.unimi.isa.exceptions.GraphException;
import it.unimi.isa.model.Bacchetta;
import it.unimi.isa.model.GrammarLanguage;
import it.unimi.isa.model.graphs.Graph;
import it.unimi.isa.service.impl.GrammarServiceImpl;
import it.unimi.isa.service.impl.WilfServiceImpl;
import it.unimi.isa.utils.Constants;
import it.unimi.isa.utils.Utils;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class GrammarServiceImplTest {
	
	private static final Logger logger = Logger.getLogger(GrammarServiceImplTest.class);

	@Autowired
	GrammarServiceImpl grammarServiceImpl;
	
	@Autowired
	WilfServiceImpl wilfServiceImpl;
	
	/**
	 * TEST
	 */
	public static void main(String args[]) throws GraphException, GrammarException {
		logger.info("######## INIZIO TEST GrammarServiceImplTest ########");
		
		final ApplicationContext context = new ClassPathXmlApplicationContext(Constants.CONFIG_PATH);
		final GrammarServiceImplTest grammarServiceImplTest = context.getBean(GrammarServiceImplTest.class);
		
		//grammarServiceImplTest.testGnk(); //{3, 7, 17, 41, 99, 239, 577, 1393, 3363, 8119}
		//grammarServiceImplTest.testGnk2(); // {5, 17, 63, 227, 827, 2999, 10897, 39561, 143677, 521721}
		//grammarServiceImplTest.testGnk3(); //{13, 99, 827, 6743, 55447, 454385, 3729091, 30584687, 250916131, 2058249165}
		//grammarServiceImplTest.testTnk(); //{7, 13, 27, 57, 117, 241, 499, 1031, 2129, 4399}
		//grammarServiceImplTest.testTnk2(); //{17, 43, 127, 381, 1099, 3197, 9367, 27317, 79631, 232485}
		grammarServiceImplTest.testZnk(); //{3, 5, 11, 21, 43, 85, 171, 341, 683, 1365}
		

		logger.info("######## FINE TEST GrammarServiceImplTest ########");
	}
	
	private void testZnk() throws GraphException, GrammarException {
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_2_1","x","z_4_1"}, 1, 3);
		List<Bacchetta> alfabeto = grammarServiceImpl.ottieneLettereAlfabeto(g);
		GrammarLanguage paroleLinguaggio = grammarServiceImpl.ottieneParoleConsentite(alfabeto,g);
		@SuppressWarnings("unused")
		List<String[][]> paroleConsentite = paroleLinguaggio.getParoleConsentite();
		HashMap <String,List<String>> sistemaProseguimento = paroleLinguaggio.getSistemaProseguimento();
		HashMap <String,List<String>> sistema = grammarServiceImpl.ottieneSistemaLineareLinguaggio(sistemaProseguimento, g);
//		String fgoGrammatica = ottieneFgoGrammatica(sistema);
		String[][] xTM = grammarServiceImpl.ottieneTransferMatrix(sistema);
		
		wilfServiceImpl.findRSnFromTM_v2(xTM);
	}
	
	@SuppressWarnings("unused")
	private void testGnk() throws GraphException, GrammarException {
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_2_1","x","p_4_1"}, 1, 3);		
		List<Bacchetta> alfabeto = grammarServiceImpl.ottieneLettereAlfabeto(g);
		GrammarLanguage paroleLinguaggio = grammarServiceImpl.ottieneParoleConsentite(alfabeto,g);
		List<String[][]> paroleConsentite = paroleLinguaggio.getParoleConsentite();
		HashMap <String,List<String>> sistemaProseguimento = paroleLinguaggio.getSistemaProseguimento();
		HashMap <String,List<String>> sistema = grammarServiceImpl.ottieneSistemaLineareLinguaggio(sistemaProseguimento, g);
		String[][] xTM = grammarServiceImpl.ottieneTransferMatrix(sistema);
		
		wilfServiceImpl.findRSnFromTM_v2(xTM);
	}
	
	@SuppressWarnings("unused")
	private void testGnk2() throws GraphException, GrammarException {
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_3_1","x","z_4_1"}, 1, 3);
		List<Bacchetta> alfabeto = grammarServiceImpl.ottieneLettereAlfabeto(g);
		GrammarLanguage paroleLinguaggio = grammarServiceImpl.ottieneParoleConsentite(alfabeto,g);
		List<String[][]> paroleConsentite = paroleLinguaggio.getParoleConsentite();
		HashMap <String,List<String>> sistemaProseguimento = paroleLinguaggio.getSistemaProseguimento();
		HashMap <String,List<String>> sistema = grammarServiceImpl.ottieneSistemaLineareLinguaggio(sistemaProseguimento, g);
		String[][] xTM = grammarServiceImpl.ottieneTransferMatrix(sistema);
		
		wilfServiceImpl.findRSnFromTM_v2(xTM);
	}
	
	@SuppressWarnings("unused")
	private void testGnk3() throws GraphException, GrammarException {
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_5_1","x","z_4_1"}, 1, 3);
		List<Bacchetta> alfabeto = grammarServiceImpl.ottieneLettereAlfabeto(g);
		GrammarLanguage paroleLinguaggio = grammarServiceImpl.ottieneParoleConsentite(alfabeto,g);
		List<String[][]> paroleConsentite = paroleLinguaggio.getParoleConsentite();
		HashMap <String,List<String>> sistemaProseguimento = paroleLinguaggio.getSistemaProseguimento();
		HashMap <String,List<String>> sistema = grammarServiceImpl.ottieneSistemaLineareLinguaggio(sistemaProseguimento, g);
		String[][] xTM = grammarServiceImpl.ottieneTransferMatrix(sistema);
		
		wilfServiceImpl.findRSnFromTM_v2(xTM);
	}

	@SuppressWarnings("unused")
	private void testTnk() throws GraphException, GrammarException {
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_2_1","x","z_4_2"}, 1, 3);		
		List<Bacchetta> alfabeto = grammarServiceImpl.ottieneLettereAlfabeto(g);
		GrammarLanguage paroleLinguaggio = grammarServiceImpl.ottieneParoleConsentite(alfabeto,g);
		List<String[][]> paroleConsentite = paroleLinguaggio.getParoleConsentite();
		HashMap <String,List<String>> sistemaProseguimento = paroleLinguaggio.getSistemaProseguimento();
		HashMap <String,List<String>> sistema = grammarServiceImpl.ottieneSistemaLineareLinguaggio(sistemaProseguimento, g);
		String[][] xTM = grammarServiceImpl.ottieneTransferMatrix(sistema);
		
		wilfServiceImpl.findRSnFromTM_v2(xTM);
	}
	
	@SuppressWarnings("unused")
	private void testTnk2() throws GraphException, GrammarException {
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_3_1","x","z_4_1"}, 1, 3);		
		List<Bacchetta> alfabeto = grammarServiceImpl.ottieneLettereAlfabeto(g);
		GrammarLanguage paroleLinguaggio = grammarServiceImpl.ottieneParoleConsentite(alfabeto,g);
		List<String[][]> paroleConsentite = paroleLinguaggio.getParoleConsentite();
		HashMap <String,List<String>> sistemaProseguimento = paroleLinguaggio.getSistemaProseguimento();
		HashMap <String,List<String>> sistema = grammarServiceImpl.ottieneSistemaLineareLinguaggio(sistemaProseguimento, g);
		String[][] xTM = grammarServiceImpl.ottieneTransferMatrix(sistema);
		
		wilfServiceImpl.findRSnFromTM_v2(xTM);
	}
}
