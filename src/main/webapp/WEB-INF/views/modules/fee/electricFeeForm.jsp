<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>电费结算管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					if(""==$("#contractName").val()) {
						top.$.jBox.tip('请选择合同.','warning');
						return;
					}
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
		
		function getMeterValue() {
			if(""==$("#contractName").val()) {
				top.$.jBox.tip('请选择合同.','warning');
				return;
			}
			var rentContractId = $("#rentContractId").val();
			$.get("${ctx}/fee/electricFee/getMeterValue?rentContractId="+rentContractId, function(data){
				$("#meterValue").val(data);
			});
		}
		
		function getMeterFee() {
			if(""==$("#contractName").val()) {
				top.$.jBox.tip('请选择合同.','warning');
				return;
			}
			var rentContractId = $("#rentContractId").val();
			$.get("${ctx}/fee/electricFee/getFee?rentContractId="+rentContractId, function(data){
				$("#personFee").val(data);
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/fee/electricFee/">电费管理列表</a></li>
		<li class="active"><a href="${ctx}/fee/electricFee/form?id=${electricFee.id}">电费充值</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="electricFee" action="${ctx}/fee/electricFee/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}" type="${messageType}"/>
		<div class="control-group">
			<label class="control-label">出租合同：</label>
			<div class="controls">
				<!--<form:input path="rentContractId" htmlEscape="false" maxlength="64" class="input-xlarge required"/>-->
				<div class="input-append">
					<input id="rentContractId" name="rentContractId" class="" type="hidden" value="">
					<input id="contractName" readonly="readonly" type="text" value="" data-msg-required="" class="" style="">
					<a id="rentContractButton" href="javascript:" class="btn  " style="">&nbsp;<i class="icon-search"></i>&nbsp;</a>&nbsp;&nbsp;
				</div>
				<span class="help-inline"><font color="red">*</font> </span>
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
			<label class="control-label">结算类型：</label>
			<div class="controls">
				<form:select path="settleType" class="input-xlarge required">
					<form:option value="2" label="电费充值"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">电费缴纳开始时间：</label>
			<div class="controls">
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${electricFee.startDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">电费缴纳开始时间：</label>
			<div class="controls">
				<input name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${electricFee.endDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">入住电表系数：</label>
			<div class="controls">
				<form:input path="meterValue" htmlEscape="false" class="input-xlarge required number"/>
				<span class="help-inline"><font color="red">*</font> </span>
				<a href="#" onclick="javascript:getMeterValue();">查看电表读数</a>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">自用金额：</label>
			<div class="controls">
				<form:input path="personFee" htmlEscape="false" class="input-xlarge  number required"/>
				<span class="help-inline"><font color="red">*</font> </span>
				<a href="#" onclick="javascript:getMeterFee();">查看电表余额</a>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="fee:electricFee:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>