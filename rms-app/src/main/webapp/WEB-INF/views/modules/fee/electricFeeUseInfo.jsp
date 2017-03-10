<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>电费充值管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
		$(document).ready(function() {
			$("#searchForm").validate({
				submitHandler: function(form){
					if(""==$("#contractCode").val()) {
						top.$.jBox.tip('请填写合同编号.','warning');
						return;
					}
					if(Date.parse($("#startDate").val()) > Date.parse($("#endDate").val())){
						top.$.jBox.tip('开始日期不能大于结束日期.','warning');
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
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/fee/electricFee/">电费充值列表</a></li>
		<li><a href="${ctx}/fee/electricFee/form">电费充值</a></li>
		<li class="active"><a href="${ctx}/fee/electricFee/viewUseInfo">电费使用查询</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="electricFeeUseInfo" action="${ctx}/fee/electricFee/viewUseInfo" method="post"
		class="breadcrumb form-search">
		<ul class="ul-form">
			<li><label style="width: 120px;">出租合同编号：</label> <form:input
					path="contractCode" htmlEscape="false" maxlength="64"
					class="input-medium required" style="width:195px;" /> <span
				class="help-inline"><font color="red">*</font></span></li>
			<li><label style="width: 120px;">开始日期：</label> <input
				id="startDate" name="startDate" type="text" readonly="readonly"
				maxlength="20" class="input-medium Wdate required"
				value="${electricFeeUseInfo.startDate}"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
				style="width: 196px;" /> <span class="help-inline"><font
					color="red">*</font></span></li>
			<li><label style="width: 120px;">结束日期：</label> <input
				id="endDate" name="endDate" type="text" readonly="readonly"
				maxlength="20" class="input-medium Wdate required"
				value="${electricFeeUseInfo.endDate}"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
				style="width: 196px;" /> <span class="help-inline"><font
					color="red">*</font></span></li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary"
				type="submit" value="查询" /></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>出租合同编号</th>
				<th>开始日期</th>
				<th>结束日期</th>
				<th>个人使用电量（度）</th>
				<th>个人电费（元）</th>
				<th>公摊使用电量（度）</th>
				<th>公摊电费（元）</th>
				<th>剩余电量（度）</th>
				<th>剩余电费（元）</th>
				<th>个人用电单价（元/度）</th>
				<th>公摊用电单价（元/度）</th>
				<th>电表系统返回的数据</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="electricFeeUseInfo">
				<tr>
					<td>${electricFeeUseInfo.contractCode}</td>
					<td>${electricFeeUseInfo.startDate}</td>
					<td>${electricFeeUseInfo.endDate}</td>
					<td>${electricFeeUseInfo.personalUseEle}</td>
					<td>${electricFeeUseInfo.personalUseAmount}</td>
					<td>${electricFeeUseInfo.publicUseEle}</td>
					<td>${electricFeeUseInfo.publicUseAmount}</td>
					<td>${electricFeeUseInfo.remainedEle}</td>
					<td>${electricFeeUseInfo.remainedEleAmount}</td>
					<td>${electricFeeUseInfo.personalPrice}</td>
					<td>${electricFeeUseInfo.publicPrice}</td>
					<td>${electricFeeUseInfo.returnValue}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>