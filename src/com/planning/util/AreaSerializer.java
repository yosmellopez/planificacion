/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.planning.entity.Area;
import java.io.IOException;

/**
 *
 * @author Nodo
 */
public class AreaSerializer extends JsonSerializer<Area> {
    
    @Override
    public void serialize(Area area, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(area.getId());
    }
    
}
