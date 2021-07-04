package com.studium.payload.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddTestRequest {

    @NotEmpty
    @NotNull
    @Size(min=3)
    private String name;

    @NotEmpty
    @NotNull
    private String date;


    @NotEmpty
    @NotNull
    @Size(min=3)
    private String subjectName;

    @NotEmpty
    @NotNull
    @Size(min=3)
    private String clazzName;


    public String getName() {
        return name;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
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
