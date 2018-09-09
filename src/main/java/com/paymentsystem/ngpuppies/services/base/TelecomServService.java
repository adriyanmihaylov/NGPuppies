package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.TelecomServ;
import com.paymentsystem.ngpuppies.web.dto.TelecomServiceDTO;
import com.paymentsystem.ngpuppies.models.users.Client;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.List;

public interface TelecomServService {

    TelecomServ getByName(String name);

    List<TelecomServ> getAll();

    boolean create(TelecomServiceDTO telecomServiceDTO) throws InvalidParameterException,SQLException;

    boolean update(String serviceName, TelecomServiceDTO serviceDTO) throws InvalidParameterException,SQLException;

    boolean delete(TelecomServ telecomServ);

    List<TelecomServ> getAllServicesOfClient(Client client);
}