<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>物业公司联系人管理</title>
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
		<li class="active"><a href="${ctx}/person/companyContact/">物业公司联系人列表</a></li>
		<shiro:hasPermission name="person:companyContact:edit"><li><a href="${ctx}/person/companyContact/form">物业公司联系人添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="companyContact" action="${ctx}/person/companyContact/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>物业公司：</label>
				<form:select path="managementCompany.id" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${listManagementCompany}" itemLabel="companyName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>姓名：</label>
				<form:input path="contactName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="cellPhone" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>座机号：</label>
				<form:input path="deskPhone" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>物业公司</th>
				<th>姓名</th>
				<th>手机号</th>
				<th>座机号</th>
				<th>创建时间</th>
				<th>修改时间</th>
				<th>创建人</th>
				<th>修改人</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="companyContact">
			<tr>
				<td>
					${companyContact.managementCompany.companyName}
				</td>
				<td><a href="${ctx}/person/companyContact/form?id=${companyContact.id}">
					${companyContact.contactName}</a>
				</td>
				<td>
					${companyContact.cellPhone}
				</td>
				<td>
					${companyContact.deskPhone}
				</td>
				<td>
					<fmt:formatDate value="${companyContact.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${companyContact.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
				 	${companyContact.createBy.loginName}
				</td>
				<td>
				 	${companyContact.updateBy.loginName}
				</td>
				<td>
					${companyContact.remarks}
				</td>
				<td>
				<shiro:hasPermission name="person:companyContact:edit">
    				<a href="${ctx}/person/companyContact/form?id=${companyContact.id}">修改</a>
    			</shiro:hasPermission>
    			<shiro:hasPermission name="person:companyContact:del">
					<a href="${ctx}/person/companyContact/delete?id=${companyContact.id}" onclick="return confirmx('确认要删除该物业公司联系人吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>