package com.studium.service;

import com.studium.models.Test;
import com.studium.payload.response.StudentRespond;
import com.studium.payload.response.TestGPA;
import com.studium.payload.response.TestResponse;
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
