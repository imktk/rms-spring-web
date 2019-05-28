package com.mitrais.rms.form;

import java.io.Serializable;

import org.apache.logging.log4j.util.Strings;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mitrais.rms.entity.User;
import com.mitrais.rms.exception.AppException;

public class UserForm implements Serializable{

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String retypePassword;
	private String role;
	
	public UserForm() {
		super();
	}
	
	public UserForm(User user)
	{
		this.id = user.getId();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.role = user.getRole();
	}
	
	private Long id;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRetypePassword() {
		return retypePassword;
	}
	public void setRetypePassword(String retypePassword) {
		this.retypePassword = retypePassword;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	public User convertToUser() throws AppException
	{
		if(this.username.isEmpty())
			throw new AppException("001");
		if(this.password.isEmpty())
			throw new AppException("002");
		if(this.retypePassword.isEmpty())
			throw new AppException("003");
		if(!this.password.equals(this.retypePassword))
			throw new AppException("004");
		User user = new User();
		user.setUsername(this.username);
		user.setPassword(new BCryptPasswordEncoder(12).encode(this.password));
		user.setId(this.id); 
		user.setRole(Strings.isEmpty(this.role) ? "USER" : this.role);  
		return user;
	}
}
