package com.sedatcan.configuration;

import com.sedatcan.security.CustomerAuthenticationFilter;
import com.sedatcan.security.CustomerLoginFilter;
import com.sedatcan.security.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@EnableGlobalMethodSecurity
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers(
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/v2/**",
                        "/swagger/**",
                        "/swagger-resources/**",
                        "/customers/**"
                )
                .permitAll()
                .anyRequest()
                .authenticated();// H2 Console Dash-board - only for testing
        http.addFilterBefore(customerLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(customerAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.cors();
    }

    @Bean
    public CustomerLoginFilter customerLoginFilter() throws Exception {
        return new CustomerLoginFilter("/customer/authentication", authenticationManager());
    }

    public CustomerAuthenticationFilter customerAuthenticationFilter() {
        return new CustomerAuthenticationFilter(tokenAuthenticationService);
    }
}
