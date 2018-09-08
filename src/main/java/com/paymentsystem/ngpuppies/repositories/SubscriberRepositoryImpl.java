package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.repositories.base.SubscriberRepository;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SubscriberRepositoryImpl implements SubscriberRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Subscriber> getAll() {
        List<Subscriber> subscribers = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            subscribers = session.createQuery("FROM Subscriber ").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return subscribers;
    }

    @Override
    public Subscriber getSubscriberByPhoneNumber(String phoneNumber) {
        Subscriber subscriber = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String query = String.format("FROM Subscriber where phone = '%s'", phoneNumber);
            List<Subscriber> foundSubscribers = session.createQuery(query).list();
            if (foundSubscribers.size() != 0) {
                subscriber = foundSubscribers.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subscriber;
    }

    @Override
    public boolean create(Subscriber subscriber) throws SQLException {
        Session session = null;
        Transaction transaction = null;
        try {
            Address address = subscriber.getAddress();

            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(address);
            session.save(subscriber);
            transaction.commit();
            session.close();

            return true;
        } catch (JDBCException e) {
            try {
                transaction.rollback();
            } catch (RuntimeException exception) {
                System.out.println("Couldn't roll back transaction!");
            }
            String message = e.getSQLException().toString().toLowerCase();

            String key = message.substring(message.lastIndexOf(" ") + 1).replace("'", "");

            String errorMessage = getDatabaseErrorMessage(e, key);
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

    @Override
    public boolean update(Subscriber subscriber) throws SQLException {
        Session session = null;
        Transaction transaction = null;
        try {
            Address address = subscriber.getAddress();

            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.update(address);
            session.update(subscriber);
            transaction.commit();
            session.close();

            System.out.printf("UPDATED: SUBSCRIBER  Id: %d Phone: %s\n", subscriber.getId(), subscriber.getPhone());
            return true;
        } catch (PersistenceException e) {
            try {
                transaction.rollback();
            } catch (RuntimeException exception) {
                System.out.println("Couldn't roll back transaction!");
            }
            String message = e.getCause().getCause().toString().toLowerCase();
            String key = message.substring(message.lastIndexOf(" ") + 1).replace("'", "");
            String errorMessage = getDatabaseErrorMessage(e, key);
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
    public boolean delete(Subscriber subscriber) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(subscriber);
            session.getTransaction().commit();

            System.out.println("DELETED Subscriber " + subscriber.getPhone());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<Subscriber> getAllSubscribersByService(Integer serviceId) {
        List<Subscriber> subscribers = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            String query = String.format(
                    "SELECT s FROM Subscriber s" +
                            " JOIN s.subscriberServices srv" +
                            " WHERE srv.id=%s", serviceId);
            session.beginTransaction();
            subscribers = session.createQuery(query).list();
            session.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return subscribers;
    }

    @Override
    public List<Subscriber> getTenAllTimeSubscribersOfClientWithBiggestBillsPaid(Integer clientId) {
        try (Session session = sessionFactory.openSession()) {
            String query = String.format(
                    " FROM Subscriber s" +
                    " WHERE s.client.id=%s" +
                    " ORDER BY s.totalAmount DESC", clientId);
            session.beginTransaction();
            List<Subscriber> subscribers = session.createQuery(query).list();
            session.getTransaction().commit();

            return subscribers;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
    @Override
    public Object[] getSubscriberOfClientWithBiggestAmountPaid(Integer clientId, String fromDate, String toDate) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String query = String.format(
                    "SELECT s, SUM(i.BGNAmount) as totalAmount" +
                            " FROM Subscriber s" +
                            " JOIN Invoice i ON s.id = i.subscriber.id" +
                            " WHERE s.client.id=%s" +
                            " AND i.payedDate IS NOT NULL " +
                            " GROUP BY s" +
                            " ORDER BY totalAmount DESC", clientId);
            Object[] result = session.createQuery(query).setMaxResults(1).list().toArray();
            session.getTransaction().commit();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Double getSubscriberAverageInvoiceSumPaid(Integer subscriberId, String fromDate, String toDate) {
        try (Session session = sessionFactory.openSession()) {
            String query = String.format(
                    "SELECT avg(i.BGNAmount)" +
                            " FROM Subscriber s" +
                            " JOIN Invoice i ON s.id=i.subscriber.id" +
                            " WHERE s.id=%s" +
                            " AND i.payedDate >= '%s' and i.payedDate <= '%s'", subscriberId, fromDate, toDate);
            List<Double> avgAmount = session.createQuery(query).list();
            if (avgAmount.size() > 0) {
                if (avgAmount.get(0) != null) {
                    return avgAmount.get(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0D;
    }


    private String getDatabaseErrorMessage(PersistenceException e, String key) {
        String errorMessage;
        switch (key) {
            case "phonenumber":
                errorMessage = "Phone number is present";
                break;
            case "egn":
                errorMessage = "Egn is present";
                break;
            default:
                e.printStackTrace();
                errorMessage = "Something went wrong! Try again later!";
                break;
        }
        return errorMessage;
    }
}