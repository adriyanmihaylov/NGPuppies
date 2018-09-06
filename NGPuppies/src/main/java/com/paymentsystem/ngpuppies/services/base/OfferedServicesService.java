package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.OfferedServices;
import com.paymentsystem.ngpuppies.models.Subscriber;

import java.sql.SQLException;
import java.util.List;

public interface OfferedServicesService {
    List<OfferedServices> getAll();

    OfferedServices getByName(String name);

    boolean create(OfferedServices offeredServices) throws SQLException;

    boolean update(OfferedServices offeredServices) throws Exception;

    boolean delete(OfferedServices offeredServices);
}