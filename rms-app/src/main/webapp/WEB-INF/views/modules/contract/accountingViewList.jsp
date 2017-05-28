<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>退租核算管理</title>
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
	<sys:message content="${message}" type="${messageType}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>出租合同</th>
				<th>核算类型</th>
				<th>核算费用方向</th>
				<th>核算费用类别</th>
				<th>核算金额</th>
				<th>核算人</th>
				<th>核算时间</th>
				<th>备注信息</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="accounting">
			<tr>
				<td>
					${accounting.rentContractName}
				</td>
				<td>
					${fns:getDictLabel(accounting.accountingType, 'accounting_type', '')}
				</td>
				<td>
					${fns:getDictLabel(accounting.feeDirection, 'fee_dirction', '')}
				</td>
				<td>
					${fns:getDictLabel(accounting.feeType, 'fee_type', '')}
				</td>
				<td>
					${accounting.feeAmount}
				</td>
				<td>
					${accounting.user.name}
				</td>
				<td>
					<fmt:formatDate value="${accounting.feeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${accounting.remarks}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>