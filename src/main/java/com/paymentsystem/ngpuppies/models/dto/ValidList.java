package com.paymentsystem.ngpuppies.models.dto;

import com.paymentsystem.ngpuppies.models.Invoice;

import javax.validation.Valid;
import java.util.*;

public class ValidList<E> {
    @Valid
    List<E> list;

    public ValidList() {
        list = new ArrayList<>();
    }

    public ValidList(List<E> list) {
        this.list = list;
    }

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