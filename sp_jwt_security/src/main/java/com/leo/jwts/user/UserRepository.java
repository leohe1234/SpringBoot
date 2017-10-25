package com.leo.jwts.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByUsername(String name);
	User findById(Long id);
	Page<User> findAll(Pageable pageable);
	
}
