/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.util;

import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 *
 * @author Nodo
 */
public class RestMappingJacksonJsonView extends MappingJackson2JsonView {

    public RestMappingJacksonJsonView() {
        super(new MapeadorObjetos());
    }
}
