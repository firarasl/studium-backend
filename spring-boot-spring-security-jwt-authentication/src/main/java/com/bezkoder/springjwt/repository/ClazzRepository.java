package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Clazz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClazzRepository extends JpaRepository<Clazz,Long> {

    List<Clazz> findAll();

//    @Query(value="INSERT INTO clazz_subject (clazz_id, subject_id) VALUES (:clazzId, :subjectId);\n", nativeQuery=true)
//    void saveSubject(Long clazzId, Long subjectId);

}
