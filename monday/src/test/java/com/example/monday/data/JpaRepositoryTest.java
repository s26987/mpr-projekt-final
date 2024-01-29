package com.example.monday.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class JpaRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        var student1 = new Student("marcin1", StudentUnit.GDANSK, 50L, 5522123, "marcin@marcin.com");
        var student2 = new Student("marcin2", StudentUnit.WARSZAWA, 15L, 123321, "marcin1@marcin.com");
        studentRepository.save(student1);
        studentRepository.save(student2);
    }

    @Test
    void givenStudents_whenGetMaxIndex_ThenReturnValidResult() {
        var maxIndex = studentRepository.getMaxIndex();
        assertTrue(maxIndex.isPresent());
        assertEquals(15L, maxIndex.get());
    }

    @Test
    void phoneNumberStudents_areInList(){
        var phoneNumberStudentsList = studentRepository.getStudentsByPhoneNumber(5522123);
        assertTrue(phoneNumberStudentsList.contains(studentRepository.getStudentByIndex(50L)));
    }

}

