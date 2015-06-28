<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>房屋设备关联信息管理</title>
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
		<li class="active"><a href="${ctx}/device/roomDevices/">房屋设备关联信息列表</a></li>
		<shiro:hasPermission name="device:roomDevices:edit"><li><a href="${ctx}/device/roomDevices/form">房屋设备关联信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="roomDevices" action="${ctx}/device/roomDevices/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>物业项目：</label>
				<form:select path="propertyProjectId" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>楼宇：</label>
				<form:select path="buildingId" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>房屋：</label>
				<form:select path="houseId" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>房间 0代表公共区域：</label>
				<form:select path="roomId" class="input-medium">
					<form:option value="" label="请选择..."/>
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
				<th>房屋</th>
				<th>房间 0代表公共区域</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="device:roomDevices:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="roomDevices">
			<tr>
				<td><a href="${ctx}/device/roomDevices/form?id=${roomDevices.id}">
					${fns:getDictLabel(roomDevices.propertyProjectId, '', '')}
				</a></td>
				<td>
					${fns:getDictLabel(roomDevices.buildingId, '', '')}
				</td>
				<td>
					${fns:getDictLabel(roomDevices.houseId, '', '')}
				</td>
				<td>
					${fns:getDictLabel(roomDevices.roomId, '', '')}
				</td>
				<td>
					<fmt:formatDate value="${roomDevices.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${roomDevices.remarks}
				</td>
				<shiro:hasPermission name="device:roomDevices:edit"><td>
    				<a href="${ctx}/device/roomDevices/form?id=${roomDevices.id}">修改</a>
					<a href="${ctx}/device/roomDevices/delete?id=${roomDevices.id}" onclick="return confirmx('确认要删除该房屋设备关联信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>