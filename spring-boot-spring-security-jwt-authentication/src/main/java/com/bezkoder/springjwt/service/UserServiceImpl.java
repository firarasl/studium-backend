package com.bezkoder.springjwt.service;


import com.bezkoder.springjwt.models.User;
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
}
