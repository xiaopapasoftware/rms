<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title></title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		$("#btnExport").click(function() {
			$("#searchForm").attr("action", "${ctx}/report/sales/exportEntireRentRateReport");
			$("#searchForm").submit();
			$("#searchForm").attr("action", "${ctx}/report/sales/entireRentRate");
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
	<form:form id="searchForm" modelAttribute="entireRentRateReport"
		action="${ctx}/report/sales/entireRentRate" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<sys:message content="${message}" type="${messageType}" />
		<ul class="ul-form">
			<li><label style="width: 120px;">日期：</label> 
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required" 
				value="<fmt:formatDate value="${jointRentRateReport.startDate}" pattern="yyyy-MM-dd"/>" 
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width: 196px;" />
			</li>
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
				<th>房屋总数</th>
				<th>部分及完全出租房屋数</th>
				<th>出租率</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="entireRentRateReport">
				<tr>
					<td>${entireRentRateReport.projectName}</td>
					<td>${entireRentRateReport.totalNum}</td>
					<td>${entireRentRateReport.rentedNum}</td>
					<td>${entireRentRateReport.rentRate}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>