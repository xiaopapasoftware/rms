<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>物业公司管理</title>
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
		<li class="active"><a href="${ctx}/inventory/managementCompany/">物业公司列表</a></li>
		<shiro:hasPermission name="inventory:managementCompany:edit"><li><a href="${ctx}/inventory/managementCompany/form">物业公司添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="managementCompany" action="${ctx}/inventory/managementCompany/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="width:120px;">物业公司名称：</label>
				<form:input path="companyName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label style="width:120px;">物业公司地址：</label>
				<form:input path="companyAddr" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>物业公司名称</th>
				<th>物业公司地址</th>
				<th>创建时间</th>
				<th>修改时间</th>
				<th>创建人</th>
				<th>修改人</th>
				<th>备注信息</th>
				<shiro:hasPermission name="inventory:managementCompany:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="managementCompany">
			<tr>
				<td><a href="${ctx}/inventory/managementCompany/form?id=${managementCompany.id}">
					${managementCompany.companyName}
				</a></td>
				<td>
					${managementCompany.companyAddr}
				</td>
				<td>
					<fmt:formatDate value="${managementCompany.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${managementCompany.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
					<td>
				 	${managementCompany.createBy.loginName}
				</td>
				<td>
				 	${managementCompany.updateBy.loginName}
				</td>
				<td>
					${managementCompany.remarks}
				</td>
				<shiro:hasPermission name="inventory:managementCompany:edit"><td>
    				<a href="${ctx}/inventory/managementCompany/form?id=${managementCompany.id}">修改</a>
					<a href="${ctx}/inventory/managementCompany/delete?id=${managementCompany.id}" onclick="return confirmx('确认要删除该物业公司吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>