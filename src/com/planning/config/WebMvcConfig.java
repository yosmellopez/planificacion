/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.config;

import com.planning.fcm.FcmSettings;
import com.planning.util.MapeadorObjetos;
import com.wavemaker.runtime.file.manager.FileServiceManager;
import de.bytefish.fcmjava.client.FcmClient;
import de.bytefish.fcmjava.http.client.IFcmClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.*;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.planning"})
@EnableSpringDataWebSupport
public class WebMvcConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {
    
    @Autowired
    Environment env;
    
    @Bean
    public MapeadorObjetos wMObjectMapper() {
        return new MapeadorObjetos();
    }
    
    @Bean
    public FileServiceManager fileServiceManager() {
        return new FileServiceManager();
    }
    
    @Bean
    public IFcmClient fcmClient(FcmSettings settings) {
        return new FcmClient(settings);
    }
    
    @Value("${planning.cachear}")
    String cachear;
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(wMObjectMapper());
        converters.add(converter);
        super.configureMessageConverters(converters);
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/fonts/**", "/flaty/**", "/docs/**", "/app/**", "/theme-classic/**", "/recursos/**")
                .addResourceLocations("/fonts/", "/flaty/", "/docs/", "/app/", "/theme-classic/", "/recursos/");
    }
    
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false).
                mediaType("html", MediaType.TEXT_HTML).
                mediaType("htm", MediaType.TEXT_HTML).
                mediaType("xml", MediaType.APPLICATION_XML).
                mediaType("do", MediaType.MULTIPART_FORM_DATA).
                mediaType("json", MediaType.APPLICATION_JSON);
        super.configureContentNegotiation(configurer);
    }
    
    @Bean(name = "contentNegotiatingViewResolver")
    public ContentNegotiatingViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        List<ViewResolver> resolvers = new LinkedList<>();
        resolvers.add(viewResolver());
        resolver.setContentNegotiationManager(manager);
        resolver.setViewResolvers(resolvers);
        return resolver;
    }
    
    private static final String UTF8 = "UTF-8";
    
    private ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    
    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        return resolver;
    }
    
    @Bean
    public TemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        engine.addTemplateResolver(htmlTemplateResolver());
        Set<IDialect> additionalDialects = new LinkedHashSet<>();
        additionalDialects.add(new SpringSecurityDialect());
        engine.setAdditionalDialects(additionalDialects);
        return engine;
    }
    
    private ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding(UTF8);
        resolver.setCacheable(Boolean.valueOf(cachear));
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;
    }
    
    private ITemplateResolver htmlTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(2);
        templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(UTF8);
        templateResolver.setCacheable(false);
        return templateResolver;
    }
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        AuthenticationPrincipalArgumentResolver resolver = new AuthenticationPrincipalArgumentResolver();
        PageableHandlerMethodArgumentResolver phmar = new PageableHandlerMethodArgumentResolver();
        phmar.setOneIndexedParameters(true);
        phmar.setSizeParameterName("size");
        phmar.setMaxPageSize(Integer.MAX_VALUE);
        phmar.setFallbackPageable(new PageRequest(0, Integer.MAX_VALUE, new Sort(Sort.Direction.DESC, "id")));
        argumentResolvers.add(resolver);
        argumentResolvers.add(phmar);
        super.addArgumentResolvers(argumentResolvers);
    }
    
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

//    @Bean(name = "simpleMappingExceptionResolver")
//    public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
//        SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
//        Properties mappings = new Properties();
//        mappings.setProperty("DatabaseException", "databaseError");
//        mappings.setProperty("InvalidCreditCardException", "creditCardError");
//        r.setExceptionMappings(mappings);  // None by default
////        r.setDefaultErrorView("error");    // No default
////        r.setExceptionAttribute("ex");     // Default is "exception"
//        return r;
}
