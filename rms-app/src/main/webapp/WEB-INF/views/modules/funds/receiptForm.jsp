<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>账务收据修正</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
            top.$.jBox.tip.mess = null;
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
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
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li>
			<a href="${ctx}/funds/receipt/">账务收据列表</a>
		</li>
		<li class="active">
			<a href="${ctx}/funds/receipt/form?id=${receipt.id}">账务收据修改</a>
		</li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="receipt" action="${ctx}/funds/receipt/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}" type="${messageType}"/>
		<div class="control-group">
			<label class="control-label">账务交易名称：</label>
			<div class="controls">
				<form:input path="tradeName" htmlEscape="false" maxlength="64" class="input-xlarge required" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">账务交易编号：</label>
			<div class="controls">
				<form:input path="tradeNo" htmlEscape="false" maxlength="64" class="input-xlarge required" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">账务交易类型：</label>
			<div class="controls">
 				<form:input path="tradeTypeDesc" htmlEscape="false" class="input-xlarge" readonly="true"/>
				<form:hidden path="tradeType" htmlEscape="false" class="input-xlarge" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">账务交易方式：</label>
			<div class="controls">
 				<form:select path="tradeMode" class="input-xlarge required">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('trans_mode')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">收据款项类型：</label>
			<div class="controls">
				<form:input path="paymentTypeDesc" htmlEscape="false" class="input-xlarge" readonly="true"/>
				<form:hidden path="paymentType" htmlEscape="false" class="input-xlarge" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">收据金额：</label>
			<div class="controls">
			 	<form:input path="receiptAmount" htmlEscape="false" maxlength="64" class="input-xlarge required" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">收据号码：</label>
			<div class="controls">
				<form:input path="receiptNo" htmlEscape="false" maxlength="100" class="input-xlarge"/>
				<span class="help-inline"><font color="red></font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">收据日期：</label>
			<div class="controls">
				<input name="receiptDate" type="text" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${receipt.receiptDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="funds:receipt:adminBackDoorModify">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
 </body>
</html>