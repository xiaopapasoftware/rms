<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>后付费查询</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {

	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a
			href="${ctx}/contract/rentContract/queryPublicBasicFeeInfo">合同初始事业费管理</a></li>
		<li class="active"><a
			href="${ctx}/fee/electricFee/postpaidFeeList">后付费查询</a></li>
		<li><a href="${ctx}/fee/electricFee/postpaidFeeForm">后付费付款</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="postpaidFee"
		action="${ctx}/fee/electricFee/postpaidFeeList" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<ul class="ul-form">
			<li><label style="width: 120px;">出租合同：</label> <form:input path="contractName" htmlEscape="false" maxlength="64"
					class="input-medium" style="width:195px;" /></li>
			<li><label style="width: 120px;">付费状态：</label> <form:select
					path="payStatus" class="input-medium" style="width:210px;">
					<form:option value="" label="全部" />
					<form:options items="${fns:getDictList('public_fee_pay_status')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select></li>
			<li><label style="width: 120px;">付费开始日期：</label> <input
				name="startDate" type="text" readonly="readonly" maxlength="20"
				class="input-medium Wdate"
				value="<fmt:formatDate value="${postpaidFee.startDate}" pattern="yyyy-MM-dd"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
				style="width: 196px;" /></li>
			<li><label style="width: 120px;">付费结束日期：</label> <input
				name="endDate" type="text" readonly="readonly" maxlength="20"
				class="input-medium Wdate"
				value="<fmt:formatDate value="${postpaidFee.endDate}" pattern="yyyy-MM-dd"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
				style="width: 196px;" /></li>
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
				<th>出租合同</th>
				<th>付费状态</th>
				<th>付费日期</th>
				<th>自用电费</th>
				<th>分摊电费</th>
				<th>水费</th>
				<th>燃气费</th>
				<th>电视费</th>
				<th>宽带费</th>
				<th>服务费</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<th>创建人</th>
				<th>更新人</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="postpaidFee">
				<tr>
					<td><a href="${ctx}/fee/electricFee/postpaidFeeForm?id=${postpaidFee.id}">${postpaidFee.contractName}</a></td>
					<td>${fns:getDictLabel(postpaidFee.payStatus, 'public_fee_pay_status', '')}</td>
					<td><fmt:formatDate value="${postpaidFee.payDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${postpaidFee.electricSelfAmt}</td>
					<td>${postpaidFee.electricShareAmt}</td>
					<td>${postpaidFee.waterAmt}</td>
					<td>${postpaidFee.gasAmt}</td>
					<td>${postpaidFee.tvAmt}</td>
					<td>${postpaidFee.netAmt}</td>
					<td>${postpaidFee.serviceAmt}</td>
					<td><fmt:formatDate value="${postpaidFee.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><fmt:formatDate value="${postpaidFee.updateDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${postpaidFee.createBy.loginName}</td>
					<td>${postpaidFee.updateBy.loginName}</td>
					<td>${postpaidFee.remarks}</td>
					<td>
						<shiro:hasPermission name="fee:postpaidFee:edit">
							<!-- 到账登记前，可以修改 后付费充值记录，并修改相应的款项数据。 -->
							<!-- 账务审核拒绝后，可以直接修改后付费记录数据，把已经到账的后付费款项删除，款项和账务关联记录数据，账务数据统统删除。重新到账登记 -->
							<c:if test="${postpaidFee.payStatus=='1'||postpaidFee.payStatus=='5'}">
								<a href="${ctx}/fee/electricFee/postpaidFeeForm?id=${postpaidFee.id}">修改</a>
							</c:if>
						</shiro:hasPermission>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>