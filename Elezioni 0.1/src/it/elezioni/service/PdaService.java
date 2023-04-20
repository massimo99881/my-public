/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.elezioni.service;

import it.elezioni.data.model.Pda;
import it.elezioni.data.model.PdaClienteSede;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author mgalasi
 */
public interface PdaService {

    public Pda getPdaById(Long idPda);

    public Long insertPdaCustomerData(Pda pda, List<PdaClienteSede> pdaClienteSedeList) throws Exception;

    public void updatePdaCustomerData(Pda pda, List<PdaClienteSede> pdaClienteSedeList) throws Exception;

    public void setStatusBackOffice(String token, Boolean okko, Long idPda) throws Exception;

    public Boolean isValidPda(Long idPda);

    
   
}
