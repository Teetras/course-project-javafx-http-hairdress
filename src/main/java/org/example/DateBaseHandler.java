package org.example;

import org.example.model.User;
import org.example.utils.SessionUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class DateBaseHandler {
    private SessionFactory sessionFactory;

    public DateBaseHandler() {
        Configuration configuration = new Configuration().configure("/hibernate.cfg.xml");
        sessionFactory = configuration.buildSessionFactory();

        User user = new User();
        SessionUtils.saveEntity(user);
    }

    public void signUpUser(String name, String phone, String login, String password) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            User user = new User();
            user.setLogin(login);
            user.setPassword(password);

            session.persist(user);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public boolean checkConnection() {
        try {
            Session session = sessionFactory.openSession();
            session.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}