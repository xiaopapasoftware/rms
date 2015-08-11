<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>承租合同管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#contractName, #deposit").keypress(function(event) {
		        if (event.keyCode == 13) {
		            event.preventDefault();
		        }
		    });
			$("input[name$='Date']").keypress(function(event) {
		        if (event.keyCode == 13) {
		            event.preventDefault();
		        }
		    });
			$("#inputForm").validate({
				submitHandler: function(form){
					if($("#leaseContractDtlList").find("tr").length==0) {
						top.$.jBox.tip('请录入月承租价格.','warning');
						return;
					}
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
		
		function changeProject() {
			var project = $("[id='propertyProject.id']").val();
			
			//把物业项目简称带入房屋编号中
			var projectSimpleName= $("[id='propertyProject.id']").find("option:selected").attr("projectSimpleName");
			if(projectSimpleName==null || projectSimpleName =="" || projectSimpleName==undefined){
				$("#contractCode").val("${leaseContract.contractCode}");
			}else{
				$("#contractCode").val(projectSimpleName + "-" + "${leaseContract.contractCode}");
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
			
			$("[id='house.id']").html(html);
			$("[id='house.id']").val("");
			$("[id='house.id']").prev("[id='s2id_house.id']").find(".select2-chosen").html("请选择...");
			
			//更新合同名称
			changeContractName();
		}
		
		function buildingChange() {
			var building = $("[id='building.id']").val();
			var html = "<option value='' selected='selected'>请选择...</option>";
			if("" != building) {
				$.ajaxSetup({ cache: false });
				$.get("${ctx}/inventory/house/findList?id=" + building, function(data){
					for(var i=0;i<data.length;i++) {
						html += "<option value='"+data[i].id+"'>"+data[i].houseNo+"</option>";
					}
					$("[id='house.id']").html(html);
				});
			} else {
				$("[id='house.id']").html(html);
			}
			$("[id='house.id']").val("");
			$("[id='house.id']").prev("[id='s2id_house.id']").find(".select2-chosen").html("请选择...");
			
			//更新合同名称
			changeContractName();
		}
		
		function houseChange() {
			//更新合同名称
			changeContractName();
		}
		
		//更新合同名称
		function changeContractName(){
			var contractName = "";
			
			var porjectId = $("[id='propertyProject.id']").find("option:selected").val();
			if(porjectId != null && porjectId != undefined && porjectId != ""){
				var projectName = $("[id='propertyProject.id']").find("option:selected").text();
				contractName = contractName + projectName;
			}
			
			
			var buildingId = $("[id='building.id']").find("option:selected").val();
			if(buildingId != null && buildingId != undefined && buildingId != ""){
				var buildingName = $("[id='building.id']").find("option:selected").text();
				if(contractName == ""){
					contractName = contractName + buildingName;
				}else{
					contractName = contractName + "-" + buildingName;
				}
			}
			
			var houseId = $("[id='house.id']").find("option:selected").val();
			if(houseId != null && houseId != undefined && houseId != ""){
				var houseNo = $("[id='house.id']").find("option:selected").text();
				if(contractName == ""){
					contractName = contractName + houseNo;
				}else{
					contractName = contractName + "-" + houseNo;
				}
			}
			
			var roomId = $("[id='room.id']").find("option:selected").val();
			if(roomId != null && roomId != undefined && roomId != ""){
				var roomNo = $("[id='room.id']").find("option:selected").text();
				if(contractName == ""){
					contractName = contractName + roomNo;
				}else{
					contractName = contractName + "-" + roomNo;
				}
			}
			
			$("#contractName").val(contractName);
		}
		
		function toAudit(id) {
			var html = "<table style='margin:20px;'><tr><td><label>审核意见：</label></td><td><textarea id='auditMsg'></textarea></td></tr></table>";
			var content = {
		    	state1:{
					content: html,
				    buttons: { '同意': 1, '拒绝':2, '取消': 0 },
				    buttonsFocus: 0,
				    submit: function (v, h, f) {
				    	if (v == 0) {
				        	return true; // close the window
				        } else if(v==1){
				        	saveAudit(id,'1');
				        } else if(v==2){
				        	saveAudit(id,'2');
				        }
				        return false;
				    }
				}
			};
			$.jBox.open(content,"审核",350,220,{});
		}
		
		function saveAudit(id,status) {
			loading('正在提交，请稍等...');
			var msg = $("#auditMsg").val();
			window.location.href="${ctx}/contract/leaseContract/audit?objectId="+id+"&auditMsg="+msg+"&auditStatus="+status;
		}
		
		function addProject() {
			top.$.jBox.open("iframe:${ctx}/inventory/propertyProject/add",'添加物业项目',850,500,{buttons:{'保存':'1','关闭':'2'},submit:saveHandler});
		}
		
		function addBuilding() {
			if(""==$("[id='propertyProject.id']").val()) {
				top.$.jBox.tip('请选择物业项目.','warning');
				return;
			}
			top.$.jBox.open("iframe:${ctx}/inventory/building/add?propertyProject.id="+$("[id='propertyProject.id']").val(),'添加楼宇',850,500,{buttons:{'保存':'1','关闭':'2'},submit:saveHandler});
		}
		
		function addHouse() {
			if(""==$("[id='building.id']").val()) {
				top.$.jBox.tip('请选择楼宇.','warning');
				return;
			}
			top.$.jBox.open("iframe:${ctx}/inventory/house/add?building.id="+$("[id='building.id']").val()+"&propertyProject.id="+$("[id='propertyProject.id']").val(),'添加房屋',850,500,{buttons:{'保存':'1','关闭':'2'},submit:saveHandler});
		}
		
		function addRemittancer() {
			top.$.jBox.open("iframe:${ctx}/person/remittancer/add",'添加汇款人',850,500,{buttons:{'保存':'1','关闭':'2'},submit:saveHandler});
		}
		
		function saveHandler(v,h,f) {
			if(v=='1') {
				h.find("iframe")[0].contentWindow.$("#inputForm").submit();
				return false;
			}
		}
		
		function effectiveDateChange() {
			var startDate = new Date($("input[name='effectiveDate']").val());
			var endDate = new Date(Date.parse(startDate));
			endDate.setFullYear(endDate.getFullYear()+6);
			endDate.setMonth(endDate.getMonth());
			endDate.setDate(endDate.getDate()-1);
			var year = endDate.getFullYear();
			var month = endDate.getMonth()+1;
			if(parseFloat(month)<10)
				month = "0"+""+month;
			var day = endDate.getDate();
			if(parseFloat(day)<10)
				day = "0"+""+day;
			var expiredDate=year+"-"+month+"-"+day;
			$("input[name='expiredDate']").val(expiredDate);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/contract/leaseContract/">承租合同列表</a></li>
		<li class="active">
			<a href="#">承租合同
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
		<form:hidden path="type"/>
		<sys:message content="${message}" type="${messageType}"/>
		<div class="control-group">
			<label class="control-label">承租合同编号：</label>
			<div class="controls">
				<form:input path="contractCode" htmlEscape="false" maxlength="100" class="input-xlarge" readonly="true"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">承租合同名称：</label>
			<div class="controls">
				<form:input path="contractName" htmlEscape="false" maxlength="100" class="input-xlarge required" readonly="true"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">物业项目：</label>
			<div class="controls">
				<form:select path="propertyProject.id" class="input-xlarge required" onchange="changeProject()">
					<form:option value="" label="请选择..."/>
					<c:forEach items="${projectList}" var="item">
						<form:option projectSimpleName="${item.projectSimpleName}" value="${item.id}">${item.projectName}</form:option>
					</c:forEach>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
				<shiro:hasPermission name="contract:leaseContract:edit"><a href="#" onclick="addProject()">添加物业项目</a></shiro:hasPermission>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">楼宇：</label>
			<div class="controls">
				<form:select path="building.id" class="input-xlarge required" onchange="buildingChange()">
					<form:option value="" label="请选择..."/>
					<form:options items="${buildingList}" itemLabel="buildingName" itemValue="id" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
				<shiro:hasPermission name="contract:leaseContract:edit"><a href="#" onclick="addBuilding()">添加楼宇</a></shiro:hasPermission>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房屋：</label>
			<div class="controls">
				<form:select path="house.id" class="input-xlarge required" onchange="houseChange()">
					<form:option value="" label="请选择..."/>
					<form:options items="${houseList}" itemLabel="houseNo" itemValue="id" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
				<shiro:hasPermission name="contract:leaseContract:edit"><a href="#" onclick="addHouse()">添加房屋</a></shiro:hasPermission>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">汇款人：</label>
			<div class="controls">
				<form:select path="remittancer.id" class="input-xlarge required">
					<form:option value="" label="请选择..."/>
					<form:options items="${remittancerList}" itemLabel="userName" itemValue="id" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
				<shiro:hasPermission name="contract:leaseContract:edit"><a href="#" onclick="addRemittancer()">添加汇款人</a></shiro:hasPermission>
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
			<label class="control-label">合同起始时间：</label>
			<div class="controls">
				<input name="effectiveDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${leaseContract.effectiveDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" onchange="effectiveDateChange()"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合同结束时间：</label>
			<div class="controls">
				<input name="expiredDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${leaseContract.expiredDate}" pattern="yyyy-MM-dd"/>"
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
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('remittance_date')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">打款月份间隔：</label>
			<div class="controls">
				<form:input path="monthSpace" htmlEscape="false" maxlength="100" class="input-xlarge required digits"/>
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
				<sys:ckfinder input="landlordId" type="files" uploadPath="/业主身份证" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">委托证明：</label>
			<div class="controls">
				<form:hidden id="profile" path="profile" htmlEscape="false" maxlength="4000" class="input-xlarge"/>
				<sys:ckfinder input="profile" type="files" uploadPath="/委托证明" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">业主房产证：</label>
			<div class="controls">
				<form:hidden id="certificate" path="certificate" htmlEscape="false" maxlength="4000" class="input-xlarge"/>
				<sys:ckfinder input="certificate" type="files" uploadPath="/业主房产证" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">动迁协议：</label>
			<div class="controls">
				<form:hidden id="relocation" path="relocation" htmlEscape="false" maxlength="4000" class="input-xlarge"/>
				<sys:ckfinder input="relocation" type="files" uploadPath="/动迁协议" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
				<label class="control-label">月承租价格：</label>
				<div class="controls">
					<table id="contentTable" class="table table-striped table-bordered table-condensed">
						<thead>
							<tr>
								<th class="hide"></th>
								<th>开始日期</th>
								<th>结束日期</th>
								<th>月承租价格</th>
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
			<shiro:hasPermission name="contract:leaseContract:audit">
			<c:if test="${leaseContract.contractStatus=='0'}">
				<input id="btnSubmit" class="btn btn-primary" type="button" value="审 核" onclick="toAudit('${leaseContract.id}')"/>
			</c:if>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>