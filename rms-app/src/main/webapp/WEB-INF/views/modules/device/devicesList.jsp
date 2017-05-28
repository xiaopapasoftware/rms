<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>设备信息管理</title>
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
		<li class="active"><a href="${ctx}/device/devices/">设备信息列表</a></li>
		<shiro:hasPermission name="device:devices:edit"><li><a href="${ctx}/device/devices/form">设备信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="devices" action="${ctx}/device/devices/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="width:100px;">设备编号：</label>
				<form:input path="deviceId" htmlEscape="false" maxlength="64" class="input-medium" style="width:177px;"/>
			</li>
			<li><label style="width:100px;">设备名称：</label>
				<form:input path="deviceName" htmlEscape="false" maxlength="100" class="input-medium" style="width:177px;"/>
			</li>
			<li><label style="width:100px;">设备型号：</label>
				<form:input path="deviceModel" htmlEscape="false" maxlength="100" class="input-medium" style="width:177px;"/>
			</li>
			<li><label style="width:100px;">设备类型：</label>
				<form:select path="deviceType" class="input-medium" style="width:177px;">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('device_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:100px;">设备品牌：</label>
				<form:input path="deviceBrand" htmlEscape="false" maxlength="64" class="input-medium" style="width:177px;"/>
			</li>
			<li><label style="width:100px;">设备状态：</label>
				<form:select path="deviceStatus" class="input-medium" style="width:177px;">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('device_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:100px;">设备分配序号：</label>
				<form:input path="distrSerlNum" htmlEscape="false" maxlength="64" class="input-medium" style="width:177px;"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" type="${messageType}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>设备编号</th>
				<th>设备名称</th>
				<th>设备型号</th>
				<th>设备类型</th>
				<th>设备赔偿价</th>
				<th>设备品牌</th>
				<th>设备状态</th>
				<th>设备分配序号</th>
				<th>创建时间</th>
				<th>修改时间</th>
				<th>创建人</th>
				<th>修改人</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="devices">
			<tr>
				<td><a href="${ctx}/device/devices/form?id=${devices.id}">
					${devices.deviceId}
				</a></td>
				<td>
					${devices.deviceName}
				</td>
				<td>
					${devices.deviceModel}
				</td>
				<td>
					${fns:getDictLabel(devices.deviceType, 'device_type', '')}
				</td>
				<td>
					${devices.devicePrice}
				</td>
				<td>
					${devices.deviceBrand}
				</td>
				<td>
					${fns:getDictLabel(devices.deviceStatus, 'device_status', '')}
				</td>
				<td>
					${devices.distrSerlNum}
				</td>
				<td>
					<fmt:formatDate value="${devices.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${devices.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
				 	${devices.createBy.loginName}
				</td>
				<td>
				 	${devices.updateBy.loginName}
				</td>
				<td>
					${devices.remarks}
				</td>
				<td>
				<shiro:hasPermission name="device:devices:edit">
    				<a href="${ctx}/device/devices/form?id=${devices.id}">修改</a>
    			</shiro:hasPermission>
    			<shiro:hasPermission name="device:devices:del">
					<a href="${ctx}/device/devices/delete?id=${devices.id}" onclick="return confirmx('确认要删除该设备信息吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>