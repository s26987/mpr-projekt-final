package com.example.monday.service;

import com.example.monday.excetionhandler.RecordNotFoundException;
import com.example.monday.resource.CreateStudent;
import com.example.monday.resource.StudentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

// Tu korzystamy już z Dependency Injection realizowanego przez konstruktor
// Pozwalamy, aby to Spring utworzył dla nas i wstrzykął StudentRepository do tej klasy
// podając to jako parametr konstruktora. Dopuszcza się też wstrzyknięcie Beana przez setter z adnotacją @Autowired
// Zamiast konstruktora używamy adnotacji z pakietu Lombok - dzięki temu jeśli dodamy tu inny Bean nie musimy już nic robić - konstruktor zaktualizuje nam lombok.
@Log
@Service
@RequiredArgsConstructor
public class StudentService {

    //definiujemy stałą z adresem endpointu aplikacji, do której wysyłamy zapytanie
    private static final String API_URL = "http://localhost:8080/students";


    //Synchroniczny "klient" webowy - dzięki tej klasie możemy wywołać synchroniczne zapytanie restowe do zewnętrznej aplikacji
    private final RestTemplate restTemplate = new RestTemplate();
    //Asynchroniczny "Klient" webowy - dzięki tej klasie możemy wywołać synchroniczne i asynchroniczne zapytanie restowe do zewnętrznej aplikacji
    //domyślnie są one asynchroniczne ale możemy wymusić synchroniczność tych zapytań
    private final WebClient webClient = WebClient.builder()
            .baseUrl(API_URL)
            .build();


    public void saveStudent(CreateStudent createStudent) {
        // wysyłamy zapytanie podając cały adres, używając metody POST, wysyłamy w body obiekt createStudent (którego typ zmieniamy na HttpEntity przy użyciu konstruktora
        // definiujemy też, że w odpowiedzi nie otrzymamy, żadnej treści (Void.class), gdybyśmy chcieli odebrać jakąś odpowiedź możemy podać tu dowolną klasę. Oczywiście odpowiedź w jsonie musi odpowiadać strukturą podanej tu klasie
//        restTemplate.exchange(URI.create(API_URL), HttpMethod.POST,
//                new HttpEntity<>(createStudent), Void.class);
        // wysyłanie zapytanie asynchronicznie (zwrócimy 200 zanim ono się zakończy). Mamy to 2 sposoby na podanie body
        // wykonujemy zapytanie (retrive) i oczekujemy pustego wyniku. Następnie metodą subscripe wołamy akcję, którą chcemy wywołać
        // w momencie, w którym zakończymy wykonywać zapytanie i odbierzemy jego odpowiedź
        webClient.post()
                .bodyValue(createStudent)
//                .body(Mono.just(createStudent), CreateStudent.class)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public StudentDto getStudentById(UUID id) {
        //wykonujemy tu synchroniczne zapytanie do pobrania studenta
        try {
            return restTemplate.getForObject(API_URL + "/" + id, StudentDto.class);
            // w przypadku błędów z grupy 4**, w e mamy metody pozwalające operować na otrzymanej odpowiedzi (np. pobrać jej body i sprawdzić przyczynę lub zweryfikować jaki konkretnie kod otrzymaliśmy)
        } catch (HttpClientErrorException e) {
            throw new RecordNotFoundException("Just to check error handling");
            // w przypadku błędów z grupy 5**, w e mamy metody pozwalające operować na otrzymanej odpowiedzi (np. pobrać jej body i sprawdzić przyczynę lub zweryfikować jaki konkretnie kod otrzymaliśmy)
        } catch (HttpServerErrorException e) {
            throw new RuntimeException();
        }
    }

    //todo dodane - getAll studentów
    //todo nie wiem czy to tutaj ale to aby się wyświetlał pierwszy student bez odświezania strony
    public List<StudentDto> getAll() {
        try {
            ResponseEntity<List<StudentDto>> response = restTemplate.exchange(API_URL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<StudentDto>>() {
                    }
            );

            return response.getBody();

        } catch (HttpClientErrorException e) {
            throw new RecordNotFoundException("Just to check error handling - client error - getAll");
        } catch (HttpServerErrorException e) {
            throw new RuntimeException();
        }
    }

    public List<StudentDto> getStudentsByName(String name) {
        //obsługa błędów w przypadku restTemplate i webClient jest taka sama - łapiemy te same wyjątki
        try {
            //tu wysyłamy synchroniczne zapytanie (odbywa się to niejawnie przy przekształcaniu flux w stream) w celu zwrócenia
            //studentów jako listy z całości w odpowiedzi na wysłane do naszej aplikacji zapytanie
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder.queryParam("name", name).build())
                    .retrieve()
                    .bodyToFlux(StudentDto.class)
                    .toStream()
                    .toList();
        } catch (HttpClientErrorException e) {
            throw new RecordNotFoundException("Just to check error handling");
        } catch (HttpServerErrorException e) {
            throw new RuntimeException();
        }
    }

    public List<StudentDto> getStudentsByEmail(String email) {
        try {
            return restTemplate.exchange(API_URL + "/email?email=" + email,
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<StudentDto>>() {
                            })
                    .getBody();
        } catch (HttpClientErrorException e) {
            throw new RecordNotFoundException("Just to check error handling");
        } catch (HttpServerErrorException e) {
            throw new RuntimeException();
        }
    }

    public List<StudentDto> getStudentsByPhoneNumber(Integer phoneNumber) {
        try {
            return restTemplate.exchange(API_URL + "/phoneNumber?phoneNumber=" + phoneNumber,
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<StudentDto>>() {
                            })
                    .getBody();
        } catch (HttpClientErrorException e) {
            throw new RecordNotFoundException("Just to check error handling");
        } catch (HttpServerErrorException e) {
            throw new RuntimeException();
        }
    }

    public void updateStudent(StudentDto studentDto) {
        try {

            HttpEntity<StudentDto> requestEntity = new HttpEntity<>(studentDto);

            restTemplate.exchange(API_URL + "/editStudent", HttpMethod.PUT, requestEntity,
                            new ParameterizedTypeReference<StudentDto>() {})
                    .getBody();
        } catch (HttpClientErrorException e) {
            throw new RecordNotFoundException("Just to check error handling");
        } catch (HttpServerErrorException e) {
            throw new RuntimeException();
        }
    }

    public StudentDto getStudentByIndex(Long index) {
        try {
            return restTemplate.exchange(
                    API_URL + "/byIndex?index=" + index,
                            HttpMethod.GET, null, new ParameterizedTypeReference<StudentDto>() {
                            })
                    .getBody();
        } catch (HttpClientErrorException e) {
            throw new RecordNotFoundException("Student not found: " + e.getMessage());
        } catch (HttpServerErrorException e) {
            throw new RuntimeException("Server error during student retrieval");
        }
    }

}
