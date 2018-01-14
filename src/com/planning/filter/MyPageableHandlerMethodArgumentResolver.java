/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.filter;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author Nodo
 */
public class MyPageableHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Pageable.class.equals(parameter.getParameterType());
    }
    
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String sizeParameter = webRequest.getParameter("length");
        String startParameter = webRequest.getParameter("start");
        if (startParameter != null && sizeParameter != null) {
            Integer start = Integer.parseInt(startParameter);
            Integer size = Integer.parseInt(sizeParameter);
            return new PageRequest(start, size);
        } else {
            if (startParameter != null) {
                Integer start = Integer.parseInt(startParameter);
                return new PageRequest(start, 20);
            } else if (sizeParameter != null) {
                Integer size = Integer.parseInt(sizeParameter);
                return new PageRequest(0, size);
            } else {
                return new PageRequest(0, 20);
            }
        }
    }
    
}
