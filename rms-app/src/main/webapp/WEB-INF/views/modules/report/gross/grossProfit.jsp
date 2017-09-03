<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title></title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<form:form id="searchForm" modelAttribute="houseRoomReport" action="${ctx}/report/sales/roomsCount" method="post" class="breadcrumb form-search">
		<sys:message content="${message}" type="${messageType}"/>
		<ul class="ul-form">
			<li class="btns"><input id="btnExport" class="btn btn-primary" type="button" value="导出"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>项目</th>
				<th>收入</th>
				<th>支出</th>
				<th>毛利</th>
				<th>毛利率</th>
			</tr>
		</thead>
		<tbody>
			<%--<c:forEach items="${page.list}" var="report">--%>
				<tr>
					<td>
						${report.name}
					</td>
					<td>
						${report.income}
					</td>
					<td>
						${report.cost}
					</td>
					<td>
						${report.totalProfit}
					</td>
					<td>
						${report.profitPercent}
					</td>
				</tr>
			<%--</c:forEach>--%>
		</tbody>
	</table>
</body>
</html>