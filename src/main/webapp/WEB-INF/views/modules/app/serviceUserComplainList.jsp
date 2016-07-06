<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>管家投拆管理</title>
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
		<li class="active"><a href="${ctx}/app/serviceUserComplain/">管家投拆列表</a></li>
		<shiro:hasPermission name="app:serviceUserComplain:edit"><li><a href="${ctx}/app/serviceUserComplain/form">管家投拆添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="serviceUserComplain" action="${ctx}/app/serviceUserComplain/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>投拆人：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>被投拆人,服务管家：</label>
				<sys:treeselect id="serviceUser" name="serviceUser.id" value="${serviceUserComplain.serviceUser.id}" labelName="" labelValue="${serviceUserComplain.serviceUser.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>投拆内容：</label>
				<form:input path="content" htmlEscape="false" maxlength="500" class="input-medium"/>
			</li>
			<li><label>备注信息：</label>
				<form:input path="remarks" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="app:serviceUserComplain:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="serviceUserComplain">
			<tr>
				<td><a href="${ctx}/app/serviceUserComplain/form?id=${serviceUserComplain.id}">
					<fmt:formatDate value="${serviceUserComplain.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</a></td>
				<td>
					${serviceUserComplain.remarks}
				</td>
				<shiro:hasPermission name="app:serviceUserComplain:edit"><td>
    				<a href="${ctx}/app/serviceUserComplain/form?id=${serviceUserComplain.id}">修改</a>
					<a href="${ctx}/app/serviceUserComplain/delete?id=${serviceUserComplain.id}" onclick="return confirmx('确认要删除该管家投拆吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>