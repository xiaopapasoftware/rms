<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>协议变更</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function submitData() {
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
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
		}
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
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
			}else if(delFlag.val() == "1"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		}
		
		function addTenant() {
			top.$.jBox.open("iframe:${ctx}/person/tenant/add?type=tenant",'添加承租人',850,500,{buttons:{'保存':'1','关闭':'2'},submit:saveHandler});
		}
		
		function addLive() {
			top.$.jBox.open("iframe:${ctx}/person/tenant/add?type=live",'添加入住人',850,500,{buttons:{'保存':'1','关闭':'2'},submit:saveHandler});
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
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/contract/rentContract/">出租合同列表</a></li>
		<shiro:hasPermission name="contract:rentContract:edit"><li><a href="${ctx}/contract/rentContract/form">出租合同添加</a></li></shiro:hasPermission>
		<li class="active"><a href="javascript:void(0);">协议变更</a></li>
	</ul>
	<form:form id="inputForm" modelAttribute="agreementChange" action="${ctx}/contract/rentContract/saveAdditional" method="post" class="form-horizontal">
		<form:hidden path="contractId"/>
		<div class="control-group">
			<label class="control-label">合同变更协议名称：</label>
			<div class="controls">
				<form:input path="agreementChangeName" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">协议生效时间：</label>
			<div class="controls">
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${agreementChange.startDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">首付房租月数：</label>
			<div class="controls">
				<form:input path="renMonths" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房租押金月数：</label>
			<div class="controls">
				<form:input path="depositMonths" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">承租人：</label>
			<div class="controls">
				<form:select path="tenantList" class="input-xlarge required" multiple="true">
					<c:forEach items="${tenantList}" var="item">
						<form:option value="${item.id}">${item.cellPhone}-${item.tenantName}</form:option>
					</c:forEach>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
				<shiro:hasPermission name="contract:rentContract:edit"><a href="#" onclick="addTenant()">添加承租人</a></shiro:hasPermission>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">入住人：</label>
			<div class="controls">
				<form:select path="liveList" class="input-xlarge required" multiple="true">
					<c:forEach items="${tenantList}" var="item">
						<form:option value="${item.id}">${item.cellPhone}-${item.tenantName}</form:option>
					</c:forEach>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
				<shiro:hasPermission name="contract:rentContract:edit"><a href="#" onclick="addLive()">添加入住人</a></shiro:hasPermission>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" onclick="submitData()"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>