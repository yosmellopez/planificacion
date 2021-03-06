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
        this.getMensaje();
    }
    
    public final String getMensaje() {
        mensaje = super.getLocalizedMessage().isEmpty() ? super.getMessage() : super.getLocalizedMessage();
        if (mensaje.contains("12519")) {
            mensaje = "No se ha podido conectar al almacenamiento de la aplicación. Si el problema persiste por favor contacte con el administrador del sistema.";
        } else if (mensaje.contains("SYS_C0012415")) {
            mensaje = "La secuencia de cargos no está correctamente configurada.";
        } else if (mensaje.contains("FK_PLTASK_TASK")) {
            mensaje = "No se puede eliminar esta tarea porque esta siendo usada en tareas del plan.";
        } else {
            mensaje = "Error interno del sistema. Si el problema persiste por favor contacte con el administrador del sistema.";
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
