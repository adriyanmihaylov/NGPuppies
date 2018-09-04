//package com.paymentsystem.ngpuppies.RepositoryTests;
//
//
//import com.fasterxml.classmate.AnnotationConfiguration;
//import com.paymentsystem.ngpuppies.models.Invoice;
//import com.paymentsystem.ngpuppies.models.Currency;
//import com.paymentsystem.ngpuppies.models.OfferedServices;
//import com.paymentsystem.ngpuppies.models.Subscriber;
//import com.paymentsystem.ngpuppies.repositories.BillingRecordRepositoryImpl;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.cfg.Configuration;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//public class BillingRecordRepositoryTests {
//    private SessionFactory sessionFactory;
//    private Session session = null;
//
////    public SessionFactory createFactory() {
////        // setup the session factory
////    return  new Configuration()
////               .addAnnotatedClass(Invoice.class)
////                .addAnnotatedClass(OfferedServices.class)
////               .setProperty("hibernate.dialect",
////                "org.hibernate.dialect.H2Dialect")
//////               .setProperty("hibernate.connection.url", "jdbc:h2:mem")
//////               .setProperty("hibernate.hbm2ddl.auto", "create")
////               .buildSessionFactory();
////    }
//    @Test
//    public void returnsHerosWithMatchingType() throws ParseException {
//        // create the objects needed for testing
//        OfferedServices offeredService = new OfferedServices("Television");
//        Currency currency = new Currency("BGN");
//        Subscriber subscriber = new Subscriber("01234564", "Yani",
//                "Drenchev", "465132","DSK");
////        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
////        java.util.Date yourDate = sdf.parse("1992-07-26");
//        java.sql.Date sqlDatre = new java.sql.Date(200118);
//        Invoice newBillingRecord = new Invoice(sqlDatre, sqlDatre, 5000,offeredService, currency, subscriber);
//        session.beginTransaction();
//        session.save(offeredService);
//        session.save(currency);
//        session.save(subscriber);
//        session.save(newBillingRecord);
//        session.getTransaction().commit();
//        session.close();
//        BillingRecordRepositoryImpl heroRepository = new BillingRecordRepositoryImpl(sessionFactory);
//        List<Invoice> records = heroRepository.getAll();
//        Assert.assertNotNull(records);
//
//    }
//}
