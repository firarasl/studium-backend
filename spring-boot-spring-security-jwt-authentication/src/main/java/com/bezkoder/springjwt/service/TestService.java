package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.payload.response.StudentRespond;

import java.sql.Timestamp;
import java.util.List;

public interface TestService {


    void addTest(String name, Timestamp date);

    void updateTest(Long id, String name, Timestamp timestamp);

    List<StudentRespond> getAllStudentsOfTest(Long id);
}
