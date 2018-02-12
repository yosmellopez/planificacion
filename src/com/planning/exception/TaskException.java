package com.planning.exception;

public final class TaskException extends OracleException {

    public TaskException(String message) {
        super(message);
        tratarMensaje();
    }

    public TaskException(Throwable cause) {
        super(cause);
        tratarMensaje();
    }

    @Override
    public void tratarMensaje() {
        mensaje = super.getLocalizedMessage().isEmpty() ? super.getMessage() : super.getLocalizedMessage();
        if (mensaje.contains("SYS_C009972")) {
            mensaje = "La secuencia de tareas no está correctamente configurada.";
        } else if (mensaje.contains("FK_DOCUMENT_TASK")) {
            mensaje = "No se puede eliminar esta tarea porque contiene documentos.";
        } else if (mensaje.contains("FK_TASK_LEVEL")) {
            mensaje = "No se puede eliminar esta tarea porque está referenciada en los estados de alerta.";
        } else if (mensaje.contains("SYS_C006343")) {
            mensaje = "La secuencia de archivos no está correctamente configurada.";
        } else if (mensaje.contains("UNIQUE_FROM_TO")) {
            mensaje = "No se puede insertar este vínculo porque ya existe.";
        } else if (mensaje.contains("SYS_C009726")) {
            mensaje = "La secuencia de tareas no está correctamente configurada.";
        } else if (mensaje.contains("SYS_C0012419")) {
            mensaje = "La secuencia de tareas no está correctamente configurada.";
        } else if (mensaje.contains("SYS_C0012412")) {
            mensaje = "La secuencia de tareas del plan no está correctamente configurada.";
        } else if (mensaje.contains("SYS_C009985")) {
            mensaje = "La secuencia de tareas del plan no está correctamente configurada.";
        } else if (mensaje.contains("FK_PLTASK_TASK")) {
            mensaje = "No se puede eliminar esta tarea porque esta contenida en los planes.";
        } else if (mensaje.contains("12519")) {
            mensaje = "No se ha podido conectar al almacenamiento de la aplicación. Si el problema persiste por favor contacte con el administrador del sistema.";
        } else {
            mensaje = "Error interno del sistema. Si el problema persiste por favor contacte con el administrador del sistema.";
        }
        if (debug) {
            mensaje = super.getLocalizedMessage();
        }
    }
}
