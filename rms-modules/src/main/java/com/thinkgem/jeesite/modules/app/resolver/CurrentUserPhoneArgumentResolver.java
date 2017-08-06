package com.thinkgem.jeesite.modules.app.resolver;

import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.modules.app.annotation.CurrentUserPhone;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author wangganggang
 * @date 2017/04/01
 */
public class CurrentUserPhoneArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUserPhone.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        //获取用户ID
        Object object = webRequest.getAttribute(Constants.APP_USER_TELPHONE, RequestAttributes.SCOPE_REQUEST);

        if(object == null){
            return null;
        }

        return object;
    }
}
