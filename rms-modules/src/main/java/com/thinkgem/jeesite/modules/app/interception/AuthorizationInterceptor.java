package com.thinkgem.jeesite.modules.app.interception;

import com.thinkgem.jeesite.common.RespConstants;
import com.thinkgem.jeesite.common.exception.AuthcException;
import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.app.annotation.AuthIgnore;
import com.thinkgem.jeesite.modules.app.entity.AppToken;
import com.thinkgem.jeesite.modules.app.entity.AppUser;
import com.thinkgem.jeesite.modules.app.service.AppTokenService;
import com.thinkgem.jeesite.modules.app.service.AppUserService;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wangganggang
 * @date 2017年07月21日
 */
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {


    @Autowired
    private AppTokenService appTokenService;

    @Autowired
    private AppUserService appUserService;

    @Value("${apiPath}")
    private String apiPath;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthIgnore annotation;

        if (!request.getRequestURI().startsWith(apiPath)) {
            return true;
        }

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
            throw new AuthcException(RespConstants.ERROR_CODE_402, RespConstants.ERROR_MSG_402);
        }

        //查询token信息
        AppToken searchToken = new AppToken();
        searchToken.setToken(token);
        AppToken userToken = appTokenService.findByToken(searchToken);
        if (userToken == null || userToken.getExprie().getTime() < System.currentTimeMillis()) {
            throw new AuthcException(RespConstants.ERROR_CODE_402, RespConstants.ERROR_MSG_402);
        }

        AppUser searchAppUser = new AppUser();
        searchAppUser.setPhone(userToken.getPhone());
        AppUser appUser = appUserService.getByPhone(searchAppUser);
        if (appUser == null) {
            throw new AuthcException(RespConstants.ERROR_CODE_403, RespConstants.ERROR_MSG_403);
        }

        request.setAttribute(Constants.APP_USER_TELPHONE, userToken.getPhone());
        request.setAttribute(Constants.APP_CURRENT_USER, appUser);
        return true;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }
}
