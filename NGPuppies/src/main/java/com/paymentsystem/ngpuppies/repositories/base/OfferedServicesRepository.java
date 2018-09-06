package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.OfferedServices;
import com.paymentsystem.ngpuppies.models.Subscriber;

import java.sql.SQLException;
import java.util.List;

public interface OfferedServicesRepository {

    List<OfferedServices> getAll();

    boolean create(OfferedServices offeredServices) throws SQLException;

    boolean update(OfferedServices offeredServices) throws Exception;

    boolean delete(OfferedServices offeredServices);

    OfferedServices getByName(String name);
}