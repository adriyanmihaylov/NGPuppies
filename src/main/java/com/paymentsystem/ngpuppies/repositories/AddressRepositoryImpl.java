package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.repositories.base.AddressRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AddressRepositoryImpl implements AddressRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Address> getAll() {
        List<Address> allAddresses = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            allAddresses = session.createQuery("FROM Address ").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Address was not created");
        }
        return allAddresses;
    }

    @Override
    public Address getById(int id) {
        Address address = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            address = session.get(Address.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return address;
    }

    @Override
    public boolean create(Address address) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(address);
            session.getTransaction().commit();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(Address address) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(address);
            session.getTransaction().commit();
            System.out.println("Address updated!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(Address address) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(address);
            session.getTransaction().commit();

            System.out.println("Address was deleted");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Address was not deleted!");
        }
        return false;
    }
}