<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>房屋信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#houseNo, #houseFloor, #houseSpace, #decorationSpance, #oriStrucRoomNum, #oriStrucCusspacNum,#oriStrucWashroNum,#decoraStrucRoomNum,#decoraStrucCusspacNum,#decoraStrucWashroNum").keypress(function(event) {
		        if (event.keyCode == 13) {
		            event.preventDefault();
		        }
		    });
			
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
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		
		function changeProject() {
			var project = $("[id='propertyProject.id']").val();
			//把物业项目简称带入房屋编号中
			var projectSimpleName= $("[id='propertyProject.id']").find("option:selected").attr("projectSimpleName");
			if(projectSimpleName==null || projectSimpleName =="" || projectSimpleName==undefined){
				$("#houseCode").val("${house.houseCode}");
			}else{
				$("#houseCode").val(projectSimpleName + "-" + "${house.houseCode}");
			}
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/inventory/house/">房屋信息列表</a></li>
		<li class="active"><a href="${ctx}/inventory/house/form?id=${house.id}">房屋信息<shiro:hasPermission name="inventory:house:edit">${not empty house.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="inventory:house:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="house" action="${ctx}/inventory/house/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}" type="${messageType}"/>
		<div class="control-group">
			<label class="control-label">物业项目：</label>
			<div class="controls">
				<form:select path="propertyProject.id" class="input-xlarge required" onchange="changeProject()">
					<form:option value="" label="请选择..."/>
					<c:forEach items="${listPropertyProject}" var="item">
						<form:option projectSimpleName="${item.projectSimpleName}" value="${item.id}">${item.projectName}</form:option>
					</c:forEach>
				</form:select>
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">楼宇：</label>
			<div class="controls">
				<form:select path="building.id" class="input-xlarge required">
					<form:option value="" label="请选择..."/>
					<form:options items="${listBuilding}" itemLabel="buildingName" itemValue="id" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">业主：</label>
			<div class="controls">
				<form:select path="ownerList" class="input-xlarge required" multiple="true">
					<c:forEach items="${ownerList}" var="item">
						<form:option value="${item.id}">${item.cellPhone}-${item.name}</form:option>
					</c:forEach>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房屋编码：</label>
			<div class="controls">
				<form:input path="houseCode" htmlEscape="false" maxlength="100" class="input-xlarge required" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房屋号：</label>
			<div class="controls">
				<form:input path="houseNo" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">楼层：</label>
			<div class="controls">
				<form:input path="houseFloor" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">原始面积：</label>
			<div class="controls">
				<form:input path="houseSpace" htmlEscape="false" class="input-xlarge required number"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">原始结构：</label>
			<div class="controls">
				<form:input path="oriStrucRoomNum" htmlEscape="false" maxlength="100" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">房 </font></span>
				<form:input path="oriStrucCusspacNum" htmlEscape="false" maxlength="100" class="input-xlarge digits required"/>
				<span class="help-inline"><font color="red">厅 </font></span>
				<form:input path="oriStrucWashroNum" htmlEscape="false" maxlength="100" class="input-xlarge digits required"/>
				<span class="help-inline"><font color="red">卫</font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">装修面积：</label>
			<div class="controls">
				<form:input path="decorationSpance" htmlEscape="false" class="input-xlarge number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">装修结构：</label>
			<div class="controls">
				<form:input path="decoraStrucRoomNum" htmlEscape="false" maxlength="100" class="input-xlarge digits"/>
				<span class="help-inline">房</span>
				<form:input path="decoraStrucCusspacNum" htmlEscape="false" maxlength="100" class="input-xlarge digits"/>
				<span class="help-inline">厅</span>
				<form:input path="decoraStrucWashroNum" htmlEscape="false" maxlength="100" class="input-xlarge digits"/>
				<span class="help-inline">卫</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">产权证号：</label>
			<div class="controls">
				<form:input path="certificateNo" htmlEscape="false" maxlength="100" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房屋图片：</label>
			<div class="controls">
				<form:hidden id="attachmentPath" path="attachmentPath" htmlEscape="false" maxlength="4000" class="input-xlarge"/>
				<sys:ckfinder input="attachmentPath" type="files" uploadPath="/房屋图片" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="inventory:house:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>