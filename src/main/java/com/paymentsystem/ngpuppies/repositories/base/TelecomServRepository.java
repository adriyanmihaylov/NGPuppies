package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.TelecomServ;

import java.sql.SQLException;
import java.util.List;

public interface TelecomServRepository {

    TelecomServ getByName(String name);

    List<TelecomServ> getAll();

    boolean create(TelecomServ telecomServ) throws SQLException;

    boolean update(TelecomServ telecomServ) throws SQLException;

    boolean delete(TelecomServ telecomServ);

    List<TelecomServ> getAllServicesOfClient(int clientId);
}