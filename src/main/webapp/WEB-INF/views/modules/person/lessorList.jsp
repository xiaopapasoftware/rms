<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>出租人管理管理</title>
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
		<li class="active"><a href="${ctx}/person/lessor/">出租人管理列表</a></li>
		<shiro:hasPermission name="person:lessor:edit"><li><a href="${ctx}/person/lessor/form">出租人管理添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="lessor" action="${ctx}/person/lessor/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>姓名：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="cellPhone" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>居住地址：</label>
				<form:input path="address" htmlEscape="false" maxlength="300" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>姓名</th>
				<th>身份证号</th>
				<th>手机号</th>
				<th>性别</th>
				<th>居住地址</th>
				<th>备注信息</th>
				<shiro:hasPermission name="person:lessor:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="lessor">
			<tr>
				<td><a href="${ctx}/person/lessor/form?id=${lessor.id}">
					${lessor.name}
				</a></td>
				<td>
					${lessor.socialNumber}
				</td>
				<td>
					${lessor.cellPhone}
				</td>
				<td>
					${fns:getDictLabel(lessor.gender, 'gender', '')}
				</td>
				<td>
					${lessor.address}
				</td>	
				<td>
					${lessor.remarks}
				</td>
				<shiro:hasPermission name="person:lessor:edit"><td>
    				<a href="${ctx}/person/lessor/form?id=${lessor.id}">修改</a>
					<a href="${ctx}/person/lessor/delete?id=${lessor.id}" onclick="return confirmx('确认要删除该出租人管理吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>