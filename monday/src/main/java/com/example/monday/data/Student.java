package com.example.monday.data;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {

    public Student(String name, StudentUnit unit, Integer phoneNumber, String email) {
        this.name = name;
        this.unit = unit;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    public Student(String name, StudentUnit unit, Long index, Integer phoneNumber, String email) {
        this.name = name;
        this.unit = unit;
        this.index = index;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private Integer phoneNumber;
    private String email;
    @Enumerated(EnumType.STRING)
    private StudentUnit unit;
    @Setter
    private Long index;

}
