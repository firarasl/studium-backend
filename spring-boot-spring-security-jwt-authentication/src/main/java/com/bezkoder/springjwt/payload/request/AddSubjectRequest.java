package com.bezkoder.springjwt.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddSubjectRequest {
    @NotBlank
    @NotNull
    @Size(min = 3)
    private String name;
    @NotBlank
    @NotNull
    @Size(min = 3)
    private String teacherName;
    @NotBlank
    @NotNull
    @Size(min = 3)
    private String clazzName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }
}
