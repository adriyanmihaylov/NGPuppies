package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.ClientDetail;
import com.paymentsystem.ngpuppies.repositories.base.ClientDetailRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDetailRepositoryImpl implements ClientDetailRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean create(ClientDetail clientDetail) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(clientDetail);
            session.getTransaction().commit();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Client was not created");
        }

        return false;
    }

    @Override
    public boolean update(ClientDetail clientDetail) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(clientDetail);
            session.getTransaction().commit();
            System.out.println("Client ID: " + clientDetail.getId() + "updated!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(ClientDetail clientDetail) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(clientDetail);
            session.getTransaction().commit();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}