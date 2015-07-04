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
		<li class="active"><a href="${ctx}/fee/normalFee/">一般费用结算列表</a></li>
		<shiro:hasPermission name="fee:normalFee:edit"><li><a href="${ctx}/fee/normalFee/form">一般费用结算添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="normalFee" action="${ctx}/fee/normalFee/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>出租合同：</label>
				<form:input path="rentContractId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>费用类型：</label>
				<form:select path="feeType" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>结算类型：</label>
				<form:select path="settleType" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>电费缴纳开始时间：</label>
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${normalFee.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>电费缴纳开始时间：</label>
				<input name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${normalFee.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>表系数：</label>
				<form:input path="meterValue" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>金额：</label>
				<form:input path="personFee" htmlEscape="false" class="input-medium"/>
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