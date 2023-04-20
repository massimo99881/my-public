/*
 * Copyright 2010-2011 Nabeel Mukhtar 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 * 
 */
package test.unimi.isa.oeis;



import java.util.List;

import org.apache.log4j.Logger;
import org.oeis.api.schema.IntegerSequence;
import org.oeis.api.services.IntegerSequenceQuery;
import org.oeis.api.services.OeisQueryFactory;


/**
 * The Class IntegerSequenceQuerySample.
 */
public class IntegerSequenceQuerySample {
	
	private static final Logger logger = Logger.getLogger(IntegerSequenceQuerySample.class);

    /**
     * The main method.
     * 
     * @param args the arguments
     * 
     * @throws Exception the exception
     */
	public static void main(String[] args) throws Exception {
		System.out.println("######## INIZIO TEST IntegerSequenceQuerySample ########");
		OeisQueryFactory factory = OeisQueryFactory.newInstance();
		IntegerSequenceQuery service = factory.createIntegerSequenceQuery();
		//List<IntegerSequence> sequences = service.withOrderedTerms(1,1,2,3,5,8).list();
		
		long array[] = new long[]{3, 7, 17, 41, 99, 239, 577, 1393, 3363, 8119};
		List<IntegerSequence> sequences = service.withOrderedTerms(array).list();
		for (IntegerSequence sequence : sequences) {
			printResult(sequence);
		}
//		InputStream is = null;
//		try {
//			SequenceMusicQuery music = factory.createSequenceMusicQuery();
//			is = music.withSequenceId("A000142").singleResult();
//			playMusic(is);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			is.close();
//		}
		System.out.println("######## FINE TEST IntegerSequenceQuerySample ########");
	}

	/**
	 * Prints the result.
	 * 
	 * @param sequence the sequence
	 */
	private static void printResult(IntegerSequence sequence) {
		System.out.println(sequence.getCatalogNumber() + ":" + sequence.getIdentification() + ":" + sequence.getName());
		logger.info(sequence.getCatalogNumber() + ":" + sequence.getIdentification() + ":" + sequence.getName());
	}
	
//	/**
//	 * Play music.
//	 * 
//	 * @param is the is
//	 * 
//	 * @throws Exception the exception
//	 */
//	private static void playMusic(InputStream is) throws Exception {
//        // From file
//        Sequence sequence = MidiSystem.getSequence(is);
//    
//        // Create a sequencer for the sequence
//        Sequencer sequencer = MidiSystem.getSequencer();
//        sequencer.open();
//        sequencer.setSequence(sequence);
//    
//        // Start playing
//        sequencer.start();
//	}
}
