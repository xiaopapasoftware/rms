<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>企业联系人管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
            top.$.jBox.tip.mess = null;
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
		<li class="active"><a href="${ctx}/person/companyLinkman/">企业联系人列表</a></li>
		<shiro:hasPermission name="person:companyLinkman:edit"><li><a href="${ctx}/person/companyLinkman/form">企业联系人添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="companyLinkman" action="${ctx}/person/companyLinkman/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>企业：</label>
				<form:select path="company.id" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${listCompany}" itemLabel="companyName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>姓名：</label>
				<form:input path="personName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>手机号码：</label>
				<form:input path="cellPhone" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>座机号码：</label>
				<form:input path="tellPhone" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>邮箱：</label>
				<form:input path="email" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<shiro:hasPermission name="person:companyLinkman:view">
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			</shiro:hasPermission>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" type="${messageType}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>企业</th>
				<th>姓名</th>
				<th>手机号码</th>
				<th>座机号码</th>
				<th>邮箱</th>
				<th>创建时间</th>
				<th>修改时间</th>
				<th>创建人</th>
				<th>修改人</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="companyLinkman">
			<tr>
				<td>
					${companyLinkman.company.companyName}
				</td>
				<td><a href="${ctx}/person/companyLinkman/form?id=${companyLinkman.id}">
					${companyLinkman.personName}</a>
				</td>
				<td>
					${companyLinkman.cellPhone}
				</td>
				<td>
					${companyLinkman.tellPhone}
				</td>
				<td>
					${companyLinkman.email}
				</td>
				<td>
					<fmt:formatDate value="${companyLinkman.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${companyLinkman.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
				 	${companyLinkman.createBy.loginName}
				</td>
				<td>
				 	${companyLinkman.updateBy.loginName}
				</td>
				<td>
					${companyLinkman.remarks}
				</td>
				<td>
				<shiro:hasPermission name="person:companyLinkman:edit">
    				<a href="${ctx}/person/companyLinkman/form?id=${companyLinkman.id}">修改</a>
    			</shiro:hasPermission>
    			<shiro:hasPermission name="person:companyLinkman:del">
					<a href="${ctx}/person/companyLinkman/delete?id=${companyLinkman.id}" onclick="return confirmx('确认要删除该企业联系人吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>