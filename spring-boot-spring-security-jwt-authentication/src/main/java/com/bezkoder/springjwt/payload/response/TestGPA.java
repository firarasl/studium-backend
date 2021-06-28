package com.bezkoder.springjwt.payload.response;

import com.bezkoder.springjwt.models.Test;

public class TestGPA {

    private Test test;

    private double grade;

    private String subjectName;
    private boolean isGraded=false;

    public boolean isGraded() {
        return isGraded;
    }

    public void setGraded() {
        isGraded = true;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    @Override
    public String toString() {
        return "TestGPA{" +
                "test=" + test +
                ", grade=" + grade +
                ", subjectName='" + subjectName + '\'' +
                ", isGraded=" + isGraded +
                '}';
    }
}
