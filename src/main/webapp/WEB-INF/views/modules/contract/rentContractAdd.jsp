<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>定金协议管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#contractName, #rental, #depositAmount, #renMonths, #depositMonths, #depositElectricAmount, #tvFee, #netFee, #waterFee, #serviceFee, #meterValue, #totalMeterValue, #peakMeterValue, #flatMeterValue, #valleyMeterValue, #coalValue, #waterValue, #userName").keypress(function(event) {
		        if (event.keyCode == 13) {
		            event.preventDefault();
		        }
		    });
			$("input[name$='Date']").keypress(function(event) {
		        if (event.keyCode == 13) {
		            event.preventDefault();
		        }
		    });
			$("input[name$='Time']").keypress(function(event) {
		        if (event.keyCode == 13) {
		            event.preventDefault();
		        }
		    });
			
			//合同续租提醒时间默认为结束时间前1个月
			var expiredDate = $("input[name='expiredDate']").val();
			var expiredDate = new Date(Date.parse(expiredDate));
			expiredDate.setMonth(expiredDate.getMonth() - 1);
			var year3 = expiredDate.getFullYear();
			var month3 = expiredDate.getMonth() + 1;
			if(parseFloat(month3)<10){
				month3 = "0" + "" + month3;
			}
			var day3 = expiredDate.getDate();
			if(parseFloat(day3)<10){
				day3 = "0" + "" + day3;
			}
			var curDateStyle3 = year3 + "-" + month3 + "-" + day3;
			$("input[name='remindTime']").val(curDateStyle3);
			
			//把物业项目简称带入房屋编号中
			var projectSimpleName= $("[id='propertyProject.id']").find("option:selected").attr("projectSimpleName");
			if(projectSimpleName==null || projectSimpleName =="" || projectSimpleName==undefined){
				$("#contractCode").val("${rentContract.contractCode}");
			}else{
				$("#contractCode").val(projectSimpleName + "-" + "${rentContract.contractCode}");
			}
			
			//更新合同名称
			changeContractName();
		});
		
		function submitData() {
			$("#agreementBusiStatus").val("2");
			$("#inputForm").validate({
				submitHandler: function(form){
					
					var rental = $("#rental").val();
					if(rental!=null && rental!=undefined && rental!=""){
						var rentalNum = parseFloat(rental);
						if(rentalNum <= 0){
							top.$.jBox.tip('月租金金额不合法！','warning');
							return;
						}
					}
					
					var renMonths = $("#renMonths").val();
					if(renMonths!=null && renMonths!=undefined && renMonths!=""){
						var renMonthsNum = parseFloat(renMonths);
						if(renMonthsNum <= 0){
							top.$.jBox.tip('首付房租月数不合法！','warning');
							return;
						}
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
			$("#contractStatus").val("0");
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
				$("#contractCode").val("${rentContract.contractCode}");
			}else{
				$("#contractCode").val(projectSimpleName + "-" + "${rentContract.contractCode}");
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
			
			changeContractName();
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
			changeContractName();
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
			
			changeContractName();
		}
		
		function roomChange(){
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
		
		function rentModeChange() {
			if($("#rentMode").val()=="0") {
				$("[id='room.id']").attr("disabled","disabled");
				$("[id='room.id']").val("");
				$("[id='room.id']").prev("[id='s2id_room.id']").find(".select2-chosen").html("请选择...");
				$("[id='room.id']").next("a").hide();
			} else {
				$("[id='room.id']").removeAttr("disabled");
				$("[id='room.id']").next("a").show();
			}
		}
		
		function addTenant() {
			top.$.jBox.open("iframe:${ctx}/person/tenant/add?type=tenant",'添加承租人',850,500,{buttons:{'保存':'1','关闭':'2'},submit:saveHandler});
		}
		
		function addLive() {
			top.$.jBox.open("iframe:${ctx}/person/tenant/add?type=live",'添加入住人',850,500,{buttons:{'保存':'1','关闭':'2'},submit:saveHandler});
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
			top.$.jBox.open("iframe:${ctx}/person/tenant/add",'添加承租人',850,500,{buttons:{'保存':'1','关闭':'2'},submit:saveHandler});
		}
		
		function addPartner() {
			top.$.jBox.open("iframe:${ctx}/person/partner/add",'添加合作人',850,500,{buttons:{'保存':'1','关闭':'2'},submit:saveHandler});
		}
		
		function saveHandler(v,h,f) {
			if(v=='1') {
				h.find("iframe")[0].contentWindow.$("#inputForm").submit();
				return false;
			}
		}
		
		function startDateChange() {
			var startDate = new Date($("input[name='startDate']").val());
			
			var endDate = new Date(Date.parse(startDate));
			endDate.setFullYear(endDate.getFullYear()+1);
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
			
			//合同续租提醒时间默认为结束时间前1个月
			endDate.setMonth(endDate.getMonth() - 1);
			year = endDate.getFullYear();
			month = endDate.getMonth() + 1;
			if(parseFloat(month)<10){
				month = "0" + "" + month;
			}
			day = endDate.getDate();
			if(parseFloat(day)<10){
				day = "0" + "" + day;
			}
			var remindTime = year + "-" + month + "-" + day;
			$("input[name='remindTime']").val(remindTime);
		}
		
		function endDateChange() {
			var expiredDate = new Date($("input[name='expiredDate']").val());
			expiredDate = new Date(Date.parse(expiredDate));
			expiredDate.setFullYear(expiredDate.getFullYear());
			expiredDate.setMonth(expiredDate.getMonth()-1);
			expiredDate.setDate(expiredDate.getDate());
			
			var year = expiredDate.getFullYear();
			var month = expiredDate.getMonth() + 1;
			if(parseFloat(month)<10){
				month = "0" + "" + month;
			}
			var day = expiredDate.getDate();
			if(parseFloat(day)<10){
				day = "0" + "" + day;
			}
			$("input[name='remindTime']").val(year + "-" + month + "-" + day);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/contract/depositAgreement/">定金协议列表</a></li>
		<li>
		<shiro:hasPermission name="contract:depositAgreement:edit">
		<a href="${ctx}/contract/depositAgreement/form">定金协议添加</a>
		</shiro:hasPermission>
		</li>
		<li class="active"><a href="javascript:void(0);">出租合同添加</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="rentContract" action="${ctx}/contract/rentContract/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="contractStatus" value="1"/>
		<form:hidden path="validatorFlag" value="1"/>
		<form:hidden path="saveSource" value="1"/>
		<form:hidden path="contractId"/>
		<form:hidden path="agreementId"/>
		<form:hidden path="depositAgreementAmount" value="${depositAmount}"/>
		<form:hidden path="signType"/>
		<sys:message content="${message}" type="${messageType}"/>
		<div class="control-group">
			<label class="control-label">合同编号：</label>
			<div class="controls">
				<form:input path="contractCode" htmlEscape="false" maxlength="100" class="input-xlarge" readonly="true"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合同名称：</label>
			<div class="controls">
				<form:input path="contractName" htmlEscape="false" maxlength="100" class="input-xlarge required" readonly="true"/>
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
				<a href="#" onclick="addProject()">添加物业项目</a>
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
				<a href="#" onclick="addBuilding()">添加楼宇</a>
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
				<a href="#" onclick="addHouse()">添加房屋</a>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房间：</label>
			<div class="controls">
				<form:select path="room.id" class="input-xlarge" onchange="roomChange()">
					<form:option value="" label="请选择..."/>
					<form:options items="${roomList}" itemLabel="roomNo" itemValue="id" htmlEscape="false"/>
				</form:select>
				<a href="#" onclick="addRoom()">添加房间</a>
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
			<label class="control-label">水电押金金额</label>
			<div class="controls">
				<form:input path="depositElectricAmount" htmlEscape="false" class="input-xlarge  number required"/>
				<span class="help-inline"><font color="red">*注意：如果是智能电表充值合同，这里不需要填写！</font> </span>
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
			<label class="control-label">承租人：</label>
			<div class="controls">
				<form:select path="tenantList" class="input-xlarge required" multiple="true">
					<c:forEach items="${tenantList}" var="item">
						<form:option value="${item.id}">${item.cellPhone}-${item.tenantName}</form:option>
					</c:forEach>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
				<a href="#" onclick="addTenant()">添加承租人</a>
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
				<a href="#" onclick="addLive()">添加入住人</a>
			</div>
		</div>
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
			<label class="control-label">公用事业费付费方式：</label>
			<div class="controls">
				<form:select path="chargeType" class="input-xlarge required">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('charge_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
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
			<label class="control-label">合同生效时间：</label>
			<div class="controls">
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${rentContract.startDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" onchange="startDateChange()"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合同过期时间：</label>
			<div class="controls">
				<input name="expiredDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${rentContract.expiredDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"  onchange="endDateChange()"/>
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
				<a href="#" onclick="addPartner()">添加合作人</a>
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
				<c:if test="${rentContract.dataSource!='2'}"><!-- 手机来源的合同不显示暂存 -->
					<input id="saveBtn" class="btn btn-primary" type="button" value="暂 存" onclick="saveData()"/>&nbsp;
				</c:if>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" onclick="submitData()"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>