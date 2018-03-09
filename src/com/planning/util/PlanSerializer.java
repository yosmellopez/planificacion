package com.planning.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.planning.entity.Plan;

import java.io.IOException;

public class PlanSerializer extends JsonSerializer<Plan> {
    
    @Override
    public void serialize(Plan value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        Plan plan = new Plan();
        plan.setId(value.getId());
        plan.setFechaCreacion(value.getFechaCreacion());
        plan.setStatusplanid(value.getStatusplanid());
        plan.setUsuario(value.getUsuario());
        plan.setFechaActivacion(value.getFechaActivacion());
        plan.setCriticalyLevel(value.getCriticalyLevel());
        plan.setName(value.getName());
        plan.setDescription(value.getDescription());
        plan.setEjecucion(value.isEjecucion());
        gen.writeObject(plan);
    }
}
