package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.Client;
import com.paymentsystem.ngpuppies.repositories.base.ClientRepository;
import com.paymentsystem.ngpuppies.repositories.base.GenericUserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientRepositoryImpl implements ClientRepository,GenericUserRepository<Client> {
    @Autowired
    private SessionFactory sessionFactory;

    public List<Client> getAll() {
        List<Client> clients = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            clients = session.createQuery("FROM Client").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return clients;
    }

    @Override
    public Client getByUsername(String username) {
        Client client = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String query = String.format("FROM Client c WHERE c.username = '%s'", username);
            List<Client> allClients = session.createQuery(query).list();
            session.getTransaction().commit();

            if (!allClients.isEmpty()) {
                client = allClients.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public Client getByEik(String eik) {
        Client client = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String query = String.format("FROM Client c WHERE c.eik = '%s'", eik);
            List<Client> allClients = session.createQuery(query).list();
            session.getTransaction().commit();

            if (!allClients.isEmpty()) {
                client = allClients.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public boolean checkIfEikIsPresent(String eik) {
        return getByEik(eik) != null;
    }


    @Override
    public boolean create(Client model) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            model.setRole("CLIENT");
            session.save(model);
            session.getTransaction().commit();

            System.out.println("CREATED Client Id: " + model.getId() + " username:" + model.getUsername());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteByUsername(String username) {
        return false;
    }
}