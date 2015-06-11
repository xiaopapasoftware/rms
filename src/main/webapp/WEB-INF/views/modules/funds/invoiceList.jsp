<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>发票信息管理</title>
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
		<li class="active"><a href="${ctx}/funds/invoice/">发票信息列表</a></li>
		<shiro:hasPermission name="funds:invoice:edit"><li><a href="${ctx}/funds/invoice/form">发票信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="invoice" action="${ctx}/funds/invoice/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>账务交易：</label>
				<form:input path="tradingAccounts.id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>开票类型：</label>
				<form:select path="invoiceType" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>发票号码：</label>
				<form:input path="invoiceNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>发票抬头：</label>
				<form:input path="invoiceTitle" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>开票日期：</label>
				<input name="invoiceDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${invoice.invoiceDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>发票金额：</label>
				<form:input path="invoiceAmount" htmlEscape="false" class="input-medium"/>
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
				<th>开票类型</th>
				<th>发票号码</th>
				<th>发票抬头</th>
				<th>开票日期</th>
				<th>发票金额</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="funds:invoice:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="invoice">
			<tr>
				<td><a href="${ctx}/funds/invoice/form?id=${invoice.id}">
					${invoice.tradingAccounts.id}
				</a></td>
				<td>
					${fns:getDictLabel(invoice.invoiceType, '', '')}
				</td>
				<td>
					${invoice.invoiceNo}
				</td>
				<td>
					${invoice.invoiceTitle}
				</td>
				<td>
					<fmt:formatDate value="${invoice.invoiceDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${invoice.invoiceAmount}
				</td>
				<td>
					<fmt:formatDate value="${invoice.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${invoice.remarks}
				</td>
				<shiro:hasPermission name="funds:invoice:edit"><td>
    				<a href="${ctx}/funds/invoice/form?id=${invoice.id}">修改</a>
					<a href="${ctx}/funds/invoice/delete?id=${invoice.id}" onclick="return confirmx('确认要删除该发票信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>