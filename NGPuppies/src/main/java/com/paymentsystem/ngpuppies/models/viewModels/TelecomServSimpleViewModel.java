package com.paymentsystem.ngpuppies.models.viewModels;

import com.paymentsystem.ngpuppies.models.TelecomServ;

public class TelecomServSimpleViewModel {

    public String service;

    public static TelecomServSimpleViewModel fromModel(TelecomServ telecomServ) {
        TelecomServSimpleViewModel viewModel = new TelecomServSimpleViewModel();
        if (telecomServ != null) {
            viewModel.service = telecomServ.getName();
        }
        return viewModel;
    }
}