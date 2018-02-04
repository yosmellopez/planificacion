package com.planning.exception;

public class PlTaskException extends OracleException {
    
    public PlTaskException(String message) {
        super(message);
        tratarMensaje();
    }
    
    public PlTaskException(Throwable cause) {
        super(cause);
        tratarMensaje();
    }
    
    public void tratarMensaje() {
        mensaje = super.getLocalizedMessage().isEmpty() ? super.getMessage() : super.getLocalizedMessage();
        if (mensaje.contains("SYS_C0012412")) {
            mensaje = "La secuencia de tareas no está correctamente configurada.";
        } else if (mensaje.contains("SYS_C009985")) {
            mensaje = "La secuencia de tareas del plan no está correctamente configurada.";
        } else if (mensaje.contains("12519")) {
            mensaje = "No se ha podido conectar al almacenamiento de la aplicación. Si el problema persiste por favor contacte con el administrador del sistema.";
        }
        if (debug) {
            mensaje = super.getLocalizedMessage();
        }
    }
}
