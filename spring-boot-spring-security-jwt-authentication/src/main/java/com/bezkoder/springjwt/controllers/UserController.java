package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")

public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping("/user-info/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username){
        return ResponseEntity.ok(userService.getUserByUsername(username));

    }
}
