package com.paymentsystem.ngpuppies.configurations;

import com.paymentsystem.ngpuppies.models.*;
import com.paymentsystem.ngpuppies.models.users.Admin;
import com.paymentsystem.ngpuppies.models.users.User;
import com.paymentsystem.ngpuppies.models.users.Authority;
import com.paymentsystem.ngpuppies.models.users.Client;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {

    @Bean
    public SessionFactory createSessionFactory() {
        System.out.println("SessionFactory was created.");

        return new org.hibernate.cfg.Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Admin.class)
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(Authority.class)
                .addAnnotatedClass(Address.class)
                .addAnnotatedClass(Invoice.class)
                .addAnnotatedClass(ClientDetail.class)
                .addAnnotatedClass(Currency.class)
                .addAnnotatedClass(Subscriber.class)
                .addAnnotatedClass(TelecomServ.class)
                .buildSessionFactory();
    }
}