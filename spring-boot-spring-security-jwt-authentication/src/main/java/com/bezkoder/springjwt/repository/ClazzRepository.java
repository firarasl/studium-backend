package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Clazz;
import com.bezkoder.springjwt.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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
