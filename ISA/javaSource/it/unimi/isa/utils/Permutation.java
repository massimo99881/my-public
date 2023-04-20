package it.unimi.isa.utils;





import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Permutation {
	
//	private static final Logger logger = Logger.getLogger(Permutation.class);
	
	static List<HashMap <Integer,List<Integer>>> bacchetteList ;
	static int j;

	public static void printPermutations(int[] n, int[] Nr, int idx) {
	    if (idx == n.length) {  //stop condition for the recursion [base clause]
	    	
	    	HashMap <Integer,List<Integer>> hash = new HashMap<Integer,List<Integer>>();
			List<Integer> list = new ArrayList<Integer>();
			boolean uniConsec = false;
			for(int i=0;i<n.length;i++){
				list.add(n[i]);
				if(n[i]==1 && i<n.length-1 && n[i+1]==1 ){
					uniConsec = true;
				}
			}
			if(!uniConsec){
				hash.put(j++, list);
				bacchetteList.add(hash);
			}
				
	    	
	        //logger.info(Arrays.toString(n));
	        return;
	    }
	    for (int i = 0; i <= Nr[idx]; i++) { 
	        n[idx] = i;
	        printPermutations(n, Nr, idx+1); //recursive invokation, for next elements
	    }
	}
	
	public static void main(String[] args) {
	    /* let n.length == 3 and Nr[0] = 2, Nr[1] = 3, Nr[2] = 3 */
//		int m = 3;
//		
//		int r = 2;
//		for(int i=1;i<m;i++){
//			r *= 2;
//		}
		bacchetteList = new ArrayList<HashMap<Integer,List<Integer>>>();
		j=0;

	    int[] n = new int[3];
	    int Nr[] = {1,1,1};
	    printPermutations(n, Nr, 0);
	    
	    Utils.stampaMatrice(bacchetteList);
	}
}
