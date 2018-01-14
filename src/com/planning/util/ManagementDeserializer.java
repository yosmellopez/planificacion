/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.planning.entity.Management;
import java.io.IOException;

/**
 *
 * @author Nodo
 */
public class ManagementDeserializer extends JsonDeserializer<Management> {

    @Override
    public Management deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode readTree = p.getCodec().readTree(p);
        System.err.println(readTree.asText());
        return new Management(readTree.asText());
    }

}
