package com.paymentsystem.ngpuppies.models;

public class SubscriberTwo {
    private Subscriber subscriber;

    private Double totalAmount;

    public SubscriberTwo() {

    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }
}
