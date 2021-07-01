package com.bezkoder.springjwt.payload.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AddTestRequest {

    @NotEmpty
    @NotNull
    private String name;

    @NotEmpty
    @NotNull
    private String date;


    @NotEmpty
    @NotNull
    private String subjectName;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
