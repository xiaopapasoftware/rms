<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>房屋信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#houseNo, #houseFloor, #houseSpace, #decorationSpance, #oriStrucRoomNum, #oriStrucCusspacNum, #oriStrucWashroNum,#decoraStrucRoomNum,#decoraStrucCusspacNum,#decoraStrucWashroNum").keypress(function(event) {
		        if (event.keyCode == 13) {
		            event.preventDefault();
		        }
		    });
			
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					var saveData = $("#inputForm").serialize();
					$.ajaxSetup({ cache: false });
					$.get("${ctx}/inventory/house/ajaxSave", saveData, function(data){
						var json = eval("("+data+")");
						if(null!=json.message) top.$.jBox.tip(json.message,'warning');
						if(null!=json.id) {
							top.$.jBox.tip('保存成功!','success');
							var iframe;
							if(undefined == $(window.parent.document).find(".tab_content").html()) {
								iframe = $(window.parent.document).find("iframe")[0];
							} else {
								iframe = $(window.parent.document).find(".tab_content").find(".curholder").find("iframe")[0];
							}
							iframe.contentWindow.$("[id='house.id']").find("option").each(function(){
								if($(this).attr("selected")=="selected") {
									$(this).removeAttr("selected");
									return false;
								}
							});
							var text = iframe.contentWindow.$("[id='house.id']").html();
							text = "<option value='"+json.id+"' selected='selected'>"+json.name+"</option>"+text;
							iframe.contentWindow.$("[id='house.id']").html(text);
							iframe.contentWindow.$("[id='house.id']").val(json.id);
							iframe.contentWindow.$("[id='house.id']").prev("[id='s2id_house.id']").find(".select2-chosen").html(json.name);
							
							iframe.contentWindow.$("[id='room.id']").val("").trigger("change");
							iframe.contentWindow.$("[id='house.id']").val(json.id).trigger("change");

							top.$.jBox.close();
						}
					});
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
			
			var projectSimpleName= $("[id='propertyProject.id']").find("option:selected").attr("projectSimpleName");
			if(projectSimpleName==null || projectSimpleName =="" || projectSimpleName==undefined){
				$("#houseCode").val("${house.houseCode}");
			}else{
				$("#houseCode").val(projectSimpleName + "-" + "${house.houseCode}");
			}
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
		
		function addOwner() {
			top.$.jBox.open("iframe:${ctx}/person/owner/add",'添加业主',850,500,{buttons:{'保存':'1','关闭':'2'},submit:saveHandler});
		}
		
		function saveHandler(v,h,f) {
			if(v=='1') {
				h.find("iframe")[0].contentWindow.$("#inputForm").submit();
				return false;
			}
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="house" action="" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="houseStatus"/>
		<sys:message content="${message}" type="${messageType}"/>
		<div class="control-group">
			<label class="control-label">物业项目：</label>
			<div class="controls">
				<form:select path="propertyProject.id" class="input-xlarge required" onchange="changeProject()">
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
					<form:options items="${listBuilding}" itemLabel="buildingName" itemValue="id" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">业主：</label>
			<div class="controls">
				<form:select path="ownerList" class="input-xlarge" multiple="true">
					<c:forEach items="${ownerList}" var="item">
						<form:option value="${item.id}">${item.cellPhone}-${item.name}</form:option>
					</c:forEach>
				</form:select>
				<a href="#" onclick="addOwner()">添加业主</a>
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
			<label class="control-label">原始建筑面积（平方米）：</label>
			<div class="controls">
				<form:input path="houseSpace" htmlEscape="false" class="input-xlarge required number"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">原始房屋结构：</label>
			<div class="controls">
				<form:input path="oriStrucRoomNum" htmlEscape="false" maxlength="100" class="input-xlarge digits required"/>
				<span class="help-inline"><font color="red">房 </font></span>
				<form:input path="oriStrucCusspacNum" htmlEscape="false" maxlength="100" class="input-xlarge digits required"/>
				<span class="help-inline"><font color="red">厅 </font></span>
				<form:input path="oriStrucWashroNum" htmlEscape="false" maxlength="100" class="input-xlarge digits required"/>
				<span class="help-inline"><font color="red">卫</font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">装修建筑面积（平方米）：</label>
			<div class="controls">
				<form:input path="decorationSpance" htmlEscape="false" class="input-xlarge number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">装修房屋结构：</label>
			<div class="controls">
				<form:input path="decoraStrucRoomNum" htmlEscape="false" maxlength="100" class="input-xlarge digits"/>
				<span class="help-inline">房 </span>
				<form:input path="decoraStrucCusspacNum" htmlEscape="false" maxlength="100" class="input-xlarge digits"/>
				<span class="help-inline">厅 </span>
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
			<label class="control-label">意向租赁类型：</label>
			<div class="controls">
				<form:select path="intentMode" class="input-xlarge required">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('rent_mode')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否精选房源：</label>
			<div class="controls">
				<form:select path="isFeature" class="input-xlarge required">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
	</form:form>
</body>
</html>