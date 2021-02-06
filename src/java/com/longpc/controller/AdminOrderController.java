/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc.controller;

import com.longpc.dao.OrderDAO;
import com.longpc.dto.OrderDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author ASUS
 */
@WebServlet(urlPatterns = {"/admin/vieworder","/admin/orderdetail"})
public class AdminOrderController extends HttpServlet {

    private OrderDAO orderDAO = new OrderDAO();
    Logger logger= Logger.getRootLogger();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        switch (path) {
            case "/admin/vieworder": {
                String url = "/error.jsp";
                try {
                    List<OrderDTO> listOrders = orderDAO.findAll();
                    req.setAttribute("listOrders", listOrders);
                    url="/admin_order.jsp";
                } catch (Exception e) {
                    logger.info("Error at admin/vieworder");
                    e.printStackTrace();
                }
                logger.info("Success at admin/vieworder");
                log("Success at admin/orderDetail");
                req.getRequestDispatcher(url).forward(req, resp);
                 break;
            }
            case "/admin/orderdetail": {
                String url = "/error.jsp";
                try {
                    String nameAccount=req.getParameter("nameAccount");
                    String fromDate = req.getParameter("fromDate");
                    String toDate = req.getParameter("toDate");
                    List<OrderDTO> listOrders = orderDAO.getByDate(fromDate, toDate,nameAccount);
                    req.setAttribute("listOrders", listOrders);
                    url="/admin_order.jsp";
                } catch (Exception e) {
                    logger.info("Error at /admin/orderDetail");
                    
                    e.printStackTrace();
                }
                logger.info("Success at admin/orderDetail");
                log("Success at admin/orderDetail");
                req.getRequestDispatcher(url).forward(req, resp);
                 break;
            }
            
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp); //To change body of generated methods, choose Tools | Templates.
    }

}
