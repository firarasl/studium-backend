package com.studium.service;

import com.studium.models.Message;
import com.studium.models.User;
import com.studium.payload.request.MessageRequest;
import com.studium.payload.request.SignupRequest;
import com.studium.payload.request.UserUpdateRequest;
import com.studium.payload.response.StudentRespond;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    List<User> getAllUsers(Long id);

    Optional<User> getUserByUsername(String username);

    void saveUser(SignupRequest userRequest);

    void deleteUser(Long id);

//    void updateMyUser(UserUpdateRequest updateUser, Long currentId);

    void updateUser(UserUpdateRequest updateRequest, Long userId);

    Set<StudentRespond> getAllTeachersStudents(Long teacherId);

    User getMyData(Long id);

    void sendMessage(MessageRequest request, Long currentUserId);

    int getMyUnreadMessages(Long id);

    List<Message> myInbox(Long id);

    Message openMessage(Long id, Long messageId);
}
