package it.unimi.isa.service.impl;


import it.unimi.isa.model.Bacchetta;
import it.unimi.isa.model.Sigma;
import it.unimi.isa.model.graphs.Graph;
import it.unimi.isa.service.LaTexFormatterService;
import it.unimi.isa.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.experimental.theories.FromDataPoints;
import org.oeis.api.schema.IntegerSequence;
import org.oeis.api.services.IntegerSequenceQuery;
import org.oeis.api.services.OeisQueryFactory;
import org.springframework.stereotype.Service;

import com.wolfram.jlink.KernelLink;

@Service
public class LaTexFormatterServiceImpl implements LaTexFormatterService {
	
	private static final Logger logger = Logger.getLogger(LaTexFormatterServiceImpl.class);
	
	/**
	 * Metodo di stampa tabella indipendenti su latex (ottenuta da forza bruta)
	 * @param listIndip
	 * @return
	 */
	public String formatIndependentMatrix(List<HashMap <Integer,List<Integer>>> listIndip, Graph g)
	{
		
		int max_k = 0, c=0;
		
		for (HashMap <Integer,List<Integer>> a : listIndip) 
		{
		    List<Integer> list = a.get(c);
		    if(list.size()>max_k){
		    	max_k = list.size();
		    }
		    c++;
		}
		
		String table = "$$\\begin{tabular}{c |";
		
		for(int i=0;i<max_k;i++)
			table += " r";
		
		//table += " r r r r r r r r r r";
		
		table += "}";
		//table += "\n$"+g.getF().toUpperCase()+"(n,k)$&$k=0$";
		table += "\n$T(n,k)$&$k=0$";
		
		for(int i=1; i<max_k; i++)
			table += "&"+i;
		
		//table += "&1&2&3&4&5&6&7&";
		
		table += "\\\\ \\hline \n";
		
		int n = 0;
		int M = g.getM();
		if(M>1)
		{
//			table += "$n=0$&1\\\\ ";
			n = 0;
		}
		else
		{
			n = 0;
		}
		
		for (HashMap <Integer,List<Integer>> a : listIndip) 
		{
			table += "$"+n+"$";				
		    List<Integer> list = null;
		    if(M>1)
		    {
		    	list = a.get(n);
			}
			else
			{
				list = a.get(n);
			}
		    
		    for(Integer j : list)
		    {
		    	table +="&"+j;
		    }

			n++;
			
			if((n-1)!=listIndip.size())
				table += "\\\\ \n";
			
		}
		
		table += "\\end{tabular}$$\n\n";
		return table;
	}

	/**
	 * metodo di stampa tabella indipendenti su latex (ottenuta dalla ricorrenza)
	 * @param m
	 * @return
	 */
	public String formatIndependentMatrix(String[][] m, Graph g) 
	{
		
		int max_k = 0;
		for (int i = 0; i < m.length; i++) 
		{
			int k = 0;
		    for (int j = 0; j < m[i].length; j++) 
		    {
		    	if(m[i][j]=="0")
		    		k = j;
		    }
		    if(k>max_k)
		    	max_k = k;
		}
		
		max_k++;
		
		String table = "$$\\begin{tabular}{c |";
		
		for(int i=0;i<max_k;i++)
			table += " r";
		
		//table += " r r r r r r r r r r";
		
		table += "}";
		table += "$"+g.getF().toUpperCase()+"(n,k)$&$k=0$";
		
		for(int i=1; i<max_k; i++)
			table += "&"+i;
		
		//table += "&1&2&3&4&5&6&7&";
		table += "\\\\ \\hline ";
		
		for (int i = 0; i < m.length; i++) 
		{
			if(i==0)
			{
				table += "$n="+i+"$";
			}
			else
			{
				table += "$"+i+"$";	
			}
			
		    for (int j = 0; (j < m[i].length) && (m[i][j]!=null && !m[i][j].equals("0")); j++) 
		    {
		    	table +="&"+m[i][j];
		    }
		    
		    table += "\\\\ ";
		}
		
		table += "\\end{tabular}$$";
	
		return table;
	}

	@SuppressWarnings("unchecked")
	public String formatSistemaLineare(HashMap<String, List<String>> sistema, String letteraFgo) 
	{
		
		String output = "\n$$\\left\\{\n\\begin{tabular}{ll} \n";
		letteraFgo = letteraFgo.toUpperCase();
		List<String> values = sistema.get(letteraFgo);
		output += "\t$"+letteraFgo.toLowerCase()+" \\longrightarrow\\ ";
	    
		int dim = values.size()-1;
		int i = 0;
		for(String s : values)
	    {
	    	output += s.toLowerCase() ;
	    	if(i<dim){
	    		output += " + ";
	    	}
	    	i++;
	    }
	    
		output += " $\\\\\n";
		
		Iterator entries = sistema.entrySet().iterator();
		
		while (entries.hasNext()) 
		{
		    Map.Entry entry = (Map.Entry) entries.next();
		    String key = (String)entry.getKey();
		
		    if(!key.equalsIgnoreCase(letteraFgo))
		    {
		    	values = (ArrayList<String>) entry.getValue();
			    output += "\t$"+key.toLowerCase()+" \\longrightarrow\\ ";
			    int dim2 = values.size()-1;
				int i2 = 0;
			    for(String s : values)
			    {
			    	output += s.toLowerCase() ;
			    	if(i2<dim2){
			    		output += " + ";
			    	}
			    	i2++;
			    }
			    
			    output += " $\\\\\n";
		    }
		}
		
		output += "\\end{tabular}\n\\right.$$\n";
		return output;
	}
	
	@SuppressWarnings("unchecked")
	public String formatSistemaLineareAutoma(HashMap<String, List<String>> sistema, String letteraFgo, int h) 
	{
		String output = "\n$$\\left\\{\n\\begin{tabular}{ll} \n";
		
		letteraFgo = letteraFgo.toUpperCase();
		List<String> values = sistema.get(letteraFgo);
		output += "\t$"+letteraFgo+" \\rightarrow ";
	    
		for(String s : values)
	    {
			String appendLetter = s.substring(s.length()-1);
	    	output += appendLetter.toLowerCase() + " " +s + " \\mid  ";
	    }
	    
		output += " \\lambda $\\\\\n";
		
		Iterator entries = sistema.entrySet().iterator();
		
		while (entries.hasNext()) 
		{
		    Map.Entry entry = (Map.Entry) entries.next();
		    String key = (String)entry.getKey();
		    if(!key.equalsIgnoreCase(letteraFgo))
		    {
			    values = (ArrayList<String>) entry.getValue();
			    output += "\t$"+key+" \\rightarrow ";
			    for(String s : values)
			    {
			    	String appendLetter = s.substring(s.length()-1);
			    	output += appendLetter.toLowerCase() + " " +s + " \\mid  ";
			    }
			    output += " \\lambda $\\\\\n";
		    }
		}
		
		output += "\\end{tabular}\n\\right.$$\n";
		return output;
	}
	
	public String formatSistemaLineareAutomaCircuiti(HashMap<String, List<String>> sistema, String letteraFgo, int h) 
	{
		String output = "\n$$\\left\\{\n\\begin{tabular}{ll} \n";
		
		letteraFgo = letteraFgo.toUpperCase();
		List<String> values = sistema.get(letteraFgo);
		output += "\t$"+letteraFgo+" \\rightarrow ";
	    
		int count1 = 0;
		for(String s : values)
	    {
			int sub = 1;
	    	int subend = s.length();
	    	if(s.contains("_")){
	    		sub = 3;
	    		subend = s.length()-2;
	    	}
	    	
	    	String appendLetter = s.substring(s.length()-sub,subend);
			//String appendLetter = s.substring(s.length()-1);
	    	output += appendLetter.toLowerCase() + " " +s + " ";
	    	
	    	if(count1<values.size()-1)
	    		output += " \\mid  ";
	    		
	    	count1++;
	    }
	    
		boolean isStatoNonFinale = false;
		if(letteraFgo.contains("_")){
	    	try{
	    		int numero = Integer.parseInt(letteraFgo.substring(letteraFgo.length()-1));
	    		isStatoNonFinale = true;
	    	}
	    	catch(NumberFormatException e){
	    		System.out.println("errore nel controllo stato non finale");
	    	}
		}
		if(!isStatoNonFinale)
			output += " \\mid \\lambda $\\\\\n";
		else
			output += " $\\\\\n";
		
		Iterator entries = sistema.entrySet().iterator();
		
		while (entries.hasNext()) 
		{
		    Map.Entry entry = (Map.Entry) entries.next();
		    String key = (String)entry.getKey();
		    if(!key.equalsIgnoreCase(letteraFgo))
		    {
			    values = (ArrayList<String>) entry.getValue();
			    output += "\t$"+key+" \\rightarrow ";
			    int count2 = 0;
			    for(String s : values)
			    {
			    	int sub = 1;
			    	int subend = s.length();
			    	if(s.contains("_")){
			    		sub = 3;
			    		subend = s.length()-2;
			    	}
			    	
			    	String appendLetter = s.substring(s.length()-sub,subend);
			    	output += appendLetter.toLowerCase() + " " +s + " ";
			    	
			    	if(count2<values.size()-1)
			    		output += " \\mid  ";
			    	
			    		
			    	count2++;
			    }
			    
			    boolean isStatoNonFinale2 = false;
				if(key.contains("_")){
			    	try{
			    		int numero = Integer.parseInt(key.substring(key.length()-1));
			    		isStatoNonFinale2 = true;
			    	}
			    	catch(NumberFormatException e){
			    		System.out.println("errore nel controllo stato non finale");
			    	}
				}
				if(!isStatoNonFinale2)
					output += " \\mid \\lambda $\\\\\n";
				else
					output += "  $\\\\\n";
				
		    }
		}
		
		output += "\\end{tabular}\n\\right.$$\n";
		return output;
	}
	
	@SuppressWarnings("unchecked")
	public String formatSistemaLineareGrammaticaCircuiti(HashMap<String, List<String>> sistema, String letteraFgo) 
	{
		/*
		$$\left\{\begin{tabular}{c}
		$S = tS + tA(t) + tB(t) + 1$\\		
		$A(t) = tC(t) + tE(t) + 1$\\		
		$B(t) = tD(t) + tF(t) + 1$\\		
		$C(t) = tS(t) + tB(t) + 1$\\		
		$D(t) = tS(t) + tA(t) + 1$\\		
		$F(t) = tC(t) + 1$\\		
		$E(t) = tD(t) + 1$ 
		\end{tabular}\right.$$
		 */
		
		String math = "\n$$\\left\\{\n\\begin{tabular}{ll}";
		
		letteraFgo = letteraFgo.toUpperCase();
		List<String> values = sistema.get(letteraFgo);
		math += "\t$"+letteraFgo+"(x) = ";
		
		int count1 = 0;
	    for(String s : values)
	    {
	    	math += " x " + s + "(x) ";
	    	
	    	if(count1<values.size()-1)
	    		math += " +  ";
	    		
	    	count1++;
	    }
	    
	    boolean isStatoNonFinale = false;
		if(letteraFgo.contains("_")){
	    	try{
	    		int numero = Integer.parseInt(letteraFgo.substring(letteraFgo.length()-1));
	    		isStatoNonFinale = true;
	    	}
	    	catch(NumberFormatException e){
	    		System.out.println("errore nel controllo stato non finale");
	    	}
		}
		if(!isStatoNonFinale)
			math += " + 1 $\\\\\n";
		else
			math += " $\\\\\n";
	    
		
		Iterator entries = sistema.entrySet().iterator();
//		int max = 0;
		
		while ( entries.hasNext()) 
		{
		    Map.Entry entry = (Map.Entry) entries.next();
		    String key = (String)entry.getKey();
		    if(!key.equalsIgnoreCase(letteraFgo))
		    {		    
			    values = (ArrayList<String>) entry.getValue();
			   		    
			    math += "\t$"+key + "(x) = ";
			    int count2 = 0;
			    for(String s : values)
			    {
			    	math += " x " + s + "(x) ";
			    	
			    	if(count2<values.size()-1)
			    		math += " +  ";
			    		
			    	count2++;
			    }
			    
			    boolean isStatoNonFinale2 = false;
				if(key.contains("_")){
			    	try{
			    		int numero = Integer.parseInt(key.substring(key.length()-1));
			    		isStatoNonFinale2 = true;
			    	}
			    	catch(NumberFormatException e){
			    		System.out.println("errore nel controllo stato non finale");
			    	}
				}
				if(!isStatoNonFinale2)
					math += " + 1 $\\\\\n";
				else
					math += "  $\\\\\n";
			    
		    }
		}

		math += "\\end{tabular}\n\\right.$$\n\n";
		
		return math;
	}

	@SuppressWarnings("unchecked")
	public String formatSistemaLineareGrammatica(HashMap<String, List<String>> sistema, String letteraFgo) 
	{
		/*
		$$\left\{\begin{tabular}{c}
		$S = tS + tA(t) + tB(t) + 1$\\		
		$A(t) = tC(t) + tE(t) + 1$\\		
		$B(t) = tD(t) + tF(t) + 1$\\		
		$C(t) = tS(t) + tB(t) + 1$\\		
		$D(t) = tS(t) + tA(t) + 1$\\		
		$F(t) = tC(t) + 1$\\		
		$E(t) = tD(t) + 1$ 
		\end{tabular}\right.$$
		 */
		
		String math = "\n$$\\left\\{\n\\begin{tabular}{ll}";
		
		letteraFgo = letteraFgo.toUpperCase();
		List<String> values = sistema.get(letteraFgo);
		math += "\t$"+letteraFgo+"(x) = ";
		
	    for(String s : values)
	    {
	    	math += " x " + s + "(x) + ";
	    }
	    
	    math += " 1$\\\\\n";
		
		Iterator entries = sistema.entrySet().iterator();
//		int max = 0;
		
		while ( entries.hasNext()) 
		{
		    Map.Entry entry = (Map.Entry) entries.next();
		    String key = (String)entry.getKey();
		    if(!key.equalsIgnoreCase(letteraFgo))
		    {		    
			    values = (ArrayList<String>) entry.getValue();
			   		    
			    math += "\t$"+key + "(x) = ";
			    for(String s : values)
			    {
			    	math += " x " + s + "(x) + ";
			    }
			    math += " 1$\\\\\n";
		    }
		}

		math += "\\end{tabular}\n\\right.$$\n\n";
		
		return math;
	}

	public String formatLettereAlfabeto(List<Bacchetta> alfabeto) 
	{
		int j = 1;
		
		String tmp = "$$\\mbox{$"+alfabeto.get(0).getLettera().toLowerCase()+"$}";
		
		for(Bacchetta b : alfabeto)
		{
			tmp += "\\ \\ \\ \n\\begin";
			tmp += "{tikzpicture}[,>=stealth',shorten >=1pt,auto,node distance=1.0cm,thick,main node/.style={circle,fill=blue!20,draw,font=\\sffamily},main node2/.style={circle,fill=white!20,draw,font=\\sffamily},main node3/.style={circle,fill=yellow!20,draw,font=\\sffamily}] \n";
			int[] list = b.getNodiScelti();
			for(int i=0; i<list.length; i++)
			{				
				String node = "node";
				if(list[i]==0)
				{
					node = "node2";
				}
				if(i==0)
				{
					tmp += "\t\\node[main "+node+"] ("+i+") {}; \n";
				}
				else 
				{
					tmp += "\t\\node[main "+node+"] ("+i+") [above of="+(i-1)+"] {}; \n";
				}				
			}
			tmp += "\\path[every node/.style={font=\\sffamily\\small}] \n";
			for(int i=0; i<list.length-1; i++)
			{
				tmp += "\t("+(i)+") edge [] node[above] {} ("+(i+1)+")\n";				
			}
			
			tmp += ";\n\\end{tikzpicture}\n";
			
			if(j<alfabeto.size())
			{
				tmp += "\\ \\ \\ \n\\mbox{$"+alfabeto.get(j).getLettera().toLowerCase()+"$}";
			}			
			
			j++;
		}
		tmp += "$$";
		return tmp;
	}

	public String formatFgo(String fgo, String letter, String var) 
	{
		String output = "";
		int i = fgo.indexOf("/");
		String num = fgo.substring(0,i);
		String den = fgo.substring(i+1);
		output = "\n\n$$" + letter.toUpperCase() + "("+var+")=\\frac{"+num+"}{"+den+"}\\ .$$\n";
//		output = "\n\n$$" + letter.toUpperCase() + "("+var+")="+fgo+"$$\n";
		return output;
	}
	
	public String formatRSnFormula(String fgo, KernelLink ml) {
		String coeffListDenominatore = ml.evaluateToInputForm("CoefficientList[Denominator["+fgo+"],x]", 0);
		int[] a = Utils.getIntArrayFromString(coeffListDenominatore);
		String output = "$$RS_n = ";
		for(int i=1;i<a.length;i++){
			if(a[i]>1){
				output += a[i];
			}
			output += "RS_{n-"+i+"}";
			if(i<a.length-1){
				output += " + ";
			}
		}
		return output+ "$$ \n\n";
	}
	
	public String formatFgo2(String fgo, String expansion, String letter, String var) 
	{
		String partialResult = "";
		String a[] = Utils.getArrayFromString(expansion);
		int dim = a.length;
		if(dim>6) dim = 6;
		for(int i=0;i<dim;i++){
			partialResult += a[i];
			if(i>=2){
				partialResult += "x^{"+i+"}";
			}
			else if (i==1){
				partialResult += "x";
			}
			 
			if(i<dim-1){
				partialResult += " + ";
			}
		}
		partialResult += " + O(x^{"+dim+"})";
		
		String output = "";
		int i = fgo.indexOf("/");
		String num = fgo.substring(0,i);
		String den = fgo.substring(i+1);
		output = "\n\n$$" + letter.toUpperCase() + "("+var+")=\\frac{"+num+"}{"+den+"}="+partialResult+" $$\n";
//		output = "\n\n$$" + letter.toUpperCase() + "("+var+")="+fgo+"$$\n";
		return output;
	}

	public String formatEspansioneInSerie(String coeffList, String type) 
	{
		/*
		$$\begin{tabular}{c | r r r r r r r r r r}
		$n$&$0$&1&2&3&4&5&6&7\\ \hline 
		$d_n$&1&1&3&5&9&17&31&57
		\end{tabular}$$\\ 
		*/
		String output = "\n$$\\begin{tabular}{c |";
		String [] rs = Utils.getArrayFromString(coeffList);
		for(int i=0;i<rs.length;i++)
		{
			output += " r";
		}
		
		output += "}\n";
		output += "$n$&$0$";
		
		for(int i=1;i<rs.length;i++)
		{
			output += "&"+i;
		}
		
		output += "\\\\ \\hline\n$"+type+"_n$";
		
		for(int i=0;i<rs.length;i++)
		{
			output += "&"+rs[i];
		}
		
		output += "\n\\end{tabular}$$\n";
		
		return output;
	}
	
	public String formatEspansioneIntegerInSerie(List<Integer> rowSumList, String type) 
	{
		String output = "\n$$\\begin{tabular}{c |";
		for(int i=0;i<rowSumList.size();i++)
		{
			output += " r";
		}
		
		output += "}\n";
		output += "$n$&$0$";
		
		for(int i=1;i<rowSumList.size();i++)
		{
			output += "&"+i;
		}
		
		output += "\\\\ \\hline\n$"+type+"_n$";
		
		for (Integer integer : rowSumList) 
		{
			output += "&"+integer;
		}
		
		output += "\n\\end{tabular}$$\n";
		
		return output;
	}

	/**
	 * La mappa passata come parametro contiene come chiave il nome della espansione e come valore l'espansione in serie.
	 * NB. Le espansioni devono essere composte dallo stesso numero di elementi, altrimenti la stampa fallisce.
	 * @param espansioniMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String formatMultiEspansioneInSerie(HashMap<String, String> espansioniMap) 
	{
		String output = "\n$$\\begin{tabular}{c |";
		
		int j=0;
		
		Iterator entries = espansioniMap.entrySet().iterator();
		while (entries.hasNext()) 
		{
			
			Map.Entry entry = (Map.Entry) entries.next();
			
			if(j==0)
			{
				String coeffList = (String)entry.getValue();
				String [] rs = Utils.getArrayFromString(coeffList);
				for(int i=0;i<rs.length;i++)
				{
					output += " r";
				}
				
				output += "}\n";
				output += "$n$&$0$";
				
				for(int i=1;i<rs.length;i++)
				{
					output += "&"+i;
				}
				
				output += "\\\\ \\hline\n";
			}
			
		    
		    String key = (String)entry.getKey();
		    String value = (String)entry.getValue();
		    String [] arrayvalue = Utils.getArrayFromString(value);
		    
		    output += "$"+key+"_n$";
			
		    for(int i=0;i<arrayvalue.length;i++)
		    {
				output += "&"+arrayvalue[i];
			}
			
		    output += "\\\\ \\hline\n";
		    
		    //System.out.println("Key = " + key + ", Value = " + value);
		    
		    j++;
		}
		
		output += "\n\\end{tabular}$$\n";
		return output;
	}
	
	@SuppressWarnings("unchecked")
	public String formatTransferMatrix(String[][] xTM, HashMap<String, List<String>> sistema) 
	{
		String tmp = 
			"\n$$\\begin{tabular}{|";
		
		for(int i=0;i<xTM.length+1;i++)
		{
			tmp += "c|";
		}
		
		tmp += "} \\hline\n";
		
		Iterator entries = sistema.entrySet().iterator();
		
		int f = 1;
		
		String temp[] = new String[sistema.size()];
		
		tmp += "$TM$   &  ";
		
		while (entries.hasNext()) 
		{
		    Map.Entry entry = (Map.Entry) entries.next();
		    String key = (String)entry.getKey();
		    tmp += "$"+key.toLowerCase()+"$";
		    temp[f-1] = key.toLowerCase();
		    
		    if(f<(sistema.size()))
		    {
				tmp += "   &  ";
			}
			else if(f==(sistema.size()))
			{
				tmp += "\\\\ \\hline\n";
			}
		    
		    f++;
		}
		
		for(int i=0;i<xTM.length;i++)
		{
			for(int j=-1;j<xTM[i].length;j++)
			{				
				if(j==-1)
				{
					tmp += "$"+temp[i]+"$";
				}
				else 
				{
					if("x".equalsIgnoreCase(xTM[i][j]))
					{
						tmp += "$1$";
					}
					else
					{
						tmp += "$"+xTM[i][j]+"$";
					}
				}
				
				if(j<(xTM[i].length-1))
				{
					tmp += "   &  ";
				}
				else if(j==(xTM[i].length-1))
				{
					tmp += "\\\\ \\hline\n";
				}
			}
		}
		
		tmp += "\\end{tabular}$$ ";
		
		return tmp;
	}

	@SuppressWarnings("unchecked")
	public String formatProseguimentoSistema(HashMap<String, List<String>> sistema, String letteraFgo) 
	{
		String output = "\n\n$$\\left\\{\n\\begin{tabular}{ll} \n";
		letteraFgo = letteraFgo.toUpperCase();
		List<String> values = sistema.get(letteraFgo);
		output += "\t$"+letteraFgo.toLowerCase()+" \\longrightarrow\\  ";
	    
		int dim = values.size()-1;
	    int i = 0;
		for(String s : values)
	    {
	    	output += s.toLowerCase() ;
	    	if(i<dim){
	    		output += " + ";
	    	}
	    	i++;
	    }
	    
		output += " $\\\\\n";
		
		Iterator entries = sistema.entrySet().iterator();
		
		while (entries.hasNext()) 
		{
		    Map.Entry entry = (Map.Entry) entries.next();
		    String key = (String)entry.getKey();
		    if(!key.equalsIgnoreCase(letteraFgo))
		    {
		    	values = (ArrayList<String>) entry.getValue();
			    output += "\t$"+key.toLowerCase()+" \\longrightarrow\\  ";
			    int dim2 = values.size()-1;
			    int i2 = 0;
			    for(String s : values)
			    {
			    	output += s.toLowerCase() ;
			    	if(i2<dim2){
			    		output += " + ";
			    	}
			    	i2++;
			    }
			    
			    output += " $\\\\\n";
		    }
		}
		
		output += "\\end{tabular}\n\\right.$$\n";
	
		return output;
	}

	public String getSloaneBijections(String sequenza) 
	{
		String output2_7 = "\n\n\n";
		
		try 
		{
			
			OeisQueryFactory factory = OeisQueryFactory.newInstance();
			IntegerSequenceQuery service = factory.createIntegerSequenceQuery();
			long array [] = Utils.getLongArrayFromString(sequenza);
			
			List<IntegerSequence> sequences = service.withOrderedTerms(array).list();		
			
			for (IntegerSequence sequence : sequences) 
			{	
			
				String catalogNumber = sequence.getCatalogNumber();
//				String identification = sequence.getIdentification();
				String name = sequence.getName();
				
//				output2_7 += " \\\\ ";
				output2_7 += "\t" + catalogNumber + " \\begin{footnotesize}$"+name+"$\\end{footnotesize} \\\\ \n";//+ ", " + identification + ", " + name ;
				
			}
			
//			output2_7 += "\\\\\n";
			
		} 
		catch (Exception e) 
		{
			//se si verifica una eccezione qui, molto probabilmente si è disconnessi dalla rete e non è possibile scaricare dati dal sito oeis.org
			output2_7 += " impossibile collegarsi al server oeis ";
			e.printStackTrace();
		}
		
		return output2_7;
	}

	public String formatParagraph(String string) 
	{
		return "\n\n\\bigskip "+string+"\\par\n";
	}

	public String formatSubSection(String string) 
	{
		return "\\subsection{"+string+"} \\\n\n";
	}

	public String creaSpazio() 
	{
		return "\n\\bigskip ";
	}

	@SuppressWarnings("unchecked")
	public String formatProseguimentoSistemaCircuito(HashMap<String, List<String>> sistema) 
	{
		String output = "\n\n$$\\left\\{\n\\begin{tabular}{ll} \n";
		Iterator entries = sistema.entrySet().iterator();
		
		while (entries.hasNext()) 
		{
		    Map.Entry entry = (Map.Entry) entries.next();
		    String key = (String)entry.getKey();
		    List<String> values = (ArrayList<String>) entry.getValue();
		    output += "\t$"+key+" \\longrightarrow\\  ";
		    
		    int i = 0;
		    
		    for(String s : values)
		    {
		    	output += s + " ";		    	
		    	if(i<values.size()-1)
		    		output += "+ ";
		    	i++;
		    }
		    
		    output += " $\\\\\n";
		}
		
		output += "\\end{tabular}\n\\right.$$\n";
		
		return output;
		
	}

	public String formatListaAdiacenza(Graph g) 
	{
		String output = "\n\n$$\\left\\{\n\\begin{tabular}{ll} \n";
		for (int v = 0; v < g.V(); v++) 
		{
			output += "\t$("+g.getVertexN().get(String.valueOf(v))+") \\longrightarrow\\  ";
			
			for (int w : g.adj(v)) 
			{
				output += "("+g.getVertexN().get(String.valueOf(w)) + "), " ; //[ label = \""+s.toLowerCase()+"\" ]
			}
			
			output += " $\\\\\n";
		}
		
		output += "\\end{tabular}\n\\right.$$\n";
		return output;
	}

	/**
	 * Questa versione di stampa dell'automa va bene solo per i non circuiti (12102015)
	 * @param sistema
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getAutoma2(HashMap<String, List<String>> sistema, String statoIniziale,int h,int m) 
	{
		logger.info("formatta disegno automa");
		
		boolean landscape = m>=2 && h>=3;
		
		String output = "";
		
		if(landscape){
			output += "\\begin{landscape} \n";
		}
		
		output +=
			"\n\\begin{figure}[H]\n";
		
		if(landscape){
			output+="\n"+
			"\\scalefont{0.8}\n";
			
			output+=
				"\t\\centering\n"+
				"\t\t\\begin{tikzpicture}[>=latex',scale=0.8]\n"+
				"\t\t\t\\begin{dot2tex}[tikz] \n"+
				"\t\t\tdigraph finite_state_machine { \n"+
				
				"\t\t\t\tgraph[rankdir=LR, center=true, margin=0.2, nodesep=0.1, ranksep=0.3] \n"+
			    "\t\t\t\tnode[shape=doublecircle, fontname=\"Courier-Bold\", fontsize=10, width=0.4, height=0.4, fixedsize=true] \n"+
			    "\t\t\t\tedge[arrowsize=0.1, arrowhead=vee] \n\n";
		}
		else {
			output+=
				"\t\\centering\n"+
				"\t\t\\begin{tikzpicture}[>=latex',scale=0.8]\n"+
				"\t\t\t\\begin{dot2tex}[tikz] \n"+
				"\t\t\tdigraph finite_state_machine { \n"+
				
				"\t\t\t\tgraph[rankdir=LR, center=true, margin=0.2, nodesep=0.1, ranksep=0.3] \n"+
			    "\t\t\t\tnode[shape=doublecircle, fontname=\"Courier-Bold\", fontsize=10, width=0.4, height=0.4, fixedsize=true] \n"+
			    "\t\t\t\tedge[arrowsize=0.6, arrowhead=vee] \n\n";
		}
		
		List<String> linkedToInitialState = sistema.get(statoIniziale);
		
		for(String s : linkedToInitialState)
		{
			
			String transizione = s.toLowerCase().substring(h-1);
			
			output += "\t\t\t\t"+ statoIniziale + " -> " + s.toUpperCase() + " [ label = \""+transizione+"\" ]; \n";	
		}
		
		Iterator entries = sistema.entrySet().iterator();
		
		while (entries.hasNext()) 
		{
		    Map.Entry entry = (Map.Entry) entries.next();
		    String statoCorrente = (String)entry.getKey();
		
		    if(!statoCorrente.equalsIgnoreCase(statoIniziale))
		    {
		    	List<String> values = (ArrayList<String>) entry.getValue();
			    for(String s : values)
			    {		    	
			    	String transizione = s.toLowerCase().substring(h-1);
			    	output += "\t\t\t\t"+ statoCorrente + " -> " + s.toUpperCase() + " [ label = \""+transizione+"\" ]; \n";		    	
			    }
		    }		    		   
		}
		
		output += 
//			" \n\t\t\t\tlabel=\"My Diagram\"; \n"+
			"\t\t\t} \n"+			
			"\t\t\\end{dot2tex} \n"+
			"\t\\end{tikzpicture} \n"+
			"\\end{figure} \n\n"+
			"";
		if(landscape){
			output += "\\end{landscape} \n\n";
		}
		
		return output;
	}
	
	public String getAutomaCircuiti2(HashMap<String, List<String>> sistema, String statoIniziale,int h,int m) 
	{
		logger.info("formatta disegno automa");
		
		boolean landscape = m>=2 && h>=3;
		
		String output = "";
		
		if(h==1){
			if(landscape){
				output += "\\begin{landscape} \n";
			}
			
			output +=
				"\n\\begin{figure}[H]\n";
			
			if(landscape){
				output+="\n"+
				"\\scalefont{0.8}\n";
				
				output+=
					"\t\\centering\n"+
					"\t\t\\begin{tikzpicture}[>=latex',scale=0.8]\n"+
					"\t\t\t\\begin{dot2tex}[tikz] \n"+
					"\t\t\tdigraph finite_state_machine { \n"+
					
					"\t\t\t\tgraph[rankdir=LR, center=true, margin=0.2, nodesep=0.1, ranksep=0.3] \n"+
				    "\t\t\t\tnode[shape=doublecircle, fontname=\"Courier-Bold\", fontsize=10, width=0.4, height=0.4, fixedsize=true] \n"+
				    "\t\t\t\tedge[arrowsize=0.1, arrowhead=vee] \n\n";
			}
			else {
				output+=
					"\t\\centering\n"+
					"\t\t\\begin{tikzpicture}[>=latex',scale=0.8]\n"+
					"\t\t\t\\begin{dot2tex}[tikz] \n"+
					"\t\t\tdigraph finite_state_machine { \n"+
					
					"\t\t\t\tgraph[rankdir=LR, center=true, margin=0.2, nodesep=0.1, ranksep=0.3] \n"+
				    "\t\t\t\tnode[shape=doublecircle, fontname=\"Courier-Bold\", fontsize=10, width=0.4, height=0.4, fixedsize=true] \n"+
				    "\t\t\t\tedge[arrowsize=0.6, arrowhead=vee] \n\n";
			}
			
			List<String> linkedToInitialState = sistema.get(statoIniziale);
			
			for(String s : linkedToInitialState)
			{
				int sub = 0;
		    	int subend = s.length();
		    	if(s.contains("_")){
		    		sub = (h-1);
		    		subend = s.length()-2;
		    	}
		    	
		    	String transizione = s.toLowerCase().substring(sub,subend);
		    	
				//String transizione = s.toLowerCase().substring(h-1);
				
				output += "\t\t\t\t"+ statoIniziale + " -> \"" + s + "\" [ label = \""+transizione+"\" ]; \n";	
				
				if(s.contains("_")){
					//prova controllo stato non finale
			    	String check_stato_non_finale = s.toLowerCase().substring(subend+1);
			    	try{
			    		int numero = Integer.parseInt(check_stato_non_finale);
			    		output += "\n\n \t\t\t\t "+s+" [shape=circle];";
			    	}
			    	catch(NumberFormatException e){
			    		System.out.println("errore nel controllo stato non finale");
			    	}
			    	System.out.println("continua..");
				}
			}
			
			Iterator entries = sistema.entrySet().iterator();
			
			while (entries.hasNext()) 
			{
			    Map.Entry entry = (Map.Entry) entries.next();
			    String statoCorrente = (String)entry.getKey();
			
			    if(!statoCorrente.equalsIgnoreCase(statoIniziale))
			    {
			    	List<String> values = (ArrayList<String>) entry.getValue();
				    for(String s : values)
				    {		    
				    	int sub = 0;
				    	int subend = s.length();
				    	if(s.contains("_")){
				    		sub = (h-1);
				    		subend = s.length()-2;
				    	}
				    	String transizione = s.toLowerCase().substring(sub,subend);
				    	output += "\t\t\t\t"+ statoCorrente + " -> " + s + " [ label = \""+transizione+"\" ]; \n";
				    	
				    	if(s.contains("_")){
							//prova controllo stato non finale
					    	String check_stato_non_finale = s.toLowerCase().substring(subend+1);
					    	try{
					    		int numero = Integer.parseInt(check_stato_non_finale);
					    		output += "\n\n \t\t\t\t "+s+" [shape=circle];";
					    	}
					    	catch(NumberFormatException e){
					    		System.out.println("errore nel controllo stato non finale");
					    	}
					    	System.out.println("continua..");
						}
				    }
			    }		    		   
			}
			
			output += 
//				" \n\t\t\t\tlabel=\"My Diagram\"; \n"+
				"\t\t\t} \n"+			
				"\t\t\\end{dot2tex} \n"+
				"\t\\end{tikzpicture} \n"+
				"\\end{figure} \n\n"+
				"";
			if(landscape){
				output += "\\end{landscape} \n\n";
			}
		}
		else {
			// h>1
			if(landscape){
				output += "\\begin{landscape} \n";
			}
			
			output +=
				"\n\\begin{figure}[H]\n";
			
			if(landscape){
				output+="\n"+
				"\\scalefont{0.8}\n";
				
				output+=
					"\t\\centering\n"+
					"\t\t\\begin{tikzpicture}[>=latex',scale=0.8]\n"+
					"\t\t\t\\begin{dot2tex}[tikz] \n"+
					"\t\t\tdigraph finite_state_machine { \n"+
					
					"\t\t\t\tgraph[rankdir=LR, center=true, margin=0.2, nodesep=0.1, ranksep=0.3] \n"+
				    "\t\t\t\tnode[shape=doublecircle, fontname=\"Courier-Bold\", fontsize=10, width=0.4, height=0.4, fixedsize=true] \n"+
				    "\t\t\t\tedge[arrowsize=0.1, arrowhead=vee] \n\n";
			}
			else {
				output+=
					"\t\\centering\n"+
					"\t\t\\begin{tikzpicture}[>=latex',scale=0.8]\n"+
					"\t\t\t\\begin{dot2tex}[tikz] \n"+
					"\t\t\tdigraph finite_state_machine { \n"+
					
					"\t\t\t\tgraph[rankdir=LR, center=true, margin=0.2, nodesep=0.1, ranksep=0.3] \n"+
				    "\t\t\t\tnode[shape=doublecircle, fontname=\"Courier-Bold\", fontsize=10, width=0.4, height=0.4, fixedsize=true] \n"+
				    "\t\t\t\tedge[arrowsize=0.6, arrowhead=vee] \n\n";
			}
			
			List<String> linkedToInitialState = sistema.get(statoIniziale);
			
			for(String s : linkedToInitialState)
			{
				int sub = 0;
		    	int subend = s.length();
		    	if(s.contains("_")){
		    		sub = (h-1);
		    		subend = s.length()-2;
		    	}
		    	
		    	String transizione = "";
		    	if(s.contains("_"))
		    		transizione = s.toLowerCase().substring(0,1);
		    	else
		    		transizione = s.toLowerCase().substring(s.length()-1);
		    	
				//String transizione = s.toLowerCase().substring(h-1);
				
				output += "\t\t\t\t"+ statoIniziale + " -> \"" + s + "\" [ label = \""+transizione+"\" ]; \n";	
				
				if(s.contains("_")){
					//prova controllo stato non finale
			    	String check_stato_non_finale = s.toLowerCase().substring(subend+1);
			    	try{
			    		int numero = Integer.parseInt(check_stato_non_finale);
			    		output += "\n\n \t\t\t\t "+s+" [shape=circle];";
			    	}
			    	catch(NumberFormatException e){
			    		System.out.println("errore nel controllo stato non finale");
			    	}
			    	System.out.println("continua..");
				}
			}
			
			Iterator entries = sistema.entrySet().iterator();
			
			while (entries.hasNext()) 
			{
			    Map.Entry entry = (Map.Entry) entries.next();
			    String statoCorrente = (String)entry.getKey();
			
			    if(!statoCorrente.equalsIgnoreCase(statoIniziale))
			    {
			    	List<String> values = (ArrayList<String>) entry.getValue();
				    for(String s : values)
				    {		    
				    	int sub = 0;
				    	int subend = s.length();
				    	if(s.contains("_")){
				    		sub = (h-1);
				    		subend = s.length()-2;
				    	}
				    	
				    	String transizione = "";
				    	if(s.contains("_"))
				    		transizione = s.toLowerCase().substring(0,1);
				    	else
				    		transizione = s.toLowerCase().substring(s.length()-1);
				    	
				    	output += "\t\t\t\t"+ statoCorrente + " -> " + s + " [ label = \""+transizione+"\" ]; \n";
				    	
				    	if(s.contains("_")){
							//prova controllo stato non finale
					    	String check_stato_non_finale = s.toLowerCase().substring(subend+1);
					    	try{
					    		int numero = Integer.parseInt(check_stato_non_finale);
					    		output += "\n\n \t\t\t\t "+s+" [shape=circle];";
					    	}
					    	catch(NumberFormatException e){
					    		System.out.println("errore nel controllo stato non finale");
					    	}
					    	System.out.println("continua..");
						}
				    }
			    }		    		   
			}
			
			output += 
//				" \n\t\t\t\tlabel=\"My Diagram\"; \n"+
				"\t\t\t} \n"+			
				"\t\t\\end{dot2tex} \n"+
				"\t\\end{tikzpicture} \n"+
				"\\end{figure} \n\n"+
				"";
			if(landscape){
				output += "\\end{landscape} \n\n";
			}
		}
		
		return output;
	}
	
//	@SuppressWarnings("unchecked")
//	public static String getAutoma3(HashMap<String, List<String>> sistema, String statoIniziale) {
//		logger.info("formatta disegno automa");
//		String output =
//			"\n\\begin{figure}[H]\n"+
//			"\t\\centering\n"+
//			"\t\t\\begin{tikzpicture}[>=latex',scale=0.8]\n"+
//			"\t\t\t\\begin{dot2tex}[tikz] \n"+
//			"\t\t\tdigraph finite_state_machine { \n"+
//			
//			"\t\t\tstyle=invis \n"+
//			
//			"\t\tsubgraph cluster_0 { \n"+
//			
//				"\t\t\t\tgraph[rankdir=LR, center=true, margin=0.2, nodesep=0.1, ranksep=0.3] \n"+
//			    "\t\t\t\tnode[shape=doublecircle, fontname=\"Courier-Bold\", fontsize=10, width=0.4, height=0.4, fixedsize=true] \n"+
//			    "\t\t\t\tedge[arrowsize=0.6, arrowhead=vee] \n\n";
//			
//				List<String> linkedToInitialState = sistema.get(statoIniziale);
//				for(String s : linkedToInitialState){
//					output += "\t\t\t\t"+ statoIniziale + " -> " + s.toUpperCase() + " [ label = \""+s.toLowerCase()+"\" ]; \n";	
//				}
//				
//				Iterator entries = sistema.entrySet().iterator();
//				while (entries.hasNext()) {
//				    Map.Entry entry = (Map.Entry) entries.next();
//				    String statoCorrente = (String)entry.getKey();
//				    if(!statoCorrente.equalsIgnoreCase(statoIniziale)){
//				    	List<String> values = (ArrayList<String>) entry.getValue();
//					    for(String s : values){		    	
//					    	output += "\t\t\t\t"+ statoCorrente + " -> " + s.toUpperCase() + " [ label = \""+s.toLowerCase()+"\" ]; \n";		    	
//					    }
//				    }		    		   
//				}
//		output += "\n } \n\n";
//		output += "subgraph cluster_1 { \n"+
//		
//					"\t\t\t\tgraph[rankdir=LR, center=true, margin=0.2, nodesep=0.1, ranksep=0.3] \n"+
//				    "\t\t\t\tnode[shape=doublecircle, fontname=\"Courier-Bold\", fontsize=10, width=0.4, height=0.4, fixedsize=true] \n"+
//				    "\t\t\t\tC [shape=circle, style=filled, fillcolor=lightgray]; \n"+
//				    "\t\t\t\tF [shape=doublecircle, style=filled, fillcolor=lightgray]; \n"+
//				    
//				    "\t\t\t\tedge[arrowsize=0.6, arrowhead=vee] \n\n";
//		
//		 			//output += "\t\t\t\t C [shape=circle] \n\n";
//		
//				    output += "\t\t\t\t B -> F [ label = \"e\" ]; \n";
//				    output += "\t\t\t\t F -> F [ label = \"e\" ]; \n";
//				    output += "\t\t\t\t F -> C [ label = \"a\" ]; \n";
//				    output += "\t\t\t\t C -> F [ label = \"e\" ]; \n\n";
//		
//				    
//				    
//		   output +="\t\t\t\tlabel = \"circuito\"; \n"+
//					//"\t\t\t\tcolor=blue \n"+
//					"\t\t} \n";
//		
//		output += 
//			"\t\t S -> "+statoIniziale+" [ label = \""+statoIniziale.toLowerCase()+"\" ]; \n"+
//			"\t\t S -> B [ label = \"a\" ]; \n";
//			
//		output += 
////			" \n\t\t\t\tlabel=\"My Diagram\"; \n"+
//			"\t\t\t} \n"+			
//			"\t\t\\end{dot2tex} \n"+
//			"\t\\end{tikzpicture} \n"+
//			"\\end{figure} \n\n"+
//			"";
//		
//		return output;
//	}

	public String getAutoma4(int h, HashMap<String,List<String>> sistemaProseguimento) 
	{
		String output = "\n\n "+
		" $$\\begin{tikzpicture}[->,>=stealth',shorten >=1pt,auto,node distance=2.cm, \n"+
		" thick,main node/.style={circle,fill=blue!20,draw,font=\\sffamily},main node2/.style={circle,fill=green!20,draw,font=\\sffamily},main node3/.style={circle,fill=yellow!20,draw,font=\\sffamily}] \n"+
		" \\node[initial,initial text=,state,accepting] (1) {S}; \n"+
		" \\node[initial text=,state,accepting] (2) [right of=1] {$A$}; \n"+
		" \\node[initial text=,state,accepting] (3) [ below of=1] {$B$};  \n"+
		" \\node[initial text=,state,accepting] (4) [right of=2] {$E_1$}; \n"+
		" \\node[initial text=,state,accepting] (5) [right of=4] {$E_2$}; \n"+
		" \\node[initial text=,state,accepting] (6) [right of=3] {$F_1$}; \n"+
		" \\node[initial text=,state] (7) [right of=6] {$F_{2}$}; \n\n";
		
		List<String> list = new ArrayList<String>();
		list.add("e");
		list.add("a");
		list.add("1");
		sistemaProseguimento.put("S",list);
		
		list = new ArrayList<String>();
		list.add("e");
		list.add("a");
		list.add("1");
		sistemaProseguimento.put("A",list);
		
		list = new ArrayList<String>();
		list.add("e");
		list.add("1");
		sistemaProseguimento.put("E1",list);
		
		list = new ArrayList<String>();
		list.add("e");
		list.add("1");
		sistemaProseguimento.put("E2",list);
		
		list = new ArrayList<String>();
		list.add("e");
		list.add("1");
		sistemaProseguimento.put("B",list);
		
		list = new ArrayList<String>();
		list.add("e");
		list.add("a");
		list.add("1");
		sistemaProseguimento.put("F1",list);
		
		int n = 8;
		for(int i=1;i<h;i++)
		{
			output += " \\node[initial text=,state] ("+n+") [right of="+(n-1)+"] {$G_"+(i)+"$}; \n";
			n++;
			list = new ArrayList<String>();
			list.add("e");
			sistemaProseguimento.put("G"+i,list);
		}
		
		if(h==1)
		{
			output += " \\node[initial text=,state,accepting] ("+n+") [right of="+(n-1)+"] {$G_1$}; \n";
			n++;
			list = new ArrayList<String>();
			list.add("e");
			list.add("1");
			sistemaProseguimento.put("G1",list);
		}
		
		output += "\n \\path[every node/.style={font=\\sffamily\\small}] \n\n"+

		" (1) edge[] node[above] {$e$} (2) \n"+
		" (1) edge[] node[] {$a$} (3) \n"+
		" (2) edge[loop above] node[left=1pt] {e} (2) \n"+
		" (3) edge[] node[below] {$e$} (6) \n"+
		" (6) edge[loop below] node[left=1pt] {e} (6) \n"+
		" (5) edge[bend right] node[above] {$e$} (2) \n"+
		" (2) edge[] node[] {$a$} (4) \n"+
		" (4) edge [] node [] {$e$} (5) \n"+
		" (6) edge[ ]node[above] {$a$} (7) \n\n";
		
		n = 7;
		for(int i=1;i<h;i++)
		{
			output += " ("+n+") edge [] node [] {$e$} ("+(n+1)+") \n";
			n++;
		}
		
		if(h==1)
		{
			output += " ("+n+") edge [] node [] {$e$} ("+(n+1)+") \n";
			n++;
		}
		
		output += "\n ("+n+") edge[bend left] node[below] {$e$} (6); \n"+
		" \\end{tikzpicture}$$ \n\n";
		
		return output;
	}
	
	public String formatMatriceForAde(String[][] matrice, int h, int v){
		String output = 
			"\\begin{tabular}{|";
		
		for(int i=0;i<matrice[0].length;i++){
			output += " c ";
			if(i<v){
				output += "| ";
			}
			
			//c | c |
		}
			
		output += "} \\hline \n";
	
		for(int i=0; i<matrice.length;i++){
			for(int j=0; j<matrice[i].length; j++){
				String value = matrice[i][j].trim();
				output += "$"+value+"$";
				if(j<matrice[i].length-1){
					output += "&";
				}
			}
			if (i<h){
				output += "\\\\ \\hline \n";
			}
			else {
				output += "\\\\ \n";
			}
			
		}
		output += "\\end{tabular} ";
		return output;
	}

	public String formatSchemaRicorrenza(String[][] schema2, String initial) {
		String schema[][] = Utils.invertMatrix(schema2);
		
		String output = 
			"\\begin{tabular}{|";
		
		for(int i=0;i<schema[0].length;i++){
			output += " c |";
			//c | c |
		}
			
		output += "} \\hline \n";
	
		for(int i=0; i<schema.length-1;i++){
			for(int j=0; j<schema[i].length; j++){
				String value = schema[i][j].trim();
				output += "$"+value+"$";
				if(j<schema[i].length-1){
					output += "&";
				}
			}
			output += "\\\\ \\hline \n";
		}
		for(int i=schema.length-1;i<schema.length;i++){
			for(int j=0; j<schema[i].length; j++){
				String value = schema[i][j].trim();
				if(value.equals("*")){
					value = initial;
				}
				output += "$"+value+"$";
				if(j<schema[i].length-1){
					output += "&";
				}
			}
			output += "\\\\ \\hline \n";
		}
		
		output += "\\end{tabular} ";
		return output;
	}

	public String formatRicorrenzaLocaleFromSchema(String[][] schema) {
		String output = "$$T(n,k)=";

		Utils.print(schema);
		
		for(int i=1; i<schema.length;i++){
			for(int j = 0;j<schema[i].length;j++){
				String v = schema[i][j].substring(1,schema[i][j].length()-1);
				int valore = Integer.parseInt(v);
				String app = "";
				boolean print = false;
				if(valore>0){
					if(j>0){
						app = " + ";
					}
					print = true;
				}
				else if(valore<0){
					if(j>0){
						app = " - ";
					}
					print = true;
				}
				
				if(print){
					int a = (0-i);
					String a1 = "";
					if (a!=0){
						a1 = String.valueOf(a);
					}
					int b = (0-j);
					String b1 = "";
					if (b!=0){
						b1 = String.valueOf(b);
					}
					
					if(Math.abs(valore)>1)
						app += valore + " ";
					
					output += app + "T(n" + a1 +",k"+b1+")";
				}
				
				
			}
		}
		
		output += "$$";
		
		return output;
	}

	public String formatFgoSchemaRicorrenza(Sigma sigma, KernelLink ml, String string) {
		String output = "";
		if("H".equalsIgnoreCase(string)){
			String[] funcGenHarray = sigma.getFuncGenH();
			for(int i=0;i<funcGenHarray.length;i++){
				if(!funcGenHarray[i].contains("/")){
					output += "H_{"+i+"} = " + funcGenHarray[i];
				}
				else 
					output += "H_{"+i+"} = \\frac{"+ml.evaluateToInputForm("Numerator["+funcGenHarray[i]+"]", 0)+"}{"+ml.evaluateToInputForm("Denominator["+funcGenHarray[i]+"]", 0)+"}";
				if(i<funcGenHarray.length-1){
					output += ", ";
				}
			}
		}
		else if("V".equalsIgnoreCase(string)){
			String[] funcGenVarray = sigma.getFuncGenV();
			for(int i=0;i<funcGenVarray.length;i++){
				if(!funcGenVarray[i].contains("/")){
					output += "V_{"+i+"} = " +funcGenVarray[i];
				}
				else
				output += "V_{"+i+"} = \\frac{"+ml.evaluateToInputForm("Numerator["+funcGenVarray[i]+"]", 0)+"}{"+ml.evaluateToInputForm("Denominator["+funcGenVarray[i]+"]", 0)+"}";
				if(i<funcGenVarray.length-1){
					output += ", ";
				}
			}
		}
		
		return output;
	}

	
	
	
	
}
