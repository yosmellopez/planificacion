/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.planning.entity.CriticalyLevel;
import java.io.IOException;

/**
 *
 * @author Nodo
 */
public class CriticalyLevelSerializer extends JsonSerializer<CriticalyLevel> {

    @Override
    public void serialize(CriticalyLevel nivel, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObject(nivel.getId());
    }

}
