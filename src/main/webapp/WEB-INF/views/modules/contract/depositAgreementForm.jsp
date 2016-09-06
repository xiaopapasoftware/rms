<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>定金协议管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#agreementName, #renMonths  , #depositMonths  , #depositAmount , #housingRent ,#userName").keypress(function(event) {
		        if (event.keyCode == 13) {
		            event.preventDefault();
		        }
		    });
			$("input[name$='Date']").keypress(function(event) {
		        if (event.keyCode == 13) {
		            event.preventDefault();
		        }
		    });
			
			var idVal = $("#id").val();
			if(idVal == null || idVal == "" || idVal == undefined){//新增，而不是修改
				var curDate = new Date();			
				var year = curDate.getFullYear();
				var month = curDate.getMonth() + 1;
				if(parseFloat(month)<10){
					month = "0" + "" + month;
				}
				var day = curDate.getDate();
				if(parseFloat(day)<10){
					day = "0" + "" + day;
				}
				var curDateStyle = year + "-" + month + "-" + day;//当前时间
				
				$("input[name='signDate']").val(curDateStyle);//协议签订时间，默认为当前日期
				$("input[name='agreementDate']").val(curDateStyle);//约定合同签约时间，默认为当前日期
				$("input[name='startDate']").val(curDateStyle);//合同开始时间，默认为当前日期
				
				curDate.setFullYear(curDate.getFullYear()+1);
				curDate.setDate(curDate.getDate()-1);
				var year2 = curDate.getFullYear();
				var month2 = curDate.getMonth() + 1;
				if(parseFloat(month2)<10){
					month2 = "0" + "" + month2;
				}
				var day2 = curDate.getDate();
				if(parseFloat(day2)<10){
					day2 = "0" + "" + day2;
				}
				var curDateStyle2 = year2 + "-" + month2 + "-" + day2;
				$("input[name='expiredDate']").val(curDateStyle2);//合同结束时间，默认为当前日期，一年后的日期 减去1天
			}
		});
		
		function submitData() {
			$("#inputForm").validate({
				submitHandler: function(form){
					if($("#rentMode").val()!="0" && $("[id='room.id']").val()=="") {
						top.$.jBox.tip('请选择房间.','warning');
						return;
					}
					loading('正在提交，请稍等...');
					$("#btnSubmit").attr("disabled",true); 
					$("#saveBtn").attr("disabled",true); 
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
			$("#agreementStatus").val("6");
			$("#validatorFlag").val("0");
			$("#inputForm").submit();
			$("#btnSubmit").attr("disabled",true); 
			$("#saveBtn").attr("disabled",true); 
		}
		
		function changeProject() {
			var project = $("[id='propertyProject.id']").val();
			
			//把物业项目简称带入房屋编号中
			var projectSimpleName= $("[id='propertyProject.id']").find("option:selected").attr("projectSimpleName");
			if(projectSimpleName==null || projectSimpleName =="" || projectSimpleName==undefined){
				$("#agreementCode").val("${depositAgreement.agreementCode}");
			}else{
				$("#agreementCode").val(projectSimpleName + "-" + "${depositAgreement.agreementCode}");
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
			
			$("[id='room.id']").html(html);
			$("[id='room.id']").val("");
			$("[id='room.id']").prev("[id='s2id_room.id']").find(".select2-chosen").html("请选择...");
			
			changeAgreementName();
		}
		
		function buildingChange() {
			var building = $("[id='building.id']").val();
			var html = "<option value='' selected='selected'>请选择...</option>";
			if("" != building) {
				$.ajaxSetup({ cache: false });
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
			
			changeAgreementName();
		}
		
		function houseChange() {
			var room = $("[id='house.id']").val();
			var html = "<option value='' selected='selected'>请选择...</option>";
			if("" != room) {
				$.ajaxSetup({ cache: false });
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
			
			changeAgreementName();
		}
		
		function roomChange() {
			changeAgreementName();
		}
		
		//更新定金协议名称
		function changeAgreementName(){
			var agreementName = "";
			
			var porjectId = $("[id='propertyProject.id']").find("option:selected").val();
			if(porjectId != null && porjectId != undefined && porjectId != ""){
				var projectName = $("[id='propertyProject.id']").find("option:selected").text();
				agreementName = agreementName + projectName;
			}
			
			
			var buildingId = $("[id='building.id']").find("option:selected").val();
			if(buildingId != null && buildingId != undefined && buildingId != ""){
				var buildingName = $("[id='building.id']").find("option:selected").text();
				if(agreementName == ""){
					agreementName = agreementName + buildingName;
				}else{
					agreementName = agreementName + "-" + buildingName;
				}
			}
			
			var houseId = $("[id='house.id']").find("option:selected").val();
			if(houseId != null && houseId != undefined && houseId != ""){
				var houseNo = $("[id='house.id']").find("option:selected").text();
				if(agreementName == ""){
					agreementName = agreementName + houseNo;
				}else{
					agreementName = agreementName + "-" + houseNo;
				}
			}
			
			var roomId = $("[id='room.id']").find("option:selected").val();
			if(roomId != null && roomId != undefined && roomId != ""){
				var roomNo = $("[id='room.id']").find("option:selected").text();
				if(agreementName == ""){
					agreementName = agreementName + roomNo;
				}else{
					agreementName = agreementName + "-" + roomNo;
				}
			}
			
			$("#agreementName").val(agreementName);
		}
		
		function rentModeChange() {
			if($("#rentMode").val()=="0") {
				$("[id='room.id']").attr("disabled","disabled");
				$("[id='room.id']").val("");
				$("[id='room.id']").prev("[id='s2id_room.id']").find(".select2-chosen").html("");
				$("[id='room.id']").next("a").hide();
			} else {
				$("[id='room.id']").removeAttr("disabled");
				$("[id='room.id']").next("a").show();
			}
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
			window.location.href="${ctx}/contract/depositAgreement/audit?objectId="+id+"&auditMsg="+msg+"&auditStatus="+status;
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
			top.$.jBox.open("iframe:${ctx}/inventory/house/add?building.id="+$("[id='building.id']").val()+"&propertyProject.id="+$("[id='propertyProject.id']").val()+"&houseStatus=1",'添加房屋',850,500,{buttons:{'保存':'1','关闭':'2'},submit:saveHandler});
		}
		
		function addRoom() {
			if(""==$("[id='house.id']").val()) {
				top.$.jBox.tip('请选择房屋.','warning');
				return;
			}
			top.$.jBox.open("iframe:${ctx}/inventory/room/add?building.id="+$("[id='building.id']").val()+"&propertyProject.id="+$("[id='propertyProject.id']").val()+"&house.id="+$("[id='house.id']").val()+"&roomStatus=1",'添加房间',850,500,{buttons:{'保存':'1','关闭':'2'},submit:saveHandler});
		}
		
		function addTenant() {
			top.$.jBox.open("iframe:${ctx}/person/tenant/add?type=tenant",'添加承租人',850,500,{buttons:{'保存':'1','关闭':'2'},submit:saveHandler});
		}
		
		function saveHandler(v,h,f) {
			if(v=='1') {
				h.find("iframe")[0].contentWindow.$("#inputForm").submit();
				return false;
			}
		}
		
		function startDateChange() {
			var startDate = new Date($("input[name='startDate']").val());
			var oriEndDate = new Date(Date.parse(startDate));
			oriEndDate.setFullYear(oriEndDate.getFullYear()+1);
			oriEndDate.setDate(oriEndDate.getDate()-1);
			var year2 = oriEndDate.getFullYear();
			var month2 = oriEndDate.getMonth() + 1;
			if(parseFloat(month2)<10){
				month2 = "0" + "" + month2;
			}
			var day2 = oriEndDate.getDate();
			if(parseFloat(day2)<10){
				day2 = "0" + "" + day2;
			}
			var curDateStyle2 = year2 + "-" + month2 + "-" + day2;
			$("input[name='expiredDate']").val(curDateStyle2);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/contract/depositAgreement/">定金协议列表</a></li>
		<li class="active">
		<a href="${ctx}/contract/depositAgreement/form?id=${depositAgreement.id}">
		<shiro:hasPermission name="contract:depositAgreement:edit">定金协议<c:if test="${depositAgreement.agreementStatus=='2' || depositAgreement.agreementStatus=='6' || empty depositAgreement.id}">${not empty depositAgreement.id?'修改':'添加'}</c:if><c:if test="${depositAgreement.agreementStatus!='2' &&depositAgreement.agreementStatus!='6' && not empty depositAgreement.id}">查看</c:if></shiro:hasPermission>
		<shiro:lacksPermission name="contract:depositAgreement:edit">查看</shiro:lacksPermission>
		</a>
		</li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="depositAgreement" action="${ctx}/contract/depositAgreement/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="agreementStatus" value="0"/><!-- 录入完成到账收据待登记 -->
		<form:hidden path="validatorFlag" value="1"/><!-- 保存 -->
		<sys:message content="${message}" type="${messageType}"/>
		<div class="control-group">
			<label class="control-label">定金协议编号：</label>
			<div class="controls">
				<form:input path="agreementCode" htmlEscape="false" maxlength="100" class="input-xlarge" readonly="true"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">定金协议名称：</label>
			<div class="controls">
				<form:input path="agreementName" htmlEscape="false" maxlength="100" class="input-xlarge required" readonly="true"/>
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
					<c:forEach items="${projectList}" var="item">
						<form:option projectSimpleName="${item.projectSimpleName}" value="${item.id}">${item.projectName}</form:option>
					</c:forEach>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
				<shiro:hasPermission name="contract:depositAgreement:edit"><a href="#" onclick="addProject()">添加物业项目</a></shiro:hasPermission>
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
				<shiro:hasPermission name="contract:depositAgreement:edit"><a href="#" onclick="addBuilding()">添加楼宇</a></shiro:hasPermission>
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
				<shiro:hasPermission name="contract:depositAgreement:edit"><a href="#" onclick="addHouse()">添加房屋</a></shiro:hasPermission>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房间：</label>
			<div class="controls">
				<form:select path="room.id" class="input-xlarge" onchange="roomChange()">
					<form:option value="" label="请选择..."/>
					<form:options items="${roomList}" itemLabel="roomNo" itemValue="id" htmlEscape="false"/>
				</form:select>
				<shiro:hasPermission name="contract:depositAgreement:edit"><a href="#" onclick="addRoom()">添加房间</a></shiro:hasPermission>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">承租人：</label>
			<div class="controls">
				<form:select path="tenantList" class="input-xlarge required" multiple="true">
					<form:options items="${tenantList}" itemValue="id" itemLabel="label"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font></span>
				<shiro:hasPermission name="contract:depositAgreement:edit"><a href="#" onclick="addTenant()">添加承租人</a></shiro:hasPermission>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">定金金额：</label>
			<div class="controls">
				<form:input path="depositAmount" htmlEscape="false" class="input-xlarge required number"/>
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房屋租金：</label>
			<div class="controls">
				<form:input path="housingRent" htmlEscape="false" class="input-xlarge required number"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">协议签订时间：</label>
			<div class="controls">
				<input name="signDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${depositAgreement.signDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">约定合同签约时间：</label>
			<div class="controls">
				<input name="agreementDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${depositAgreement.agreementDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合同开始时间：</label>
			<div class="controls">
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${depositAgreement.startDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" onchange="startDateChange()"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合同结束时间：</label>
			<div class="controls">
				<input name="expiredDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${depositAgreement.expiredDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">首付房租月数：</label>
			<div class="controls">
				<form:input path="renMonths" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房租押金月数：</label>
			<div class="controls">
				<form:input path="depositMonths" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">销售：</label>
			<div class="controls">
				<sys:treeselect id="user" name="user.id" value="${depositAgreement.user.id}" labelName="user.name" labelValue="${depositAgreement.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="" allowClear="true" notAllowSelectParent="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">定金协议：</label>
			<div class="controls">
				<c:if test="${'2'==depositAgreement.dataSource && not empty depositAgreement.agreementBusiStatus}">
					<a target="_blank" href="http://rms.tangroom.com:12302/rms-api/depositAgreement.html?id=${depositAgreement.id}">定金协议附件</a>
				</c:if>
				<c:if test="${'2'!=depositAgreement.dataSource}">
					<form:hidden id="depositAgreementFile" path="depositAgreementFile" htmlEscape="false" maxlength="4000" class="input-xlarge"/>
					<sys:ckfinder input="depositAgreementFile" type="files" uploadPath="/定金协议" selectMultiple="true"/>
				</c:if>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">租客身份证：</label>
			<div class="controls">
				<form:hidden id="depositCustomerIDFile" path="depositCustomerIDFile" htmlEscape="false" maxlength="4000" class="input-xlarge"/>
				<sys:ckfinder input="depositCustomerIDFile" type="files" uploadPath="/租客身份证" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">定金协议其他附件：</label>
			<div class="controls">
				<form:hidden id="depositOtherFile" path="depositOtherFile" htmlEscape="false" maxlength="4000" class="input-xlarge"/>
				<sys:ckfinder input="depositOtherFile" type="files" uploadPath="/定金协议其他" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="contract:depositAgreement:edit">
				<c:if test="${depositAgreement.agreementStatus=='0'||depositAgreement.agreementStatus=='2'|| depositAgreement.agreementStatus=='6' || empty depositAgreement.id}">
					<c:if test="${depositAgreement.dataSource!='2'}"><!-- 手机来源的定金协议不显示暂存 -->
						<input id="saveBtn" class="btn btn-primary" type="button" value="暂 存" onclick="saveData()"/>
					</c:if>
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" onclick="submitData()"/>&nbsp;
				</c:if>
			</shiro:hasPermission>
			<shiro:hasPermission name="contract:depositAgreement:audit">
				<c:if test="${depositAgreement.agreementStatus=='1'}">
					<input id="btnSubmit" class="btn btn-primary" type="button" value="审 核" onclick="toAudit('${depositAgreement.id}')"/>
				</c:if>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>