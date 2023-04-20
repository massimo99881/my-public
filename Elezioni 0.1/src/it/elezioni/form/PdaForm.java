package it.elezioni.form;

import it.elezioni.data.model.Pda;
import it.elezioni.data.model.PdaClienteSede;

public class PdaForm implements Cloneable {

    private PdaClienteSede pdaClienteSedeL;
    private PdaClienteSede pdaClienteSedeA;
    private Pda pda;

    //Checkbox che abilita i campi di "DATI DELLA SEDE DI ATTIVAZIONE" (se diversa da sede legale)
    private String sedeAttivazioneAltra;
    //Checkbox che abilita i campi di "DATI DEL REFERENTE AZIENDALE" (se diverso da rappres.legale o delegato)
    private String referenteAziendaleAltro;
    //Checkbox che abilita i campi di "INDIRIZZO DI SPEDIZIONE DELLA FATTURA" (se diverso da indirizzo della sede legale)
    private String indirizzoSpedizioneAltro;

    private String fromScript;
    private String token;
    private String selectedValidate;

    

    public String getIndirizzoSpedizioneAltro() {
        return indirizzoSpedizioneAltro;
    }

    public void setIndirizzoSpedizioneAltro(String indirizzoSpedizioneAltro) {
        this.indirizzoSpedizioneAltro = indirizzoSpedizioneAltro;
    }

    public Pda getPda() {
        return pda;
    }

    public void setPda(Pda pda) {
        this.pda = pda;
    }
   
    public PdaClienteSede getPdaClienteSedeA() {
        return pdaClienteSedeA;
    }

    public void setPdaClienteSedeA(PdaClienteSede pdaClienteSedeA) {
        this.pdaClienteSedeA = pdaClienteSedeA;
    }

    public PdaClienteSede getPdaClienteSedeL() {
        return pdaClienteSedeL;
    }

    public void setPdaClienteSedeL(PdaClienteSede pdaClienteSedeL) {
        this.pdaClienteSedeL = pdaClienteSedeL;
    }

    public String getReferenteAziendaleAltro() {
        return referenteAziendaleAltro;
    }

    public void setReferenteAziendaleAltro(String referenteAziendaleAltro) {
        this.referenteAziendaleAltro = referenteAziendaleAltro;
    }

    public String getSedeAttivazioneAltra() {
        return sedeAttivazioneAltra;
    }

    public void setSedeAttivazioneAltra(String sedeAttivazioneAltra) {
        this.sedeAttivazioneAltra = sedeAttivazioneAltra;
    }

    public String getFromScript() {
        return fromScript;
    }

    public void setFromScript(String fromScript) {
        this.fromScript = fromScript;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSelectedValidate() {
        return selectedValidate;
    }

    public void setSelectedValidate(String selectedValidate) {
        this.selectedValidate = selectedValidate;
    }


    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();


        return ret.toString();
    }

/* Per ora non serve
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

 */
}