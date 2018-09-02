package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.users.AppUser;
import com.paymentsystem.ngpuppies.repositories.base.AppUserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AppUserRepositoryImpl implements AppUserRepository {
    private SessionFactory sessionFactory;

    public AppUserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public AppUser loadById(Integer id) {
        AppUser user = null;
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            user = session.get(AppUser.class,id);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<AppUser> getAll() {
        List<AppUser> appUsers = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            appUsers = session.createQuery("FROM AppUser").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return appUsers;
    }

    @Override
    public AppUser loadByUsername(String username) {
        AppUser appUser = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String query = String.format("FROM AppUser WHERE username = '%s'", username);
            List<AppUser> allUsers = session.createQuery(query).list();
            session.getTransaction().commit();

            if (!allUsers.isEmpty()) {
                appUser = allUsers.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appUser;
    }

    @Override
    public boolean deleteByUsername(String username) {
        AppUser user = loadByUsername(username);
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

        System.out.println("User with username: " + username + " wasn't found!");
        return false;
    }
}