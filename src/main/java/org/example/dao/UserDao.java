package org.example.dao;

import org.example.model.User;

import java.util.List;

public interface UserDao {
    boolean addUser(User User);
    boolean updateUser(User User);
    boolean deleteUser(int id);
    List<User> showPeople();
    User findByLogin(String login);
    User findUserById(int id);
}