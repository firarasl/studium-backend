package com.bezkoder.springjwt.controllers;


import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.response.SubjectAndGpaResponse;
import com.bezkoder.springjwt.security.UserDetailsImpl;
import com.bezkoder.springjwt.service.SubjectService;
import com.bezkoder.springjwt.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


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
