/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.elezioni.data.dao;

import it.elezioni.data.model.PdaClienteSede;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author mgalasi
 */
public interface PdaClienteSedeDao {

    public PdaClienteSede selectSitePDA(@Param("ref") String ref);

    public PdaClienteSede selectByIdPdaAndRef(@Param("idPda") Long idPda,@Param("ref") String ref);

    public int insertPdaClienteSede(PdaClienteSede pdaClienteSede);

    public int deleteByIdPda(Long idPda);
}
