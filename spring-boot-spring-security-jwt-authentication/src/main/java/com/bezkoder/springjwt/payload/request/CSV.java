package com.bezkoder.springjwt.payload.request;

import com.opencsv.bean.CsvBindByName;

import java.sql.Timestamp;

public class CSV {
    @CsvBindByName
    private String testName;

    @CsvBindByName
    private String subjectName;

    @CsvBindByName
    private String studentUsername;

    @CsvBindByName
    private Timestamp date;


    @CsvBindByName
    private double grade;

    public CSV() {
    }

    public CSV(String testName, String subjectName, String studentUsername, double grade) {
        this.testName = testName;
        this.subjectName = subjectName;
        this.studentUsername = studentUsername;
        this.grade = grade;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
