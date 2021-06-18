package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.models.Subject;
import com.bezkoder.springjwt.models.Test;
import com.bezkoder.springjwt.payload.request.SubjectUpdateRequest;
import com.bezkoder.springjwt.repository.SubjectRepository;
import com.bezkoder.springjwt.repository.TestsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {

@Autowired
private SubjectRepository subjectRepository;

    @Autowired
    private TestsRepository testRepository;


    @Override
    public List<Subject> getAllSubejects() {
        return subjectRepository.findAll();
    }

    @Override
    public void saveSubject(String name, Long teacherId) {
        subjectRepository.saveNewSubject(name, teacherId);
    }

    @Override
    public void updateSubject(SubjectUpdateRequest request) {
        Optional<Subject> optionalSubject = subjectRepository.findById(request.getId());
        if (!optionalSubject.isPresent()){
            throw new NoSuchElementException("no subject like this");
        }
        if (!optionalSubject.get().isArchieved()){
            subjectRepository.updateSubject(request.getId(),
                    request.getName(), request.getTeacher_id());
        }
        else{
            throw new IllegalArgumentException("subject is archieved");
        }
    }

    @Override
    public void archieveSubject(Long id) {

        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        if (!optionalSubject.isPresent()){
            throw new NoSuchElementException("no subject like this");
        }
        subjectRepository.archieveSubject(id);
    }

    @Override
    public void deleteSubject(Long id) {

        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        if (!optionalSubject.isPresent()){
            throw new NoSuchElementException("no subject like this");
        }

        List<Test> testList = testRepository.findAllBySubjectId(id);
        if (testList.size()==0){
            subjectRepository.deleteById(id);
        }
        else{
            throw new IllegalArgumentException("subject has tests");
        }
    }
}
