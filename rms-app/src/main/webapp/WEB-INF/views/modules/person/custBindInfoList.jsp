<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>三方账号管理</title>
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
		<li class="active"><a href="${ctx}/person/custBindInfo/">三方账号列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="custBindInfo" action="${ctx}/person/custBindInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>三方账号：</label>
				<form:input path="account" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<shiro:hasPermission name="person:custBindInfo:view">
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			</shiro:hasPermission>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" type="${messageType}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>三方账号</th>
				<th>账号类型</th>
				<th>是否有效</th>
				<th>创建时间</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="custBindInfo">
			<tr>
				<td>
					${custBindInfo.account}
				</td>
				<td>
					${fns:getDictLabel(custBindInfo.accountType, 'account_type', '')}
				</td>
				<td>
					<c:if test="${custBindInfo.valid eq '1'}">
						无效
					</c:if>
					<c:if test="${custBindInfo.valid eq '0'}">
						有效
					</c:if>
				</td>
				<td>
					<fmt:formatDate value="${custBindInfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${custBindInfo.remarks}
				</td>
				<td>
					<shiro:hasPermission name="person:custBindInfo:edit">
					<a href="${ctx}/person/custBindInfo/delete?id=${custBindInfo.id}" onclick="return confirmx('确认要删除该信息吗？', this.href)">删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>