package com.planning.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planning.entity.Area;
import com.planning.entity.CriticalyLevel;
import com.planning.entity.Management;
import com.planning.entity.Plan;
import com.planning.entity.Position;
import com.planning.impl.GenericSpecification;
import com.planning.service.AreaService;
import com.planning.service.CriticalyLevelService;
import com.planning.service.ManagementService;
import com.planning.service.PlanService;
import com.planning.service.PositionService;
import com.planning.util.MapeadorObjetos;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Service
public class SpecificationArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private AreaService areaService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private CriticalyLevelService levelService;

    @Autowired
    private PlanService planService;

    private static final String DEFAULT_SPECIFICATION_PARAMETER = "parametros";

    private HashMap<String, Object> hashMap = new HashMap<>();

    private String parametrosParameterName = DEFAULT_SPECIFICATION_PARAMETER;

    private final ObjectMapper objectMapper = new MapeadorObjetos();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return PlTaskFilter.class.equals(parameter.getParameterType());
    }

    @Override
    public Specification resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String parametro = webRequest.getParameter(parametrosParameterName);
        String planId = webRequest.getParameter("planId");
        PlTaskFilter filter = new PlTaskFilter();
        boolean hasText = StringUtils.hasText(parametro);
        if (!hasText && planId == null) {
            return null;
        }
        if (hasText) {
            hashMap = objectMapper.readValue(parametro, HashMap.class);
        }
        if (hashMap == null && planId == null) {
            return null;
        }
        try {
            String cargoString = hashMap.get("cargo").toString();
            int cargoId = cargoString.isEmpty() ? 0 : Integer.parseInt(cargoString);
            if (cargoId != 0) {
                Position position = positionService.findOne(cargoId);
                filter.setPosition(position);
            }
            String areaString = hashMap.get("gerencia").toString();
            int areaId = areaString.isEmpty() ? 0 : Integer.parseInt(areaString);
            if (areaId != 0) {
                Area area = areaService.findOne(areaId);
                filter.setArea(area);
            }
            String direccionString = hashMap.get("direccion").toString();
            int direccionId = direccionString.isEmpty() ? 0 : Integer.parseInt(direccionString);
            if (direccionId != 0) {
                Management management = managementService.findOne(direccionId);
                filter.setManagement(management);
            }
            String nivelString = hashMap.get("criticidad").toString();
            int nivelId = nivelString.isEmpty() ? 0 : Integer.parseInt(nivelString);
            if (nivelId != 0) {
                CriticalyLevel criticalyLevel = levelService.findOne(nivelId);
                filter.setCriticalyLevel(criticalyLevel);
            }
            if (planId != null) {
                int idPlan = Integer.parseInt(planId);
                Plan plan = planService.findOne(idPlan);
                filter.setPlan(plan);
                return filter;
            }
            return filter;
        } catch (NumberFormatException exception) {
            if (planId != null) {
                int idPlan = Integer.parseInt(planId);
                Plan plan = planService.findOne(idPlan);
                filter.setPlan(plan);
                return filter;
            }
            return null;
        }
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
