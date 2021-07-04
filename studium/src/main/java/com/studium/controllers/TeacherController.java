package com.studium.controllers;


import com.studium.payload.request.AddTestRequest;
import com.studium.payload.request.TestUpdate;
import com.studium.payload.response.MessageResponse;
import com.studium.security.UserDetailsImpl;
import com.studium.service.SubjectService;
import com.studium.service.TestService;
import com.studium.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/all-students")
    public ResponseEntity<?> getAllStudents(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(userService.getAllTeachersStudents(currentUser.getId()));

    }


    @GetMapping("all-teachers-subjects")
    public ResponseEntity<?> getAllTeachersSubjects(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();


        return ResponseEntity.ok(subjectService.getAllSubejectsByTeacher(currentUser.getId()));
    }



//    ----------------------- tests ----------------------------------

    @PostMapping("/add-test")
    public ResponseEntity<?> addTest(@RequestBody AddTestRequest request
                                     ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        Timestamp timestamp;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date parsedDate = dateFormat.parse(request.getDate());
             timestamp = new java.sql.Timestamp(parsedDate.getTime());
        } catch(Exception e) {
            return new ResponseEntity<>(new MessageResponse(
                    "Not proper date format"),
                    HttpStatus.BAD_REQUEST);
        }
        testService.addTest(request.getName(), timestamp, request.getSubjectName(), currentUser.getId(), request.getClazzName());
        return ResponseEntity.ok(new MessageResponse("Added new test successfully!"));


    }


    @GetMapping("/all-tests")
    public ResponseEntity<?> getAllTeachersTest(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(testService.getAllTestsOfTeacher(currentUser.getId()));

    }

    @PutMapping("/update-test")
    public ResponseEntity<?> updateTest(@RequestParam  String name,
                                        @RequestParam @NotEmpty @NotNull Long id,
                                     @RequestParam  String date){

        Timestamp timestamp = null;
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            if (date !=null && !date.isEmpty()){
                Date parsedDate = dateFormat.parse(date);
                timestamp = new java.sql.Timestamp(parsedDate.getTime());
            }

        } catch(Exception e) {
            return new ResponseEntity<>(
                    "Not proper date format",
                    HttpStatus.BAD_REQUEST);
        }

        testService.updateTest(id,name, timestamp);
        return ResponseEntity.ok(new MessageResponse("Updated test successfully!"));

    }




//    @GetMapping("/all-students-test")
//    public ResponseEntity<?> getAllStudentsOfTest(@RequestParam @NotEmpty @NotNull Long id){
//        return ResponseEntity.ok(testService.getAllStudentsOfTest(id));
//    }



    @GetMapping("/test/{id}")
    public ResponseEntity<?> getTestById(@PathVariable Long id){
        return ResponseEntity.ok(testService.findById(id));
    }


    @PutMapping("change-test-grade")
    public ResponseEntity<?> changeTestGrade(@RequestBody TestUpdate request){
        String response = testService.changeTestGrade(request.getTestId(), request.getStudentName(), request.getGrade());
        return ResponseEntity.ok(new MessageResponse(response));
    }


    @PostMapping("/upload-csv-file")
    public ResponseEntity<?> uploadCSVFile(@RequestParam(name = "file")  MultipartFile file) {

        testService.parseCSV(file);

        return ResponseEntity.ok(new MessageResponse("File uploaded !"));

    }


    @DeleteMapping("delete-test")
    public ResponseEntity<?> deleteTest(@RequestParam @NotEmpty @NotNull Long testId){
        testService.deleteTest(testId);
        return ResponseEntity.ok(new MessageResponse("Deleted the test!"));
    }


}
