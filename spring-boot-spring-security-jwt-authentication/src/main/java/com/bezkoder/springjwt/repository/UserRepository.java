package com.bezkoder.springjwt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.models.User;

@Component
public interface UserRepository extends CrudRepository<User, Long> {
	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);


	List<User> findAll();


@Query(value="UPDATE users SET clazz_id= :clazzId WHERE id= :studentId;\n", nativeQuery=true)
    void updateSetClazz(Long studentId, Long clazzId);



}


