/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc.dao;

import com.longpc.dto.LogProductDTO;
import com.longpc.utils.DBUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class LogDAO extends BaseDAO{
    public List<LogProductDTO> getLogProducts(String id) throws Exception{
        List<LogProductDTO> result= new ArrayList<>();
        try{
            
            String sql="select a.name,l.updateDate,l.content from tblLogProduct l, tblAccounts a where l.productId= ? and a.userId=l.userId "
                    + "order by l.updateDate DESC";
            cn=DBUtil.makeConnection();
            if(cn!=null){
                ps=cn.prepareStatement(sql);
                ps.setObject(1,id);
                rs=ps.executeQuery();
                while(rs.next()){
                    LogProductDTO logProductDTO= new LogProductDTO();
                    logProductDTO.setUserName(rs.getString("name"));
                    logProductDTO.setUpdateDate(rs.getTimestamp("updateDate"));
                    logProductDTO.setContent(rs.getString("content"));
                    result.add(logProductDTO);
                }
            }
        }finally{
            DBUtil.closeConnection(cn, ps, rs);
        }
        return result;
    }
}
