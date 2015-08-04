<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>楼宇管理</title>
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
		<li class="active"><a href="${ctx}/inventory/building/">楼宇列表</a></li>
		<shiro:hasPermission name="inventory:building:edit"><li><a href="${ctx}/inventory/building/form">楼宇添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="building" action="${ctx}/inventory/building/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>物业项目：</label>
				<form:select path="propertyProject.id" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${listPropertyProject}" itemLabel="projectName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>楼宇名称：</label>
				<form:input path="buildingName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>物业项目</th>
				<th>楼宇名称</th>
				<th>创建时间</th>
				<th>修改时间</th>
				<th>创建人</th>
				<th>修改人</th>
				<th>备注信息</th>
				<shiro:hasPermission name="inventory:building:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="building">
			<tr>
				<td>
					${building.propertyProject.projectName}
				</td>
				<td>
					<a href="${ctx}/inventory/building/form?id=${building.id}">
						${building.buildingName}
					</a>
				</td>
				<td>
					<fmt:formatDate value="${building.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${building.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
				 	${building.createBy.loginName}
				</td>
				<td>
				 	${building.updateBy.loginName}
				</td>
				<td>
					${building.remarks}
				</td>
				<td>
				<shiro:hasPermission name="inventory:building:edit">
    				<a href="${ctx}/inventory/building/form?id=${building.id}">修改</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="inventory:building:del">
					<a href="${ctx}/inventory/building/delete?id=${building.id}" onclick="return confirmx('确认要删除该楼宇和图片及其所有房屋和图片、房间和图片吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>