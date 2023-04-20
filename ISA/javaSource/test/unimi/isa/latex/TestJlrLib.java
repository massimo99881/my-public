package test.unimi.isa.latex;

import it.unimi.isa.beans.PlaceholderBean;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import de.nixosoft.jlr.JLRConverter;
import de.nixosoft.jlr.JLRGenerator;
import de.nixosoft.jlr.JLROpener;

/**
 * TEST UTILIZZO LIBRERIA JAVA JLR: 
 * Definito un template tex, genera il file tex riempiendo i placeholder usando velocity
 * Infine genera il pdf 
 * Compilazione: Latex
 * @author Administrator
 *
 */
public class TestJlrLib {
	
	private static final Logger logger = Logger.getLogger(TestJlrLib.class);
	
	final static String workingPath = "resources//test-jlr";

    public static void main(String[] args) {
    	System.out.println("######## INIZIO TEST TestJlrLib ########");

        File workingDirectory = new File(workingPath);
        /**
         * NOTA: per il template la libreria JLR non vuole nessuna cartella dopo quella definita in workingDirectory
         * Esempio non consentito:
         * File template = new File(workingDirectory.getAbsolutePath() + "//template//templateTestJlr.tex");
         */
        File template = new File(workingDirectory.getAbsolutePath() + "//templateTestJlr.tex");
        File tempDir = new File(workingDirectory.getAbsolutePath() + File.separator + "temp");
        if (!tempDir.isDirectory()) {
            tempDir.mkdir();
        }

        File invoice1 = new File(tempDir.getAbsolutePath() + File.separator + "testJlrLib.tex");
       
         try {
            JLRConverter converter = new JLRConverter(workingDirectory);
            
            PlaceholderBean pb = new PlaceholderBean();
            pb.setTitle("Independent Sets Algorithm");
            pb.setSection1("Calcolo valori tabella");
            pb.setOutput1o1("");
            pb.setOutput1o2("");
            pb.setOutput1o3("");
            pb.setOutput1o4("");
            pb.setOutput1o5("");
            pb.setOutput1o6("");
            
            converter.replace("title", pb.getTitle());
            converter.replace("section1", pb.getSection1());
            converter.replace("output1o1", pb.getOutput1o1());
            converter.replace("output1o2", pb.getOutput1o2());
            converter.replace("output1o3", pb.getOutput1o3());
            converter.replace("output1o4", pb.getOutput1o4());
            converter.replace("output1o5", pb.getOutput1o5());
            converter.replace("output1o6", pb.getOutput1o6());
            
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
        System.out.println("######## FINE TEST TestJlrLib ########");
    }
}