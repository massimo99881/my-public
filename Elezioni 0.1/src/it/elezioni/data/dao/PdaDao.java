package it.elezioni.data.dao;


import it.elezioni.data.model.Pda;
import it.elezioni.data.model.PdaClienteSede;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author mgalasi
 */
public interface PdaDao {

    public Pda selectPdaById(Long idPda);

    public int insertPda(Pda pda);

    public int updatePdaCustomerData(Pda pda);

//    public Pda selectPdaByIdOfferversion(@Param("idOffervesion") Long idOffervesion);

}
