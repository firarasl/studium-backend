package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    Optional<User> getUserByUsername(String username);

    void saveUser(User user);

    void deleteUser(Long id);

    void updateMyUser(User updateUser, User currentUser);
}
