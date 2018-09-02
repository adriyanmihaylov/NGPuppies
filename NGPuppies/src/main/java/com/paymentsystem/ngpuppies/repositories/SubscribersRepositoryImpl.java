package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.repositories.base.SubscribersRepository;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Repository
public class SubscribersRepositoryImpl implements SubscribersRepository {
    private SessionFactory factory;

    public SubscribersRepositoryImpl(SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public List<Subscriber> getAll() {
        List<Subscriber> subscribers = new ArrayList<>();
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            subscribers = session.createQuery("FROM Subscriber ").list();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return subscribers;
    }

    @Override
    public Subscriber getByNumber(String phoneNumber) {
        Subscriber subscriber = null;
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            String query = String.format("FROM Subscriber s where s.phoneNumber = '%s'", phoneNumber);
            List<Subscriber> subscribers = session.createQuery(query).list();
            if (subscribers.size() == 0) {
                System.out.println("No such subscriber, need to create new");
            } else {
                return subscribers.get(0);
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No such subscriber");
        }

        return subscriber;
    }

    @Override
    public boolean deleteByNumber(String phoneNumber) {
        Subscriber subscriberToBeDeleted = null;
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            subscriberToBeDeleted = (Subscriber) session.createQuery("FROM Subscriber s where" +
                    " s.phone = " + phoneNumber).list().get(0);
            session.delete(subscriberToBeDeleted);
            tx.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No such subscriber");
        }
        return false;
    }

    @Override
    public boolean update(Subscriber updatedSubscriber) {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(updatedSubscriber);
            tx.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No such subscriber");
        }
        return false;
    }

    @Override
    public boolean create(Subscriber subscriber) throws Exception {
        try (Session session = factory.openSession()) {
            subscriber.setPhone(subscriber.getPhone());
            session.beginTransaction();
            session.save(subscriber);
            session.getTransaction().commit();

            return true;
        } catch (JDBCException e) {
            String message = e.getSQLException().toString().toLowerCase();

            String key = message.substring(message.lastIndexOf(" ") + 1).replace("'", "");

            String errorMessage;
            switch (key) {
                case "phonenumber":
                    errorMessage = "Phone number is present";
                    break;
                case "egn":
                    errorMessage = "Egn is present";
                    break;
                default:
                    throw new Exception("Something went wrong when creating new subscriber");
            }
            throw new SQLException(errorMessage, e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}