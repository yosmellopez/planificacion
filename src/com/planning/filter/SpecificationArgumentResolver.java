package com.planning.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planning.impl.GenericSpecification;
import com.planning.util.MapeadorObjetos;
import java.util.HashMap;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class SpecificationArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String DEFAULT_SPECIFICATION_PARAMETER = "parametros";

    private HashMap<String, Object> hashMap = new HashMap<>();

    private String parametrosParameterName = DEFAULT_SPECIFICATION_PARAMETER;

    private final ObjectMapper objectMapper = new MapeadorObjetos();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Specification.class.equals(parameter.getParameterType());
    }

    @Override
    public Specification resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String parametro = webRequest.getParameter(parametrosParameterName);
        boolean hasText = StringUtils.hasText(parametro);
        if (!hasText) {
            return null;
        }
        hashMap = objectMapper.readValue(parametro, HashMap.class);
        if (hashMap == null) {
            return null;
        }
        return new GenericSpecification(hashMap);
    }

    public String getParametrosParameterName() {
        return parametrosParameterName;
    }

    public void setParametrosParameterName(String parametrosParameterName) {
        this.parametrosParameterName = parametrosParameterName;
    }

    public HashMap<String, Object> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, Object> hashMap) {
        this.hashMap = hashMap;
    }
}
