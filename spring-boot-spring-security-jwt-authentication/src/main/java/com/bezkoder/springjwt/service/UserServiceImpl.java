package com.bezkoder.springjwt.service;


import com.bezkoder.springjwt.helper.StudentDataUtil;
import com.bezkoder.springjwt.models.*;
import com.bezkoder.springjwt.payload.request.UserUpdateRequest;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.payload.response.MyProfile;
import com.bezkoder.springjwt.payload.response.StudentRespond;
import com.bezkoder.springjwt.repository.SubjectRepository;
import com.bezkoder.springjwt.repository.TestResultRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;

@Service
public class UserServiceImpl implements UserService{

@Autowired
private UserRepository userRepository;


    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private TestResultRepository testResultRepository;


    @Override
    @Transactional
    public List<User> getAllUsers() {
        List<User> userList = (List<User>) Hibernate.unproxy(userRepository.findAll());
        return userList;
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        Optional<User> user= userRepository.findByUsername(username);
        if(!user.isPresent()){
            throw new NoSuchElementException("this username doesnt exist!");
        }else{
            return user;
        }
    }

    @Autowired
    public StudentDataUtil helper;

    @Override
    @Transactional
    public void saveUser(User user) {

        Role role = entityManager.getReference(Role.class, user.getRole().getId());
        user.setRole(role);
        entityManager.persist(user);

    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> currentUser = userRepository.findById(id);
        if(!currentUser.isPresent()){
            throw new NoSuchElementException("this user doesnt exist!");
        }

        if (currentUser.get().getRole().getName() == ERole.ROLE_TEACHER){
            if (subjectRepository.findAllByTeacherIdAndArchieved(id, false).size()>0){
                throw new IllegalArgumentException("this teacher has assigned active subjects");
            }

            List<Subject> teachersSubjects = (List<Subject>) Hibernate.unproxy(subjectRepository.findAllByUser(currentUser.get()));
            for (int i = 0; i < teachersSubjects.size(); i++) {
                teachersSubjects.get(i).setUser(null);
            }
        }else if(currentUser.get().getRole().getName() == ERole.ROLE_STUDENT){
            List<TestResult> testResultList = (List<TestResult>) Hibernate.unproxy(testResultRepository.findAllByUser(currentUser.get()));
            testResultRepository.deleteAll(testResultList);
        }

        userRepository.deleteById(id);
    }

//    @Override
//    public void updateMyUser(UserUpdateRequest updateUser, Long currentId) {
//
//        Optional<User> optionalUser = (Optional<User>) Hibernate.unproxy(userRepository.findById(currentId));
//        if(!optionalUser.isPresent()){
//            throw new NoSuchElementException("this username doesnt exist!");
//        }
//
//        if (!updateUser.getUsername().isEmpty() &&
//                updateUser.getUsername()!=null &&
//                !updateUser.getUsername().equals(optionalUser.get().getUsername())){
//            optionalUser.get().setUsername(updateUser.getUsername());
//        }
//        if (!updateUser.getFirstname().isEmpty() &&
//                updateUser.getFirstname()!=null &&
//                !updateUser.getFirstname().equals(optionalUser.get().getFirstname())){
//            optionalUser.get().setFirstname(updateUser.getFirstname());
//        }
//        if (!updateUser.getLastname().isEmpty() &&
//                updateUser.getLastname()!=null &&
//                !updateUser.getLastname().equals(optionalUser.get().getLastname())){
//            optionalUser.get().setLastname(updateUser.getLastname());
//        }
//
//        if (!updateUser.getPassword().isEmpty() &&
//                updateUser.getPassword()!=null &&
//                !updateUser.getLastname().equals(optionalUser.get().getLastname())){
//            optionalUser.get().setLastname(updateUser.getLastname());
//        }
//
//        if (updateUser.getId()==currentUser.getId()){
//            userRepository.save(updateUser);
//        }
//        else {
//            throw new IllegalArgumentException("you're trying to update someone else's data");
//        }
//    }

    @Override
    public void updateUser(UserUpdateRequest updateRequest, Long userId) {
        Optional<User> currentUser = (Optional<User>) Hibernate.unproxy(userRepository.findById(userId));
        if(!currentUser.isPresent()){
            throw new NoSuchElementException("this user doesnt exist!");
        }

        if(updateRequest.getFirstname() == null && updateRequest.getLastname() == null
              && updateRequest.getUsername() == null && updateRequest.getPassword() == null
        ){
            throw new IllegalArgumentException("Error: Edit something");
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (updateRequest.getFirstname() != null
                && !updateRequest.getFirstname().equals(currentUser.get().getFirstname())
        ) {

            currentUser.get().setFirstname(updateRequest.getFirstname());
        }

        if (updateRequest.getLastname() != null && !updateRequest.getLastname().equals(currentUser.get().getLastname())
        ) {

            currentUser.get().setLastname(updateRequest.getLastname());
        }

        if (updateRequest.getUsername() != null && !updateRequest.getUsername().equals(currentUser.get().getUsername())
        ) {
            if (userRepository.existsByUsername(updateRequest.getUsername())) {
                throw new IllegalArgumentException("Error: Username is already taken");
            }

            currentUser.get().setUsername(updateRequest.getUsername());
        }

        if (updateRequest.getPassword() != null && !passwordEncoder.matches(currentUser.get().getPassword(),
                updateRequest.getPassword())
        ) {

            currentUser.get().setPassword(encoder.encode(updateRequest.getUsername()));
        }
        userRepository.save(currentUser.get());

    }

    @Override
    public List<StudentRespond> getAllStudents() {

        List<User> userList = (List<User>) Hibernate.unproxy(userRepository.findAll());
        List<StudentRespond> studentGPAlist = new ArrayList<>();

        double gpa=0;

        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getRole().getName().equals(ERole.ROLE_STUDENT)){
                gpa= helper.getGPA((List<TestResult>) Hibernate.unproxy(testResultRepository.findAllByUser(userList.get(i))));
                StudentRespond studentRespond = helper.getPayload(userList.get(i), gpa);

                studentGPAlist.add(studentRespond);
            }
        }

        return studentGPAlist;
    }

    @Override
    public User getMyData(Long id) {
        Optional<User> user= userRepository.findById(id);
        if(!user.isPresent()){
            throw new NoSuchElementException("this username doesnt exist!");
        }

//        MyProfile myProfile = new MyProfile();
//        myProfile.setFirstname(user.get().getFirstname());
//        myProfile.setLastname(user.get().getLastname());
//        myProfile.setUsername(user.get().getUsername());
//        myProfile.setId(user.get().getId());
//        myProfile.setRole(user.get().getRole().getName());


        return user.get();
    }


}
