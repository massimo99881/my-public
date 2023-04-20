package it.unimi.isa.service.impl;


import it.unimi.isa.beans.PlaceholderBean;
import it.unimi.isa.model.Bacchetta;
import it.unimi.isa.model.GrammarLanguage;
import it.unimi.isa.model.Sigma;
import it.unimi.isa.model.graphs.Graph;
import it.unimi.isa.service.LaTexPrinterService;
import it.unimi.isa.singleton.Singleton;
import it.unimi.isa.utils.Constants;
import it.unimi.isa.utils.ISAConsole;
import it.unimi.isa.utils.Utils;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.nixosoft.jlr.JLRConverter;

@Service
public class LaTexPrinterV1ServiceImpl implements LaTexPrinterService {
	
	@Autowired
	GrammarServiceImpl grammarServiceImpl;
	
	@Autowired
	WilfServiceImpl wilfServiceImpl;
	
	@Autowired
	IndepSetServiceImpl indepSetServiceImpl;
	
	@Autowired
	AdeServiceImpl adeServiceImpl;
	
	@Autowired
	LaTexFormatterServiceImpl laTexFormatterServiceImpl;
	
	@Autowired
	Singleton singleton;

	private static final Logger logger = Logger.getLogger(LaTexPrinterV1ServiceImpl.class);
	
	/**
	 * Metodo utilizzato per stampare tutto cio che sappiamo descrivere di un Grid-Graph
	 * @param pb
	 * @param graphtype
	 * @return
	 * @throws Exception
	 */
	public String [] creaStampaPerGrafo(PlaceholderBean pb, String [] graphtype) throws Exception {		
		
		Graph g = Utils.ottieneGrafo(graphtype, 1, 3);
		List<HashMap<Integer,List<Integer>>> indepList = indepSetServiceImpl.forzaBruta(g);
        
		String content1 = "\\section{ Caso di studio : "+g.getNome()+" } \n\n";
		/**
		 * Output 1.2: disegna il grafo
		 */
		
		content1 += laTexFormatterServiceImpl.formatParagraph(
			"\\begin{definition}\n "+
		"Un grafo (non orientato e finito) è una coppia ordinata $(V,E)$ dove $V$ è un insieme finito ed $E$ è un multiinsieme di coppie non ordinate di elementi di $V$. L'insieme V contiene i vertici del grafo ed $E$ i suoi lati. Per un generico grafo $G$, l'insieme dei suoi vertici è indicato con $V(G)$ e quello dei suoi lati con $E(G)$.\n "+
		"\\end{definition} "  
		);
		
		content1 += laTexFormatterServiceImpl.formatParagraph("La struttura dati con la quale si è scelto di memorizzare il grafo è la matrice di adicenza.");
		
		content1 += laTexFormatterServiceImpl.formatParagraph(
		"\\begin{definition} \n"+
		"La matrice di adiacenza di un grafo $G$ i cui vertici siano $v_1,v_2, \\dots ,v_n$ è una matrice $A(G)=[a(i,j)]$  simmetrica di ordine $n\\times n$ in cui si pone: \n" +
		"\\end{definition} \n"+

		"$$a(i,j)=\\left\\{\\begin{tabular}{ll} \n"+
		"1& se $(v_i,v_j)\\in E(G)$ \\\\ \n"+ 
		"0& altrimenti \n"+
		"\\end{tabular}\\right. \n $$ \n\n" 
		);
		
		
		content1 += laTexFormatterServiceImpl.formatParagraph("Di seguito viene mostrata invece la lista di adiacenza che permette una più facile lettura delle adiacenze:");
		content1 += laTexFormatterServiceImpl.formatListaAdiacenza(g);
        content1 += g.createForLatex() + " ";
        
        //controllo stampa in forma circolare per cammini
        if(g.isCircuit() ){
        	
        	if(g.getM()==1){
        		/**
        		 * Rappresento disegno in forma circolare solo nel caso m = 1
        		 */
        		content1 += laTexFormatterServiceImpl.formatParagraph("In forma circolare diventa:");
            	content1 += g.createForLatexCircuit() + " ";
        	}
        	
        	/**
        	 * Breve appunto per indicare la differenza tra i circuiti e i "semi-circuiti"
        	 */
        	String f = g.getF();
        	
        	if("h".equalsIgnoreCase(f)){
        		content1 += laTexFormatterServiceImpl.formatParagraph(
        				"Con le famiglie di grafi \\emph{H} vogliamo indicare dei circuiti che hanno le potenze orizzontali \\emph{limitate} al valore di $n$, quindi l'unico arco che fa da circuito è quello tra il primo nodo e l'ultimo."
        			);
        	}
        	else if("c".equalsIgnoreCase(f)){
        		content1 += laTexFormatterServiceImpl.formatParagraph(
        				"Con le famiglie di grafi $C$ vogliamo indicare dei circuiti \\emph{veri e propri} in cui, oltre all'arco che collega il "+
        				"primo nodo con l'ultimo, abbiamo anche archi delle potenze dei cammini orizzontali che possono collegarsi ai nodi precedenti rispetto ai nodi dai quali partono. "
        		);
        	}
        }
		
		/*********************************************
		 * Section 1 : Algoritmo forza bruta
		 *********************************************/
		
		content1 += laTexFormatterServiceImpl.formatSubSection("Calcolo insiemi indipendenti con metodo forza bruta");
		
		content1 += laTexFormatterServiceImpl.formatParagraph(
		"\\begin{definition}"+
		"Un insieme indipendente di un grafo è un insieme di vertici non adiacenti del grafo."+
		"\\end{definition}"
		);
		
		content1 += laTexFormatterServiceImpl.formatParagraph(			
		"Definiamo $T(n,k)$ il numero di $k$-sottoinsiemi indipendenti di "+g.getNome()+". \\\\"+
		"Ecco alcuni valori"
		);
        
        /**
		 * Output 1.1: ottiene gli insiemi indipendenti 
		 */
			
        content1 += laTexFormatterServiceImpl.formatIndependentMatrix(indepList,g);
        
        /**
         * mostra le successioni: Kn, RSn, ADn 
         */
        HashMap <String,String> espansioniMap = new HashMap<String, String>();
        content1 += laTexFormatterServiceImpl.formatParagraph("Seguono le successioni delle antidiagonali, della somma delle righe e dei valori massimali di $k$ per cui esistono insiemi indipendenti:");
        //ottiene massimo valore di k per riga
        List<Integer> lastComputedKList = indepSetServiceImpl.getLastComputedKList();
        String kList = Utils.getStringFromIntegerArray(lastComputedKList);
        logger.info("K: "+kList);
        espansioniMap.put("K", kList);
       
        //ottiene somma delle righe calcolata sul risultato del metodo forza bruta
        List<Integer> rowSumList = indepSetServiceImpl.getRowSumList();
        String sumList = Utils.getStringFromIntegerArray(rowSumList);
        logger.info("RS: "+sumList);
        espansioniMap.put("RS", sumList);
        
        //ottiene antidiagonali dalla tabella degli indipendenti ottenuta dal metodo forza bruta
        AdeServiceImpl ade = new AdeServiceImpl();
		Integer[][] indep = Utils.convertIndepHashMapListToMatrix(indepSetServiceImpl.getIndependentMatrix());
		List<Integer> antidiagonals = ade.getAntidiagonals(indep);
		String antidiagList = Utils.getStringFromIntegerArray(antidiagonals);
        logger.info("AD: "+antidiagList);
        espansioniMap.put("AD", antidiagList);
        content1 += laTexFormatterServiceImpl.formatMultiEspansioneInSerie(espansioniMap);
		
        /**
    	 * Sloane bijections: ADn, RSn, Kn
    	 */
        if(Constants.SLOANE_BIJECTIONS_ENABLED){
	    	logger.info("ricerca bijezioni sloane");	
	    	content1 += laTexFormatterServiceImpl.formatParagraph("Ricerca delle bijezioni sulla successione delle antidiagonali:");
			content1 += laTexFormatterServiceImpl.creaSpazio();
			content1 += laTexFormatterServiceImpl.getSloaneBijections(espansioniMap.get("AD"));
			
	    	content1 += laTexFormatterServiceImpl.formatParagraph("Ricerca delle bijezioni sulla successione della somma delle righe:");
			content1 += laTexFormatterServiceImpl.creaSpazio();
			logger.info("ricerca bijezioni sloane");		
			content1 += laTexFormatterServiceImpl.getSloaneBijections(espansioniMap.get("RS"));
			
	    	content1 += laTexFormatterServiceImpl.formatParagraph("Ricerca delle bijezioni sulla successione dei valori massimali di $k$ per cui esistono insiemi indipendenti:");
			content1 += laTexFormatterServiceImpl.creaSpazio();
			logger.info("ricerca bijezioni sloane");		
			content1 += laTexFormatterServiceImpl.getSloaneBijections(espansioniMap.get("K"));
        }
        else{
        	content1 += laTexFormatterServiceImpl.formatParagraph("\\emph{Ricerca delle bijezioni disabilitata per questa stampa.}");
        }
        
		/*********************************************
		 * Section 2: Wilf & Sloane
		 *********************************************/
        String content2 = "";
        String resTM[] = null;
        List<Bacchetta> alfabeto = grammarServiceImpl.ottieneLettereAlfabeto(g);
        //
        //provo a calcolare il sistema proseguimento anche per i circuiti..
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
			if(!g.isCircuit())
				sistemaLineareLinguaggio = sistemaProseguimento;
		}
        
		if(!g.isCircuit()){
			
			String[][] xTM = grammarServiceImpl.ottieneTransferMatrix(sistemaLineareLinguaggio);
			resTM = wilfServiceImpl.findRSnFromTM_v3(xTM,g);		
			String fgoGrammatica = grammarServiceImpl.ottieneFgoGrammatica(sistemaLineareLinguaggio);
			String coeffList = grammarServiceImpl.ottieniEspansioneInSerieFunzione(fgoGrammatica,"x");    		
	    		
			/**
			 * METODO WILF
			 */
			content1 += laTexFormatterServiceImpl.creaSpazio();
			content1 += laTexFormatterServiceImpl.formatSubSection("Il problema");
			content1 += laTexFormatterServiceImpl.formatParagraph("Nel loro lavoro [Wilf], Wilf e Calkin basano la ricerca del numero di insiemi indipendenti di una "
			+ "supergriglia, $SG(m,n)$, sul concetto di \\emph{matrice di trasferimento}, TM nel seguito. "		
			);
			
			/**
			 * Output 2.1: lettere alfabeto
			 */
			content1 += laTexFormatterServiceImpl.formatParagraph("Il procedimento per costruire l'automa associato a questa supergriglia è il seguente");
			content1 += laTexFormatterServiceImpl.formatLettereAlfabeto(alfabeto);
			/**
			 * Output 2.2: sistema "proseguimento"
			 */
			if(g.findMaxPotenzaCamminoOrizzontale()>1){
				content1 += laTexFormatterServiceImpl.formatParagraph("Il sistema ottenuto dai possibili proseguimenti (di un passo) è il seguente:");
			    content1 += laTexFormatterServiceImpl.formatProseguimentoSistema(sistemaProseguimento,grammarServiceImpl.getLetteraFGO());
				content1 += laTexFormatterServiceImpl.formatParagraph("Riscriviamo lo schema con stringhe tutte di lunghezza $h$ (la potenza del cammino orizzontale):");
			    content1 += laTexFormatterServiceImpl.formatSistemaLineare(sistemaLineareLinguaggio,grammarServiceImpl.getLetteraFGO());
			}
			else {
				content1 += laTexFormatterServiceImpl.formatParagraph("Il sistema ottenuto dai possibili proseguimenti (di un passo) è il seguente:");
				content1 += laTexFormatterServiceImpl.formatSistemaLineare(sistemaLineareLinguaggio,grammarServiceImpl.getLetteraFGO());
			}
			/**
			 * Output 2.3: Transfer Matrix
			 */
			content1 += laTexFormatterServiceImpl.formatParagraph("Un risultato di algebra lineare afferma che la somma degli elementi della matrice $(I-xTM)^{-1}$ è la funzione generatrice degli insiemi indipendenti. " +
																  "La matrice TM di questo esempio è ");
		    content1 += (laTexFormatterServiceImpl.formatTransferMatrix(xTM,sistemaLineareLinguaggio));
			
			/**
			 * Output 2.5: Ottiene fgo RSn
			 */
		    content1 += laTexFormatterServiceImpl.formatParagraph("La funzione generatrice è");
		    content1 += laTexFormatterServiceImpl.formatFgo2(resTM[5],resTM[9], "f", "x");
			
			
			
			content1 += laTexFormatterServiceImpl.formatParagraph("Dalla espansione in serie della fgo otteniamo i valori di $RS_n$ (\\footnote{Ricordiamo che il metodo di Wilf non considera il grafo vuoto})");
			
			
			content1 += laTexFormatterServiceImpl.formatEspansioneInSerie(sumList, "RS");
			
			content1 += laTexFormatterServiceImpl.formatParagraph("Il coefficiente di $x^n$ nell'espansione in serie di questa funzione è il numero totale di insiemi indipendenti, dove $n$ è il numero di colonne del grafo considerato.");
			
			//questo metodo è da rivedere..
			content1 += controlloCorrispondenzaSuccessioneSommeTramiteWilf(sumList,resTM);
			
			/*********************************************
			 * Section 3 : GRAMMATICA
			 *********************************************/
			content1 += laTexFormatterServiceImpl.formatSubSection("Il nostro metodo");
			
			content1 += laTexFormatterServiceImpl.formatParagraph(
					"Adesso costruiamo il sistema lineare in cui le variabili sono funzioni generatrici nell'indeterminata $x$. Avremo tante variabili ed equazioni quante sono le stringhe legali.\\\\ \n"+
					"Alla generica linea dello schema \n"+
					"$$ab \\longrightarrow cd + \\dots + ef $$ \n"+
					"associamo la equazione \n"+
					"$$AB(x) = xCD(x) +\\dots + xEF(x) + 1$$ \n\n"	
			);
			
			/**
			 * Output 3.2: Sistema lineare nella forma della grammatica
			 */
			content1 += laTexFormatterServiceImpl.formatParagraph("In questo caso abbiamo il seguente schema");
			content1 += laTexFormatterServiceImpl.formatSistemaLineareGrammatica(sistemaLineareLinguaggio,grammarServiceImpl.getLetteraFGO());
			/**
			 * Output 3.3: Fgo grammatica
			 */
			
			content1 += laTexFormatterServiceImpl.formatParagraph(
					"L'automa che stiamo generando avrà uno stato per ogni stringa legale del linguaggio. Tutti gli stati sono finali. \n"+
					"Ognuna delle nostre variabili è la funzione generatrice del linguaggio riconosciuto dall'automa a partire dallo stato corrispondente alla variabile.\\\\ \n"+
					"In questo esempio lo stato iniziale è $"+grammarServiceImpl.getLetteraFGO().toUpperCase()+"$. Quindi risolvendo in $"+grammarServiceImpl.getLetteraFGO().toUpperCase()+"(x)$ si ottiene il linguaggio accettato dall'automa. \n"
		    		);
			
			content1 += laTexFormatterServiceImpl.formatFgo2(fgoGrammatica, coeffList, grammarServiceImpl.getLetteraFGO(), "x");
				
			/**
			 * Output 3.4: Espansione in serie della fgo della grammatica
			 */    		
			content1 += laTexFormatterServiceImpl.formatEspansioneInSerie(coeffList,"RS");
			
			content1 += laTexFormatterServiceImpl.formatParagraph(
			"abbiamo che il coefficiente di $x^t$ è il numero di insiemi indipendenti del grafo costituito dalle prime $t$ barrette verticali.\\\\"
			);
			
    		/**
    		 * Section 4 : Automa
    		 */
//    		content1 += laTexFormatterServiceImpl.formatSubSection("Automa");
			int h = g.findMaxPotenzaCamminoOrizzontale();
			
    		content1 += laTexFormatterServiceImpl.formatParagraph("Il software costruisce il sistema e genera l'automa ");
    		content1 += laTexFormatterServiceImpl.formatSistemaLineareAutoma(sistemaLineareLinguaggio,grammarServiceImpl.getLetteraFGO(),h);

    		content2 += laTexFormatterServiceImpl.getAutoma2(sistemaLineareLinguaggio,grammarServiceImpl.getLetteraFGO().toUpperCase(),h,g.getM());//automa.getFakeAutoma(sistema);
        }
        else{
        	//siamo nel caso di semi-circuito H_n^{h} oppure di circuiti C_n^{h}
        	
        	content1 += laTexFormatterServiceImpl.formatParagraph("\\textbf{Wilf}: Non possiamo usare il metodo di Wilf per trovare la Fgo delle somme delle righe in quanto il grafo \\`e un circuito.");
        	int m = g.getM();
        	
        	
        	//circuiti : per il momento siamo in grado di stampare solo gli automi dei grafi per m=1
        	
        	if(m==1){
        		
        		content1 += laTexFormatterServiceImpl.formatSubSection("Automa");
            	int h = g.findMaxPotenzaCamminoOrizzontale();            	
            		
    			//circuiti che hanno il collegamento primo nodo con ultimo nodo ma anche i collegamenti tra le potenze dei cammini h
    			//Esempi:
    			
    			// print all p_1_1 x c_6_1
    			// print all p_1_1 x c_6_2
    			// print all p_1_1 x c_6_<3
    			// print all p_1_1 x c_6_3
    			                	
            	//content1 += laTexFormatterServiceImpl.formatParagraph("Questo \\`e l'automa che riconosce (tutte e sole) le stringhe che corrispondono agli insiemi indipendenti di $C_n^{("+h+")}$: ");
        		content1 += g.getAutoma(h,null);//automa.getFakeAutoma(sistema);
        		
        		//TODO da sistemare questa parte di costruzione del sistema lineare 
        		//ottiene il sistema lineare nella forma della grammatica
        		content1 += laTexFormatterServiceImpl.formatParagraph("Il sistema lineare diventa:");
                //content1 += laTexFormatterServiceImpl.formatProseguimentoSistemaCircuito(sistemaProseguimento);

        		content1 += g.getSistemaLineareCircuiti();
        	}
        	
        	int h = g.findMaxPotenzaCamminoOrizzontale();
        	if(h==1 && g.getM()==1){
	        	/**
	        	 * NUOVA PARTE DI CALCOLO SISTEMA LINEARE E AUTOMA PER CIRCUITI - 18012016
	        	 */
	        	content1 += laTexFormatterServiceImpl.formatParagraph("Calcolo automatico sistema lineare e automa per circuiti:");
	        	content1 += laTexFormatterServiceImpl.formatLettereAlfabeto(alfabeto);
	        	
        		grammarServiceImpl.creaSistemaProseguimentoCircuiti(sistemaProseguimento, alfabeto, g);
            	
            	Utils.printHash(sistemaProseguimento);
            	sistemaLineareLinguaggio = grammarServiceImpl.ottieneSistemaLineareLinguaggioCircuiti(sistemaProseguimento, g);
            	Utils.printHash(sistemaLineareLinguaggio);
            	content1 += laTexFormatterServiceImpl.formatProseguimentoSistema(sistemaProseguimento,grammarServiceImpl.getLetteraFGO());        	
    			content1 += laTexFormatterServiceImpl.formatSistemaLineareGrammaticaCircuiti(sistemaLineareLinguaggio,grammarServiceImpl.getLetteraFGO());
        		content1 += laTexFormatterServiceImpl.formatSistemaLineareAutomaCircuiti(sistemaLineareLinguaggio,grammarServiceImpl.getLetteraFGO(),h);
        		content2 += laTexFormatterServiceImpl.getAutomaCircuiti2(sistemaLineareLinguaggio,"S",h,g.getM());//automa.getFakeAutoma(sistema);

        		//ricava fgo dal sistema costruito in modo automatico
        		String mathscript = grammarServiceImpl.creaSistemaLineareGrammaticaCircuiti(sistemaLineareLinguaggio);
        		String result = grammarServiceImpl.risolviSistemaLineareGrammatica(mathscript);
        		String fgoGrammatica = grammarServiceImpl.ottieniFunzioneGenGrammaticaCircuiti(result);
        		
        		String coeffList = grammarServiceImpl.ottieniEspansioneInSerieFunzione(fgoGrammatica,"x");

        		content1 += laTexFormatterServiceImpl.formatFgo2(fgoGrammatica, coeffList, grammarServiceImpl.getLetteraFGO(), "x");
        		    		
        		content1 += laTexFormatterServiceImpl.formatEspansioneInSerie(coeffList,"RS");
        	}
        	
        	
    		
        }
		
		
		/**
		 * Output 4.3: Traduzione della grammatica per l'automa
		 */
		String content3 = "";

		/*********************************************
		 * Section 5 : Ricorrenza Lineare ?
		 *********************************************/
		
		boolean hasRicorrenzaLineare = g.hasRicorrenzaLineare();//ricorrenzaLocale.findLocal(resTM[7], im);
		
		if(hasRicorrenzaLineare){
			content3 += laTexFormatterServiceImpl.formatSubSection("Ricorrenza Lineare");
			
			/**
			 * Output 5.1: Dal denominatore della fgo di RSn ottenuto tramite Wilf provo ad ottenere la ricorrenza
			 */
			
			
			/**
			 * Se e stata trovata una ricorrenza valida mostra lo schema
			 */
			
			content3 += laTexFormatterServiceImpl.formatParagraph("In questo caso otteniamo la \\emph{ricorrenza locale} dal denominatore della funzione generatrice della somma delle righe:");
			
//			String output5o2 = 
//				"\n$$\\mbox{schema}\\ \\ \\ \n\\begin{tabular}{| c | c |} \\hline \n"+
//				"$1$&$0$\\\\ \\hline \n"+
//				"$1$&$1$\\\\ \\hline \n"+
//				"$0$&$T_{n,k}$\\\\ "+
//				"\\hline \n"+
//				"\\end{tabular}$$ \\\\\n\n"; 
			
			content3 += laTexFormatterServiceImpl.formatFgo(resTM[5], "f", "x");
			
			content3 += laTexFormatterServiceImpl.formatEspansioneIntegerInSerie(rowSumList,"RS");
			
			content3 += laTexFormatterServiceImpl.formatRSnFormula(resTM[5],singleton.getKernelLink());
			
			String output5o2 = "\n$$\\mbox{schema}\\ \\ \\ \n" + laTexFormatterServiceImpl.formatSchemaRicorrenza(g.getSigma().getSchema(),"T_{n,k}") + "$$ \n\n";
			
			content3 += output5o2;
			
//			String output5o3 = 			
//				"\n\nProvo a ... dimostrare che $T_{n,k}=T_{n-1,k}+T_{n-1,k-1}+T_{n-2,k-1}$. Stiamo parlando di $P_2^{(1)}\\times P_n^{(1)}$, ma scrivo semplicemente $P_2\\times P_{n-1}$. Sia $\\mathcal{T}_{n,k}$ l'insieme dei $k$-sottoinsiemi indipendenti di $P_2\\times P_{n}$. Ripartisco tale insieme in due blocchi: $\\mathcal{A}$ e $\\mathcal{B}$. In $\\mathcal{A}$ metto tutti e soli i $k$-sottoinsiemi indipendenti di $P_2\\times P_{n}$ che non contengono vertici dell'ultima colonna e in $\\mathcal{B}$ gli altri (cio\\`e i $k$-sottoinsiemi indipendenti di $P_2\\times P_{n}$ che contengono un solo vertice dell'ultima colonna).\\\\ "+ 
//				"$\\bullet$ Dovrebbe essere chiaro che $|\\mathcal{A}|=T_{n-1,k}.$ Infatti tutti i $k$-sottoinsiemi indipendenti di $P_2\\times P_{n-1}$ sono $k$-sottoinsiemi indipendenti di $P_2\\times P_{n}$ che non contengono vertici dell'ultima colonna e viceversa.\\\\ \n\n";
//			
//			content3 += output5o3;
			
			content3 += laTexFormatterServiceImpl.formatRicorrenzaLocaleFromSchema(g.getSigma().getSchema());
		
			/*********************************************
			 * Section 6 : Antidiagonali 
			 *********************************************/
			
			String section6 = laTexFormatterServiceImpl.formatSubSection("Antidiagonali");
				
			/**
			 * Output 6.2: Disegna lo schema e le funzioni generatrici (condizioni al contorno)
			 */
			section6 += "$$sigma = "+laTexFormatterServiceImpl.formatSchemaRicorrenza(g.getSigma().getSchema(),"*")+"$$ \n\n";
			section6 += "$$ "+laTexFormatterServiceImpl.formatFgoSchemaRicorrenza(g.getSigma(),singleton.getKernelLink(),"H")+" $$ \n\n";
			section6 += "$$ "+laTexFormatterServiceImpl.formatFgoSchemaRicorrenza(g.getSigma(),singleton.getKernelLink(),"V")+" $$ \n\n ";
			Sigma sigma = g.getSigma();
			sigma.setFuncGenH(sigma.getFuncGenH(),singleton.getKernelLink());
			sigma.setFuncGenV(sigma.getFuncGenV(),singleton.getKernelLink());
			adeServiceImpl.setSigma(sigma);
			String[][] m = adeServiceImpl.complete(); 
			section6 += "$$" + laTexFormatterServiceImpl.formatMatriceForAde(m,sigma.getH(),sigma.getV()) + "$$ \n\n";
			
			//TODO
			//adeServiceImpl.obtainRecurrenceAndGF(m);
			
			
//			/**
//			 * Output 6.3: Disegna formula di ricorrenza Antidiagonali e successione (ADn)
//			 */
//			section6 += 
//				"In this case we have $d_0=1, d_1=2$, and, for $n\\geq2$, \\\\ "+
//
//				"$$d_n=1+1+d_{n-2}+2d_{n-1}-1-1=d_{n-2}+2d_{n-1}.$$ \\\\ "+
//
//				"$$\\begin{tabular}{|c|c c c c c c}\\hline "+
//				"$n$     &0&1&2&3   &4&5\\\\ \\hline"+
//				"$d_n$&1&2 &5&12&29&\\\\ \\hline"+
//				"\\end{tabular}$$\\\\ \\\\";
//			
//			/**
//			 * Output 6.4: Stampa la fgo di ADn
//			 */
//			section6 +=
//				"The sequence $\\{d_n\\}_{n\\geq0}$ is generated by "+
//
//				"\\begin{equation}"+
//				"D(x)=\\frac{1}{1-2x-x^2}"+
//				"\\end{equation}\\\\ ";
//			
//			/**
//			 * Output 6.5: ricalcola gli indipendenti usando la ricorrenza
//			 */
//			g.createSchema();
//			section6 += laTexFormatterServiceImpl.formatIndependentMatrix(indepList,g);
			
//			/**
//			 * Output 6.6: IsoTrangolarizza
//			 */
////			IsoTriangular t = new IsoTriangular(ml,sigma,ade.getD());
////			t.isodiagonalizza();
////			
////			t.obtainFgoVx(sigmaDelannoy,matrixExample);
////			t.obtainFgoRSn(sigmaDelannoy,matrixExample);
			
			content3 += section6;
		}
		
		content3 += laTexFormatterServiceImpl.creaSpazio();
		content3 += laTexFormatterServiceImpl.creaSpazio();
		content3 += laTexFormatterServiceImpl.creaSpazio();
		content1 += laTexFormatterServiceImpl.creaSpazio();
		content1 += laTexFormatterServiceImpl.creaSpazio();
		
		return new String []{content1, content2, content3};
		
	}
	
	/**
	 * Controllo corrispondenza su sequenza ottenuta dalla fgo ottenuta tramite metodo Wilf
	 * Nel caso in cui ci sia una corrispondeza parziale la si considera come buona in quanto
	 * la correzione fgo_new = 1 + x*fgo non funziona in tutti i casi. 
	 * Quando la differenza è solo di shift, le successioni sono considerate uguali.
	 * @param sumList
	 * @param resTM
	 * @return
	 */
	private String controlloCorrispondenzaSuccessioneSommeTramiteWilf(String sumList, String[] resTM) {
		String output = "";
		
		logger.info("controllo congruenza successione somme ottenuta con la somma degli indipendenti");
		
//		if(Constants.CHECK_EQUAL_SUMS){
//			if(Utils.isPartOfExpansion(sumList,resTM[9]))
//			{
//				output += laTexFormatterServiceImpl.formatParagraph("\\emph{La successione ottenuta \\`e coretta }"); //a partire da indice "+res[1]+".
//			}
//			else{
//				output += laTexFormatterServiceImpl.formatParagraph("\\textcolor{red}{\\textbf{ATTENZIONE}: la somma non coincide con la somma degli indipendenti.}");
//			}
//			
//		}
		
		return output;
	}

	public String printDocument3(String p1, String p2) throws Exception {
		logger.info("Start .. "); 
		
		PlaceholderBean pb = new PlaceholderBean();
		pb.setTitle("ISA Software v.1.3");
		
		try {
			String result[] = null;
			
			result = creaStampaPerGrafo(pb, new String[]{"indep",p1,"x",p2});
			
			String part[] = p2.split("_");
			p2=""+part[0]+"_n_"+part[2];
			String nomeFile = p1+"_x_"+p2;
			
			pb.setContent1(result[0] + result[1] + result[2]);
			pb.setContent2("");
			pb.setContent3("");
			pb.setContent4("");
			pb.setContent5("");
			pb.setContent6("");
			pb.setContent7("");
			pb.setContent8("");
			pb.setContent9("");
			pb.setContent10("");
			pb.setContent11("");
			pb.setContent12("");
			pb.setContent13("");
			pb.setContent14("");
			pb.setContent15("");
			pb.setContent16("");
			pb.setContent17("");
			pb.setContent18("");
			pb.setContent19("");
			pb.setContent20("");
			pb.setContent21("");
			pb.setContent22("");
			pb.setContent23("");
			pb.setContent24("");
			pb.setContent25("");
			pb.setContent26("");
			pb.setContent27("");
			pb.setContent28("");
			pb.setContent29("");
			pb.setContent30("");
			pb.setContent31("");
			pb.setContent32("");
			pb.setContent33("");
			pb.setContent34("");
			pb.setContent35("");
			pb.setContent36("");
			pb.setContent37("");
			pb.setContent38("");
			pb.setContent39("");
			pb.setContent40("");
			pb.setContent41("");
			return stampa4(pb,nomeFile);
				
		} catch (UnknownHostException e) {			
			e.printStackTrace();
			logger.error("Connessione non stabilita: non è possibile collegarsi al server oeis");
			return e.getMessage();
		
		} 
	}

	public String printDocument3() {
		
		logger.info("Start .. "); 
		
		PlaceholderBean pb = new PlaceholderBean();
		pb.setTitle("Independent Sets v.1.2 ");
		
		int n1 = 11;
		int n2 = 7;
		int n3 = 4;
		
		try {
			String result[] = null;
			
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_1_1", "x", "p_"+n1+"_1"});
			pb.setContent1(result[0] + result[1] + result[2]);	
			logger.info("completato: p_1_1 x p_"+n1+"_1");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_1_1", "x", "p_"+n1+"_2"});
			pb.setContent2(result[0] + result[1] + result[2]);	
			logger.info("completato: p_1_1 x p_"+n1+"_2");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_1_1", "x", "p_"+n1+"_e3"});
			pb.setContent3(result[0] + result[1] + result[2]);
			logger.info("completato: p_1_1 x p_"+n1+"_e3");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_1_1", "x", "p_"+n1+"_3"});
			pb.setContent4(result[0] + result[1] + result[2]);
			logger.info("completato: p_1_1 x p_"+n1+"_3");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_1_1", "x", "h_"+n1+"_1"});
			pb.setContent5(result[0] + result[1] + result[2]);
			logger.info("completato: p_1_1 x h_"+n1+"_1");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_1_1", "x", "h_"+n1+"_2"});
			pb.setContent6(result[0] + result[1] + result[2]);
			logger.info("completato: p_1_1 x h_"+n1+"_2");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_1_1", "x", "h_"+n1+"_e3"});
			pb.setContent7(result[0] + result[1] + result[2]);	
			logger.info("completato: p_1_1 x h_"+n1+"_e3");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_1_1", "x", "h_"+n1+"_3"});
			pb.setContent8(result[0] + result[1] + result[2]);
			logger.info("completato: p_1_1 x h_"+n1+"_3");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_1_1", "x", "c_"+n1+"_1"});
			pb.setContent9(result[0] + result[1] + result[2]);
			logger.info("completato: p_1_1 x c_"+n1+"_1");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_1_1", "x", "c_"+n1+"_2"});
			pb.setContent10(result[0] + result[1] + result[2]);
			logger.info("completato: p_1_1 x c_"+n1+"_2");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_1_1", "x", "c_"+n1+"_e3"});
			pb.setContent11(result[0] + result[1] + result[2]);
			logger.info("completato: p_1_1 x c_"+n1+"_e3");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_1_1", "x", "c_"+n1+"_3"});
			pb.setContent12(result[0] + result[1] + result[2]);
			logger.info("completato: p_1_1 x c_"+n1+"_3");
			//grid
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "p_"+n2+"_1"});
			pb.setContent13(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x p_"+n2+"_1");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "p_"+n2+"_2"});
			pb.setContent14(result[0] + result[1] + result[2]);
//			logger.info("completato: p_2_1 x p_5_2");
			//result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "p_4_e3"}); //troppo tempo di ricerca per fgo
			//pb.setContent15(result[0] + result[1] + result[2]);
			pb.setContent15("");
			//logger.info("completato: p_2_1 x p_4_e3");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "p_"+n2+"_3"});
			pb.setContent16(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x p_"+n2+"_3");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "z_"+n2+"_1"});
			pb.setContent17(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x z_"+n2+"_1");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "z_"+n2+"_2"});
			pb.setContent18(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x z_"+n2+"_2");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "z_"+n2+"_e3"});
			pb.setContent19(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x z_"+n2+"_e3");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "z_"+n2+"_3"});
			pb.setContent20(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x z_"+n2+"_3");
			result = creaStampaPerGrafo(pb, new String[]{"indep", "p_2_1", "x", "f_"+n2+"_1"});
			pb.setContent21(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x f_"+n2+"_1");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "f_"+n2+"_2"});
			pb.setContent22(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x f_"+n2+"_2");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "f_"+n2+"_e3"});
			pb.setContent23(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x f_"+n2+"_e3");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "f_"+n2+"_3"});
			pb.setContent24(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x f_"+n2+"_3");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "h_"+n2+"_1"});
			pb.setContent25(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x h_"+n2+"_1");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "h_"+n2+"_2"});
			pb.setContent26(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x h_"+n2+"_2");
			//result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "h_5_3"}); //pesante elaborazione per trovare fgo
			//pb.setContent27(result[0] + result[1] + result[2]);
			pb.setContent27("");
			//logger.info("completato: p_2_1 x h_5_3");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "h_"+n2+"_3"});
			pb.setContent28(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x h_"+n2+"_3");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "c_"+n2+"_1"});
			pb.setContent29(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x c_"+n2+"_1");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "c_"+n2+"_2"});
			pb.setContent30(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x c_"+n2+"_2");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "c_"+n2+"_e3"});
			pb.setContent31(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x c_"+n2+"_e3");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "c_"+n2+"_3"});
			pb.setContent32(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x c_"+n2+"_3");
			
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "hf_"+n2+"_1"});
			pb.setContent33(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x hf_"+n2+"_1");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "hf_"+n2+"_2"});
			pb.setContent34(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x hf_"+n2+"_2");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "hz_"+n2+"_1"});
			pb.setContent35(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x hz_"+n2+"_1");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "hz_"+n2+"_2"});
			pb.setContent36(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x hz_"+n2+"_2");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "cf_"+n2+"_1"});
			pb.setContent37(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x cf_"+n2+"_1");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "cf_"+n2+"_2"});
			pb.setContent38(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x cf_"+n2+"_2");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "cz_"+n2+"_1"});
			pb.setContent39(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x cz_"+n2+"_1");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "cz_"+n2+"_2"});
			pb.setContent40(result[0] + result[1] + result[2]);
			logger.info("completato: p_2_1 x cz_"+n2+"_2");
			result = creaStampaPerGrafo(pb, new String[]{"indep","p_2_1", "x", "m_"+n2+"_1"});
			logger.info("completato: p_2_1 x m_"+n2+"_1");
			
			String output = "";
			output += result[0] + result[1] + result[2];
			
//			result = creaStampaPerGrafo(pb, new String[]{"indep","p_3_1", "x", "p_"+n3+"_1"});
//			output += result[0] + result[1] + result[2];
//			logger.info("completato: p_3_1 x p_"+n3+"_1");
			
			pb.setContent41(output);
			
			
			//tutti i grid-graphs con p_3_1 richiedono tempi molto lunghi.. meglio richiedere la 
			//stampa puntuale con il comando print all p_3_1 x p_4_2 
			logger.info("elaborazione stampa di test terminata, inizio scrittura tex..");
			
			//TODO una idea: salvare in cache le fgo delle RS-n perchè continuano a richiedere davvero troppo tempo di calcolo!! soprattutto per m>=3
			
			return stampa4(pb,Constants.FILENAME_ALL2);
			
		} catch (Exception e) {			
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public String stampa4(PlaceholderBean pb, String nomefile) {
		try {
			/**
			 * Stampa PDF da Latex
			 */
			File workingDirectory = new File("resources" + File.separator + "tex");

			File template = new File(workingDirectory.getAbsolutePath() + File.separator + Constants.TEX_TEMPLATE_ALL2);

			File tempDir = new File(workingDirectory.getAbsolutePath() + File.separator + "temp");
			if (!tempDir.isDirectory()) {
			    tempDir.mkdir();
			}

			File invoice1 = new File(tempDir.getAbsolutePath() + File.separator + nomefile+ ".tex");
      
			
			JLRConverter converter = new JLRConverter(workingDirectory);
			
			converter.replace("title", pb.getTitle());
			converter.replace("content1", pb.getContent1());
			converter.replace("content2", pb.getContent2());
			converter.replace("content3", pb.getContent3());
			converter.replace("content4", pb.getContent4());
			converter.replace("content5", pb.getContent5());
			converter.replace("content6", pb.getContent6());
			converter.replace("content7", pb.getContent7());
			converter.replace("content8", pb.getContent8());
			converter.replace("content9", pb.getContent9());
			converter.replace("content10", pb.getContent10());
			converter.replace("content11", pb.getContent11());
			converter.replace("content12", pb.getContent12());
			converter.replace("content13", pb.getContent13());
			converter.replace("content14", pb.getContent14());
			converter.replace("content15", pb.getContent15());
			converter.replace("content16", pb.getContent16());
			converter.replace("content17", pb.getContent17());
			converter.replace("content18", pb.getContent18());
			converter.replace("content19", pb.getContent19());
			converter.replace("content20", pb.getContent20());
			
			converter.replace("content21", pb.getContent21());
			converter.replace("content22", pb.getContent22());
			converter.replace("content23", pb.getContent23());
			converter.replace("content24", pb.getContent24());
			converter.replace("content25", pb.getContent25());
			converter.replace("content26", pb.getContent26());
			converter.replace("content27", pb.getContent27());
			converter.replace("content28", pb.getContent28());
			converter.replace("content29", pb.getContent29());
			converter.replace("content30", pb.getContent30());
			converter.replace("content31", pb.getContent31());
			converter.replace("content32", pb.getContent32());
			converter.replace("content33", pb.getContent33());
			converter.replace("content34", pb.getContent34());
			converter.replace("content35", pb.getContent35());
			converter.replace("content36", pb.getContent36());
			converter.replace("content37", pb.getContent37());
			converter.replace("content38", pb.getContent38());
			converter.replace("content39", pb.getContent39());
			converter.replace("content40", pb.getContent40());
			converter.replace("content41", pb.getContent41());
			
			if (!converter.parse(template, invoice1)) {
			    logger.info(converter.getErrorMessage());
			    return converter.getErrorMessage();
			}
			
			return printPdf2(nomefile);
			
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}



	public String printPdf2(String nomefile) {
		try {
		      String line;
		      //Provare anche: Process p = Runtime.getRuntime().exec("\"C:\\Program Files\\MiKTeX 2.9\\miktex\\bin\\latex.exe\" --output-format=pdf \"C:\\Users\\Administrator.max-PC\\WolframWorkspaces\\ISA\\resources\\tex\\provaGraphviz.tex\" -shell-escape ");
		      Process p = Runtime.getRuntime().exec("\"" + Constants.LATEX_PATH + "\" --output-format=pdf \"" + Constants.PATH_TO_TEX + nomefile + ".tex\" -shell-escape ");

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
		        if(line.startsWith(Constants.ERROR_FILE_OPENING_PROG)){
		        	ISAConsole.setNotifylineText(Constants.ERROR_FILE_OPENING);
		        	return Constants.ERROR_FILE_OPENING;
		        }
		      }
		      bri.close();
		      while ((line = bre.readLine()) != null) {
		        logger.info(line);
		      }
		      bre.close();
		      p.waitFor();
		      logger.info("Done.");
		      
	    	  	if (Desktop.isDesktopSupported()) {
		    	  logger.info("isDesktopSupported..");
		    	    try {
		    	    	
		    	        File myFile = new File(Constants.PATH_TO_PDF+"\\" + nomefile + ".pdf");
		    	        Desktop.getDesktop().open(myFile);
		    	        
		    	        return "Stampa completata";
		    	        
		    	    } catch (IOException ex) {
		    	    	
		    	    	// no application registered for PDFs
		    	    	logger.error("ERROR: ", ex);
		    	        return ex.getMessage();
		    	    }
		      }
		      else {
		    	  return "nessuna applicazione supportata per aprire pdf";
		      }				
		      
		    }
		    catch (Exception err) {
		      err.printStackTrace();
		      return err.getMessage();
		    }
	}

	@Override
	public String printGraphOnly(Graph g) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String printPdfGraph() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String stampaGrafo(PlaceholderBean pb) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
