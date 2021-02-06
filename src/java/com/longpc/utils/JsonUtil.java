/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc.utils;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author ASUS
 */
public class JsonUtil {
    public static void writeError(String content,HttpServletResponse response) throws Exception {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("Error", "Lỗi lấy thông tin");
        PrintWriter out = response.getWriter();
        out.write(jSONObject.toString());
        out.flush();
    }
}
