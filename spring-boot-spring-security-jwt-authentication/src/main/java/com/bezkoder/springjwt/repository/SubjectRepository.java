package com.bezkoder.springjwt.repository;


import com.bezkoder.springjwt.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject,Long> {

@Query(value="SELECT * FROM subject WHERE teacher_id = :id AND is_archieved= :isArchieved \n", nativeQuery=true)
    List<Subject> findAllByTeacherIdAndArchieved(Long id, boolean isArchieved);

@Query(value="INSERT INTO subject (name, teacher_id, is_archieved) VALUES (:name, :teacherId, false );\n", nativeQuery=true)
    void saveNewSubject(String name, Long teacherId);


@Query(value="UPDATE subject SET name= :name AND teacher_id= :teacherId WHERE id= :id;\n", nativeQuery=true)
    void updateSubject(Long id, String name, Long teacherId);

@Query(value="UPDATE subject SET archieved= TRUE WHERE id= :id;\n", nativeQuery=true)
void archieveSubject(Long id);
}
