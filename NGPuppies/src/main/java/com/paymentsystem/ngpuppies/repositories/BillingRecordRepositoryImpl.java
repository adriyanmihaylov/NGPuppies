package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.BillingRecord;
import com.paymentsystem.ngpuppies.repositories.base.BillingRecordRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BillingRecordRepositoryImpl implements BillingRecordRepository {
    private final SessionFactory factory;
    BillingRecordRepositoryImpl(SessionFactory factory){
        this.factory = factory;
    }
    @Override
    public List<BillingRecord> getAll() {
        List<BillingRecord> all = new ArrayList<>();
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            all = session.createQuery("From BillingRecord ").list();
            tx.commit();
            return all;
        }catch (Exception e ){
            e.printStackTrace();
            System.out.println("Something went wrong");

        }
        return all;
    }

    @Override
    public BillingRecord getBySubscriber(String phoneNumber) {
        BillingRecord billingRecordBySubscriber = null;
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            billingRecordBySubscriber = (BillingRecord) session.createQuery("from BillingRecord b " +
                    "where b.subscriber.phoneNumber = " + phoneNumber).list().get(0);
            tx.commit();
            return billingRecordBySubscriber;
        }catch (Exception e ){
            e.printStackTrace();
            System.out.println("Something went wrong");
        }
        return billingRecordBySubscriber;
    }

    @Override
    public boolean deleteBySubscriber(String phoneNumber) {
        BillingRecord billingRecordBySubscriber = null;
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            billingRecordBySubscriber = (BillingRecord) session.createQuery("from BillingRecord b " +
                    "where b.subscriber.phoneNumber = " + phoneNumber).list().get(0);
            session.delete(billingRecordBySubscriber);
            tx.commit();
            return true;
        }catch (Exception e ){
            e.printStackTrace();
            System.out.println("Something went wrong");
        }
        return false;
    }

    @Override
    public boolean create(BillingRecord billingRecordToBeCreated) {
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            session.save(billingRecordToBeCreated);
            tx.commit();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("The billing record was not created");
        }
        return false;
    }

    @Override
    public boolean update(BillingRecord updatedBillingRecord) {
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            session.update(updatedBillingRecord);
            tx.commit();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("The Billing record was not updated");
        }
        return false;
    }
}
