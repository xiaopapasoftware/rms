<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>楼宇管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					var saveData = $("#inputForm").serialize();
					$.ajaxSetup({ cache: false });
					$.get("${ctx}/inventory/building/ajaxSave", saveData, function(data){
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
							iframe.contentWindow.$("[id='building.id']").find("option").each(function(){
								if($(this).attr("selected")=="selected") {
									$(this).removeAttr("selected");
									return false;
								}
							});
							var text = iframe.contentWindow.$("[id='building.id']").html();
							text = "<option value='"+json.id+"' selected='selected'>"+json.name+"</option>"+text;
							iframe.contentWindow.$("[id='building.id']").html(text);
							iframe.contentWindow.$("[id='building.id']").val(json.id);
							iframe.contentWindow.$("[id='building.id']").prev("[id='s2id_building.id']").find(".select2-chosen").html(json.name);
							
							iframe.contentWindow.$("[id='house.id']").val("").trigger("change");
							iframe.contentWindow.$("[id='building.id']").val(json.id).trigger("change");
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
		});
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="building" action="" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}" type="${messageType}"/>
		<div class="control-group">
			<label class="control-label">物业项目：</label>
			<div class="controls">
				<form:select path="propertyProject.id" class="input-xlarge required">
					<form:options items="${listPropertyProject}" itemLabel="projectName" itemValue="id" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">楼宇名称：</label>
			<div class="controls">
				<form:input path="buildingName" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">公寓类型：</label>
			<div class="controls">
				<form:select path="type" class="input-xlarge required">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('building_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">总楼层数：</label>
			<div class="controls">
				<form:input type = "number" placeholder="请填写正整数" path="totalFloorCount" htmlEscape="false" maxlength="100" class="input-xlarge digits required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">公寓别名：</label>
			<div class="controls">
				<form:input path="nickName" htmlEscape="false" maxlength="100" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房源最小租金：</label>
			<div class="controls">
				<form:input path="minAmount" htmlEscape="false" maxlength="100" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房源最大租金：</label>
			<div class="controls">
				<form:input path="maxAmount" htmlEscape="false" maxlength="100" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">楼宇图片：</label>
			<div class="controls">
				<form:hidden id="attachmentPath" path="attachmentPath" htmlEscape="false" maxlength="4000" class="input-xlarge"/>
				<sys:ckfinder input="attachmentPath" type="files" uploadPath="/楼宇图片" selectMultiple="true"/>
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