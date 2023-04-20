package test.unimi.isa.indepsets;

import it.unimi.isa.model.Bacchetta;
import it.unimi.isa.model.GrammarLanguage;
import it.unimi.isa.model.graphs.Graph;
import it.unimi.isa.service.impl.GrammarServiceImpl;
import it.unimi.isa.service.impl.IndepSetServiceImpl;
import it.unimi.isa.service.impl.LaTexFormatterServiceImpl;
import it.unimi.isa.service.impl.WilfServiceImpl;
import it.unimi.isa.utils.Constants;
import it.unimi.isa.utils.ISAConsole;
import it.unimi.isa.utils.Utils;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import de.nixosoft.jlr.JLRConverter;

@Service
public class RicorrenzaLocalePrinter {
	
	private static final Logger logger = Logger.getLogger(RicorrenzaLocalePrinter.class);
	
	@Autowired
	IndepSetServiceImpl indepSetServiceImpl;
	
	@Autowired
	LaTexFormatterServiceImpl laTexFormatterServiceImpl;
	
	@Autowired
	GrammarServiceImpl grammarServiceImpl;
	
	@Autowired
	WilfServiceImpl wilfServiceImpl;

	public static void main(String args[]) throws Exception{
		logger.info("######## INIZIO TEST RicorrenzaLocalePrinter ########");
		final ApplicationContext context = new ClassPathXmlApplicationContext(Constants.CONFIG_PATH);
		final RicorrenzaLocalePrinter ricorrenzaLocalePrinter = context.getBean(RicorrenzaLocalePrinter.class);
		
		String title = "Ricorrenze Locali";
		String section = "Ricorrenze Locali";
		String output = "\\bigskip \\bigskip \\bigskip \n";
		
		/**
		 * P_n
		 */
		output += ricorrenzaLocalePrinter.stampaRicorrenzaGrafo(new String[]{"indep","p_1_1", "x", "p_8_1"});
		output += ricorrenzaLocalePrinter.stampaRicorrenzaGrafo(new String[]{"indep","p_1_1", "x", "p_8_2"});
		output += ricorrenzaLocalePrinter.stampaRicorrenzaGrafo(new String[]{"indep","p_1_1", "x", "p_8_3"});
		output += ricorrenzaLocalePrinter.stampaRicorrenzaGrafo(new String[]{"indep","p_1_1", "x", "p_8_e3"});
		output += ricorrenzaLocalePrinter.stampaRicorrenzaGrafo(new String[]{"indep","p_2_1", "x", "p_7_1"});
		output += ricorrenzaLocalePrinter.stampaRicorrenzaGrafo(new String[]{"indep","p_2_1", "x", "p_7_2"});
		/**
		 * Z_n
		 */
		output += ricorrenzaLocalePrinter.stampaRicorrenzaGrafo(new String[]{"indep","p_2_1", "x", "z_7_1"});
		output += ricorrenzaLocalePrinter.stampaRicorrenzaGrafo(new String[]{"indep","p_2_1", "x", "z_7_2"});
		//output += ricorrenzaLocalePrinter.stampaRicorrenzaGrafo(new String[]{"indep","p_3_1", "x", "z_4_1"});
		/**
		 * F_n
		 */
		output += ricorrenzaLocalePrinter.stampaRicorrenzaGrafo(new String[]{"indep","p_2_1", "x", "f_7_1"});
		
		//TODO..
//		/**
//		 * C_n
//		 */
//		output += ricorrenzaLocalePrinter.stampaRicorrenzaGrafo(new String[]{"indep","p_1_1", "x", "c_7_1"});
//		output += ricorrenzaLocalePrinter.stampaRicorrenzaGrafo(new String[]{"indep","p_1_1", "x", "c_7_2"});
//		output += ricorrenzaLocalePrinter.stampaRicorrenzaGrafo(new String[]{"indep","p_2_1", "x", "c_7_1"});
//		/**
//		 * H_n
//		 */
//		output += ricorrenzaLocalePrinter.stampaRicorrenzaGrafo(new String[]{"indep","p_1_1", "x", "h_7_1"});
//		output += ricorrenzaLocalePrinter.stampaRicorrenzaGrafo(new String[]{"indep","p_1_1", "x", "h_7_2"});
		
		
		String result = ricorrenzaLocalePrinter.printAll(title, section , output);
		logger.info(result);
		
		logger.info("######## FINE TEST RicorrenzaLocalePrinter ########");
	}

	

	private String stampaRicorrenzaGrafo(String [] graphtype) throws Exception {
		Graph g = Utils.ottieneGrafo(graphtype, 1, 3);
		List<HashMap<Integer,List<Integer>>> indepList = indepSetServiceImpl.forzaBruta(g);
        
//		String content1 = "\\section{ Caso di studio : "+g.getNome()+" } \n\n";
		String content1 = laTexFormatterServiceImpl.formatSubSection("Ricorrenza locale "+g.getNome());
		
		content1 += g.createForLatex() + " ";
		if(g.isCircuit() && g.getM()==1 ){
    		/**
    		 * Rappresento disegno in forma circolare solo nel caso m = 1
    		 */
    		content1 += laTexFormatterServiceImpl.formatParagraph("In forma circolare diventa:");
        	content1 += g.createForLatexCircuit() + " ";
		}
		
		/**
		 * Output 1.1: ottiene gli insiemi indipendenti 
		 */
			
        content1 += laTexFormatterServiceImpl.formatIndependentMatrix(indepList,g);
        
        if(g.hasRicorrenzaLineare()){
			//content1 += laTexFormatterServiceImpl.formatSubSection("Ricorrenza Lineare");
	        
	        String resTM [] = null;
	        if(!g.isCircuit()){
	        	List<Bacchetta> alfabeto = grammarServiceImpl.ottieneLettereAlfabeto(g);	
				GrammarLanguage paroleLinguaggio = grammarServiceImpl.ottieneParoleConsentite(alfabeto, g); 		
				HashMap <String,List<String>> sistemaProseguimento = paroleLinguaggio.getSistemaProseguimento();
				logger.info("sistema proseguimento");
				Utils.printHash(sistemaProseguimento);
				HashMap <String,List<String>> sistemaLineareLinguaggio = null;
				if(g.findMaxPotenzaCamminoOrizzontale()>1){
					sistemaLineareLinguaggio = grammarServiceImpl.ottieneSistemaLineareLinguaggio(sistemaProseguimento, g);
					Utils.printHash(sistemaLineareLinguaggio);
				}
				else {
					sistemaLineareLinguaggio = sistemaProseguimento;
				}
				String[][] xTM = grammarServiceImpl.ottieneTransferMatrix(sistemaLineareLinguaggio);
				resTM = wilfServiceImpl.findRSnFromTM_v3(xTM,g);
				
				content1 += laTexFormatterServiceImpl.formatParagraph("In questo caso otteniamo la \\emph{ricorrenza locale} dal denominatore della funzione generatrice della somma delle righe:");
				content1 += laTexFormatterServiceImpl.formatFgo(resTM[5], "f", "x");
				
	        }
	        
	        content1 += "\n$$\\mbox{schema}\\ \\ \\ \n" + laTexFormatterServiceImpl.formatSchemaRicorrenza(g.getSigma().getSchema(),"T_{n,k}") + "$$\\\\\n\n";
	        content1 += laTexFormatterServiceImpl.formatRicorrenzaLocaleFromSchema(g.getSigma().getSchema());
        }
        else
        {
        	content1 += "\n Ricorrenza non trovata";
        }
        
        content1 += "\\newpage";
        
		return content1;
	}



	private String printAll(String title, String section, String output) {
		try {
			/**
			 * Stampa PDF da Latex
			 */
			File workingDirectory = new File("resources" + File.separator + "tex");

			File template = new File(workingDirectory.getAbsolutePath() + File.separator + Constants.TEX_TEMPLATE_GRAPH);

			File tempDir = new File(workingDirectory.getAbsolutePath() + File.separator + "temp");
			if (!tempDir.isDirectory()) {
			    tempDir.mkdir();
			}

			File invoice1 = new File(tempDir.getAbsolutePath() + File.separator + Constants.TEX_FILE_NAME_RICORRENZE);
      
			
			JLRConverter converter = new JLRConverter(workingDirectory);
			
			converter.replace("title", title);
			/**
			 * Placeholder: section1
			 */
			converter.replace("section1", section);
			
			converter.replace("output1o1", output);
			
			if (!converter.parse(template, invoice1)) {
				logger.info(converter.getErrorMessage());
			    return converter.getErrorMessage();
			}
			

			try {
			      String line;
			      //Provare anche: Process p = Runtime.getRuntime().exec("\"C:\\Program Files\\MiKTeX 2.9\\miktex\\bin\\latex.exe\" --output-format=pdf \"C:\\Users\\Administrator.max-PC\\WolframWorkspaces\\ISA\\resources\\tex\\provaGraphviz.tex\" -shell-escape ");
			      Process p = Runtime.getRuntime().exec("\"" + Constants.LATEX_PATH + "\" --output-format=pdf \"" + Constants.PATH_TO_TEX + Constants.TEX_FILE_NAME_RICORRENZE + "\" -shell-escape ");

			      /**
			       * #2373 lualatex: --output-directory doesn't work
			       * The option --output-directory does nothing with lualatex.
			       * http://sourceforge.net/p/miktex/bugs/2373/
			       */
			      
			      BufferedReader bri = new BufferedReader
			        (new InputStreamReader(p.getInputStream()));
			      BufferedReader bre = new BufferedReader
			        (new InputStreamReader(p.getErrorStream()));
			      while ((line = bri.readLine()) != null) {
			    	  logger.info(line);
			      }
			      bri.close();
			      while ((line = bre.readLine()) != null) {
			    	  logger.info(line);
			    	  if(line.startsWith(Constants.ERROR_FILE_OPENING_PROG)){
				        	ISAConsole.setNotifylineText(Constants.ERROR_FILE_OPENING);
				        	return Constants.ERROR_FILE_OPENING;
				        }
			      }
			      bre.close();
			      p.waitFor();
			      logger.info("Done.");
			      
			      
			      if (Desktop.isDesktopSupported()) {
			    	  logger.info("isDesktopSupported..");
			    	    try {
			    	        File myFile = new File(Constants.PATH_TO_PDF + File.separator + Constants.PDF_FILE_NAME_RICORRENZE);
			    	        Desktop.getDesktop().open(myFile);
			    	        return "Stampa completata";
			    	    } catch (IOException ex) {
			    	    	
			    	    	// no application registered for PDFs
			    	    	logger.error("ERROR: ", ex);
			    	    	return ex.getMessage();
			    	        
			    	    }
			    	}
			      else {
			    	  return "applicazione non trovata nel tentativo di aprire il documento";
			      }
			    }
			    catch (Exception err) {
			      err.printStackTrace();
			      return err.getMessage();
			    }
		
			
		} catch (Exception e) {
			
			e.printStackTrace();
			ISAConsole.setNotifylineText("An error occurred: "+e.getMessage());
			return e.getMessage();
		}
	
	}
	
}
