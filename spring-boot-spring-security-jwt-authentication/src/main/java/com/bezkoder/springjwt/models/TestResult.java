package com.bezkoder.springjwt.models;

import javax.persistence.*;

@Entity
@Table(	name = "tests_result")

public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne(cascade = CascadeType.REMOVE)
    private Test test;


    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private User user;


    @Column(name="grade")
    private int grade;


}
