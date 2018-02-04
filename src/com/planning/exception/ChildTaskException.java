package com.planning.exception;

public class ChildTaskException extends OracleException {
    
    public ChildTaskException(String message) {
        super(message);
        tratarMensaje();
    }
    
    public ChildTaskException(Throwable cause) {
        super(cause);
        tratarMensaje();
    }
    
    @Override
    public void tratarMensaje() {
        mensaje = super.getLocalizedMessage();
        if (mensaje.contains("unq_facultad_0")) {
            mensaje = "Ya existen estas siglas.";
        } else if (mensaje.contains("unq_facultad_1")) {
            mensaje = "Ya existe este nombre de facultad.";
        } else if (mensaje.contains("fk_departamento_id_facultad")) {
            mensaje = "No se puede eliminar esta facultad porque contiene departamentos.";
        }
    }
}
