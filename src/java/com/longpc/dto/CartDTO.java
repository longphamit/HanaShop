/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc.dto;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 *
 * @author ASUS
 */
public class CartDTO implements Serializable {

    private String idAccount;
    private LinkedHashMap<String, ProductDTO> cart;
    private String paymentId;
    private String payerId;
    public CartDTO() {
        this.cart = new LinkedHashMap<>();
    }

    public CartDTO(String id) {
        this.idAccount = id;
        this.cart = new LinkedHashMap<>();
    }

    public void setIdAccount(String idAccount) {
        this.idAccount = idAccount;
    }

    public String getIdAccount() {
        return idAccount;
    }

    public LinkedHashMap<String, ProductDTO> getCart() {
        return cart;
    }

    public void setCart(LinkedHashMap<String, ProductDTO> cart) {
        this.cart = cart;
    }

    public boolean add(ProductDTO productDTO, int quantityInCart) {
        if (cart.containsKey(productDTO.getId())) {
            productDTO.setQuantityInCart(cart.get(productDTO.getId()).getQuantityInCart() + quantityInCart);
        } else {
            productDTO.setQuantityInCart(quantityInCart);
        }
        if (productDTO.getQuantity() < productDTO.getQuantityInCart()) {
            return false;
        }
        cart.put(productDTO.getId(), productDTO);
        return true;
    }

    public void remove(String id) {
        if (cart.containsKey(id)) {
            cart.remove(id);
        }
    }

    public void update(String id, int quantityInCart) {
        if (cart.containsKey(id)) {
            cart.get(id).setQuantity(quantityInCart);
        }
    }

    public float total() {
        float total = 0;
        for (ProductDTO dto : cart.values()) {
            total += dto.getQuantityInCart() * dto.getPrice();
        }System.out.println(total);
        return Float.parseFloat(new String().format("%.2f", total));
    }

    public String getTotalString() {
        float total = 0;
        for (ProductDTO dto : cart.values()) {
            total += dto.getQuantityInCart() * dto.getPrice();
        }
        System.out.println(total);
        System.out.println(Float.parseFloat(new String().format("%.2f", total)));
        return new String().format("%.2f", total);
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentId() {
        return paymentId;
    }
    
}
