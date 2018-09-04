package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.OfferedServices;
import com.paymentsystem.ngpuppies.repositories.base.OfferedServicesRepository;
import com.paymentsystem.ngpuppies.services.base.OfferedServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class OfferedServicesServiceImpl implements OfferedServicesService {
    @Autowired
    OfferedServicesRepository offeredServicesRepository;

    @Override
    public List<OfferedServices> getAll() {
        return offeredServicesRepository.getAll();
    }

    @Override
    public OfferedServices getByName(String name) {
        return offeredServicesRepository.getByName(name);
    }

    @Override
    public boolean create(OfferedServices offeredServices) throws SQLException {
        return offeredServicesRepository.create(offeredServices);
    }

    @Override
    public boolean update(OfferedServices offeredServices) throws Exception {
        return offeredServicesRepository.update(offeredServices);
    }

    @Override
    public boolean delete(OfferedServices offeredServices) {
        return offeredServicesRepository.delete(offeredServices);
    }
}