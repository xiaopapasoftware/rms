<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>房间信息管理</title>
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
		<li class="active"><a href="${ctx}/inventory/room/">房间信息列表</a></li>
		<shiro:hasPermission name="inventory:room:edit"><li><a href="${ctx}/inventory/room/form">房间信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="room" action="${ctx}/inventory/room/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>物业项目：</label>
				<form:select path="propertyProject.id" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>楼宇：</label>
				<form:select path="building.id" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>房屋号：</label>
				<form:select path="house.houseNo" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>房间号：</label>
				<form:input path="roomNo" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>电表号：</label>
				<form:input path="meterNo" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>房间面积：</label>
				<form:input path="roomSpace" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>朝向：</label>
				<form:checkboxes path="orientation" items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>附属结构：</label>
				<form:checkboxes path="structure" items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>房间状态：</label>
				<form:select path="roomStatus" class="input-medium">
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
				<th>房屋号</th>
				<th>房间号</th>
				<th>电表号</th>
				<th>房间面积</th>
				<th>朝向</th>
				<th>附属结构</th>
				<th>房间状态</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="inventory:room:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="room">
			<tr>
				<td><a href="${ctx}/inventory/room/form?id=${room.id}">
					${fns:getDictLabel(room.propertyProject.id, '', '')}
				</a></td>
				<td>
					${fns:getDictLabel(room.building.id, '', '')}
				</td>
				<td>
					${fns:getDictLabel(room.house.houseNo, '', '')}
				</td>
				<td>
					${room.roomNo}
				</td>
				<td>
					${room.meterNo}
				</td>
				<td>
					${room.roomSpace}
				</td>
				<td>
					${fns:getDictLabel(room.orientation, '', '')}
				</td>
				<td>
					${fns:getDictLabel(room.structure, '', '')}
				</td>
				<td>
					${fns:getDictLabel(room.roomStatus, '', '')}
				</td>
				<td>
					<fmt:formatDate value="${room.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${room.remarks}
				</td>
				<shiro:hasPermission name="inventory:room:edit"><td>
    				<a href="${ctx}/inventory/room/form?id=${room.id}">修改</a>
					<a href="${ctx}/inventory/room/delete?id=${room.id}" onclick="return confirmx('确认要删除该房间信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>