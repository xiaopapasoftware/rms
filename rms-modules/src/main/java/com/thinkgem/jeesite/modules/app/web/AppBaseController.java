package com.thinkgem.jeesite.modules.app.web;

import com.thinkgem.jeesite.common.RespConstants;
import com.thinkgem.jeesite.common.exception.GlobalException;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wangganggang
 * @date 2017年08月05日 下午2:55
 */
public class AppBaseController {

  Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 统一异常处理
   */
  @ExceptionHandler({GlobalException.class})
  @ResponseBody
  public ResponseData bindException(GlobalException e) {
    logger.error(e.getMessage());
    e.printStackTrace();
    return ResponseData.failure(e.getCode()).message(e.getMessage());
  }


  @ExceptionHandler({Exception.class})
  @ResponseBody
  public ResponseData bindException(Exception e) {
    logger.error(e.getMessage());
    e.printStackTrace();
    return ResponseData.failure(RespConstants.ERROR_CODE_500).message(RespConstants.ERROR_MSG_500);
  }
}
