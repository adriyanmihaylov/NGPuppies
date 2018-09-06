package com.paymentsystem.ngpuppies.models.viewModels;

import com.paymentsystem.ngpuppies.models.OfferedServices;

public class OfferedServiceSimpleViewModel {

    public String service;

    public static OfferedServiceSimpleViewModel fromModel(OfferedServices offeredServices) {
        OfferedServiceSimpleViewModel viewModel = new OfferedServiceSimpleViewModel();
        if (offeredServices != null) {
            viewModel.service = offeredServices.getName();
        }
        return viewModel;
    }
}