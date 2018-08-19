package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.Client;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.repositories.base.SubscribersRepository;
import com.sun.org.apache.bcel.internal.generic.FADD;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

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
                    " s.phoneNumber = " + phoneNumber).list().get(0);
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
    public boolean create(Subscriber subscriber) {
        if (subscriber.getClientUsername() != null) {
            try (Session session = factory.openSession()) {
                Transaction tx = session.beginTransaction();
                String query = String.format("From Client c where c.username = '%s'", subscriber.getClientUsername());
                Client client = (Client) session.createQuery(query).
                        list().get(0);
                subscriber.setClient(client);
                session.save(subscriber);
                tx.commit();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Subscriber was not saved");
            }
        } else {
            try (Session session = factory.openSession()) {
                Transaction tx = session.beginTransaction();
                session.save(subscriber);
                tx.commit();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Subscriber was not saved");
            }
        }
        return false;
    }
}