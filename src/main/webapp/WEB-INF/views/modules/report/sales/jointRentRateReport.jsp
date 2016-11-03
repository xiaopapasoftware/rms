<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title></title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		$("#btnExport").click(function() {
			$("#searchForm").attr("action", "${ctx}/report/sales/exportJointRentRateReport");
			$("#searchForm").submit();
			$("#searchForm").attr("action", "${ctx}/report/sales/jointRentRateReport");
		});
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
	<form:form id="searchForm" modelAttribute="jointRentRateReport"
		action="${ctx}/report/sales/jointRentRate" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<sys:message content="${message}" type="${messageType}" />
		<ul class="ul-form">
			<li><label style="width: 120px;">物业项目：</label> <form:select
					path="propertyProject.id" class="input-medium" style="width:190px;">
					<form:option itemLabel="ALL" itemValue="ALL" value="ALL" label="全部" />
					<form:options items="${projectList}" itemLabel="projectName"
						itemValue="id" htmlEscape="false" />
				</form:select></li>
			<li><label style="width: 120px;">开始时间：</label> 
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required" 
				value="<fmt:formatDate value="${jointRentRateReport.startDate}" pattern="yyyy-MM-dd"/>" 
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width: 196px;" />
			</li>
			<li><label style="width: 120px;">结束时间：</label> <input
				name="endDate" type="text" readonly="readonly" maxlength="20"
				class="input-medium Wdate required" value="<fmt:formatDate value="${jointRentRateReport.endDate}" pattern="yyyy-MM-dd"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
				style="width: 196px;" /></li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary"
				type="submit" value="查询" /></li>
			<li class="btns"><input id="btnExport" class="btn btn-primary"
				type="button" value="导出" /></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>物业项目名称</th>
				<th>房间总数</th>
				<th>已出租房间数</th>
				<th>出租率</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="jointRentRateReport">
				<tr>
					<td>${jointRentRateReport.projectName}</td>
					<td>${jointRentRateReport.totalNum}</td>
					<td>${jointRentRateReport.rentedNum}</td>
					<td>${jointRentRateReport.rentRate}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>