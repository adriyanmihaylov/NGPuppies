package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.Invoice;
import com.paymentsystem.ngpuppies.repositories.base.InvoiceRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class InvoiceRepositoryImpl implements InvoiceRepository {
    @Autowired
    private SessionFactory sessionFactory;

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
    public Invoice getById(Integer id) {
        Invoice invoice = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            invoice = session.get(Invoice.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invoice;
    }

    @Override
    public boolean create(Invoice invoice) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(invoice);
            transaction.commit();
            return true;
        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (RuntimeException exception) {
                System.out.println("Couldn't roll back transaction!");
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return false;
    }

    @Override
    public boolean update(List<Invoice> invoices) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            for (Invoice invoice : invoices) {
                session.update(invoice);
            }
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (RuntimeException exception) {
                System.out.println("Couldn't roll back transaction!");
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return false;
    }

    @Override
    public boolean delete(Invoice invoice) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(invoice);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean payInvoice(Invoice invoice) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.update(invoice.getSubscriber());
            session.update(invoice);
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (RuntimeException exception) {
                System.out.println("Couldn't roll back transaction!");
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return false;
    }

    @Override
    public List<Invoice> getAllUnpaidInvoices() {
        List<Invoice> invoices;
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("" +
                    " FROM Invoice i" +
                    " WHERE i.payedDate IS NULL" +
                    " ORDER BY i.endDate");
            session.beginTransaction();
            invoices = query.list();
            session.getTransaction().commit();

            return invoices;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public Invoice getSubscriberLargestPaidInvoiceForPeriodOfTime(Integer subscriberId, LocalDate fromDate, LocalDate toDate) {
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("" +
                    " FROM Invoice i" +
                    " WHERE i.subscriber.id=:subscriberId" +
                    " AND i.payedDate >= :fromDate and i.payedDate <= :toDate" +
                    " ORDER BY i.BGNAmount DESC");

            query.setParameter("subscriberId", subscriberId);
            query.setParameter("fromDate", fromDate);
            query.setParameter("toDate", toDate);

            session.beginTransaction();
            List<Invoice> invoices = query.setMaxResults(1).list();
            session.getTransaction().commit();

            if (invoices.size() > 0) {
                return invoices.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Invoice> getAllUnpaidInvoicesOfService(String serviceName) {
        List<Invoice> invoices;
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("" +
                    " FROM Invoice i" +
                    " WHERE i.payedDate IS NULL" +
                    " AND i.telecomServ.name=:serviceName" +
                    " ORDER BY i.endDate");
            query.setParameter("serviceName",serviceName);

            session.beginTransaction();
            invoices = query.list();
            session.getTransaction().commit();

            return invoices;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public List<Invoice> geAllUnpaidInvoicesFromDateToDate(LocalDate fromDate, LocalDate toDate) {
        List<Invoice> invoices = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("" +
                    " FROM Invoice i" +
                    " WHERE i.endDate >= :fromDate and i.endDate <= :toDate" +
                    " ORDER BY i.endDate DESC");
            query.setParameter("fromDate", fromDate);
            query.setParameter("toDate", toDate);

            session.beginTransaction();
            invoices = query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invoices;
    }

    @Override
    public List<Invoice> geAllUnpaidInvoicesOfAllClientSubscribers(int clientId) {
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("" +
                    " FROM Invoice i" +
                    " WHERE i.subscriber.client.id = :clientId" +
                    " AND i.payedDate IS NULL");
            query.setParameter("clientId", clientId);

            session.beginTransaction();
            List<Invoice> allInvoices = query.list();
            session.getTransaction().commit();

            return allInvoices;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public List<Invoice> getAllPaidInvoicesOfSubscriberByPeriodOfTimeInDescOrder(int subscriberId, LocalDate fromDate, LocalDate toDate) {
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("" +
                    " FROM Invoice i " +
                    " WHERE i.subscriber.id=:subscriberId" +
                    " AND i.payedDate >= :fromDate" +
                    " AND i.payedDate <= :toDate " +
                    " ORDER BY i.payedDate DESC ");
            query.setParameter("subscriberId", subscriberId);
            query.setParameter("fromDate", fromDate);
            query.setParameter("toDate", toDate);

            session.beginTransaction();
            List<Invoice> invoices = query.list();
            session.getTransaction().commit();

            return invoices;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public List<Invoice> getAllUnpaidInvoicesOfSubscriberInDescOrder(String subscriberPhone) {
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("" +
                    " From Invoice i " +
                    " WHERE i.subscriber.phone=:subscriberPhone" +
                    " AND i.payedDate IS NULL" +
                    " ORDER BY i.endDate DESC ");
            query.setParameter("subscriberPhone", subscriberPhone);

            session.beginTransaction();
            List<Invoice> invoices = query.list();
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
            Query query = session.createQuery("" +
                    " From Invoice i " +
                    " WHERE i.subscriber.id=:subscriberId" +
                    " ORDER BY i.payedDate,i.endDate");
            query.setParameter("subscriberId", subscriberId);
            session.beginTransaction();
            List<Invoice> invoices = query.list();
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
            Query query = session.createQuery("" +
                    " From Invoice i " +
                    " WHERE i.subscriber.client.id=:clientId" +
                    " AND i.payedDate IS NOT NULL" +
                    " ORDER BY i.payedDate DESC ");
            query.setParameter("clientId", clientId);

            session.beginTransaction();
            List<Invoice> invoices = query.setMaxResults(10).list();
            session.getTransaction().commit();

            return invoices;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}