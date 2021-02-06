/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc.controller;

import com.longpc.dao.ProductDAO;
import com.longpc.dto.AccountDTO;
import com.longpc.dto.CartDTO;
import com.longpc.dto.OrderDTO;
import com.longpc.dto.ProductDTO;
import java.io.IOException;
import java.util.LinkedHashMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author ASUS
 */
@WebServlet("/authorize_payment")
public class AuthorizePaymentController extends HttpServlet {
    private ProductDAO productDAO= new ProductDAO();
    private Logger logger= Logger.getRootLogger();
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession();
            AccountDTO accountDTO = (AccountDTO) session.getAttribute("USER");
            if (accountDTO == null) {
                logger.info("Success at /authorize_payment");
                resp.sendRedirect(req.getContextPath() + "/auth/login");
                return;
            } else {
                CartDTO cartDTO = (CartDTO) session.getAttribute("cart");
               
            }
            
        } catch (Exception e) {
            logger.error("Error at /authorize_payment");
            e.printStackTrace();
        }
    }

}
