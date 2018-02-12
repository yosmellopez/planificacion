package com.planning.exception;

public final class PositionException extends OracleException {
    
    public PositionException(String message) {
        super(message);
        tratarMensaje();
    }
    
    public PositionException(Throwable cause) {
        super(cause);
        tratarMensaje();
    }
    
    @Override
    public void tratarMensaje() {
        mensaje = super.getLocalizedMessage().isEmpty() ? super.getMessage() : super.getLocalizedMessage();
        if (mensaje.contains("SYS_C0012415")) {
            mensaje = "Ya existen estas siglas.";
        } else if (mensaje.contains("SYS_C009972")) {
            mensaje = "La secuencia no está correctamente configurada para esta entidad.";
        } else if (mensaje.contains("SYS_C009726")) {
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
