<#include "copyright.ftl"/>
package ${basePackage}.${moduleName}.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;

import ${basePackage}.${moduleName}.model.${table.className};
import ${basePackage}.${moduleName}.service.${table.className}Service;

/**
 * <p>${table.remarks}实现类 service</p>
 * <p>Table: ${table.tableName} - ${table.remarks}</p>
 * @since ${.now}
 * @author generator code
 */
@Service
@Transactional(readOnly = true)
public class ${table.className}ServiceImpl extends CrudService<${table.className}Dao, ${table.className}>  implements ${table.className}Service {

}