package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.UserUpdateRequest;
import com.bezkoder.springjwt.payload.response.MyProfile;
import com.bezkoder.springjwt.payload.response.StudentRespond;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    Optional<User> getUserByUsername(String username);

    void saveUser(User user);

    void deleteUser(Long id);

//    void updateMyUser(UserUpdateRequest updateUser, Long currentId);

    void updateUser(UserUpdateRequest updateRequest, Long userId);

    List<StudentRespond> getAllStudents();

    User getMyData(Long id);
}
