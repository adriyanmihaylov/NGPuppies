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
    public Address create(Address address) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(address);
            session.getTransaction().commit();

            return address;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Address was not created");
        }

        return null;
    }

    @Override
    public boolean delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Address addressToBeDelete = session.get(Address.class, id);
            session.delete(addressToBeDelete);
            System.out.println("Address was deleted: " + addressToBeDelete);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Address was not deleted no such id: " + id);
        }
        return false;
    }
}
