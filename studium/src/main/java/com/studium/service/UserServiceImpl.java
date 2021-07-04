package com.studium.service;


import com.studium.helper.StudentDataUtil;
import com.studium.models.*;
import com.studium.models.*;
import com.studium.payload.request.MessageRequest;
import com.studium.payload.request.SignupRequest;
import com.studium.payload.request.UserUpdateRequest;
import com.studium.payload.response.StudentRespond;
import com.studium.repository.*;
import com.studium.repository.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

@Autowired
private UserRepository userRepository;


    @Autowired
    private SubjectRepository subjectRepository;


    @Autowired
    private ClazzRepository clazzRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private TestResultRepository testResultRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private RoleRepository roleRepository;


    @Override
    @Transactional
    public List<User> getAllUsers(Long id) {
        List<User> userList = (List<User>) Hibernate.unproxy(userRepository.findAll());
        Optional<User> currentUser = userRepository.findById(id);

        userList.remove(currentUser.get());

        return userList;
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        Optional<User> user= userRepository.findByUsername(username);
        if(!user.isPresent()){
            throw new NoSuchElementException("This username doesnt exist!");
        }else{
            return user;
        }
    }

    @Autowired
    public StudentDataUtil helper;

    @Override
    @Transactional
    public void saveUser(SignupRequest userRequest) {

        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }


        Role roles = null;

        if (userRequest.getRole() == null) {

            Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles =userRole;
        } else {

            switch (userRequest.getRole()) {
                case "admin":

                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles =adminRole;

                    break;
                case "teacher":
                    Role modRole = roleRepository.findByName(ERole.ROLE_TEACHER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles=modRole;

                    break;
                default:
                    Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles=userRole;
            }
        }


        User user = new User(userRequest.getUsername(),
                userRequest.getFirstname(),
                userRequest.getLastname(),
                encoder.encode(userRequest.getPassword()));

        user.setRole(roles);




        Role role = entityManager.getReference(Role.class, user.getRole().getId());
        user.setRole(role);
        entityManager.persist(user);

    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> currentUser = userRepository.findById(id);
        if(!currentUser.isPresent()){
            throw new NoSuchElementException("This user doesnt exist!");
        }

        if (currentUser.get().getRole().getName() == ERole.ROLE_TEACHER){
            if (subjectRepository.findAllByTeacherIdAndArchieved(id, false).size()>0){
                throw new IllegalArgumentException("This teacher has assigned active subjects");
            }

            List<Subject> teachersSubjects = (List<Subject>) Hibernate.unproxy(subjectRepository.findAllByTeacherIdAndArchieved(id, true));
            for (int i = 0; i < teachersSubjects.size(); i++) {
                teachersSubjects.get(i).setUser(null);
            }
        }else if(currentUser.get().getRole().getName() == ERole.ROLE_STUDENT){
            List<TestResult> testResultList = (List<TestResult>) Hibernate.unproxy(testResultRepository.findAllByUser(currentUser.get()));
            testResultRepository.deleteAll(testResultList);
        }

        userRepository.deleteById(id);
    }

//    @Override
//    public void updateMyUser(UserUpdateRequest updateUser, Long currentId) {
//
//        Optional<User> optionalUser = (Optional<User>) Hibernate.unproxy(userRepository.findById(currentId));
//        if(!optionalUser.isPresent()){
//            throw new NoSuchElementException("this username doesnt exist!");
//        }
//
//        if (!updateUser.getUsername().isEmpty() &&
//                updateUser.getUsername()!=null &&
//                !updateUser.getUsername().equals(optionalUser.get().getUsername())){
//            optionalUser.get().setUsername(updateUser.getUsername());
//        }
//        if (!updateUser.getFirstname().isEmpty() &&
//                updateUser.getFirstname()!=null &&
//                !updateUser.getFirstname().equals(optionalUser.get().getFirstname())){
//            optionalUser.get().setFirstname(updateUser.getFirstname());
//        }
//        if (!updateUser.getLastname().isEmpty() &&
//                updateUser.getLastname()!=null &&
//                !updateUser.getLastname().equals(optionalUser.get().getLastname())){
//            optionalUser.get().setLastname(updateUser.getLastname());
//        }
//
//        if (!updateUser.getPassword().isEmpty() &&
//                updateUser.getPassword()!=null &&
//                !updateUser.getLastname().equals(optionalUser.get().getLastname())){
//            optionalUser.get().setLastname(updateUser.getLastname());
//        }
//
//        if (updateUser.getId()==currentUser.getId()){
//            userRepository.save(updateUser);
//        }
//        else {
//            throw new IllegalArgumentException("you're trying to update someone else's data");
//        }
//    }

    @Override
    public void updateUser(UserUpdateRequest updateRequest, Long userId) {
        Optional<User> currentUser = (Optional<User>) Hibernate.unproxy(userRepository.findById(userId));
        if(!currentUser.isPresent()){
            throw new NoSuchElementException("This user doesnt exist!");
        }

        if(updateRequest.getFirstname() == null && updateRequest.getLastname() == null
              && updateRequest.getUsername() == null && updateRequest.getPassword() == null &&

                !updateRequest.getFirstname().isEmpty() && !updateRequest.getLastname().isEmpty()
                && !updateRequest.getUsername().isEmpty() && !updateRequest.getPassword().isEmpty()
        ){
            throw new IllegalArgumentException("Error: Edit something");
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (updateRequest.getFirstname() != null && !updateRequest.getFirstname().isEmpty()
                && !updateRequest.getFirstname().equals(currentUser.get().getFirstname())
        ) {

            currentUser.get().setFirstname(updateRequest.getFirstname());
        }

        if (updateRequest.getLastname() != null && !updateRequest.getLastname().isEmpty()

                && !updateRequest.getLastname().equals(currentUser.get().getLastname())
        ) {

            currentUser.get().setLastname(updateRequest.getLastname());
        }

        if (updateRequest.getUsername() != null  && !updateRequest.getUsername().isEmpty()
                && !updateRequest.getUsername().equals(currentUser.get().getUsername())
        ) {
            if (userRepository.existsByUsername(updateRequest.getUsername())) {
                throw new IllegalArgumentException("Error: Username is already taken");
            }

            currentUser.get().setUsername(updateRequest.getUsername());
        }

        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()
                && !passwordEncoder.matches(currentUser.get().getPassword(),
                updateRequest.getPassword())
        ) {

            currentUser.get().setPassword(encoder.encode(updateRequest.getUsername()));
        }
        userRepository.save(currentUser.get());

    }

    @Override
    public Set<StudentRespond> getAllTeachersStudents(Long teacherId) {

        Optional<User> currentTeacher = (Optional<User>) Hibernate.unproxy(userRepository.findById(teacherId));
        if(!currentTeacher.isPresent()){
            throw new NoSuchElementException("This user doesnt exist!");
        }

        List<Subject> subjectList = subjectRepository.findAllByUser(currentTeacher.get());

        List<User> userListAll = userRepository.findAll();

        Set<Clazz> clazzList = new HashSet<>();

        for (int i = 0; i < userListAll.size(); i++) {
            if(userListAll.get(i).getRole().getName().equals(ERole.ROLE_STUDENT)
                && userListAll.get(i).getClazz() !=null &&
                    !Collections.disjoint(userListAll.get(i).getClazz().getSubjects(), subjectList)){
//                collecting classes which have subjects of this teacher
                clazzList.add(userListAll.get(i).getClazz());
            }
        }



        List<User> userList = new ArrayList<>();

        for(Clazz clazz : clazzList){
            userList.addAll(userRepository.findAllByClazz(clazz));
        }


        Set<StudentRespond> studentGPAlist = new HashSet<>();

        double gpa=0;

        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getRole().getName().equals(ERole.ROLE_STUDENT)){
                gpa= helper.getGPA((List<TestResult>) Hibernate.unproxy(testResultRepository.findAllByUser(userList.get(i))));
                StudentRespond studentRespond = helper.getPayload(userList.get(i), gpa);

                studentGPAlist.add(studentRespond);
            }
        }
        return studentGPAlist;
    }

    @Override
    public User getMyData(Long id) {
        Optional<User> user= userRepository.findById(id);
        if(!user.isPresent()){
            throw new NoSuchElementException("This username doesnt exist!");
        }

//        MyProfile myProfile = new MyProfile();
//        myProfile.setFirstname(user.get().getFirstname());
//        myProfile.setLastname(user.get().getLastname());
//        myProfile.setUsername(user.get().getUsername());
//        myProfile.setId(user.get().getId());
//        myProfile.setRole(user.get().getRole().getName());


        return user.get();
    }

    @Override
    public void sendMessage(MessageRequest request, Long currentUserId) {
        Optional<User> user= userRepository.findById(currentUserId);
        if(!user.isPresent()){
            throw new NoSuchElementException("This sender doesnt exist!");
        }



        Optional<User> user2= userRepository.findByUsername(request.getReceiverUsername());
        if(!user2.isPresent()){
            throw new NoSuchElementException("This receiver doesnt exist!");
        }

        if(user.get().getUsername().equals(user2.get().getUsername())) {
            throw new IllegalArgumentException("You cant send message to yourself!");
        }

        if(user.get().getRole().getName().equals(ERole.ROLE_STUDENT)){
            if (user.get().getClazz()==null || user.get().getClazz().getSubjects()==null
            ){
                throw new IllegalArgumentException("You may message only your classmates!");
            }
            List<Subject> firstSubjects = user.get().getClazz().getSubjects().stream().filter(o-> o.isArchieved()==false).collect(Collectors.toList());
            if(user2.get().getRole().getName().equals(ERole.ROLE_STUDENT)){
                if (user2.get().getClazz()==null || user2.get().getClazz().getSubjects()==null
                ){
                    throw new IllegalArgumentException("You may message only your classmates!");
                }
                List<Subject> secondSubjects = user2.get().getClazz().getSubjects().stream().filter(o-> o.isArchieved()==false).collect(Collectors.toList());
                if (Collections.disjoint(firstSubjects, secondSubjects)){
                    throw new IllegalArgumentException("This receiver isn't your classmate!");
                }
            }else if(user2.get().getRole().getName().equals(ERole.ROLE_TEACHER)){
                List<Subject> secondSubjects = subjectRepository.findAllByTeacherIdAndArchieved(user2.get().getId(), false);
                if (Collections.disjoint(firstSubjects, secondSubjects)){
                    throw new IllegalArgumentException("This user is not your teacher !");
                }
            }
        }
        else if(user.get().getRole().getName().equals(ERole.ROLE_TEACHER)){
//            all subjects of a teacher
            List<Subject> subjectList = subjectRepository.findAllByUser(user.get());
            // all subjects of a receiver student

            if(user2.get().getRole().getName().equals(ERole.ROLE_STUDENT)){
                if ( user2.get().getClazz()==null || user2.get().getClazz().getSubjects()==null
                ){
                    throw new IllegalArgumentException("This student isn't yours!");
                }
                List<Subject> secondSubjects = user2.get().getClazz().getSubjects();

            if (Collections.disjoint(subjectList, secondSubjects)){
                throw new IllegalArgumentException("As a teacher you may send message to only ur students!");
            }}

        }

        Message message = new Message();
        message.setSender(user.get());

        message.setReceiver(user2.get());
        message.setText(request.getText());
messageRepository.save(message);

    }

    @Override
    public int getMyUnreadMessages(Long id) {

        Optional<User> user= userRepository.findById(id);
        if(!user.isPresent()){
            throw new NoSuchElementException("This sender doesnt exist!");
        }

        int count = messageRepository.countByReceiverAndIsRead(user.get().getId(), false);


        return count;
    }

    @Override
    public List<Message> myInbox(Long id) {
        Optional<User> user= userRepository.findById(id);
        if(!user.isPresent()){
            throw new NoSuchElementException("This sender doesnt exist!");
        }
        List<Message> messages = messageRepository.findAllByReceiver(user.get());


        return messages;
    }

    @Override
    public Message openMessage(Long id, Long messageId) {
        Optional<User> user= userRepository.findById(id);
        if(!user.isPresent()){
            throw new NoSuchElementException("This sender doesnt exist!");
        }
        Optional<Message> message= messageRepository.findById(messageId);
        if(!message.isPresent()){
            throw new NoSuchElementException("This message doesnt exist!");
        }

        if(!message.get().getReceiver().equals(user.get())){
            throw new IllegalArgumentException("Not a message for you");
        }

        message.get().setRead(true);
        messageRepository.save(message.get());
        return message.get();
    }


}
