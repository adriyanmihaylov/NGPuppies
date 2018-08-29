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

    public BillingRecordRepositoryImpl(SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public List<BillingRecord> getAll() {
        List<BillingRecord> all = new ArrayList<>();
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            all = session.createQuery("From BillingRecord ").list();
            tx.commit();
            return all;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong");

        }
        return all;
    }

    @Override
    public BillingRecord getBySubscriber(String phoneNumber) {
        BillingRecord billingRecordBySubscriber = null;
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            String query = String.format("From BillingRecord b where b.subscriber.phoneNumber = '%s'" , phoneNumber);
            billingRecordBySubscriber = (BillingRecord) session.createQuery(query).list().get(0);
            tx.commit();
            return billingRecordBySubscriber;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong");
        }
        return null;
    }

    @Override
    public boolean deleteBySubscriber(String phoneNumber) {
        BillingRecord billingRecordBySubscriber = null;
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            String query = String.format("From BillingRecord b where b.subscriber.phoneNumber = '%s'" , phoneNumber);
            billingRecordBySubscriber = (BillingRecord) session.createQuery(query).list().get(0);
            session.delete(billingRecordBySubscriber);
            tx.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong");
        }
        return false;
    }
    @Override
    public boolean create(BillingRecord billingRecordToBeCreated) {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(billingRecordToBeCreated);
            tx.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("The billing record was not created");
        }
        return false;
    }

    @Override
    public boolean update(BillingRecord updatedBillingRecord) {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(updatedBillingRecord);
            tx.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("The Billing record was not updated");
        }
        return false;
    }

    @Override
    public List<BillingRecord> getByDate(String startDate, String endDate) {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            String query =String.format("from  BillingRecord b where b.startDate >= '%s' and b.endDate <= '%s'",startDate,endDate);
            List<BillingRecord> billingRecords = session.createQuery(query).list();
            tx.commit();
            return billingRecords;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<BillingRecord> searchBills(Boolean payed) {
        try (Session session = factory.openSession()) {
            int status;
            if (!payed){
                status = 0;
            }else{
                status = 1;
            }
            Transaction tx = session.beginTransaction();
            String query =String.format("from  BillingRecord b where b.payed = '%s'", status);
            List<BillingRecord> billingRecords = session.createQuery(query).list();
            tx.commit();
            return billingRecords;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}