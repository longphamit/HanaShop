/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc.dao;

import com.longpc.utils.DBUtil;
import com.longpc.dto.CategoryDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author ASUS
 */
public class CategoryDAO extends BaseDAO {
    public boolean add(CategoryDTO categoryDTO) throws Exception {
        try {
            String sql = "insert into tblCategories(id,name) values(?,?)";
            cn = DBUtil.makeConnection();
            if (cn != null) {
                PreparedStatement ps = cn.prepareStatement(sql);
                ps.setObject(1,categoryDTO.getId());
                ps.setObject(2, categoryDTO.getName());
                int check=ps.executeUpdate();
                if(check>0){
                    return true;
                }
            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return false;
    }
    public List<CategoryDTO> findAll() throws Exception {
        try {
            String sql = "select id,name from tblCategories";
            cn = DBUtil.makeConnection();
            if (cn != null) {
                List<CategoryDTO> result= new ArrayList<>();
                PreparedStatement ps = cn.prepareStatement(sql);
                rs=ps.executeQuery();
                while(rs.next()){
                    CategoryDTO categoryDTO= new CategoryDTO();
                    categoryDTO.setId(rs.getString("id"));
                    categoryDTO.setName(rs.getString("name"));
                    result.add(categoryDTO);
                }
                return result;
            }
        } finally {
            DBUtil.closeConnection(cn, ps, rs);
        }
        return null;
    }
}
