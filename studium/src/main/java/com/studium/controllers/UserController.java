package com.studium.controllers;

import com.studium.payload.request.MessageRequest;
import com.studium.payload.request.UserUpdateRequest;
import com.studium.payload.response.MessageResponse;
import com.studium.security.UserDetailsImpl;
import com.studium.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest updateUser
                                            ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();

        userService.updateUser(updateUser, currentUser.getId());

        return ResponseEntity.ok(new MessageResponse("Your data was updated"));
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


    @PostMapping("/send-message")
    public ResponseEntity<?> getUserByUsername(@RequestBody MessageRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();

        userService.sendMessage(request, currentUser.getId());
        return ResponseEntity.ok(new MessageResponse("Message was sent !"));
    }

    @GetMapping("/my-unread-msgs")
    public ResponseEntity<?> getMyUnreadMessages(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();

        ;
        return ResponseEntity.ok().body(userService.getMyUnreadMessages( currentUser.getId()));
    }


    @GetMapping("/my-inbox")
    public ResponseEntity<?> myInbox(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();


        return ResponseEntity.ok().body(userService.myInbox( currentUser.getId()));
    }


    @GetMapping("/open-message")
    public ResponseEntity<?> openMessage(@RequestParam Long messageId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();


        return ResponseEntity.ok().body(userService.openMessage( currentUser.getId(), messageId));
    }

}
