<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>房间设备维护管理</title>
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
		<li>
			<a href="${ctx}/inventory/room/">房间信息列表</a>
		</li>
		<li>
			<a href="${ctx}/inventory/room/form?id=${room.id}">房间信息<shiro:hasPermission name="inventory:room:edit">${not empty room.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="inventory:room:edit">查看</shiro:lacksPermission></a>
		</li>
		<li class="active">
			<shiro:hasPermission name="device:roomDevices:edit">
						<a href="${ctx}/device/roomDevices/maintainDevices?roomId=${room.id}">房间设备维护</a>
			</shiro:hasPermission>
			<shiro:lacksPermission name="device:roomDevices:edit">
				<a href="${ctx}/device/roomDevices/viewDevices?roomId=${room.id}">房间设备查看</a>
			</shiro:lacksPermission>
		</li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="roomDevices" action="${ctx}/inventory/room/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}" type="${messageType}"/>
		<div class="control-group">
			<label class="control-label">物业项目：</label>
			<div class="controls">
				<form:input path="projectName" htmlEscape="false" maxlength="100" class="input-xlarge" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">楼宇：</label>
			<div class="controls">
				<form:input path="buildingName" htmlEscape="false" maxlength="100" class="input-xlarge" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房屋号：</label>
			<div class="controls">
				<form:input path="houseNo" htmlEscape="false" maxlength="100" class="input-xlarge" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房间号（0:公共区域）：</label>
			<div class="controls">
				<form:input path="roomNo" htmlEscape="false" maxlength="100" class="input-xlarge" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房间面积（平方米）：</label>
			<div class="controls">
				<form:input path="room.roomSpace" htmlEscape="false" class="input-xlarge number" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">朝向：</label>
			<div class="controls">
				<form:input path="room.orientation" value="${fns:getDictLabels(room.orientation, 'orientation', '')}" htmlEscape="false" maxlength="100" class="input-xlarge" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">附属结构：</label>
			<div class="controls">
				<form:input path="room.structure" value="${fns:getDictLabels(room.structure, 'structure', '')}" htmlEscape="false" maxlength="100" class="input-xlarge" readonly="true"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="device:roomDevices:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>