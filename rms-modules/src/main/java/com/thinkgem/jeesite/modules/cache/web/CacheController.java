package com.thinkgem.jeesite.modules.cache.web;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cache.MyCacheBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xiao
 */
@Controller
@RequestMapping(value = "${adminPath}/cache")
public class CacheController extends BaseController {

  @RequestMapping(value = {"soft/clear"})
  @ResponseBody
  public String softClear(HttpServletRequest request, HttpServletResponse response) {
    String id = request.getParameter("id");
    MyCacheBuilder.getInstance().getSoftCache(id).clear();
    return "success";
  }

  @RequestMapping(value = {"scheduled/clear"})
  @ResponseBody
  public String scheduledClear(HttpServletRequest request, HttpServletResponse response) {
    String id = request.getParameter("id");
    MyCacheBuilder.getInstance().getScheduledCache(id).clear();
    return "success";
  }
}
