package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.helper.StudentDataUtil;
import com.bezkoder.springjwt.models.*;
import com.bezkoder.springjwt.payload.request.SubjectUpdateRequest;
import com.bezkoder.springjwt.payload.response.SubjectAndGpaResponse;
import com.bezkoder.springjwt.repository.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.nio.channels.IllegalChannelGroupException;
import java.util.*;

@Service
public class SubjectServiceImpl implements SubjectService {

@Autowired
private SubjectRepository subjectRepository;

    @Autowired
    private TestsRepository testRepository;

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private StudentDataUtil studentDataUtil;





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

    public void saveSubject(String name, String teacherName) {

        Optional<User> teacher = (Optional<User>) Hibernate.unproxy(userRepository.findByUsername(teacherName));

        if (!teacher.isPresent()){
            throw new NoSuchElementException("This user does not exist");
        }
        if (!teacher.get().getRole().getName().equals(ERole.ROLE_TEACHER)){
            throw new NoSuchElementException("This user isnt a teacher");
        }
        Optional<Subject> subjectOptional = subjectRepository.findByName(name);
        if (subjectOptional.isPresent()){
            throw new IllegalArgumentException("This subject already exists");
        }

        User teacherObject = entityManager.getReference(User.class, teacher.get().getId());
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
            throw new NoSuchElementException("No subject like this");
        }
        if (optionalSubject.get().isArchieved()){
            throw new IllegalArgumentException("Subject is archieved");
        }

if ((request.getTeacherName()==null || request.getTeacherName().isEmpty() )
&& (request.getName()==null || request.getName().isEmpty()  )){
    throw new IllegalArgumentException("no data to change");
}

if(request.getTeacherName()!=null && !request.getTeacherName().isEmpty() ){

    Optional<User> teacher = (Optional<User>) Hibernate.unproxy(userRepository.findByUsername(request.getTeacherName()));
    if (!teacher.isPresent()){
        throw new NoSuchElementException("No user like this");
    }
    else if(!teacher.get().getRole().getName().equals(ERole.ROLE_TEACHER)){
        throw new NoSuchElementException("User isnt a teacher");
    }

    if (!optionalSubject.get().getUser().getUsername().equals(request.getTeacherName()) ){
        User teacherObject = entityManager.getReference(User.class, teacher.get().getId());
        optionalSubject.get().setUser(teacherObject);
    }else{
        throw new IllegalArgumentException("Assigning to the same teacher");
    }
}
        if( request.getName()!=null && !request.getName().isEmpty() ){
            if (!optionalSubject.get().getName().equals(request.getName())){
                optionalSubject.get().setName(request.getName());
            }else{
                throw new IllegalArgumentException("Assigning to the same name");
            }
        }


            entityManager.persist(optionalSubject.get());
    }

    @Override
    public void archieveSubject(Long id) {

        Optional<Subject> optionalSubject = (Optional<Subject>) Hibernate.unproxy(subjectRepository.findById(id));
        if (!optionalSubject.isPresent()){
            throw new NoSuchElementException("No subject like this");
        }

        List<Test> testList = testRepository.findAllBySubject(optionalSubject.get());
        if (testList.size()==0){
            throw new IllegalArgumentException("Subject doesn't have tests");
        }

        optionalSubject.get().setArchieved(true);


        subjectRepository.save(optionalSubject.get());
    }

    @Override
    public void deleteSubject(Long id) {

        Optional<Subject> optionalSubject = (Optional<Subject>) Hibernate.unproxy(subjectRepository.findById(id));
        if (!optionalSubject.isPresent()){
            throw new NoSuchElementException("No subject like this");
        }

        List<Test> testList = (List<Test>) Hibernate.unproxy(testRepository.findAllBySubjectId(id));
        if (testList.size()==0){
            subjectRepository.deleteById(id);
        }
        else{
            throw new IllegalArgumentException("Subject has tests");
        }
    }






    @Override
    public SubjectAndGpaResponse mySubjectsAndGpa(Long currentUserId) {

        User currentUser = (User) Hibernate.unproxy(userRepository.findById(currentUserId).get());


        Optional<Clazz> optionalClazz = Optional.ofNullable(currentUser.getClazz());
        if (!optionalClazz.isPresent()){
            throw new NoSuchElementException("This student doesnt have a class");
        }

        List<Subject> subjectList= optionalClazz.get().getSubjects();

        SubjectAndGpaResponse answer = new SubjectAndGpaResponse();
        answer.setClazzName(optionalClazz.get().getName());

        Map<Double, Subject> answerSubject= new HashMap<>();


        for (int i = 0; i < subjectList.size(); i++) {
            double gpa = 0;
            List<Test> testList= testRepository.findAllBySubject(subjectList.get(i));
            List<TestResult> testResultList= new ArrayList<>();
            for (int u = 0; u < testList.size(); u++) {
                testResultList.addAll(testResultRepository.findAllByUserAndTest(currentUser, testList.get(u)));
            }
            gpa= studentDataUtil.getGPA(testResultList);

            answerSubject.put(gpa, subjectList.get(i));
        }
        answer.setSubjects(answerSubject);

return answer;
    }

    @Override
    public Subject getSubjectById(Long id) {

        Optional<Subject> optionalSubject = (Optional<Subject>) Hibernate.unproxy(subjectRepository.findById(id));
        if (!optionalSubject.isPresent()){
            throw new NoSuchElementException("This subject doesnt exist");
        }
        return optionalSubject.get();
    }

    @Override
    public List<Subject> getAllSubejectsByTeacher(Long id) {

        Optional<User> optionalUser = (Optional<User>) Hibernate.unproxy(userRepository.findById(id));
        if (!optionalUser.isPresent()){
            throw new NoSuchElementException("This user doesnt have a class");
        }

        List<Subject> subjectList = subjectRepository.findAllByUser(optionalUser.get());

        return subjectList;
    }

    @Override
    @Transactional
    public void assignClazz(Long id, String clazzName) {
        Optional<Subject> optionalSubject = (Optional<Subject>) Hibernate.unproxy(subjectRepository.findById(id));
        if (!optionalSubject.isPresent()){
            throw new NoSuchElementException("This subject doesnt exist");
        }

        Optional<Clazz> optionalClazz = clazzRepository.findByName(clazzName);
        if (!optionalClazz.isPresent()){
            throw new NoSuchElementException("This class doesnt exist");
        }
        if (optionalClazz.get().getSubjects().contains(optionalSubject.get())){
            throw new IllegalArgumentException("This class already has this subject");
        }

        Subject subject = entityManager.getReference(Subject.class, optionalSubject.get().getId());
        List<Subject> list;
        if(optionalClazz.get().getSubjects()==null){
             list = new ArrayList<>();
            list.add(subject);
        }
        else{
            list=optionalClazz.get().getSubjects();
            list.add(subject);
        }
        optionalClazz.get().setSubjects(list);

        entityManager.persist(optionalClazz.get());

    }


}
