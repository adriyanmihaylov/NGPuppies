package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.TelecomServ;
import com.paymentsystem.ngpuppies.repositories.base.TelecomServRepository;
import com.paymentsystem.ngpuppies.services.base.TelecomServService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public boolean create(TelecomServ telecomServ) throws SQLException {
        return telecomServRepository.create(telecomServ);
    }

    @Override
    public boolean update(TelecomServ telecomServ) throws Exception {
        return telecomServRepository.update(telecomServ);
    }

    @Override
    public boolean delete(TelecomServ telecomServ) {
        return telecomServRepository.delete(telecomServ);
    }
}