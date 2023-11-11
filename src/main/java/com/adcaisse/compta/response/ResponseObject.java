package com.adcaisse.compta.response;

/**
 * Created by Hassine on 08/02/2020.
 */
public class ResponseObject {
    private int result;
    private String errorDescription;
    private Object objectResponse;

    public ResponseObject() {
    }

    public ResponseObject(int result, String errorDescription, Object objectResponse) {
        this.result = result;
        this.errorDescription = errorDescription;
        this.objectResponse = objectResponse;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public Object getObjectResponse() {
        return objectResponse;
    }

    public void setObjectResponse(Object objectResponse) {
        this.objectResponse = objectResponse;
    }
}
