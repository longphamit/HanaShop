/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author ASUS
 */
public class OrderDTO implements Serializable{
    private String orderId;
    private Date createDate;
    private String userId;
    private float total;
    private LinkedHashMap<String,ProductDTO> listProducts;
    public OrderDTO(){
        orderId= UUID.randomUUID().toString();
        createDate= Calendar.getInstance().getTime();
        listProducts= new LinkedHashMap<>();
        
    }
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public LinkedHashMap<String, ProductDTO> getListProducts() {
        return listProducts;
    }

    public void setListProducts(LinkedHashMap<String, ProductDTO> listProducts) {
        this.listProducts = listProducts;
    }

    
    
    
    
}
