<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>账务交易管理</title>
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
		<li class="active"><a href="${ctx}/funds/tradingAccounts/">账务交易列表</a></li>
		<shiro:hasPermission name="funds:tradingAccounts:edit"><li><a href="${ctx}/funds/tradingAccounts/form">账务交易添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="tradingAccounts" action="${ctx}/funds/tradingAccounts/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>账务交易对象：</label>
				<form:input path="tradeId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>账务交易类型：</label>
				<form:select path="tradeType" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>账务交易方向：</label>
				<form:select path="tradeDirection" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>交易方式：</label>
				<form:select path="tradeMode" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>交易金额：</label>
				<form:input path="tradeAmount" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>交易时间：</label>
				<input name="tradeTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${tradingAccounts.tradeTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>收款人名称：</label>
				<form:input path="payeeName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>收款人类型：</label>
				<form:select path="payeeType" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>账务状态：</label>
				<form:select path="tradeStatus" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>账务交易对象</th>
				<th>账务交易类型</th>
				<th>账务交易方向</th>
				<th>交易方式</th>
				<th>交易金额</th>
				<th>交易时间</th>
				<th>收款人名称</th>
				<th>收款人类型</th>
				<th>账务状态</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="funds:tradingAccounts:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tradingAccounts">
			<tr>
				<td><a href="${ctx}/funds/tradingAccounts/form?id=${tradingAccounts.id}">
					${tradingAccounts.tradeId}
				</a></td>
				<td>
					${fns:getDictLabel(tradingAccounts.tradeType, '', '')}
				</td>
				<td>
					${fns:getDictLabel(tradingAccounts.tradeDirection, '', '')}
				</td>
				<td>
					${fns:getDictLabel(tradingAccounts.tradeMode, '', '')}
				</td>
				<td>
					${tradingAccounts.tradeAmount}
				</td>
				<td>
					<fmt:formatDate value="${tradingAccounts.tradeTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${tradingAccounts.payeeName}
				</td>
				<td>
					${fns:getDictLabel(tradingAccounts.payeeType, '', '')}
				</td>
				<td>
					${fns:getDictLabel(tradingAccounts.tradeStatus, '', '')}
				</td>
				<td>
					<fmt:formatDate value="${tradingAccounts.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${tradingAccounts.remarks}
				</td>
				<shiro:hasPermission name="funds:tradingAccounts:edit"><td>
    				<a href="${ctx}/funds/tradingAccounts/form?id=${tradingAccounts.id}">修改</a>
					<a href="${ctx}/funds/tradingAccounts/delete?id=${tradingAccounts.id}" onclick="return confirmx('确认要删除该账务交易吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>