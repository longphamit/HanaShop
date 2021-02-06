/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc;

import com.longpc.utils.PagingUtilPro;

/**
 *
 * @author ASUS
 */
public class test {
    public static void main(String[] args) {
        PagingUtilPro pagingUtilPro= new PagingUtilPro();
        pagingUtilPro.setCurrentPage(3);
        pagingUtilPro.calculatorMinMax(16);
        System.out.println(pagingUtilPro.getMinPage());
        System.out.println(pagingUtilPro.getMaxPage());
    }
}
