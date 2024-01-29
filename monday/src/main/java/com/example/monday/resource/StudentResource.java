package com.example.monday.resource;

import com.example.monday.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// Tu podobnie jak przy adnotacji @Service, @RestController mówi Springowi, że jest to definicja Beana,
// z tą różnicą, że tu wskazujemy na to, że będzie to Bean udostępniający żądania do wykonania z zewnątrz w postaci zapytań HTTP
// Adnotacja @RequestMapping wskazuje nam ścieżkę jaką należy dodać do adresu serwera aby wykonać żądanie do metod w tej klasie
@RestController
@RequestMapping(path = "/students")
@RequiredArgsConstructor
public class StudentResource {

    private final StudentService studentService;

    //@PostMapping w tym wypadku wskazuje na to, że metoda konieczna do wykonania żądania to POST
    //Spring posiada odpowieniki dla większości metod HTTP
    //@ResponseStatus(HttpStatus.CREATED) - wskazuje na kod odpowiedzi dla każdego żądania, które nie zakończy się błędem
    //Żądania zakończone niespodziewanym błędem dostają status 500 - INTERNAL_SERVER_ERROR.
    //@RequestBody wskazuje na to, w którym elemencie żadania znajdziemy wartość parametru - w tym wypadku jest to body
    //Parametry możemy znaleźć w url (jako parametry podane po ? w formacie key=value) i jest to domyślny sposób odczytania. Można zastosować też adnotacje @RequestParam
    //oraz w nagłówkach żądania. Należy użyć wtedy adnotacji @RequestHeader.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveStudents(@Validated @RequestBody CreateStudent createStudent) {
        studentService.saveStudent(createStudent);
    }

    //@PathVariable razem ze zmienną zawartą w ścieżce w formacie {nazwa_zmiennej} tworzą zmienną wartość url, którą odczytujemy bezpośrednio z adresu
    //Zwrócenie ResponseEntity<?> pozwala nam zmieniać dynamicznie parametry odpowiedzi w metodzie w zależności od wyniku działania aplikacji
    //możemy tu ustawić status ale też body, czy nagłówki. Jako ? podajemy informajcę jaki format powinno mieć body w przypadku poprawnej odpowiedzi.
    @GetMapping("/{id}")
    public StudentDto getStudentById(@PathVariable UUID id) {
        return studentService.getStudentById(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByName(String name) {
        studentService.deleteByName(name);
    }

    @GetMapping("/studentByName")
    public List<StudentDto> getStudentsByName(@RequestParam String name) {
        return studentService.getStudentsByName(name);
    }

    @GetMapping("/email")
    public List<StudentDto> getStudentsByEmail(@RequestParam String email) {
        return studentService.getStudentsByEmail(email);
    }

    @GetMapping("/phoneNumber")
    public List<StudentDto> getStudentsByPhoneNumber(@RequestParam Integer phoneNumber) {
        return studentService.getStudentsByPhoneNumber(phoneNumber);
    }

    @GetMapping("/byIndex")
    public StudentDto getStudentByIndex(@RequestParam Long index) {
        return studentService.getStudentByIndex(index);
    }

    @PutMapping("/editStudent")
    public StudentDto editStudentByIndex(@RequestBody StudentDto studentDto) {
        return studentService.updateStudent(studentDto);
    }

    @GetMapping
    public List<StudentDto> getAll() {
        return studentService.getAll();
    }


}
