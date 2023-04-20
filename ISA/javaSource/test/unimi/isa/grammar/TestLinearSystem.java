package test.unimi.isa.grammar;

import it.unimi.isa.beans.PlaceholderBean;
import it.unimi.isa.exceptions.GraphException;
import it.unimi.isa.model.Bacchetta;
import it.unimi.isa.model.GrammarLanguage;
import it.unimi.isa.model.graphs.Graph;
import it.unimi.isa.service.impl.GrammarServiceImpl;
import it.unimi.isa.service.impl.IndepSetServiceImpl;
import it.unimi.isa.service.impl.LaTexFormatterServiceImpl;
import it.unimi.isa.service.impl.WilfServiceImpl;
import it.unimi.isa.utils.Constants;
import it.unimi.isa.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import de.nixosoft.jlr.JLRConverter;
import de.nixosoft.jlr.JLRGenerator;
import de.nixosoft.jlr.JLROpener;

@Component
public class TestLinearSystem {
	
	private static final Logger logger = Logger.getLogger(TestLinearSystem.class);

	final static String workingPath = "resources//test-jlr";
	
	@Autowired
	LaTexFormatterServiceImpl laTexFormatterServiceImpl;
	
	@Autowired
	IndepSetServiceImpl indepSetServiceImpl;
	
	@Autowired
	WilfServiceImpl wilfServiceImpl;
	
	@Autowired
	GrammarServiceImpl grammarServiceImpl;
	
	public static void main(String args[]) throws Exception {
		logger.info("######## INIZIO TEST TestLinearSystem ########");
		
		ApplicationContext context = new ClassPathXmlApplicationContext(Constants.CONFIG_PATH);
		TestLinearSystem testLinearSystem = context.getBean(test.unimi.isa.grammar.TestLinearSystem.class);
		
//		testLinearSystem.eseguiTest("p_2_1","p_6_1"); //{1, 3, 7, 17, 41, 99, 239, 577, 1393, 3363}
//		testLinearSystem.eseguiTest("p_2_1","p_6_2"); //{1, ?, 7, 13, 27, 57, 117, 241, 499, 1031, 2129}
		
//		testLinearSystem.eseguiTest("p_2_1","c_5_1"); //NON POSSIAMO USARE WILF PER I CIRCUITI!
		testLinearSystem.eseguiTest("p_3_1","f_5_1"); //{1, 5, 13, 42, 126, 387, 1180, 3606, 11012, 33636}
//		testLinearSystem.eseguiTest("p_2_1","f_5_2"); //{1, 6, 11, 22, 44, 86, 169, 334, 658, 1295, 2552}
//		testLinearSystem.eseguiTest("p_4_3","p_4_2"); //{1, ?, 21, 73, 261, 945, 3409, 12289, 44317, 159821, 576345}
//		testLinearSystem.eseguiTest("p_2_1","m_4_1"); //NON POSSIAMO USARE WILF PER I CIRCUITI!
//		
//		testLinearSystem.eseguiTest("p_4_2","p_3_2"); //{1, ?, 27, 94, 356, 1369, 5167, 19569, 74358, 281925, 1068716}
//		testLinearSystem.eseguiTest("p_5_3","p_3_1"); //{1, 7, 39, 223, 1267, 7213, 41037, 233521, 1328761, 7560967}
//		testLinearSystem.eseguiTest("p_4_3","p_5_2"); //{1, ?, 21, 73,  261, 945, 3409, 12289, 44317, 159821, 576345}
//		testLinearSystem.eseguiTest("p_4_3","p_4_3"); //{1, ?,  ?, 73, 209, 637, 2005, 6365, 19957, 62393, 195261, 612041, 1918365}
//		testLinearSystem.eseguiTest("p_2_1","z_5_2"); //{1, ?,  5,  9,  17, 31, 57, 105, 193, 355, 653, 1201}
//		testLinearSystem.eseguiTest("p_4_3","p_3_1"); //{1, 5, 21, 89, 377, 1597, 6765, 28657, 121393, 514229, 2178309}
//		testLinearSystem.eseguiTest("p_3_1","z_4_1"); //{1, 5, 11, 35, 93,  269, 747, 2115, 5933, 16717, 47003}
		
		logger.info("######## FINE TEST TestLinearSystem ########");
	}

    private void stampa(PlaceholderBean pb, String nomeFile) throws GraphException {
    	File workingDirectory = new File(workingPath);
        /**
         * NOTA: per il template la libreria JLR non vuole nessuna cartella dopo quella definita in workingDirectory
         * Esempio non consentito:
         * File template = new File(workingDirectory.getAbsolutePath() + "//template//templateTestGraphs.tex");
         */
        File template = new File(workingDirectory.getAbsolutePath() + "//templateTestLinearSystem.tex");
        File tempDir = new File(workingDirectory.getAbsolutePath() + File.separator + "temp");
        if (!tempDir.isDirectory()) {
            tempDir.mkdir();
        }

        File invoice1 = new File(tempDir.getAbsolutePath() + File.separator + "" +nomeFile+ ".tex");
       
         try {
            JLRConverter converter = new JLRConverter(workingDirectory);
            
            converter.replace("title", pb.getTitle());            
            converter.replace("section1", pb.getSection1());
            converter.replace("output1o1", pb.getOutput1o1());
            converter.replace("output1o2", pb.getOutput1o2());
            converter.replace("output1o3", pb.getOutput1o3());
            converter.replace("output1o4", pb.getOutput1o4());
            converter.replace("output1o5", pb.getOutput1o5());
            converter.replace("output1o6", pb.getOutput1o6());
            converter.replace("output1o7", pb.getOutput1o7());
            converter.replace("output1o8", pb.getOutput1o8());
            converter.replace("output1o9", pb.getOutput1o9());
            converter.replace("output1o10", pb.getOutput1o10());
            
            if (!converter.parse(template, invoice1)) {
                logger.info(converter.getErrorMessage());
            }

            File desktop = new File(workingPath + "//temp"); 

            JLRGenerator pdfGen = new JLRGenerator();           

            if (!pdfGen.generate(invoice1, desktop, workingDirectory)) { 
            	logger.info(pdfGen.getErrorMessage());
            }

            JLROpener.open(pdfGen.getPDF());

        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
	}
    
    public void eseguiTest(String p1, String p2) throws Exception {
    	
    	PlaceholderBean pb = new PlaceholderBean();
        pb.setTitle("Linear System Test");
        Graph g = Utils.ottieneGrafo(new String[]{"indep",p1,"x",p2}, 1, 3);
        List<HashMap<Integer,List<Integer>>> listIndip = indepSetServiceImpl.forzaBruta(g);
        pb.setSection1(g.getNome());        
        pb.setOutput1o1(g.createForLatex());        
        pb.setOutput1o2(laTexFormatterServiceImpl.formatIndependentMatrix(listIndip, g));
        //lettere alfabeto
        List<Bacchetta> alfabeto = grammarServiceImpl.ottieneLettereAlfabeto(g);            
        pb.setOutput1o3(laTexFormatterServiceImpl.formatLettereAlfabeto(alfabeto));
        //Sistema Lineare e TM
        GrammarLanguage paroleLinguaggio = grammarServiceImpl.ottieneParoleConsentite(alfabeto,g);
//		List<String[][]> paroleConsentite = paroleLinguaggio.getParoleConsentite();
		HashMap <String,List<String>> sistemaProseguimento = paroleLinguaggio.getSistemaProseguimento();
        pb.setOutput1o4(laTexFormatterServiceImpl.formatProseguimentoSistema(sistemaProseguimento,grammarServiceImpl.getLetteraFGO()));
		HashMap <String,List<String>> sistema = grammarServiceImpl.ottieneSistemaLineareLinguaggio(sistemaProseguimento,g);
		pb.setOutput1o5(laTexFormatterServiceImpl.formatSistemaLineare(sistema,grammarServiceImpl.getLetteraFGO()));
		String[][] xTM = grammarServiceImpl.ottieneTransferMatrix(sistema);
		pb.setOutput1o6(laTexFormatterServiceImpl.formatTransferMatrix(xTM,sistema));
		//FGO indipendenti RSn
		String resTM[] = wilfServiceImpl.findRSnFromTM_v3(xTM,g);
		pb.setOutput1o7(laTexFormatterServiceImpl.formatFgo(resTM[5], "f", "x"));
        //Espansione in serie RSn
        pb.setOutput1o8(laTexFormatterServiceImpl.formatEspansioneInSerie(resTM[9], "RS"));
        pb.setOutput1o9("");
        pb.setOutput1o10("");
        stampa(pb,g.getNomeFile());
	}

}
