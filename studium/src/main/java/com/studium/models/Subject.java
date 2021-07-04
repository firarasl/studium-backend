package com.studium.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "subject")

public class Subject {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50, min = 3)
    private String name;


    @ManyToOne()
    @JoinColumn(name = "teacher_id")
    private User user;

    @Column(name="is_archieved",columnDefinition = "boolean default false")
    private Boolean isArchieved = false;



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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean isArchieved() {
        return isArchieved;
    }

    public void setArchieved(Boolean archieved) {
        if (isArchieved){
            throw new IllegalArgumentException("Subject is already archieved");
        }
        isArchieved = archieved;
    }


}
