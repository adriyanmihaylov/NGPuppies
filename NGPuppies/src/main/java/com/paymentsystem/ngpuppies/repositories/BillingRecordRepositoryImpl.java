package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.BillingRecord;
import com.paymentsystem.ngpuppies.models.Currency;
import com.paymentsystem.ngpuppies.models.OfferedServices;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.repositories.base.BillingRecordRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BillingRecordRepositoryImpl implements BillingRecordRepository {
    @Autowired
    private SessionFactory factory;

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

    public List<BillingRecord> getAllBillingMethodsOfSubscriberByPhoneNumber(String phoneNumber) {
        List<BillingRecord> allRecords;
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            String query = String.format("From BillingRecord b where b.subscriber.phoneNumber = '%s'", phoneNumber);
            allRecords = session.createQuery(query).list();
            tx.commit();

            return allRecords;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong");
        }
        return new ArrayList<>();
    }

    @Override
    public boolean create(BillingRecord billingRecord) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.save(billingRecord);
            session.getTransaction().commit();

            System.out.println("Created BILLING RECORD! id: " + billingRecord.getId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(BillingRecord billingRecord) {
        String phoneNumber = billingRecord.getSubscriber().getPhone();
        String currencyName = billingRecord.getCurrency().getName();
        String offeredServiceName = billingRecord.getOfferedServices().getName();

        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            List<Currency> currencies = session.createQuery(String.format("from Currency c where c.name = '%s'", currencyName)).list();
            if (currencies.size() != 0) {
                billingRecord.setCurrency(currencies.get(0));
            } else {
                session.save(billingRecord.getCurrency());
            }
            List<OfferedServices> services = session.createQuery(String.format("from OfferedServices where name = '%s'", offeredServiceName)).list();
            if (services.size() != 0) {
                billingRecord.setOfferedServices(services.get(0));
            } else {
                session.save(billingRecord.getOfferedServices());
            }
            String query = String.format("from Subscriber where phoneNumber = '%s'", phoneNumber);
            List<Subscriber> subscribers = session.createQuery(query).list();
            if (subscribers.size() != 0) {
                billingRecord.setSubscriber(subscribers.get(0));
            } else {
                System.out.println("No such subscriber");
            }
            session.merge(billingRecord);
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
            String query = String.format("From  BillingRecord b where b.startDate >= '%s' and b.endDate <= '%s'", startDate, endDate);
            List<BillingRecord> billingRecords = session.createQuery(query).list();
            tx.commit();

            return billingRecords;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public List<BillingRecord> searchBills(Boolean isPayed) {
        try (Session session = factory.openSession()) {
            int status;
            if (!isPayed) {
                status = 0;
            } else {
                status = 1;
            }
            Transaction tx = session.beginTransaction();
            String query = String.format("From  BillingRecord b where b.payed = '%s'", status);
            List<BillingRecord> billingRecords = session.createQuery(query).list();
            tx.commit();

            return billingRecords;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}