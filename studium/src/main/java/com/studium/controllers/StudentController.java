package com.studium.controllers;


import com.studium.security.UserDetailsImpl;
import com.studium.service.SubjectService;
import com.studium.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private TestService testService;

    @GetMapping("my-subjects-gpa")
    public ResponseEntity<?> getSubjectsGpa(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(subjectService.mySubjectsAndGpa(currentUser.getId()));
    }



    @GetMapping("my-tests-grade")
    public ResponseEntity<?> getTestGpa(@RequestParam Long subjectId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(testService.myTestsAndGpa(currentUser.getId(), subjectId));
    }


}
