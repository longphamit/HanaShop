/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc.controller;

import com.longpc.dao.AccountDAO;
import com.longpc.dto.AccountDTO;
import com.longpc.dto.MessageDTO;
import com.longpc.utils.GoogleUtil;
import com.longpc.utils.LinkConfig;
import java.io.IOException;
import java.util.LinkedHashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "AuthenController", urlPatterns = {
    "/auth/login",
    "/auth/logout",
    "/auth/register",
    "/auth/login/google"})
public class AuthenController extends HttpServlet {

    private AccountDAO accountDAO = new AccountDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String path = req.getServletPath();
            switch (path) {
                case "/auth/login": {
                    req.getRequestDispatcher("/login.jsp").forward(req, resp);
                    break;
                }
                case "/auth/logout": {
                    HttpSession session = req.getSession();
                    session.invalidate();
                    req.getRequestDispatcher("/login.jsp").forward(req, resp);
                    break;
                }
                case "/auth/login/google": {
                    String code = req.getParameter("code");
                    String accessToken = GoogleUtil.getToken(code);
                    JSONObject jSONObject = GoogleUtil.getInfoUser(accessToken);
                    String email = jSONObject.getString("email");
                    String id = jSONObject.getString("id");
                    AccountDTO accountDTO = accountDAO.checkAccountWithLoginGoogle(email, id);
                    if (null == accountDTO) {
                        req.getRequestDispatcher("/login.jsp").forward(req, resp);
                    } else {
                        HttpSession session = req.getSession();
                        session.setAttribute("USER", accountDTO);
                        req.getRequestDispatcher("/home.jsp").forward(req, resp);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession();
        switch (path) {
            case "/auth/login": {
                if (null != session.getAttribute("message")) {
                    session.removeAttribute("message");
                }

                String userId = req.getParameter("txtUserId");
                String password = req.getParameter("txtPassword");
                String url = LinkConfig.ERROR_PAGE;
                try {
                    AccountDTO accountDTO = accountDAO.checkLogin(userId, password);
                    if (null != accountDTO) {

                        session.setAttribute("USER", accountDTO);
                        if (accountDTO.getRole().equals("ADMIN")) {
                            session.removeAttribute("cart");
                            url = req.getContextPath() + "/product";
                            resp.sendRedirect(url);
                            return;
                        } else {
                            url = req.getContextPath() + "/home";
                            resp.sendRedirect(url);
                            return;
                        }

                    } else {
                        MessageDTO messageDTO = new MessageDTO();
                        messageDTO.setContent("Đăng nhập thất bại");
                        messageDTO.setStatus(false);
                        session.setAttribute("message", messageDTO);
                        url = req.getContextPath() + "/auth/login";
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
                resp.sendRedirect(url);
                break;
            }
            case "/auth/logout": {
                session.invalidate();
                resp.sendRedirect(req.getContextPath() + "/home");
                break;
            }
            case "/auth/register": {
                String url="";
                try {
                    String userId = req.getParameter("txtUserId");
                    String email = req.getParameter("txtEmail");
                    String password = req.getParameter("txtPassword");
                    AccountDTO accountDTO = new AccountDTO();
                    accountDTO.setEmail(email);
                    accountDTO.setPassword(password);
                    accountDTO.setUserId(userId);
                    boolean check=accountDAO.add(accountDTO);
                    if(check){
                        session.setAttribute("USER", accountDTO);
                        url="/home";
                    }
                    else{
                        MessageDTO messageDTO= new MessageDTO();
                        messageDTO.setStatus(false);
                        messageDTO.setContent("Lỗi đăng ký");
                        session.setAttribute("message",messageDTO);
                        url="/auth/login";
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    if(e.toString().contains("duplicate")){
                        MessageDTO messageDTO= new MessageDTO();
                        messageDTO.setStatus(false);
                        messageDTO.setContent("User Name đã có trong hệ thống");
                        session.setAttribute("message",messageDTO);
                        url="/auth/login";
                    }
                    
                }
                resp.sendRedirect(req.getContextPath()+url);
                break;
            }
        }
    }

}
