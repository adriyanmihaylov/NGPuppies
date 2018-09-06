package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.ClientDetail;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.repositories.base.ClientRepository;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import javax.xml.soap.Detail;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientRepositoryImpl implements ClientRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public Client loadByUsername(String username) {
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
    public Client loadByEik(String eik) {
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
    public boolean create(Client client) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            ClientDetail detail = client.getDetails();
            client.setEnabled(Boolean.TRUE);
            client.setPassword(passwordEncoder.encode(client.getPassword()));

            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(detail);
            session.save(client);
            transaction.commit();
            session.close();

            System.out.println("CREATED CLIENT Id: " + client.getId() + " username:" + client.getUsername());
            return true;
        } catch (JDBCException e) {
            try {
                transaction.rollback();
            } catch (RuntimeException exception) {
                System.out.println("Couldn't roll back transaction!");
            }
            String message = e.getSQLException().toString().toLowerCase();
            String key = message.substring(message.lastIndexOf(" ") + 1).replace("'", "");

            String errorMessage;
            switch (key) {
                case "username":
                    errorMessage = "Username is present";
                    break;
                case "clienteik":
                    errorMessage = "Eik is present";
                    break;
                default:
                    System.out.println("Something went wrong in the database method CREATE client!");
                    throw new Exception();
            }
            throw new SQLException(errorMessage, e);
        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (RuntimeException exception) {
                System.out.println("Couldn't roll back transaction!");
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return false;
    }

    @Override
    public boolean update(Client client) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            ClientDetail detail = client.getDetails();
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            if(detail == null || detail.getId() == 0 ) {
                session.save(detail);
            } else {
                session.update(detail);
            }
            session.update(client);
            transaction.commit();
            session.close();

            System.out.printf("UPDATED: CLIENT  Id: %d\n", client.getId());
            return true;
        } catch (PersistenceException e) {
            try {
                transaction.rollback();
            } catch (RuntimeException exception) {
                System.out.println("Couldn't roll back transaction!");
            }

            String message = e.getCause().getCause().toString().toLowerCase();
            String key = message.substring(message.lastIndexOf(" ") + 1).replace("'", "");
            String errorMessage;

            switch (key) {
                case "username":
                    errorMessage = "Username is present";
                    break;
                case "clienteik":
                    errorMessage = "Eik is present";
                    break;
                default:
                    System.out.println("Something went wrong in the database on client UPDATE!");
                    throw new Exception();
            }
            throw new SQLException(errorMessage, e);
        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (RuntimeException exception) {
                System.out.println("Couldn't roll back transaction!");
            }

            e.printStackTrace();
        } finally {
            if(session != null) {
                session.close();
            }
        }

        return false;
    }
}