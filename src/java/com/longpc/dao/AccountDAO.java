/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc.dao;

import com.longpc.utils.DBUtil;
import com.longpc.dto.AccountDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

/**
 *
 * @author ASUS
 */
public class AccountDAO extends BaseDAO {
    public AccountDTO checkLogin(String userId, String password) throws Exception {
        String sql = "select userId,email,role,name from tblAccounts where userId= ? and password = ?";
        try {
            cn=DBUtil.makeConnection();
            if(cn!=null){
                ps=cn.prepareStatement(sql);
                ps.setObject(1, userId);
                ps.setObject(2, password);
                
                rs=ps.executeQuery();
                if(rs.next()){
                    AccountDTO acc= new AccountDTO();
                    acc.setPassword(password);
                    if(null!=rs.getString("userId")){
                        acc.setUserId(rs.getString("userId"));
                    }
                    if(null!=rs.getString("email")){
                        acc.setEmail(rs.getString("email"));
                    }
                    if(null!=rs.getString("role")){
                        acc.setRole(rs.getString("role"));
                    }
                    if(null!=rs.getString("name")){
                        acc.setName(rs.getString("name"));
                    }
                    if(acc.getUserId().equals(userId)&&acc.getPassword().equals(password)){
                        return acc;
                    }
                    return  null;
                }
            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return null;
    }
    
    public AccountDTO checkAccountWithLoginGoogle(String email,String id) throws Exception{
        AccountDTO account=checkExistByEmail(email);
        if(account==null){
            try{
                AccountDTO accountDTO= new AccountDTO();
                accountDTO.setUserId(id);
                accountDTO.setEmail(email);
                String sql="insert into tblAccounts(userId,password,name,role,email) "
                        + "values(?,?,?,?,?)";
                cn=DBUtil.makeConnection();
                if(cn!=null){
                    ps=cn.prepareStatement(sql);
                    ps.setObject(1,accountDTO.getUserId());
                    ps.setObject(2, UUID.randomUUID().toString());
                    ps.setObject(3, accountDTO.getName());
                    ps.setObject(4, accountDTO.getRole());
                    ps.setObject(5, accountDTO.getEmail());
                    ps.executeUpdate();
                    return accountDTO;
                }
            }finally{
                DBUtil.closeConnection(cn, ps, rs);
            }
        }else{
            try{
                String sql="update tblAccounts set email = ? where userId = ?";
                cn=DBUtil.makeConnection();
                if(cn!=null){
                    ps=cn.prepareStatement(sql);
                    ps.setObject(1, email);
                    ps.setObject(2, account.getUserId());
                    ps.executeUpdate();
                    return account;
                }
            }finally{
                DBUtil.closeConnection(cn, ps, rs);
            }
        }
        return null;
        
    }
    public AccountDTO checkExistByEmail(String email) throws Exception{
        String sql="select userId,name,role,email from tblAccounts where email = ? ";
        AccountDTO accountDTO= new AccountDTO();
        
        try{
            cn=DBUtil.makeConnection();
            if(cn!=null){
                ps=cn.prepareStatement(sql);
                ps.setObject(1,email);
                rs=ps.executeQuery();
                if(rs.next()){
                    accountDTO.setEmail(rs.getString("email"));
                    accountDTO.setRole(rs.getString("role"));
                    accountDTO.setUserId(rs.getString("userId"));
                    accountDTO.setName(rs.getString("name"));
                    return accountDTO;
                }
            }
        }finally{
            DBUtil.closeConnection(cn, ps, rs);
        }
        return  null;
    }
    private boolean checkExist(String id) throws Exception{
        String sql="select id from tblAccounts where id= ?";
        try{
            cn=DBUtil.makeConnection();
            if(cn!=null){
                ps=cn.prepareStatement(sql);
                ps.setObject(1, id);
                rs=ps.executeQuery();
                if(rs.next()){
                    return true;
                }
                
            }
        }finally{
            DBUtil.closeConnection(cn, ps, rs);
        }
        return false;
    }
    public boolean add(AccountDTO accountDTO) throws Exception{
        String sql="insert into tblAccounts(userId,password,name,role,email) values(?,?,?,?,?)";
        try{
            cn=DBUtil.makeConnection();
            if(cn!=null){
                ps=cn.prepareStatement(sql);
                ps.setObject(1, accountDTO.getUserId());
                ps.setObject(2, accountDTO.getPassword());
                ps.setObject(3,accountDTO.getName());
                ps.setObject(4, accountDTO.getRole());
                ps.setObject(5,accountDTO.getEmail());
                int check=ps.executeUpdate();
                if(check>0){
                    return true;
                }
                
            }
        }finally{
            DBUtil.closeConnection(cn, ps, rs);
        }
        return false;
    }
}
