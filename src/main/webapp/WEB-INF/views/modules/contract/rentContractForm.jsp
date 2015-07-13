<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>出租合同管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
		});
		
		function submitData() {
			$("#inputForm").validate({
				submitHandler: function(form){
					if($("#rentMode").val()!="0" && $("[id='room.id']").val()=="") {
						top.$.jBox.tip('请选择房间.','warning');
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
		}
		
		function saveData() {
			$("#contractStatus").val("0");
			$("#validatorFlag").val("0");
			$("#inputForm").submit();
		}
		
		function changeProject() {
			var project = $("[id='propertyProject.id']").val();
			var html = "<option value='' selected='selected'>请选择...</option>";
			if("" != project) {
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
			
			$("[id='room.id']").html(html);
			$("[id='room.id']").val("");
			$("[id='room.id']").prev("[id='s2id_room.id']").find(".select2-chosen").html("请选择...");
		}
		
		function buildingChange() {
			var building = $("[id='building.id']").val();
			var html = "<option value='' selected='selected'>请选择...</option>";
			if("" != building) {
				$.get("${ctx}/inventory/house/findList?id=" + building+"&choose=1", function(data){
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
			
			$("[id='room.id']").html(html);
			$("[id='room.id']").val("");
			$("[id='room.id']").prev("[id='s2id_room.id']").find(".select2-chosen").html("请选择...");
		}
		
		function houseChange() {
			var room = $("[id='house.id']").val();
			var html = "<option value='' selected='selected'>请选择...</option>";
			if("" != room) {
				$.get("${ctx}/inventory/room/findList?id=" + room+"&choose=1", function(data){
					for(var i=0;i<data.length;i++) {
						html += "<option value='"+data[i].id+"'>"+data[i].roomNo+"</option>";
					}
					$("[id='room.id']").html(html);
				});
			} else {
				$("[id='room.id']").html(html);
			}
			$("[id='room.id']").val("");
			$("[id='room.id']").prev("[id='s2id_room.id']").find(".select2-chosen").html("请选择...");
		}
		
		function rentModeChange() {
			if($("#rentMode").val()=="0") {
				$("[id='room.id']").attr("disabled","disabled");
				$("[id='room.id']").val("");
				$("[id='room.id']").prev("[id='s2id_room.id']").find(".select2-chosen").html("请选择...");
			} else {
				$("[id='room.id']").removeAttr("disabled");
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/contract/rentContract/">出租合同列表</a></li>
		<li class="active">
		<a href="${ctx}/contract/rentContract/form?id=${rentContract.id}">
			出租合同<shiro:hasPermission name="contract:rentContract:edit"><c:if test="${rentContract.contractStatus=='0' || rentContract.contractStatus=='3'}">修改</c:if><c:if test="${rentContract.contractStatus!='0' && rentContract.contractStatus!='3'}">${not empty rentContract.id?'查看':'添加'}</c:if></shiro:hasPermission><shiro:lacksPermission name="contract:rentContract:edit">查看</shiro:lacksPermission>
		</a>
		</li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="rentContract" action="${ctx}/contract/rentContract/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="contractStatus" value="1"/>
		<form:hidden path="validatorFlag" value="1"/>
		<form:hidden path="saveSource" value="0"/>
		<form:hidden path="contractId"/>
		<form:hidden path="signType"/>
		<sys:message content="${message}" type="${messageType}"/>
		<div class="control-group">
			<label class="control-label">合同来源：</label>
			<div class="controls">
				<form:select path="contractSource" class="input-xlarge required">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('contract_source')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合同名称：</label>
			<div class="controls">
				<form:input path="contractName" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">出租方式：</label>
			<div class="controls">
				<form:select path="rentMode" class="input-xlarge required" onchange="rentModeChange()">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('rent_mode')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">物业项目：</label>
			<div class="controls">
				<form:select path="propertyProject.id" class="input-xlarge required" onchange="changeProject()">
					<form:option value="" label="请选择..."/>
					<form:options items="${projectList}" itemLabel="projectName" itemValue="id" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
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
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房间：</label>
			<div class="controls">
				<form:select path="room.id" class="input-xlarge">
					<form:option value="" label="请选择..."/>
					<form:options items="${roomList}" itemLabel="roomNo" itemValue="id" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">月租金：</label>
			<div class="controls">
				<form:input path="rental" htmlEscape="false" class="input-xlarge required number"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房租押金金额：</label>
			<div class="controls">
				<form:input path="depositAmount" htmlEscape="false" class="input-xlarge  number required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">首付房租月数：</label>
			<div class="controls">
				<form:input path="renMonths" htmlEscape="false" maxlength="11" class="input-xlarge  digits required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房租押金月数：</label>
			<div class="controls">
				<form:input path="depositMonths" htmlEscape="false" maxlength="11" class="input-xlarge  digits required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">水电押金金额：</label>
			<div class="controls">
				<form:input path="depositElectricAmount" htmlEscape="false" class="input-xlarge  number required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">付费方式：</label>
			<div class="controls">
				<form:select path="chargeType" class="input-xlarge required">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('charge_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合同生效时间：</label>
			<div class="controls">
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${rentContract.startDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合同过期时间：</label>
			<div class="controls">
				<input name="expiredDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${rentContract.expiredDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合同签订时间：</label>
			<div class="controls">
				<input name="signDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${rentContract.signDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">续租提醒时间：</label>
			<div class="controls">
				<input name="remindTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${rentContract.remindTime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合作人：</label>
			<div class="controls">
				<form:select path="partner.id" class="input-xlarge">
					<form:option value="" label="请选择..."/>
					<form:options items="${partnerList}" itemLabel="partnerName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">销售：</label>
			<div class="controls">
				<sys:treeselect id="user" name="user.id" value="${rentContract.user.id}" labelName="user.name" labelValue="${rentContract.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="" allowClear="true" notAllowSelectParent="true"/>
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
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否开通有线电视：</label>
			<div class="controls">
				<form:select path="hasTv" class="input-xlarge ">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">有线电视每月费用：</label>
			<div class="controls">
				<form:input path="tvFee" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否开通宽带：</label>
			<div class="controls">
				<form:select path="hasNet" class="input-xlarge ">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">每月宽带费用：</label>
			<div class="controls">
				<form:input path="netFee" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合租每月水费：</label>
			<div class="controls">
				<form:input path="waterFee" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">服务费比例(%)：</label>
			<div class="controls">
				<form:input path="serviceFee" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否需办理居住证及落户：</label>
			<div class="controls">
				<form:select path="hasVisa" class="input-xlarge ">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">入住分电表系数：</label>
			<div class="controls">
				<form:input path="meterValue" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">入住总电表系数：</label>
			<div class="controls">
				<form:input path="totalMeterValue" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">入住峰电系数：</label>
			<div class="controls">
				<form:input path="peakMeterValue" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">入住平电系数：</label>
			<div class="controls">
				<form:input path="flatMeterValue" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">入住谷电系数：</label>
			<div class="controls">
				<form:input path="valleyMeterValue" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">入住煤表系数：</label>
			<div class="controls">
				<form:input path="coalValue" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">入住水表系数：</label>
			<div class="controls">
				<form:input path="waterValue" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="contract:rentContract:edit">
				<c:if test="${rentContract.contractStatus=='0' || rentContract.contractStatus=='3'}">
				<input id="saveBtn" class="btn btn-primary" type="button" value="暂 存" onclick="saveData()"/>&nbsp;
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" onclick="submitData()"/>&nbsp;
				</c:if>
				<c:if test="${empty rentContract.id}">
				<input id="saveBtn" class="btn btn-primary" type="button" value="暂 存" onclick="saveData()"/>&nbsp;
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" onclick="submitData()"/>&nbsp;
				</c:if>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>