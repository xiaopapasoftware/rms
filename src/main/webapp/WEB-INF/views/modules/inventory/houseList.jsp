<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>房屋信息管理</title>
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
		<li class="active"><a href="${ctx}/inventory/house/">房屋信息列表</a></li>
		<shiro:hasPermission name="inventory:house:edit"><li><a href="${ctx}/inventory/house/form">房屋信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="house" action="${ctx}/inventory/house/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>物业项目：</label>
				<form:select path="propertyProject.id" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${listPropertyProject}" itemLabel="projectName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>楼宇：</label>
				<form:select path="building.id" class="input-medium">
					<form:option value="" label="请选择..."/>
				</form:select>
			</li>
			<li><label>业主：</label>
				<form:select path="owner.id" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${listOwner}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>房屋号：</label>
				<form:input path="houseNo" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>房屋状态：</label>
				<form:select path="houseStatus" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>物业项目</th>
				<th>楼宇</th>
				<th>业主</th>
				<th>房屋号</th>
				<th>楼层</th>
				<th>原始建筑面积</th>
				<th>装修建筑面积</th>
				<th>原始房屋结构</th>
				<th>装修房屋结构</th>
				<th>房屋状态</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="inventory:house:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="house">
			<tr>
				<td><a href="${ctx}/inventory/house/form?id=${house.id}">
					${fns:getDictLabel(house.propertyProject.id, '', '')}
				</a></td>
				<td>
					${fns:getDictLabel(house.building.id, '', '')}
				</td>
				<td>
					${fns:getDictLabel(house.owner.id, '', '')}
				</td>
				<td>
					${house.houseNo}
				</td>
				<td>
					${house.houseFloor}
				</td>
				<td>
					${house.houseSpace}
				</td>
				<td>
					${house.decorationSpance}
				</td>
				<td>
					${house.houseStructure}
				</td>
				<td>
					${house.decorationStructure}
				</td>
				<td>
					${fns:getDictLabel(house.houseStatus, '', '')}
				</td>
				<td>
					<fmt:formatDate value="${house.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${house.remarks}
				</td>
				<shiro:hasPermission name="inventory:house:edit"><td>
    				<a href="${ctx}/inventory/house/form?id=${house.id}">修改</a>
					<a href="${ctx}/inventory/house/delete?id=${house.id}" onclick="return confirmx('确认要删除该房屋信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>