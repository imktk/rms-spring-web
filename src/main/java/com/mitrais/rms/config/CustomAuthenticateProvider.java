package com.mitrais.rms.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.mitrais.rms.entity.User;
import com.mitrais.rms.service.UserService;

@Component
public class CustomAuthenticateProvider implements AuthenticationProvider {

	@Autowired
	private UserService userService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			String username = authentication.getName();
			String password = authentication.getCredentials().toString();
			User checkLogin = userService.checkLogin(username, password);
			if(checkLogin == null)
				throw new BadCredentialsException("Authentication failed");
			return new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());
		} catch (Exception e) {
			throw new BadCredentialsException("Authentication failed");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
