package com.planning.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.planning.diagram.Columna;
import java.io.IOException;

/**
 *
 * @author Nodo
 */
public class ColumnaSerializer extends JsonSerializer<Columna> {
    
    @Override
    public void serialize(Columna columna, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(columna.getNombre());
    }
    
}
