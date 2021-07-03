package com.bezkoder.springjwt.controllers;


import com.bezkoder.springjwt.models.Clazz;
import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.*;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.UserDetailsImpl;
import com.bezkoder.springjwt.service.ClazzService;
import com.bezkoder.springjwt.service.SubjectService;
import com.bezkoder.springjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Arrays;
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
    PasswordEncoder encoder;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    RoleRepository roleRepository;

//---------------------------- users ----------------------------------------

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(userService.getAllUsers(currentUser.getId()));

    }

    @PostMapping("/add-user")
    public ResponseEntity<?> addUser(@RequestBody SignupRequest userRequest){
        userService.saveUser(userRequest);
        return ResponseEntity.ok(new MessageResponse("User was added successfully!"));
    }

    @PutMapping("/update-user")
public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest updateRequest
                                          ){
        userService.updateUser(updateRequest, updateRequest.getId());
        return ResponseEntity.ok(new MessageResponse("User changed successfully!"));
    }


    @DeleteMapping("delete-user")
    public ResponseEntity<?> deleteUser(@RequestParam Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok(new MessageResponse("User was deleted successfully!"));
    }


//---------------------------- classes ----------------------------------------


    @GetMapping("/all-clazzes")
    public ResponseEntity<?> getAllClazzes(){
        return ResponseEntity.ok(clazzService.getAllClazzes());
    }

    @GetMapping("/clazz/{id}")
    public ResponseEntity<?> getClazzById(@PathVariable Long id){
        return ResponseEntity.ok(clazzService.getClazzById(id));
    }

    @PostMapping("/add-clazz")
    public ResponseEntity<?> addClazz(@RequestParam String clazzname){
        clazzService.saveClazz(clazzname);
        return ResponseEntity.ok(new MessageResponse("Added a new class!"));

    }


    @PutMapping("/update-clazzName")
    public ResponseEntity<?> updateClazzName(@RequestParam Long id,
                                             @RequestParam String name){
        clazzService.updateClazzName(id, name);
        return ResponseEntity.ok(new MessageResponse("You changed a class name!"));
    }

    //TODO manage subjects

    @PutMapping("/manage-class-subjects")
    public ResponseEntity<?> manageClazzSubjects(@RequestBody ManageClazzRequest request){
        clazzService.manageClazzSubjects(request.getClazzId(), request.getSubjectIds());
        return ResponseEntity.ok(new MessageResponse("Assigned subjects for the class!"));
    }


    @PutMapping("/update-student-toclazz")
    public ResponseEntity<?> updateStudentClazz(@RequestParam Long clazzId,
                                                   @RequestParam String username){
        String response = clazzService.updateStudentToClazz(clazzId, username);
        return ResponseEntity.ok(new MessageResponse(response));
    }



    @DeleteMapping("/delete-class")
    public ResponseEntity<?> deleteClazz(@RequestParam Long clazzId){
        clazzService.deleteClazz(clazzId);
        return ResponseEntity.ok(new MessageResponse("Deleted a new class!"));
    }



    //---------------------------- subjects ----------------------------------------


    @GetMapping("/all-subjects")
    public ResponseEntity<?> getAllSubjects(){
        return ResponseEntity.ok(subjectService.getAllSubejects());
    }



    @PostMapping("/add-subject")
    public ResponseEntity<?> addSubject(@RequestBody AddSubjectRequest request){
        subjectService.saveSubject(request.getName(), request.getTeacherName(), request.getClazzName());
        return ResponseEntity.ok(new MessageResponse("Added a new subject!"));
    }



    @PutMapping("/update-subject")
    public ResponseEntity<?> updateSubject(@RequestBody SubjectUpdateRequest subjectUpdateRequest){
        subjectService.updateSubject(subjectUpdateRequest);
        return ResponseEntity.ok(new MessageResponse("Subject was updated !"));
    }

//    @PutMapping("/subject-assign-clazz")
//    public ResponseEntity<?> updateSubject(@RequestParam Long id, String clazzName){
//        subjectService.assignClazz(id, clazzName);
//        return ResponseEntity.ok(new MessageResponse("Subject was assigned to a class !"));
//    }



    @PutMapping("/archieve-subject")
    public ResponseEntity<?> archieveSubject(@RequestParam Long id){

        subjectService.archieveSubject(id);
        return ResponseEntity.ok(new MessageResponse("Archieved the subject !"));
    }

    @DeleteMapping("/delete-subject")
    public ResponseEntity<?> deleteSubject(@RequestParam Long id){
        subjectService.deleteSubject(id);
        return ResponseEntity.ok(new MessageResponse("Subject deleted !"));
    }

    @GetMapping("/subject/{id}")
    public ResponseEntity<?> getSubjectById(@PathVariable Long id){
        return ResponseEntity.ok(subjectService.getSubjectById(id));
    }
}
