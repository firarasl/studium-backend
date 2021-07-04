package com.studium.service;

import com.studium.models.Subject;
import com.studium.payload.request.SubjectUpdateRequest;
import com.studium.payload.response.SubjectAndGpaResponse;
import com.studium.payload.response.SubjectResponse;

import java.util.List;

public interface SubjectService {


    List<Subject> getAllSubejects();

    void saveSubject(String name, String teacherName, String clazzName);

    void updateSubject(SubjectUpdateRequest subjectUpdateRequest);

    void archieveSubject(Long id);

    void deleteSubject(Long id);

    SubjectAndGpaResponse mySubjectsAndGpa(Long currentUserId);

    SubjectResponse getSubjectById(Long id);

    List<SubjectResponse> getAllSubejectsByTeacher(Long id);

//    void assignClazz(Long id, String clazzName);
}
