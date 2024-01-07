package org.example.dao.daoImpl;


import org.example.dao.PersonDao;
import org.example.model.Person;
import org.example.sessionFactory.SessionFactoryImpl;
import org.example.utils.SessionUtils;

import java.util.List;

public class PersonDaoImpl implements PersonDao {

    @Override
    public boolean addPerson(Person person) {
        return SessionUtils.saveEntity(person);
    }

    @Override
    public boolean updatePerson(Person person) {
        return SessionUtils.updateEntity(person);
    }

    @Override
    public boolean deletePerson(int id) {
        return SessionUtils.deleteEntity(id, Person.class);
    }

    @Override
    public List<Person> getAllPersons() {
        System.out.println((List<Person>) SessionFactoryImpl.getSessionFactory()
                .openSession()
                .createQuery("FROM Person")
                .list() + " LIST");
        return (List<Person>) SessionFactoryImpl.getSessionFactory()
                .openSession()
                .createQuery("FROM Person")
                .list();
    }

    @Override
    public Person findPersonById(int id) {
        return SessionUtils.find(Person.class, id, "id");
    }
}
