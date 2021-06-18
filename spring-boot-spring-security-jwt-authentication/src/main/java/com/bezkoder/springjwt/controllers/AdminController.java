package com.bezkoder.springjwt.controllers;


import com.bezkoder.springjwt.models.Clazz;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.SubjectUpdateRequest;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.service.ClazzService;
import com.bezkoder.springjwt.service.SubjectService;
import com.bezkoder.springjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClazzService clazzService;

    @Autowired
    private SubjectService subjectService;


//---------------------------- users ----------------------------------------

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());

    }

    @PostMapping("/add-user")
    public ResponseEntity<Void> addUser(@RequestParam User user){
        userService.saveUser(user);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-user")
    public ResponseEntity<Void> updateUser(@RequestParam User user){
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("delete-user")
    public ResponseEntity<Void> deleteUser(@RequestParam Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


//---------------------------- classes ----------------------------------------


    @GetMapping("/all-clazzes")
    public ResponseEntity<?> getAllClazzes(){
        return ResponseEntity.ok(clazzService.getAllClazzes());
    }



    @PostMapping("/add-clazz")
    public ResponseEntity<Void> addClazz(@RequestParam String clazzname){
        clazzService.saveClazz(clazzname);

        return ResponseEntity.noContent().build();
    }


    @PutMapping("/update-clazzName")
    public ResponseEntity<Void> updateClazzName(@RequestParam Long id, String name){
        clazzService.updateClazzName(id, name);
        return ResponseEntity.noContent().build();
    }

    //TODO manage subjects

    @PostMapping("/manage-class-subjects")
    public ResponseEntity<Void> manageClazzSubjects(@RequestParam Long clazzId, List<Long> subjectIds){
        clazzService.manageClazzSubjects(clazzId, subjectIds);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/update-student-toclazz")
    public ResponseEntity<Void> updateClazzName(@RequestParam Long clazzId, Long studentId){
        clazzService.updateStudentToClazz(clazzId, studentId);
        return ResponseEntity.noContent().build();
    }



    @DeleteMapping("/delete-class")
    public ResponseEntity<Void> deleteClazz(@RequestParam Long clazzId){
        clazzService.deleteClazz(clazzId);
        return ResponseEntity.noContent().build();
    }



    //---------------------------- subjects ----------------------------------------


    @GetMapping("/all-subjects")
    public ResponseEntity<?> getAllSubjects(){
        return ResponseEntity.ok(subjectService.getAllSubejects());
    }



    @PostMapping("/add-subject")
    public ResponseEntity<Void> addSubject(@RequestParam String name, Long teacherId){
        subjectService.saveSubject(name, teacherId);
        return ResponseEntity.noContent().build();
    }



    @PutMapping("/update-subject")
    public ResponseEntity<Void> updateSubject(@RequestParam SubjectUpdateRequest subjectUpdateRequest){
        subjectService.updateSubject(subjectUpdateRequest);
        return ResponseEntity.noContent().build();
    }




    @PutMapping("/archieve-subject")
    public ResponseEntity<Void> archieveSubject(@RequestParam Long id){

        subjectService.archieveSubject(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-subject")
    public ResponseEntity<Void> deleteSubject(@RequestParam Long id){
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }
}
