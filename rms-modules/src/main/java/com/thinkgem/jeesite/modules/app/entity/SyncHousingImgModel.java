package com.thinkgem.jeesite.modules.app.entity;

/**
 * 同步房源图片对象
 */
public class SyncHousingImgModel {

    private String file_base;//文件内容字节组Base64处理，一次上传一张

    private String file_type;//文件类型1：图片（支持jpg、png、jpeg、bmp格式） 2：合同（HTML格式）

    private Boolean is_public;//是否公共读写，私密文件使用否，如电子合同

    public String getFile_base() {
        return file_base;
    }

    public void setFile_base(String file_base) {
        this.file_base = file_base;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public Boolean getIs_public() {
        return is_public;
    }

    public void setIs_public(Boolean is_public) {
        this.is_public = is_public;
    }


}
