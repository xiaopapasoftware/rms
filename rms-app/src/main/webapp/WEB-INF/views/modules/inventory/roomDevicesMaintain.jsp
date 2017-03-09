<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>房间设备维护管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					$("#btnSubmit").attr("disabled",true);
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
		
		function delRow(obj, prefix){
			var delFlag = $(prefix+"_delFlag");
			var manualSetFlag = $(prefix+"_manualSetFlag").val();
			if (manualSetFlag == "1"){
				var id = $(prefix+"_id");
				$(obj).parent().parent().remove();
				$.post("${ctx}/device/devices/updateDevicesChooseFlag",{id:id.val(),status:"0"},function(){});//更新为未设置状态
			}else if(delFlag.val() == "0" && manualSetFlag == "0"){
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
			}else if(delFlag.val() == "1" && manualSetFlag == "0"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		}
		
		function addDialog(obj) {
			top.$.jBox.open("iframe:${ctx}/device/devices/deviceDialog", "选择设备", 1000, 520, {
				buttons:{"确定":"ok", "关闭":true}, submit:function(v, h, f){
					if (v=="ok"){//点了确定后
						var checkedRadio = h.find("iframe")[0].contentWindow.$("input[name='selectedDeviceRadio']:checked");
						if($(checkedRadio).val() == "undefined" || $(checkedRadio).val() == null || $(checkedRadio).val() == ""){//没有任何数据选中
							$("#roomDevicesDtlList"+ obj).remove();
						}else{//选中了数据
							var serNum = $(checkedRadio).attr("attr-distrSerlNum");
							if(serNum == "undefined" || serNum == null || serNum == ""){
								serNum = $("#projectName").val() + "-" + $("#buildingName").val() + "-" + $("#houseNo").val() + "-" + $("#roomNo").val() + "-" + (obj+1);
							}
							//把选中的设备设置状态变更为“已设置”
							$.post("${ctx}/device/devices/updateDevicesChooseFlag",{id: $(checkedRadio).val(),status:"1"},function(){});
							$("#roomDevicesDtlList"+ obj +"_id").val($(checkedRadio).val());
							$("#roomDevicesDtlList"+ obj +"_deviceBrand").val($(checkedRadio).attr("attr-deviceBrand"));
							$("#roomDevicesDtlList"+ obj +"_deviceType").val($(checkedRadio).attr("attr-deviceType"));
							$("#roomDevicesDtlList"+ obj +"_deviceTypeDesc").val($(checkedRadio).attr("attr-deviceTypeDesc"));
							$("#roomDevicesDtlList"+ obj +"_deviceName").val($(checkedRadio).attr("attr-deviceName"));
							$("#roomDevicesDtlList"+ obj +"_deviceId").val($(checkedRadio).attr("attr-deviceId"));
							$("#roomDevicesDtlList"+ obj +"_deviceModel").val($(checkedRadio).attr("attr-deviceModel"));
							$("#roomDevicesDtlList"+ obj +"_devicePrice").val($(checkedRadio).attr("attr-devicePrice"));
							$("#roomDevicesDtlList"+ obj +"_distrSerlNum").val(serNum);
							$("#roomDevicesDtlList"+ obj +"_manualSetFlag").val("1");//已经手动设置
					   }
					} else{//点了关闭后
						$("#roomDevicesDtlList"+ obj).remove();
					}
				},persistent:true,showClose: false
			});
 		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li>
			<a href="${ctx}/inventory/room/">房间信息列表</a>
		</li>
		<li>
			<a href="${ctx}/inventory/room/form?id=${room.id}">房间信息<shiro:hasPermission name="inventory:room:edit">${not empty room.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="inventory:room:edit">查看</shiro:lacksPermission></a>
		</li>
		<li class="active">
			<a href="${ctx}/device/roomDevices/maintainDevices?roomId=${room.id}">设备<shiro:hasPermission name="device:roomDevices:edit">维护</shiro:hasPermission><shiro:lacksPermission name="device:roomDevices:edit">查看</shiro:lacksPermission></a>
		</li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="roomDevices" action="${ctx}/device/roomDevices/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="propertyProjectId"/>
		<form:hidden path="buildingId"/>
		<form:hidden path="houseId"/>
		<form:hidden path="roomId"/>

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
			<label class="control-label">房间号（0:公共区域）：</label>
			<div class="controls">
				<form:input path="roomNo" htmlEscape="false" maxlength="100" class="input-xlarge" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房间面积（平方米）：</label>
			<div class="controls">
				<form:input path="room.roomSpace" htmlEscape="false" class="input-xlarge number" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">朝向：</label>
			<div class="controls">
				<form:input path="room.orientation" value="${fns:getDictLabels(room.orientation, 'orientation', '')}" htmlEscape="false" maxlength="100" class="input-xlarge" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">附属结构：</label>
			<div class="controls">
				<form:input path="room.structure" value="${fns:getDictLabels(room.structure, 'structure', '')}" htmlEscape="false" maxlength="100" class="input-xlarge" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">设备信息：</label>
			<div class="controls">
				<table id="contentTable" class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th class="hide"></th>
							<th>设备品牌</th>
							<th>设备类型</th>
							<th>设备名称</th>
							<th>设备编号</th>
							<th>设备型号</th>
							<th>设备采购价格</th>
							<th>设备分配序号</th>
							<th class="hide"></th>
							<th width="10">操作</th>
						</tr>
					</thead>
					<tbody id="roomDevicesDtlList">
					</tbody>
					<shiro:hasPermission name="device:roomDevices:edit">
						<tfoot>
							<tr>
								<td colspan="8">
									<a href="javascript:" onclick="addRow('#roomDevicesDtlList', roomDevicesDtlRowIdx, roomDevicesDtlTpl);roomDevicesDtlRowIdx = roomDevicesDtlRowIdx + 1;addDialog(roomDevicesDtlRowIdx-1);" class="btn">新增</a>
								</td>
							</tr>
						</tfoot>
					</shiro:hasPermission>
				</table>
				<script type="text/template" id="roomDevicesDtlTpl">
				   <tr id="roomDevicesDtlList{{idx}}">
					 <td class="hide">
						 <input id="roomDevicesDtlList{{idx}}_id" name="roomDevicesDtlList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						 <input id="roomDevicesDtlList{{idx}}_delFlag" name="roomDevicesDtlList[{{idx}}].delFlag" type="hidden" value="0"/>
				     </td>
					 <td>
						 <input id="roomDevicesDtlList{{idx}}_deviceBrand" name="roomDevicesDtlList[{{idx}}].deviceBrand" type="text" readonly="readonly" value="{{row.deviceBrand}}" class="input-medium"/>
				     </td>
					 <td>
					     <input id="roomDevicesDtlList{{idx}}_deviceTypeDesc" name="roomDevicesDtlList[{{idx}}].deviceTypeDesc" type="text" readonly="readonly" value="{{row.deviceTypeDesc}}" class="input-medium"/>
						 <input id="roomDevicesDtlList{{idx}}_deviceType" name="roomDevicesDtlList[{{idx}}].deviceType" type="text" readonly="readonly" value="{{row.deviceType}}" class="input-medium hide"/>
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
						 <input id="roomDevicesDtlList{{idx}}_distrSerlNum" name="roomDevicesDtlList[{{idx}}].distrSerlNum" type="text" value="{{row.distrSerlNum}}" class="input-medium required"/>
						 <span class="help-inline"><font color="red">*</font></span>
					 </td>
					 <td class="hide">
						 <input id="roomDevicesDtlList{{idx}}_manualSetFlag" name="roomDevicesDtlList[{{idx}}].manualSetFlag" value="0" type="hidden"/>
					 </td>
					 <shiro:hasPermission name="device:roomDevices:edit">
					 <td class="text-center" width="10">
						{{#delBtn}}
							<span class="close" onclick="delRow(this, '#roomDevicesDtlList{{idx}}')" title="删除">
								&times;
							</span>
						{{/delBtn}}
				     </td>
				    </shiro:hasPermission>
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
			<shiro:hasPermission name="device:roomDevices:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>