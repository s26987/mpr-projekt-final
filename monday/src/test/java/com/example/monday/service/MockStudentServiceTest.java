package com.example.monday.service;

import com.example.monday.data.Student;
import com.example.monday.data.StudentRepository;
import com.example.monday.data.StudentUnit;
import com.example.monday.resource.StudentDto;
import com.example.monday.resource.StudentMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


//Rozszerzamy junit rozszerzeniem z mockito, aby móc w junitowych testach korzystać z funkcji biblioteki mockito
@ExtendWith(MockitoExtension.class)
class MockStudentServiceTest {

    //Mock tworzy nam proxy naszej klasy - to sprawia, że wywołania tej klasy nie wykonają rzeczywistej metody
    //i każdorazowe jej wywołanie musimy skonfigurować, możemy też tak jak w przypadku Spy śledzić jej wywołania

    @Mock
    private StudentRepository studentRepository = mock(StudentRepository.class);

    @Mock
    private StudentMapper studentMapper = new StudentMapper();

    //InjectMocks pozwala nam stworzyć klasę testowaną z wykorzystaniem obiektów, które zdefiniowaliśmy
    //jako elementy mockito używając adnotacji Mock/Spy
    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository, studentMapper);
    }

    @AfterEach
    void cleanup() {
        studentRepository.deleteAll();
    }

    @Test
    void givenStudentDtoWhenUpdateStudentThenReturnUpdatedStudentDto() {
        Long index = 5L;
        Student student = new Student("marcin", StudentUnit.GDANSK, index, 123456789, "test@test.com");
        StudentDto studentDto = new StudentDto(UUID.fromString("193c30a0-2c73-4229-989c-c257c05a9413"),"marcin", StudentUnit.GDANSK, index, "test@test.com", 123456789);
        StudentDto updatedStudentDto = new StudentDto(UUID.fromString("193c30a0-2c73-4229-989c-c257c05a9413"),"UpdatedName", StudentUnit.WARSZAWA, index, "updated@test.com", 987654321);

        when(studentRepository.getStudentByIndex(index)).thenReturn(student);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentMapper.toDto(any(Student.class))).thenReturn(updatedStudentDto);

        StudentDto result = studentService.updateStudent(studentDto);

        assertNotNull(result);
        assertEquals(updatedStudentDto, result);
    }

    @Test
    void givenEmailWhenGetStudentsByEmailThenReturnListOfStudents() {

        String email = "test@test.com";
        Student student = new Student("marcin", StudentUnit.GDANSK, 5L, 123456789, email);
        StudentDto studentDto = new StudentDto(UUID.fromString("193c30a0-2c73-4229-989c-c257c05a9413"),"marcin", StudentUnit.GDANSK, 5L, email, 123456789);

        when(studentRepository.getStudentsByEmail(email)).thenReturn(Arrays.asList(student));
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        List<StudentDto> result = studentService.getStudentsByEmail(email);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(studentDto, result.get(0));
    }

    @Test
    void givenPhoneNumberWhenGetStudentsByPhoneNumberThenReturnListOfStudents() {
        // given
        Integer phoneNumber = 123456789;
        Student student = new Student("marcin", StudentUnit.GDANSK, 5L, phoneNumber, "test@test.com");
        StudentDto studentDto = new StudentDto(UUID.fromString("193c30a0-2c73-4229-989c-c257c05a9413"),"marcin", StudentUnit.GDANSK, 5L, "test@test.com", phoneNumber);

        // when
        when(studentRepository.getStudentsByPhoneNumber(phoneNumber)).thenReturn(Arrays.asList(student));
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        // then
        List<StudentDto> result = studentService.getStudentsByPhoneNumber(phoneNumber);

        // verify
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(studentDto, result.get(0));
    }








}


