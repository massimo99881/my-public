package it.unimi.isa.service.impl;


import it.unimi.isa.beans.PlaceholderBean;
import it.unimi.isa.model.graphs.Graph;
import it.unimi.isa.service.LaTexPrinterService;
import it.unimi.isa.utils.Constants;
import it.unimi.isa.utils.ISAConsole;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import de.nixosoft.jlr.JLRConverter;

@Service
public class LaTexPrinterV2ServiceImpl implements LaTexPrinterService {

	private static final Logger logger = Logger.getLogger(LaTexPrinterV2ServiceImpl.class);
	
	/**
	 * Stampa un documento per singola famiglia
	 * @throws Exception 
	 */
	public String printGraphOnly(Graph g) {
		PlaceholderBean pb = new PlaceholderBean();
		pb.setTitle("Independent Sets");
		
		try {
			pb.setSection1(g.getNome());
		
			/**
			 * Output 1.2: disegna il grafo
			 */
			
			pb.setOutput1o1(g.createForLatex());
		
			return stampaGrafo(pb);
			
		} catch (Exception e) {			
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public String stampaGrafo(PlaceholderBean pb) {

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

			File invoice1 = new File(tempDir.getAbsolutePath() + File.separator + Constants.TEX_FILE_NAME_GRAPH);
      
			
			JLRConverter converter = new JLRConverter(workingDirectory);
			
			converter.replace("title", pb.getTitle());
			/**
			 * Placeholder: section1
			 */
			converter.replace("section1", pb.getSection1());
			converter.replace("output1o1", pb.getOutput1o1());
			
			if (!converter.parse(template, invoice1)) {
				logger.info(converter.getErrorMessage());
			    return converter.getErrorMessage();
			}
			
			return printPdfGraph();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			ISAConsole.setNotifylineText("An error occurred: "+e.getMessage());
			return e.getMessage();
		}
	
	}
	
	public String printPdfGraph() {
		try {
		      String line;
		      //Provare anche: Process p = Runtime.getRuntime().exec("\"C:\\Program Files\\MiKTeX 2.9\\miktex\\bin\\latex.exe\" --output-format=pdf \"C:\\Users\\Administrator.max-PC\\WolframWorkspaces\\ISA\\resources\\tex\\provaGraphviz.tex\" -shell-escape ");
		      Process p = Runtime.getRuntime().exec("\"" + Constants.LATEX_PATH + "\" --output-format=pdf \"" + Constants.PATH_TO_TEX + Constants.TEX_FILE_NAME_GRAPH + "\" -shell-escape ");

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
		    	        File myFile = new File(Constants.PATH_TO_PDF + File.separator + Constants.PDF_FILE_NAME_GRAPH);
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
	}

	@Override
	public String[] creaStampaPerGrafo(PlaceholderBean pb, String[] graphtype)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String printDocument3(String p1, String p2) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String printDocument3() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String printPdf2(String nomefile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String stampa4(PlaceholderBean pb, String nomefile) {
		// TODO Auto-generated method stub
		return null;
	}

}
