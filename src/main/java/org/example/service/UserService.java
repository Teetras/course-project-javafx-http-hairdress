package org.example.service;

import org.example.model.User;

import java.util.List;

public interface UserService {
    boolean addUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(int id);
    User login(String login, String pass);
    List<User> showPeople();
    User findUserById(int id);
}
