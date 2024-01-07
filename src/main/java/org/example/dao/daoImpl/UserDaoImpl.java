package org.example.dao.daoImpl;


import org.example.dao.UserDao;
import org.example.model.User;
import org.example.sessionFactory.SessionFactoryImpl;
import org.example.utils.SessionUtils;

import java.util.List;

public class UserDaoImpl implements UserDao {

    @Override
    public boolean updateUser(User User) {
        return SessionUtils.updateEntity(User);
    }

    @Override
    public boolean addUser(User User) {
        System.out.println(User+"  2");

        return SessionUtils.saveEntity(User);
    }

    @Override
    public boolean deleteUser(int id) {
        return SessionUtils.deleteEntity(id, User.class);
    }

    @Override
    public List<User> showPeople() {
        return (List<User>) SessionFactoryImpl.getSessionFactory()
                .openSession()
                .createQuery("FROM User")
                .list();
    }

    @Override
    public User findByLogin(String login) {
        return SessionUtils.find(User.class, login, "login");
    }

    @Override
    public User findUserById(int id) {
        return SessionUtils.find(User.class, id, "UserId");
    }
}
