<#include "copyright.ftl"/>
package ${basePackage}.${moduleName}.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import ${basePackage}.${moduleName}.entity.${table.className};

/**
 * <p>${table.remarks}实现类service</p>
 * <p>Table: ${table.tableName} - ${table.remarks}</p>
 * @since ${.now}
 * @author generator code
*/
@MyBatisDao
public interface ${table.className}Dao extends CrudDao<${table.className}>{
}