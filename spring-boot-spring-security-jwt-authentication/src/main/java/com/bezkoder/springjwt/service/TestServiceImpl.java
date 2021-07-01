package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.helper.StudentDataUtil;
import com.bezkoder.springjwt.models.*;
import com.bezkoder.springjwt.payload.request.CSV;
import com.bezkoder.springjwt.payload.response.Student;
import com.bezkoder.springjwt.payload.response.StudentRespond;
import com.bezkoder.springjwt.payload.response.TestGPA;
import com.bezkoder.springjwt.payload.response.TestResponse;
import com.bezkoder.springjwt.repository.SubjectRepository;
import com.bezkoder.springjwt.repository.TestResultRepository;
import com.bezkoder.springjwt.repository.TestsRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;

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
    public void addTest(String name, Timestamp date, String subjectName, Long teacherId) {
        Test test = new Test();
        test.setName(name);
        test.setDate(date);

        Optional<Subject> subject =
                (Optional<Subject>) Hibernate.unproxy(subjectRepository.findByName(subjectName));

        if (!subject.isPresent()){
            throw new NoSuchElementException("This subject doesnt exist");
        }

        Optional<User> teacher =
                (Optional<User>) Hibernate.unproxy(userRepository.findById(teacherId));

        if (!teacher.isPresent()){
            throw new NoSuchElementException("This teacher doesnt exist");
        }

        if (subject.get().getUser()!=null && subject.get().getUser().equals(teacher.get())) {
            test.setSubject(subject.get());
        }
else{
    throw new IllegalArgumentException("You cant interfere in other teacher's tests");
        }
        testsRepository.save(test);

    }

    @Override
    public void updateTest(Long id, String name, Timestamp timestamp) {
        Optional<Test> optionalTest = (Optional<Test>) Hibernate.unproxy(testsRepository.findById(id));

        if (!optionalTest.isPresent()){
            throw new NoSuchElementException("This test doesnt exist");
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
            throw new NoSuchElementException("This test doesnt exist");
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
            throw new NoSuchElementException("This test doesnt exist");
        }

        Optional<User> currentUser = (Optional<User>) Hibernate.unproxy(userRepository.findById(studentId));
        if(!currentUser.isPresent()){
            throw new NoSuchElementException("This username doesnt exist!");
        }

        if (!currentUser.get().getRole().getName().equals(ERole.ROLE_STUDENT)){
            throw new NoSuchElementException("This user isnt a student!");
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
        return "Added a test result";
        }


    }

    @Override
    public void parseCSV(MultipartFile file) {

        try (        Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {



            ColumnPositionMappingStrategy<CSV> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(CSV.class);
            String[] fields = {"username", "testId", "grade"};
            strategy.setColumnMapping(fields);


            CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                    .withMappingStrategy(strategy)
                    .withSkipLines(1)
                    .withSeparator(',')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<CSV> results = csvToBean.parse();

            for (int i = 0; i < results.size(); i++) {
                Optional<User> optionalUser = (Optional<User>) Hibernate.unproxy(userRepository.findByUsername(results.get(i).getUsername()));

                if (!optionalUser.isPresent()){
                    throw new NoSuchElementException("This user doesnt exist");
                }
                Optional<Test> optionalTest = (Optional<Test>) Hibernate.unproxy(testsRepository.findById(results.get(i).getTestId()));

                if (!optionalTest.isPresent()){
                    throw new NoSuchElementException("This test doesnt exist");
                }
                if(!optionalUser.get().getRole().getName().equals(ERole.ROLE_STUDENT)){
                    throw new IllegalArgumentException("This user isnt a student");
                }

                if (!optionalUser.get().getClazz().getSubjects().contains(optionalTest.get().getSubject())){
                    throw new IllegalArgumentException("This student doesnt have this test's subject");
                }

                TestResult testResult = new TestResult();
                testResult.setUser(optionalUser.get());
                testResult.setTest(optionalTest.get());
                testResult.setGrade(results.get(i).getGrade());

                testResultRepository.save(testResult);

            }

        }
        catch (Exception ex) {
               throw new IllegalArgumentException("An error occurred while processing the CSV file.");
            }
    }

    @Override
    public void deleteTest(Long testId) {
        Optional<Test> testOptional = (Optional<Test>) Hibernate.unproxy(testsRepository.findById(testId));
        if (!testOptional.isPresent()){
            throw new NoSuchElementException("This test doesnt exist");
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
            throw new NoSuchElementException("This subject doesnt exist");
        }
        User user= (User) Hibernate.unproxy(userRepository.findById(id).get());

        Clazz clazz = user.getClazz();

        List<Subject> clazzSubjects = clazz.getSubjects();

        if (!clazzSubjects.contains(subject.get())){
            throw new IllegalArgumentException("This student requested subject not assigned to his clazz");
        }


        List<Test> testList = (List<Test>) Hibernate.unproxy(testsRepository.findAllBySubject(subject.get()));



        List<TestGPA> results= new ArrayList<>();



        for (int i = 0; i < testList.size(); i++) {
            TestGPA result = new TestGPA();
            result.setTest(testList.get(i));
            Optional<TestResult> testResult= testResultRepository.findByTestAndUser(testList.get(i), user);

            if (testResult.isPresent()){
                result.setGrade(testResult.get().getGrade());
                result.setSubjectName(subject.get().getName());
                result.setGraded();
            }                results.add(result);

        }


        System.out.println(results);

        return results;
    }

    @Override
    public Set<Test> getAllTestsOfTeacher(Long id) {

        Optional<User> optionalUser = (Optional<User>) Hibernate.unproxy(userRepository.findById(id));
        if (!optionalUser.isPresent()){
            throw new NoSuchElementException("This user doesnt have a class");
        }

        List<Subject> subjectList = subjectRepository.findAllByUser(optionalUser.get());

        Set<Test> testList = new HashSet<>();


        for (int i = 0; i < subjectList.size(); i++) {
            testList.addAll(testsRepository.findAllBySubject(subjectList.get(i)));
        }

        return testList;
    }

    @Override
    public TestResponse findById(Long id) {

        Optional<Test> testOptional = (Optional<Test>) Hibernate.unproxy(testsRepository.findById(id));
        if (!testOptional.isPresent()){
            throw new NoSuchElementException("This test doesnt have a class");
        }

        List<TestResult> testResultList = testResultRepository.findAllByTest(testOptional.get());

        TestResponse testResponse = new TestResponse();

        testResponse.setId(testOptional.get().getId());
        testResponse.setDate(testOptional.get().getDate());
        testResponse.setName(testOptional.get().getName());

        testResponse.setSubjectName(testOptional.get().getSubject().getName());

        List<Student> students = new ArrayList<>();


        for(TestResult testResult: testResultList){
            Student student = new Student();
            student.setGrade(testResult.getGrade());
            student.setId(testResult.getUser().getId());
            student.setUsername(testResult.getUser().getUsername());
            student.setLastname(testResult.getUser().getLastname());
            student.setFirstname(testResult.getUser().getFirstname());

            students.add(student);
        }

        testResponse.setStudents(students);
        return testResponse;
    }


}



