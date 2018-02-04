package com.planning.util;

public class Error {
    
    private String error;
    
    private boolean success;
    
    public Error(boolean success, String error) {
        this.error = error;
        this.success = success;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
