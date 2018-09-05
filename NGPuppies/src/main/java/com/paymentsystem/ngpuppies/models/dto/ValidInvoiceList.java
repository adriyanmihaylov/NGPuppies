package com.paymentsystem.ngpuppies.models.dto;

import javax.validation.Valid;
import java.util.*;

public class ValidInvoiceList<E> {

    @Valid
    List<E> list;

    public ValidInvoiceList() {
        list = new ArrayList<>();
    }
    public ValidInvoiceList(List<E> list) {
        this.list = list;
    }

    @Valid
    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    public boolean add(E e) {
        return list.add(e);
    }

    public void clear() {
        list.clear();
    }
}