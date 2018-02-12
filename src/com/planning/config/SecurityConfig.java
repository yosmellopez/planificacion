/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.config;

import com.planning.security.*;
import com.planning.security.jwt.JwtAuthenticationProvider;
import com.planning.security.jwt.JwtTokenAuthenticationProcessingFilter;
import com.planning.security.jwt.SkipPathRequestMatcher;
import com.planning.security.jwt.token.TokenExtractor;
import com.planning.service.UserTokenService;
import com.planning.util.MapeadorObjetos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    public static final String AUTHENTICATION_HEADER_NAME = "Authorization";
    
    public static final String AUTHENTICATION_URL = "/api/auth/login";
    
    public static final String AUTHENTICATION_RESTORE_PASSWORD = "/apirest/auth/restore";
    
    public static final String REFRESH_TOKEN_URL = "/api/auth/token";
    
    public static final String API_ROOT_URL = "/api/**";
    
    public static final String API_ROOT_PATTERN = "/api/";
    
    @Autowired
    ReloadableResourceBundleMessageSource messageSource;
    
    @Autowired
    UserDetailsService userDetailsService;
    
    @Autowired
    private RestAuthenticationEntryPoint authenticationEntryPoint;
    
    @Autowired
    private AutenticacionAjaxFallida autenticacionAjaxFallida;
    
    @Autowired
    private AutenticacionAjaxExitosa autenticacionAjaxExitosa;
    
    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    
    @Autowired
    private TokenExtractor tokenExtractor;
    
    @Autowired
    private MapeadorObjetos mapeadorObjetos;
    
    @Autowired
    private UserTokenService tokenService;
    
    @Override
    protected void configure(AuthenticationManagerBuilder registry) throws Exception {
        registry.authenticationProvider(daoAuthenticationProvider());
        registry.authenticationProvider(jwtAuthenticationProvider);
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/bundles/**", "/img/**", "/icons/**", "/font/**", "/fonts/**");
    }
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> permitAllEndpointList = Arrays.asList(AUTHENTICATION_URL, REFRESH_TOKEN_URL, AUTHENTICATION_RESTORE_PASSWORD, "/console");
        http.csrf().disable().headers().frameOptions().disable().and()
                .authorizeRequests()
                .antMatchers("/", "/area/**", "/canal/**", "/criticidad-tarea/**", "/criticidad-plan/**", "/documento/**", "/gerencia/**", "/planTarea/**", "/plan/**",
                        "/cargo/**", "/perfil/**", "/estado-plan/**", "/estado-tarea/**", "/tarea/**", "/usuario/**").authenticated()
                .antMatchers(API_ROOT_URL).authenticated()
                .antMatchers("/login.html").permitAll()
                .antMatchers("/usuario.html").hasAuthority("Administrador").and()
                .formLogin().loginPage("/login.html").loginProcessingUrl("/usuario/autenticar").successHandler(autenticacionAjaxExitosa).failureHandler(autenticacionAjaxFallida)
                .usernameParameter("email").passwordParameter("pass").and()
                .logout().logoutSuccessHandler(manejadorLogout()).logoutUrl("/logout").logoutSuccessUrl("/login.html").and()
                .addFilterBefore(new CustomCorsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildAjaxLoginProcessingFilter(AUTHENTICATION_URL), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(permitAllEndpointList, API_ROOT_URL), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(new AccesoDenegadoHandler());
        http.headers().defaultsDisabled().cacheControl();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).maximumSessions(Integer.MAX_VALUE);
    }
    
    protected AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter(String loginEntryPoint) throws Exception {
        AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(loginEntryPoint, autenticacionAjaxExitosa, autenticacionAjaxFallida, mapeadorObjetos);
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setTokenService(tokenService);
        return filter;
    }
    
    protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter(List<String> pathsToSkip, String pattern) throws Exception {
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pattern);
        JwtTokenAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(autenticacionAjaxFallida, tokenExtractor, matcher);
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }
    
    @Bean
    public AuthenticationProvider daoAuthenticationProvider() throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setMessageSource(messageSource);
//        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    
    @Bean
    public ShaPasswordEncoder passwordEncoder() {
        return new ShaPasswordEncoder(1);
    }
    
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public ManejadorLogout manejadorLogout() {
        return new ManejadorLogout();
    }
}
