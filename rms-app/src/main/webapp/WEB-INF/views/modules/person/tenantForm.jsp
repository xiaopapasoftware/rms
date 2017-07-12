<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>租客信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#tenantName, #idNo, #cellPhone, #email,#houseRegister,#position").keypress(function(event) {
		        if (event.keyCode == 13) {
		            event.preventDefault();
		        }
		    });
			$("input[name$='day']").keypress(function(event) {
		        if (event.keyCode == 13) {
		            event.preventDefault();
		        }
		    });
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
		
		function showBirthday(){
			var idType = $("#idType").val();
			var idNo = $("#idNo").val();
			if(idType == "0"){
				var birthdayValue;
				var gender;
				var genderDesc;
				if(15==idNo.length){ //15位身份证号码
				 	birthdayValue = idNo.charAt(6)+idNo.charAt(7);
					if(parseInt(birthdayValue)<10){
					 	birthdayValue = '20'+birthdayValue;
					}else{
					 	birthdayValue = '19'+birthdayValue;
					}
					birthdayValue=birthdayValue+'-'+idNo.charAt(8)+idNo.charAt(9)+'-'+idNo.charAt(10)+idNo.charAt(11);
					if(parseInt(idNo.charAt(14)/2)*2!=idNo.charAt(14)){
						gender='1';
						genderDesc='男';
					} else{
						gender='2';
						genderDesc='女';
					}
				}
				if(18==idNo.length){ //18位身份证号码
				   birthdayValue=idNo.charAt(6)+idNo.charAt(7)+idNo.charAt(8)+idNo.charAt(9)+'-'+idNo.charAt(10)+idNo.charAt(11)+'-'+idNo.charAt(12)+idNo.charAt(13);
				   if(parseInt(idNo.charAt(16)/2)*2!=idNo.charAt(16)){
						gender='1';
						genderDesc='男';
					} else{
						gender='2';
						genderDesc='女';
					}
				}
				if(birthdayValue!=null && birthdayValue != "" && birthdayValue != undefined){
					$("input[name=birthday]").val(birthdayValue);
				}else{
					$("input[name=birthday]").val("");
				}
				if(gender!=null && gender != "" && gender != undefined){
					$("[id='gender']").val(gender);
					$("[id='gender']").prev("[id='s2id_gender']").find(".select2-chosen").html(genderDesc);
					$("[id='gender']").trigger('change');
				}else{
					$("[id='gender']").val("");
					$("[id='gender']").prev("[id='s2id_gender']").find(".select2-chosen").html("请选择...");
					$("[id='gender']").trigger('change');
				}
			} 
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/person/tenant/">租客信息列表</a></li>
		<li class="active"><a href="${ctx}/person/tenant/form?id=${tenant.id}">租客信息<shiro:hasPermission name="person:tenant:edit">${not empty tenant.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="person:tenant:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="tenant" action="${ctx}/person/tenant/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}" type="${messageType}"/>
		<div class="control-group">
			<label class="control-label">姓名：</label>
			<div class="controls">
				<form:input path="tenantName" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">证件类型：</label>
			<div class="controls">
				<form:select path="idType" class="input-xlarge required">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('id_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">证件号码：</label>
			<div class="controls">
				<form:input path="idNo" htmlEscape="false" maxlength="100" class="input-xlarge required" onblur="showBirthday()"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">性别：</label>
			<div class="controls">
				<form:select path="gender" class="input-xlarge required">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">出生日期：</label>
			<div class="controls">
				<input name="birthday" type="text" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${tenant.birthday}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">租客类型：</label>
			<div class="controls">
				<form:select path="tenantType" class="input-xlarge required">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('tenant_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">手机号码：</label>
			<div class="controls">
				<form:input path="cellPhone" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">学历：</label>
			<div class="controls">
				<form:select path="degrees" class="input-xlarge ">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('degrees')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">所属企业：</label>
			<div class="controls">
				<form:select path="company.id" class="input-xlarge">
					<form:option value="" label="请选择..."/>
					<form:options items="${listCompany}" itemLabel="companyName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">电子邮箱：</label>
			<div class="controls">
				<form:input path="email" htmlEscape="false" maxlength="64" class="input-xlarge email"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">户籍所在地：</label>
			<div class="controls">
				<form:input path="houseRegister" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">职位：</label>
			<div class="controls">
				<form:input path="position" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">跟进销售：</label>
			<div class="controls">
				<form:select path="user.id" class="input-xlarge">
					<form:option value="" label="请选择..."/>
					<form:options items="${listUser}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="person:tenant:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>