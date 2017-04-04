<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>后付费付款</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					if(""==$("#contractName").val()) {
						top.$.jBox.tip('请选择合同.','warning');
						return;
					}
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
		<li><a href="${ctx}/contract/rentContract/queryPublicBasicFeeInfo">合同初始事业费管理</a></li>
		<li><a href="${ctx}/fee/electricFee/postpaidFeeList">后付费查询</a></li>
		<li class="active"><a href="${ctx}/fee/electricFee/postpaidFeeForm">后付费付款</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="postpaidFee" action="${ctx}/fee/electricFee/postpaidFeeSave" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}" type="${messageType}"/>
		<div class="control-group">
			<label class="control-label">出租合同：</label>
			<div class="controls">
				<div class="input-append">
					<input id="rentContractId" name="rentContractId" class="" type="hidden" value="${rentContractId}">
					<input id="contractName" readonly="readonly" type="text"  value="${contractName}">
					<a id="rentContractButton" href="javascript:" class="btn" style="">&nbsp;<i class="icon-search"></i>&nbsp;</a>&nbsp;&nbsp;
				</div>
				<span class="help-inline"><font color="red">*</font></span>
				<script type="text/javascript">
					$("#rentContractButton, #contractName").click(function(){
						if ($("#rentContractButton").hasClass("disabled")){
							return true;
						}
						top.$.jBox.open("iframe:${ctx}/contract/rentContract/rentContractDialog", "选择合同", 1000, 520, {
							buttons:{"确定":"ok", "关闭":true}, submit:function(v, h, f){
								if (v=="ok"){
									var checkedId = h.find("iframe")[0].contentWindow.$("input[name='rentContractId']:checked");
									$("#rentContractId").val($(checkedId).val());
									$("#contractName").val($(checkedId).attr("attr-name"));
								}
							}
						});
					});
				</script>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">付费类型：</label>
			<div class="controls">
				<form:select path="publicFeeType" class="input-medium" style="width:210px;">
					<form:options items="${fns:getDictList('public_fee_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
	 			</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">付费金额：</label>
			<div class="controls">
				<form:input path="payAmount" htmlEscape="false" class="input-xlarge number required"/>
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
			<shiro:hasPermission name="fee:postpaidFee:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>