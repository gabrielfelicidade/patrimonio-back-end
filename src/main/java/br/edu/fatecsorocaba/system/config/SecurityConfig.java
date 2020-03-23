package br.edu.fatecsorocaba.system.config;

import static br.edu.fatecsorocaba.system.config.SecurityConstants.*;
import br.edu.fatecsorocaba.system.config.CustomAuthenticationEntryPoint;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;

import br.edu.fatecsorocaba.system.service.CustomUserDetailsService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
		config.addAllowedMethod(HttpMethod.PUT);
        config.addAllowedMethod(HttpMethod.DELETE);
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        //
		http.cors()
		.configurationSource(request -> config)
		.and()
		.csrf().disable()
		.authorizeRequests()
		.antMatchers(HttpMethod.GET, LOGIN_URL).permitAll()
		.and()
			.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
		.and()
		.addFilter(new JWTAuthenticationFilter(authenticationManager()))
		.addFilter(new JWTAuthorizationFilter(authenticationManager(), customUserDetailsService));
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(customUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	@Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
		 return new CustomAuthenticationEntryPoint();
    }
}