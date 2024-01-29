package com.example.monday.data;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseRepository extends CrudRepository<Course, UUID>{

    //Pobranie obydwu encji oraz zapisanie ich w bazie danych

    @Transactional
    default Course addStudentToCourse(UUID courseId, UUID studentId, StudentRepository studentRepository) {
        Course course = findById(courseId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono kursu"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono studenta"));

        course.getStudents().add(student);
        return save(course);
    }

}
