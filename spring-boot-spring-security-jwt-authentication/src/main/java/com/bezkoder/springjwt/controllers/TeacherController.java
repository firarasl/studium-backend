package com.bezkoder.springjwt.controllers;


import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.service.TestService;
import com.bezkoder.springjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    private UserService userService;

    @Autowired
    private TestService testService;

    @GetMapping("/all-students")
    public ResponseEntity<?> getAllStudents(){
        return ResponseEntity.ok(userService.getAllStudents());

    }


//
//    @PostMapping("/add-test")
//    public ResponseEntity<?> addTest(@RequestParam @NotEmpty String name,
//                                     @RequestParam @NotNull Timestamp date){
//    testService.addTest(name, date)
//        return ResponseEntity.ok(testService.());
//
//    }
    @PostMapping("/add-test")
    public ResponseEntity<?> addTest(@RequestParam @NotEmpty @NotNull String name,
                                     @RequestParam @NotEmpty @NotNull String date){


        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(date);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            testService.addTest(name, timestamp);
            return ResponseEntity.ok(new MessageResponse("Added new test successfully!"));

        } catch(Exception e) {
            return new ResponseEntity<>(
                    "Not proper date format",
                    HttpStatus.BAD_REQUEST);
        }

    }


    @PostMapping("/update-test")
    public ResponseEntity<?> updateTest(@RequestParam @NotEmpty @NotNull String name,
                                        @RequestParam @NotEmpty @NotNull Long id,
                                     @RequestParam @NotEmpty @NotNull String date){


        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(date);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            testService.updateTest(id,name, timestamp);
            return ResponseEntity.ok(new MessageResponse("Updated test successfully!"));

        } catch(Exception e) {
            return new ResponseEntity<>(
                    "Not proper date format",
                    HttpStatus.BAD_REQUEST);
        }

    }




    @GetMapping("/all-students-test")
    public ResponseEntity<?> getAllStudentsOfTest(@RequestParam @NotEmpty @NotNull Long id){
        return ResponseEntity.ok(testService.getAllStudentsOfTest(id));
    }
}
