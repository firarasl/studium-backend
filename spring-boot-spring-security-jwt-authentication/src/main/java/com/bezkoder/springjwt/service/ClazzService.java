package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.models.Clazz;

import java.util.List;

public interface ClazzService {
    List<Clazz> getAllClazzes();


    void saveClazz(String clazzname);

    void updateClazzName(Long id, String name);

    void updateStudentToClazz(Long clazzId, Long studentId);
}
