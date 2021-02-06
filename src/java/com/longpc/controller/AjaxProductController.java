/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc.controller;

import com.google.gson.Gson;
import com.longpc.dao.LogDAO;
import com.longpc.dao.ProductDAO;
import com.longpc.dto.AccountDTO;
import com.longpc.dto.CartDTO;
import com.longpc.dto.ImageDTO;
import com.longpc.dto.LogProductDTO;
import com.longpc.dto.MessageDTO;
import com.longpc.dto.ProductDTO;
import com.longpc.dto.SearchProductDTO;
import com.longpc.utils.JsonUtil;
import com.longpc.utils.LinkConfig;
import com.longpc.utils.SessionCollector;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.websocket.Session;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 *
 * @author ASUS
 */
@WebServlet(urlPatterns = {"/ajax/product",
    "/ajax/product/detail",
    "/ajax/product/add",
    "/ajax/product/update",
    "/ajax/product/update_image",
    "/ajax/product/search",
    "/ajax/product/remove",
    "/ajax/product/addToCart",
    "/ajax/product/getCart",
    "/ajax/product/removeFromCart",
    "/ajax/product/updateNumCart",
    "/ajax/product/log_update",
    "/ajax/product/active",
    "/ajax/product/async_data"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class AjaxProductController extends HttpServlet {
    private static final String UPDATE="update";
    private static final String REMOVE="remove";
    private static final String ACTIVE="active";
    private ProductDAO productDAO = new ProductDAO();
    private LogDAO logDAO = new LogDAO();
    Logger logger = Logger.getRootLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        String path = req.getServletPath();
        switch (path) {
            case "/ajax/product": {
                String name = req.getParameter("name");
                String fromPrice = req.getParameter("fromPrice");
                String toPrice = req.getParameter("toPrice");
                String idCategory = req.getParameter("idCategory");
                SearchProductDTO searchProductDTO = new SearchProductDTO();
                searchProductDTO.setName(name);
                if (fromPrice.isEmpty()) {
                    searchProductDTO.setFromPrice(-1);
                } else {
                    searchProductDTO.setFromPrice(Float.parseFloat(fromPrice));
                }
                if (toPrice.isEmpty()) {
                    searchProductDTO.setToPrice(-1);
                } else {
                    searchProductDTO.setToPrice(Float.parseFloat(toPrice));
                }
                searchProductDTO.setIdCategory(idCategory);
                int num = Integer.parseInt(req.getParameter("page"));
                PrintWriter out = resp.getWriter();
                String url = "/error.jsp";
                int currentPage = Integer.parseInt("" + req.getParameter("currentPage"));

                try {
                    LinkedHashMap<String, ProductDTO> listProducts = productDAO.searchAdmin(searchProductDTO, num, currentPage);
                    Gson gson = new Gson();
                    String json = gson.toJson(listProducts.values());
                    logger.info("success at /ajax/product");
                    out.write(json);
                    out.flush();
                } catch (Exception e) {
                    logger.error("Error at /ajax/product");
                    e.printStackTrace();
                }
                break;
            }
            case "/ajax/product/search": {
                try {
                    String name = req.getParameter("name");
                    String fromPrice = req.getParameter("fromPrice");
                    String toPrice = req.getParameter("toPrice");
                    String idCategory = req.getParameter("idCategory");
                    SearchProductDTO searchProductDTO = new SearchProductDTO();
                    searchProductDTO.setName(name);
                    if (fromPrice.isEmpty()) {
                        searchProductDTO.setFromPrice(-1);
                    } else {
                        searchProductDTO.setFromPrice(Float.parseFloat(fromPrice));
                    }
                    if (toPrice.isEmpty()) {
                        searchProductDTO.setToPrice(-1);
                    } else {
                        searchProductDTO.setToPrice(Float.parseFloat(toPrice));
                    }
                    searchProductDTO.setIdCategory(idCategory);
                    LinkedHashMap<String, ProductDTO> result = productDAO.searchAdmin(searchProductDTO, 0, 0);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("result", result.values());
                    jSONObject.put("countProduct", productDAO.countSearchAdmin(searchProductDTO, 0, 0));
                    PrintWriter out = resp.getWriter();
                    logger.info("Success at /ajax/product/search");
                    out.write(jSONObject.toString());
                    out.flush();

                } catch (Exception e) {
                    logger.error("Error at /ajax/product/search");
                    e.printStackTrace();

                }
                break;
            }
            case "/ajax/product/detail": {
                String id = req.getParameter("id");
                PrintWriter printWriter = resp.getWriter();
                try {
                    Gson gson = new Gson();
                    ProductDTO productDTO = productDAO.findById(id);
                    if (null != productDTO) {
                        String json = gson.toJson(productDTO);
                        printWriter.write(json);
                        printWriter.flush();
                    }
                    logger.info("Success at /ajax/product/detail");
                } catch (Exception e) {
                    logger.error("Error at /ajax/product/detail");
                    e.printStackTrace();
                }
                break;
            }
            case "/ajax/product/getCart": {
                //String sessionId=req.getParameter("sessionId");
                JSONObject jSONObject = new JSONObject();
                try {
                    HttpSession session = req.getSession();
                    CartDTO cartDTO = (CartDTO) session.getAttribute("cart");
                    jSONObject.put("total", cartDTO.getTotalString());
                    jSONObject.put("listItems", cartDTO.getCart().values());
                    PrintWriter printWriter = resp.getWriter();
                    logger.info("Success at /ajax/product/getCart");
                    printWriter.write(jSONObject.toString());
                    printWriter.flush();

                } catch (Exception e) {
                    logger.error("Error at /ajax/product/getCart");
                    e.printStackTrace();
                }
                break;
            }
            case "/ajax/product/log_update": {
                try {
                    String id = req.getParameter("idProduct");
                    List<LogProductDTO> result = logDAO.getLogProducts(id);
                    PrintWriter printWriter = resp.getWriter();
                    Gson gson = new Gson();
                    logger.info("Success at /ajax/product/log_update");
                    String json = gson.toJson(result);
                    printWriter.write(json);
                    printWriter.flush();
                } catch (Exception e) {
                    logger.error("Error at /ajax/product/log_update");
                    e.printStackTrace();
                }
                break;
            }
            case "/ajax/product/active": {
                try {
                    String id = req.getParameter("productId");
                    LogProductDTO logProductDTO= new LogProductDTO();
                    HttpSession session= req.getSession();
                    AccountDTO accountDTO= (AccountDTO) session.getAttribute("USER");
                    logProductDTO.setUserId(accountDTO.getUserId());
                    logProductDTO.setProductId(id);
                    logProductDTO.setContent(ACTIVE);
                    productDAO.updateActive(id,logProductDTO);
                    JSONObject jSONObject= new JSONObject();
                    jSONObject.put("status", 1);
                    PrintWriter printWriter = resp.getWriter();
                    printWriter.write(jSONObject.toString());
                    printWriter.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            }
            case "/ajax/product/async_data":{
                try{
                    ServletContext servletContext = getServletContext();
                    servletContext.setAttribute("listProducts", productDAO.search(new SearchProductDTO(), 0, 0));
                    JSONObject jSONObject= new JSONObject();
                    jSONObject.put("status", 1);
                    PrintWriter printWriter = resp.getWriter();
                    printWriter.write(jSONObject.toString());
                    printWriter.flush();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
                      
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();
        PrintWriter printWriter = resp.getWriter();
        switch (path) {
            case "/ajax/product/addToCart": {
                JSONObject jSONObject = new JSONObject();
                try {
                    HttpSession session = req.getSession();
                    String idProduct = req.getParameter("idProduct");
                    int quantityCart = Integer.parseInt(req.getParameter("productQuantityCart"));
                    CartDTO cart = new CartDTO();
                    if (null != session.getAttribute("cart")) {
                        cart = (CartDTO) session.getAttribute("cart");
                    }
                    ProductDTO productDTO = productDAO.findById(idProduct);
                    if (null != productDTO) {
                        if (cart.add(productDTO, quantityCart)) {
                            session.setAttribute("cart", cart);
                        } else {
                            jSONObject.put("status", 0);
                            printWriter.write(jSONObject.toString());
                            printWriter.flush();
                            return;
                        }

                    }
                    if (null != session.getAttribute("cart")) {
                        int size = 0;
                        CartDTO cartDTO = (CartDTO) session.getAttribute("cart");
                        if (cartDTO.getCart().values().size() > 0) {
                            for (ProductDTO p: cartDTO.getCart().values()) {
                                size += p.getQuantityInCart();
                            }
                            session.setAttribute("sizeCart", size);
                        }
                    }
                    logger.info("Success at /ajax/product/addToCart");
                } catch (Exception e) {
                    logger.error("Error at /ajax/product/addToCart");
                    e.printStackTrace();
                }
                jSONObject.put("status", 1);
                printWriter.write(jSONObject.toString());
                printWriter.flush();
                break;
            }
            case "/ajax/product/removeFromCart": {
                JSONObject jSONObject = new JSONObject();
                try {
                    HttpSession session = req.getSession();
                    CartDTO cartDTO = (CartDTO) session.getAttribute("cart");
                    String sessionId = req.getParameter("sessionId");
                    String[] arrayRemove = req.getParameterValues("removeArray[]");
                    for (int i = 0; i < arrayRemove.length; i++) {
                        cartDTO.getCart().remove(arrayRemove[i]);
                    }
                    session.setAttribute("cart", cartDTO);
                    if (null != session.getAttribute("cart")) {
                        int size = 0;
                        if (cartDTO.getCart().values().size() > 0) {
                            for (ProductDTO productDTO : cartDTO.getCart().values()) {
                                size += productDTO.getQuantityInCart();
                            }
                            session.setAttribute("sizeCart", size);
                        }else{
                            session.setAttribute("sizeCart", 0);
                        }
                    }
                    jSONObject.put("total", cartDTO.total());
                    jSONObject.put("listItems", cartDTO.getCart().values());
                    jSONObject.put("num", cartDTO.getCart().values().size());
                    logger.info("Success at /ajax/product/removeFromCart");
                } catch (Exception e) {
                    logger.error("Error at /ajax/product/removeFromCart");
                    e.printStackTrace();
                }
                jSONObject.put("status", 1);
                printWriter.write(jSONObject.toString());
                printWriter.flush();
                break;
            }

            case "/ajax/product/update": {
                JSONObject jSONObject = new JSONObject();
                String url = "";
                try {
                    HttpSession session = req.getSession();
                    String name = req.getParameter("name");
                    String price = req.getParameter("price");
                    String quantity = req.getParameter("quantity");
                    String desc = req.getParameter("desc");
                    String type = req.getParameter("type");
                    String id = req.getParameter("id");
                    ImageDTO imageDTO = new ImageDTO();
                    imageDTO.setProductId(id);

                    ProductDTO productDTO = new ProductDTO();
                    productDTO.setId(id);
                    productDTO.setCategoryId(type);
                    productDTO.setPrice(Float.parseFloat(price));
                    productDTO.setName(name);
                    productDTO.setDescription(desc);
                    productDTO.setQuantity(Integer.parseInt(quantity));

                    LogProductDTO logProductDTO = new LogProductDTO();
                    logProductDTO.setProductId(id);
                    logProductDTO.setContent(UPDATE);
                    AccountDTO accountDTO = (AccountDTO) session.getAttribute("USER");
                    logProductDTO.setUserId(accountDTO.getUserId());

                    boolean check = productDAO.update(productDTO, imageDTO, logProductDTO);
                    url = "/product";

                    MessageDTO messageDTO = new MessageDTO();
                    if (check) {
                        LinkedHashMap<String, ProductDTO> listProducts = productDAO.searchAdmin(new SearchProductDTO(), 0, 0);
                        int count = productDAO.countSearchAdmin(new SearchProductDTO(), 0, 0);
                        if (null != listProducts) {
                            ServletContext servletContext = getServletContext();
                            servletContext.setAttribute("countProduct", count);
                            servletContext.setAttribute("listProducts", listProducts.values());
                        }
                        messageDTO.setStatus(true);
                        messageDTO.setContent("Update thành công");
                        session.setAttribute("message", messageDTO);
                    } else {
                        messageDTO.setStatus(false);
                        messageDTO.setContent("Update không thành công");
                        session.setAttribute("message", messageDTO);
                    }
                    if (null != session.getAttribute("cart")) {
                        int size = 0;
                        CartDTO cartDTO = (CartDTO) session.getAttribute("cart");
                        if (cartDTO.getCart().values().size() > 0) {
                            for (ProductDTO p : cartDTO.getCart().values()) {
                                size += p.getQuantityInCart();
                            }
                            session.setAttribute("sizeCart", size);
                        }else{
                            session.setAttribute("sizeCart", 0);
                        }
                    }
                    logger.info("Success at /ajax/product/update");
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("error at /ajax/product/update");
                    req.getRequestDispatcher("error.jsp").forward(req, resp);
                    return;
                }
                resp.sendRedirect(req.getContextPath() + url);
                break;
            }
            case "/ajax/product/update_image": {
                String url = "";
                try {
                    HttpSession session = req.getSession();
                    String id = req.getParameter("id");
                    String image = getUploadFile(req);
                    ImageDTO imageDTO = new ImageDTO();
                    imageDTO.setSourceAddress(image);
                    imageDTO.setProductId(id);

                    LogProductDTO logProductDTO = new LogProductDTO();
                    logProductDTO.setProductId(id);
                    AccountDTO accountDTO = (AccountDTO) session.getAttribute("USER");
                    logProductDTO.setUserId(accountDTO.getUserId());
                    logProductDTO.setContent(UPDATE);
                    boolean check = productDAO.updateImage(imageDTO, logProductDTO);
                    MessageDTO messageDTO = new MessageDTO();
                    url = "/product";
                    if (check) {
                        LinkedHashMap<String, ProductDTO> listProducts = productDAO.search(new SearchProductDTO(), 0, 0);
                        int count = productDAO.countSearchAdmin(new SearchProductDTO(), 0, 0);
                        if (null != listProducts) {
                            ServletContext servletContext = getServletContext();
                            servletContext.setAttribute("countProduct", count);
                            servletContext.setAttribute("listProducts", listProducts.values());
                        }
                        messageDTO.setStatus(true);
                        messageDTO.setContent("Update thành công");
                        session.setAttribute("message", messageDTO);
                    } else {
                        messageDTO.setStatus(false);
                        messageDTO.setContent("Update không thành công");
                        session.setAttribute("message", messageDTO);
                    }
                    logger.info("Success at /ajax/product/update_image");
                } catch (Exception e) {
                    logger.error("Error at /ajax/product/update_image");
                    e.printStackTrace();
                    req.getRequestDispatcher("error.jsp").forward(req, resp);
                    return;
                }
                resp.sendRedirect(req.getContextPath() + url);
                break;
            }
            case "/ajax/product/updateNumCart": {
                JSONObject jSONObject = new JSONObject();
                try {
                    HttpSession session = req.getSession();
                    CartDTO cartDTO = (CartDTO) session.getAttribute("cart");
                    String id = req.getParameter("id");
                    int num = Integer.parseInt(req.getParameter("num"));
                    String[] arrayRemove = req.getParameterValues("removeArray[]");
                    cartDTO.getCart().get(id).setQuantityInCart(num);
                    session.setAttribute("cart", cartDTO);
                    session.setAttribute("sizeCart", num);
                    jSONObject.put("total", cartDTO.total());
                    jSONObject.put("listItems", cartDTO.getCart().values());
                    jSONObject.put("num", cartDTO.getCart().values().size());
                    int realNum = 0;
                    for (Map.Entry<String, ProductDTO> en : cartDTO.getCart().entrySet()) {
                        realNum += en.getValue().getQuantityInCart();
                    }
                    jSONObject.put("realNum", realNum);
                    logger.info("Success at /ajax/product/updateNumCart");
                } catch (Exception e) {
                    logger.error("Error at /ajax/product/updateNumCart");
                    e.printStackTrace();
                }
                jSONObject.put("status", 1);
                printWriter.write(jSONObject.toString());
                printWriter.flush();
                break;
            }
            case "/ajax/product/remove": {
                JSONObject jSONObject = new JSONObject();
                try {
                    HttpSession session = req.getSession();
                    AccountDTO accountDTO=(AccountDTO) session.getAttribute("USER");
                    String[] arrayRemove = req.getParameterValues("removeArray[]");
                    boolean check = productDAO.remove(arrayRemove);
                    productDAO.addLogRemove(arrayRemove, accountDTO.getUserId(),REMOVE);
                    ServletContext context = getServletContext();
                    int count = productDAO.countSearchAdmin(new SearchProductDTO(), 0, 0);
                    context.setAttribute("countProduct", count);
                    if (check) {
                        jSONObject.put("status", 1);
                    } else {
                        jSONObject.put("status", 0);
                    }
                    logger.info("Success at /ajax/product/remove");
                } catch (Exception e) {
                    logger.error("Error at /ajax/product/remove");
                    e.printStackTrace();
                }
                printWriter.write(jSONObject.toString());
                printWriter.flush();
                break;
            }
        }
    }

    public String getUploadFile(HttpServletRequest request) throws Exception {
        String fileName = "";
        for (Part part : request.getParts()) {
            String contentDisp = part.getHeader("content-disposition");
            String[] items = contentDisp.split(";");
            for (String s : items) {
                System.out.println(s);
                if (s.trim().startsWith("filename")) {
                    fileName = s.substring(s.indexOf("=") + 2, s.length() - 1);
                }
            }

            if (!fileName.isEmpty()) {

                if (!fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).equals("png")
                        && !fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).equals("jpg")) {
                    return "fail";
                }

                String serverPath = getServletContext().getRealPath(""); //+ File.separator + "image";
                StringBuilder realPath = new StringBuilder();
                StringTokenizer stk = new StringTokenizer(serverPath, "\\");
                while (stk.hasMoreTokens()) {
                    String tmp = stk.nextToken();
                    if (!tmp.equals("build")) {
                        realPath.append("\\");
                        realPath.append(tmp);
                    }
                }
                realPath.append(File.separator);
                realPath.append("image");
                File folderUpload = new File(realPath.toString());
                if (!folderUpload.exists()) {
                    folderUpload.mkdirs();
                }
                part.write(folderUpload.getAbsolutePath() + File.separator + fileName);
                String src = "image" + File.separator + fileName;
                return src;
            }
        }
        return "";
    }
}
