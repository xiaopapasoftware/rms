<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>承租合同到期提醒</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>承租合同名称</th>
				<th>物业项目</th>
				<th>楼宇</th>
				<th>房屋</th>
				<th>汇款人</th>
				<th>合同生效时间</th>
				<th>首次打款日期</th>
				<th>打款日期</th>
				<th>合同过期时间</th>
				<th>合同签订时间</th>
				<th>承租押金</th>
				<th>合同审核状态</th>
				<th>更新时间</th>
				<th>备注信息</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="leaseContract">
			<tr>
				<td>
					${leaseContract.contractName}
				</td>
				<td>
					${leaseContract.projectName}
				</td>
				<td>
					${leaseContract.buildingBame}
				</td>
				<td>
					${leaseContract.houseNo}
				</td>
				<td>
					${leaseContract.remittancerName}
				</td>
				<td>
					<fmt:formatDate value="${leaseContract.effectiveDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${leaseContract.firstRemittanceDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${fns:getDictLabel(leaseContract.remittanceDate, 'remittance_date', '')}
				</td>
				<td>
					<fmt:formatDate value="${leaseContract.expiredDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${leaseContract.contractDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${leaseContract.deposit}
				</td>
				<td>
					${fns:getDictLabel(leaseContract.contractStatus, 'contract_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${leaseContract.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${leaseContract.remarks}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>