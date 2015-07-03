<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>账务交易管理</title>
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
		<li><a href="${ctx}/funds/paymentTrans/">款项交易列表</a></li>
		<li class="active">
			<a href="javascript:void(0);">到账登记</a>
		</li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="tradingAccounts" action="${ctx}/funds/tradingAccounts/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="tradeId"/>
		<form:hidden path="transIds"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">账务交易类型：</label>
			<div class="controls">
				<form:select path="tradeType" class="input-xlarge required">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('trans_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">账务交易方向：</label>
			<div class="controls">
				<form:select path="tradeDirection" class="input-xlarge required">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('fee_dirction')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">交易方式：</label>
			<div class="controls">
				<form:select path="tradeMode" class="input-xlarge required">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('trans_mode')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">交易金额：</label>
			<div class="controls">
				<form:input path="tradeAmount" htmlEscape="false" class="input-xlarge required number"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">收款人名称：</label>
			<div class="controls">
				<form:input path="payeeName" htmlEscape="false" maxlength="100" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">收款人类型：</label>
			<div class="controls">
				<form:select path="payeeType" class="input-xlarge">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('receive_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
			<shiro:hasPermission name="funds:tradingAccounts:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>