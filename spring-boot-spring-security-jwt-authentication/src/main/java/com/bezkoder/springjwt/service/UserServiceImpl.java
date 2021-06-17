package com.bezkoder.springjwt.service;


import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Subject;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.repository.SubjectRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

@Autowired
private UserRepository userRepository;


    @Autowired
    private SubjectRepository subjectRepository;


    @Override
    public List<User> getAllUsers() {

        List<User> userList = userRepository.findAll();
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
    public void saveUser(User user) {
        userRepository.save(user);
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
}
