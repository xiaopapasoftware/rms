<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>汇款人信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					var saveData = $("#inputForm").serialize();
					$.ajaxSetup({ cache: false });
					$.get("${ctx}/person/remittancer/ajaxSave", saveData, function(data){
						var json = eval("("+data+")");
						if(null!=json.message) top.$.jBox.tip(json.message,'warning');
						if(null!=json.id) {
							top.$.jBox.tip('保存成功!','success');
							var iframe;
							if(undefined == $(window.parent.document).find(".tab_content").html()) {
								iframe = $(window.parent.document).find("iframe")[0];
							} else {
								iframe = $(window.parent.document).find(".tab_content").find("iframe")[1];
							}
							iframe.contentWindow.$("[id='remittancer.id']").find("option").each(function(){
								if($(this).attr("selected")=="selected") {
									$(this).removeAttr("selected");
									return false;
								}
							});
							var text = iframe.contentWindow.$("[id='remittancer.id']").html();
							text = "<option value='"+json.id+"' selected='selected'>"+json.name+"</option>"+text;
							iframe.contentWindow.$("[id='remittancer.id']").html(text);
							iframe.contentWindow.$("[id='remittancer.id']").val(json.id);
							iframe.contentWindow.$("[id='remittancer.id']").prev("[id='s2id_remittancer.id']").find(".select2-chosen").html(json.name);
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
	<form:form id="inputForm" modelAttribute="remittancer" action="" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}" type="${messageType}"/>
		<div class="control-group">
			<label class="control-label">开户人姓名：</label>
			<div class="controls">
				<form:input path="userName" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开户行名称：</label>
			<div class="controls">
				<form:input path="bankName" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开户行账号：</label>
			<div class="controls">
				<form:input path="bankAccount" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
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