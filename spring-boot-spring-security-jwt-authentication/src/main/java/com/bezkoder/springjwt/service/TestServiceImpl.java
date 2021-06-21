package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.helper.StudentDataUtil;
import com.bezkoder.springjwt.models.*;
import com.bezkoder.springjwt.payload.request.CSV;
import com.bezkoder.springjwt.payload.response.StudentRespond;
import com.bezkoder.springjwt.payload.response.TestGPA;
import com.bezkoder.springjwt.repository.SubjectRepository;
import com.bezkoder.springjwt.repository.TestResultRepository;
import com.bezkoder.springjwt.repository.TestsRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TestServiceImpl implements TestService {


@Autowired
private TestsRepository testsRepository;
@Autowired
    public StudentDataUtil helper;

    @Autowired
    private TestResultRepository testResultRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void addTest(String name, Timestamp date) {
        Test test = new Test();
        test.setName(name);
        test.setDate(date);
        testsRepository.save(test);

    }

    @Override
    public void updateTest(Long id, String name, Timestamp timestamp) {
        Optional<Test> optionalTest = (Optional<Test>) Hibernate.unproxy(testsRepository.findById(id));

        if (!optionalTest.isPresent()){
            throw new NoSuchElementException("this test doesnt exist");
        }

        if (!optionalTest.get().getName().equals(name)){
            optionalTest.get().setName(name);
        }

        if (!optionalTest.get().getDate().equals(timestamp)){
            optionalTest.get().setDate(timestamp);
        }

        testsRepository.save(optionalTest.get());

    }

    @Override
    public List<StudentRespond> getAllStudentsOfTest(Long id) {
        Optional<Test> optionalTest = (Optional<Test>) Hibernate.unproxy(testsRepository.findById(id));

        if (!optionalTest.isPresent()){
            throw new NoSuchElementException("this test doesnt exist");
        }

        List<StudentRespond> studentResponds = new ArrayList<>();

        List<TestResult> testResultList = (List<TestResult>) Hibernate.unproxy(testResultRepository.findAllByTest(optionalTest.get()));


        for (int i = 0; i < testResultList.size(); i++) {
            StudentRespond respond= helper.getPayload(testResultList.get(i).getUser(), testResultList.get(i).getGrade());
            studentResponds.add(respond);
        }


        return studentResponds;
    }

    @Override
    public String changeTestGrade(Long testId, Long studentId, double grade) {

        Optional<Test> testOptional = (Optional<Test>) Hibernate.unproxy(testsRepository.findById(testId));
        if (!testOptional.isPresent()){
            throw new NoSuchElementException("this test doesnt exist");
        }

        Optional<User> currentUser = (Optional<User>) Hibernate.unproxy(userRepository.findById(studentId));
        if(!currentUser.isPresent()){
            throw new NoSuchElementException("this username doesnt exist!");
        }

        if (!currentUser.get().getRole().getName().equals(ERole.ROLE_STUDENT)){
            throw new NoSuchElementException("this user isnt a student!");
        }



        Optional<TestResult> testResultOptional = (Optional<TestResult>) Hibernate.unproxy(testResultRepository.findByTestAndUser(testOptional.get(), currentUser.get()));
        if (testResultOptional.isPresent()){
            testResultOptional.get().setGrade(grade);
            testResultRepository.save(testResultOptional.get());
            return "Updated the test result";
        }

        else{
            TestResult testResult = new TestResult();
            testResult.setGrade(grade);
            testResult.setTest(testOptional.get());
            testResult.setUser(currentUser.get());
            testResultRepository.save(testResult);
        return "added a test result";
        }


    }

    @Override
    public void parseCSV(MultipartFile file) {
        // validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("you didnt upload anything");
        } else {

            // parse CSV file to create a list of `User` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<CSV> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(CSV.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of users
                List<CSV> csv = csvToBean.parse();

                // TODO: save users in DB?

                // save users list on model
                System.out.println(csv);

            } catch (Exception ex) {
               throw new IllegalArgumentException("An error occurred while processing the CSV file.");
            }
        }
    }

    @Override
    public void deleteTest(Long testId) {
        Optional<Test> testOptional = (Optional<Test>) Hibernate.unproxy(testsRepository.findById(testId));
        if (!testOptional.isPresent()){
            throw new NoSuchElementException("this test doesnt exist");
        }
        List<TestResult> testResultList = (List<TestResult>) Hibernate.unproxy(testResultRepository.findAllByTest(testOptional.get()));

        testResultRepository.deleteAll(testResultList);

        testsRepository.delete(testOptional.get());
    }







    @Override
    public List<TestGPA> myTestsAndGpa(Long id, Long subjectId) {

        Optional<Subject> subject =
                (Optional<Subject>) Hibernate.unproxy(subjectRepository.findById(subjectId));

        if (!subject.isPresent()){
            throw new NoSuchElementException("this subject doesnt exist");
        }
        User user= (User) Hibernate.unproxy(userRepository.findById(id).get());

        Clazz clazz = user.getClazz();

        List<Subject> clazzSubjects = clazz.getSubjects();

        if (!clazzSubjects.contains(subject.get())){
            throw new IllegalArgumentException("this student requested subject not assigned to his clazz");
        }


        List<Test> testList = (List<Test>) Hibernate.unproxy(testsRepository.findAllBySubject(subject.get()));



        List<TestGPA> results= new ArrayList<>();



        for (int i = 0; i < testList.size(); i++) {
            TestGPA result = new TestGPA();
            result.setTestName(testList.get(i).getName());
            Optional<TestResult> testResult= testResultRepository.findByTestAndUser(testList.get(i), user);

            if (testResult.isPresent()){
                result.setGpa(testResult.get().getGrade());
                result.setSubjectName(subject.get().getName());
                result.setGraded();
            }                results.add(result);

        }




        return results;
    }


}



