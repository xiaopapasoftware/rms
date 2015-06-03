<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>物业项目管理</title>
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
		<li class="active"><a href="${ctx}/inventory/propertyProject/">物业项目列表</a></li>
		<shiro:hasPermission name="inventory:propertyProject:edit"><li><a href="${ctx}/inventory/propertyProject/form">物业项目添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="propertyProject" action="${ctx}/inventory/propertyProject/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>居委会：</label>
				<form:select path="neighborhood.id" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>物业公司：</label>
				<form:select path="managementCompany.id" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>物业项目名称：</label>
				<form:input path="projectName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>物业项目地址：</label>
				<form:input path="projectAddr" htmlEscape="false" maxlength="300" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>居委会</th>
				<th>物业公司</th>
				<th>物业项目名称</th>
				<th>物业项目地址</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="inventory:propertyProject:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="propertyProject">
			<tr>
				<td><a href="${ctx}/inventory/propertyProject/form?id=${propertyProject.id}">
					${fns:getDictLabel(propertyProject.neighborhood.id, '', '')}
				</a></td>
				<td>
					${fns:getDictLabel(propertyProject.managementCompany.id, '', '')}
				</td>
				<td>
					${propertyProject.projectName}
				</td>
				<td>
					${propertyProject.projectAddr}
				</td>
				<td>
					<fmt:formatDate value="${propertyProject.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${propertyProject.remarks}
				</td>
				<shiro:hasPermission name="inventory:propertyProject:edit"><td>
    				<a href="${ctx}/inventory/propertyProject/form?id=${propertyProject.id}">修改</a>
					<a href="${ctx}/inventory/propertyProject/delete?id=${propertyProject.id}" onclick="return confirmx('确认要删除该物业项目吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>