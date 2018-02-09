<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>公共事业费修改</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#meterValue, #totalMeterValue, #peakMeterValue, #valleyMeterValue, #coalValue, #waterValue").keypress(function(event) {
		        if (event.keyCode == 13) {
		            event.preventDefault();
		        }
		    });
            top.$.jBox.tip.mess = null;
		});
		function submitData() {
			$("#inputForm").validate({
				submitHandler: function(form){//单间
					$("#saveBtn").attr("disabled",true);
					$("#btnSubmit").attr("disabled",true);
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
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active">
			<a href="${ctx}/contract/rentContract/form?id=${rentContract.id}">
				<shiro:hasPermission name="contract:rentContract:editContractFeeInfo">合同公共事业费修改</shiro:hasPermission>
			</a>
		</li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="rentContract" action="${ctx}/contract/rentContract/basicFeeSave" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}" type="${messageType}"/>
		<div class="control-group">
			<label class="control-label">合同编号：</label>
			<div class="controls">
				<form:input path="contractCode" htmlEscape="false" maxlength="100" class="input-xlarge" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合同名称：</label>
			<div class="controls">
				<form:input path="contractName" htmlEscape="false" maxlength="100" class="input-xlarge required" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">入住分电表系数：</label>
			<div class="controls">
				<form:input path="meterValue" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">入住总电表系数：</label>
			<div class="controls">
				<form:input path="totalMeterValue" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">入住峰电系数：</label>
			<div class="controls">
				<form:input path="peakMeterValue" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">入住谷电系数：</label>
			<div class="controls">
				<form:input path="valleyMeterValue" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">入住煤表系数：</label>
			<div class="controls">
				<form:input path="coalValue" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">入住水表系数：</label>
			<div class="controls">
				<form:input path="waterValue" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="contract:rentContract:editContractFeeInfo">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" onclick="submitData()"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>