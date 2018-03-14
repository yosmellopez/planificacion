package com.planning.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.planning.entity.Users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class SerializadorBackups extends JsonSerializer<Set<Users>> {
    
    @Override
    public void serialize(Set<Users> users, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        ArrayList<Users> arrayList = new ArrayList<>();
        for (Users u : users) {
            Users newUser = new Users(u.getId(), u.getUsername(), u.getLastname(), u.getRut(), u.getCellphone(), u.getEmail(), u.getName());
            newUser.setBackups(null);
            arrayList.add(newUser);
        }
        gen.writeObject(arrayList);
    }
}
