package it.elezioni.util;

/**
 *
 * @author gcolciago
 */


public class GenericError {
    private String errorType;
    public String errorMessage;


   // public GenericError(){super();};
    
    public GenericError(String msg, String errType) {
        this.errorMessage = msg;
        this.errorType = errType;
    }
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String Msg) {
        this.errorMessage = Msg;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String toString(){
        return errorMessage;
    }
}
