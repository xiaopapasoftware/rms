<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>退租核算管理</title>
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
		<li class="active"><a href="${ctx}/contract/accounting/">退租核算列表</a></li>
		<shiro:hasPermission name="contract:accounting:edit"><li><a href="${ctx}/contract/accounting/form">退租核算添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="accounting" action="${ctx}/contract/accounting/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>出租合同：</label>
				<form:select path="rentContract.id" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>核算类型：</label>
				<form:select path="accountingType" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>核算费用方向：</label>
				<form:select path="feeDirection" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>核算费用类别：</label>
				<form:select path="feeType" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>核算金额：</label>
				<form:input path="feeAmount" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>核算人：</label>
				<sys:treeselect id="user" name="user.id" value="${accounting.user.id}" labelName="user.name" labelValue="${accounting.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>核算时间：</label>
				<input name="feeDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${accounting.feeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
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
				<th>核算类型</th>
				<th>核算费用方向</th>
				<th>核算费用类别</th>
				<th>核算金额</th>
				<th>核算人</th>
				<th>核算时间</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="contract:accounting:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="accounting">
			<tr>
				<td><a href="${ctx}/contract/accounting/form?id=${accounting.id}">
					${fns:getDictLabel(accounting.rentContract.id, '', '')}
				</a></td>
				<td>
					${fns:getDictLabel(accounting.accountingType, '', '')}
				</td>
				<td>
					${fns:getDictLabel(accounting.feeDirection, '', '')}
				</td>
				<td>
					${fns:getDictLabel(accounting.feeType, '', '')}
				</td>
				<td>
					${accounting.feeAmount}
				</td>
				<td>
					${accounting.user.name}
				</td>
				<td>
					<fmt:formatDate value="${accounting.feeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${accounting.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${accounting.remarks}
				</td>
				<shiro:hasPermission name="contract:accounting:edit"><td>
    				<a href="${ctx}/contract/accounting/form?id=${accounting.id}">修改</a>
					<a href="${ctx}/contract/accounting/delete?id=${accounting.id}" onclick="return confirmx('确认要删除该退租核算吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>