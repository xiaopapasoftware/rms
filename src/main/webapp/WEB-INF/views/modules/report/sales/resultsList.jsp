<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title></title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function(){
				$("#searchForm").attr("action","${ctx}/report/sales/exportResults");
				$("#searchForm").submit();
				$("#searchForm").attr("action","${ctx}/report/sales/results");
			});
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="resultsReport" action="${ctx}/report/sales/results" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:message content="${message}" type="${messageType}"/>
		<ul class="ul-form">
			<li>
				<label style="width:120px;">销售：</label>
				<sys:treeselect id="user" name="salerId" value="${resultsReport.salerId}" labelName="user.name" labelValue="${resultsReport.saler}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="btns"><input id="btnExport" class="btn btn-primary" type="button" value="导出"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>月份</th>
				<th>销售</th>
				<th>成交笔数</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="resultsReport">
			<tr>
				<td>${resultsReport.reportMonth}</td>
				<td>${resultsReport.saler}</td>
				<td>${resultsReport.counts}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>