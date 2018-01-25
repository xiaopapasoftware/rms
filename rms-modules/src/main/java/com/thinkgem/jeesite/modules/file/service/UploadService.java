package com.thinkgem.jeesite.modules.file.service;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.filter.search.Criterion;
import com.thinkgem.jeesite.common.filter.search.PropertyFilter;
import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import com.thinkgem.jeesite.modules.file.dao.UploadDao;
import com.thinkgem.jeesite.modules.file.support.FilesEx;
import com.thinkgem.jeesite.modules.file.support.UploadSupport;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wangganggang
 * @date 2017/03/26
 */
@Service
public class UploadService {

    @Autowired
    private UploadDao uploadDao;

    public String uploadFile(MultipartFile partFile) throws IOException {
        String fileName = partFile.getOriginalFilename();
        String ext = FilenameUtils.getExtension(fileName);
        File temp = FilesEx.getTempFile(ext);
        partFile.transferTo(temp);
        String pathname = UploadSupport.getQuickPathname("images", ext);
        try {
            storeFile(temp, pathname);
        } finally {
            FileUtils.deleteQuietly(temp);
        }
        return pathname;
    }

    public void storeFile(File file, String filename) throws IllegalStateException, IOException {
        String prefix = Global.getInstance().getConfig("file.store.path");
        File dest = new File(UploadSupport.getPath(filename, prefix));
        FileUtils.moveFile(file, dest);
    }

    public List<String> queryFile(List<PropertyFilter> propertyFilters) {
        List<Map> files = uploadDao.queryAttachment(new Criterion(propertyFilters));
        List<String> filePaths = new ArrayList<>();
        files.forEach(map -> {
            String[] paths = StringUtils.split(MapUtils.getString(map, "attachment_path", ""), "|");
            for (String path : paths) {
                filePaths.add(path);
            }
        });
        return filePaths;
    }


}
