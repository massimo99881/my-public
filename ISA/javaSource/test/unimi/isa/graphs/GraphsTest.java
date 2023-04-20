package test.unimi.isa.graphs;

import it.unimi.isa.beans.PlaceholderBean;
import it.unimi.isa.model.graphs.Graph;
import it.unimi.isa.service.impl.IndepSetServiceImpl;
import it.unimi.isa.service.impl.LaTexFormatterServiceImpl;
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

/**
 * Classe che stampa tutti i disegni e le tabelle
 * in un unico foglio
 * @author Administrator
 *
 */
@Component
public class GraphsTest {
	
	private static final Logger logger = Logger.getLogger(GraphsTest.class);
	
	private int count;
	
	@Autowired
	LaTexFormatterServiceImpl laTexFormatterServiceImpl;
	
	final static String workingPath = "resources//test-jlr";

    private void stampa() throws Exception {
    	
    	File workingDirectory = new File(workingPath);
        /**
         * NOTA: per il template la libreria JLR non vuole nessuna cartella dopo quella definita in workingDirectory
         * Esempio non consentito:
         * File template = new File(workingDirectory.getAbsolutePath() + "//template//templateTestGraphs.tex");
         */
        File template = new File(workingDirectory.getAbsolutePath() + "//templateTestGraphs.tex");
        File tempDir = new File(workingDirectory.getAbsolutePath() + File.separator + "temp");
        if (!tempDir.isDirectory()) {
            tempDir.mkdir();
        }

        File invoice1 = new File(tempDir.getAbsolutePath() + File.separator + "testGraphs.tex");
       
         try {
            JLRConverter converter = new JLRConverter(workingDirectory);
            
            PlaceholderBean pb = new PlaceholderBean();
            pb.setTitle("Graphs Test");
            count = 1;
            pb = prepareGraphics(new String[]{"indep","p_2_1","x","p_4_1"}, pb);
            pb = prepareGraphics(new String[]{"indep","p_1_1","x","p_4_1"}, pb);
            pb = prepareGraphics(new String[]{"indep","p_2_1","x","p_4_2"}, pb);
            pb = prepareGraphics(new String[]{"indep","p_1_1","x","p_4_2"}, pb);
            pb = prepareGraphics(new String[]{"indep","p_2_1","x","z_4_1"}, pb);
            pb = prepareGraphics(new String[]{"indep","p_2_1","x","f_4_1"}, pb);
            pb = prepareGraphics(new String[]{"indep","p_2_1","x","f_5_2"}, pb);
            pb = prepareGraphics(new String[]{"indep","p_2_1","x","z_5_2"}, pb);
            pb = prepareGraphics(new String[]{"indep","p_2_1","x","c_5_1"}, pb);
            pb = prepareGraphics(new String[]{"indep","p_1_1","x","c_5_1"}, pb);
            pb = prepareGraphics(new String[]{"indep","p_2_1","x","cf_5_1"}, pb);
            pb = prepareGraphics(new String[]{"indep","p_2_1","x","cz_5_1"}, pb);
            pb = prepareGraphics(new String[]{"indep","p_3_2","x","cz_4_1"}, pb);
            pb = prepareGraphics(new String[]{"indep","p_2_1","x","m_5_1"}, pb);
            pb = prepareGraphics(new String[]{"indep","p_2_1","x","c_5_3"}, pb);
            pb = prepareGraphics(new String[]{"indep","p_3_1","x","p_4_3"}, pb);
            pb = prepareGraphics(new String[]{"indep","p_4_3","x","p_3_2"}, pb);
            pb = prepareGraphics(new String[]{"indep","p_4_2","x","p_3_2"}, pb);
            pb = prepareGraphics(new String[]{"indep","p_4_3","x","p_4_3"}, pb);			
			//
            
            converter.replace("title", pb.getTitle());
            
            converter.replace("section1", pb.getSection1());
            converter.replace("section2", pb.getSection2());
            converter.replace("section3", pb.getSection3());
            converter.replace("section4", pb.getSection4());
            converter.replace("section5", pb.getSection5());
            converter.replace("section6", pb.getSection6());
            converter.replace("section7", pb.getSection7());
            converter.replace("section8", pb.getSection8());
            converter.replace("section9", pb.getSection9());
            converter.replace("section10", pb.getSection10());
            converter.replace("section11", pb.getSection11());
            converter.replace("section12", pb.getSection12());
            converter.replace("section13", pb.getSection13());
            converter.replace("section14", pb.getSection14());
            converter.replace("section15", pb.getSection15());
            converter.replace("section16", pb.getSection16());
            converter.replace("section17", pb.getSection17());
            converter.replace("section18", pb.getSection18());
            converter.replace("section19", pb.getSection19());
            
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
            converter.replace("output1o11", pb.getOutput1o11());
            converter.replace("output1o12", pb.getOutput1o12());
            
            converter.replace("output1o13", pb.getOutput1o13());
            converter.replace("output1o14", pb.getOutput1o14());
            converter.replace("output1o15", pb.getOutput1o15());
            converter.replace("output1o16", pb.getOutput1o16());
            converter.replace("output1o17", pb.getOutput1o17());
            converter.replace("output1o18", pb.getOutput1o18());
            
            converter.replace("output1o19", pb.getOutput1o19());
            converter.replace("output1o20", pb.getOutput1o20());
            converter.replace("output1o21", pb.getOutput1o21());
            converter.replace("output1o22", pb.getOutput1o22());
            converter.replace("output1o23", pb.getOutput1o23());
            converter.replace("output1o24", pb.getOutput1o24());
            converter.replace("output1o25", pb.getOutput1o25());
            converter.replace("output1o26", pb.getOutput1o26());
            converter.replace("output1o27", pb.getOutput1o27());
            converter.replace("output1o28", pb.getOutput1o28());
            converter.replace("output1o29", pb.getOutput1o29());
            converter.replace("output1o30", pb.getOutput1o30());
            converter.replace("output1o31", pb.getOutput1o31());
            converter.replace("output1o32", pb.getOutput1o32());
            converter.replace("output1o33", pb.getOutput1o33());
            converter.replace("output1o34", pb.getOutput1o34());
            
            converter.replace("output1o35", pb.getOutput1o35());
            converter.replace("output1o36", pb.getOutput1o36());
            converter.replace("output1o37", pb.getOutput1o37());
            converter.replace("output1o38", pb.getOutput1o38());
            
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
    
    private PlaceholderBean prepareGraphics(String [] graphDescriptor, PlaceholderBean pb) throws Exception{
    	Graph g = Utils.ottieneGrafo(graphDescriptor, 1, 3);
        IndepSetServiceImpl tester = new IndepSetServiceImpl();
        List<HashMap<Integer,List<Integer>>> indepList = tester.forzaBruta(g);
        String nome = g.getNome();
    	String disegno = g.createForLatex();
    	String tabella = laTexFormatterServiceImpl.formatIndependentMatrix(indepList,g);
    	
		pb = posizionaStampa(nome, disegno, tabella, pb, count++);
		return pb;
    }
   
    
    private PlaceholderBean posizionaStampa(String nome, String disegno, String tabella, PlaceholderBean pb, int count){
    	
    	if(count==1){
    		pb.setSection1(nome);
            pb.setOutput1o1(disegno);
            pb.setOutput1o2(tabella);
    	}
    	if(count==2){
    		pb.setSection2(nome);
            pb.setOutput1o3(disegno);
            pb.setOutput1o4(tabella);
    	}
    	if(count==3){
    		pb.setSection3(nome);
            pb.setOutput1o5(disegno);
            pb.setOutput1o6(tabella);
    	}
    	if(count==4){
    		pb.setSection4(nome);
            pb.setOutput1o7(disegno);
            pb.setOutput1o8(tabella);
    	}
    	if(count==5){
    		pb.setSection5(nome);
            pb.setOutput1o9(disegno);
            pb.setOutput1o10(tabella);
    	}
    	if(count==6){
    		pb.setSection6(nome);
            pb.setOutput1o11(disegno);
            pb.setOutput1o12(tabella);
    	}
    	if(count==7){
    		pb.setSection7(nome);
            pb.setOutput1o13(disegno);
            pb.setOutput1o14(tabella);
    	}
    	if(count==8){
    		pb.setSection8(nome);
            pb.setOutput1o15(disegno);
            pb.setOutput1o16(tabella);
    	}
    	if(count==9){
    		pb.setSection9(nome);
            pb.setOutput1o17(disegno);
            pb.setOutput1o18(tabella);
    	}
		if(count==10){
    		pb.setSection10(nome);
            pb.setOutput1o19(disegno);
            pb.setOutput1o20(tabella);
    	}
		if(count==11){
    		pb.setSection11(nome);
            pb.setOutput1o21(disegno);
            pb.setOutput1o22(tabella);
    	}
		if(count==12){
    		pb.setSection12(nome);
            pb.setOutput1o23(disegno);
            pb.setOutput1o24(tabella);
    	}
		if(count==13){
    		pb.setSection13(nome);
            pb.setOutput1o25(disegno);
            pb.setOutput1o26(tabella);
    	}
		if(count==14){
    		pb.setSection14(nome);
            pb.setOutput1o27(disegno);
            pb.setOutput1o28(tabella);
    	}
		if(count==15){
    		pb.setSection15(nome);
            pb.setOutput1o29(disegno);
            pb.setOutput1o30(tabella);
    	}
		if(count==16){
    		pb.setSection16(nome);
            pb.setOutput1o31(disegno);
            pb.setOutput1o32(tabella);
    	}
		if(count==17){
    		pb.setSection17(nome);
            pb.setOutput1o33(disegno);
            pb.setOutput1o34(tabella);
    	}
		if(count==18){
    		pb.setSection18(nome);
            pb.setOutput1o35(disegno);
            pb.setOutput1o36(tabella);
    	}
		if(count==19){
    		pb.setSection19(nome);
            pb.setOutput1o37(disegno);
            pb.setOutput1o38(tabella);
    	}
        
        return pb;
    }

	public static void main(String[] args) throws Exception {
		logger.info("######## INIZIO TEST GraphsTest ########");
    	final ApplicationContext context = new ClassPathXmlApplicationContext(Constants.CONFIG_PATH);
		final GraphsTest graphTest = context.getBean(GraphsTest.class);
		graphTest.stampa();
		logger.info("######## FINE TEST GraphsTest ########");
    }
}
