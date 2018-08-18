package com.paymentsystem.ngpuppies.configurations;

import com.paymentsystem.ngpuppies.models.*;
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
                .addAnnotatedClass(ApplicationUser.class)
                .addAnnotatedClass(Address.class)
                .addAnnotatedClass(BillingRecord.class)
                .addAnnotatedClass(ClientDetail.class)
                .addAnnotatedClass(Currency.class)
                .addAnnotatedClass(OfferedService.class)
                .addAnnotatedClass(Subscriber.class)
                .buildSessionFactory();
    }
}