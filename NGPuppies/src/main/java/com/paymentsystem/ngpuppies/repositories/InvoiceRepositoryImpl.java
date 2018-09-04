package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.Invoice;
import com.paymentsystem.ngpuppies.repositories.base.InvoiceRepository;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class InvoiceRepositoryImpl implements InvoiceRepository {
    @Autowired
    private SessionFactory sessionFactory;

    private final double EUR_TO_BGN_FIXING = 1.955;
    private final double USD_TO_BGN_FIXING = 1.689;

    @Override
    public List<Invoice> getAll() {
        List<Invoice> all = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            all = session.createQuery("From Invoice ").list();
            tx.commit();
            return all;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong");

        }
        return all;
    }

    @Override
    public List<Invoice> getAllInvoicesOfSubscriberByPhoneNumber(String phoneNumber, boolean isPayed) {
        List<Invoice> allRecords;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            String status = isPayed ? "NOT NULL" : "NULL";
            String query = String.format("From Invoice i where i.subscriber.phoneNumber = '%s'" +
                    " AND payedDate IS %s", phoneNumber, status);

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
    public boolean create(Invoice invoice) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            switch (invoice.getCurrency().getName()) {
                case "EUR":
                    invoice.setBGNAmount(invoice.getAmount() * EUR_TO_BGN_FIXING);
                    break;
                case "USD":
                    invoice.setBGNAmount(invoice.getAmount() * USD_TO_BGN_FIXING);
                    break;
                default:
                    invoice.setBGNAmount(invoice.getAmount());
                    break;
            }
            session.save(invoice);
            session.getTransaction().commit();

            System.out.println("Created new INVOICE! id: " + invoice.getId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(List<Invoice> invoices) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (Invoice invoice : invoices) {
                session.update(invoice);
            }
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<Invoice> geAllUnpaidInvoicesOfAllClientSubscribers(int clientId) {
        try (Session session = sessionFactory.openSession()) {
            String query = String.format("FROM Invoice b " +
                    "WHERE b.subscriber.client.id = %s AND b.payedDate IS NULL", clientId);
            session.beginTransaction();
            List<Invoice> allInvoices = (List<Invoice>) session.createQuery(query).list();
            session.getTransaction().commit();
            return allInvoices;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public List<Invoice> getInvoicesByIdAndClientId(List<Integer> invoicesId, Integer clientId) {
        List<Invoice> allInvoices = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (Integer id : invoicesId) {
                String query = String.format("FROM Invoice i " +
                        "WHERE i.id=%s AND i.subscriber.client.id = %s", id, clientId);
                List<Invoice> invoices = session.createQuery(query).list();
                if (invoices.size() > 0) {
                    allInvoices.addAll(invoices);
                }
            }
            session.getTransaction().commit();

            return allInvoices;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public boolean payInvoices(List<Invoice> allInvoices) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            for (Invoice invoice : allInvoices) {
                invoice.setPayedDate(new Date());
                session.update(invoice);
            }

            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            session.close();

            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<Invoice> getAllPaidInvoicesOfSubscriberInDescOrder(Integer subscriberId, String fromDate, String endDate) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String query = String.format("FROM Invoice i" +
                    " WHERE i.subscriber.id=%s" +
                    " AND i.payedDate >= '%s' and i.payedDate <= '%s'" +
                    " ORDER BY i.payedDate DESC", subscriberId, fromDate, endDate);
            List<Invoice> invoices = session.createQuery(query).list();
            session.getTransaction().commit();

            return invoices;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public List<Invoice> getAllUnpaidInvoicesOfSubscriberInDescOrder(Integer subscriberId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String query = String.format("FROM Invoice i" +
                    " WHERE i.subscriber.id=%s" +
                    " AND i.payedDate IS NULL" +
                    " ORDER BY i.endDate DESC", subscriberId);
            List<Invoice> invoices = session.createQuery(query).list();
            session.getTransaction().commit();

            return invoices;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public List<Invoice> getAllInvoicesOfSubscriberBySubscriberId(Integer subscriberId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String query = String.format("FROM Invoice i" +
                    " WHERE i.subscriber.id=%s" +
                    " ORDER BY i.payedDate, i.endDate", subscriberId);
            List<Invoice> invoices = session.createQuery(query).list();
            session.getTransaction().commit();

            return invoices;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public List<Invoice> getTenMostRecentInvoices(Integer clientId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String query = String.format("FROM Invoice i" +
                    " WHERE i.subscriber.client.id = %s" +
                    " AND i.payedDate IS NOT NULL" +
                    " ORDER BY i.payedDate DESC", clientId);
            List<Invoice> invoices = session.createQuery(query).setMaxResults(10).list();
            session.getTransaction().commit();

            return invoices;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}