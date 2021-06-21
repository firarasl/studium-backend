package com.bezkoder.springjwt.payload.response;

public class TestGPA {

    private String testName;

    private double gpa;

    private String subjectName;
    private boolean isGraded=false;

    public boolean isGraded() {
        return isGraded;
    }

    public void setGraded() {
        isGraded = true;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }
}
