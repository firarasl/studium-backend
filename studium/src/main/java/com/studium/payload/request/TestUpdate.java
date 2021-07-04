package com.studium.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TestUpdate {

    @NotNull
    @NotBlank
    private Long testId;
    @NotNull
    @NotBlank
    private String studentName;
    @NotNull
    @NotBlank
    private double grade;


    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
