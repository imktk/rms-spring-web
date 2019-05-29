package com.mitrais.rms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private CustomAuthenticateProvider authenProvider;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		
        http.authorizeRequests()
	    	.antMatchers("/userInfo")
	    	.hasAnyAuthority("ADMIN", "USER")
	    	;

	    http.authorizeRequests()
	    	.antMatchers("/allUser", "/deleteUser/**")
	    	.hasAuthority("ADMIN")
	    	;
		
		http.authorizeRequests()
			.antMatchers("/", "/checkLogin", "/register", "/createUser", "/js/**", "/css/**", "/fonts/**", "/images/**")
			.permitAll()
			;
		
        http.authorizeRequests()
	        .anyRequest()
	        .authenticated()
	        .and().httpBasic()
	        ;
        
		http.authorizeRequests()
			.and().formLogin()
            .loginPage("/login").permitAll()
            .failureUrl("/login?error")
            .defaultSuccessUrl("/")
            .usernameParameter("username")
            .passwordParameter("password")
            ;
		
		http.authorizeRequests()
			.and().logout()
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login")
				.permitAll()
				;
		
		http.authorizeRequests()
			.and().exceptionHandling()
		        .accessDeniedPage("/error")
		        ;

    }

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenProvider);
	}
	
}
