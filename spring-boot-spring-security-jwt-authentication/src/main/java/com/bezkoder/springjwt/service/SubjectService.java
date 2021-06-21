package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.models.Subject;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.SubjectUpdateRequest;
import com.bezkoder.springjwt.payload.response.SubjectAndGpaResponse;

import java.util.List;

public interface SubjectService {


    List<Subject> getAllSubejects();

    void saveSubject(String name, Long teacherId);

    void updateSubject(SubjectUpdateRequest subjectUpdateRequest);

    void archieveSubject(Long id);

    void deleteSubject(Long id);

    SubjectAndGpaResponse mySubjectsAndGpa(Long currentUserId);

}
