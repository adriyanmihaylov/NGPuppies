package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.Admin;
import com.paymentsystem.ngpuppies.repositories.base.AdminRepository;
import com.paymentsystem.ngpuppies.repositories.base.GenericUserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AdminRepositoryImpl implements AdminRepository, GenericUserRepository<Admin> {
    @Autowired
    private SessionFactory sessionFactory;

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

    @Override
    public Admin getByUsername(String username) {
        Admin admin = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String query = String.format("FROM Admin a WHERE a.username = '%s'", username);
            List<Admin> allAdmins = session.createQuery(query).list();
            session.getTransaction().commit();

            if (!allAdmins.isEmpty()) {
                admin = allAdmins.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return admin;
    }

    @Override
    public Admin getByEmail(String email) {
        Admin admin = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String query = String.format("FROM Admin a WHERE a.email = '%s'", email);
            List<Admin> allAdmins = session.createQuery(query).list();
            session.getTransaction().commit();

            if (!allAdmins.isEmpty()) {
                admin = allAdmins.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return admin;
    }

    @Override
    public boolean checkIfEmailIsPresent(String email) {
        return getByEmail(email) != null;
    }

    @Override
    public boolean create(Admin model) {
        if (model.getUsername() == null || model.getPassword() == null || model.getEmail() == null) {
            return false;
        }

        if (getByUsername(model.getUsername()) != null || getByEmail(model.getEmail()) != null) {
            return false;
        }

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            model.setRole("ADMIN");
            session.save(model);
            session.getTransaction().commit();
            System.out.println("CREATED ADMIN Id: " + model.getId() + " username:" + model.getUsername());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteByUsername(String username) {
        return false;
    }
}