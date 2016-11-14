<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title></title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		$("#btnExport").click(function() {
			$("#searchForm").attr("action", "${ctx}/report/sales/exportRentAveragePriceReport");
			$("#searchForm").submit();
			$("#searchForm").attr("action", "${ctx}/report/sales/rentAveragePriceReport");
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
	<form:form id="searchForm" modelAttribute="rentAveragePriceReport" action="${ctx}/report/sales/rentAveragePriceReport" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
		<sys:message content="${message}" type="${messageType}" />
		<ul class="ul-form">
			<li><label style="width: 120px;">开始时间：</label> 
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required" value="<fmt:formatDate value="${rentAveragePriceReport.startDate}" pattern="yyyy-MM-dd"/>" 
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width: 196px;" />
			</li>
			<li><label style="width: 120px;">结束时间：</label> 
				<input name="endDate" type="text" readonly="readonly" maxlength="20"
				class="input-medium Wdate required" value="<fmt:formatDate value="${rentAveragePriceReport.endDate}" pattern="yyyy-MM-dd"/>"
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
				<th>单间平均房租价格</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="rentAveragePriceReport">
				<tr>
					<td>${rentAveragePriceReport.projectName}</td>
					<td>${rentAveragePriceReport.jointRentAvgPrice}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>