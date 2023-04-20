package test.unimi.isa.indepsets;

import it.unimi.isa.service.impl.IndepOperationServiceImpl;
import it.unimi.isa.utils.Constants;
import it.unimi.isa.utils.Utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class IndepOperationServiceImplTest {
	
	private static final Logger logger = Logger.getLogger(IndepOperationServiceImplTest.class);
	
	@Autowired
	IndepOperationServiceImpl indepOperationServiceImpl;

	public static void main(final String[] args) throws Exception {		
		/**
		 * Esegue l'operazione desiderata tra gli indipendenti di indip1 e indip2
		 */
		logger.info("######## INIZIO TEST IndepOperationServiceImplTest ########");
		final ApplicationContext context = new ClassPathXmlApplicationContext(Constants.CONFIG_PATH);
		final IndepOperationServiceImplTest indepOperationServiceImplTest = context.getBean(IndepOperationServiceImplTest.class);
		
		indepOperationServiceImplTest.executeDifferentTest();
		
		logger.info("######## FINE TEST IndepOperationServiceImplTest ########");
	}
	
	private void executeDifferentTest() throws Exception {
		//remove load cache
		Constants.ENABLE_CACHE_LOAD = false;
		Constants.ENABLE_CACHE_SAVE = false;
		
		String OPERATOR = "-";
		
		Integer[][] firstIndep = indepOperationServiceImpl.createIndependentTable(new String[]{"indep","p_2_1","x","p_6_1"});
		Integer[][] secondIndep = indepOperationServiceImpl.createIndependentTable(new String[]{"indep","p_2_1","x","c_6_1"});
		//restore load cache 
		Constants.ENABLE_CACHE_LOAD = true;
		Constants.ENABLE_CACHE_SAVE = true;
		
		//result
		System.out.println("#######################   INIZIO STAMPA RISULTATI   ##################");
		System.out.println("#######################   p_2_1 x p_6_1   ##################");
		Utils.print(firstIndep);
		System.out.println("#######################   p_2_1 x c_6_1   ##################");
		Utils.print(secondIndep);
		System.out.println("#######################   p_2_1 x p_6_1 "+OPERATOR+" p_2_1 x c_6_1   ##################");
		indepOperationServiceImpl.execute(firstIndep,secondIndep,OPERATOR);
		//Integer[][] result = execute(firstIndep,secondIndep,"-");
	}
}
