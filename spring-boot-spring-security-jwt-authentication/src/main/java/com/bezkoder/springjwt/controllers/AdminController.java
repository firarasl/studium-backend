package com.bezkoder.springjwt.controllers;


import com.bezkoder.springjwt.models.Clazz;
import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.ManageClazzRequest;
import com.bezkoder.springjwt.payload.request.SignupRequest;
import com.bezkoder.springjwt.payload.request.SubjectUpdateRequest;
import com.bezkoder.springjwt.payload.request.UserUpdateRequest;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.service.ClazzService;
import com.bezkoder.springjwt.service.SubjectService;
import com.bezkoder.springjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.ok(userService.getAllUsers());

    }

    @PostMapping("/add-user")
    public ResponseEntity<?> addUser(@RequestBody SignupRequest userRequest){
        System.out.println(userRequest);

        if (userRepository.existsByUsername(userRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }


        Role roles = null;

        if (userRequest.getRole() == null) {

            Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles =userRole;
        } else {

            switch (userRequest.getRole()) {
                case "admin":

                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles =adminRole;

                    break;
                case "teacher":
                    Role modRole = roleRepository.findByName(ERole.ROLE_TEACHER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles=modRole;

                    break;
                default:
                    Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles=userRole;
            }
        }


        User userObject = new User(userRequest.getUsername(),
                userRequest.getFirstname(),
                userRequest.getLastname(),
                encoder.encode(userRequest.getPassword()));

        userObject.setRole(roles);



        userService.saveUser(userObject);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PutMapping("/update-user")
public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest updateRequest,
                                          @RequestParam Long userId){
        userService.updateUser(updateRequest, userId);
        return ResponseEntity.ok(new MessageResponse("User changed successfully!"));
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
    public ResponseEntity<?> addClazz(@RequestParam String clazzname){
        clazzService.saveClazz(clazzname);
        return ResponseEntity.ok(new MessageResponse("added a new class!"));

    }


    @PutMapping("/update-clazzName")
    public ResponseEntity<?> updateClazzName(@RequestParam Long id, String name){
        clazzService.updateClazzName(id, name);
        return ResponseEntity.ok(new MessageResponse("Changed a class name!"));
    }

    //TODO manage subjects

    @PutMapping("/manage-class-subjects")
    public ResponseEntity<?> manageClazzSubjects(@RequestBody ManageClazzRequest request){
        clazzService.manageClazzSubjects(request.getClazzId(), request.getSubjectIds());
        return ResponseEntity.ok(new MessageResponse("Assigned subjects for the class!"));
    }


    @PutMapping("/update-student-toclazz")
    public ResponseEntity<?> updateStudentClazz(@RequestParam Long clazzId,
                                                   @RequestParam Long studentId){
        String response = clazzService.updateStudentToClazz(clazzId, studentId);
        return ResponseEntity.ok(new MessageResponse(response));
    }



    @DeleteMapping("/delete-class")
    public ResponseEntity<?> deleteClazz(@RequestParam Long clazzId){
        clazzService.deleteClazz(clazzId);
        return ResponseEntity.ok(new MessageResponse("deleted a new class!"));
    }



    //---------------------------- subjects ----------------------------------------


    @GetMapping("/all-subjects")
    public ResponseEntity<?> getAllSubjects(){
        return ResponseEntity.ok(subjectService.getAllSubejects());
    }



    @PostMapping("/add-subject")
    public ResponseEntity<?> addSubject(@RequestParam String name,
                                        @RequestParam Long teacherId){
        subjectService.saveSubject(name, teacherId);
        return ResponseEntity.ok(new MessageResponse("added a new subject!"));
    }



    @PutMapping("/update-subject")
    public ResponseEntity<?> updateSubject(@RequestBody SubjectUpdateRequest subjectUpdateRequest){
        System.out.println(subjectUpdateRequest);
        subjectService.updateSubject(subjectUpdateRequest);
        return ResponseEntity.ok(new MessageResponse("subject was updated !"));
    }




    @PutMapping("/archieve-subject")
    public ResponseEntity<?> archieveSubject(@RequestParam Long id){

        subjectService.archieveSubject(id);
        return ResponseEntity.ok(new MessageResponse("archieved the subject !"));
    }

    @DeleteMapping("/delete-subject")
    public ResponseEntity<?> deleteSubject(@RequestParam Long id){
        subjectService.deleteSubject(id);
        return ResponseEntity.ok(new MessageResponse("subject deleted !"));
    }
}
