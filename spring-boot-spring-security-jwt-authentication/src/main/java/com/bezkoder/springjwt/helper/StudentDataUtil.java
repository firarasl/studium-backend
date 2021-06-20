package com.bezkoder.springjwt.helper;

import com.bezkoder.springjwt.models.TestResult;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.response.StudentRespond;

import java.util.List;

public class StudentDataUtil {

    public StudentRespond getPayload(User user, double gpa){
        StudentRespond studentRespond = new StudentRespond();
        studentRespond.setId(user.getId());
        studentRespond.setFirstname(user.getFirstname());
        studentRespond.setLastname(user.getLastname());
        studentRespond.setGpa(gpa);
        studentRespond.setRole(user.getRole().getName());
        studentRespond.setUsername(user.getUsername());
        if (user.getClazz()!=null){
            studentRespond.setClassName(user.getClazz().getName());
        }

        return studentRespond;
    }


    public double getGPA(List<TestResult> testResults){
        double gpa=0;

        for (int i = 0; i < testResults.size(); i++) {
            gpa+=testResults.get(i).getGrade();
        }

        if (testResults.size()!=0){
            gpa = gpa/testResults.size();
        }
        return gpa;
    }
}
