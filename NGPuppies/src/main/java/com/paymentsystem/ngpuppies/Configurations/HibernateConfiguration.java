package com.paymentsystem.ngpuppies.Configurations;

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
                .addAnnotatedClass(Bill.class)
                .addAnnotatedClass(Currency.class)
                .addAnnotatedClass(Service.class)
                .addAnnotatedClass(Subscriber.class)
                .addAnnotatedClass(Role.class)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
    }

}
