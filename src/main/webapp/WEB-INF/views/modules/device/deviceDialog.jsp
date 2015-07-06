<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>设备信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("tr[attr-devicesChooseFlag='1']").addClass("error");
			$("tr[attr-devicesChooseFlag='1'] td:nth-child(1)").find("INPUT[type = 'radio']").attr("disabled",true); 
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
	 <form:form id="searchForm" modelAttribute="devices" action="${ctx}/device/devices/deviceDialog" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="deviceStatus" name="deviceStatus" type="hidden" value="0"/>
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
			<li><label style="width:100px;">设备分配序号：</label>
				<form:input path="distrSerlNum" htmlEscape="false" maxlength="64" class="input-medium" style="width:177px;"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th></th>
				<th>设备编号</th>
				<th>设备名称</th>
				<th>设备型号</th>
				<th>设备类型</th>
				<th>设备采购价格</th>
				<th>设备品牌</th>
				<th>设备分配序号</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="devices">
			<tr attr-devicesChooseFlag="${devices.devicesChooseFlag}">
				<td>
					<input type="radio" name="selectedDeviceRadio" value="${devices.id}" attr-deviceBrand="${devices.deviceBrand}"
						attr-deviceType="${devices.deviceType}" attr-deviceTypeDesc="${fns:getDictLabel(devices.deviceType, 'device_type', '')}"
						attr-deviceName="${devices.deviceName}" attr-deviceId="${devices.deviceId}" attr-deviceModel="${devices.deviceModel}"
						attr-devicePrice="${devices.devicePrice}" attr-distrSerlNum="${devices.distrSerlNum}"/>
				</td>
				<td>
					${devices.deviceId}
				</td>
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
					${devices.distrSerlNum}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>