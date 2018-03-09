package com.planning.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MapeadorObjetos extends ObjectMapper {

    public MapeadorObjetos() {
        super();
        Hibernate5Module hbm = new Hibernate5Module();
        hbm.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
        hbm.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
        registerModule(hbm);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(Date.class, new DeserializadorFecha());
        simpleModule.addSerializer(Date.class, new SerializadorFecha());
        registerModule(simpleModule);
        getDeserializationConfig().with(new SimpleDateFormat("dd/MM/yyyy"));
        getSerializationConfig().with(new SimpleDateFormat("dd/MM/yyyy"));
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    public <T> T readValue(String content, Class<T> valueType) {
        try {
            return super.readValue(content, valueType);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String writeValueAsString(Object value) {
        try {
            return super.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            return ex.getMessage();
        }
    }
}
