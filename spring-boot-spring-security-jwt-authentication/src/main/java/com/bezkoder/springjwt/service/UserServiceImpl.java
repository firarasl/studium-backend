package com.bezkoder.springjwt.service;


import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.Subject;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.UserUpdateRequest;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.repository.SubjectRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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



    @Override
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
            throw new NoSuchElementException("this username doesnt exist!");
        }

        if (currentUser.get().getRole().getName() == ERole.ROLE_TEACHER){
            if (subjectRepository.findAllByTeacherIdAndArchieved(id, true).size()!=0){
                throw new IllegalArgumentException("this user has assigned active subjects");
            }
        }

        userRepository.deleteById(id);
    }

    @Override
    public void updateMyUser(User updateUser, User currentUser) {
        if (updateUser.getId()==currentUser.getId()){
            userRepository.save(updateUser);
        }
        else {
            throw new IllegalArgumentException("you're trying to update someone else's data");
        }
    }

    @Override
    public void updateUser(UserUpdateRequest updateRequest, Long userId) {
        Optional<User> currentUser = (Optional<User>) Hibernate.unproxy(userRepository.findById(userId));
        if(!currentUser.isPresent()){
            throw new NoSuchElementException("this username doesnt exist!");
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("1");
        if (!updateRequest.getFirstname().equals(currentUser.get().getFirstname())
                && !updateRequest.getFirstname().isEmpty() && updateRequest.getFirstname() != null
        ) {
            System.out.println("2");

            currentUser.get().setFirstname(updateRequest.getFirstname());
        }

        if (!updateRequest.getLastname().equals(currentUser.get().getLastname())
                && !updateRequest.getLastname().isEmpty() && updateRequest.getFirstname() != null
        ) {
            System.out.println("3");

            currentUser.get().setLastname(updateRequest.getLastname());
        }

        if (!updateRequest.getUsername().equals(currentUser.get().getUsername())
                && !updateRequest.getUsername().isEmpty() && updateRequest.getFirstname() != null
        ) {
            if (userRepository.existsByUsername(updateRequest.getUsername())) {
                throw new IllegalArgumentException("Error: Username is already taken");
            }
            System.out.println("4");

            currentUser.get().setUsername(updateRequest.getUsername());
        }

        if (!passwordEncoder.matches(currentUser.get().getPassword(),
                updateRequest.getPassword())
                && !updateRequest.getPassword().isEmpty() && updateRequest.getFirstname() != null
        ) {
            System.out.println("5");

            currentUser.get().setPassword(encoder.encode(updateRequest.getUsername()));
        }
        System.out.println("--------------------------------");

        System.out.println(currentUser.get());
        userRepository.save(currentUser.get());

    }


}
