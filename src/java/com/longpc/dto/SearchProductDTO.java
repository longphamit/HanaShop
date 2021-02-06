/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ASUS
 */
public class SearchProductDTO implements Serializable{
    private String name;
    private float fromPrice;
    private float toPrice;
    private String idCategory;
    public SearchProductDTO(){
        
    }

    public SearchProductDTO(String name, float fromPrice, float toPrice, String idCategory) {
        this.name = name;
        this.fromPrice = fromPrice;
        this.toPrice = toPrice;
        this.idCategory = idCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getFromPrice() {
        return fromPrice;
    }

    public void setFromPrice(float fromPrice) {
        this.fromPrice = fromPrice;
    }

    public float getToPrice() {
        return toPrice;
    }

    public void setToPrice(float toPrice) {
        this.toPrice = toPrice;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    

    
    
}
