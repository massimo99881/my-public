/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elezioni.service;

import it.elezioni.data.dao.PdaClienteSedeDao;
import it.elezioni.data.model.PdaClienteSede;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mgalasi
 */
@Service
public class PdaClienteSedeServiceImpl implements PdaClienteSedeService {

    @Autowired
    private PdaClienteSedeDao pdaClienteSedeDao;
      
    private static final Logger LOGGER = Logger.getLogger("PdaClienteSedeService");
   

    public PdaClienteSede selectByIdPdaAndRef(Long idPda,String ref) {

        return pdaClienteSedeDao.selectByIdPdaAndRef(idPda, ref);
    }

    
}
