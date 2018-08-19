package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.repositories.base.AddressRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class AddressRepositoryImpl implements AddressRepository {
    private SessionFactory sessionFactory;
    public AddressRepositoryImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean create(Address address) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(address);
            System.out.println("Address was created: " + address);
            session.getTransaction().commit();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Address was not created");
        }

        return false;
    }

    @Override
    public boolean update(Address address) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(address);
            System.out.println("Address was updated: " + address);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Address was not updated");
        }
        return false;
    }

    @Override
    public boolean delete(Address address) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(address);
            System.out.println("Address was deleted: " + address);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Address was not deleted");
        }
        return false;
    }
}
