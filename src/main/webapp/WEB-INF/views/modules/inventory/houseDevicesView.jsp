<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>房屋设备查看</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if(element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		
		function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/inventory/house/">房屋信息列表</a></li>
		<li>
			<a href="${ctx}/inventory/house/form?id=${house.id}">房屋信息<shiro:hasPermission name="inventory:house:edit">${not empty house.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="inventory:house:edit">查看</shiro:lacksPermission></a>
		</li>
		<li class="active">
			<a href="${ctx}/device/roomDevices/viewHouseDevices?houseId=${house.id}">设备查看</a>
		</li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="roomDevices" class="form-horizontal">
		<sys:message content="${message}" type="${messageType}"/>
		<div class="control-group">
			<label class="control-label">物业项目：</label>
			<div class="controls">
				<form:input path="projectName" htmlEscape="false" maxlength="100" class="input-xlarge" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">楼宇：</label>
			<div class="controls">
				<form:input path="buildingName" htmlEscape="false" maxlength="100" class="input-xlarge" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房屋号：</label>
			<div class="controls">
				<form:input path="houseNo" htmlEscape="false" maxlength="100" class="input-xlarge" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">设备信息：</label>
			<div class="controls">
				<table id="contentTable" class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th>设备品牌</th>
							<th>设备类型</th>
							<th>设备名称</th>
							<th>设备编号</th>
							<th>设备型号</th>
							<th>设备采购价格</th>
							<th>设备分配序号</th>
						</tr>
					</thead>
					<tbody id="roomDevicesDtlList">
					</tbody>
				</table>
				<script type="text/template" id="roomDevicesDtlTpl">
				   <tr id="roomDevicesDtlList{{idx}}">
					 <td>
						 <input id="roomDevicesDtlList{{idx}}_deviceBrand" name="roomDevicesDtlList[{{idx}}].deviceBrand" type="text" readonly="readonly" value="{{row.deviceBrand}}" class="input-medium"/>
				     </td>
					 <td>
					     <input id="roomDevicesDtlList{{idx}}_deviceTypeDesc" name="roomDevicesDtlList[{{idx}}].deviceTypeDesc" type="text" readonly="readonly" value="{{row.deviceTypeDesc}}" class="input-medium"/>
					 </td>
				     <td>
						 <input id="roomDevicesDtlList{{idx}}_deviceName" name="roomDevicesDtlList[{{idx}}].deviceName" type="text" readonly="readonly" value="{{row.deviceName}}" class="input-medium"/>
					 </td>
					 <td>
						 <input id="roomDevicesDtlList{{idx}}_deviceId" name="roomDevicesDtlList[{{idx}}].deviceId" type="text" readonly="readonly" value="{{row.deviceId}}" class="input-medium"/>
					 </td>
					 <td>
					     <input id="roomDevicesDtlList{{idx}}_deviceModel" name="roomDevicesDtlList[{{idx}}].deviceModel" type="text" readonly="readonly" value="{{row.deviceModel}}" class="input-medium"/>
					 </td>
					 <td>
						 <input id="roomDevicesDtlList{{idx}}_devicePrice" name="roomDevicesDtlList[{{idx}}].devicePrice" type="text" readonly="readonly" value="{{row.devicePrice}}" class="input-medium"/>
					 </td>
					 <td>
						 <input id="roomDevicesDtlList{{idx}}_distrSerlNum" name="roomDevicesDtlList[{{idx}}].distrSerlNum" type="text" readonly="readonly" value="{{row.distrSerlNum}}" class="input-medium"/>
					 </td>
				</tr>
				</script>
				<script type="text/javascript">
					var roomDevicesDtlRowIdx = 0;
					var roomDevicesDtlTpl = $("#roomDevicesDtlTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
					$(document).ready(function() {
						var data = ${fns:toJson(roomDevices.roomDevicesDtlList)};
						for (var i=0; i<data.length; i++){
							addRow('#roomDevicesDtlList', roomDevicesDtlRowIdx, roomDevicesDtlTpl, data[i]);
							roomDevicesDtlRowIdx = roomDevicesDtlRowIdx + 1;
						}
					});
				</script>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>