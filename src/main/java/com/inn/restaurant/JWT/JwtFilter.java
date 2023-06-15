package com.inn.restaurant.JWT;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;

@Component
public class JwtFilter extends OncePerRequestFilter{

	@Autowired
	private JwtUtil jwtutil;
	
	@Autowired
	private CustomerUserDetailsService service;
	
	Claims claims=null;
	private String userName=null;
	
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, FilterChain filterChain)
			throws ServletException, IOException 
	{
		if(httpservletrequest.getServletPath().matches("/user/login|/user/forgotPassword|/user/signup")) {
			filterChain.doFilter(httpservletrequest,httpservletresponse);
		}
		else {
			String authorizationHeader= httpservletrequest.getHeader("Authorization");
			String token =null;
			
			if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
				token = authorizationHeader.substring(7);
				userName =jwtutil.extractUsername(token);
				claims=jwtutil.extractAllClaims(token);
				
				
			}
			if(userName!= null && SecurityContextHolder.getContext().getAuthentication()==null) {
				UserDetails userdetails= service.loadUserByUsername(userName);
				if(jwtutil.validateToken(token, userdetails)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
							new UsernamePasswordAuthenticationToken(userdetails, null,userdetails.getAuthorities());
					usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource()
							.buildDetails(httpservletrequest)
				);
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
			filterChain.doFilter(httpservletrequest, httpservletresponse);
			
			
		}
		
		
	}
	public boolean isAdmin()
	{
		return "admin".equalsIgnoreCase((String) claims.get("role"));
	}
	public boolean isUser()
	{
		return "admin".equalsIgnoreCase((String) claims.get("role"));
	}
	public String getCurrentUser() {
		return userName;
	}
	
	

}