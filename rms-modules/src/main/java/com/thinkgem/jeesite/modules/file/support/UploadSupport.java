package com.thinkgem.jeesite.modules.file.support;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangganggang
 * @date 2017/03/28
 */
public class UploadSupport {
    /**
     * 快速上传文件夹
     */
    public static final String QUICK_UPLOAD = "public";

    public static final String SEPARATOR = "/";

    public static String randomPathname(String extension) {
        return randomPathname("/yyyyMM/yyyyMMddHHmmss_", extension);
    }

    public static String randomPathname(String pattern, String extension) {
        StringBuilder filename = new StringBuilder();
        DateFormat df = new SimpleDateFormat(pattern);
        filename.append(df.format(new Date()));
        filename.append(RandomStringUtils.random(10, '0', 'Z', true, true).toLowerCase());
        if (StringUtils.isNotBlank(extension)) {
            filename.append(".").append(extension.toLowerCase());
        }
        return filename.toString();
    }

    public static String getQuickPathname(String type, String extension) {
        StringBuilder name = new StringBuilder();
        name.append('/').append(type);
        name.append('/').append(QUICK_UPLOAD);
        name.append(randomPathname(extension));
        return name.toString();
    }

    public static String getPath(String uri, String prefix) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(prefix)) {
            sb.append(prefix.replace("\\", SEPARATOR));
        }

        if (StringUtils.isBlank(uri)) {
            return sb.toString();
        }
        if (!StringUtils.startsWith(uri, SEPARATOR)) {
            sb.append(SEPARATOR);
        }
        sb.append(uri.replace("\\", SEPARATOR));
        return sb.toString();
    }
}
