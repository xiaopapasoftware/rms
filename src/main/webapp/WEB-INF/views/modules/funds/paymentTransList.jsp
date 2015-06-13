<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>款项交易管理</title>
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
		<li class="active"><a href="${ctx}/funds/paymentTrans/">款项交易列表</a></li>
		<shiro:hasPermission name="funds:paymentTrans:edit"><li><a href="${ctx}/funds/paymentTrans/form">款项交易添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="paymentTrans" action="${ctx}/funds/paymentTrans/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>交易类型：</label>
				<form:select path="tradeType" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>款项类型：</label>
				<form:select path="paymentType" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>交易对象：</label>
				<form:input path="transId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>交易款项方向：</label>
				<form:select path="tradeDirection" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>交易款项开始时间：</label>
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${paymentTrans.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>交易款项到期时间：</label>
				<input name="expiredDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${paymentTrans.expiredDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>应该交易金额：</label>
				<form:input path="tradeAmount" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>实际交易金额：</label>
				<form:input path="transAmount" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>剩余交易金额：</label>
				<form:input path="lastAmount" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>交易款项状态：</label>
				<form:select path="transStatus" class="input-medium">
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
				<th>交易类型</th>
				<th>款项类型</th>
				<th>交易对象</th>
				<th>交易款项方向</th>
				<th>交易款项开始时间</th>
				<th>交易款项到期时间</th>
				<th>应该交易金额</th>
				<th>实际交易金额</th>
				<th>剩余交易金额</th>
				<th>交易款项状态</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="funds:paymentTrans:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="paymentTrans">
			<tr>
				<td><a href="${ctx}/funds/paymentTrans/form?id=${paymentTrans.id}">
					${fns:getDictLabel(paymentTrans.tradeType, '', '')}
				</a></td>
				<td>
					${fns:getDictLabel(paymentTrans.paymentType, '', '')}
				</td>
				<td>
					${paymentTrans.transId}
				</td>
				<td>
					${fns:getDictLabel(paymentTrans.tradeDirection, '', '')}
				</td>
				<td>
					<fmt:formatDate value="${paymentTrans.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${paymentTrans.expiredDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
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
					${fns:getDictLabel(paymentTrans.transStatus, '', '')}
				</td>
				<td>
					<fmt:formatDate value="${paymentTrans.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${paymentTrans.remarks}
				</td>
				<shiro:hasPermission name="funds:paymentTrans:edit"><td>
    				<a href="${ctx}/funds/paymentTrans/form?id=${paymentTrans.id}">修改</a>
					<a href="${ctx}/funds/paymentTrans/delete?id=${paymentTrans.id}" onclick="return confirmx('确认要删除该款项交易吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>