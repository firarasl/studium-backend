package com.studium.payload.response;

import com.studium.models.Subject;

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
