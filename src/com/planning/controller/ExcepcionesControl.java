package com.planning.controller;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@ControllerAdvice
public class ExcepcionesControl extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class, IOException.class})
    public ModelAndView handleCustomException(Exception ex, WebRequest request) {
        logger.error("Exception during execution of SpringSecurity application", ex);
        ModelMap map = new ModelMap();
        map.put("msg", ex.getLocalizedMessage());
        map.put("exception", ex.getLocalizedMessage());
        map.put("success", false);
        return new ModelAndView(new MappingJackson2JsonView(), map);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("Exception during execution of SpringSecurity application", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
    }

    private static Logger logger = LoggerFactory.getLogger(ExcepcionesControl.class);

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(final Throwable throwable, final Model model) {
        logger.error("Exception during execution of SpringSecurity application", throwable);
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        model.addAttribute("errorMessage", errorMessage);
        return "error/error";
    }

}
