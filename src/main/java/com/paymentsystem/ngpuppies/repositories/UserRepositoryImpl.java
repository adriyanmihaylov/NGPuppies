package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.IpAddress;
import com.paymentsystem.ngpuppies.models.users.User;
import com.paymentsystem.ngpuppies.repositories.base.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public User loadById(Integer id) {
        User user = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            user = session.get(User.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            users = session.createQuery("FROM User").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public User loadByUsername(String username) {
        User user = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String query = String.format("FROM User WHERE username = '%s'", username);
            List<User> allUsers = session.createQuery(query).list();
            session.getTransaction().commit();

            if (!allUsers.isEmpty()) {
                user = allUsers.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public boolean delete(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("" +
                    " DELETE FROM User u" +
                    " WHERE u.username=:user");
            query.setParameter("user", username);

            session.beginTransaction();
            int result = query.executeUpdate();
            session.getTransaction().commit();

            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addIpAddressToUser(User user, IpAddress ipAddress) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(ipAddress);
            user.addIpAddress(ipAddress);
            session.update(user);
            transaction.commit();

            return true;
        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (RuntimeException exception) {
                System.out.println("Couldn't roll back transaction!");
            }
            e.printStackTrace();
        }
        return false;
    }
}