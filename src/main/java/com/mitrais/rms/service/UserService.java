package com.mitrais.rms.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitrais.rms.dao.UserRepository;
import com.mitrais.rms.entity.User;
import com.mitrais.rms.exception.AppException;
import com.mitrais.rms.form.UserForm;

@Service
public class UserService {

	static final Logger logger = Logger.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepo;
	
	@Transactional
	public Page<User> searchByUsername(String username, Pageable pageable)
	{
		return userRepo.findByUsernameContaining(username, pageable);
	}
	
	@Transactional
	public User findUserAccount(String username)
	{
		return userRepo.findByUsername(username);
	}

    @Transactional
    public Page<User> getAllUser(Pageable pageable) 
    {
    	return userRepo.findAll(pageable);
    }
    
    @Transactional
    public void deleteUser(Long userId)
    {
    	userRepo.deleteById(userId);
    }
    
    public User findById(Long userId)
    {
    	return userRepo.findById(userId).orElse(null);
    }

    @Transactional
    public boolean createUser(UserForm user) throws AppException
    {
    	try {
    		User findUserAccount = userRepo.findByUsername(user.getUsername());
    		if(findUserAccount != null)
    			throw new AppException("005");
    		
    		return userRepo.save(user.convertToUser()) != null;
    	} catch (Exception e) {
    		logger.error(e); 
			throw e;
		}
    }
    
    @Transactional
    public boolean updateUser(User user)
    {
    	return userRepo.save(user) != null;
    }
}
