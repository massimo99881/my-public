package test.unimi.isa.indepsets;

import static org.junit.Assert.assertTrue;
import it.unimi.isa.model.graphs.Graph;
import it.unimi.isa.service.impl.IndepSetServiceImpl;
import it.unimi.isa.utils.Utils;

import org.junit.Test;
import org.springframework.stereotype.Component;


/**
 * Classe di Test per verificare i valori degli indipendenti
 * sulle famiglie di grafi (e circuiti) utilizzando l'algorimto forza bruta.
 * NOTA: per eseguire un solo test, evidenziare il metodo e fare click su 
 * esegui come test junit.
 * @author Administrator
 *
 */
@Component
public class BruteForceJUnitTest {
	
	@Test
	public void test_p_2_1_x_p_7_1() throws Exception {
		
		System.out.println("indep p_2_1 x p_6_1");
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_2_1","x","p_7_1"}, 1, 3);
		IndepSetServiceImpl tester = new IndepSetServiceImpl();
		tester.forzaBruta(g);
		
		String expected = "\n" +
		"0|   1\n" +
		"1|   1   2\n" +
		"2|   1   4   2\n" +
		"3|   1   6   8   2\n" +
		"4|   1   8  18  12   2\n" +
		"5|   1  10  32  38  16   2\n" +
		"6|   1  12  50  88  66  20   2\n" +
		"7|   1  14  72 170 192 102  24   2\n";
		
		System.out.println(Utils.stampaMatrice(tester.getIndependentMatrix()));
		assertTrue(expected.equals(Utils.stampaMatrice(tester.getIndependentMatrix())));
	}
	
	/**
	 * G_n,k 
	 * Input.n = 6;
	 * Input.m = 1;		
	 * @throws Exception 
	 */
	@Test
	public void test_p_1_1_x_p_10_1() throws Exception {
		System.out.println("indep p_1_1 x p_10_1");
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_1_1","x","p_10_1"}, 1, 3);
		IndepSetServiceImpl tester = new IndepSetServiceImpl();
		tester.forzaBruta(g);
		
		String expected = "\n" +
		"0|   1\n"+
		"1|   1   1\n"+
		"2|   1   2\n"+
		"3|   1   3   1\n"+
		"4|   1   4   3\n"+
		"5|   1   5   6   1\n"+
		"6|   1   6  10   4\n"+
		"7|   1   7  15  10   1\n"+
		"8|   1   8  21  20   5\n"+
		"9|   1   9  28  35  15   1\n"+
		"10|   1  10  36  56  35   6\n"+
		"11|   1  11  45  84  70  21   1\n";
		
		System.out.println(Utils.stampaMatrice(tester.getIndependentMatrix()));
		assertTrue(expected.equals(Utils.stampaMatrice(tester.getIndependentMatrix())));
	}
	
	@Test
	public void test_p_2_1_x_p_7_2() throws Exception {
		System.out.println("indep p_2_1 x p_7_2");
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_2_1","x","p_7_2"}, 1, 3);
		IndepSetServiceImpl tester = new IndepSetServiceImpl();
		tester.forzaBruta(g);
		
		String expected = "\n" +
		"0|   1\n"+
		"1|   1   2\n"+
		"2|   1   4   2\n"+
		"3|   1   6   6\n"+
		"4|   1   8  14   4\n"+
		"5|   1  10  26  18   2\n"+
		"6|   1  12  42  48  14\n"+
		"7|   1  14  62 102  56   6\n";
		
		System.out.println(Utils.stampaMatrice(tester.getIndependentMatrix()));
		assertTrue(expected.equals(Utils.stampaMatrice(tester.getIndependentMatrix())));
	}
	
	@Test
	public void test_p_1_1_x_p_10_2() throws Exception {
		System.out.println("indep p_1_1 x p_10_2");
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_1_1","x","p_10_2"}, 1, 3);
		IndepSetServiceImpl tester = new IndepSetServiceImpl();
		tester.forzaBruta(g);

		String expected = "\n" +
		"0|   1\n" +
		"1|   1   1\n" +
		"2|   1   2\n" +
		"3|   1   3\n" +
		"4|   1   4   1\n" +
		"5|   1   5   3\n"+
		"6|   1   6   6\n"+
		"7|   1   7  10   1\n"+
		"8|   1   8  15   4\n"+
		"9|   1   9  21  10\n"+
		"10|   1  10  28  20   1\n"+
		"11|   1  11  36  35   5\n";
		
		System.out.println(Utils.stampaMatrice(tester.getIndependentMatrix()));
		assertTrue(expected.equals(Utils.stampaMatrice(tester.getIndependentMatrix())));
	}
	
	@Test
	public void test_p_3_1_x_p_4_2() throws Exception {
		System.out.println("indep p_3_1 x p_4_2");
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_3_1","x","p_4_2"}, 1, 3);
		IndepSetServiceImpl tester = new IndepSetServiceImpl();
		tester.forzaBruta(g);
		
		String expected = "\n" +
		"0|   1\n" +
		"1|   1   3   1\n" +
		"2|   1   6   8   2\n" +
		"3|   1   9  21  12\n" +
		"4|   1  12  43  52  17   2\n" ;
//		"5|   1  15  74 146 112  31   2\n" ;
		
		System.out.println(Utils.stampaMatrice(tester.getIndependentMatrix()));
		assertTrue(expected.equals(Utils.stampaMatrice(tester.getIndependentMatrix())));
	}
	
	@Test
	public void test_p_3_2_x_p_4_1() throws Exception {
		System.out.println("indep p_3_2 x p_4_1");
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_3_2","x","p_4_1"}, 1, 3);
		IndepSetServiceImpl tester = new IndepSetServiceImpl();
		tester.forzaBruta(g);

		String expected = "\n" +
		"0|   1\n" +
		"1|   1   3\n" +
		"2|   1   6   6\n" +
		"3|   1   9  21  12\n" +
		"4|   1  12  45  60  24\n" ;
		//"5|   1  15  78 171 156  48\n" ;
		
		System.out.println(Utils.stampaMatrice(tester.getIndependentMatrix()));
		assertTrue(expected.equals(Utils.stampaMatrice(tester.getIndependentMatrix())));
	}
	
	@Test
	public void test_p_3_2_x_p_6_2() throws Exception {
		System.out.println("indep p_3_2 x p_6_2");
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_3_2","x","p_6_2"}, 1, 3);
		IndepSetServiceImpl tester = new IndepSetServiceImpl();
		tester.forzaBruta(g);

		String expected = "\n" +
		"0|   1\n" +
		"1|   1   3\n" +
		"2|   1   6   6\n" +
		"3|   1   9  18   6\n" +
		"4|   1  12  39  36   6\n" +
		"5|   1  15  69 114  60   6\n" +
		"6|   1  18 108 264 258  84   6\n";
		
		System.out.println(Utils.stampaMatrice(tester.getIndependentMatrix()));
		assertTrue(expected.equals(Utils.stampaMatrice(tester.getIndependentMatrix())));
	}
	
	@Test
	public void test_p_4_3_x_p_4_1() throws Exception {
		System.out.println("indep p_4_3 x p_4_1");
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_4_3","x","p_4_1"}, 1, 3);
		IndepSetServiceImpl tester = new IndepSetServiceImpl();
		tester.forzaBruta(g);

		String expected = "\n" +
		"0|   1\n" +
		"1|   1   4\n" +
		"2|   1   8  12\n" +
		"3|   1  12  40  36\n" +
		"4|   1  16  84 168 108\n" ;
		//"5|   1  20 144 460 648 324\n" ;
		
		System.out.println(Utils.stampaMatrice(tester.getIndependentMatrix()));
		assertTrue(expected.equals(Utils.stampaMatrice(tester.getIndependentMatrix())));
	}
	
	@Test
	public void test_p_2_1_x_z_7_1() throws Exception {
		System.out.println("indep p_2_1 x z_7_1");
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_2_1","x","z_7_1"}, 1, 3);
		IndepSetServiceImpl tester = new IndepSetServiceImpl();
		tester.forzaBruta(g);
		
		String expected = "\n" +
		"0|   1\n" +
		"1|   1   2\n" +
		"2|   1   4\n" +
		"3|   1   6   4\n" +
		"4|   1   8  12\n" +
		"5|   1  10  24   8\n"+
		"6|   1  12  40  32\n"+
		"7|   1  14  60  80  16\n";
		
		System.out.println(Utils.stampaMatrice(tester.getIndependentMatrix()));
		assertTrue(expected.equals(Utils.stampaMatrice(tester.getIndependentMatrix())));
	}
	
	
	@Test
	public void test_p_2_1_x_f_7_1() throws Exception {
		System.out.println("indep p_2_1 x f_7_1");
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_2_1","x","f_7_1"}, 1, 3);
		IndepSetServiceImpl tester = new IndepSetServiceImpl();
		tester.forzaBruta(g);
		
		String expected = "\n" +
		"0|   1\n" +
		"1|   1   2\n" +
		"2|   1   4   1\n" +
		"3|   1   6   6\n" +
		"4|   1   8  15   4\n" +
		"5|   1  10  28  20   1\n"+
		"6|   1  12  45  56  15\n" +
		"7|   1  14  66 120  70   6\n";
		
		System.out.println(Utils.stampaMatrice(tester.getIndependentMatrix()));
		assertTrue(expected.equals(Utils.stampaMatrice(tester.getIndependentMatrix())));
	}
	
	@Test
	public void test_p_3_1_x_f_4_1() throws Exception {
		System.out.println("indep p_3_1 x f_4_1");
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_3_1","x","f_4_1"}, 1, 3);
		IndepSetServiceImpl tester = new IndepSetServiceImpl();
		tester.forzaBruta(g);
		
		String expected = "\n" +
		"0|   1\n" +
		"1|   1   3   1\n" +
		"2|   1   6   6\n" +
		"3|   1   9  20  11   1\n" +
		"4|   1  12  43  52  18\n" +
		"5|   1  15  75 150 117  28   1\n" ;
		
		System.out.println(Utils.stampaMatrice(tester.getIndependentMatrix()));
		assertTrue(expected.equals(Utils.stampaMatrice(tester.getIndependentMatrix())));
	}
	
	@Test
	public void test_p_2_1_x_cf_7_1() throws Exception {
		System.out.println("indep p_2_1 x cf_7_1");
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_2_1","x","cf_7_1"}, 1, 3);
		IndepSetServiceImpl tester = new IndepSetServiceImpl();
		tester.forzaBruta(g);
		
		String expected = "\n" +
		"0|   1\n"+
		"1|   1   2\n"+
		"2|   1   4   1\n"+
		"3|   1   6   4\n"+
		"4|   1   8  13   2\n"+
		"5|   1  10  26  14   1\n"+
		"6|   1  12  43  46   9\n"+
		"7|   1  14  64 106  50   4\n";

		System.out.println(Utils.stampaMatrice(tester.getIndependentMatrix()));
		assertTrue(expected.equals(Utils.stampaMatrice(tester.getIndependentMatrix())));
	}
	
	@Test
	public void test_p_2_1_x_c_7_1() throws Exception {
		System.out.println("indep p_2_1 x c_7_1");
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_2_1","x","c_7_1"}, 1, 3);
		IndepSetServiceImpl tester = new IndepSetServiceImpl();
		tester.forzaBruta(g);

		String expected = "\n" +
		"0|   1\n" +
		"1|   1   2\n" +
		"2|   1   4   2\n" +
		"3|   1   6   6\n" +
		"4|   1   8  16   8   2\n" +
		"5|   1  10  30  30  10\n"+
		"6|   1  12  48  76  48  12   2\n"+
		"7|   1  14  70 154 154  70  14\n";
		
		System.out.println(Utils.stampaMatrice(tester.getIndependentMatrix()));
		assertTrue(expected.equals(Utils.stampaMatrice(tester.getIndependentMatrix())));
	}
	
	@Test
	public void test_p_2_1_x_cz_7_1() throws Exception {
		System.out.println("indep p_2_1 x cz_7_1");
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_2_1","x","cz_7_1"}, 1, 3);
		IndepSetServiceImpl tester = new IndepSetServiceImpl();
		tester.forzaBruta(g);

		String expected = "\n" +
		"0|   1\n" +
		"1|   1   2\n" +
		"2|   1   4\n" +
		"3|   1   6   2\n" +
		"4|   1   8  10\n" +
		"5|   1  10  22   4\n" +
		"6|   1  12  38  24\n" +
		"7|   1  14  58  68   8\n";
		
		System.out.println(Utils.stampaMatrice(tester.getIndependentMatrix()));
		assertTrue(expected.equals(Utils.stampaMatrice(tester.getIndependentMatrix())));
	}
	
	@Test
	public void test_p_2_1_x_z_7_2() throws Exception {
		System.out.println("indep p_2_1 x z_7_2");
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_2_1","x","z_7_2"}, 1, 3);
		IndepSetServiceImpl tester = new IndepSetServiceImpl();
		tester.forzaBruta(g);
		
		String expected = "\n" +
		"0|   1\n" +
		"1|   1   2\n" +
		"2|   1   4\n" +
		"3|   1   6   2\n" +
		"4|   1   8   8\n" +
		"5|   1  10  18   2\n" +
		"6|   1  12  32  12\n" +
		"7|   1  14  50  38   2\n";

		System.out.println(Utils.stampaMatrice(tester.getIndependentMatrix()));
		assertTrue(expected.equals(Utils.stampaMatrice(tester.getIndependentMatrix())));
	}
	
	@Test
	public void test_p_3_2_x_cz_4_2() throws Exception {
		System.out.println("indep p_3_2 x cz_4_2");
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_3_2","x","cz_4_2"}, 1, 3);
		IndepSetServiceImpl tester = new IndepSetServiceImpl();
		tester.forzaBruta(g);
		
		String expected = "\n" +
		"0|   1\n" +
		"1|   1   3\n" +
		"2|   1   6   2\n" +
		"3|   1   9  10\n" +
		"4|   1  12  25   4\n";

		System.out.println(Utils.stampaMatrice(tester.getIndependentMatrix()));
		assertTrue(expected.equals(Utils.stampaMatrice(tester.getIndependentMatrix())));
	}
	
	@Test
	public void test_p_1_1_x_c_11_1() throws Exception {
		System.out.println("indep p_1_1 x c_11_1");
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_1_1","x","c_11_1"}, 1, 3);
		IndepSetServiceImpl tester = new IndepSetServiceImpl();
		tester.forzaBruta(g);
		
		String expected = "\n" +
		"0|   1\n" +
		"1|   1   1\n" +
		"2|   1   2\n" +
		"3|   1   3\n" +
		"4|   1   4   2\n" +
		"5|   1   5   5\n"+
		"6|   1   6   9   2\n"+
		"7|   1   7  14   7\n"+
		"8|   1   8  20  16   2\n"+
		"9|   1   9  27  30   9\n"+
		"10|   1  10  35  50  25   2\n"+
		"11|   1  11  44  77  55  11\n";
		
		System.out.println(Utils.stampaMatrice(tester.getIndependentMatrix()));
		assertTrue(expected.equals(Utils.stampaMatrice(tester.getIndependentMatrix())));
	}
	
	@Test
	public void test_p_2_1_x_c_7_3() throws Exception {
		System.out.println("indep p_2_1 x c_7_3");
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_2_1","x","c_7_3"}, 1, 3);
		IndepSetServiceImpl tester = new IndepSetServiceImpl();
		tester.forzaBruta(g);

		String expected = "\n"+
		"0|   1\n"+
		"1|   1   2\n"+
		"2|   1   4   2\n"+
		"3|   1   6   6\n"+
		"4|   1   8  12\n"+
		"5|   1  10  20\n"+
		"6|   1  12  30\n"+
		"7|   1  14  42\n";
		
		System.out.println(Utils.stampaMatrice(tester.getIndependentMatrix()));
		assertTrue(expected.equals(Utils.stampaMatrice(tester.getIndependentMatrix())));
	}
	
	@Test
	public void test_p_2_1_x_m_7_1() throws Exception {
		System.out.println("indep p_2_1 x m_7_1");
		Graph g = Utils.ottieneGrafo(new String[]{"indep","p_2_1","x","m_7_1"}, 1, 3);
		IndepSetServiceImpl tester = new IndepSetServiceImpl();
		tester.forzaBruta(g);
		
		String expected = "\n" +
		"0|   1\n" +
		"1|   1   2\n" +
		"2|   1   4\n" +
		"3|   1   6   6   2\n" +
		"4|   1   8  16   8\n" +
		"5|   1  10  30  30  10   2\n"+
		"6|   1  12  48  76  48  12\n"+
		"7|   1  14  70 154 154  70  14   2\n";
		
		System.out.println(Utils.stampaMatrice(tester.getIndependentMatrix()));
		assertTrue(expected.equals(Utils.stampaMatrice(tester.getIndependentMatrix())));
	}
	
}


