/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc.controller;

import com.longpc.dao.CategoryDAO;
import com.longpc.dao.ProductDAO;
import com.longpc.dto.CategoryDTO;
import com.longpc.dto.MessageDTO;
import com.longpc.dto.ProductDTO;
import com.longpc.dto.SearchProductDTO;
import com.longpc.utils.LinkConfig;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.log4j.Logger;

/**
 *
 * @author ASUS
 */
@WebServlet(urlPatterns = {"/product", "/product/add", "/product/category/add"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class ProductController extends HttpServlet {
    private Logger logger= Logger.getRootLogger();
    private ProductDAO productDAO = new ProductDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private final String LIST_PRODUCT="listProducts";
    private final String COUNT_PRODUCT="countProduct";
    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.
        try {
            ServletContext context = getServletContext();
            LinkedHashMap<String, ProductDTO> listProductDTOs = productDAO.searchAdmin(new SearchProductDTO(),0, 0);
            int count=productDAO.countSearchAdmin(new SearchProductDTO(), 0, 0);
            //context.setAttribute("listProducts", listProductDTOs.values());
            context.setAttribute("listCategories", categoryDAO.findAll());
            context.setAttribute(COUNT_PRODUCT, count);
            logger.info("init data in app context");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error at init data");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        switch (path) {
            case "/product": {
                try {
                    req.getRequestDispatcher("/admin.jsp").forward(req, resp);
                    logger.info("Success at /product");
                } catch (Exception e) {
                    logger.error("Error at /product");
                    e.printStackTrace();
                }
                break;
            }
            case "/product/search": {
                try {
                    String name = req.getParameter("txtName");
                    String fromDate = req.getParameter("txtFromDate");
                    String toDate = req.getParameter("txtToDate");
                    String idCategory = req.getParameter("txtIdCategory");
                    SearchProductDTO searchProductDTO = new SearchProductDTO();
                    searchProductDTO.setName(name);
                    searchProductDTO.setIdCategory(idCategory);
                    HashMap<String, ProductDTO> result = productDAO.searchAdmin(searchProductDTO,0,0);
                    ServletContext context = getServletContext();
                    logger.info("Success at ");
                    req.setAttribute("listProducts", result);
                    req.getRequestDispatcher("admin.jsp").forward(req, resp);
                    
                } catch (Exception e) {
                    logger.error("Error at /product/search");
                    e.printStackTrace();
                    resp.sendRedirect(LinkConfig.ERROR_PAGE);
                }
                break;
            }

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        switch (path) {
            case "/product/add": {
                resp.setContentType("text/html;charset=UTF-8");
                req.setCharacterEncoding("UTF-8");
                String url = LinkConfig.ERROR_PAGE;
                try {
                    String src = getUploadFile(req);
                    String name = req.getParameter("txtProductName");
                    float price = Float.parseFloat(req.getParameter("txtProductPrice"));
                    int quantity = Integer.parseInt(req.getParameter("txtProductQuantity"));
                    String desc = req.getParameter("txtProductDesc");
                    String idCategory = req.getParameter("txtProductCategory");
                    ProductDTO productDTO = new ProductDTO();
                    productDTO.setName(name);
                    productDTO.setPrice(price);
                    productDTO.setQuantity(quantity);
                    productDTO.setCategoryId(idCategory);
                    productDTO.setDescription(desc);
                    boolean check = productDAO.add(productDTO, src);
                    HttpSession session = req.getSession();
                    if (check) {

                        LinkedHashMap<String, ProductDTO> listProducts = productDAO.searchAdmin(new SearchProductDTO(),0, 0);
                        int count=productDAO.countSearchAdmin(new SearchProductDTO(),0, 0);
                        if (null != listProducts) {
                            ServletContext servletContext = getServletContext();
                            servletContext.setAttribute("listProducts", listProducts.values());
                            servletContext.setAttribute(COUNT_PRODUCT, count);
                        }
                        MessageDTO messageDTO= new MessageDTO();
                        messageDTO.setContent("Thêm sản phẩm thành công");
                        messageDTO.setStatus(true);
                        session.setAttribute("message", messageDTO);
                    } else {
                        MessageDTO messageDTO= new MessageDTO();
                        messageDTO.setContent("Thêm sản phẩm thất bại");
                        messageDTO.setStatus(false);
                        session.setAttribute("message", messageDTO);
                    }
                    logger.info("Success at /product/add");
                    resp.sendRedirect(req.getContextPath() + "/product");
                } catch (Exception e) {
                    logger.error("Error at /product/add");
                    e.printStackTrace();
                }
                break;
            }
            case "/product/category/add": {
                req.setCharacterEncoding("UTF-8");
                resp.setCharacterEncoding("UTF-8");
                String url = LinkConfig.ERROR_PAGE;
                try {
                    String txtName = req.getParameter("txtNameCategory");
                    CategoryDTO categoryDTO = new CategoryDTO();
                    categoryDTO.setName(txtName);
                    boolean check = categoryDAO.add(categoryDTO);
                    ServletContext servletContext = getServletContext();
                    List<CategoryDTO> listCategoryDTOs = (List<CategoryDTO>) servletContext.getAttribute("listCategories");
                    listCategoryDTOs.add(categoryDTO);
                    servletContext.setAttribute("listCategories", listCategoryDTOs);
                    HttpSession session = req.getSession();
                    if (check) {
                        url = req.getContextPath() + "/product";
                        MessageDTO messageDTO= new MessageDTO();
                        messageDTO.setContent("Thêm loại sản phẩm thành công");
                        messageDTO.setStatus(true);
                        session.setAttribute("message", messageDTO);
                    } else {
                        url = req.getContextPath() + "/product";
                        MessageDTO messageDTO= new MessageDTO();
                        messageDTO.setContent("Thêm loại sản phẩm thất bại");
                        messageDTO.setStatus(false);
                        session.setAttribute("message", messageDTO);
                    }
                    logger.info("Succcess at /product/category/add");
                } catch (Exception e) {
                    logger.error("Error at /product/category/add");
                    e.printStackTrace();
                }
                resp.sendRedirect(url);
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
                StringBuilder realPath=new StringBuilder();
                StringTokenizer stk= new StringTokenizer(serverPath,"\\");
                while(stk.hasMoreTokens()){
                    String tmp=stk.nextToken();
                    if(!tmp.equals("build")){
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
