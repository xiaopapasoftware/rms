package com.thinkgem.jeesite.modules.fee;

import com.thinkgem.jeesite.common.RespConstants;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wangganggang
 * @date 2017年09月28日 下午11:05
 */
public class FeeBaseController extends BaseController {

    @ResponseBody
    @ExceptionHandler({RuntimeException.class})
    public Object runtimeException(RuntimeException e){
        return ResponseData.failure(RespConstants.ERROR_CODE_101).message(e.getMessage());
    }
}
