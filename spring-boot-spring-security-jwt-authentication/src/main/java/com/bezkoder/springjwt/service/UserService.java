package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.models.Message;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.MessageRequest;
import com.bezkoder.springjwt.payload.request.SignupRequest;
import com.bezkoder.springjwt.payload.request.UserUpdateRequest;
import com.bezkoder.springjwt.payload.response.MyProfile;
import com.bezkoder.springjwt.payload.response.StudentRespond;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    List<User> getAllUsers();

    Optional<User> getUserByUsername(String username);

    void saveUser(SignupRequest userRequest);

    void deleteUser(Long id);

//    void updateMyUser(UserUpdateRequest updateUser, Long currentId);

    void updateUser(UserUpdateRequest updateRequest, Long userId);

    Set<StudentRespond> getAllStudents(Long teacherId);

    User getMyData(Long id);

    void sendMessage(MessageRequest request, Long currentUserId);

    int getMyUnreadMessages(Long id);

    List<Message> myInbox(Long id);

    Message openMessage(Long id, Long messageId);
}
