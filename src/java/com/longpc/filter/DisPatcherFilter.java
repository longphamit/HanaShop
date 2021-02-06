/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc.filter;

import com.longpc.dto.AccountDTO;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ASUS
 */
@WebFilter(filterName = "DispatchFilter", urlPatterns = {"/*"})
public class DisPatcherFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        System.out.println(req.getServletPath());
        String url = "/error.jsp";
        if (req.getRequestURI().contains(".css") || req.getRequestURI().contains(".js")||req.getRequestURI().contains(".jpg")) {
            chain.doFilter(request, response);
            return;
        }
        if (null == session.getAttribute("USER")) {
            if (req.getServletPath().contains("/AdminController")) {
                resp.sendRedirect("login.jsp");
                return;
            }
            if (req.getServletPath().equals("/home.jsp")) {
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }
            if (req.getServletPath().startsWith("/product")) {
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }
        } else {
            if (req.getServletPath().startsWith("/product")) {
                AccountDTO accountDTO = (AccountDTO) session.getAttribute("USER");
                if (null == accountDTO || !accountDTO.getRole().equals("ADMIN")) {
                    resp.sendRedirect(req.getContextPath() + "/home");
                    return;
                }
            }
        }
        chain.doFilter(request, response);
        return;
    }

    @Override
    public void destroy() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

}
