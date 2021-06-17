package com.bezkoder.springjwt.models;


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
    @Size(max = 50)
    private String name;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinTable(	name = "clazz_subject",
            joinColumns = @JoinColumn(name = "clazz_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects = new ArrayList<>();



    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinTable(	name = "clazz_test",
            joinColumns = @JoinColumn(name = "clazz_id"),
            inverseJoinColumns = @JoinColumn(name = "tests_id"))
    private List<Test> tests = new ArrayList<>();


    public Clazz(@NotBlank @Size(max = 50) String name) {
        this.name = name;
    }

    public Clazz(Long id, @NotBlank @Size(max = 50) String name) {
        this.id = id;
        this.name = name;
    }
}
