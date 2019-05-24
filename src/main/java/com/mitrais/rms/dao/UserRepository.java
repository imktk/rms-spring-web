package com.mitrais.rms.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mitrais.rms.entity.User;

public interface UserRepository<P> extends JpaRepository<User, Long>{

	@Query("SELECT t FROM User t WHERE UPPER(t.username) LIKE CONCAT('%',UPPER(:username),'%')")
	List<User> searchByUsername(@Param("username") String username);
	
	List<User> findByUsernameLike(String username);
	
	Page<User> findByUsernameLike(String user, Pageable pageable);
}
