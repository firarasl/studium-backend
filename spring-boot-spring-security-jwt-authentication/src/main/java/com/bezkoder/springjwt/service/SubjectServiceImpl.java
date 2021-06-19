package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Subject;
import com.bezkoder.springjwt.models.Test;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.SubjectUpdateRequest;
import com.bezkoder.springjwt.repository.SubjectRepository;
import com.bezkoder.springjwt.repository.TestsRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {

@Autowired
private SubjectRepository subjectRepository;

    @Autowired
    private TestsRepository testRepository;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;


    @Override
    public List<Subject> getAllSubejects() {
        return subjectRepository.findAll();
    }

    @Override
    @Transactional

    public void saveSubject(String name, Long teacherId) {

        Optional<User> teacher = (Optional<User>) Hibernate.unproxy(userRepository.findById(teacherId));

        if (!teacher.isPresent()){
            throw new NoSuchElementException("this user does not exist");
        }
        if (!teacher.get().getRole().getName().equals(ERole.ROLE_TEACHER)){
            throw new NoSuchElementException("this user isnt a teacher");
        }

        User teacherObject = entityManager.getReference(User.class, teacherId);
        Subject subject = new Subject();
        subject.setName(name);
        subject.setUser(teacherObject);
        entityManager.persist(subject);


    }

    @Override
    @Transactional
    public void updateSubject(SubjectUpdateRequest request) {
        Optional<Subject> optionalSubject = (Optional<Subject>) Hibernate.unproxy(subjectRepository.findById(request.getId()));
        if (!optionalSubject.isPresent()){
            throw new NoSuchElementException("no subject like this");
        }
        if (optionalSubject.get().isArchieved()){
            throw new IllegalArgumentException("subject is archieved");
        }


        Optional<User> teacher = (Optional<User>) Hibernate.unproxy(userRepository.findById(request.getTeacher_id()));
        if (!teacher.isPresent()){
            throw new NoSuchElementException("no user like this");
        }
        else if(!teacher.get().getRole().getName().equals(ERole.ROLE_TEACHER)){
            throw new NoSuchElementException("user isnt a teacher");
        }

        if (optionalSubject.get().getUser().getId()!=request.getTeacher_id()
        && request.getTeacher_id()!=null){
            User teacherObject = entityManager.getReference(User.class, teacher.get().getId());
            optionalSubject.get().setUser(teacherObject);
        }
            if (!optionalSubject.get().getName().equals(request.getName()) &&
                    !request.getName().isEmpty() && request.getName()!=null){
                optionalSubject.get().setName(request.getName());
            }
            entityManager.persist(optionalSubject.get());
    }

    @Override
    public void archieveSubject(Long id) {

        Optional<Subject> optionalSubject = (Optional<Subject>) Hibernate.unproxy(subjectRepository.findById(id));
        if (!optionalSubject.isPresent()){
            throw new NoSuchElementException("no subject like this");
        }

        optionalSubject.get().setArchieved(true);


        subjectRepository.save(optionalSubject.get());
    }

    @Override
    public void deleteSubject(Long id) {

        Optional<Subject> optionalSubject = (Optional<Subject>) Hibernate.unproxy(subjectRepository.findById(id));
        if (!optionalSubject.isPresent()){
            throw new NoSuchElementException("no subject like this");
        }

        List<Test> testList = (List<Test>) Hibernate.unproxy(testRepository.findAllBySubjectId(id));
        if (testList.size()==0){
            subjectRepository.deleteById(id);
        }
        else{
            throw new IllegalArgumentException("subject has tests");
        }
    }
}
