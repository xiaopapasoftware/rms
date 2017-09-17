<#include "copyright.ftl"/>
package ${basePackage}.${moduleName}.web;


import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import ${basePackage}.${moduleName}.entity.${table.className};
import ${basePackage}.${moduleName}.service.${table.className}Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>${table.remarks} controller</p>
 * <p>Table: ${table.tableName} - ${table.remarks}</p>
 * @since ${.now}
 * @author generator code
 */
@RestController
@RequestMapping("${adminPath}/${table.controllerPath}")
public class ${table.className}Controller extends BaseController {

      @Autowired
      private ${table.className}Service ${table.javaProperty}Service;

      @RequestMapping(value = "save")
      public Object save(${table.className} ${table.javaProperty}) {
          ${table.javaProperty}Service.save(${table.javaProperty});
          return ResponseData.success();
      }

     @RequestMapping(value = "list")
     public Object list(${table.className} ${table.javaProperty},@RequestParam(value = "pageSize",defaultValue = "1") Integer pageSize,@RequestParam(value = "pageSize",defaultValue = "15") Integer pageNo) {
         Page<${table.className}> page = ${table.javaProperty}Service.findPage(new Page(pageSize, pageNo), ${table.javaProperty});
         return ResponseData.success().data(page);
     }

    @RequestMapping(value = "delete")
    public Object delete(String id) {
        ${table.className} ${table.javaProperty} = new ${table.className}();
        ${table.javaProperty}.setId(id);
        ${table.javaProperty}.setDelFlag(Constants.DEL_FLAG_NO);
        ${table.javaProperty}Service.save(${table.javaProperty});
        return ResponseData.success();
    }

   @RequestMapping(value = "get")
   public Object get(String id) {
        ${table.className} ${table.javaProperty} = ${table.javaProperty}Service.get(id);
        return ResponseData.success().data(${table.javaProperty});
   }

}