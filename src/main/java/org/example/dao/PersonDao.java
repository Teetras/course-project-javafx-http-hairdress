package org.example.dao;

import org.example.model.Person;

import java.util.List;

public interface PersonDao {
    boolean addPerson(Person person);
    boolean updatePerson(Person person);
    boolean deletePerson(int id);
    List<Person> getAllPersons();
    Person findPersonById(int id);
}