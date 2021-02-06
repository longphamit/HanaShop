/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 *
 * @author ASUS
 */
public class ProductDTO  implements Serializable,Comparable<ProductDTO>{
    private String id;
    private String name;
    private Date createDate;
    private String status;
    private int quantity;
    private String description;
    private Float price;
    private String categoryId;
    private String categoryName;
    private List<ImageDTO> images; 
    private int quantityInCart;
    public ProductDTO(){
        id=UUID.randomUUID().toString();
        createDate=Calendar.getInstance().getTime();
        status="1";
        images= new ArrayList<>();
        quantityInCart=0;
    }
    public String getId() {
        return id;
    }

    public int getQuantityInCart() {
        return quantityInCart;
    }

    public void setQuantityInCart(int quantityInCart) {
        this.quantityInCart = quantityInCart;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

  
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setImages(List<ImageDTO> images) {
        this.images = images;
    }

    public List<ImageDTO> getImages() {
        return images;
    }

    @Override
    public int compareTo(ProductDTO t) {
        return t.id.compareTo(this.id);
    }
    
    
}
