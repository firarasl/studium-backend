package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.models.Clazz;
import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Subject;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.response.ClazzResponse;
import com.bezkoder.springjwt.repository.ClazzRepository;
import com.bezkoder.springjwt.repository.SubjectRepository;
import com.bezkoder.springjwt.repository.UserRepository;
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

public class ClazzServiceImpl implements ClazzService {

    @Autowired
    private ClazzRepository clazzRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public List<Clazz> getAllClazzes() {
        List<Clazz> list = (List<Clazz>) Hibernate.unproxy(clazzRepository.findAll());
        return list;
    }

    @Override
    public void saveClazz(String clazzname) {

        Optional<Clazz> clazzOptional = (Optional<Clazz>) Hibernate.unproxy(clazzRepository.findByName(clazzname));
        if(clazzOptional.isPresent()){
            throw new IllegalArgumentException("This class already exists!");
        }

        Clazz clazz = new Clazz(clazzname);
        clazzRepository.save(clazz);
    }

    @Override
    public void updateClazzName(Long id, String name) {
        Optional<Clazz> clazz = clazzRepository.findById(id);
        if (!clazz.isPresent()){
            throw new NoSuchElementException("This class doesnt exist!");
        }

        if(clazz.get().getName().equals(name)){
            throw new IllegalArgumentException("The same name for the class!");
        }


        Optional<Clazz> clazzOptional = (Optional<Clazz>) Hibernate.unproxy(clazzRepository.findByName(name));
        if(clazzOptional.isPresent()){
            throw new IllegalArgumentException("This class already exists!");
        }

        Clazz clazzNew = new Clazz(id, name);
        clazzRepository.save(clazzNew);

    }

    @Override
    @Transactional


    public String updateStudentToClazz(Long clazzId, String username) {


        Optional<User> user = (Optional<User>) Hibernate.unproxy(userRepository.findByUsername(username));

        if (!user.isPresent()){
            throw new NoSuchElementException("This user doesnt exist!");
        }

        if (!user.get().getRole().getName().equals(ERole.ROLE_STUDENT)){
            throw new IllegalArgumentException("This user isnt a student!");
        }

        if (user.get().getClazz().getId() == clazzId){
            throw new IllegalArgumentException("This student is already in this class!");
        }
        Clazz classObject = new Clazz();
        if (clazzId != 0){
            Optional<Clazz> clazz = (Optional<Clazz>) Hibernate.unproxy(clazzRepository.findById(clazzId));
            if (!clazz.isPresent()){
                throw new NoSuchElementException("This class doesnt exist!");
            }

           classObject  = entityManager.
                    getReference(Clazz.class, clazz.get().getId() );
            user.get().setClazz(classObject);
            entityManager.persist(user.get());
            return "Class was added to this user!";
        }
        else{
            user.get().setClazz(null);
            userRepository.save(user.get());
            return "Class was detached from this user!";
        }

    }

    @Override
    @Transactional
    public void deleteClazz(Long clazzId) {
        Optional<Clazz> clazz = clazzRepository.findById(clazzId);
        if (!clazz.isPresent()){
            throw new NoSuchElementException("This class doesnt exist!");
        }

        List<User> userList = userRepository.findAllByClazz(clazz.get());

        for (int i = 0; i < userList.size(); i++) {
            userList.get(i).setClazz(null);
        }
        userRepository.saveAll(userList);

        entityManager.remove(clazz.get());
//            clazzRepository.deleteById(clazzId);
    }

    @Override
    @Transactional
    public void manageClazzSubjects(Long clazzId, List<Long> subjectIds) {
        Optional<Clazz> clazz = (Optional<Clazz>) Hibernate.unproxy(clazzRepository.findById(clazzId));
        if (!clazz.isPresent()){
            throw new NoSuchElementException("This class doesnt exist!");
        }
List<Subject> subjectList = new ArrayList<>();



        for (int i = 0; i < subjectIds.size(); i++) {
            if (subjectRepository.findById(subjectIds.get(i)).isPresent()){
                Subject subject = entityManager.
                        getReference(Subject.class,subjectIds.get(i) );
                subjectList.add(subject);
            }
            else{
                throw new NoSuchElementException("This subject doesnt exist!");
            }

        }

        clazz.get().setSubjects(subjectList);
        entityManager.persist(clazz.get());



    }

    @Override
    public ClazzResponse getClazzById(Long clazzId) {
        Optional<Clazz> clazz = clazzRepository.findById(clazzId);
        if (!clazz.isPresent()){
            throw new NoSuchElementException("This class doesnt exist!");
        }

        List<User> studentList = userRepository.findAllByClazz(clazz.get());

        List<Subject> subjectList = clazz.get().getSubjects();

        ClazzResponse response = new ClazzResponse();
        response.setId(clazzId);
        response.setName(clazz.get().getName());
        response.setSubjectList(subjectList);
        response.setStudentList(studentList);

        return response;
    }
}
