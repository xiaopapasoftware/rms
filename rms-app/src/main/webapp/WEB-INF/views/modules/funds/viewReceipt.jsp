<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>收据信息</title>
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
				<th>账务交易对象</th>
				<th>账务交易类型</th>
				<th>款项类型</th>
				<th>账务交易方式</th>
				<th>收据号码</th>
				<th>收据日期</th>
				<th>款项开始日期</th>
				<th>款项结束日期</th>
				<th>收据金额</th>
				<th>备注信息</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="receipt">
			<tr>
				<td>
					${receipt.tradeName}
				</td>
				<td>
					${fns:getDictLabel(receipt.tradeType, 'trans_type', '')}
				</td>
				<td>
					${fns:getDictLabel(receipt.paymentType, 'payment_type', '')}
				</td>
				<td>
					${fns:getDictLabel(receipt.tradeMode, 'trans_mode', '')}
				</td>
				<td>
					${receipt.receiptNo}
				</td>
				<td>
					<fmt:formatDate value="${receipt.receiptDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${receipt.transBeginDateDesc}
				</td>
				<td>
					${receipt.transEndDateDesc}
				</td>
				<td>
					${receipt.receiptAmount}
				</td>
				<td>
					${receipt.remarks}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>