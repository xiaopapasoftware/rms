<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>出租合同管理</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>原出租合同名称</th>
				<th>原定金协议名称</th>
				<th>合同来源</th>
				<th>合同签订类型</th>
				<th>合同编号</th>
				<th>合同名称</th>
				<th>出租方式</th>
				<th>物业项目</th>
				<th>楼宇</th>
				<th>房屋号</th>
				<th>房间号</th>
				<th>月租金</th>
				<th>合同生效时间</th>
				<th>合同过期时间</th>
				<th>合同签订时间</th>
				<th>续租提醒时间</th>
				<th>合同审核状态</th>
				<th>合同业务状态</th>
				<th>销售姓名</th>
				<th>合作人姓名</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="rentContract">
			<tr>
				<td>
					${rentContract.refContractName}
				</td>
				<td>
					${rentContract.refAgreementName}
				</td>
				<td>
					${fns:getDictLabel(rentContract.contractSource, 'contract_source', '')}
				</td>
				<td>
					${fns:getDictLabel(rentContract.signType, 'contract_sign_type', '')}
				</td>
				<td>
					${rentContract.contractCode}
				</td>
				<td>
					${rentContract.contractName}
				</td>
				<td>
					${fns:getDictLabel(rentContract.rentMode, 'rent_mode', '')}
				</td>
				<td>
					${rentContract.projectName}
				</td>
				<td>
					${rentContract.buildingBame}
				</td>
				<td>
					${rentContract.houseNo}
				</td>
				<td>
					${rentContract.roomNo}
				</td>
				<td>
					${rentContract.rental}
				</td>
				<td>
					<fmt:formatDate value="${rentContract.startDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${rentContract.expiredDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${rentContract.signDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${rentContract.remindTime}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${fns:getDictLabel(rentContract.contractStatus, 'rent_contract_status', '')}
				</td>
				<td>
					${fns:getDictLabel(rentContract.contractBusiStatus, 'rent_contract_busi_status', '')}
				</td>
				<td>
					${rentContract.user.name}
				</td>
				<td>
					${rentContract.partner.partnerName}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>