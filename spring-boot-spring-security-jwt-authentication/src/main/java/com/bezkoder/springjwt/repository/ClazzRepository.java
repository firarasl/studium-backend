package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Clazz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClazzRepository extends JpaRepository<Clazz,Long> {

    List<Clazz> findAll();


}
