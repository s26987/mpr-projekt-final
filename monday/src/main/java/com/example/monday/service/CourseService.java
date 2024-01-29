package com.example.monday.service;

import com.example.monday.data.Course;
import com.example.monday.data.CourseRepository;
import com.example.monday.data.Student;
import com.example.monday.data.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    public Course addStudentToCourse(UUID courseId, UUID studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        course.getStudents().add(student);
        return courseRepository.save(course);
    }
}