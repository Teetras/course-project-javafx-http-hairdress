package org.example.utils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.model.HairdressingServices;
import org.example.model.Order;
import org.example.sessionFactory.SessionFactoryImpl;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;


public class SessionUtils {
    public static boolean saveEntity(Object entity) {
        boolean isAdded = false;
        try {
            Session session = SessionFactoryImpl.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.save(entity);
            tx.commit();
            session.close();
            isAdded = true;
        }
        catch (NoClassDefFoundError e) {
            System.out.println("Exception: " + e);
        }
        return isAdded;
    }

    public static boolean updateEntity(Object entity) {
        boolean isUpdated = false;
        try {
            Session session = SessionFactoryImpl.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.update(entity);
            tx.commit();
            session.close();
            isUpdated = true;
        }
        catch (NoClassDefFoundError e) {
            System.out.println("Exception: " + e);
        }
        return isUpdated;
    }

    public static <T> boolean deleteEntity(int id, Class<T> clazz) {
        boolean isDeleted = false;
        try {
            Session session = SessionFactoryImpl.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();

            T entity = session.load(clazz, id);

            if (entity instanceof HairdressingServices) {
                HairdressingServices hairdressingService = (HairdressingServices) entity;

                // Получаем связанные Order
                List<Order> orders = session.createQuery("FROM Order o WHERE o.hairdressingService = :service")
                        .setParameter("service", hairdressingService)
                        .getResultList();

                // Удаляем связанные Order
                for (Order order : orders) {
                    session.delete(order);
                }
            }

            session.delete(entity);
            transaction.commit();
            session.close();
            isDeleted = true;
        } catch (NoClassDefFoundError e) {
            System.out.println("Exception: " + e);
        }
        return isDeleted;
    }
    public static <F, T> T find (Class<T> clazz, F criteriaField, String criteriaFieldName) {
        T entity = null;
        try {
            Session session = SessionFactoryImpl.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
            Root<T> companyRoot = criteriaQuery.from(clazz);
            criteriaQuery.select(companyRoot)
                    .where(criteriaBuilder.equal(companyRoot.get(criteriaFieldName), criteriaField));
            entity = session.createQuery(criteriaQuery).getSingleResult();
            transaction.commit();
            session.close();
        }
        catch (NoClassDefFoundError e) {
            System.out.println("Exception: " + e);
        }
        return entity;
    }
}
