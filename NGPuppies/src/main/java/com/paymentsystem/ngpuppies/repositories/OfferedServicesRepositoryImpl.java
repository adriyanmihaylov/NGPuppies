package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.OfferedServices;
import com.paymentsystem.ngpuppies.repositories.base.OfferedServicesRepository;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OfferedServicesRepositoryImpl implements OfferedServicesRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<OfferedServices> getAll() {
        List<OfferedServices> currencies = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            currencies = session.createQuery("FROM OfferedServices").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return currencies;
    }

    @Override
    public OfferedServices getByName(String name) {
        OfferedServices offeredServices = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String query = String.format("FROM OfferedServices  WHERE name = '%s'", name);
            List<OfferedServices> allOfferedServices = session.createQuery(query).list();
            session.getTransaction().commit();

            if (!allOfferedServices.isEmpty()) {
                offeredServices = allOfferedServices.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return offeredServices;
    }

    @Override
    public boolean create(OfferedServices offeredServices) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(offeredServices);
            session.getTransaction().commit();
            System.out.println("CREATED New Service " + offeredServices.getName());

            return true;
        } catch (JDBCException e) {
            throw new SQLException("Service is present", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //TODO implement
    @Override
    public boolean update(OfferedServices offeredServices) throws Exception {
        return false;
    }

    //TODO implement
    @Override
    public boolean delete(OfferedServices offeredServices) {
        return false;
    }
}