package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.TelecomServ;
import com.paymentsystem.ngpuppies.repositories.base.TelecomServRepository;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TelecomServRepositoryImpl implements TelecomServRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<TelecomServ> getAll() {
        List<TelecomServ> currencies = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            currencies = session.createQuery("FROM TelecomServ").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return currencies;
    }

    @Override
    public TelecomServ getByName(String name) {
        TelecomServ telecomServ = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String query = String.format("FROM TelecomServ  WHERE name = '%s'", name);
            List<TelecomServ> allOfferedServices = session.createQuery(query).list();
            session.getTransaction().commit();

            if (!allOfferedServices.isEmpty()) {
                telecomServ = allOfferedServices.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return telecomServ;
    }

    @Override
    public boolean create(TelecomServ telecomServ) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(telecomServ);
            session.getTransaction().commit();
            System.out.println("CREATED New Service " + telecomServ.getName());

            return true;
        } catch (JDBCException e) {
            throw new SQLException("Service is present", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalException("Something went wrong! Please try again later!");
        }
    }

    @Override
    public boolean update(String oldName,String newName) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("" +
                    " UPDATE TelecomServ AS t" +
                    " SET t.name=:newName" +
                    " WHERE t.name=:oldName");
            query.setParameter("oldName",oldName);
            query.setParameter("newName",newName);
            int result = query.executeUpdate();
            session.getTransaction().commit();
            return result > 0;
        } catch (PersistenceException e) {
            throw new SQLException("Service already exist", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(String telecomServName) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("" +
                    " DELETE TelecomServ AS t" +
                    " WHERE t.name=:serviceName");
            query.setParameter("serviceName", telecomServName);

            int result = query.executeUpdate();
            session.getTransaction().commit();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<TelecomServ> getAllServicesOfClientByClientId(int clientId) {
        List<TelecomServ> telecomServs = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("" +
                    " SELECT t FROM TelecomServ t, Subscriber s" +
                    " WHERE s.client.id = :clientId" +
                    " AND t in elements(s.subscriberServices)");
            query.setParameter("clientId", clientId);
            session.beginTransaction();
            telecomServs = query.list();
            session.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return telecomServs;
    }
}