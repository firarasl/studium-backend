package com.bezkoder.springjwt.payload.response;

import com.bezkoder.springjwt.models.Subject;

import java.util.List;

public class SubjectAndGpaResponse {

    private List<String> subjectName;
    private String clazzName;
    private double gpa;


    public List<String> getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(List<String> subjectName) {
        this.subjectName = subjectName;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

}
