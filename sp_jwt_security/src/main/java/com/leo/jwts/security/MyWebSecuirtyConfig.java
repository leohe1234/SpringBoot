package com.leo.jwts.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MyWebSecuirtyConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private JwtFilter jwtFilter;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests().antMatchers("/user/*").permitAll()
								.anyRequest().authenticated();
		
		http.addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class);
			
			
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);
	}

	
	
}
