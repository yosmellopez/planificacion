package com.planning.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.planning.diagram.Color;

import java.io.IOException;

/**
 * @author Nodo
 */
public class ColorSerializer extends JsonSerializer<Color> {
    
    @Override
    public void serialize(Color columna, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(columna.getColor());
    }
    
}
