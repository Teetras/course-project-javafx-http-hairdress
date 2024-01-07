package org.example.service.serviceImpl;

import lombok.NoArgsConstructor;
import org.example.dao.daoImpl.UserDaoImpl;
import org.example.model.User;
import org.example.service.UserService;
import org.hibernate.HibernateError;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

@NoArgsConstructor
public class UserServiceImpl implements UserService {
    org.example.dao.UserDao UserDao = new UserDaoImpl();
    @Override
    public User login(String login, String pass) {
        User user = null;
        try {
            user = UserDao.findByLogin(login);
            if (BCrypt.checkpw(pass, user.getPassword())) {
                return user;
            } else System.out.println("Auth Error");
        } catch (HibernateError e) {
            throw new RuntimeException(e);

        }
        return null;
    }
    @Override
    public boolean addUser(User user) {
        System.out.println(user+" 1  ");

        String hash = BCrypt.gensalt();
        String hashPass = BCrypt.hashpw(user.getPassword(), hash);
        user.setPassword(hashPass);
        boolean isAdded = false;
        try {
            if (UserDao.addUser(user))
                isAdded = true;
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return isAdded;
    }

    @Override
    public boolean updateUser(User User) {
        boolean isUpdated = false;
        try {
            if (UserDao.updateUser(User))
                isUpdated = true;
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return isUpdated;
    }

    @Override
    public boolean deleteUser(int id) {
        boolean isDeleted = false;
        try {
            if (UserDao.deleteUser(id))
                isDeleted = true;
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return isDeleted;
    }



    @Override
    public List<User> showPeople() {

        List<User> people = null;
        try {
            people = UserDao.showPeople();
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return people;
    }

    @Override
    public User findUserById(int id) {
        User user = null;
        try {
            user = UserDao.findUserById(id);
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return user;
    }
}
