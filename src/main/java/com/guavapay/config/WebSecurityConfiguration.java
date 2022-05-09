package com.guavapay.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Order(value = 100)
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        prePostEnabled = true
)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final SecurityFilter securityFilter;

    public WebSecurityConfiguration(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers()
                .contentSecurityPolicy("default-src 'self';");
        http
                .logout()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .and()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .mvcMatchers(SWAGGER_URLS).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js");
    }

    private static final String[] SWAGGER_URLS = {
            "/v2/api-docs",
            "/configuration/**",
            "/swagger*/**",
            "/webjars/**",
    };

}
