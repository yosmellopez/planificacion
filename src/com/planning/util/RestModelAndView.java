/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.util;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

public class RestModelAndView extends ModelAndView {

    public RestModelAndView(Map<String, ?> model) {
        super(new RestMappingJacksonJsonView(), model);
    }

    public RestModelAndView(Map<String, ?> model, HttpStatus httpStatus) {
        super(new RestMappingJacksonJsonView(), model);
        super.setStatus(httpStatus);
    }

    public static ModelAndView ok() {
        RestModelAndView view = new RestModelAndView(new ModelMap());
        view.setStatus(HttpStatus.OK);
        return view;
    }

    public static ModelAndView ok(ModelMap map) {
        RestModelAndView view = new RestModelAndView(map);
        view.setStatus(HttpStatus.OK);
        return view;
    }

}
