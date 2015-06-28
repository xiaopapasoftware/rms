<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>客户信息管理</title>
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
		<li class="active"><a href="${ctx}/person/customer/">客户信息列表</a></li>
		<shiro:hasPermission name="person:customer:edit"><li><a href="${ctx}/person/customer/form">客户信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="customer" action="${ctx}/person/customer/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>姓名：</label>
				<form:input path="contactName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>性别：</label>
				<form:select path="gender" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>手机号：</label>
				<form:input path="cellPhone" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>销售：</label>
				<form:select path="user.id" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${listUser}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>是否转租客：</label>
				<form:select path="isTenant" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
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
				<th>性别</th>
				<th>手机号</th>
				<th>跟进销售</th>
				<th>是否转租客</th>
				<th>创建时间</th>
				<th>修改时间</th>
				<th>创建人</th>
				<th>修改人</th>
				<th>备注信息</th>
				<shiro:hasPermission name="person:customer:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="customer">
			<tr>
				<td>
					<a href="${ctx}/person/customer/form?id=${customer.id}">${customer.contactName}</a>
				</td>
				<td>
					${fns:getDictLabel(customer.gender, 'sex', '')}
				</td>
				<td>
					${customer.cellPhone}
				</td>
				<td>
					${customer.user.name}
				</td>
				<td>
					${fns:getDictLabel(customer.isTenant, 'yes_no', '')}
				</td>
				<td>
					<fmt:formatDate value="${customer.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${customer.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
				 	${customer.createBy.loginName}
				</td>
				<td>
				 	${customer.updateBy.loginName}
				</td>
				<td>
					${customer.remarks}
				</td>
				<shiro:hasPermission name="person:customer:edit"><td>
    				<a href="${ctx}/person/customer/form?id=${customer.id}">修改</a>
					<a href="${ctx}/person/customer/delete?id=${customer.id}" onclick="return confirmx('确认要删除该客户信息吗？', this.href)">删除</a>
					<c:if test="${customer.isTenant eq '0'}">
						<a href="${ctx}/person/tenant/form?id=${customer.id}">转租客</a>
					</c:if>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>