<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>企业信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#companyName, #idNo, #tellPhone, #companyAdress,#businessAdress,#bankName,#bankAccount").keypress(function(event) {
		        if (event.keyCode == 13) {
		            event.preventDefault();
		        }
		    });
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
					$("#btnSubmit").attr("disabled",true);
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
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
		<li><a href="${ctx}/person/company/">企业信息列表</a></li>
		<li class="active"><a href="${ctx}/person/company/form?id=${company.id}">企业信息<shiro:hasPermission name="person:company:edit">${not empty company.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="person:company:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="company" action="${ctx}/person/company/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}" type="${messageType}"/>
		<div class="control-group">
			<label class="control-label">企业名称：</label>
			<div class="controls">
				<form:input path="companyName" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">企业证件类型：</label>
			<div class="controls">
				<form:select path="idType" class="input-xlarge">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('comp_idtype')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">企业证件号码：</label>
			<div class="controls">
				<form:input path="idNo" htmlEscape="false" maxlength="100" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">企业电话：</label>
			<div class="controls">
				<form:input path="tellPhone" htmlEscape="false" maxlength="100" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">企业注册地址：</label>
			<div class="controls">
				<form:input path="companyAdress" htmlEscape="false" maxlength="100" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">企业营业地址：</label>
			<div class="controls">
				<form:input path="businessAdress" htmlEscape="false" maxlength="100" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开户行名称：</label>
			<div class="controls">
				<form:input path="bankName" htmlEscape="false" maxlength="100" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开户行账号：</label>
			<div class="controls">
				<form:input path="bankAccount" htmlEscape="false" maxlength="100" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="person:company:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>