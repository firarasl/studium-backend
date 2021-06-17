package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.models.Clazz;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.repository.ClazzRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service

public class ClazzServiceImpl implements ClazzService {

    @Autowired
    private ClazzRepository clazzRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Clazz> getAllClazzes() {
        return clazzRepository.findAll();
    }

    @Override
    public void saveClazz(String clazzname) {
        Clazz clazz = new Clazz(clazzname);
        clazzRepository.save(clazz);
    }

    @Override
    public void updateClazzName(Long id, String name) {
        Optional<Clazz> clazz = clazzRepository.findById(id);
        if (clazz.isPresent()){
            Clazz clazzNew = new Clazz(id, name);
            clazzRepository.save(clazzNew);
        }
        else {
            throw new NoSuchElementException("this class doesnt exist!");
        }
    }

    @Override
    public void updateStudentToClazz(Long clazzId, Long studentId) {

        Optional<Clazz> clazz = clazzRepository.findById(clazzId);
        if (!clazz.isPresent()){
            throw new NoSuchElementException("this class doesnt exist!");
        }

        Optional<User> user = userRepository.findById(studentId);

        if (user.isPresent()){
            userRepository.updateSetClazz(studentId, clazzId);
        }
        else{
            throw new NoSuchElementException("this user doesnt exist!");
        }

    }
}
