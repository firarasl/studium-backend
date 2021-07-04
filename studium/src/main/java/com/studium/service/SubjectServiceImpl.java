package com.studium.service;

import com.studium.helper.StudentDataUtil;
import com.studium.models.*;
import com.studium.models.*;
import com.studium.payload.request.SubjectUpdateRequest;
import com.studium.payload.response.SubjectAndGpaResponse;
import com.studium.payload.response.SubjectResponse;
import com.studium.repository.*;
import com.studium.repository.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
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

    //CORRECTED

    public void saveSubject(String name, String teacherName, String clazzName) {

        Optional<User> teacher = (Optional<User>) Hibernate.unproxy(userRepository.findByUsername(teacherName));

        if (!teacher.isPresent()){
            throw new NoSuchElementException("This user does not exist");
        }
        if (!teacher.get().getRole().getName().equals(ERole.ROLE_TEACHER)){
            throw new NoSuchElementException("This user isnt a teacher");
        }

        Optional<Clazz> optionalClazz = clazzRepository.findByName(clazzName);
        if (!optionalClazz.isPresent()){
            throw new IllegalArgumentException("This class doesnt exist");
        }
        List<Subject> subjects= optionalClazz.get().getSubjects();
        for(Subject subject: subjects){
            if (subject.getName().equals(name)){
                throw new IllegalArgumentException("This class already has this subject");
            }
        }


//        User teacherObject = entityManager.getReference(User.class, teacher.get().getId());
        Subject subject = new Subject();
        subject.setName(name);
        subject.setUser(teacher.get());
        subjects.add(subject);
        optionalClazz.get().setSubjects(subjects);

        subjectRepository.save(subject);
        clazzRepository.save(optionalClazz.get());
//        entityManager.persist(subject);
//        clazzRepository.save(optionalClazz.get());


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
    throw new IllegalArgumentException("Nothing to change");
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
            throw new NoSuchElementException("You don't have a class");
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
    public SubjectResponse getSubjectById(Long id) {

        Optional<Subject> optionalSubject = (Optional<Subject>) Hibernate.unproxy(subjectRepository.findById(id));
        if (!optionalSubject.isPresent()){
            throw new NoSuchElementException("This subject doesnt exist");
        }
        SubjectResponse response = new SubjectResponse();
        response.setArchieved(optionalSubject.get().isArchieved());
        response.setName(optionalSubject.get().getName());
        response.setTeacherName(optionalSubject.get().getUser().getUsername());
        response.setId(optionalSubject.get().getId());
        List<Subject> list = new ArrayList<>();
        list.add(optionalSubject.get());
        Optional<Clazz> clazz = clazzRepository.findBySubjects(list);
        if(clazz.isPresent()){
            response.setClazzName(clazz.get().getName());
        }



        return response;
    }

    @Override
    public List<SubjectResponse> getAllSubejectsByTeacher(Long id) {

        Optional<User> optionalUser = (Optional<User>) Hibernate.unproxy(userRepository.findById(id));
        if (!optionalUser.isPresent()){
            throw new NoSuchElementException("This user doesnt exist");
        }

        List<Subject> subjectList = subjectRepository.findAllByUser(optionalUser.get());
        List<SubjectResponse> responses = new ArrayList<>();

        for(Subject subject: subjectList){
            SubjectResponse subjectResponse = new SubjectResponse();
            subjectResponse.setId(subject.getId());
            subjectResponse.setName(subject.getName());
            subjectResponse.setArchieved(subject.isArchieved());
            List<Subject> helper = new ArrayList<>();
            helper.add(subject);
            Optional<Clazz> clazz = clazzRepository.findBySubjects(helper);
if(clazz.isPresent()){
    subjectResponse.setClazzName(clazz.get().getName());

}
responses.add(subjectResponse);
helper.remove(0);
        }

        return responses;
    }

//    @Override
//    @Transactional
//    public void assignClazz(Long id, String clazzName) {
//        Optional<Subject> optionalSubject = (Optional<Subject>) Hibernate.unproxy(subjectRepository.findById(id));
//        if (!optionalSubject.isPresent()){
//            throw new NoSuchElementException("This subject doesnt exist");
//        }
//
//        Optional<Clazz> optionalClazz = clazzRepository.findByName(clazzName);
//        if (!optionalClazz.isPresent()){
//            throw new NoSuchElementException("This class doesnt exist");
//        }
//        if (optionalClazz.get().getSubjects().contains(optionalSubject.get())){
//            throw new IllegalArgumentException("This class already has this subject");
//        }
//
//        Subject subject = entityManager.getReference(Subject.class, optionalSubject.get().getId());
//        List<Subject> list;
//        if(optionalClazz.get().getSubjects()==null){
//             list = new ArrayList<>();
//            list.add(subject);
//        }
//        else{
//            list=optionalClazz.get().getSubjects();
//            list.add(subject);
//        }
//        optionalClazz.get().setSubjects(list);
//
//        entityManager.persist(optionalClazz.get());
//
//    }


}
