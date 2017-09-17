<#include "copyright.ftl"/>
package ${basePackage}.${moduleName}.service;

import com.thinkgem.jeesite.common.service.CrudService;
import ${basePackage}.${moduleName}.dao.${table.className}Dao;
import ${basePackage}.${moduleName}.entity.${table.className};
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* <p>${table.remarks}实现类 service</p>
* <p>Table: ${table.tableName} - ${table.remarks}</p>
* @since ${.now}
* @author generator code
*/
@Service
@Transactional(readOnly = true)
public class ${table.className}Service extends CrudService<${table.className}Dao, ${table.className}> {

}