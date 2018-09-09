package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.TelecomServ;
import com.paymentsystem.ngpuppies.web.dto.TelecomServDto;
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
    private TelecomServRepository telecomServRepository;

    @Override
    public List<TelecomServ> getAll() {
        return telecomServRepository.getAll();
    }

    @Override
    public TelecomServ getByName(String name) throws InvalidParameterException {
        if (name == null) {
            throw new InvalidParameterException("Service name must not be empty!");
        }

        return telecomServRepository.getByName(name);
    }

    @Override
    public boolean create(TelecomServDto telecomServDto) throws InvalidParameterException, SQLException {
        if (telecomServDto.getName() == null) {
            throw new InvalidParameterException("Service name is missing!");
        }
        TelecomServ telecomServ = new TelecomServ(telecomServDto.getName());

        return telecomServRepository.create(telecomServ);
    }

    @Override
    public boolean update(String serviceName, TelecomServDto telecomServDto)throws InvalidParameterException, SQLException {
        if (serviceName == null || telecomServDto.getName() == null) {
            throw new InvalidParameterException("Invalid service name!");
        }

        return telecomServRepository.update(serviceName, telecomServDto.getName().toUpperCase());
    }

    @Override
    public boolean delete(String telecomServName) throws InvalidParameterException {
        if (telecomServName == null) {
            throw new InvalidParameterException("Invalid service name!");
        }

        return telecomServRepository.delete(telecomServName);
    }

    @Override
    public List<TelecomServ> getAllServicesOfClient(Client client) {
        return telecomServRepository.getAllServicesOfClientByClientId(client.getId());
    }
}