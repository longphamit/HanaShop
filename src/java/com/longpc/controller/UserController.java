/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc.controller;

import com.google.gson.Gson;
import com.longpc.dao.CategoryDAO;
import com.longpc.dao.OrderDAO;
import com.longpc.dao.ProductDAO;
import com.longpc.dto.AccountDTO;
import com.longpc.dto.CartDTO;
import com.longpc.dto.MessageDTO;
import com.longpc.dto.OrderDTO;
import com.longpc.dto.ProductDTO;
import com.longpc.dto.SearchProductDTO;
import com.longpc.utils.LinkConfig;
import com.longpc.utils.PagingUtil;
import com.longpc.utils.PagingUtilPro;
import com.longpc.utils.PayPalUtil;
import com.paypal.api.payments.Payment;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 *
 * @author ASUS
 */
@WebServlet(urlPatterns = {
    "/home",
    "/user/paypal",
    "/user/detail",
    "/user/order",
    "/user/search",
    "/user/product/detail",
    "/user/order/search",
    "/ajax/user/order/detail",
    "/user/paypal/review_payment",
    "/user/paypal/excute_payment",
    "/user/product/add_to_cart",
    "/user/search/autocomplete"})
public class UserController extends HttpServlet {

    private OrderDAO orderDAO = new OrderDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private Logger logger = Logger.getRootLogger();

    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.
        try {
            categoryDAO.findAll();
            ServletContext context = getServletContext();
            LinkedHashMap<String, ProductDTO> listProductDTOs = productDAO.search(new SearchProductDTO(), 0, 0);
            int count = productDAO.countSearch(new SearchProductDTO(), 0, 0);
            context.setAttribute("listProducts", listProductDTOs.values());
            context.setAttribute("listCategories", categoryDAO.findAll());
            context.setAttribute("countProduct", count);
            logger.info("Success at init data app context");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Success at init data app context");
        }
    }
    private ProductDAO productDAO = new ProductDAO();
    private PagingUtil pagingUtil = new PagingUtil();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        resp.setContentType("application/json;charset=UTF-8");
        switch (path) {
            case "/home": {
                try {
                    String name = req.getParameter("name");
                    String fromPrice = req.getParameter("fromPrice");
                    String toPrice = req.getParameter("toPrice");
                    String idCategory = req.getParameter("idCategory");
                    SearchProductDTO searchProductDTO = new SearchProductDTO();
                    searchProductDTO.setName(name);
                    if (null == fromPrice || fromPrice.isEmpty()) {
                        searchProductDTO.setFromPrice(-1);
                    } else {
                        searchProductDTO.setFromPrice(Float.parseFloat(fromPrice));
                    }
                    if (null == fromPrice || toPrice.isEmpty()) {
                        searchProductDTO.setToPrice(-1);
                    } else {
                        searchProductDTO.setToPrice(Float.parseFloat(toPrice));
                    }
                    searchProductDTO.setIdCategory(idCategory);
                    HttpSession session = req.getSession();

                    if (null != session.getAttribute("cart")) {
                        int size = 0;
                        CartDTO cartDTO = (CartDTO) session.getAttribute("cart");
                        if (cartDTO.getCart().values().size() > 0) {
                            for (ProductDTO productDTO : cartDTO.getCart().values()) {
                                size += productDTO.getQuantityInCart();
                            }
                            session.setAttribute("sizeCart", size);
                        }
                    }

                    
                    int page = 1;
                    String pageParam = req.getParameter("page");
                    if (null != pageParam) {
                        page = Integer.parseInt(pageParam);
                    }
                    PagingUtilPro pagingUtilPro = new PagingUtilPro();
                    int count = productDAO.countSearch(searchProductDTO, 0, 0);
                    pagingUtilPro.setCurrentPage(page);
                    int pageTotal=pagingUtilPro.calculatorMinMax(count);
                    session.setAttribute("currentPage", page);
                    session.setAttribute("pageTotal",pageTotal);
                    LinkedHashMap<String, ProductDTO> listProductDTOs = productDAO.search(searchProductDTO, page-1, 0);
                    session.setAttribute("countProduct", productDAO.countSearch(searchProductDTO, page-1, page-1));
                    req.setAttribute("listProducts", listProductDTOs.values());
                    session.setAttribute("minPage", pagingUtilPro.getMinPage());
                    session.setAttribute("maxPage", pagingUtilPro.getMaxPage());
                    logger.info("Success at /home");
                } catch (Exception e) {
                    logger.error("Error at /home");
                    e.printStackTrace();
                }
                req.getRequestDispatcher("/home.jsp").forward(req, resp);
                break;
            }
            case "/user/order": {
                try {
                    HttpSession session = req.getSession();
                    AccountDTO accountDTO = (AccountDTO) session.getAttribute("USER");
                    if (accountDTO == null) {
                        logger.info("Success at ");
                        resp.sendRedirect(req.getContextPath() + "/auth/login");
                        return;
                    } else {
                        CartDTO cartDTO = (CartDTO) session.getAttribute("cart");
                        OrderDTO orderDTO = new OrderDTO();
                        List<ProductDTO> productDTOs = productDAO.getListProductByCart(cartDTO.getCart());
                        List<ProductDTO> outOfStockList = new ArrayList<>();

                        if (productDTOs != null) {
                            for (int i = 0; i < productDTOs.size(); i++) {
                                if (productDTOs.get(i).getQuantity() < cartDTO.getCart().get(productDTOs.get(i).getId()).getQuantityInCart()) {
                                    outOfStockList.add(cartDTO.getCart().get(productDTOs.get(i).getId()));
                                }
                            }
                            if (outOfStockList.size() != 0) {
                                session.setAttribute("outOfStock", outOfStockList);
                            } else {
                                if (orderDAO.addOrder(cartDTO, orderDTO, accountDTO, productDTOs)) {
                                    CartDTO cartDTONew = new CartDTO();

                                    session.setAttribute("cart", cartDTONew);
                                    session.setAttribute("sizeCart", 0);
                                    MessageDTO messageDTO = new MessageDTO();
                                    messageDTO.setStatus(true);
                                    messageDTO.setContent("Mua hàng thành công");
                                    session.setAttribute("message", messageDTO);

                                    ServletContext context = getServletContext();
                                    LinkedHashMap<String, ProductDTO> listProductDTOs = productDAO.search(new SearchProductDTO(), 0, 0);
                                    context.setAttribute("listProducts", listProductDTOs.values());
                                }
                            }
                        }
                    }
                    logger.info("Success at /user/order");
                } catch (Exception e) {
                    logger.error("Error at /user/order");
                    e.printStackTrace();
                }
                resp.sendRedirect(req.getContextPath() + "/home");
                break;
            }
            case "/user/search": {
                String url = "/error.jsp";
                try {
                    String name = req.getParameter("name");
                    String fromPrice = req.getParameter("fromPrice");
                    String toPrice = req.getParameter("toPrice");
                    String idCategory = req.getParameter("idCategory");
                    SearchProductDTO searchProductDTO = new SearchProductDTO();
                    searchProductDTO.setName(name);
                    if (null == fromPrice || fromPrice.isEmpty()) {
                        searchProductDTO.setFromPrice(-1);
                    } else {
                        searchProductDTO.setFromPrice(Float.parseFloat(fromPrice));
                    }
                    if (null == toPrice || toPrice.isEmpty()) {
                        searchProductDTO.setToPrice(-1);
                    } else {
                        searchProductDTO.setToPrice(Float.parseFloat(toPrice));
                    }
                    searchProductDTO.setIdCategory(idCategory);
                    PagingUtilPro pagingUtilPro = new PagingUtilPro();
                    int count = productDAO.countSearch(searchProductDTO, 0, 0);
                    int page = 1;
                    String pageParam = req.getParameter("page");
                    if (null != pageParam) {
                        page = Integer.parseInt(pageParam);
                    }
                    HttpSession session= req.getSession();
                    pagingUtilPro.setCurrentPage(page);
                    int pageTotal=pagingUtilPro.calculatorMinMax(count);
                    session.setAttribute("currentPage", page);
                    session.setAttribute("pageTotal",pageTotal);
                    LinkedHashMap<String, ProductDTO> listProductDTOs = productDAO.search(searchProductDTO, page-1, 0);
                    session.setAttribute("countProduct", productDAO.countSearch(searchProductDTO, page-1, page-1));
                    req.setAttribute("listProducts", listProductDTOs.values());
                    session.setAttribute("minPage", pagingUtilPro.getMinPage());
                    session.setAttribute("maxPage", pagingUtilPro.getMaxPage());

                    url = "/home.jsp";
                    logger.info("Success at /user/search");
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Error at /user/search");
                }
                req.getRequestDispatcher(url).forward(req, resp);
                break;
            }
            case "/user/detail": {
                String url = "/error.jsp";
                try {
                    HttpSession session = req.getSession();
                    AccountDTO accountDTO = (AccountDTO) session.getAttribute("USER");
                    List<OrderDTO> listOrders = orderDAO.getByUserId(accountDTO.getUserId());
                    req.setAttribute("listOrders", listOrders);
                    url = "/user.jsp";
                    logger.info("Success at /user/detail");
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Error at /user/detail");
                }
                req.getRequestDispatcher(url).forward(req, resp);
                break;
            }
            case "/ajax/user/order/detail": {
                try {
                    PrintWriter printWriter = resp.getWriter();
                    String orderId = req.getParameter("id");
                    OrderDAO orderDAO = new OrderDAO();
                    OrderDTO orderDTO = orderDAO.getOrderDetail(orderId);
                    Gson gson = new Gson();
                    JSONObject jSONObject = new JSONObject();
                    logger.info("Success at /ajax/user/order/detail");
                    jSONObject.put("listProducts", orderDTO.getListProducts().values());
                    jSONObject.put("total", orderDTO.getTotal());
                    printWriter.write(jSONObject.toString());
                    printWriter.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Error at /ajax/user/order/detail");
                }
                break;
            }
            case "/user/order/search": {
                String url = "/error.jsp";
                try {
                    HttpSession session = req.getSession();
                    AccountDTO accountDTO = (AccountDTO) session.getAttribute("USER");
                    String fromDate = req.getParameter("fromDate");
                    String toDate = req.getParameter("toDate");
                    List<OrderDTO> orderDTOs = orderDAO.getByUserIdAndDate(accountDTO.getUserId(), fromDate, toDate);
                    req.setAttribute("listOrders", orderDTOs);
                    url = "/user.jsp";
                    logger.info("Success at /user/order/search");
                } catch (Exception e) {
                    logger.error("Error at /user/order/search");
                    e.printStackTrace();
                }
                req.getRequestDispatcher(url).forward(req, resp);
                break;
            }
            case "/user/paypal": {
                try {
                    HttpSession session = req.getSession();
                    CartDTO cartDTO = (CartDTO) session.getAttribute("cart");
                    OrderDTO orderDTO = new OrderDTO();
                    List<ProductDTO> productDTOs = productDAO.getListProductByCart(cartDTO.getCart());
                    List<ProductDTO> outOfStockList = new ArrayList<>();
                    for (int i = 0; i < productDTOs.size(); i++) {
                        if (productDTOs.get(i).getQuantity() < cartDTO.getCart().get(productDTOs.get(i).getId()).getQuantityInCart()) {
                            outOfStockList.add(cartDTO.getCart().get(productDTOs.get(i).getId()));
                        }
                    }
                    if (outOfStockList.size() != 0) {
                        session.setAttribute("outOfStock", outOfStockList);
                        resp.sendRedirect(req.getContextPath() + "/home");
                        return;
                    }
                    if (null == session.getAttribute("USER")) {
                        resp.sendRedirect(req.getContextPath() + "/auth/login");
                        return;
                    } else {
                        AccountDTO accountDTO = (AccountDTO) session.getAttribute("USER");
                        cartDTO = (CartDTO) session.getAttribute("cart");
                        PayPalUtil paypalUtil = new PayPalUtil();
                        String approvalLink = paypalUtil.authorizePayment(accountDTO, cartDTO);
                        resp.sendRedirect(approvalLink);
                    }
                    logger.info("Success at /user/paypal");
                } catch (Exception e) {
                    logger.error("Error at /user/paypal");
                    e.printStackTrace();
                }
                break;
            }
            case "/user/paypal/review_payment": {
                String paymentId = req.getParameter("paymentId");
                String payerId = req.getParameter("PayerID");
                try {
                    PayPalUtil payPalUtil = new PayPalUtil();
                    Payment payment = payPalUtil.getPaymentDetail(paymentId);

                    HttpSession session = req.getSession();
                    CartDTO cartDTO = (CartDTO) session.getAttribute("cart");
                    cartDTO.setPaymentId(paymentId);
                    cartDTO.setPayerId(payerId);
                    logger.info("Success at /user/paypal/review_payment");
                    session.setAttribute("totalReview", cartDTO.getTotalString());
                    resp.sendRedirect(req.getContextPath() + "/review_payment.jsp");
                    return;
                } catch (Exception e) {
                    logger.error("Error at /user/paypal/review_payment");
                    e.printStackTrace();
                }
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
            }
            case "/user/paypal/excute_payment": {
                PayPalUtil payPalUtil = new PayPalUtil();
                HttpSession session = req.getSession();
                AccountDTO accountDTO = (AccountDTO) session.getAttribute("USER");
                CartDTO cartDTO = (CartDTO) session.getAttribute("cart");
                String payerId = cartDTO.getPayerId();
                String paymentId = cartDTO.getPaymentId();
                String url = "/error.jsp";
                try {

                    OrderDTO orderDTO = new OrderDTO();
                    List<ProductDTO> productDTOs = productDAO.getListProductByCart(cartDTO.getCart());
                    List<ProductDTO> outOfStockList = new ArrayList<>();
                    if (productDTOs != null) {
                        for (int i = 0; i < productDTOs.size(); i++) {
                            if (productDTOs.get(i).getQuantity() < cartDTO.getCart().get(productDTOs.get(i).getId()).getQuantityInCart()) {
                                outOfStockList.add(cartDTO.getCart().get(productDTOs.get(i).getId()));
                            }
                        }
                        if (outOfStockList.size() != 0) {
                            session.setAttribute("outOfStock", outOfStockList);
                        } else {
                            if (orderDAO.addOrder(cartDTO, orderDTO, accountDTO, productDTOs)) {
                                CartDTO cartDTONew = new CartDTO();
                                payPalUtil.excutePayment(paymentId, payerId);
                                session.setAttribute("cart", cartDTONew);
                                session.setAttribute("sizeCart", 0);
                                MessageDTO messageDTO = new MessageDTO();
                                messageDTO.setStatus(true);
                                messageDTO.setContent("Mua hàng thành công");
                                session.setAttribute("message", messageDTO);
                                ServletContext context = getServletContext();
                                LinkedHashMap<String, ProductDTO> listProductDTOs = productDAO.search(new SearchProductDTO(), 0, 0);
                                context.setAttribute("listProducts", listProductDTOs.values());

                            }
                        }
                    }
                    logger.info("Success at /user/paypal/excute_payment");
                    url = "/home";
                } catch (Exception e) {
                    logger.error("Error at /user/paypal/excute_payment");
                    e.printStackTrace();
                }
                resp.sendRedirect(req.getContextPath() + url);
                break;
            }
            case "/user/product/detail": {
                String url = "/error.jsp";
                try {
                    String idProduct = req.getParameter("idProductDetail");
                    LinkedHashMap<String, ProductDTO> listRecommend = productDAO.getProductPurchaseTogether(idProduct);
                    ProductDTO productDTO = productDAO.findByIdWithImage(idProduct);
                    req.setAttribute("listRecommend", listRecommend);
                    logger.info("Success at /user/product/detail");
                    req.setAttribute("productDetail", productDTO);
                    url = "/detail_product.jsp";
                } catch (Exception e) {
                    logger.error("Error at /user/product/detail");
                    e.printStackTrace();
                }
                req.getRequestDispatcher(url).forward(req, resp);

                break;
            }
            case "/user/search/autocomplete":{
                resp.setContentType("application/json;UTF-8");
                String url = "/error.jsp";
                try{
                    List<String> listProducts= productDAO.getAllName();
                    PrintWriter writer= resp.getWriter();
                    Gson gson= new Gson();
                    writer.write(gson.toJson(listProducts));
                    writer.flush();
                }catch(Exception e){
                    logger.error("Error at /user/product/detail");
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        switch (path) {
            case "/user/product/add_to_cart": {
                String url = "error.jsp";
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
                        }
                    }
                    MessageDTO messageDTO = new MessageDTO();
                    messageDTO.setContent("Đã thêm vào giỏ hàng");
                    messageDTO.setStatus(true);
                    session.setAttribute("message", messageDTO);
                    url = "/user/product/detail?idProductDetail=" + idProduct;
                    if (null != session.getAttribute("cart")) {
                        int size = 0;
                        CartDTO cartDTO = (CartDTO) session.getAttribute("cart");
                        if (cartDTO.getCart().values().size() > 0) {
                            for (ProductDTO p : cartDTO.getCart().values()) {
                                size += p.getQuantityInCart();
                            }
                            session.setAttribute("sizeCart", size);
                        }
                    }
                    logger.info("Success at /user/product/add_to_cart");
                } catch (Exception e) {
                    logger.error("Error at /user/product/add_to_cart");
                    e.printStackTrace();
                }
                resp.sendRedirect(req.getContextPath() + "/" + url);
                break;
            }
        }
    }

}
