package org.example.service.serviceImpl;

import org.example.dao.PersonDao;
import org.example.dao.daoImpl.PersonDaoImpl;
import org.example.model.Person;
import org.example.service.PersonService;
import org.hibernate.HibernateError;

import java.util.List;

public class PersonServiceImpl implements PersonService {
    PersonDao personDao = new PersonDaoImpl();

    @Override
    public boolean addPerson(Person person) {
        boolean isAdded = false;
        try {
            if (personDao.addPerson(person))
                isAdded = true;
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return isAdded;
    }

    @Override
    public boolean updatePerson(Person person) {
        boolean isUpdated = false;
        try {
            if (personDao.updatePerson(person))
                isUpdated = true;
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return isUpdated;
    }

    @Override
    public boolean deletePerson(int id) {
        boolean isDeleted = false;
        try {
            if (personDao.deletePerson(id))
                isDeleted = true;
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return isDeleted;
    }

    @Override
    public Person findPersonById(int id) {
        Person person = null;
        try {
            person = personDao.findPersonById(id);
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return person;
    }

    @Override
    public List<Person> getAllPersons() {
        List<Person> persons = null;
        try {
            persons = personDao.getAllPersons();
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return persons;
    }
}
