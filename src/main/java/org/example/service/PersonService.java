package org.example.service;

import org.example.model.Person;

import java.util.List;

public interface PersonService {
    boolean addPerson(Person user);
    boolean updatePerson(Person  user);
    boolean deletePerson(int id);

    List<Person > getAllPersons();
    Person  findPersonById(int id);
}
