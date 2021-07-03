package com.bezkoder.springjwt.payload.response;

public class SubjectResponse {

    private Long id;

    private Boolean archieved;

    private String name;

    private String clazzName;

    private String teacherName;

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getArchieved() {
        return archieved;
    }

    public void setArchieved(Boolean archieved) {
        this.archieved = archieved;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }
}
