package com.mitrais.rms.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mitrais.rms.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Page<User> findByUsernameContaining(String user, Pageable pageable);
	
	User findByUsername(String username);
}
