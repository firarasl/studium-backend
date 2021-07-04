package com.studium.models;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(	name = "clazz",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")}
)

public class Clazz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50, min = 3)
    private String name;


    @ManyToMany()
    @JoinTable(	name = "clazz_subject",
            joinColumns = @JoinColumn(name = "clazz_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects = new ArrayList<>();







    public Clazz(@NotBlank @Size(max = 50) String name) {
        this.name = name;
    }

    public Clazz(Long id, @NotBlank @Size(max = 50) String name) {
        this.id = id;
        this.name = name;
    }

    public Clazz() {
    }

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

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }


    @Override
    public String toString() {
        return "Clazz{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", subjects=" + subjects +
                '}';
    }
}