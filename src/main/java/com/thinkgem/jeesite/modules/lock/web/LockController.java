package com.thinkgem.jeesite.modules.lock.web;

import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by mabindong on 2016/1/7.
 */
@Controller
public class LockController {
    Logger log = LoggerFactory.getLogger(LockController.class);
    @RequestMapping(value = "lock")
    @ResponseBody
    public ResponseData callback(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        log.debug("in lock callback: " + request.getParameter("access_token"));
        log.debug("in lock callback: " + request.getParameter("refresh_token"));
        log.debug("in lock callback: " + request.getParameter("openid"));
        log.debug("in lock callback: " + request.getParameter("expires_in"));
        return data;
    }
}
