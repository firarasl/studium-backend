package com.studium.repository;

import com.studium.models.Test;
import com.studium.models.TestResult;
import com.studium.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface TestResultRepository extends JpaRepository<TestResult, Long> {


    List<TestResult> findAllByUser(User user);

    List<TestResult> findAllByTest(Test test);

    Optional<TestResult> findByTestAndUser(Test test, User user);


    List<TestResult> findAllByUserAndTest(User currentUser, Test test);
}
