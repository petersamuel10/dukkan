package com.example.dukkanexpress.model;

public class OrderModel {

    String order_id;
    String order_number;
    String order_created_at;
    String order_status_id;
    String order_status_name;
    String order_status_en_name;
    String order_time;

    public OrderModel() {
    }

    public OrderModel(String order_id, String order_number, String order_created_at,
                      String order_status_id, String order_status_name, String order_status_en_name, String order_time) {
        this.order_id = order_id;
        this.order_number = order_number;
        this.order_created_at = order_created_at;
        this.order_status_id = order_status_id;
        this.order_status_name = order_status_name;
        this.order_status_en_name = order_status_en_name;
        this.order_time = order_time;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getOrder_created_at() {
        return order_created_at;
    }

    public void setOrder_created_at(String order_created_at) {
        this.order_created_at = order_created_at;
    }

    public String getOrder_status_id() {
        return order_status_id;
    }

    public void setOrder_status_id(String order_status_id) {
        this.order_status_id = order_status_id;
    }

    public String getOrder_status_name() {
        return order_status_name;
    }

    public void setOrder_status_name(String order_status_name) {
        this.order_status_name = order_status_name;
    }

    public String getOrder_status_en_name() {
        return order_status_en_name;
    }

    public void setOrder_status_en_name(String order_status_en_name) {
        this.order_status_en_name = order_status_en_name;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }
}
