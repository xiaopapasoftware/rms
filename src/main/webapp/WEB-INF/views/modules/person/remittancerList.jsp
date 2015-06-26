<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>汇款人信息管理</title>
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
		<li class="active"><a href="${ctx}/person/remittancer/">汇款人信息列表</a></li>
		<shiro:hasPermission name="person:remittancer:edit"><li><a href="${ctx}/person/remittancer/form">汇款人信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="remittancer" action="${ctx}/person/remittancer/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>开户人姓名：</label>
				<form:input path="userName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>开户行名称：</label>
				<form:input path="bankName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>开户行账号：</label>
				<form:input path="bankAccount" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>开户人姓名</th>
				<th>开户行名称</th>
				<th>开户行账号</th>
				<th>创建时间</th>
				<th>修改时间</th>
				<th>创建人</th>
				<th>修改人</th>
				<th>备注信息</th>
				<shiro:hasPermission name="person:remittancer:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="remittancer">
			<tr>
				<td><a href="${ctx}/person/remittancer/form?id=${remittancer.id}">
					${remittancer.userName}
				</a></td>
				<td>
					${remittancer.bankName}
				</td>
				<td>
					${remittancer.bankAccount}
				</td>
				<td>
					<fmt:formatDate value="${remittancer.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${remittancer.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
				 	${remittancer.createBy.loginName}
				</td>
				<td>
				 	${remittancer.updateBy.loginName}
				</td>
				<td>
					${remittancer.remarks}
				</td>
				<shiro:hasPermission name="person:remittancer:edit"><td>
    				<a href="${ctx}/person/remittancer/form?id=${remittancer.id}">修改</a>
					<a href="${ctx}/person/remittancer/delete?id=${remittancer.id}" onclick="return confirmx('确认要删除该汇款人信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>