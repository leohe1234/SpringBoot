package com.leo.jwts.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlet4preview.GenericFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 139878490832048L;
	
    
    @Autowired
    UserService userService;
    
	@Autowired
	JwtUtils jwtUtils;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

 
	
    @Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException,ServletException {
		
		System.out.println("JwtFilter dofilter here .....");
	    String authHeader =  request.getHeader(this.tokenHeader);
	     
	    if (authHeader != null && authHeader.startsWith(tokenHead)) 
	    {
	            final String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
	            String username = jwtUtils.getUsernameFromToken(authToken);


	            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

	                UserDetails userDetails = this.userService.loadUserByUsername(username);

	                if (jwtUtils.isvalidToken(authToken)) 
	                {
	                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
	                            userDetails, null, userDetails.getAuthorities());
	                    
	                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                   
	                    logger.info("authenticated user " + username + ", setting security context");
	                    SecurityContextHolder.getContext().setAuthentication(authentication);
	                }
	            }
	        }
	
		chain.doFilter(request, response);
	
	}



}
