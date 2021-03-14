package com.chl.community.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {
    public static String getCookie(HttpServletRequest request, String ticket){
        if(request == null || StringUtils.isEmpty(ticket))
            throw new IllegalArgumentException("参数为空");
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie c : cookies)
                if(c.getName().equals(ticket))
                    return c.getValue();
        }
        return null;
    }
}
