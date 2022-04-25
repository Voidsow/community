package com.voidsow.community.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voidsow.community.entity.User;
import com.voidsow.community.utils.HostHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Map;

@Component
public class UserInterceptor implements HandlerInterceptor {
    Key key;
    HostHolder hostHolder;
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public UserInterceptor(Key key, HostHolder hostHolder) {
        this.key = key;
        this.hostHolder = hostHolder;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (var cookie : cookies)
                if (cookie.getName().equals("token")) {
                    try {
                        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(cookie.getValue());
                        hostHolder.user.set(objectMapper.convertValue(claimsJws.getBody().get("user", Map.class), User.class));
                    } catch (SignatureException e) {
                        cookie.setMaxAge(0);
                        break;
                    }
                    break;
                }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && hostHolder.user.get() != null)
            modelAndView.addObject("user", hostHolder.user.get());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.user.remove();
    }
}
