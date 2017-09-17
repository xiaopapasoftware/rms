<#include "copyright.ftl"/>
package ${basePackage}.${moduleName}.mapper;


import com.xqsight.common.core.dao.Dao;

import ${basePackage}.${moduleName}.model.${table.className};



/**
 * <p>${table.remarks}实现类service</p>
 * <p>Table: ${table.tableName} - ${table.remarks}</p>
 * @since ${.now}
 * @author wangganggang
*/
public interface ${table.className}Dao extends Dao<${table.className},Long>{
}