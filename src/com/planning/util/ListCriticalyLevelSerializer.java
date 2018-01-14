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
import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author Nodo
 */
public class ListCriticalyLevelSerializer extends JsonSerializer<Set<CriticalyLevel>> {

    @Override
    public void serialize(Set<CriticalyLevel> niveles, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        ArrayList<Integer> ids = new ArrayList<>();
        for (CriticalyLevel criticalyLevel : niveles) {
            ids.add(criticalyLevel.getId());
        }
        gen.writeObject(ids);
    }

}
