package com.federicoboni.database.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.federicoboni.database.entities.User;
import com.federicoboni.database.manager.HibernateManager;

public class UserDao {

    public void saveUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateManager.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();

            session.save(user);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateManager.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();

            session.update(user);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteUser(int id) {

        Transaction transaction = null;
        try (Session session = HibernateManager.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();

            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                System.out.println("user is deleted");
            }

            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public User getUser(int id) {

        Transaction transaction = null;
        User user = null;
        try (Session session = HibernateManager.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();

            user = session.get(User.class, id);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return user;
    }

    public User getUser(String identifier) {

        List<User> users = getAllUser();
        User user = null;
        try {
            user = users.stream()
                    .filter(p -> p.getUsername().equals(identifier))
                    .collect(Collectors.toList()).get(0);

        } catch (IndexOutOfBoundsException e1) {
            try {
                user = users.stream()
                        .filter(p -> p.getEmail().equals(identifier))
                        .collect(Collectors.toList()).get(0);

            } catch (IndexOutOfBoundsException e2) {
                return null;
            }
        }
        return user;
    }

    @SuppressWarnings("unchecked") // in order to avoid type safety warning
    public List<User> getAllUser() {

        Transaction transaction = null;
        List<User> listOfUser = null;
        try (Session session = HibernateManager.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();

            listOfUser = session.createQuery("from User").getResultList();

            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return listOfUser;
    }
}