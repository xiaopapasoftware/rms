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
				<th>设备状态</th>
				<th>设备分配序号</th>
				<th>创建时间</th>
				<th>修改时间</th>
				<th>创建人</th>
				<th>修改人</th>
				<th>备注信息</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="devices">
			<tr>
				<td>
					<input id="deviceId" name="deviceId" type="checkbox" value="${devices.id}"/>
				</td>
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
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>