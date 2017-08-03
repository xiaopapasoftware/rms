package com.thinkgem.jeesite.modules.app.interception;

import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.app.annotation.AuthIgnore;
import com.thinkgem.jeesite.modules.app.entity.AppToken;
import com.thinkgem.jeesite.modules.app.service.AppTokenService;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wangganggang
 * @date 2017年07月21日
 */
//@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private AppTokenService appTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthIgnore annotation;
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(AuthIgnore.class);
        } else {
            return true;
        }

        //如果有@IgnoreAuth注解，则不验证token
        if (annotation != null) {
            return true;
        }

        //从header中获取token
        String token = request.getHeader("token");
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(token)) {
            token = request.getParameter("token");
        }

        //token为空
        if (StringUtils.isBlank(token)) {
            throw new ExpiredCredentialsException( "token失效，请重新登录");
        }

        //查询token信息
        AppToken searchToken = new AppToken();
        searchToken.setToken(token);
        AppToken userToken = appTokenService.findByToken(searchToken);
        if (userToken == null || userToken.getExprie().getTime() < System.currentTimeMillis()) {
            throw new ExpiredCredentialsException( "token失效，请重新登录");
        }

        request.setAttribute("mobile_phone", userToken.getPhone());
        return true;
    }
}
