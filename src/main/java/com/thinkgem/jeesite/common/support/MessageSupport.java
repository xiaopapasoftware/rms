package com.thinkgem.jeesite.common.support;

import java.util.HashMap;
import java.util.Map;

import com.github.pagehelper.Page;

/**
 * Created by wangganggang on 2017/2/28.
 */
public class MessageSupport {
  private final static int SUCCESS = 0;

  private final static int FAILURE = -1;

  private static final String KEY_STATUS = "status";

  private static final String KEY_MESSAGE = "msg";

  private static final String KEY_DATA = "data";

  private static final String KEY_TOTAL_PAGE = "totalPage";
  private static final String KEY_PAGE_SIZE = "pageSize";
  private static final String KEY_PAGE_NUM = "pageNum";

  public static Object successDataMsg(Object dataObj, String message) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(KEY_STATUS, SUCCESS);
    map.put(KEY_DATA, dataObj);
    map.put(KEY_MESSAGE, message);
    return responseBody(map);
  }

  public static Object failureMsg(String message) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(KEY_STATUS, FAILURE);
    map.put(KEY_MESSAGE, message);
    return responseBody(map);
  }

  public static Object successDataTableMsg(Page page, Object object) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(KEY_STATUS, SUCCESS);
    map.put(KEY_DATA, object);
    map.put(KEY_TOTAL_PAGE, page.getPages());
    map.put(KEY_PAGE_SIZE, page.getPageSize());
    map.put(KEY_PAGE_NUM, page.getPageNum());
    return responseBody(map);
  }

  private static Object responseBody(Object objectData) {
    return objectData;
  }
}
