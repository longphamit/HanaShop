/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author ASUS
 */
public class GoogleUtil {

    public static final String GOOGLE_CLIENT_ID = "378885465668-vfed05jhhfhdi8gad3tfjilu8ehcd4ji.apps.googleusercontent.com";
    public static final String GOOGLE_CLIENT_SECRET = "aZH9BguomKaktlc5xG1I9Iu5";
    public static final String GOOGLE_REDIRECT_LINK = "http://localhost:8080/HanaShop/auth/login/google";
    public static final String GOOGLE_GET_TOKEN = "https://oauth2.googleapis.com/token";
    public static final String GOOGLE_GET_USER_INFO = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";
    public static final String GOOGLE_GRANT_TYPE = "authorization_code";

    public static String getToken(final String code) throws Exception {
        String urlParameters = "code="
                + code
                + "&client_id=" + GOOGLE_CLIENT_ID
                + "&client_secret=" + GOOGLE_CLIENT_SECRET
                + "&redirect_uri=" + GOOGLE_REDIRECT_LINK
                + "&grant_type=" + GOOGLE_GRANT_TYPE;
        URL url = new URL(GOOGLE_GET_TOKEN);
        URLConnection urlConn = url.openConnection();
        urlConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(
                urlConn.getOutputStream());
        writer.write(urlParameters);
        writer.flush();
        BufferedReader br= new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        String line="";
        JSONTokener tokener= new JSONTokener(br);
        JSONObject jSONObject= new JSONObject(tokener);
        System.out.println(jSONObject.toString());
        return jSONObject.get("access_token").toString();
    }
    public static JSONObject getInfoUser(final String accessToken) throws Exception{
        URL url= new URL(GOOGLE_GET_USER_INFO+accessToken);
        URLConnection uRLConnection= url.openConnection();
        uRLConnection.setDoOutput(true);
        BufferedReader br= new BufferedReader(new InputStreamReader(uRLConnection.getInputStream()));
        JSONTokener tokener= new JSONTokener(br);
        JSONObject jSONObject= new JSONObject(tokener);
        return jSONObject;
    }
}
