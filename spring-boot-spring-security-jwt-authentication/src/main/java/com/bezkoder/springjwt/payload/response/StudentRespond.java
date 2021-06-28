package com.bezkoder.springjwt.payload.response;

import com.bezkoder.springjwt.models.ERole;

public class StudentRespond {

    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private ERole role;
    private String className;
    private double gpa;


    public Long getId() {
        return id;
    }

    public StudentRespond() {
    }

    public StudentRespond(Long id, String username, String firstname, String lastname, ERole role, String className, double gpa) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.className = className;
        this.gpa = gpa;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public ERole getRole() {
        return role;
    }

    public void setRole(ERole role) {
        this.role = role;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }


    @Override
    public String toString() {
        return "StudentRespond{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", role=" + role +
                ", className='" + className + '\'' +
                ", gpa=" + gpa +
                '}';
    }
}
