<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>房屋信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
		$(document).ready(function() {
		
		});
		
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function changeProject() {
			var project = $("[id='propertyProject.id']").val();
			var html = "<option value='' selected='selected'>请选择...</option>";
			if("" != project) {
				$.ajaxSetup({ cache: false });
				$.get("${ctx}/inventory/building/findList?id=" + project, function(data){
					for(var i=0;i<data.length;i++) {
						html += "<option value='"+data[i].id+"'>"+data[i].buildingName+"</option>";
					}
					$("[id='building.id']").html(html);
				});
			} else {
				$("[id='building.id']").html(html);
			}
			$("[id='building.id']").val("");
			$("[id='building.id']").prev("[id='s2id_building.id']").find(".select2-chosen").html("请选择...");
		}
		
		function finishDirect(houseId){
			$.get("${ctx}/inventory/house/finishDirect?id=" + houseId, function(data){
				if("SUCCESS" == data){
					alertx("操作成功！");
				}else if("NEEDDO" == data){
					alertx("必须先为房屋分配设备！");
				}else{
					alertx("操作失败！");
				}
				$("#searchForm").submit();
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/inventory/house/">房屋信息列表</a></li>
		<shiro:hasPermission name="inventory:house:edit">
			<li><a href="${ctx}/inventory/house/form">房屋信息添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="house"
		action="${ctx}/inventory/house/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<ul class="ul-form">
			<li>
				<label>物业项目：</label>
				<form:select path="propertyProject.id" class="input-medium" onchange="changeProject()">
					<form:option value="" label="请选择..." />
					<form:options items="${listPropertyProject}" itemLabel="projectName" itemValue="id" htmlEscape="false" />
				</form:select>
			</li>
			<li>
				<label>楼宇：</label> 
				<form:select path="building.id" class="input-medium">
					<form:option value="" label="请选择..." />
					<form:options items="${listBuilding}" itemLabel="buildingName" itemValue="id" htmlEscape="false" />
				</form:select>
			</li>
			<li>
				<label>业主：</label> 
				<form:select path="owner.id" class="input-medium">
					<form:option value="" label="请选择..." />
					<form:options items="${listOwner}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>
			</li>
			<li>
				<label>房屋编码：</label> 
				<form:input path="houseCode" htmlEscape="false" maxlength="100" class="input-medium" />
			</li>
			<li>
				<label>房屋号：</label> 
				<form:input path="houseNo" htmlEscape="false" maxlength="100" class="input-medium" />
			</li>
			<li>
				<label>房屋状态：</label> 
				<form:select path="houseStatus" class="input-medium">
					<form:option value="" label="请选择..." />
					<form:options items="${fns:getDictList('house_status')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</li>
			<li class="btns">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" /></li>
				<li class="clearfix">
			</li>
		</ul>
	</form:form>
	<sys:message content="${message}" type="${messageType}"/>
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>物业项目</th>
				<th>楼宇</th>
				<th>业主</th>
				<th>房屋编码</th>
				<th>房屋号</th>
				<th>房屋状态</th>
				<th>楼层</th>
				<th>原始建筑面积(平方米)</th>
				<th>装修建筑面积(平方米)</th>
				<th>原始房屋结构</th>
				<th>装修房屋结构</th>
				<th>创建时间</th>
				<th>修改时间</th>
				<th>创建人</th>
				<th>修改人</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="house">
				<tr>
					<td>${house.propertyProject.projectName}</td>
					<td>${house.building.buildingName}</td>
					<td>${house.owner.name}</td>
					<td>${house.houseCode}</td>
					<td><a href="${ctx}/inventory/house/form?id=${house.id}">${house.houseNo}</a></td>
					<td>${fns:getDictLabel(house.houseStatus, 'house_status', '')}</td>
					<td>${house.houseFloor}</td>
					<td>${house.houseSpace}</td>
					<td>${house.decorationSpance}</td>
					<td>${house.oriStrucRoomNum}房${house.oriStrucCusspacNum}厅${house.oriStrucWashroNum}卫</td>
					<td>${house.decoraStrucRoomNum}房${house.decoraStrucCusspacNum}厅${house.decoraStrucWashroNum}卫</td>
					<td><fmt:formatDate value="${house.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><fmt:formatDate value="${house.updateDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${house.createBy.loginName}</td>
					<td>${house.updateBy.loginName}</td>
					<td>${house.remarks}</td>
					<shiro:hasPermission name="inventory:house:edit">
						<td>
							<a href="${ctx}/inventory/house/form?id=${house.id}">修改</a>
						</td>
					</shiro:hasPermission>
					<shiro:hasPermission name="inventory:house:del">
						<td>	
							<a href="${ctx}/inventory/house/delete?id=${house.id}" onclick="return confirmx('确认要删除该房屋、图片及其所有房间和图片的信息吗？', this.href)">删除</a>
						</td>
					</shiro:hasPermission>
					<shiro:hasPermission name="device:house:done">
						<td>
							<c:if test="${house.houseStatus eq '0'}">
								<a href="#" onclick="finishDirect('${house.id}');">装修完成</a>
							</c:if>
						</td>
					</shiro:hasPermission>
				 	<shiro:hasPermission name="device:roomDevices:view">
					 	<td><a href="${ctx}/device/roomDevices/viewHouseDevices?houseId=${house.id}">查看设备</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>