/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.planning.entity.Task;
import java.io.IOException;

/**
 *
 * @author Nodo
 */
public class TaskSerializer extends JsonSerializer<Task> {

    @Override
    public void serialize(Task tarea, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        Task task = new Task(tarea);
        gen.writeObject(task);
    }

}
