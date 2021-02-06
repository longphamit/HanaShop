/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc.utils;

import com.longpc.dao.ProductDAO;
import com.longpc.dto.CartDTO;
import com.longpc.dto.ProductDTO;
import com.longpc.dto.SearchProductDTO;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ASUS
 */
public class PagingUtil {

    private ProductDAO productDAO = new ProductDAO();

    public LinkedHashMap<String, ProductDTO> pagingJSTL(SearchProductDTO searchProductDTO, HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession();
        String pageParam = req.getParameter("page");
        int page = 0;
        if (null != pageParam) {
            page = Integer.parseInt(pageParam);
        }
        if (page == 0) {
            session.setAttribute("currentPage", page);
            LinkedHashMap<String, ProductDTO> listProductDTOs = productDAO.search(searchProductDTO, page, 0);
            session.setAttribute("countProduct", productDAO.countSearch(searchProductDTO, page, page));
            req.setAttribute("listProducts", listProductDTOs.values());
            return listProductDTOs;
        } else if (page > 0) {
            session.setAttribute("currentPage", page);
            LinkedHashMap<String, ProductDTO> listProductDTOs = productDAO.search(searchProductDTO, page, 0);
            session.setAttribute("countProduct", productDAO.countSearch(searchProductDTO, page, page));
            req.setAttribute("listProducts", listProductDTOs.values());
            return listProductDTOs;
        } else if (page < 0) {
            int currentPage = 0;
            try {
                currentPage = Integer.parseInt(session.getAttribute("currentPage") + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            LinkedHashMap<String, ProductDTO> listProductDTOs = productDAO.search(searchProductDTO, page, currentPage);
            req.setAttribute("listProducts", listProductDTOs.values());
            session.setAttribute("countProduct", productDAO.countSearch(searchProductDTO, page, page));
            if (page == -1) {
                session.setAttribute("currentPage", currentPage - 1);
            } else if (page == -2) {
                session.setAttribute("currentPage", currentPage + 1);
            }
            System.out.println(session.getAttribute("currentPage"));
            
            return listProductDTOs;
        }
        return null;
    }

    public void PagingAjax() {

    }
}
