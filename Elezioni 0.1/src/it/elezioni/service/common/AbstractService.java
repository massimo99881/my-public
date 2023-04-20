/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elezioni.service.common;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;

/**
 *
 * @author jdinardo
 */
public abstract class AbstractService {

    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    protected PropertiesFactoryBean propertiesFactoryBean;


    protected interface ActivitiesRole{
        public static String ACTIVITY_ECONOMIC_VALUE = "CUSTOM_CONFPROD_ECONOMIC_VALUE_ROLE";
        public static String ACTIVITY_VIEWALL_VALUE = "CUSTOM_CONFPROD_VIEWALL_ROLE";
    }
     

    protected String getWebservicesEasycimHost() {
        Properties custProp = this.getProperties();
        return custProp.getProperty("webservices.easycim3.host", "localhost");

    }

    protected String getWebservicesEasycimPort() {
        Properties custProp = this.getProperties();
        return custProp.getProperty("webservices.easycim3.port", "8080");

    }

    protected Properties getProperties() {
        try {
            Properties properties = propertiesFactoryBean.getObject();
            return properties;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

 
    
}
