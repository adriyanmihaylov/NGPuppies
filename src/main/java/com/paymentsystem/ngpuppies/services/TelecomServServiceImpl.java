package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.TelecomServ;
import com.paymentsystem.ngpuppies.models.dto.TelecomServiceDTO;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.repositories.base.TelecomServRepository;
import com.paymentsystem.ngpuppies.services.base.TelecomServService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.List;

@Service
public class TelecomServServiceImpl implements TelecomServService {
    @Autowired
    TelecomServRepository telecomServRepository;

    @Override
    public List<TelecomServ> getAll() {
        return telecomServRepository.getAll();
    }

    @Override
    public TelecomServ getByName(String name) {
        return telecomServRepository.getByName(name);
    }

    @Override
    public boolean create(TelecomServiceDTO telecomServiceDTO) throws InvalidParameterException, SQLException {
        if (telecomServiceDTO.getName() == null) {
            throw new InvalidParameterException("Service name is missing!");
        }
        TelecomServ telecomServ = new TelecomServ(telecomServiceDTO.getName());

        return telecomServRepository.create(telecomServ);
    }

    @Override
    public boolean update(String serviceName, TelecomServiceDTO serviceDTO) throws InvalidParameterException, SQLException {
        TelecomServ telecomServ = telecomServRepository.getByName(serviceName);

        if (telecomServ == null) {
            throw new InvalidParameterException("There is no such service in the database!");
        }

        telecomServ.setName(serviceDTO.getName());

        return telecomServRepository.update(telecomServ);
    }

    @Override
    public boolean delete(TelecomServ telecomServ) {
        return telecomServRepository.delete(telecomServ);
    }

    @Override
    public List<TelecomServ> getAllServicesOfClient(Client client) {
        return telecomServRepository.getAllServicesOfClient(client.getId());
    }
}