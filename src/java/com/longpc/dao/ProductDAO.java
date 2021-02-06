/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc.dao;

import com.longpc.dto.AccountDTO;
import com.longpc.dto.ImageDTO;
import com.longpc.dto.LogProductDTO;
import com.longpc.utils.DBUtil;
import com.longpc.dto.ProductDTO;
import com.longpc.dto.SearchProductDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author ASUS
 */
public class ProductDAO extends BaseDAO {

    protected final int SIZE = 5;
    private final int NEXT = -2;
    private final int PREVIOUS = -1;
    private String sqlSearch = "";
//    public LinkedHashMap<String, ProductDTO> listProduct(int num, int currentNum) throws Exception {
//        String sql = "select p.id,p.name,p.createDate,p.status,p.quantity,p.description,p.price,p.categoryId,img.sourceAddress,img.id as idImage, ca.name as categoryName from tblProducts p, Images img,tblCategories ca where "
//                + "p.status = 1 and p.quantity >0 and p.id=img.productId and ca.id=p.categoryId "
//                + "order by p.createDate DESC "
//                + "OFFSET ? ROW FETCH NEXT ? ROWS ONLY";
//        int offset = 0;
//        if (num >= 0) {
//            offset = num * SIZE;
//        } else {
//            if (num == PREVIOUS) {
//                offset = (currentNum - 1) * SIZE;
//            } else if (num == NEXT) {
//                offset = (currentNum + 1) * SIZE;
//            }
//        }
//        try {
//            cn = DBUtil.makeConnection();
//            if (cn != null) {
//                ps = cn.prepareStatement(sql);
//                ps.setObject(1, offset);
//                ps.setObject(2, SIZE);
//                rs = ps.executeQuery();
//                LinkedHashMap<String, ProductDTO> productDTOs = new LinkedHashMap<>();
//                while (rs.next()) {
//                    ProductDTO productDTO = new ProductDTO();
//                    String id = rs.getString("id");
//                    if (productDTOs.containsKey(id)) {
//                        productDTO = productDTOs.get(id);
//                        ImageDTO imageDTO = new ImageDTO();
//                        imageDTO.setId(rs.getString("idImage"));
//                        imageDTO.setSourceAddress(rs.getString("sourceAddress"));
//                        productDTO.getImages().add(imageDTO);
//                    } else {
//                        ImageDTO imageDTO = new ImageDTO();
//                        imageDTO.setId(rs.getString("idImage"));
//                        imageDTO.setSourceAddress(rs.getString("sourceAddress"));
//                        productDTO.getImages().add(imageDTO);
//                        productDTO.setId(rs.getString("id"));
//                        productDTO.setName(rs.getString("name"));
//                        productDTO.setCreateDate(rs.getTimestamp("createDate"));
//                        productDTO.setStatus(rs.getString("status"));
//                        productDTO.setQuantity(rs.getInt("quantity"));
//                        productDTO.setDescription(rs.getString("description"));
//                        productDTO.setPrice(rs.getFloat("price"));
//                        productDTO.setCategoryId(rs.getString("categoryId"));
//                        productDTO.setCategoryName(rs.getString("categoryName"));
//                        productDTOs.put(id, productDTO);
//                    }
//                }
//                return productDTOs;
//            }
//        } finally {
//            DBUtil.closeConnection(cn, ps, rs);
//        }
//        return null;
//    }

    public boolean add(ProductDTO productDTO, String src) throws Exception {
        try {
            productDTO.setId(UUID.randomUUID().toString());
            String sqlSaveImage = "insert into Images(id,sourceAddress,productId) values(?,?,?)";
            String sql = "insert into tblProducts(id,name,createDate,status,quantity,description,price,categoryId) values(?,?,?,?,?,?,?,?)";
            cn = DBUtil.makeConnection();
            cn.setAutoCommit(false);
            if (cn != null) {
                ps = cn.prepareStatement(sql);
                ps.setObject(1, productDTO.getId());
                ps.setObject(2, productDTO.getName());
                ps.setObject(3, new Timestamp(productDTO.getCreateDate().getTime()));
                ps.setObject(4, productDTO.getStatus());
                ps.setObject(5, productDTO.getQuantity());
                ps.setObject(6, productDTO.getDescription());
                ps.setObject(7, productDTO.getPrice());
                ps.setObject(8, productDTO.getCategoryId());
                int check = ps.executeUpdate();
                if (check > 0) {
                    ps = cn.prepareStatement(sqlSaveImage);
                    ps.setObject(1, UUID.randomUUID().toString());
                    ps.setObject(2, src);
                    ps.setObject(3, productDTO.getId());
                    check = ps.executeUpdate();
                    if (check > 0) {
                        cn.commit();
                        return true;
                    }
                }
            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return false;
    }

    public ProductDTO findById(String id) throws Exception {
        String sql = "select id,name,createDate,status,quantity,description,price,categoryId from tblProducts where id = ?";
        try {
            cn = DBUtil.makeConnection();
            if (cn != null) {
                ps = cn.prepareStatement(sql);
                ps.setObject(1, id);
                rs = ps.executeQuery();
                if (rs.next()) {
                    ProductDTO productDTO = new ProductDTO();
                    productDTO.setId(rs.getString("id"));
                    productDTO.setName(rs.getString("name"));
                    productDTO.setCreateDate(rs.getTimestamp("createDate"));
                    productDTO.setStatus(rs.getString("status"));
                    productDTO.setQuantity(rs.getInt("quantity"));
                    productDTO.setDescription(rs.getString("description"));
                    productDTO.setPrice(rs.getFloat("price"));
                    productDTO.setCategoryId(rs.getString("categoryId"));
                    return productDTO;
                }
            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return null;
    }

    public boolean updateImage(ImageDTO imageDTO, LogProductDTO logProductDTO) throws Exception {
        String sqlUpdateImage = "update Images set sourceAddress= ? where productId = ?";
        String sqlLog = "insert into tblLogProduct(id,userId,productId,updateDate,content) values(?,?,?,?,?) ";
        try {
            cn = DBUtil.makeConnection();
            if (null != cn) {
                cn.setAutoCommit(false);
                ps = cn.prepareStatement(sqlUpdateImage);
                ps.setObject(1, imageDTO.getSourceAddress());
                ps.setObject(2, imageDTO.getProductId());
                ps.executeUpdate();
                ps = cn.prepareStatement(sqlLog);
                ps.setObject(1, logProductDTO.getId());
                ps.setObject(2, logProductDTO.getUserId());
                ps.setObject(3, logProductDTO.getProductId());
                ps.setObject(4, new Timestamp(logProductDTO.getUpdateDate().getTime()));
                ps.setObject(5, logProductDTO.getContent());
                ps.executeUpdate();
                cn.commit();
                return true;
            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return false;
    }

    public boolean update(ProductDTO productDTO, ImageDTO imageDTO, LogProductDTO logProductDTO) throws Exception {
        String sql = "update tblProducts set name = ?,quantity = ?,description = ?,price = ?,categoryId=? where id = ?";
        String sqlLog = "insert into tblLogProduct(id,userId,productId,updateDate,content) values(?,?,?,?,?) ";
        try {
            cn = DBUtil.makeConnection();
            if (cn != null) {
                cn.setAutoCommit(false);
                ps = cn.prepareStatement(sql);
                ps.setObject(1, productDTO.getName());
                ps.setObject(2, productDTO.getQuantity());
                ps.setObject(3, productDTO.getDescription());
                ps.setObject(4, productDTO.getPrice());
                ps.setObject(5, productDTO.getCategoryId());
                ps.setObject(6, productDTO.getId());
                ps.executeUpdate();
                ps = cn.prepareStatement(sqlLog);
                ps.setObject(1, logProductDTO.getId());
                ps.setObject(2, logProductDTO.getUserId());
                ps.setObject(3, logProductDTO.getProductId());
                ps.setObject(4, new Timestamp(logProductDTO.getUpdateDate().getTime()));
                ps.setObject(5, logProductDTO.getContent());
                ps.executeUpdate();
                cn.commit();
                return true;
            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return false;
    }

    public int countSearch(SearchProductDTO searchProductDTO, int num, int currentNum) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select Count(p.id) as numProduct  from tblProducts p, Images img, tblCategories ca where ");
        sql.append("p.status = 1 and p.quantity >0 and p.id=img.productId and p.categoryId=ca.id ");
        HashMap<String, Boolean> statusProducSearch = new HashMap<>();
        statusProducSearch.put("name", Boolean.FALSE);
        statusProducSearch.put("fromPrice", Boolean.FALSE);
        statusProducSearch.put("toPrice", Boolean.FALSE);
        statusProducSearch.put("idCategory", Boolean.FALSE);
        statusProducSearch.put("name", Boolean.FALSE);

        try {
            cn = DBUtil.makeConnection();
            if (cn != null) {
                for (Map.Entry<String, Boolean> entry : statusProducSearch.entrySet()) {
                    if (entry.getKey().equals("name")) {
                        if (null != searchProductDTO.getName() && !searchProductDTO.getName().isEmpty()) {
                            sql.append("and p.name like ? ");
                            statusProducSearch.put("name", Boolean.TRUE);
                        }
                    }
                    if (entry.getKey().equals("fromPrice")) {
                        if (searchProductDTO.getFromPrice() > 0) {
                            sql.append("and p.price >= ? ");
                            statusProducSearch.put("fromPrice", Boolean.TRUE);
                        }
                    }
                    if (entry.getKey().equals("toPrice")) {
                        if (searchProductDTO.getToPrice() > 0) {
                            sql.append("and p.price <= ? ");
                            statusProducSearch.put("toPrice", Boolean.TRUE);
                        }
                    }
                    if (entry.getKey().equals("idCategory")) {
                        if (null != searchProductDTO.getIdCategory() && !searchProductDTO.getIdCategory().isEmpty()) {
                            sql.append("and p.categoryId = ? ");
                            statusProducSearch.put("idCategory", Boolean.TRUE);
                        }
                    }
                }
                ps = cn.prepareStatement(sql.toString());
                int count = 1;
                for (Map.Entry<String, Boolean> entry : statusProducSearch.entrySet()) {
                    if (entry.getKey().equals("name") && entry.getValue()) {
                        ps.setNString(count, "%" + searchProductDTO.getName() + "%");
                        count++;
                    }
                    if (entry.getKey().equals("fromPrice") && entry.getValue()) {
                        ps.setObject(count, searchProductDTO.getFromPrice());
                        count++;
                    }
                    if (entry.getKey().equals("toPrice") && entry.getValue()) {
                        ps.setObject(count, searchProductDTO.getToPrice());
                        count++;
                    }
                    if (entry.getKey().equals("idCategory") && entry.getValue()) {
                        ps.setObject(count, searchProductDTO.getIdCategory());
                        count++;
                    }

                }
                rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("numProduct");
                }

            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return 0;
    }

    public LinkedHashMap<String, ProductDTO> searchAdmin(SearchProductDTO searchProductDTO, int num, int currentNum) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select p.id,p.name,p.createDate,p.status,p.quantity,p.description,p.price,p.categoryId,img.sourceAddress,img.id as idImage, ca.name as categoryName from tblProducts p, Images img, tblCategories ca where ");
        sql.append(" p.id=img.productId and p.categoryId=ca.id ");
        HashMap<String, Boolean> statusProducSearch = new HashMap<>();
        statusProducSearch.put("name", Boolean.FALSE);
        statusProducSearch.put("fromPrice", Boolean.FALSE);
        statusProducSearch.put("toPrice", Boolean.FALSE);
        statusProducSearch.put("idCategory", Boolean.FALSE);
        statusProducSearch.put("name", Boolean.FALSE);
        int offset = 0;
        if (num >= 0) {
            offset = num * SIZE;
        } else {
            if (num == PREVIOUS) {
                offset = (currentNum - 1) * SIZE;
            } else if (num == NEXT) {
                offset = (currentNum + 1) * SIZE;
            }
        }
        try {
            cn = DBUtil.makeConnection();
            if (cn != null) {
                for (Map.Entry<String, Boolean> entry : statusProducSearch.entrySet()) {
                    if (entry.getKey().equals("name")) {
                        if (null != searchProductDTO.getName() && !searchProductDTO.getName().isEmpty()) {
                            sql.append("and p.name like ? ");
                            statusProducSearch.put("name", Boolean.TRUE);
                        }
                    }
                    if (entry.getKey().equals("fromPrice")) {
                        if (searchProductDTO.getFromPrice() > 0) {
                            sql.append("and p.price >= ? ");
                            statusProducSearch.put("fromPrice", Boolean.TRUE);
                        }
                    }
                    if (entry.getKey().equals("toPrice")) {
                        if (searchProductDTO.getToPrice() > 0) {
                            sql.append("and p.price <= ? ");
                            statusProducSearch.put("toPrice", Boolean.TRUE);
                        }
                    }
                    if (entry.getKey().equals("idCategory")) {
                        if (null != searchProductDTO.getIdCategory() && !searchProductDTO.getIdCategory().isEmpty()) {
                            sql.append("and p.categoryId = ? ");
                            statusProducSearch.put("idCategory", Boolean.TRUE);
                        }
                    }
                }
                sql.append(" order by p.createDate DESC ");
                sql.append(" OFFSET ? ROW FETCH NEXT ? ROWS ONLY ");

                ps = cn.prepareStatement(sql.toString());
                int count = 1;
                for (Map.Entry<String, Boolean> entry : statusProducSearch.entrySet()) {
                    if (entry.getKey().equals("name") && entry.getValue()) {
                        ps.setNString(count, "%" + searchProductDTO.getName() + "%");
                        count++;
                    }
                    if (entry.getKey().equals("fromPrice") && entry.getValue()) {
                        ps.setObject(count, searchProductDTO.getFromPrice());
                        count++;
                    }
                    if (entry.getKey().equals("toPrice") && entry.getValue()) {
                        ps.setObject(count, searchProductDTO.getToPrice());
                        count++;
                    }
                    if (entry.getKey().equals("idCategory") && entry.getValue()) {
                        ps.setObject(count, searchProductDTO.getIdCategory());
                        count++;
                    }

                }
               
                ps.setObject(count++, offset);
                ps.setObject(count++, SIZE);
                Logger logger = Logger.getRootLogger();
                BasicConfigurator.configure();
                logger.info(ps.toString());
                rs = ps.executeQuery();
                LinkedHashMap<String, ProductDTO> productDTOs = new LinkedHashMap<>();

                while (rs.next()) {
                    ProductDTO productDTO = new ProductDTO();
                    String id = rs.getString("id");
                    if (productDTOs.containsKey(id)) {
                        productDTO = productDTOs.get(id);
                        ImageDTO imageDTO = new ImageDTO();
                        imageDTO.setId(rs.getString("idImage"));
                        imageDTO.setSourceAddress(rs.getString("sourceAddress"));
                        productDTO.getImages().add(imageDTO);
                    } else {
                        ImageDTO imageDTO = new ImageDTO();
                        imageDTO.setId(rs.getString("idImage"));
                        imageDTO.setSourceAddress(rs.getString("sourceAddress"));
                        productDTO.getImages().add(imageDTO);
                        productDTO.setId(rs.getString("id"));
                        productDTO.setName(rs.getString("name"));
                        productDTO.setCreateDate(rs.getTimestamp("createDate"));
                        productDTO.setStatus(rs.getString("status"));
                        productDTO.setQuantity(rs.getInt("quantity"));
                        productDTO.setCategoryName(rs.getString("categoryName"));
                        productDTO.setDescription(rs.getString("description"));
                        productDTO.setPrice(rs.getFloat("price"));
                        productDTO.setCategoryId(rs.getString("categoryId"));
                        productDTOs.put(id, productDTO);
                    }
                }

                return productDTOs;
            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return null;

    }

    public LinkedHashMap<String, ProductDTO> search(SearchProductDTO searchProductDTO, int num, int currentNum) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select p.id,p.name,p.createDate,p.status,p.quantity,p.description,p.price,p.categoryId,img.sourceAddress,img.id as idImage, ca.name as categoryName from tblProducts p, Images img, tblCategories ca where ");
        sql.append("p.status = 1 and p.quantity >0 and p.id=img.productId and p.categoryId=ca.id ");
        HashMap<String, Boolean> statusProducSearch = new HashMap<>();
        statusProducSearch.put("name", Boolean.FALSE);
        statusProducSearch.put("fromPrice", Boolean.FALSE);
        statusProducSearch.put("toPrice", Boolean.FALSE);
        statusProducSearch.put("idCategory", Boolean.FALSE);
        statusProducSearch.put("name", Boolean.FALSE);
        int offset = 0;
        if (num >= 0) {
            offset = num * SIZE;
        } else {
            if (num == PREVIOUS) {
                offset = (currentNum - 1) * SIZE;
            } else if (num == NEXT) {
                offset = (currentNum + 1) * SIZE;
            }
        }
        try {
            cn = DBUtil.makeConnection();
            if (cn != null) {
                for (Map.Entry<String, Boolean> entry : statusProducSearch.entrySet()) {
                    if (entry.getKey().equals("name")) {
                        if (null != searchProductDTO.getName() && !searchProductDTO.getName().isEmpty()) {
                            sql.append("and p.name like ? ");
                            statusProducSearch.put("name", Boolean.TRUE);
                        }
                    }
                    if (entry.getKey().equals("fromPrice")) {
                        if (searchProductDTO.getFromPrice() > 0) {
                            sql.append("and p.price >= ? ");
                            statusProducSearch.put("fromPrice", Boolean.TRUE);
                        }
                    }
                    if (entry.getKey().equals("toPrice")) {
                        if (searchProductDTO.getToPrice() > 0) {
                            sql.append("and p.price <= ? ");
                            statusProducSearch.put("toPrice", Boolean.TRUE);
                        }
                    }
                    if (entry.getKey().equals("idCategory")) {
                        if (null != searchProductDTO.getIdCategory() && !searchProductDTO.getIdCategory().isEmpty()) {
                            sql.append("and p.categoryId = ? ");
                            statusProducSearch.put("idCategory", Boolean.TRUE);
                        }
                    }
                }
                sql.append(" order by p.createDate DESC ");
                sql.append(" OFFSET ? ROW FETCH NEXT ? ROWS ONLY ");

                ps = cn.prepareStatement(sql.toString());
                int count = 1;
                for (Map.Entry<String, Boolean> entry : statusProducSearch.entrySet()) {
                    if (entry.getKey().equals("name") && entry.getValue()) {
                        ps.setNString(count, "%" + searchProductDTO.getName() + "%");
                        count++;
                    }
                    if (entry.getKey().equals("fromPrice") && entry.getValue()) {
                        ps.setObject(count, searchProductDTO.getFromPrice());
                        count++;
                    }
                    if (entry.getKey().equals("toPrice") && entry.getValue()) {
                        ps.setObject(count, searchProductDTO.getToPrice());
                        count++;
                    }
                    if (entry.getKey().equals("idCategory") && entry.getValue()) {
                        ps.setObject(count, searchProductDTO.getIdCategory());
                        count++;
                    }

                }
                ps.setObject(count++, offset);
                ps.setObject(count++, SIZE);
                Logger logger = Logger.getRootLogger();
                BasicConfigurator.configure();
                logger.info(ps.toString());
                rs = ps.executeQuery();
                LinkedHashMap<String, ProductDTO> productDTOs = new LinkedHashMap<>();

                while (rs.next()) {
                    ProductDTO productDTO = new ProductDTO();
                    String id = rs.getString("id");
                    if (productDTOs.containsKey(id)) {
                        productDTO = productDTOs.get(id);
                        ImageDTO imageDTO = new ImageDTO();
                        imageDTO.setId(rs.getString("idImage"));
                        imageDTO.setSourceAddress(rs.getString("sourceAddress"));
                        productDTO.getImages().add(imageDTO);
                    } else {
                        ImageDTO imageDTO = new ImageDTO();
                        imageDTO.setId(rs.getString("idImage"));
                        imageDTO.setSourceAddress(rs.getString("sourceAddress"));
                        productDTO.getImages().add(imageDTO);
                        productDTO.setId(rs.getString("id"));
                        productDTO.setName(rs.getString("name"));
                        productDTO.setCreateDate(rs.getTimestamp("createDate"));
                        productDTO.setStatus(rs.getString("status"));
                        productDTO.setQuantity(rs.getInt("quantity"));
                        productDTO.setCategoryName(rs.getString("categoryName"));
                        productDTO.setDescription(rs.getString("description"));
                        productDTO.setPrice(rs.getFloat("price"));
                        productDTO.setCategoryId(rs.getString("categoryId"));
                        productDTOs.put(id, productDTO);
                    }
                }

                return productDTOs;
            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return null;

    }

    public boolean addLogRemove(String [] arrayRemove,String userId,String content) throws Exception {
        String sqlLog = "insert into tblLogProduct(id,userId,productId,updateDate,content) values(?,?,?,?,?)";
        try {
            cn = DBUtil.makeConnection();
            cn.setAutoCommit(false);
            if (cn != null) {
                ps = cn.prepareStatement(sqlLog);
                for (String s : arrayRemove) {
                    ps.setObject(1,UUID.randomUUID().toString());
                    ps.setObject(2, userId);
                    ps.setObject(3, s);
                    ps.setObject(4,new Timestamp(Calendar.getInstance().getTimeInMillis()));
                    ps.setObject(5, content);
                    ps.addBatch();
                }
                int count[] = ps.executeBatch();
                if (count.length == arrayRemove.length) {
                    cn.commit();
                    return true;
                }
            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return false;
    }

    public boolean remove(String[] arrayRemove) throws Exception {
        String sql = "update tblProducts set status=0 where id = ? ";

        try {
            cn = DBUtil.makeConnection();
            cn.setAutoCommit(false);
            ps = cn.prepareStatement(sql);
            if (cn != null) {
                for (String s : arrayRemove) {
                    ps.setObject(1, s);
                    ps.addBatch();
                }
                int count[] = ps.executeBatch();

                if (count.length == arrayRemove.length) {
                    cn.commit();
                    return true;
                }
            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return false;
    }

    public List<ProductDTO> getListProductByCart(Map<String, ProductDTO> items) throws Exception {
        List<ProductDTO> productList = null;
        try {
            String sql = "select id, quantity from tblProducts  where id = ?";

            cn = DBUtil.makeConnection();
            if (null != cn) {
                if (items != null && items.size() > 0) {
                    for (String itemKey : items.keySet()) {
                        ps = cn.prepareStatement(sql);
                        ps.setString(1, itemKey);
                        rs = ps.executeQuery();

                        if (rs.next()) {
                            if (null == productList) {
                                productList = new ArrayList<>();
                            }
                            ProductDTO dto = new ProductDTO();
                            dto.setId(rs.getString("id"));
                            dto.setQuantity(rs.getInt("quantity"));
                            productList.add(dto);
                        }
                    }
                }
            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return productList;
    }

    public LinkedHashMap<String, ProductDTO> getProductPurchaseTogether(String idProduct) throws Exception {
        String sql = "select p.id, p.name,p.price,p.quantity,img.sourceAddress,img.id as idImage from "
                + "tblOrderDetails od,tblProducts p,Images img,tblOrders o "
                + "where od.orderId in (select od.orderId "
                + "from tblOrderDetails od,tblProducts p "
                + "where od.productId= ? and p.id=od.productId) "
                + "and p.id=od.productId and od.productId != ? and img.productId=p.id and o.id=od.orderId and p.status=1 and p.quantity >0 "
                + "group by p.id, p.name,p.price,p.quantity,img.sourceAddress,img.id,o.createDate "
                + "order by o.createDate DESC "
                + "OFFSET 0 ROW FETCH NEXT 5 ROWS ONLY";
        LinkedHashMap<String, ProductDTO> productDTOs = new LinkedHashMap<>();

        try {
            cn = DBUtil.makeConnection();
            if (cn != null) {
                ps = cn.prepareStatement(sql);
                ps.setObject(1, idProduct);
                ps.setObject(2, idProduct);
                rs = ps.executeQuery();

                while (rs.next()) {
                    ProductDTO productDTO = new ProductDTO();
                    String id = rs.getString("id");
                    if (productDTOs.containsKey(id)) {
                        productDTO = productDTOs.get(id);
                        ImageDTO imageDTO = new ImageDTO();
                        imageDTO.setId(rs.getString("idImage"));
                        imageDTO.setSourceAddress(rs.getString("sourceAddress"));
                        productDTO.getImages().add(imageDTO);
                    } else {
                        ImageDTO imageDTO = new ImageDTO();
                        imageDTO.setId(rs.getString("idImage"));
                        imageDTO.setSourceAddress(rs.getString("sourceAddress"));
                        productDTO.getImages().add(imageDTO);
                        productDTO.setId(rs.getString("id"));
                        productDTO.setName(rs.getString("name"));
                        productDTO.setQuantity(rs.getInt("quantity"));
                        productDTO.setPrice(rs.getFloat("price"));
                        productDTOs.put(id, productDTO);
                    }
                }
                return productDTOs;

            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return null;
    }

    public ProductDTO findByIdWithImage(String productId) throws Exception {
        String sql = "select p.id, p.name,p.price,p.quantity,p.description,img.sourceAddress,img.id as idImage from tblProducts p,Images img where "
                + "p.id= ? and p.id=img.productId";
        try {
            cn = DBUtil.makeConnection();

            if (cn != null) {
                ps = cn.prepareStatement(sql);
                ps.setObject(1, productId);
                rs = ps.executeQuery();
                if (rs.next()) {
                    ProductDTO productDTO = new ProductDTO();
                    ImageDTO imageDTO = new ImageDTO();
                    imageDTO.setId(rs.getString("idImage"));
                    imageDTO.setSourceAddress(rs.getString("sourceAddress"));
                    productDTO.getImages().add(imageDTO);
                    productDTO.setId(rs.getString("id"));
                    productDTO.setName(rs.getString("name"));
                    productDTO.setQuantity(rs.getInt("quantity"));
                    productDTO.setDescription(rs.getString("description"));
                    productDTO.setPrice(rs.getFloat("price"));
                    return productDTO;
                }

            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return null;
    }

    public boolean updateActive(String id,LogProductDTO logProductDTO) throws Exception {
        String sql = "update tblProducts set status = 1 where id = ?";
        String sqlLog="insert into tblLogProduct(id,userId,productId,updateDate,content) values(?,?,?,?,?)";
        try {
            cn = DBUtil.makeConnection();
            cn.setAutoCommit(false);
            if (cn != null) {
                ps = cn.prepareStatement(sql);
                ps.setObject(1, id);
                ps.executeUpdate();
                ps=cn.prepareStatement(sqlLog);
                ps.setObject(1,logProductDTO.getId());
                ps.setObject(2,logProductDTO.getUserId());
                ps.setObject(3,logProductDTO.getProductId());
                ps.setObject(4,new Timestamp(logProductDTO.getUpdateDate().getTime()));
                ps.setObject(5, logProductDTO.getContent());
                ps.executeUpdate();
                cn.commit();
                return true;
            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return false;
    }

    public int countSearchAdmin(SearchProductDTO searchProductDTO, int num, int currentNum) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select Count(p.id) as numProduct  from tblProducts p, Images img, tblCategories ca where ");
        sql.append(" p.id=img.productId and p.categoryId=ca.id ");
        HashMap<String, Boolean> statusProducSearch = new HashMap<>();
        statusProducSearch.put("name", Boolean.FALSE);
        statusProducSearch.put("fromPrice", Boolean.FALSE);
        statusProducSearch.put("toPrice", Boolean.FALSE);
        statusProducSearch.put("idCategory", Boolean.FALSE);
        statusProducSearch.put("name", Boolean.FALSE);

        try {
            cn = DBUtil.makeConnection();
            if (cn != null) {
                for (Map.Entry<String, Boolean> entry : statusProducSearch.entrySet()) {
                    if (entry.getKey().equals("name")) {
                        if (null != searchProductDTO.getName() && !searchProductDTO.getName().isEmpty()) {
                            sql.append("and p.name like ? ");
                            statusProducSearch.put("name", Boolean.TRUE);
                        }
                    }
                    if (entry.getKey().equals("fromPrice")) {
                        if (searchProductDTO.getFromPrice() > 0) {
                            sql.append("and p.price >= ? ");
                            statusProducSearch.put("fromPrice", Boolean.TRUE);
                        }
                    }
                    if (entry.getKey().equals("toPrice")) {
                        if (searchProductDTO.getToPrice() > 0) {
                            sql.append("and p.price <= ? ");
                            statusProducSearch.put("toPrice", Boolean.TRUE);
                        }
                    }
                    if (entry.getKey().equals("idCategory")) {
                        if (null != searchProductDTO.getIdCategory() && !searchProductDTO.getIdCategory().isEmpty()) {
                            sql.append("and p.categoryId = ? ");
                            statusProducSearch.put("idCategory", Boolean.TRUE);
                        }
                    }
                }
                ps = cn.prepareStatement(sql.toString());
                int count = 1;
                for (Map.Entry<String, Boolean> entry : statusProducSearch.entrySet()) {
                    if (entry.getKey().equals("name") && entry.getValue()) {
                        ps.setNString(count, "%" + searchProductDTO.getName() + "%");
                        count++;
                    }
                    if (entry.getKey().equals("fromPrice") && entry.getValue()) {
                        ps.setObject(count, searchProductDTO.getFromPrice());
                        count++;
                    }
                    if (entry.getKey().equals("toPrice") && entry.getValue()) {
                        ps.setObject(count, searchProductDTO.getToPrice());
                        count++;
                    }
                    if (entry.getKey().equals("idCategory") && entry.getValue()) {
                        ps.setObject(count, searchProductDTO.getIdCategory());
                        count++;
                    }

                }
                rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("numProduct");
                }

            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return 0;
    }
    public List<String> getAllName()throws Exception{
        String sql="select name from tblProducts where status=1 and quantity > 0 ";
        List<String> result= new ArrayList<>();
        try{
            cn=DBUtil.makeConnection();
            if(cn!=null){
                ps=cn.prepareStatement(sql);
                rs=ps.executeQuery();
                while(rs.next()){
                    result.add(rs.getString("name"));
                }
                return result;
            }
            
        }finally{
            DBUtil.closeConnection(cn, ps, rs);
        }
         return null;
    }
   
}
