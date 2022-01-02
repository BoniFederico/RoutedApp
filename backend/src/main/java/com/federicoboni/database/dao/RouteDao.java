package com.federicoboni.database.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

import javax.persistence.Query;

import com.federicoboni.database.entities.Route;

import com.federicoboni.database.manager.HibernateManager;

public class RouteDao {

    public void saveRoute(Route route) {
        Transaction transaction = null;
        try (Session session = HibernateManager.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();

            session.save(route);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void updateRoute(Route route) {
        Transaction transaction = null;
        try (Session session = HibernateManager.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();

            session.update(route);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteRoute(int id) {

        Transaction transaction = null;
        try (Session session = HibernateManager.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();

            Route route = session.get(Route.class, id);
            if (route != null) {
                session.delete(route);
                System.out.println("Route is deleted");
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

    public Route getRoute(int id) {

        Transaction transaction = null;
        Route route = null;
        try (Session session = HibernateManager.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();

            route = session.get(Route.class, id);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return route;
    }

    @SuppressWarnings("unchecked") // in order to avoid type safety warning
    public List<Route> getRoutes(int userid) {

        Transaction transaction = null;
        List<Route> listOfRoute = null;
        try (Session session = HibernateManager.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();

            Query query = session.createQuery("from Route R WHERE R.user=:userid");
            query.setParameter("userid", userid);
            listOfRoute = query.getResultList();

            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return listOfRoute;
    }

}