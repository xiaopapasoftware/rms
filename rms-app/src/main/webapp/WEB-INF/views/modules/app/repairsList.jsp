<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>报修管理</title>
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
		<li class="active"><a href="${ctx}/app/repairs/">报修列表</a></li>
		<shiro:hasPermission name="app:repairs:edit"><li><a href="${ctx}/app/repairs/form">报修添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="repairs" action="${ctx}/app/repairs/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>手机号：</label>
				<form:input path="userMobile" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>房间号：</label>
				<form:input path="roomId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>描述：</label>
				<form:input path="description" htmlEscape="false" maxlength="18" class="input-medium"/>
			</li>
			<li><label>管家：</label>
				<form:input path="steward" htmlEscape="false" maxlength="1" class="input-medium"/>
			</li>
			<li><label>管家电话：</label>

				<form:select path="stewardMobile" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" type="${messageType}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>报修填写的联系手机号</th>
				<th>房间号</th>
				<th>描述</th>
				<th>管家</th>
				<th>管家电话</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="app:repairs:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="repairs">
			<tr>
				<td><a href="${ctx}/app/repairs/form?id=${repairs.id}">
					${repairs.userMobile}
				</a></td>
				<td>
					${repairs.roomId}
				</td>
				<td>
					${repairs.description}
				</td>
				<td>
					${repairs.steward}
				</td>
				<td>
					${repairs.stewardMobile}
				</td>
				<td>
					<fmt:formatDate value="${repairs.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${repairs.remarks}
				</td>
				<shiro:hasPermission name="app:repairs:edit"><td>
    				<a href="${ctx}/app/repairs/form?id=${repairs.id}">修改</a>
					<a href="${ctx}/app/repairs/delete?id=${repairs.id}" onclick="return confirmx('确认要删除该报修吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>