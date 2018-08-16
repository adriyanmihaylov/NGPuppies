package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.Client;
import com.paymentsystem.ngpuppies.repositories.base.ClientRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientRepositoryImpl implements ClientRepository {
    private SessionFactory sessionFactory;

    public ClientRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
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
            String query = String.format("FROM Client c where c.username LIKE '%s'", username);
            List<Client> clients = session.createQuery(query).list();
            session.getTransaction().commit();

            if (!clients.isEmpty()) {
                client = clients.get(0);
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
            String query = String.format("FROM Client c where c.eik LIKE '%s'", eik);
            List<Client> clients = session.createQuery(query).list();
            session.getTransaction().commit();

            if (!clients.isEmpty()) {
                client = clients.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return client;
    }

    @Override
    public boolean create(Client client) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(client);
            session.getTransaction().commit();

            System.out.println("CREATED Client Id: " + client.getId() + " username:" + client.getUsername());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(Client client) {
        return false;
    }

    @Override
    public boolean deleteByUsername(String username) {
        Client client = getByUsername(username);
        if (client != null) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                session.delete(client);
                session.getTransaction().commit();
                System.out.println("DELETED: Client username: " + client.getUsername());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}