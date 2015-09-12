<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>工作记录管理</title>
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
		<li><a href="${ctx}/company/workRecord/">工作记录列表</a></li>
		<li class="active">
			<a href="${ctx}/company/workRecord/form?id=${workRecord.id}">工作记录
			<shiro:hasPermission name="company:workRecord:edit">
				<c:if test="${workRecord.recordStatus=='2'}">
				查看
				</c:if>
				<c:if test="${workRecord.recordStatus!='2'}">
				${not empty workRecord.id?'修改':'添加'}
				</c:if>
			</shiro:hasPermission>
			<shiro:lacksPermission name="company:workRecord:edit">查看</shiro:lacksPermission></a>
		</li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="workRecord" action="${ctx}/company/workRecord/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">记录类型：</label>
			<div class="controls">
				<form:select path="recordType" class="input-xlarge ">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('work_record_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">记录标题：</label>
			<div class="controls">
				<form:input path="recordTitle" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">记录内容：</label>
			<div class="controls">
				<form:textarea path="recordContent" htmlEscape="false" rows="4" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="company:workRecord:edit">
			<c:if test="${workRecord.recordStatus!='2'}">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			</c:if>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>