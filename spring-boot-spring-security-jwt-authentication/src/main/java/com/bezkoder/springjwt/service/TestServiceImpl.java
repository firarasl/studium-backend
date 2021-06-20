package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.helper.StudentDataUtil;
import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Test;
import com.bezkoder.springjwt.models.TestResult;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.response.StudentRespond;
import com.bezkoder.springjwt.repository.TestResultRepository;
import com.bezkoder.springjwt.repository.TestsRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TestServiceImpl implements TestService {


@Autowired
private TestsRepository testsRepository;

    public StudentDataUtil helper;

    @Autowired
    private TestResultRepository testResultRepository;


    @Override
    public void addTest(String name, Timestamp date) {
        Test test = new Test();
        test.setName(name);
        test.setDate(date);
        testsRepository.save(test);

    }

    @Override
    public void updateTest(Long id, String name, Timestamp timestamp) {
        Optional<Test> optionalTest = (Optional<Test>) Hibernate.unproxy(testsRepository.findById(id));

        if (!optionalTest.isPresent()){
            throw new NoSuchElementException("this test doesnt exist");
        }

        if (!optionalTest.get().getName().equals(name)){
            optionalTest.get().setName(name);
        }

        if (!optionalTest.get().getDate().equals(timestamp)){
            optionalTest.get().setDate(timestamp);
        }

        testsRepository.save(optionalTest.get());

    }

    @Override
    public List<StudentRespond> getAllStudentsOfTest(Long id) {
        Optional<Test> optionalTest = (Optional<Test>) Hibernate.unproxy(testsRepository.findById(id));

        if (!optionalTest.isPresent()){
            throw new NoSuchElementException("this test doesnt exist");
        }

        List<StudentRespond> studentResponds = new ArrayList<>();

        List<TestResult> testResultList = (List<TestResult>) Hibernate.unproxy(testResultRepository.findAllByTest(optionalTest.get()));

        List<User> userList = new ArrayList<>();

        for (int i = 0; i < testResultList.size(); i++) {
            userList.add(testResultList.get(i).getUser());
        }

        //TODO
//        double gpa=0;
//
//        for (int i = 0; i < userList.size(); i++) {
//                gpa= helper.getGPA(userList.get(i).g);
//                StudentRespond studentRespond = helper.getPayload(userList.get(i), gpa);
//
//                studentGPAlist.add(studentRespond);
//        }

        return null;
    }
}



