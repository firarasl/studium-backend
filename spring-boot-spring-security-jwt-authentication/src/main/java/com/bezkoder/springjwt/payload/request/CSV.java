package com.bezkoder.springjwt.payload.request;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class CSV {
    @NotEmpty
    @NotNull
    @CsvBindByPosition(position = 0)
    private String username;

    @NotEmpty
    @NotNull

    @CsvBindByPosition(position = 1)
    private Long testId;

    @NotEmpty
    @NotNull
    @CsvBindByPosition(position = 2)
    private double grade;

    public CSV() {
    }

    public CSV(String username, Long testId, double grade) {
        this.username = username;
        this.testId = testId;
        this.grade = grade;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "CSV{" +
                "testId=" + testId +
                ", username='" + username + '\'' +
                ", grade=" + grade +
                '}';
    }
}
