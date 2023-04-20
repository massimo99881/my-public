/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.elezioni.service;

import it.elezioni.data.model.PdaClienteSede;

/**
 *
 * @author mgalasi
 */
public interface PdaClienteSedeService {

    public PdaClienteSede selectByIdPdaAndRef(Long idPda,String ref);
 

}
