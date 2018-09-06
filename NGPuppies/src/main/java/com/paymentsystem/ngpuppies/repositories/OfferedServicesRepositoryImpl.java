package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.OfferedServices;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.repositories.base.OfferedServicesRepository;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
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
            throw new InternalException("Something went wrong! Please try again later!");
        }
    }

    @Override
    public boolean update(OfferedServices offeredServices) throws Exception {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(offeredServices);
            session.getTransaction().commit();

            System.out.printf("UPDATED: Service  Id: %d\n", offeredServices.getId());
            return true;
        } catch (PersistenceException e) {
            throw new SQLException("Service already exist", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(OfferedServices offeredServices) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(offeredServices);
            session.getTransaction().commit();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}