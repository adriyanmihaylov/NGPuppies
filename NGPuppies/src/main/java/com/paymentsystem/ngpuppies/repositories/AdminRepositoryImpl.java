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

    @Override
    public Admin getByUsername(String username) {
        Admin admin = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String query = String.format("FROM Admin a where a.username LIKE '%s'", username);
            List<Admin> admins = session.createQuery(query).list();
            session.getTransaction().commit();

            if (!admins.isEmpty()) {
                admin = admins.get(0);
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
            String query = String.format("FROM Admin a where a.email LIKE '%s'", email);
            List<Admin> admins = session.createQuery(query).list();
            session.getTransaction().commit();

            if (!admins.isEmpty()) {
                admin = admins.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return admin;
    }

    @Override
    public boolean create(Admin admin) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(admin);
            session.getTransaction().commit();

            System.out.println("CREATED User Id: " + admin.getId() + " username:" + admin.getUsername());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteByUsername(String username) {
        Admin admin = getByUsername(username);
        if(admin != null) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                session.delete(admin);
                session.getTransaction().commit();
                System.out.println("DELETED: Admin: " + admin.getUsername());
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}