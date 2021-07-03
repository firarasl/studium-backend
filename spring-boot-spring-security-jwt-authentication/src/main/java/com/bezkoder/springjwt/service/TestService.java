package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.models.Test;
import com.bezkoder.springjwt.payload.response.StudentRespond;
import com.bezkoder.springjwt.payload.response.TestGPA;
import com.bezkoder.springjwt.payload.response.TestResponse;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

public interface TestService {


    void addTest(String name, Timestamp date, String subjectName, Long teacherId, String clazzName);

    void updateTest(Long id, String name, Timestamp timestamp);

    List<StudentRespond> getAllStudentsOfTest(Long id);

    String changeTestGrade(Long testId, String studentName, double grade);

    void parseCSV(MultipartFile file);

    void deleteTest(Long testId);

    List<TestGPA> myTestsAndGpa(Long id, Long subjectId);

    Set<Test> getAllTestsOfTeacher(Long id);

    TestResponse findById(Long id);
}
