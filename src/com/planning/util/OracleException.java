package com.planning.util;

public class OracleException extends Exception {
    
    private String mensaje;
    
    public OracleException(String message) {
        super(message);
        this.mensaje = message;
    }
    
    public OracleException(Throwable cause) {
        super(cause);
    }
    
    public String getMensaje() {
        mensaje = super.getLocalizedMessage().isEmpty() ? super.getMessage() : super.getLocalizedMessage();
        if (mensaje.contains("12519")) {
            mensaje = "No se ha podido conectar al almacenamiento de la aplicación. Si el problema persiste por favor contacte con el administrador del sistema.";
        } else {
            mensaje = "No se ha podido conectar al almacenamiento de la aplicación. Si el problema persiste por favor contacte con el administrador del sistema.";
        }
        return mensaje;
    }
}
