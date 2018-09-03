package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.repositories.base.ClientRepository;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientRepositoryImpl implements ClientRepository {
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
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            client.setEnabled(Boolean.TRUE);
            session.save(client);
            session.getTransaction().commit();
            System.out.println("CREATED CLIENT Id: " + client.getId() + " username:" + client.getUsername());

            return true;
        } catch (JDBCException e) {
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
                    System.out.println("Something went wrong in the database on client CREATE!");
                    throw new Exception();
            }
            throw new SQLException(errorMessage, e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(Client client) throws Exception {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(client);
            session.getTransaction().commit();
            System.out.printf("UPDATED: CLIENT  Id: %d\n", client.getId());
            return true;
        } catch (PersistenceException e) {
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
            e.printStackTrace();
        }

        return false;
    }
}