package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.Currency;
import com.paymentsystem.ngpuppies.repositories.base.CurrencyRepository;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CurrencyRepositoryImpl implements CurrencyRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Currency> getAll() {
        List<Currency> currencies = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            currencies = session.createQuery("FROM Currency").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return currencies;
    }

    @Override
    public Currency getByName(String name) {
        Currency currency = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String query = String.format("FROM Currency WHERE name = '%s'", name);
            List<Currency> allCurrencies = session.createQuery(query).list();
            session.getTransaction().commit();

            if (!allCurrencies.isEmpty()) {
                currency = allCurrencies.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return currency;
    }

    @Override
    public boolean create(Currency currency) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(currency);
            session.getTransaction().commit();
            System.out.println("CREATED Currency " + currency.getName());

            return true;
        } catch (JDBCException e) {
            throw new SQLException("Currency is present", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(String currencyName,double fixing) {
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("" +
                    " UPDATE Currency c" +
                    " SET c.fixing=:newFixing" +
                    " WHERE c.name=:currencyName");
            query.setParameter("newFixing",fixing);
            query.setParameter("currencyName",currencyName);
            session.beginTransaction();
            int result = query.executeUpdate();
            session.getTransaction().commit();

            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
