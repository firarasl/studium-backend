package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.payload.response.StudentRespond;
import com.bezkoder.springjwt.payload.response.TestGPA;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

public interface TestService {


    void addTest(String name, Timestamp date);

    void updateTest(Long id, String name, Timestamp timestamp);

    List<StudentRespond> getAllStudentsOfTest(Long id);

    String changeTestGrade(Long testId, Long studentId, double grade);

    void parseCSV(MultipartFile file);

    void deleteTest(Long testId);

    List<TestGPA> myTestsAndGpa(Long id, Long subjectId);
}
