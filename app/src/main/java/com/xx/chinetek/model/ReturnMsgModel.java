package com.xx.chinetek.model;

/**
 * Created by GHOST on 2016/12/15.
 */

public class ReturnMsgModel<T> {

    public String HeaderStatus;

    public String Message;

    public T ModelJson;


    public String getHeaderStatus() {
        return HeaderStatus;
    }

    public void setHeaderStatus(String headerStatus) {
        HeaderStatus = headerStatus;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public T getModelJson() {
        return ModelJson;
    }

    public void setModelJson(T modelJson) {
        ModelJson = modelJson;
    }
}
