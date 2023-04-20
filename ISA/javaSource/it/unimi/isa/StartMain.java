
package it.unimi.isa;

import it.unimi.isa.utils.Constants;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * CLASSE MAIN
 * @author massimo galasi
 * @matricola 747112
 */
@Component
public class StartMain {

	private static final Logger logger = Logger.getLogger(StartMain.class);

	public static void main(final String[] args) {
		final ApplicationContext context = new ClassPathXmlApplicationContext(Constants.CONFIG_PATH);
		context.getBean(StartMain.class);
		logger.info("########Program started########");
	}
}
