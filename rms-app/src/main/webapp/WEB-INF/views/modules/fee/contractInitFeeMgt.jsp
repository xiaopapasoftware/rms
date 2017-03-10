<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>公共事业费管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
		$(document).ready(function() {
			$("#searchForm").validate({
				submitHandler: function(form){
					if(""==$("#contractCode").val()) {
						top.$.jBox.tip('请填写合同编号.','warning');
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs"><li class="active"><a href="${ctx}/contract/rentContract/initFeeMgt">公共事业费管理</a></li></ul>
	<form:form id="searchForm" modelAttribute="rentContract" action="${ctx}/contract/rentContract/queryPublicBasicFeeInfo" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
			<li>
				<label style="width: 120px;">出租合同编号：</label>
				<form:input path="contractCode" htmlEscape="false" maxlength="64" class="input-medium required" style="width:195px;" /> 
				<span class="help-inline"><font color="red">*</font></span>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" /></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>出租合同编号</th>
				<th>出租合同名称</th>
				<th>入住分电表系数</th>
				<th>入住总电表系数</th>
				<th>入住峰电系数</th>
				<th>入住谷电系数</th>
				<th>入住煤表系数</th>
				<th>入住水表系数</th>
			</tr>
		</thead>
		<tbody>
			<!--<c:forEach items="${list}" var="rentContractInfo">
				<tr>
					<td>${rentContractInfo.contractCode}</td>
					<td>${rentContractInfo.contractName}</td>
					<td>${rentContractInfo.meterValue}</td>
					<td>${rentContractInfo.totalMeterValue}</td>
					<td>${rentContractInfo.peakMeterValue}</td>
					<td>${rentContractInfo.valleyMeterValue}</td>
					<td>${rentContractInfo.coalValue}</td>
					<td>${rentContractInfo.waterValue}</td>
				</tr>
			</c:forEach>-->
		</tbody>
	</table>
</body>
</html>