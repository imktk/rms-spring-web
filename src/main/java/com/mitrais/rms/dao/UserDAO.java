package com.mitrais.rms.dao;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mitrais.rms.entity.User;

@Repository
@Transactional
public class UserDAO {

	static final Logger logger = Logger.getLogger(UserDAO.class);
	
	@Autowired
	private EntityManager entityManager;
	
    public User findUserAccount(String username) {
        try {
            String sql = "Select e from " + User.class.getName() + " e "
                    + " Where e.username = :username ";
 
            Query query = entityManager.createQuery(sql, User.class);
            query.setParameter("username", username);
 
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
        	logger.error(e); 
            return null;
        }
    }
    
    public List<User> getAllUser() 
    {
        try {
            String sql = "Select e from " + User.class.getName() + " e ";
            Query query = entityManager.createQuery(sql, User.class);
 
            return query.getResultList();
        } catch (NoResultException e) {
        	logger.error(e); 
            return Collections.emptyList();
        }
    }
    
    public User checkLogin(String username, String password)
    {
    	try {
            String sql = "Select e from " + User.class.getName() + " e "
                    + " Where e.username = :username and e.password = :password";
 
            Query query = entityManager.createQuery(sql, User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
 
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
        	logger.error(e); 
            return null;
        }
    }

}
