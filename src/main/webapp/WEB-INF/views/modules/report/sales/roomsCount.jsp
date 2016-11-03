<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title></title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function(){
				$("#searchForm").attr("action","${ctx}/report/sales/exportRoomsCount");
				$("#searchForm").submit();
				$("#searchForm").attr("action","${ctx}/report/sales/roomsCount");
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
	<form:form id="searchForm" modelAttribute="houseRoomReport" action="${ctx}/report/sales/roomsCount" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:message content="${message}" type="${messageType}"/>
		<ul class="ul-form">
			<li><label style="width:120px;">物业项目：</label>
				<form:select path="propertyProject.id" class="input-medium" style="width:190px;">
					<form:option itemLabel="ALL" itemValue="ALL" value="ALL" label="全部"/>
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
				<th>物业项目名称</th>
				<th>房间总数</th>
				<th>待装修房间数</th>
				<th>待出租可预订房间数</th>
				<th>已预定房间数</th>
				<th>已出租房间数</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="houseRoomReport">
				<tr>
					<td>
						${houseRoomReport.projectName}
					</td>
					<td>
						${houseRoomReport.totalNum}
					</td>
					<td>
						${houseRoomReport.renovationNum}
					</td>
					<td>
						${houseRoomReport.toBeReservedNum}
					</td>
					<td>
						${houseRoomReport.reservedNum}
					</td>
					<td>
						${houseRoomReport.leasedNum}
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>