package com.planning.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

public class SerializadorFecha extends JsonSerializer<Date> {

    @Override
    public void serialize(Date t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        ShaPasswordEncoder encoder = new ShaPasswordEncoder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        jg.writeString(dateFormat.format(t));
    }
}
