package test.unimi.isa.indepsets;

import it.unimi.isa.model.SchemaObj;
import it.unimi.isa.service.impl.RicorrenzaLocaleServiceImpl;
import it.unimi.isa.utils.Constants;
import it.unimi.isa.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class RicorrenzaLocaleServiceImplTest {
	
	private static final Logger logger = Logger.getLogger(RicorrenzaLocaleServiceImplTest.class);
	
	@Autowired 
	RicorrenzaLocaleServiceImpl ricorrenzaLocaleServiceImpl;

	/**
	 * TEST ESECUZIONE ALGO DI RICERCA RICORRENZA LOCALE
	 * In questa classe sono state trovate le ricorrenze per alcune famiglie di supergriglie.
	 * A causa dei lunghi tempi di elaborazione per ottenere tabelle degli indipendenti necessarie per
	 * ottimizzare il risultato della ricerca di una ricorrenza, si è dovuto abbandonare quei casi in cui
	 * l'algoritmo non torna degli schemi che siano verosimili.
	 * 
	 * TODO alcuni test sono rimasti senza uno schema 
	 * preciso in quanto per avere una maggiore affidabilità del risultato è necessario calcolare tabelle
	 * di indipendenti con valori di n molto più grandi, il che richiede tempi di elaborazione drasticamente
	 * lunghi.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		logger.info("######## INIZIO TEST RicorrenzaLocaleServiceImplTest ########");
		
		final ApplicationContext context = new ClassPathXmlApplicationContext(Constants.CONFIG_PATH);
		final RicorrenzaLocaleServiceImplTest ricorrenzaLocaleServiceImplTest = context.getBean(RicorrenzaLocaleServiceImplTest.class);
//		ricorrenzaLocaleTest.init();
		
		/**
		 * Ricorrenze trovate con algo
		 */
		//ricorrenzaLocaleServiceImplTest.testP_1_1xP_10_1();
		//ricorrenzaLocaleServiceImplTest.testP_1_1xP_10_2();		
		//ricorrenzaLocaleServiceImplTest.testP_1_1xP_10_3();
		//ricorrenzaLocaleServiceImplTest.testP_1_1xP_10_e3(); 
		//ricorrenzaLocaleServiceImplTest.testP_2_1xP_6_1();		
		//ricorrenzaLocaleServiceImplTest.testP_2_1xP_8_2();
		//ricorrenzaLocaleServiceImplTest.testP_2_1xZ_8_1();
		//ricorrenzaLocaleServiceImplTest.testP_2_1xZ_8_2();
		//ricorrenzaLocaleServiceImplTest.testP_3_1xZ_5_1();
		//ricorrenzaLocaleServiceImplTest.testP_2_1xF_n_1();
		
		/**
		 * TODO
		 */
		//ricorrenzaLocaleServiceImplTest.testP_2_1xP_8_3(); // calcolare altre righe e rifare il test
		//ricorrenzaLocaleServiceImplTest.testP_2_1xP_8_e3(); // calcolare altre righe e rifare il test
		ricorrenzaLocaleServiceImplTest.testP_3_1xP_5_1();  
		//ricorrenzaLocaleServiceImplTest.testP_2_1xF_n_2(); --> non riesce
		
		logger.info("######## FINE TEST RicorrenzaLocaleServiceImplTest ########");
	}
	
	

	public void stampaRicorrenzeTrovate(){
		//stampa tutti gli schemi trovati
		Utils.stampaSchemaSalvati(ricorrenzaLocaleServiceImpl.getSavedSchemas());
		
		List<SchemaObj> candidateList = new ArrayList<SchemaObj>();
		
		//trova lo schema candidato
		int max = -1;
		
		for(SchemaObj o : ricorrenzaLocaleServiceImpl.getSavedSchemas()){
			int num = o.getCount();
			if(num>=max){
				max = num;
				candidateList.add(o);
				
			}
		}
		
		//stampa schema candidato
		System.out.println("*****************************************************************");
		System.out.println("schema candidato con "+max+" occorrenze su "+ricorrenzaLocaleServiceImpl.getSavedSchemas().size()+":");
		Utils.stampaSchemaSalvati(candidateList);
	}
	
//	public void init() throws Exception{
//		logger.info("ricorrenzaLocaleTest started..");
//		
//		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_2_1","x","p_7_1"}, 1, 3); //con n=4 cicla una sola volta nel for di RicorrenzaLocaleServiceImpl
//		IndepSetServiceImpl tester = new IndepSetServiceImpl();
//		List<HashMap<Integer,List<Integer>>> indepList = tester.forzaBruta(g);
//		
//		String fgoRsn = "-((1 + x)/(-1 + 2*x + x^2))";
//		ricorrenzaLocaleServiceImpl.findLocal(fgoRsn, indepList, tester.getLastComputedKList());
//	}
	
	public void findLocalTest(Integer[][] independentMatrix, List<Integer> kList, long[] coeffList) {
		
		ArrayUtils.reverse(coeffList);
		Utils.print(coeffList);
		
		//fine input.
		
		logger.info("Cercando ricorrenza locale..");
		
		//posso gia dire che lo schema sara composto da # di righe
		//schema = new String[righe_schema+1][];
		
		//crea matrice riempiendo di zeri per valori nulli
		//indepMatrixRicorrenza = independentMatrix;//Utils.copyMatrix(independentMatrix);
		logger.info("stampa indepMatrixRicorrenza");
		//Utils.print(indepMatrixRicorrenza);	
		
		
		
		//lista che tiene conto delle somme delle caselle per ciascun tentativo
		//List<Integer> sums = new ArrayList<Integer>();
		//
		ricorrenzaLocaleServiceImpl.setSavedSchemas(new ArrayList<SchemaObj>());
		
		for(int i=1;i<independentMatrix.length-(coeffList.length); i++){
			
			/**
			 * A partire dalla riga 1 e andando verso l'ultima, eseguo il calcolo delle combinazioni.
			 * Possiamo considerare che il calcolo delle combinazioni parta dalla prima colonna
			 * poichè nella ricerca di tutte le possibili combinazioni partiamo dall'ultima riga 
			 * dello schema. Esempio:
			 * 
			 * se lo schema è 1,2,1, l'ultimo 1 che è il coefficiente di x^2 è il primo coefficiente 
			 * che andiamo a valutare nelle possibili combinazioni. Poi passiamo al coefficiente 2 di x^1.
			 * Andiamo quindi a ritroso nello schema e arrivati all'ultimo coeff. dovremo confrontare il
			 * risultato della somma con tutte le caselle.
			 * 
			 * Per il numero delle colonne da verificare utilizzo la lista che memorizza i massimi valori di k.
			 * 
			 * NB. più grande è la lista dei coefficienti e più grande deve essere l'n calcolato della tabella
			 * degli indipendenti perchè altrimenti non vengono fatte abbastanza prove per cercare la ricorrenza.
			 * Uno dei problemi riscontrati infatti è dovuto al fatto che le tabelle degli indipendenti sono limitate
			 * al valore massimo di n pari il più delle volte a 10. Purtroppo in molti casi abbiamo denominatori
			 * della funzione generatrice con più di 4 coefficienti e con valori dei coefficienti che sono 
			 * anche maggiori di 1 (il numero di possibili combinazioni aumenta così drasticamente).
			 */

			logger.info("riga selezionata: "+i);
			
			ricorrenzaLocaleServiceImpl.calcola(i, kList, coeffList, independentMatrix);
			
		}
	}
	
	/**
	FGO : (-3 - x - 2x^2)/(-1 + x + x^3)
	schema candidato con 11 occorrenze su 13:
	Schema n.1, occorrenze: 11
	2016-01-03 20:04:55 INFO  Utils:319 - 
	 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 1 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 1 :: 0 :: 0 :: 0 :: 0
	 */
	@SuppressWarnings("unused")
	private void testP_1_1xP_10_2() {
		//input:
		Integer[][] independentMatrix = new Integer[][]
		    {	{   1,   0,   0,   0,   0,   0,   0	},
				{   1,   1,   0,   0,   0,   0,   0	},
				{   1,   2,   0,   0,   0,   0,   0	},
				{   1,   3,   0,   0,   0,   0,   0	},
				{   1,   4,   1,   0,   0,   0,   0	},
				{   1,   5,   3,   0,   0,   0,   0	},
				{   1,   6,   6,   0,   0,   0,   0	},
				{   1,   7,  10,   1,   0,   0,   0	},
				{   1,   8,  15,   4,   0,   0,   0	},
				{   1,   9,  21,  10,   0,   0,   0	},
				{   1,  10,  28,  20,   1,   0,   0	},
				{   1,  11,  36,  35,   5,   0,   0	},
				{   1,  12,  45,  56,  15,   0,   0	},
				{   1,  13,  55,  84,  35,   1,   0	},
				{   1,  14,  66, 120,  70,   6,   0	},
				{   1,  15,  78, 165, 126,  21,   0	}
			};
		//recupero i valori di k massimi
		List<Integer> kList = ricorrenzaLocaleServiceImpl.initKList(independentMatrix);
		//ottiene i coefficienti in valore assoluto
		long[] coeffList = Utils.getLongArrayFromString("{1,1,0,1}");
		findLocalTest(independentMatrix, kList, coeffList);
		if(!ricorrenzaLocaleServiceImpl.getSavedSchemas().isEmpty()){
			stampaRicorrenzeTrovate();
		}
	}
	
	/**
	FGO : (-2 - x)/(-1 + x + x^2)
	schema candidato con 9 occorrenze su 15:
	Schema n.1, occorrenze: 9
	2016-01-02 19:04:17 INFO  Utils:317 - 
	 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0
	 */
	@SuppressWarnings("unused")
	private void testP_1_1xP_10_1() {
		//input:
		Integer[][] independentMatrix = new Integer[][]
		    {	{   1,   0,   0,   0,   0,   0,   0,   0	},
				{   1,   1,   0,   0,   0,   0,   0,   0	},
				{   1,   2,   0,   0,   0,   0,   0,   0	},
				{   1,   3,   1,   0,   0,   0,   0,   0	},
				{   1,   4,   3,   0,   0,   0,   0,   0	},
				{   1,   5,   6,   1,   0,   0,   0,   0	},
				{   1,   6,  10,   4,   0,   0,   0,   0	},
				{   1,   7,  15,  10,   1,   0,   0,   0	},
				{   1,   8,  21,  20,   5,   0,   0,   0	},
				{   1,   9,  28,  35,  15,   1,   0,   0	},
				{   1,  10,  36,  56,  35,   6,   0,   0	},
				{   1,  11,  45,  84,  70,  21,   1,   0	},
				{   1,  12,  55, 120, 126,  56,   7,   0	}
			};
		
		//recupero i valori di k massimi
		List<Integer> kList = ricorrenzaLocaleServiceImpl.initKList(independentMatrix);
		//ottiene i coefficienti in valore assoluto
		long[] coeffList = Utils.getLongArrayFromString("{1,1,1}");
		findLocalTest(independentMatrix, kList, coeffList);
		if(!ricorrenzaLocaleServiceImpl.getSavedSchemas().isEmpty()){
			stampaRicorrenzeTrovate();
		}
	}
	
	/**
	FGO : (-4 - x - 2x^2 - 3x^3)/(-1 + x + x^4)
	Schema n.1, occorrenze: 10
	2016-01-03 20:06:05 INFO  Utils:319 - 
	 :: 1 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 1 :: 0 :: 0 :: 0
	 :: 0 :: 1 :: 0 :: 0 :: 0
	 */
	@SuppressWarnings("unused")
	private void testP_1_1xP_10_3() {
		//input:
		Integer[][] independentMatrix = new Integer[][]
		    {	{   1,   0,   0,   0,   0,   0	},
				{   1,   1,   0,   0,   0,   0	},
				{   1,   2,   0,   0,   0,   0	},
				{   1,   3,   0,   0,   0,   0	},
				{   1,   4,   0,   0,   0,   0	},
				{   1,   5,   1,   0,   0,   0	},
				{   1,   6,   3,   0,   0,   0	},
				{   1,   7,   6,   0,   0,   0	},
				{   1,   8,  10,   0,   0,   0	},
				{   1,   9,  15,   1,   0,   0	},
				{   1,  10,  21,   4,   0,   0	},
				{   1,  11,  28,  10,   0,   0	},
				{   1,  12,  36,  20,   0,   0	},
				{   1,  13,  45,  35,   1,   0	},
				{   1,  14,  55,  56,   5,   0	},
				{   1,  15,  66,  84,  15,   0	}
			}
;
		
		//recupero i valori di k massimi
		List<Integer> kList = ricorrenzaLocaleServiceImpl.initKList(independentMatrix);
		//ottiene i coefficienti in valore assoluto
		long[] coeffList = Utils.getLongArrayFromString("{1,1,0,0,1}");
		findLocalTest(independentMatrix, kList, coeffList);
		if(!ricorrenzaLocaleServiceImpl.getSavedSchemas().isEmpty()){
			stampaRicorrenzeTrovate();
		}
	}
	
	/**
	FGO : (-5 - 2x + x^2 - 3x^3)/(-1 + x + x^2 - x^3 + x^4)
	 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: -1 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0
	 */
	@SuppressWarnings("unused")
	private void testP_1_1xP_10_e3() {
		//input:
		Integer[][] independentMatrix = new Integer[][]
		    {	{   1,   0,   0,   0,   0,   0,   0,   0,   0,   0	},
				{   1,   1,   0,   0,   0,   0,   0,   0,   0,   0	},
				{   1,   2,   0,   0,   0,   0,   0,   0,   0,   0	},
				{   1,   3,   1,   0,   0,   0,   0,   0,   0,   0	},
				{   1,   4,   2,   0,   0,   0,   0,   0,   0,   0	},
				{   1,   5,   4,   1,   0,   0,   0,   0,   0,   0	},
				{   1,   6,   7,   2,   0,   0,   0,   0,   0,   0	},
				{   1,   7,  11,   5,   1,   0,   0,   0,   0,   0	},
				{   1,   8,  16,  10,   2,   0,   0,   0,   0,   0	},
				{   1,   9,  22,  18,   6,   1,   0,   0,   0,   0	},
				{   1,  10,  29,  30,  13,   2,   0,   0,   0,   0	},
				{   1,  11,  37,  47,  26,   7,   1,   0,   0,   0	},
				{   1,  12,  46,  70,  48,  16,   2,   0,   0,   0	},
				{   1,  13,  56, 100,  83,  35,   8,   1,   0,   0	},
				{   1,  14,  67, 138, 136,  70,  19,   2,   0,   0	},
				{   1,  15,  79, 185, 213, 131,  45,   9,   1,   0  }
			}
		;
		
		//recupero i valori di k massimi
		List<Integer> kList = ricorrenzaLocaleServiceImpl.initKList(independentMatrix);
		//ottiene i coefficienti in valore assoluto
		long[] coeffList = Utils.getLongArrayFromString("{1,1,1,-1,1}");
		findLocalTest(independentMatrix, kList, coeffList);
		if(!ricorrenzaLocaleServiceImpl.getSavedSchemas().isEmpty()){
			stampaRicorrenzeTrovate();
		}
	}

	/**
	FGO : (5 + 7x - x^2 - x^3)/(1 - 2x - 6x^2 + x^4)
	
	 */
	public void testP_3_1xP_5_1(){
		//input:
		Integer[][] independentMatrix = new Integer[][]
		       {
				{1,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0},
				{1,   3,   1,   0,   0,   0,   0,   0,   0,   0,   0},
				{1,   6,   8,   2,   0,   0,   0,   0,   0,   0,   0},
				{1,   9,  24,  22,   6,   1,   0,   0,   0,   0,   0},
				{1,  12,  49,  84,  61,  18,   2,   0,   0,   0,   0},
				{1,  15,  83, 215, 276, 174,  53,   9,   1,   0,   0},			
				{1,  18, 126, 442, 840, 880, 504, 158,  28,   2,   0}
		       };
		
		//recupero i valori di k massimi
		List<Integer> kList = ricorrenzaLocaleServiceImpl.initKList(independentMatrix);
		//ottiene i coefficienti in valore assoluto
		long[] coeffList = Utils.getLongAbsArrayFromString("{1,2,6,0,-1}");
		findLocalTest(independentMatrix, kList, coeffList);
		if(!ricorrenzaLocaleServiceImpl.getSavedSchemas().isEmpty()){
			stampaRicorrenzeTrovate();
		}
	}
	
	/**
	 FGO : (5 + x - 2x^2)/(1 - 2x - 3x^2 + 2x^3)
	 ::-1 ::-1 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 1 :: 2 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 0 :: 1 :: 1 :: 0 :: 0 :: 0
	 :: 0 :: 0 :: 0 :: 1 :: 0 :: 0 :: 0
	 */
	public void testP_3_1xZ_5_1(){
		//input:
		Integer[][] independentMatrix = new Integer[][]
		       {
				{1,   0,   0,   0,   0,   0,   0,   0},
				{1,   3,   1,   0,   0,   0,   0,   0},
				{1,   6,   4,   0,   0,   0,   0,   0},
				{1,   9,  16,   8,   1,   0,   0,   0},
				{1,  12,  37,  34,   9,   0,   0,   0},
				{1,  15,  67, 105,  65,  15,   1,   0}		
		       };
		
		//recupero i valori di k massimi
		List<Integer> kList = ricorrenzaLocaleServiceImpl.initKList(independentMatrix);
		
		//ottiene i coefficienti in valore assoluto
		//long[] coeffList = Utils.getLongAbsArrayFromString("{1,2,3,-2}");
		long[] coeffList = Utils.getLongArrayFromString("{1,2,3,-2}");
		findLocalTest(independentMatrix, kList, coeffList);
		if(!ricorrenzaLocaleServiceImpl.getSavedSchemas().isEmpty()){
			stampaRicorrenzeTrovate();
		}
	}
	
	/**
	FGO : (-7 - 6x - 7x^2 - 3x^3)/(-1 + x + x^2 + 2x^3 + x^4)
	Schema n.1, occorrenze: 3
	2016-01-01 21:55:55 INFO  Utils:274 - 
	 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 1 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 0 :: 1 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 0 :: 1 :: 0 :: 0 :: 0 :: 0
	 */
	public void testP_2_1xP_8_2(){
		//input:
		Integer[][] independentMatrix = new Integer[][]
		       {
				{1,   0,   0,   0,   0,   0,   0,   0},
				{1,   2,   0,   0,   0,   0,   0,   0},
				{1,   4,   2,   0,   0,   0,   0,   0},
				{1,   6,   6,   0,   0,   0,   0,   0},
				{1,   8,  14,   4,   0,   0,   0,   0},
				{1,  10,  26,  18,   2,   0,   0,   0},
				{1,  12,  42,  48,  14,   0,   0,   0},
				{1,  14,  62, 102,  56,   6,   0,   0},
				{1,  16,  86, 188, 162,  44,   2,   0}				
		       };
		
		//recupero i valori di k massimi
		List<Integer> kList = ricorrenzaLocaleServiceImpl.initKList(independentMatrix);
//		List<Integer> kList = new ArrayList<Integer>();
//		kList.add(0);
//		kList.add(1);
//		kList.add(2);
//		kList.add(2);
//		kList.add(3);
//		kList.add(4);
//		kList.add(4);
//		kList.add(5);
//		kList.add(6);
		
		//ottiene i coefficienti in valore assoluto
		long[] coeffList = Utils.getLongAbsArrayFromString("{1,1,1,2,1}");
		findLocalTest(independentMatrix, kList, coeffList);
		if(!ricorrenzaLocaleServiceImpl.getSavedSchemas().isEmpty()){
			stampaRicorrenzeTrovate();
		}
	}
	
	/**
	FGO : (13 + 8x + 5x^2 + 15x^3 + 3x^4 - 10x^5 - 7x^6)/(1 - x - x^2 - 2x^4 - x^5 + x^6 + x^7)
	 */
	public void testP_2_1xP_8_3(){
		//input:
		Integer[][] independentMatrix = new Integer[][]
		    {	{   1,   0,   0,   0,   0,   0,   0	},
				{   1,   2,   0,   0,   0,   0,   0	},
				{   1,   4,   2,   0,   0,   0,   0	},
				{   1,   6,   6,   0,   0,   0,   0	},
				{   1,   8,  12,   0,   0,   0,   0	},
				{   1,  10,  22,   6,   0,   0,   0	},
				{   1,  12,  36,  24,   2,   0,   0	},
				{   1,  14,  54,  60,  14,   0,   0	},
				{   1,  16,  76, 120,  50,   0,   0	},
				{   1,  18, 102, 212, 140,  12,   0	},
				{   1,  20, 132, 344, 328,  76,   2	}
			};
		
		//recupero i valori di k massimi
		List<Integer> kList = ricorrenzaLocaleServiceImpl.initKList(independentMatrix);
		//ottiene i coefficienti in valore assoluto
		long[] coeffList = Utils.getLongArrayFromString("{1,1,1,0,2,1,-1,-1}");
		findLocalTest(independentMatrix, kList, coeffList);
		if(!ricorrenzaLocaleServiceImpl.getSavedSchemas().isEmpty()){
			stampaRicorrenzeTrovate();
		}
	}
	
	/**
	FGO : (-17 - 18x - 8x^2 - 18x^3 - 10x^4 - 7x^5)/(-1 + x + 2x^2 + 2x^4 + x^5 + x^6)
	 */
	public void testP_2_1xP_8_e3(){
		//input:
		Integer[][] independentMatrix = new Integer[][]
		    {	{   1,   0,   0,   0,   0,   0,   0,   0,   0	},
				{   1,   2,   0,   0,   0,   0,   0,   0,   0	},
				{   1,   4,   2,   0,   0,   0,   0,   0,   0	},
				{   1,   6,   8,   2,   0,   0,   0,   0,   0	},
				{   1,   8,  16,   8,   2,   0,   0,   0,   0	},
				{   1,  10,  28,  26,  10,   2,   0,   0,   0	},
				{   1,  12,  44,  60,  34,  12,   2,   0,   0	},
				{   1,  14,  64, 120, 102,  46,  14,   2,   0}
			};
		
		//7|   
		
		//recupero i valori di k massimi
		List<Integer> kList = ricorrenzaLocaleServiceImpl.initKList(independentMatrix);
		//ottiene i coefficienti in valore assoluto
		long[] coeffList = Utils.getLongArrayFromString("{1,1,2,0,2,1,1}");
		findLocalTest(independentMatrix, kList, coeffList);
		if(!ricorrenzaLocaleServiceImpl.getSavedSchemas().isEmpty()){
			stampaRicorrenzeTrovate();
		}
	}
	
	/**
	 FGO : (-3 - x)/(-1 + 2x + x^2)
	 Schema n.1, occorrenze: 4
	 2016-01-01 21:39:36 INFO  Utils:274 - 
	 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 1 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0
	 */
	public void testP_2_1xP_6_1(){
		//input:
		Integer[][] independentMatrix = new Integer[][]
		       {
				{1,	  0,   0,   0,	 0,	  0,   0,  0,   0},
				{1,   2,   0,   0,	 0,	  0,   0,  0,   0},
				{1,   4,   2,   0,	 0,	  0,   0,  0,   0},
				{1,   6,   8,   2,	 0,	  0,   0,  0,   0},
				{1,   8,  18,  12,   2,	  0,   0,  0,   0},
				{1,  10,  32,  38,  16,   2,   0,  0,   0},
				{1,  12,  50,  88,  66,  20,   2,  0,   0},
				{1,  14,  72, 170, 192, 102,  24,  2,   0}
		       };
		
		//recupero i valori di k massimi
		List<Integer> kList = ricorrenzaLocaleServiceImpl.initKList(independentMatrix);
		//ottiene i coefficienti in valore assoluto
		long[] coeffList = Utils.getLongAbsArrayFromString("{1,2,1}");
		findLocalTest(independentMatrix, kList, coeffList);
		if(!ricorrenzaLocaleServiceImpl.getSavedSchemas().isEmpty()){
			stampaRicorrenzeTrovate();
		}
	}
	
	/**
	 FGO : (-3 - 2x)/(-1 + x + 2x^2)
	 Schema n.1, occorrenze: 5
	 2016-01-01 21:52:38 INFO  Utils:274 - 
	 :: 2 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 1 :: 0 :: 0 :: 0
	 :: 0 :: 1 :: 0 :: 0 :: 0
	 */
	public void testP_2_1xZ_8_1(){
		//input:
		Integer[][] independentMatrix = new Integer[][]
		       {
				{1,   0,   0,   0,   0,   0},
				{1,   2,   0,   0,   0,   0},
				{1,   4,   0,   0,   0,   0},
				{1,   6,   4,   0,   0,   0},
				{1,   8,  12,   0,   0,   0},
				{1,  10,  24,   8,   0,   0},
				{1,  12,  40,  32,   0,   0},
				{1,  14,  60,  80,  16,   0},
				{1,  16,  84, 160,  80,   0}
		       };
		
		//recupero i valori di k massimi
		List<Integer> kList = ricorrenzaLocaleServiceImpl.initKList(independentMatrix);
		
		//ottiene i coefficienti in valore assoluto
		long[] coeffList = Utils.getLongAbsArrayFromString("{1,1,2}");
		findLocalTest(independentMatrix, kList, coeffList);
		if(!ricorrenzaLocaleServiceImpl.getSavedSchemas().isEmpty()){
			stampaRicorrenzeTrovate();
		}
	}
	
	/**
	 FGO : (-5 - 4x - 3x^2)/(-1 + x + x^2 + x^3)
	 schema candidato con 6 occorrenze su 30:
	 Schema n.1, occorrenze: 6
	 2016-01-02 17:25:04 INFO  Utils:317 - 
	 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 1 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 1 :: 0 :: 0 :: 0 :: 0
	 */
	public void testP_2_1xZ_8_2(){
		//input:
		Integer[][] independentMatrix = new Integer[][]
		    {	{   1,   0,   0,   0,   0,   0,   0	},
				{   1,   2,   0,   0,   0,   0,   0	},
				{   1,   4,   0,   0,   0,   0,   0	},
				{   1,   6,   2,   0,   0,   0,   0	},
				{   1,   8,   8,   0,   0,   0,   0	},
				{   1,  10,  18,   2,   0,   0,   0	},
				{   1,  12,  32,  12,   0,   0,   0	},
				{   1,  14,  50,  38,   2,   0,   0	},
				{   1,  16,  72,  88,  16,   0,   0	},
				{   1,  18,  98, 170,  66,   2,   0	},
				{   1,  20, 128, 292, 192,  20,   0	}
			};
		
		//recupero i valori di k massimi
		List<Integer> kList = ricorrenzaLocaleServiceImpl.initKList(independentMatrix);
		
		//ottiene i coefficienti in valore assoluto
		long[] coeffList = Utils.getLongAbsArrayFromString("{1,1,1,1}");
		findLocalTest(independentMatrix, kList, coeffList);
		if(!ricorrenzaLocaleServiceImpl.getSavedSchemas().isEmpty()){
			stampaRicorrenzeTrovate();
		}
	}
	
	/**
	FGO : (-3 - 3x - x^2)/(-1 + x + 2x^2 + x^3)
	schema candidato con 6 occorrenze su 66:
	Schema n.2, occorrenze: 6
	2016-01-02 17:00:24 INFO  Utils:309 - 
	 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 2 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 0 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 0 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0
	*/
	@SuppressWarnings("unused")
	private void testP_2_1xF_n_1() {
		//input:
		Integer[][] independentMatrix = new Integer[][]
		    {	{   1,   0,   0,   0,   0,   0,   0,   0,   0	},
				{   1,   2,   0,   0,   0,   0,   0,   0,   0	},
				{   1,   4,   1,   0,   0,   0,   0,   0,   0	},
				{   1,   6,   6,   0,   0,   0,   0,   0,   0	},
				{   1,   8,  15,   4,   0,   0,   0,   0,   0	},
				{   1,  10,  28,  20,   1,   0,   0,   0,   0	},
				{   1,  12,  45,  56,  15,   0,   0,   0,   0	},
				{   1,  14,  66, 120,  70,   6,   0,   0,   0	},
				{   1,  16,  91, 220, 210,  56,   1,   0,   0	},
				{   1,  18, 120, 364, 495, 252,  28,   0,   0	},
				{   1,  20, 153, 560,1001, 792, 210,   8,   0	}
			};
		
		//recupero i valori di k massimi
		List<Integer> kList = ricorrenzaLocaleServiceImpl.initKList(independentMatrix);
		
		//ottiene i coefficienti in valore assoluto
		long[] coeffList = Utils.getLongAbsArrayFromString("{1,1,2,1}");
		findLocalTest(independentMatrix, kList, coeffList);
		if(!ricorrenzaLocaleServiceImpl.getSavedSchemas().isEmpty()){
			stampaRicorrenzeTrovate();
		}
	}
	
	/**
	FGO : (-6 - 5x - 11x^2 - 4x^3 - 3x^4)/(-1 + x + 3x^3 + x^4 + x^5)
	Schema n.38, occorrenze: 5
	2016-01-02 18:06:40 INFO  Utils:317 - 
	 :: 0 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 2 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 0 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 0 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0
	
	Schema n.39, occorrenze: 5
	2016-01-02 18:06:40 INFO  Utils:317 - 
	 :: 0 :: 0 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 0 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 0 :: 2 :: 1 :: 0 :: 0 :: 0 :: 0
	 :: 1 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0 :: 0
	 :: 0 :: 0 :: 0 :: 1 :: 0 :: 0 :: 0 :: 0
	*/
	@SuppressWarnings("unused")
	private void testP_2_1xF_n_2() {
		//input:
		Integer[][] independentMatrix = new Integer[][]
		    {	{   1,   0,   0,   0,   0,   0,   0,   0,   0	},
				{   1,   2,   0,   0,   0,   0,   0,   0,   0	},
				{   1,   4,   1,   0,   0,   0,   0,   0,   0	},
				{   1,   6,   4,   0,   0,   0,   0,   0,   0	},
				{   1,   8,  11,   2,   0,   0,   0,   0,   0	},
				{   1,  10,  22,  10,   1,   0,   0,   0,   0	},
				{   1,  12,  37,  30,   6,   0,   0,   0,   0	},
				{   1,  14,  56,  70,  26,   2,   0,   0,   0	},
				{   1,  16,  79, 138,  83,  16,   1,   0,   0	},
				{   1,  18, 106, 242, 213,  70,   8,   0,   0	},
				{   1,  20, 137, 390, 468, 232,  45,   2,   0   }
			};
		
		//recupero i valori di k massimi
		List<Integer> kList = ricorrenzaLocaleServiceImpl.initKList(independentMatrix);
		
		//ottiene i coefficienti in valore assoluto
		long[] coeffList = Utils.getLongAbsArrayFromString("{1,1,3,1,1}");
		findLocalTest(independentMatrix, kList, coeffList);
		if(!ricorrenzaLocaleServiceImpl.getSavedSchemas().isEmpty()){
			stampaRicorrenzeTrovate();
		}
	}
}
