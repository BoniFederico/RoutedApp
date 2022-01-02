package com.federicoboni.database.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.federicoboni.database.entities.RouteType;

import com.federicoboni.database.manager.HibernateManager;

public class RouteTypeDao {

    public void saveType(RouteType type) {
        Transaction transaction = null;
        try (Session session = HibernateManager.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();

            session.save(type);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public RouteType getType(int id) {

        Transaction transaction = null;
        RouteType type = null;
        try (Session session = HibernateManager.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();

            type = session.get(RouteType.class, id);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return type;
    }

    @SuppressWarnings("unchecked") // in order to avoid type safety warning
    public List<RouteType> getAllType() {

        Transaction transaction = null;
        List<RouteType> listOfType = null;
        try (Session session = HibernateManager.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();

            listOfType = session.createQuery("from RouteType").getResultList();

            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return listOfType;
    }
}