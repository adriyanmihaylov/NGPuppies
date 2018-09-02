package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.users.Authority;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;
import com.paymentsystem.ngpuppies.repositories.base.AuthorityRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorityRepositoryImpl implements AuthorityRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Authority getById(int id) {
        Authority authority = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            authority = session.get(Authority.class, id);
            session.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return authority;
    }

    @Override
    public Authority getByName(AuthorityName name) {
        Authority authority = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String query = String.format("FROM Authority WHERE name = '%s'", name);
            List<Authority> allAuthorities = session.createQuery(query).list();
            session.getTransaction().commit();

            if (!allAuthorities.isEmpty()) {
                authority = allAuthorities.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return authority;
    }
}