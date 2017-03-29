package com.thinkgem.jeesite.modules.file.web;

import com.thinkgem.jeesite.common.filter.search.MatchType;
import com.thinkgem.jeesite.common.filter.search.PropertyFilter;
import com.thinkgem.jeesite.common.filter.search.PropertyType;
import com.thinkgem.jeesite.common.filter.search.builder.PropertyFilterBuilder;
import com.thinkgem.jeesite.common.support.MessageSupport;
import com.thinkgem.jeesite.modules.file.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author wangganggang
 * @date 2017/03/26
 */
@Controller
@RequestMapping(value = "${adminPath}/file")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @RequestMapping("/index")
    public String redirectIndex() {
        return "modules/file/cropperUpload";
    }

    @RequestMapping("/upload")
    @ResponseBody
    public Object uploadImg(HttpServletRequest request) {
        MultipartFile multipartFile = getMultipartFile(request);
        String filePath = null;
        try {
            filePath = uploadService.uploadFile(multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
            return MessageSupport.failureMsg("上传失败");
        }
        return MessageSupport.successDataMsg(filePath, "上传成功");
    }

    @RequestMapping("/download")
    @ResponseBody
    public Object download(String columnName, String relatedId) {
        List<PropertyFilter> propertyFilters = PropertyFilterBuilder.create().matchTye(MatchType.EQ)
                .propertyType(PropertyType.S).add(columnName, relatedId).end();
        List<String> files = uploadService.queryFile(propertyFilters);
        return MessageSupport.successDataMsg(files, "查询成功");
    }


    private MultipartFile getMultipartFile(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        if (CollectionUtils.isEmpty(fileMap)) {
            throw new IllegalStateException("No upload files found!");
        }
        return fileMap.entrySet().iterator().next().getValue();
    }
}
