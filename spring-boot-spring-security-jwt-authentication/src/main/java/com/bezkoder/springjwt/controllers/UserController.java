package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.UserUpdateRequest;
import com.bezkoder.springjwt.security.UserDetailsImpl;
import com.bezkoder.springjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")

public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping("/user-info/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username){
        return ResponseEntity.ok(userService.getUserByUsername(username));

    }


    @PutMapping("/update-myself")
    public ResponseEntity<Void> updateUser(@RequestBody UserUpdateRequest updateUser
                                            ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();

        System.out.println(updateUser);
        userService.updateUser(updateUser, currentUser.getId());

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/my-data")
    public ResponseEntity<?> getMyData(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();

//        return ResponseEntity.ok(userService.getMyData(currentUser.getId()))
//                .cacheControl(CacheControl.noCache().cachePrivate());
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noCache().cachePrivate())
                .body(userService.getMyData(currentUser.getId()));
    }


}
