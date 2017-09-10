<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>居委会管理</title>
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
		<shiro:hasPermission name="inventory:neighborhood:view">
			<li class="active">
				<a href="${ctx}/inventory/neighborhood/">居委会列表</a>
			</li>
		</shiro:hasPermission>
		<shiro:hasPermission name="inventory:neighborhood:edit">
			<li>
				<a href="${ctx}/inventory/neighborhood/form">居委会添加</a>
			</li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="neighborhood" action="${ctx}/inventory/neighborhood/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>居委会名称：</label>
				<form:input path="neighborhoodName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>居委会地址：</label>
				<form:input path="neighborhoodAddr" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" type="${messageType}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>居委会名称</th>
				<th>居委会地址</th>
				<th>创建时间</th>
				<th>修改时间</th>
				<th>创建人</th>
				<th>修改人</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="neighborhood">
			<tr>
				<td>
					<a href="${ctx}/inventory/neighborhood/form?id=${neighborhood.id}">
						${neighborhood.neighborhoodName}
					</a>
				</td>
				<td>
					${neighborhood.neighborhoodAddr}
				</td>
				<td>
					<fmt:formatDate value="${neighborhood.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${neighborhood.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
				 	${neighborhood.createBy.loginName}
				</td>
				<td>
				 	${neighborhood.updateBy.loginName}
				</td>
				<td>
					${neighborhood.remarks}
				</td>
				<td>
				<shiro:hasPermission name="inventory:neighborhood:edit">
    				<a href="${ctx}/inventory/neighborhood/form?id=${neighborhood.id}">修改</a>
    			</shiro:hasPermission>
    			<shiro:hasPermission name="inventory:neighborhood:del">
					<a href="${ctx}/inventory/neighborhood/delete?id=${neighborhood.id}" onclick="return confirmx('确认要删除该居委会及所有的居委会联系人吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>