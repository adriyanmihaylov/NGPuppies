package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.ApplicationUser;
import com.paymentsystem.ngpuppies.repositories.base.GenericUserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ApplicationUserUserRepositoryImpl implements GenericUserRepository<ApplicationUser> {
    private SessionFactory sessionFactory;

    public ApplicationUserUserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<ApplicationUser> getAll() {
        List<ApplicationUser> applicationUsers = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            applicationUsers = session.createQuery("FROM ApplicationUser").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return applicationUsers;
    }

    @Override
    public ApplicationUser getByUsername(String username) {
        ApplicationUser applicationUser = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String query = String.format("FROM ApplicationUser a WHERE a.username = '%s'", username);
            List<ApplicationUser> allUsers = session.createQuery(query).list();
            session.getTransaction().commit();

            if (!allUsers.isEmpty()) {
                applicationUser = allUsers.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return applicationUser;
    }

}