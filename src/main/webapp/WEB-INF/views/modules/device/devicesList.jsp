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
			<li><label>设备ID：</label>
				<form:input path="deviceId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>设备名称：</label>
				<form:input path="deviceName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>设备型号：</label>
				<form:input path="deviceModel" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>设备类型：</label>
				<form:select path="deviceType" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>设备采购价格：</label>
				<form:input path="devicePrice" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>设备品牌：</label>
				<form:input path="deviceBrand" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>设备数量：</label>
				<form:input path="deviceAmount" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>设备状态：</label>
				<form:select path="deviceStatus" class="input-medium">
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
				<th>设备ID</th>
				<th>设备名称</th>
				<th>设备型号</th>
				<th>设备类型</th>
				<th>设备采购价格</th>
				<th>设备品牌</th>
				<th>设备数量</th>
				<th>设备状态</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="device:devices:edit"><th>操作</th></shiro:hasPermission>
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
					${fns:getDictLabel(devices.deviceType, '', '')}
				</td>
				<td>
					${devices.devicePrice}
				</td>
				<td>
					${devices.deviceBrand}
				</td>
				<td>
					${devices.deviceAmount}
				</td>
				<td>
					${fns:getDictLabel(devices.deviceStatus, '', '')}
				</td>
				<td>
					<fmt:formatDate value="${devices.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${devices.remarks}
				</td>
				<shiro:hasPermission name="device:devices:edit"><td>
    				<a href="${ctx}/device/devices/form?id=${devices.id}">修改</a>
					<a href="${ctx}/device/devices/delete?id=${devices.id}" onclick="return confirmx('确认要删除该设备信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>