package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.Admin;
import com.paymentsystem.ngpuppies.repositories.base.AdminRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AdminRepositoryImpl implements AdminRepository {
    private SessionFactory sessionFactory;

    public AdminRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Admin> getAll() {
        List<Admin> admins = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            admins = session.createQuery("FROM Admin").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return admins;
    }
}