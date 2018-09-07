package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.users.User;
import com.paymentsystem.ngpuppies.repositories.base.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
    public boolean delete(User user) {
        if (user != null) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                session.delete(user);
                session.getTransaction().commit();
                System.out.println("DELETED User Id:" + user.getId() + " username: " + user.getUsername());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("User with username: " + user.getUsername() + " wasn't found!");
        return false;
    }
}