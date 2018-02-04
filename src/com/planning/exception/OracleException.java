package com.planning.exception;

import com.planning.util.Error;

public class OracleException extends Exception {
    
    protected String mensaje;
    
    protected boolean debug = false;
    
    protected Error error;
    
    public OracleException(String message) {
        super(message);
        this.mensaje = message;
    }
    
    public OracleException(Throwable cause) {
        super(cause);
        getMensaje();
    }
    
    public String getMensaje() {
        mensaje = super.getLocalizedMessage().isEmpty() ? super.getMessage() : super.getLocalizedMessage();
        if (mensaje.contains("12519")) {
            mensaje = "No se ha podido conectar al almacenamiento de la aplicación. Si el problema persiste por favor contacte con el administrador del sistema.";
        } else if (mensaje.contains("SYS_C0012415")) {
            mensaje = "La secuencia de cargos no está correctamente configurada.";
        }
        return mensaje;
    }
    
    public void tratarMensaje() {
    
    }
    
    public Error getError() {
        error = new Error(false, mensaje);
        return error;
    }
}
