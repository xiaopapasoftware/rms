<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>一般费用结算管理</title>
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
	<ul class="nav nav-tabs">
		<li class="active"><a href="#">${normalFee.type}费管理列表</a></li>
		<li><a href="${ctx}/fee/normalFee/form?type=${normalFee.type}&feeType=${normalFee.feeType}">${normalFee.type}费充值</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="normalFee" action="${ctx}/fee/normalFee/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<form:hidden path="feeType"/>
		<form:hidden path="type"/>
		<ul class="ul-form">
			<li><label style="width:120px;">出租合同：</label>
				<form:input path="contractName" htmlEscape="false" maxlength="64" class="input-medium" style="width:195px;"/>
			</li>
			<li><label style="width:120px;">电费缴纳开始时间：</label>
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${normalFee.startDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li><label style="width:120px;">电费缴纳开始时间：</label>
				<input name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${normalFee.endDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li><label style="width:120px;">结算状态：</label>
				<form:select path="settleStatus" class="input-medium" style="width:210px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('settle_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>出租合同</th>
				<th>费用类型</th>
				<th>结算类型</th>
				<th>电费缴纳开始时间</th>
				<th>电费缴纳开始时间</th>
				<th>表系数</th>
				<th>金额</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="fee:normalFee:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="normalFee">
			<tr>
				<td><a href="${ctx}/fee/normalFee/form?id=${normalFee.id}">
					${normalFee.rentContractId}
				</a></td>
				<td>
					${fns:getDictLabel(normalFee.feeType, '', '')}
				</td>
				<td>
					${fns:getDictLabel(normalFee.settleType, '', '')}
				</td>
				<td>
					<fmt:formatDate value="${normalFee.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${normalFee.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${normalFee.meterValue}
				</td>
				<td>
					${normalFee.personFee}
				</td>
				<td>
					<fmt:formatDate value="${normalFee.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${normalFee.remarks}
				</td>
				<shiro:hasPermission name="fee:normalFee:edit"><td>
    				<a href="${ctx}/fee/normalFee/form?id=${normalFee.id}">修改</a>
					<a href="${ctx}/fee/normalFee/delete?id=${normalFee.id}" onclick="return confirmx('确认要删除该一般费用结算吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>