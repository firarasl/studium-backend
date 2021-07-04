package com.studium.payload.request;



public class SubjectUpdateRequest {
    private Long id;

    private String name;


    private String teacherName;


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


    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    @Override
    public String toString() {
        return "SubjectUpdateRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teacher_name=" + teacherName +
                '}';
    }
}
