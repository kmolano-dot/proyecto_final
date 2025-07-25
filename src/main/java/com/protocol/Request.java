package com.protocol;

import java.io.Serializable;

/**
 * Request object that will be sent from client to server
 */
public class Request implements Serializable {
    private RequestType type;
    private Object data;
    
    public Request() {
    }
    
    public Request(RequestType type, Object data) {
        this.type = type;
        this.data = data;
    }
    
    public RequestType getType() {
        return type;
    }
    
    public void setType(RequestType type) {
        this.type = type;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
    
    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }
}