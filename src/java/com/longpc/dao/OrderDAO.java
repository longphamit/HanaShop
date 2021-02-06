/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc.dao;

import com.longpc.dto.AccountDTO;
import com.longpc.dto.CartDTO;
import com.longpc.dto.ImageDTO;
import com.longpc.dto.OrderDTO;
import com.longpc.dto.ProductDTO;
import com.longpc.utils.DBUtil;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class OrderDAO extends BaseDAO {

    public boolean addOrder(CartDTO cartDTO, OrderDTO orderDTO, AccountDTO accountDTO, List<ProductDTO> listProduct) throws Exception {
        try {
            String sqlCreateOrder = "insert into tblOrders(id,createDate,userId,total) values(?,?,?,?)";
            String sqlCreateOrderDetail = "insert into tblOrderDetails(orderId,productId,quantity,price) values(?,?,?,?)";
            String sqlUpdateQuantity = "update tblProducts set quantity = ? where id = ?";
            cn = DBUtil.makeConnection();
            if (cn != null) {
                cn.setAutoCommit(false);
                ps = cn.prepareStatement(sqlCreateOrder);
                ps.setObject(1, orderDTO.getOrderId());
                ps.setObject(2, new Timestamp(orderDTO.getCreateDate().getTime()));
                ps.setObject(3, accountDTO.getUserId());
                ps.setObject(4, cartDTO.total());
                ps.execute();
                ps = cn.prepareStatement(sqlCreateOrderDetail);
                for (ProductDTO productDTO : cartDTO.getCart().values()) {
                    ps.setObject(1, orderDTO.getOrderId());
                    ps.setObject(2, productDTO.getId());
                    ps.setObject(3, productDTO.getQuantityInCart());
                    ps.setObject(4, productDTO.getPrice());
                    ps.addBatch();

                }
                ps.executeBatch();
                ps = cn.prepareStatement(sqlUpdateQuantity);
                for (int i = 0; i < cartDTO.getCart().values().size(); i++) {
                    ps.setObject(1, listProduct.get(i).getQuantity() - cartDTO.getCart().get(listProduct.get(i).getId()).getQuantityInCart());
                    ps.setObject(2, listProduct.get(i).getId());
                    ps.addBatch();
                }
                ps.executeBatch();
                cn.commit();
            }
            return true;
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
    }

    public List<OrderDTO> getByUserId(String userId) throws Exception {
        String sql = "select id,createDate,total from tblOrders where userId= ? order by createDate DESC";
        try {
            cn = DBUtil.makeConnection();
            if (cn != null) {
                ps = cn.prepareStatement(sql);
                ps.setObject(1, userId);
                rs = ps.executeQuery();
                List<OrderDTO> listOrders = new ArrayList<>();
                while (rs.next()) {
                    OrderDTO orderDTO = new OrderDTO();
                    orderDTO.setOrderId(rs.getString("id"));
                    orderDTO.setCreateDate(rs.getTimestamp("createDate"));
                    orderDTO.setTotal(rs.getFloat("total"));
                    listOrders.add(orderDTO);
                }

                return listOrders;
            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return null;
    }

    public List<OrderDTO> findAll() throws Exception {
        String sql = "select id,createDate,total from tblOrders order by createDate DESC";
        try {
            cn = DBUtil.makeConnection();
            if (cn != null) {
                ps = cn.prepareStatement(sql);
                rs = ps.executeQuery();
                List<OrderDTO> listOrders = new ArrayList<>();
                while (rs.next()) {
                    OrderDTO orderDTO = new OrderDTO();
                    orderDTO.setOrderId(rs.getString("id"));
                    orderDTO.setCreateDate(rs.getTimestamp("createDate"));
                    orderDTO.setTotal(rs.getFloat("total"));
                    listOrders.add(orderDTO);
                }
                return listOrders;
            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return null;
    }

    public OrderDTO getOrderDetail(String orderId) throws Exception {
        String sql = "select od.quantity as quantityCart,o.total,p.id,p.name,p.createDate,p.description,od.price,p.categoryId "
                + "from tblProducts p,tblOrders o,tblOrderDetails od where "
                + "p.id=od.productId and od.orderId=o.id and o.id= ? "
                + "order by p.createDate DESC";
        try {
            cn = DBUtil.makeConnection();
            if (cn != null) {
                OrderDTO orderDTO = new OrderDTO();
                ps = cn.prepareStatement(sql);
                ps.setObject(1, orderId);
                rs = ps.executeQuery();
                if(rs.next()){
                    ProductDTO productDTO = new ProductDTO();
                    String id = rs.getString("id");
                    productDTO.setId(id);
                    productDTO.setName(rs.getString("name"));
                    productDTO.setDescription(rs.getString("description"));
                    productDTO.setPrice(rs.getFloat("price"));
                    productDTO.setCategoryId(rs.getString("categoryId"));
                    productDTO.setQuantityInCart(Integer.parseInt(rs.getString("quantityCart")));
                    orderDTO.getListProducts().put(id, productDTO);
                    orderDTO.setTotal(Float.parseFloat(rs.getString("total")));
                }
                while (rs.next()) {
                    ProductDTO productDTO = new ProductDTO();
                    String id = rs.getString("id");
                    productDTO.setId(id);
                    productDTO.setName(rs.getString("name"));
                    productDTO.setDescription(rs.getString("description"));
                    productDTO.setPrice(rs.getFloat("price"));
                    productDTO.setCategoryId(rs.getString("categoryId"));
                    productDTO.setQuantityInCart(Integer.parseInt(rs.getString("quantityCart")));
                    orderDTO.getListProducts().put(id, productDTO);
                }
                return orderDTO;
            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return null;
    }

    public List<OrderDTO> getByUserIdAndDate(String userId, String fromDate, String toDate) throws Exception {
        StringBuilder sql = new StringBuilder("select id,createDate,total from tblOrders where userId= ? ");
        if (!fromDate.isEmpty()) {
            sql.append("and createDate > ");
            sql.append("'");
            sql.append(fromDate);
            sql.append("'");
            sql.append(" ");
        }
        if (!toDate.isEmpty()) {
            sql.append("and createDate < ");
            sql.append("'");
            sql.append(toDate);
            sql.append("'");
            sql.append(" ");
        }

        try {
            cn = DBUtil.makeConnection();
            if (cn != null) {
                ps = cn.prepareStatement(sql.toString());
                ps.setObject(1, userId);
                rs = ps.executeQuery();
                List<OrderDTO> listOrders = new ArrayList<>();
                while (rs.next()) {
                    OrderDTO orderDTO = new OrderDTO();
                    orderDTO.setOrderId(rs.getString("id"));
                    orderDTO.setCreateDate(rs.getTimestamp("createDate"));
                    orderDTO.setTotal(rs.getFloat("total"));
                    listOrders.add(orderDTO);
                }

                return listOrders;
            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return null;
    }

    public List<OrderDTO> getByDate(String fromDate, String toDate, String nameAccount) throws Exception {

        StringBuilder sql = new StringBuilder("select o.id,o.createDate,o.total from tblOrders o,tblAccounts a where o.userId=a.userId ");
        if (!nameAccount.isEmpty()) {
            sql.append("and name like N'%");
            sql.append(nameAccount);
            sql.append("%' ");
        }
        if (!fromDate.isEmpty()) {
            sql.append("and createDate > ");
            sql.append("'");
            sql.append(fromDate);
            sql.append("'");
            sql.append(" ");
        }
        if (!toDate.isEmpty()) {
            sql.append("and createDate < ");
            sql.append("'");
            sql.append(toDate);
            sql.append("'");
            sql.append(" ");
        }

        try {
            cn = DBUtil.makeConnection();
            if (cn != null) {
                ps = cn.prepareStatement(sql.toString());
                rs = ps.executeQuery();
                List<OrderDTO> listOrders = new ArrayList<>();
                while (rs.next()) {
                    OrderDTO orderDTO = new OrderDTO();
                    orderDTO.setOrderId(rs.getString("id"));
                    orderDTO.setCreateDate(rs.getTimestamp("createDate"));
                    orderDTO.setTotal(rs.getFloat("total"));
                    listOrders.add(orderDTO);
                }
                return listOrders;
            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return null;
    }
}
