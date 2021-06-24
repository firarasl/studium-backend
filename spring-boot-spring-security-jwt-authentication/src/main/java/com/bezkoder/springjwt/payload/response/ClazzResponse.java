package com.bezkoder.springjwt.payload.response;

import com.bezkoder.springjwt.models.Subject;
import com.bezkoder.springjwt.models.User;

import java.util.List;

public class ClazzResponse {

    private Long id;
    private String name;

    private List<Subject> subjectList;

    private List<User> studentList;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    public List<User> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<User> studentList) {
        this.studentList = studentList;
    }
}
