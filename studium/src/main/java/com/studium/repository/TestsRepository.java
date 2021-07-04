package com.studium.repository;

import com.studium.models.Subject;
import com.studium.models.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface TestsRepository extends JpaRepository<Test,Long> {



    List<Test> findAllBySubjectId(Long id);

    List<Test> findAllBySubject(Subject subject);
}





