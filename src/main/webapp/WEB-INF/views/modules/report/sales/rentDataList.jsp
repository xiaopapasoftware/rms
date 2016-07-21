<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title></title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function(){
				$("#searchForm").attr("action","${ctx}/report/sales/exportRentData");
				$("#searchForm").submit();
				$("#searchForm").attr("action","${ctx}/report/sales/rentData");
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
	<form:form id="searchForm" modelAttribute="rentDataReport" action="${ctx}/report/sales/rentData" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:message content="${message}" type="${messageType}"/>
		<ul class="ul-form">
			<li><label style="width:120px;">物业项目：</label>
				<form:select path="propertyProject.id" class="input-medium" style="width:190px;" onchange="changeProject()">
					<form:option value="" label="全部"/>
					<form:options items="${projectList}" itemLabel="projectName" itemValue="id" htmlEscape="false"/>
				</form:select>
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
				<th>物业项目</th>
				<th>当月共出租间数</th>
				<th>正常退租</th>
				<th>定金约金</th>
				<th>合同违约退租</th>
				<th>特殊情况合同违约退租</th>
				<th>月末在出租状态总数</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="rentDataReport">
			<tr>
				<td>
					${rentDataReport.reportMonth}
				</td>
				<td>
					${rentDataReport.projectName}
				</td>
				<td>
					${rentDataReport.rent}
				</td>
				<td>
					${rentDataReport.normal}
				</td>
				<td>
					${rentDataReport.breakContract}
				</td>
				<td>
					${rentDataReport.early}
				</td>
				<td>
					${rentDataReport.special}
				</td>
				<td>
					${rentDataReport.renting}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>