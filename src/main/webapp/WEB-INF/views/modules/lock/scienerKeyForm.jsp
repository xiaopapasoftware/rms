<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>科技侠门锁钥匙管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
					$("#btnSubmit").attr("disabled",true);
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

        function keyTypeChange() {
            if($("#keyType").val()=="0") {
                $("#startDate").attr("disabled","disabled");
                $("#startDate").val("");
                $("#startDate").prev("[id='s2id_startDate']").find(".select2-chosen").html("");
                $("#startDate").next("a").hide();
            } else {
                $("#startDate").removeAttr("disabled");
                $("#startDate").next("a").show();
            }
        };
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/lock/scienerLockKey/">钥匙列表</a></li>
		<li class="active"><a href="${ctx}/lock/scienerLockKey/form">分配钥匙<shiro:hasPermission name="person:neighborhoodContact:edit"></shiro:hasPermission><shiro:lacksPermission name="person:neighborhoodContact:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="key" action="${ctx}/lock/scienerLockKey/save" method="post" class="form-horizontal">

		<sys:message content="${message}" type="${messageType}"/>
		<div class="control-group">
			<label class="control-label">门锁：</label>
			<div class="controls">
				<form:select path="lockId" class="input-xlarge required">
					<form:option value="" label="请选择..."/>
                    <c:forEach items="${lockList}" var="item">
                        <form:option value="${item.room_id}" label="${item.lock_alias}"/>
                    </c:forEach>

				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
            <label class="control-label">用户：</label>
            <div class="controls">
                <form:select path="username" class="input-xlarge required">
                    <form:option value="" label="请选择..."/>
                    <form:options items="${users}" itemLabel="name" itemValue="scienerUserName" htmlEscape="false"/>
                </form:select>
                <span class="help-inline"><font color="red">*</font> </span>
            </div>
		</div>

		<div class="control-group">
            <label class="control-label">生效时间：</label>
            <div class="controls">
                <input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
                       value=""
                       onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
                <span class="help-inline"><font color="red">*</font> </span>
            </div>
		</div>
		<div class="control-group">
            <label class="control-label">	结束时间：</label>
            <div class="controls">
                <input name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
                       value=""
                       onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
                <span class="help-inline"><font color="red">*</font> </span>
            </div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remark" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>