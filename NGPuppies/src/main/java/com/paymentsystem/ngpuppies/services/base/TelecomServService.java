package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.TelecomServ;

import java.sql.SQLException;
import java.util.List;

public interface TelecomServService {
    List<TelecomServ> getAll();

    TelecomServ getByName(String name);

    boolean create(TelecomServ telecomServ) throws SQLException;

    boolean update(TelecomServ telecomServ) throws Exception;

    boolean delete(TelecomServ telecomServ);
}