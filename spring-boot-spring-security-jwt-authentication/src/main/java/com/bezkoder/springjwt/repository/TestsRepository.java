package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Subject;
import com.bezkoder.springjwt.models.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface TestsRepository extends JpaRepository<Test,Long> {



    List<Test> findAllBySubjectId(Long id);

    List<Test> findAllBySubject(Subject subject);
}





