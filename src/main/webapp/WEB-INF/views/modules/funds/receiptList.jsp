<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>账务收据管理</title>
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
		<li class="active"><a href="${ctx}/funds/receipt/">账务收据列表</a></li>
		<shiro:hasPermission name="funds:receipt:edit"><li><a href="${ctx}/funds/receipt/form">账务收据添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="receipt" action="${ctx}/funds/receipt/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>账务交易：</label>
				<form:input path="tradingAccounts.id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>收据号码：</label>
				<form:input path="receiptNo" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>收据日期：</label>
				<input name="receiptDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${receipt.receiptDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>收据金额：</label>
				<form:input path="receiptAmount" htmlEscape="false" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>账务交易</th>
				<th>收据号码</th>
				<th>收据日期</th>
				<th>收据金额</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="funds:receipt:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="receipt">
			<tr>
				<td><a href="${ctx}/funds/receipt/form?id=${receipt.id}">
					${receipt.tradingAccounts.id}
				</a></td>
				<td>
					${receipt.receiptNo}
				</td>
				<td>
					<fmt:formatDate value="${receipt.receiptDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${receipt.receiptAmount}
				</td>
				<td>
					<fmt:formatDate value="${receipt.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${receipt.remarks}
				</td>
				<shiro:hasPermission name="funds:receipt:edit"><td>
    				<a href="${ctx}/funds/receipt/form?id=${receipt.id}">修改</a>
					<a href="${ctx}/funds/receipt/delete?id=${receipt.id}" onclick="return confirmx('确认要删除该账务收据吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>