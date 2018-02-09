<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>承租入住人管理表单</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
            top.$.jBox.tip.mess = null;
			$(".tanants").select2({
			    ajax: {
				    url: "${ctx}/person/tenant/syncAjaxQuery",
				    dataType: 'json',
				    delay: 250,
				    data: function (params) {
				      return {
				        q: params.term,
				        page: params.page
				      };
				    },
				    processResults: function (data, params) {
				      params.page = params.page || 1;
				      return {
				        results: data,
				        pagination: {
				          more: (params.page * 30) < data.total_count
				        }
				      };
				    },
				    cache: true
				  },
				  placeholder: "请选择",
				  allowClear: true,
				  escapeMarkup: function (markup) { return markup; },
				  minimumInputLength: 1,
				  templateResult: function (repo) { return repo.text;}
 			});
			
			$.ajax({
				  type: 'GET',
				  url: "${ctx}/contract/rentContract/ajaxQueryLivedTenants?rentContractId="+$("#id").val(),
				  dataType: 'TEXT'
			}).then(function(data){
				  $("#liveList").append(data);
				  $("#liveList").removeData();
				  $("#liveList").trigger('change');
			});

			$.ajax({
				  type: 'GET',
				  url: "${ctx}/contract/rentContract/ajaxQueryLeasedTenants?rentContractId="+$("#id").val(),
				  dataType: 'TEXT'
			}).then(function(data){
				  $("#tenantList").append(data);
				  $("#tenantList").removeData();
				  $("#tenantList").trigger('change');
			});
		});
			function submitData() {
				$("#inputForm").validate({
					submitHandler: function(form){//单间
						loading('正在提交，请稍等...');
						$("#saveBtn").attr("disabled",true);
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

		function changeLiveList(rentContractId){//更改入住人列表
		    $.post("${ctx}/contract/rentContract/changeLiveList", {'rentContractId':rentContractId, 'liveIds':$.makeArray($("#liveList").val()).join()});
		}
		
		function changeTenantList(rentContractId){//更改承租人列表
		    $.post("${ctx}/contract/rentContract/changeTenantList", {'rentContractId':rentContractId, 'tenantIds':$.makeArray($("#tenantList").val()).join()});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/person/tanentLiveMgt/">出租合同列表</a></li>
		<li class="active">
			<a href="${ctx}/person/tanentLiveMgt/form?id=${rentContract.id}">
				<shiro:hasPermission name="person:tanentLiveMgt:edit">修改承租人/入住人</shiro:hasPermission>
			</a>
		</li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="rentContract" action="${ctx}/contract/rentContract/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}" type="${messageType}"/>
		<div class="control-group">
			<label class="control-label">合同编号：</label>
			<div class="controls">
				<form:input path="contractCode" htmlEscape="false" maxlength="100" class="input-xlarge" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合同名称：</label>
			<div class="controls">
				<form:input path="contractName" htmlEscape="false" maxlength="100" class="input-xlarge required" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">承租人：</label>
			<div class="controls">
				<form:select path="tenantList" style="width:450px;" class="input-xlarge required tanants" multiple="true" onchange="changeTenantList('${rentContract.id}');">
					<option></option>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
				<shiro:hasPermission name="person:tanentLiveMgt:edit"><a href="#" onclick="addTenant()">添加承租人</a></shiro:hasPermission>
			</div>
		</div>	
		<div class="control-group">
			<label class="control-label">入住人：</label>
			<div class="controls">
				<form:select path="liveList" style="width:450px;" class="input-xlarge required tanants" multiple="true" onchange="changeLiveList('${rentContract.id}');">
				    <option></option>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
				<shiro:hasPermission name="person:tanentLiveMgt:edit"><a href="#" onclick="addLive()">添加入住人</a></shiro:hasPermission>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>