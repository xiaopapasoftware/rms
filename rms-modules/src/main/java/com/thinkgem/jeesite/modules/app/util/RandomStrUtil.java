package com.thinkgem.jeesite.modules.app.util;

public class RandomStrUtil {

  public static void main(String[] args) {
    System.out.println(generateCode(true, 12));
  }

  /**
   * 创建指定数量的随机字符串
   * 
   * @param numberFlag 是否是数字
   * @param length
   * @return
   */
  public static String generateCode(boolean numberFlag, int length) {
    String retStr = "";
    String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
    int len = strTable.length();
    boolean bDone = true;
    do {
      retStr = "";
      int count = 0;
      for (int i = 0; i < length; i++) {
        double dblR = Math.random() * len;
        int intR = (int) Math.floor(dblR);
        char c = strTable.charAt(intR);
        if (('0' <= c) && (c <= '9')) {
          count++;
        }
        retStr += strTable.charAt(intR);
      }
      if (count >= 2) {
        bDone = false;
      }
    } while (bDone);

    return retStr;
  }

}
