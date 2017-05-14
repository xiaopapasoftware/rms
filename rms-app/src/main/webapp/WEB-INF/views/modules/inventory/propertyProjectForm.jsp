<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>物业项目管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {
            //$("#name").focus();
            $("#inputForm").validate({
                submitHandler: function (form) {
                    loading('正在提交，请稍等...');
                    $("#btnSubmit").attr("disabled", true);
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        });
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li>
        <a href="${ctx}/inventory/propertyProject/">物业项目列表</a>
    </li>
    <li class="active">
        <a href="${ctx}/inventory/propertyProject/form?id=${propertyProject.id}">物业项目<shiro:hasPermission
                name="inventory:propertyProject:edit">${not empty propertyProject.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission
                name="inventory:propertyProject:edit">查看</shiro:lacksPermission></a>
    </li>
</ul>
<br/>
<form:form id="inputForm" modelAttribute="propertyProject" action="${ctx}/inventory/propertyProject/save" method="post"
           class="form-horizontal">
    <form:hidden path="id"/>
    <sys:message content="${message}" type="${messageType}"/>
    <div class="control-group">
        <label class="control-label">居委会：</label>
        <div class="controls">
            <form:select path="neighborhood.id" class="input-xlarge required">
                <form:option value="" label="请选择..."/>
                <form:options items="${listNeighborhood}" itemLabel="neighborhoodName" itemValue="id"
                              htmlEscape="false"/>
            </form:select>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">物业公司：</label>
        <div class="controls">
            <form:select path="managementCompany.id" class="input-xlarge required">
                <form:option value="" label="请选择..."/>
                <form:options items="${listManagementCompany}" itemLabel="companyName" itemValue="id"
                              htmlEscape="false"/>
            </form:select>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">所属区域:</label>
        <div class="controls">
            <sys:treeselect id="area" name="area.id" value="${propertyProject.area.id}" labelName="area.name"
                    labelValue="${propertyProject.area.name}"
                    notAllowSelectParent="true" title="区域" url="/sys/area/treeData"
                    cssClass="required" allowClear="true"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">物业项目名称：</label>
        <div class="controls">
            <form:input path="projectName" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">物业项目拼音首字母：</label>
        <div class="controls">
            <form:input path="projectSimpleName" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">物业项目地址：</label>
        <div class="controls">
            <form:input path="projectAddr" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">物业项目图片：</label>
        <div class="controls">
            <form:hidden id="attachmentPath" path="attachmentPath" htmlEscape="false" maxlength="4000"
                         class="input-xlarge"/>
            <sys:ckfinder input="attachmentPath" type="files" uploadPath="/物业项目图片" selectMultiple="true"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">备注信息：</label>
        <div class="controls">
            <form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
        </div>
    </div>
    <div class="form-actions">
        <shiro:hasPermission name="inventory:propertyProject:edit"><input id="btnSubmit" class="btn btn-primary"
                                                                          type="submit"
                                                                          value="保 存"/>&nbsp;</shiro:hasPermission>
        <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form:form>
</body>
</html>