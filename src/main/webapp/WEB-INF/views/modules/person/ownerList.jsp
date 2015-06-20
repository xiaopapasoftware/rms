<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>业主信息管理</title>
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
		<li class="active"><a href="${ctx}/person/owner/">业主信息列表</a></li>
		<shiro:hasPermission name="person:owner:edit"><li><a href="${ctx}/person/owner/form">业主信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="owner" action="${ctx}/person/owner/" method="post" class="breadcrumb form-search" cssStyle="width:1150px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="width:100px;">姓名：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label style="width:100px;">身份证号：</label>
				<form:input path="socialNumber" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label style="width:100px;">手机号：</label>
				<form:input path="cellPhone" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label style="width:100px;">座机号：</label>
				<form:input path="deskPhone" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label style="width:100px;">详细居住地址：</label>
				<form:input path="address" htmlEscape="false" maxlength="300" class="input-medium"/>
			</li>
			<li class="btns"><label style="width:40px;"></label><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
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
				<th>座机号</th>
				<th>详细居住地址</th>
				<th>创建时间</th>
				<th>修改时间</th>
				<th>创建人</th>
				<th>修改人</th>
				<th>备注信息</th>
				<shiro:hasPermission name="person:owner:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="owner">
			<tr>
				<td><a href="${ctx}/person/owner/form?id=${owner.id}">
					${owner.name}
				</a></td>
				<td>
					${owner.socialNumber}
				</td>
				<td>
					${owner.cellPhone}
				</td>
				<td>
					${owner.deskPhone}
				</td>
				<td>
					${owner.address}
				</td>
				<td>
					<fmt:formatDate value="${owner.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${owner.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
				 	${owner.createBy.loginName}
				</td>
				<td>
				 	${owner.updateBy.loginName}
				</td>
				<td>
					${owner.remarks}
				</td>
				<shiro:hasPermission name="person:owner:edit"><td>
    				<a href="${ctx}/person/owner/form?id=${owner.id}">修改</a>
					<a href="${ctx}/person/owner/delete?id=${owner.id}" onclick="return confirmx('确认要删除该业主信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>