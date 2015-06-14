<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>审核历史</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
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
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>审核人</th>
				<th>审核时间</th>
				<th>审核结果</th>
				<th>审核意见</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="auditHis">
			<tr>
				<td>
					${auditHis.auditUserName}
				</td>
				<td>
					<fmt:formatDate value="${auditHis.auditTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(auditHis.auditStatus, 'contract_status', '')}
				</td>
				</td>
				<td>
					${auditHis.auditMsg}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>