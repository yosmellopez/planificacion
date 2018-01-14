/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.config;

import com.planning.security.AccesoDenegadoHandler;
import com.planning.security.AutenticacionAjaxExitosa;
import com.planning.security.AutenticacionAjaxFallida;
import com.planning.security.AutenticacionFallida;
import com.planning.security.ManejadorLogout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
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

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    ReloadableResourceBundleMessageSource messageSource;

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder registry) throws Exception {
        registry.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/bundles/**", "/img/**", "/icons/**", "/font/**", "/fonts/**", "/flaty/**", "/docs/**", "/app/**", "/theme-classic/**", "/recursos/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().headers().frameOptions().disable().and()
                .authorizeRequests()
                .antMatchers("/").authenticated()
                .antMatchers("/login.html").permitAll()
                .antMatchers("/usuario.html", "/slider.html", "/noticia.html", "/trazas.html", "/planPostgrado.html").hasAuthority("Administrador").and()
                .formLogin().loginPage("/login.html").loginProcessingUrl("/usuario/autenticar").successHandler(autenticacionExitosa()).failureHandler(autenticacionAjaxFallida())
                .usernameParameter("email").passwordParameter("pass").and()
                .logout().logoutSuccessHandler(manejadorLogout()).logoutUrl("/logout").logoutSuccessUrl("/login.html").and()
                .exceptionHandling().accessDeniedHandler(new AccesoDenegadoHandler());
        http.headers().defaultsDisabled().cacheControl();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).maximumSessions(Integer.MAX_VALUE);
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
    public AutenticacionAjaxExitosa autenticacionExitosa() {
        return new AutenticacionAjaxExitosa();
    }

    @Bean
    public ShaPasswordEncoder passwordEncoder() {
        return new ShaPasswordEncoder(1);
    }

    @Bean
    public AutenticacionFallida autenticacionFallida() {
        AutenticacionFallida fallida = new AutenticacionFallida();
        fallida.setUsernameParameter("username");
        fallida.setDefaultFailureUrl("/login.html?error");
        return fallida;
    }

    @Bean
    public AutenticacionAjaxFallida autenticacionAjaxFallida() {
        AutenticacionAjaxFallida ajaxFallida = new AutenticacionAjaxFallida();
        return ajaxFallida;
    }

    @Bean
    public ManejadorLogout manejadorLogout() {
        return new ManejadorLogout();
    }
}
