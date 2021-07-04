package com.studium.repository;

import java.util.List;
import java.util.Optional;

import com.studium.models.Clazz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.studium.models.User;

@Component
public interface UserRepository extends JpaRepository<User, Long> {


	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);


	List<User> findAll();


    List<User> findAllByClazz(Clazz clazz);
}


