/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author Nodo
 */
public class GenericSpecification implements Specification {

    private final Map<String, Object> parametros;

    public GenericSpecification(Map<String, Object> parametros) {
        this.parametros = parametros;
    }

    @Override
    public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
        Set<String> claves = parametros.keySet();
        LinkedList<Predicate> predicados = new LinkedList<>();
        String valor1;
        Object valor;
        for (String clave : claves) {
            valor = parametros.get(clave);
            if (clave.contains("rango")) {
                String claveRango = clave.contains("rangoI") ? clave.replaceFirst("I", "F") : clave.replaceFirst("F", "I");
                valor1 = String.valueOf(parametros.get(claveRango));
                parametros.replace(claveRango, null);
                clave = clave.substring(6);
                Date dateRango;
                if (canParseValueToDate(valor.toString()) && canParseValueToDate(valor1)) {
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = dateFormat.parse(valor.toString());
                        dateRango = dateFormat.parse(valor1);
                        Predicate predicate = cb.between(root.get(clave), date.before(dateRango) ? date : dateRango, date.after(dateRango) ? date : dateRango);
                        predicados.add(predicate);
                    } catch (ParseException ex) {
                        ex.hashCode();
                    }
                }
            } else {
                String[] split = clave.split("[.]");
                if (split.length != 1) {
                    Path path = root.get(split[0]);
                    for (int j = 1; j < split.length; j++) {
                        path = path.get(split[j]);
                    }
                    predicados.add(cb.equal(path, valor));
                } else {
                    predicados.add(cb.equal(root.get(clave), valor));
                }
            }
        }
        Predicate[] toArray = predicados.toArray(new Predicate[predicados.size()]);
        return predicados.size() == 1 ? predicados.get(0) : cb.and(toArray);
    }

    private boolean canParseValueToDate(String valor) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateFormat.parse(valor);
            return true;
        } catch (ParseException ex) {
            ex.hashCode();
            return false;
        }
    }
}
