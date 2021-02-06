/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc.utils;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author ASUS
 */
public class SessionCollector implements HttpSessionListener {

    private static final Map<String,HttpSession> SESSION = new HashMap<String, HttpSession>();

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        SESSION.put(session.getId(), session);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        SESSION.remove(se.getSession().getId());
    }
    public static HttpSession find(String sessionId){
        return SESSION.get(sessionId);
    }

}
