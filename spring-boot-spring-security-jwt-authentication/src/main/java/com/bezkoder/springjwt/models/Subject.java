package com.bezkoder.springjwt.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(	name = "subject")

public class Subject {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String name;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "teacher_id")
    private User user;

    @Column(name="is_archieved")
    private boolean isArchieved;



//    @ManyToMany(fetch = FetchType.LAZY)
//	@JoinTable(	name = "class_subject",
//				joinColumns = @JoinColumn(name = "subject_id"),
//				inverseJoinColumns = @JoinColumn(name = "class_id"))
//	private List<Clazz> clazzes = new ArrayList<>();



}
