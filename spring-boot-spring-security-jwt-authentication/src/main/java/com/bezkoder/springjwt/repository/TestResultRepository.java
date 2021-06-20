package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Test;
import com.bezkoder.springjwt.models.TestResult;
import com.bezkoder.springjwt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface TestResultRepository extends JpaRepository<TestResult, Long> {


    List<TestResult> findAllByUser(User user);

    List<TestResult> findAllByTest(Test test);
}
