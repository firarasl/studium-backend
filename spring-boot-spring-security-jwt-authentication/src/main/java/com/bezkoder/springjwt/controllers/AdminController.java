package com.bezkoder.springjwt.controllers;


import com.bezkoder.springjwt.models.Clazz;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.service.ClazzService;
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
    private ClazzService clazzService;

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
        userService.saveUser(user);

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

    @PutMapping("/update-student-toclazz")
    public ResponseEntity<Void> updateClazzName(@RequestParam Long clazzId, Long studentId){
        clazzService.updateStudentToClazz(clazzId, studentId);
        return ResponseEntity.noContent().build();
    }



}
