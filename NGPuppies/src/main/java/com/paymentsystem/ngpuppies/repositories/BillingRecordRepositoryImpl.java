package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.BillingRecord;
import com.paymentsystem.ngpuppies.models.Currency;
import com.paymentsystem.ngpuppies.models.OfferedService;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.repositories.base.BillingRecordRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.propertyeditors.CurrencyEditor;
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
        List<BillingRecord> billingRecordsToBeDelete = null;
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            String query = String.format("From BillingRecord b where b.subscriber.phoneNumber = '%s'" , phoneNumber);
            billingRecordsToBeDelete = session.createQuery(query).list();
            for (BillingRecord aBillingRecordsToBeDelete : billingRecordsToBeDelete) {
                session.delete(aBillingRecordsToBeDelete);
            }
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
        String phoneNumber = billingRecordToBeCreated.getSubscriber().getPhoneNumber();
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            List<Currency> currencies = session.createQuery(String.format("from Currency c where c.name = '%s'", billingRecordToBeCreated.getCurrency().getName())).list();
            if (currencies.size()!= 0){
                billingRecordToBeCreated.setCurrency(currencies.get(0));
            }else{
                session.save(billingRecordToBeCreated.getCurrency());
            }
            List<OfferedService> services = session.createQuery(String.format("from OfferedService where name = '%s'", billingRecordToBeCreated.getOfferedService().getName())).list();
            if (services.size()!= 0){
                billingRecordToBeCreated.setOfferedService(services.get(0));
            }else{
                session.save(billingRecordToBeCreated.getOfferedService());
            }
            String query =String.format("from Subscriber where phoneNumber = '%s'", phoneNumber);
            List<Subscriber> subscribers = session.createQuery(query).list();
            if (subscribers.size()!= 0){
                billingRecordToBeCreated.setSubscriber(subscribers.get(0));
            }else{

            }
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
        String phoneNumber = updatedBillingRecord.getSubscriber().getPhoneNumber();
        String currencyName = updatedBillingRecord.getCurrency().getName();
        String offeredServiceName = updatedBillingRecord.getOfferedService().getName();

        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            List<Currency> currencies = session.createQuery(String.format("from Currency c where c.name = '%s'", currencyName)).list();
            if (currencies.size()!= 0){
                updatedBillingRecord.setCurrency(currencies.get(0));
            }else{
                session.save(updatedBillingRecord.getCurrency());
            }
            List<OfferedService> services = session.createQuery(String.format("from OfferedService where name = '%s'",offeredServiceName)).list();
            if (services.size()!= 0){
                updatedBillingRecord.setOfferedService(services.get(0));
            }else{
                session.save(updatedBillingRecord.getOfferedService());
            }
            String query = String.format("from Subscriber where phoneNumber = '%s'", phoneNumber);
            List<Subscriber> subscribers = session.createQuery(query).list();
            if (subscribers.size()!= 0){
                updatedBillingRecord.setSubscriber(subscribers.get(0));
            }else{
                System.out.println("No such subscriber");
            }
            session.merge(updatedBillingRecord);
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