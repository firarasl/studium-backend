package com.bezkoder.springjwt.payload.response;

import com.bezkoder.springjwt.models.Subject;

import java.util.List;
import java.util.Map;

public class SubjectAndGpaResponse {

    private Map<Double, Subject> subjects;
    private String clazzName;

    public Map<Double, Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Map<Double, Subject> subjects) {
        this.subjects = subjects;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }
}
