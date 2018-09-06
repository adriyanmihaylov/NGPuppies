package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.TelecomServ;

import java.sql.SQLException;
import java.util.List;

public interface TelecomServRepository {

    List<TelecomServ> getAll();

    boolean create(TelecomServ telecomServ) throws SQLException;

    boolean update(TelecomServ telecomServ) throws Exception;

    boolean delete(TelecomServ telecomServ);

    TelecomServ getByName(String name);
}