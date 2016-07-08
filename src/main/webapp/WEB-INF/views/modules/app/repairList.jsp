<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>报修记录管理</title>
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
		<li class="active"><a href="${ctx}/app/repair/">报修记录列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="repair" action="${ctx}/app/repair/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>报修人：</label>
				<form:input path="userName" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>报修人电话：</label>
				<form:input path="userMobile" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>联系电话：</label>
				<form:input path="repairMobile" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>报修描述：</label>
				<form:input path="description" htmlEscape="false" maxlength="500" class="input-medium"/>
			</li>
			<li><label>管家：</label>
				<form:input path="keeper" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>管家电话：</label>
				<form:input path="keeperMobile" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('repair_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>报修人</th>
				<th>报修人电话</th>
				<th>联系电话</th>
				<th>期望维修时间</th>
				<th>报修描述</th>
				<th>管家</th>
				<th>管家电话</th>
				<th>状态</th>
				<th>创建者</th>
				<th>创建时间</th>
				<th>更新者</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="repair">
			<tr>
				<td><a href="${ctx}/app/repair/form?id=${repair.id}">
					${repair.userName}
				</a></td>
				<td>
					${repair.userMobile}
				</td>
				<td>
					${repair.repairMobile}
				</td>
				<td>
					${repair.expectRepairTime}
				</td>
				<td>
					${repair.description}
				</td>
				<td>
					${repair.keeper}
				</td>
				<td>
					${repair.keeperMobile}
				</td>
				<td>
					${fns:getDictLabel(repair.status, 'repair_status', '')}
				</td>
				<td>
					${repair.createBy.id}
				</td>
				<td>
					<fmt:formatDate value="${repair.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${repair.updateBy.id}
				</td>
				<td>
					<fmt:formatDate value="${repair.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${repair.remarks}
				</td>
				<td>
    				<a href="${ctx}/app/repair/form?id=${repair.id}">修改</a>
					<a href="${ctx}/app/repair/delete?id=${repair.id}" onclick="return confirmx('确认要删除该报修记录吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>