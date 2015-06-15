<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>承租合同管理</title>
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
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/contract/leaseContract/">承租合同列表</a></li>
		<li class="active">
			<a href="${ctx}/contract/leaseContract/form?id=${leaseContract.id}">承租合同
			<shiro:hasPermission name="contract:leaseContract:edit">
				<c:if test="${leaseContract.contractStatus=='0'||leaseContract.contractStatus=='2'||empty leaseContract.id}">
					${not empty leaseContract.id?'修改':'添加'}
				</c:if>
				<c:if test="${leaseContract.contractStatus=='1'}">
					<span>查看</span>
				</c:if>
			</shiro:hasPermission>
			<shiro:lacksPermission name="contract:leaseContract:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="leaseContract" action="${ctx}/contract/leaseContract/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">物业项目：</label>
			<div class="controls">
				<form:select path="propertyProject.id" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${projectList}" itemLabel="projectName" itemValue="id" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">楼宇：</label>
			<div class="controls">
				<form:select path="building.id" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${buildingList}" itemLabel="buildingName" itemValue="id" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房屋：</label>
			<div class="controls">
				<form:select path="house.id" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${houseList}" itemLabel="houseNo" itemValue="id" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">汇款人：</label>
			<div class="controls">
				<form:select path="remittancer.id" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${remittancerList}" itemLabel="userName" itemValue="id" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">承租合同名称：</label>
			<div class="controls">
				<form:input path="contractName" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合同生效时间：</label>
			<div class="controls">
				<input name="effectiveDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${leaseContract.effectiveDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">首次打款日期：</label>
			<div class="controls">
				<input name="firstRemittanceDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${leaseContract.firstRemittanceDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">打款日期：</label>
			<div class="controls">
				<form:select path="remittanceDate" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('remittance_date')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合同过期时间：</label>
			<div class="controls">
				<input name="expiredDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${leaseContract.expiredDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合同签订时间：</label>
			<div class="controls">
				<input name="contractDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${leaseContract.contractDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">承租押金：</label>
			<div class="controls">
				<form:input path="deposit" htmlEscape="false" class="input-xlarge required number"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">业主身份证：</label>
			<div class="controls">
				<form:hidden id="landlordId" path="landlordId" htmlEscape="false" maxlength="4000" class="input-xlarge"/>
				<sys:ckfinder input="landlordId" type="files" uploadPath="/0" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">委托证明：</label>
			<div class="controls">
				<form:hidden id="profile" path="profile" htmlEscape="false" maxlength="4000" class="input-xlarge"/>
				<sys:ckfinder input="profile" type="files" uploadPath="/1" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">业主房产证：</label>
			<div class="controls">
				<form:hidden id="certificate" path="certificate" htmlEscape="false" maxlength="4000" class="input-xlarge"/>
				<sys:ckfinder input="certificate" type="files" uploadPath="/2" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
				<label class="control-label">承租价格：</label>
				<div class="controls">
					<table id="contentTable" class="table table-striped table-bordered table-condensed">
						<thead>
							<tr>
								<th class="hide"></th>
								<th>开始日期</th>
								<th>结束日期</th>
								<th>承租价格</th>
								<shiro:hasPermission name="contract:leaseContract:edit"><th width="10">&nbsp;</th></shiro:hasPermission>
							</tr>
						</thead>
						<tbody id="leaseContractDtlList">
						</tbody>
						<shiro:hasPermission name="contract:leaseContract:edit"><tfoot>
							<tr><td colspan="4"><a href="javascript:" onclick="addRow('#leaseContractDtlList', leaseContractDtlRowIdx, leaseContractDtlTpl);leaseContractDtlRowIdx = leaseContractDtlRowIdx + 1;" class="btn">新增</a></td></tr>
						</tfoot></shiro:hasPermission>
					</table>
					<script type="text/template" id="leaseContractDtlTpl">//<!--
						<tr id="leaseContractDtlList{{idx}}">
							<td class="hide">
								<input id="leaseContractDtlList{{idx}}_id" name="leaseContractDtlList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
								<input id="leaseContractDtlList{{idx}}_delFlag" name="leaseContractDtlList[{{idx}}].delFlag" type="hidden" value="0"/>
							</td>
							<td>
								<input id="leaseContractDtlList{{idx}}_startDate" name="leaseContractDtlList[{{idx}}].startDate" type="text" readonly="readonly" value="{{row.startDate}}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" class="input-medium Wdate required"/>
								<span class="help-inline"><font color="red">*</font> </span>
							</td>
							<td>
								<input id="leaseContractDtlList{{idx}}_endDate" name="leaseContractDtlList[{{idx}}].endDate" type="text" readonly="readonly" value="{{row.endDateStr}}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" class="input-medium Wdate required"/>
								<span class="help-inline"><font color="red">*</font> </span>
							</td>
							<td>
								<input id="leaseContractDtlList{{idx}}_deposit" name="leaseContractDtlList[{{idx}}].deposit" type="text" value="{{row.deposit}}" maxlength="255" class="input-small required number"/>
								<span class="help-inline"><font color="red">*</font> </span>
							</td>
							<shiro:hasPermission name="contract:leaseContract:edit"><td class="text-center" width="10">
								{{#delBtn}}<span class="close" onclick="delRow(this, '#leaseContractDtlList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
							</td></shiro:hasPermission>
						</tr>//-->
					</script>
					<script type="text/javascript">
						var leaseContractDtlRowIdx = 0, leaseContractDtlTpl = $("#leaseContractDtlTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
						$(document).ready(function() {
							var data = ${fns:toJson(leaseContract.leaseContractDtlList)};
							for (var i=0; i<data.length; i++){
								addRow('#leaseContractDtlList', leaseContractDtlRowIdx, leaseContractDtlTpl, data[i]);
								leaseContractDtlRowIdx = leaseContractDtlRowIdx + 1;
							}
						});
					</script>
				</div>
			</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="contract:leaseContract:edit">
				<c:if test="${leaseContract.contractStatus=='0' || leaseContract.contractStatus=='2' || empty leaseContract.id}">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
				</c:if>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>