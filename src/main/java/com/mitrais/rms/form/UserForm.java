package com.mitrais.rms.form;

import java.io.Serializable;

import com.mitrais.rms.entity.User;

public class UserForm implements Serializable{

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String retypePassword;
	
	public UserForm() {
		super();
	}
	
	public UserForm(User user)
	{
		this.id = user.getId();
		this.username = user.getUsername();
		this.password = user.getPassword();
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
	public User convertToUser() throws Exception
	{
		if(this.username.isEmpty())
			throw new Exception("001");
		if(this.password.isEmpty())
			throw new Exception("002");
		if(this.retypePassword.isEmpty())
			throw new Exception("003");
		if(!this.password.equals(this.retypePassword))
			throw new Exception("004");
		User user = new User();
		user.setUsername(this.username);
		user.setPassword(this.password);
		user.setId(this.id); 
		return user;
	}
}
