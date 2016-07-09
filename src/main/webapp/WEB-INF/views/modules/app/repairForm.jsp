<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>报修记录管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
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
		<li><a href="${ctx}/app/repair/">报修记录列表</a></li>
		<li class="active"><a href="${ctx}/app/repair/form?id=${repair.id}">报修记录修改</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="repair" action="${ctx}/app/repair/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
        <form:hidden path="userId"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">报修人：</label>
			<div class="controls">
				<form:input path="userName" htmlEscape="false" maxlength="20" class="input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">报修电话：</label>
			<div class="controls">
				<form:input path="userMobile" htmlEscape="false" maxlength="20" class="input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">报修联系电话：</label>
			<div class="controls">
				<form:input path="repairMobile" htmlEscape="false" maxlength="20" class="input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">期望维修时间：</label>
			<div class="controls">
				<form:input path="expectRepairTime" htmlEscape="false" maxlength="50" class="input-xlarge " readonly="true"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">报修描述：</label>
			<div class="controls">
				<form:input path="description" htmlEscape="false" maxlength="500" class="input-xlarge " readonly="true"/>
			</div>
		</div>


		<div class="control-group">
			<label class="control-label">管家：</label>
			<div class="controls">
				<form:input path="keeper" htmlEscape="false" maxlength="20" class="input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">管家电话：</label>
			<div class="controls">
				<form:input path="keeperMobile" htmlEscape="false" maxlength="20" class="input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">状态：</label>
			<div class="controls">
				<form:select path="status" class="input-xlarge ">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('repair_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>