package it.unimi.isa.singleton;


import it.unimi.isa.utils.Constants;
import it.unimi.isa.utils.ISAConsole;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wolfram.jlink.KernelLink;
import com.wolfram.jlink.MathLinkException;
import com.wolfram.jlink.MathLinkFactory;

/**
 * Usare questa classe per MathKernel
 * @author Administrator
 *
 */
@Component
public class Singleton {
	
	private static final Logger logger = Logger.getLogger(Singleton.class);
	
	private String temp;
	public String temp2;
	
	private KernelLink kernelLink = null;
	
	@Autowired
	ISAConsole isaConsole;
	
	private Singleton(){
		
	}
	
	@PostConstruct
	public void init(){
		temp = "stringa1";
		temp2 = "stringa2";
		logger.info("singleton init");
		
		
        try {
            System.setProperty("com.wolfram.jlink.libdir", Constants.JLINKDIR);
            kernelLink = MathLinkFactory.createKernelLink(Constants.EXEC_KERNEL_LINK);
            kernelLink.discardAnswer();
        } catch (MathLinkException e) {
            logger.error("MathLinkException occurred: " + e.getMessage());
        } catch (Exception e) {
			e.printStackTrace();
		}/* finally {
			kernelLink.close();
        }*/
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getTemp2() {
		return temp2;
	}

	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}

	public KernelLink getKernelLink() {
		return kernelLink;
	}

	public ISAConsole getIsaConsole() {
		return isaConsole;
	}

	public void setIsaConsole(ISAConsole isaConsole) {
		this.isaConsole = isaConsole;
	}
	
	
}
