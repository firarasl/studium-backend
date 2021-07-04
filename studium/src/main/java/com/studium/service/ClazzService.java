package com.studium.service;

import com.studium.models.Clazz;
import com.studium.payload.response.ClazzResponse;

import java.util.List;

public interface ClazzService {
    List<Clazz> getAllClazzes();


    void saveClazz(String clazzname);

    void updateClazzName(Long id, String name);

    String updateStudentToClazz(Long clazzId, String username);

    void deleteClazz(Long clazzId);

    void manageClazzSubjects(Long clazzId, List<Long> subjectIds);

    ClazzResponse getClazzById(Long clazzId);
}
