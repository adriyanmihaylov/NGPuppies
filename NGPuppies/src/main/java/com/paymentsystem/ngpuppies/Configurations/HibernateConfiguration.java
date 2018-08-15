package com.paymentsystem.ngpuppies.Configurations;

import com.paymentsystem.ngpuppies.models.Bill;
import com.paymentsystem.ngpuppies.models.Currency;
import com.paymentsystem.ngpuppies.models.Service;
import com.paymentsystem.ngpuppies.models.Subscriber;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfiguration {
    @Bean
    public SessionFactory createSessionFactory() {
        System.out.println("SessionFactory was created.");

        return new org.hibernate.cfg.Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Bill.class)
                .addAnnotatedClass(Currency.class)
                .addAnnotatedClass(Service.class)
                .addAnnotatedClass(Subscriber.class)
                .buildSessionFactory();
    }

}
