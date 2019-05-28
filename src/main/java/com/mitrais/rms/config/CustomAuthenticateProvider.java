package com.mitrais.rms.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.mitrais.rms.entity.User;
import com.mitrais.rms.service.UserService;

@Component
public class CustomAuthenticateProvider implements AuthenticationProvider {

	@Autowired
	private UserService userService;
	
	static final String AUTHENTICATION_FAILED = "Authentication failed";
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			String username = authentication.getName();
			String password = authentication.getCredentials().toString();
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
			User findUserAccount = userService.findUserAccount(username);
			if(findUserAccount == null)
				throw new BadCredentialsException(AUTHENTICATION_FAILED);
			boolean matches = bCryptPasswordEncoder.matches(password, findUserAccount.getPassword());
			if(!matches)
				throw new BadCredentialsException(AUTHENTICATION_FAILED);
			List<GrantedAuthority> authorities = new ArrayList<>();
			GrantedAuthority authority = new SimpleGrantedAuthority(findUserAccount.getRole());
			authorities.add(authority);
			return new UsernamePasswordAuthenticationToken(username, findUserAccount.getPassword(), authorities);
		} catch (Exception e) {
			throw new BadCredentialsException(AUTHENTICATION_FAILED);
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
