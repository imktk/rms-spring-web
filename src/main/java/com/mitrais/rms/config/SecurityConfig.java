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
		
		http.csrf().disable();
		
		http.authorizeRequests()
			.antMatchers("/", "/login", "/checkLogin", "/register", "/createUser", "/js/**", "/css/**", "/fonts/**", "/images/**").permitAll()
			.anyRequest().authenticated()
			.and().httpBasic();
		
        http.authorizeRequests()
        	.antMatchers("/userInfo")
        	.access("hasAnyRole('USER', 'ADMIN')");

        http.authorizeRequests()
        	.antMatchers("/allUser")
        	.access("hasRole('ADMIN')");
		
		http.authorizeRequests()
			.and().formLogin()
            .loginPage("/login")
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
				.permitAll();
		
		http.authorizeRequests()
			.and().exceptionHandling()
		        .accessDeniedPage("/error");

    }

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenProvider);
	}
	
}
