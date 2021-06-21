package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.models.*;
import com.bezkoder.springjwt.payload.request.SubjectUpdateRequest;
import com.bezkoder.springjwt.payload.response.SubjectAndGpaResponse;
import com.bezkoder.springjwt.repository.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
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
    private TestResultRepository testResultRepository;


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClazzRepository clazzRepository;

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






    @Override
    public SubjectAndGpaResponse mySubjectsAndGpa(Long currentUserId) {

        User currentUser = (User) Hibernate.unproxy(userRepository.findById(currentUserId).get());


        Optional<Clazz> optionalClazz = Optional.ofNullable(currentUser.getClazz());
        if (!optionalClazz.isPresent()){
            throw new NoSuchElementException("This student doesnt have a class");
        }

//        List<Subject> subjectList= subjectRepository.findAllByClazz(optionalClazz.get());
        List<Subject> subjectList= optionalClazz.get().getSubjects();


        List<TestResult> testResultList = testResultRepository.findAllByUser(currentUser);

        double gpa=0;

        for (int i = 0; i < testResultList.size(); i++) {
            gpa+=testResultList.get(i).getGrade();

        }
        SubjectAndGpaResponse response = new SubjectAndGpaResponse();

        gpa=gpa/testResultList.size();
List<String> subjectNames= new ArrayList<>();

        for (int i = 0; i < subjectList.size(); i++) {
            subjectNames.add(subjectList.get(i).getName());
        }

        response.setClazzName(optionalClazz.get().getName());
        response.setGpa(gpa);
        response.setSubjectName(subjectNames);
        return response;
    }


}
