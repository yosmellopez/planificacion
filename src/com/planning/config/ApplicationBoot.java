/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.config;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 *
 * @author Nodo
 */
public class ApplicationBoot extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{AppConfig.class, SecurityConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebMvcConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        return new Filter[]{new DelegatingFilterProxy("springSecurityFilterChain"), new OpenEntityManagerInViewFilter(), encodingFilter, new MultipartFilter()};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        String url = this.getClass().getResource("").getPath();
        File file = new File(url);
        while (file.exists() && !file.getName().equals("WEB-INF")) {
            file = file.getParentFile();
        }
        file = file.getParentFile();
        registration.setAsyncSupported(true);
        String uploadDir = file.getAbsolutePath() + "/uploads";
        MultipartConfigElement element = new MultipartConfigElement(uploadDir, 1000000000000000L, 1000000000000000L, 0);
        registration.setMultipartConfig(element);
        file = new File(uploadDir);
        if (!file.isDirectory()) {
            file.mkdir();
        }
    }
}
