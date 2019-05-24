package com.mitrais.rms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mitrais.rms.dao.UserDAO;
import com.mitrais.rms.dto.UserDTO;
import com.mitrais.rms.entity.User;

//@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserDAO userDAO;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User appUser = this.userDAO.findUserAccount(userName);
		 
        if (appUser == null) {
            System.out.println("User not found! " + userName);
            throw new UsernameNotFoundException("User " + userName + " was not found in the database");
        }
 
        System.out.println("Found User: " + appUser);
 
        List<String> roleNames = new ArrayList<String>();
        roleNames.add("ROLE_USER");
 
        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        if (roleNames != null) {
            for (String role : roleNames) {
                GrantedAuthority authority = new SimpleGrantedAuthority(role);
                grantList.add(authority);
            }
        }
        UserDTO dto = new UserDTO();
        dto.setAuthorities(grantList);
        dto.setPassword(appUser.getPassword());
        dto.setUsername(appUser.getUsername());
        UserDetails userDetails = dto;
 
        return userDetails;
    }

}
