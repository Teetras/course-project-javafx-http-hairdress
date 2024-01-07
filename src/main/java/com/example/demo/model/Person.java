package com.example.demo.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Person {
    private int id;
    private String name;
    private String phone;


    public Person(String name, String phone, User user) {
        this.name = name;
        this.phone = phone;

    }
}