package com.studium.repository;

import com.studium.models.Clazz;
import com.studium.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClazzRepository extends JpaRepository<Clazz,Long> {

    List<Clazz> findAll();

//    List<Clazz> findAllBySubjects(Subject subjects);

    Optional<Clazz> findByName(String clazzname);

//    List<Clazz> findAllBySubjects(List<Subject> subject);

    Optional<Clazz> findBySubjects(List<Subject> subjectList);


//    @Query(value="INSERT INTO clazz_subject (clazz_id, subject_id) VALUES (:clazzId, :subjectId);\n", nativeQuery=true)
//    void saveSubject(Long clazzId, Long subjectId);

}
