package com.bezkoder.springjwt.models;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(	name = "tests")

public class Test {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String name;


    private Timestamp date;






    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Subject subject;

}
