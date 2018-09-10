package com.paymentsystem.ngpuppies.repositories;

import com.paymentsystem.ngpuppies.models.PasswordResetToken;
import com.paymentsystem.ngpuppies.repositories.base.PasswordResetTokenRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PasswordResetTokenRepositoryImpl implements PasswordResetTokenRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean create(PasswordResetToken passwordResetToken) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(passwordResetToken);
            transaction.commit();

            return true;
        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (RuntimeException ignored) {
            }
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("" +
                    " FROM PasswordResetToken p" +
                    " WHERE p.toke=:passToken");
            query.setParameter("passToken", token);
            PasswordResetToken passwordResetToken = (PasswordResetToken) query.list().get(0);
            session.getTransaction().commit();

            return passwordResetToken;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
