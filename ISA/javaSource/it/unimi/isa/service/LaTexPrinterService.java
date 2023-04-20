package it.unimi.isa.service;

import it.unimi.isa.beans.PlaceholderBean;
import it.unimi.isa.model.graphs.Graph;


public interface LaTexPrinterService {
	
	public String printPdfGraph();
	public String printGraphOnly(Graph g);
	public String [] creaStampaPerGrafo(PlaceholderBean pb, String [] graphtype) throws Exception;
	public String printDocument3(String p1, String p2) throws Exception;
	public String printDocument3();
	public String stampa4(PlaceholderBean pb, String nomefile);
	public String printPdf2(String nomefile);
	public String stampaGrafo(PlaceholderBean pb);
}
