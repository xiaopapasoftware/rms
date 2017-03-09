package com.thinkgem.jeesite.modules.report.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.supcan.annotation.treelist.SupTreeList;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.xml.bind.annotation.XmlTransient;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangganggang on 17/2/26.
 */
@SupTreeList
public class ReportEntity extends HashMap{
    /**
     * 当前实体分页对象
     */
    protected Page page;

    /**
     * 自定义SQL（SQL标识，SQL内容）
     */
    protected Map<String, String> sqlMap;

    @JsonIgnore
    @XmlTransient
    public Page getPage() {
        if (page == null){
            page = new Page();
        }
        return page;
    }

    public Page setPage(Page page) {
        this.page = page;
        return page;
    }
    @JsonIgnore
    @XmlTransient
    public Map<String, String> getSqlMap() {
        if (sqlMap == null){
            sqlMap = Maps.newHashMap();
        }
        return sqlMap;
    }

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }

    /**
     * 全局变量对象
     */
    @JsonIgnore
    public Global getGlobal() {
        return Global.getInstance();
    }

    /**
     * 获取数据库名称
     */
    @JsonIgnore
    public String getDbName(){
        return Global.getConfig("jdbc.type");
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    /**
     * 删除标记（0：正常；1：删除；2：审核；）
     */
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETE = "1";
    public static final String DEL_FLAG_AUDIT = "2";

}
