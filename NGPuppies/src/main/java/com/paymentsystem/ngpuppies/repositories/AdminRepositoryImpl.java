package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.exceptions.EmailIsPresentException;
import com.paymentsystem.ngpuppies.exceptions.UsernameIsPresentException;
import com.paymentsystem.ngpuppies.models.users.Admin;
import com.paymentsystem.ngpuppies.models.users.Authority;
import com.paymentsystem.ngpuppies.repositories.base.AdminRepository;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AdminRepositoryImpl implements AdminRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public Admin loadByUsername(String username) {
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
    public boolean create(Admin admin) throws Exception {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            admin.setEnabled(false);
            session.save(admin);
            session.getTransaction().commit();
            System.out.println("CREATED ADMIN Id: " + admin.getId() + " username:" + admin.getUsername());

            return true;
        } catch (JDBCException e) {
            String message = e.getSQLException().toString().toLowerCase();

            String key = message.substring(message.lastIndexOf(" ") + 1).replace("'", "");

            String errorMessage;
            switch (key) {
                case "username":
                    errorMessage = "Username is present";
                    break;
                case "adminemail":
                    errorMessage = "Email is present";
                    break;
                default:
                    throw new Exception("Something went wrong in the database when registering new admin: " + admin.getUsername());
            }

            throw new SQLException(errorMessage, e);
        } catch (Exception e) {

        }

        return false;
    }
}