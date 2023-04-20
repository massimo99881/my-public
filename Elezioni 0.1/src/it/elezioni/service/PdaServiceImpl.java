/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elezioni.service;


import it.elezioni.data.dao.PdaDao;
import it.elezioni.data.dao.PdaClienteSedeDao;
import it.elezioni.data.model.Pda;
import it.elezioni.data.model.PdaClienteSede;
import it.elezioni.service.common.AbstractService;
import it.elezioni.util.Constants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author mgalasi
 */
@Service
public class PdaServiceImpl /*extends AbstractService*/ implements PdaService {

    @Autowired
    private PdaDao pdaDao;
    
    @Autowired
    private PdaClienteSedeDao clienteSedeDao;
    
    private static final Logger LOGGER = Logger.getLogger(PdaServiceImpl.class);
   
    public Pda getPdaById(Long idPda) {

        return pdaDao.selectPdaById(idPda);

    }

    @Transactional(value = "txConfigManager")
    public Long insertPdaCustomerData(Pda pda, List<PdaClienteSede> pdaClienteSedeList) throws Exception {


        // Inserisce il record in pda ottenendo l'id
        pdaDao.insertPda(pda);
        Long idPda = pda.getIdPda();

        // Inserisce i record in pda_cliente_sede settando l'id pda
        Iterator<PdaClienteSede> pdaClienteSedeIter = pdaClienteSedeList.iterator();
        while (pdaClienteSedeIter.hasNext()) {
            PdaClienteSede pdaClienteSede = pdaClienteSedeIter.next();
            pdaClienteSede.setIdPda(idPda);
            clienteSedeDao.insertPdaClienteSede(pdaClienteSede);
        }
        
        return idPda;

    }

    @Transactional(value = "txConfigManager")
    public void updatePdaCustomerData(Pda pda, List<PdaClienteSede> pdaClienteSedeList) throws Exception {

        Long idPda = pda.getIdPda();
        //Cancella prima tutti i record di pda_cliente_sedere riferiti alla pda        
        clienteSedeDao.deleteByIdPda(idPda);

        // Inserisce i record in pda_cliente_sede settando l'id pda
        Iterator<PdaClienteSede> pdaClienteSedeIter = pdaClienteSedeList.iterator();
        while (pdaClienteSedeIter.hasNext()) {
            PdaClienteSede pdaClienteSede = pdaClienteSedeIter.next();
            pdaClienteSede.setIdPda(idPda);
            clienteSedeDao.insertPdaClienteSede(pdaClienteSede);
        }

        pdaDao.updatePdaCustomerData(pda);
    }

    public void setStatusBackOffice(String token, Boolean okko, Long idPda) throws Exception {
        String status = "";
        String note = "";
        if (okko) {//se arriva ok
            LOGGER.debug("setStatusBackOffice: stato ok");
            status = Constants.PDA_SCRIPT;
            note = "Pda Completata da Script";
        } else {//se arriva ko
            LOGGER.debug("setStatusBackOffice: stato ko");
            status = Constants.PDA_KO_SCRIPT;
            note = "Pda Scartata da Script";
        }
        Pda pdaEx = pdaDao.selectPdaById(idPda);
       

    }

     
    public Boolean isValidPda(Long idPda) {
        try{
            return true;
        }
        catch(Exception e){
            LOGGER.error("isValidPda fallita", e);
            return false;
        }
    }

}
