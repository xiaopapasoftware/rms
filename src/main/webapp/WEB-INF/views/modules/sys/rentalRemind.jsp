<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>待收房租提醒</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>交易对象</th>
				<th>交易类型</th>
				<th>款项类型</th>
				<th>交易款项方向</th>
				<th>交易款项开始时间</th>
				<th>交易款项到期时间</th>
				<th>应该交易金额</th>
				<th>实际交易金额</th>
				<th>剩余交易金额</th>
				<th>交易款项状态</th>
				<th>更新时间</th>
				<th>备注信息</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="paymentTrans">
			<tr>
				<td>
					${paymentTrans.transName}
				</td>
				<td>
					${fns:getDictLabel(paymentTrans.tradeType, 'trans_type', '')}
				</td>
				<td>
					${fns:getDictLabel(paymentTrans.paymentType, 'payment_type', '')}
				</td>
				<td>
					${fns:getDictLabel(paymentTrans.tradeDirection, 'fee_dirction', '')}
				</td>
				<td>
					<fmt:formatDate value="${paymentTrans.startDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${paymentTrans.expiredDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${paymentTrans.tradeAmount}
				</td>
				<td>
					${paymentTrans.transAmount}
				</td>
				<td>
					${paymentTrans.lastAmount}
				</td>
				<td>
					${fns:getDictLabel(paymentTrans.transStatus, 'trade_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${paymentTrans.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${paymentTrans.remarks}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>