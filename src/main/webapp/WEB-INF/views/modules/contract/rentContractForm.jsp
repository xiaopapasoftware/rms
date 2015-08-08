<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>出租合同管理</title>
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
			var idVal = $("#id").val();
			if(idVal == null || idVal == "" || idVal == undefined){//新增，而不是修改
				var signtype = $("#signType").val();//签约类型
				if(signtype == "1" || signtype == "2"){//1=正常续签，2=逾期续签
 					var oriEndDate = $("#oriEndDate").val();
					var oriEndDate = new Date(Date.parse(oriEndDate));
					oriEndDate.setDate(oriEndDate.getDate()+1);//原合同结束日期基础上加一天
					
					var year = oriEndDate.getFullYear();
					var month = oriEndDate.getMonth() + 1;
					if(parseFloat(month)<10){
						month = "0" + "" + month;
					}
					var day = oriEndDate.getDate();
					if(parseFloat(day)<10){
						day = "0" + "" + day;
					}
					
					var continueStartDateStyle = year + "-" + month + "-" + day;//原合同结束日期+1
					
					$("input[name='signDate']").val(continueStartDateStyle);//合同签订时间默认为原合同结束日期的后一天
					$("input[name='startDate']").val(continueStartDateStyle);//合同生效时间默认为原合同结束日期的后一天
					
					//合同过期时间默认为一年后的当天时间减去前一天
					oriEndDate.setFullYear(oriEndDate.getFullYear()+1);//合同过期时间默认为一年后的日期减一天
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
					
					//合同续租提醒时间默认为结束时间前1个月
					oriEndDate.setMonth(oriEndDate.getMonth() - 1);
					var year3 = oriEndDate.getFullYear();
					var month3 = oriEndDate.getMonth() + 1;
					if(parseFloat(month3)<10){
						month3 = "0" + "" + month3;
					}
					var day3 = oriEndDate.getDate();
					if(parseFloat(day3)<10){
						day3 = "0" + "" + day3;
					}
					var curDateStyle3 = year3 + "-" + month3 + "-" + day3;
					$("input[name='remindTime']").val(curDateStyle3);
					
				}else{//如果是新签
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
					
					$("input[name='signDate']").val(curDateStyle);//合同签订时间默认为当天时间
					$("input[name='startDate']").val(curDateStyle);//合同生效时间默认为当天时间
					
					//合同过期时间默认为一年后的当天时间减去前一天
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
					$("input[name='expiredDate']").val(curDateStyle2);
					
					//合同续租提醒时间默认为结束时间前1个月
					curDate.setMonth(curDate.getMonth() - 1);
					var year3 = curDate.getFullYear();
					var month3 = curDate.getMonth() + 1;
					if(parseFloat(month3)<10){
						month3 = "0" + "" + month3;
					}
					var day3 = curDate.getDate();
					if(parseFloat(day3)<10){
						day3 = "0" + "" + day3;
					}
					var curDateStyle3 = year3 + "-" + month3 + "-" + day3;
					$("input[name='remindTime']").val(curDateStyle3);
				}
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
		
		function toAudit(id,type) {
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
				        	saveAudit(id,'1',type);
				        } else if(v==2){
				        	saveAudit(id,'2',type);
				        }
				        return false;
				    }
				}
			};
			$.jBox.open(content,"审核",350,220,{});
		}
		
		function saveAudit(id,status,type) {
			loading('正在提交，请稍等...');
			var msg = $("#auditMsg").val();
			window.location.href="${ctx}/contract/rentContract/audit?objectId="+id+"&auditMsg="+msg+"&auditStatus="+status+"&type="+type;
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

			var date = new Date();
			date.setFullYear(startDate.getFullYear()+1);
			date.setDate(startDate.getDate()-1);
			var year = date.getFullYear();
			var month = date.getMonth()+1;
			if(parseFloat(month)<10)
				month = "0"+""+month;
			var day = date.getDate();
			if(parseFloat(day)<10)
				day = "0"+""+day;
			var expiredDate=year+"-"+month+"-"+day;
			$("input[name='expiredDate']").val(expiredDate);
			
			//合同续租提醒时间默认为结束时间前1个月
			date.setMonth(startDate.getMonth() - 1);
			year = date.getFullYear();
			month = date.getMonth() + 1;
			if(parseFloat(month)<10){
				month = "0" + "" + month;
			}
			day = date.getDate();
			if(parseFloat(day)<10){
				day = "0" + "" + day;
			}
			var remindTime = year + "-" + month + "-" + day;
			$("input[name='remindTime']").val(remindTime);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/contract/rentContract/">出租合同列表</a></li>
		<li class="active">
		<a href="${ctx}/contract/rentContract/form?id=${rentContract.id}">
			出租合同<shiro:hasPermission name="contract:rentContract:edit"><c:if test="${rentContract.contractStatus=='0' || rentContract.contractStatus=='3'||rentContract.contractStatus=='1'}">修改</c:if><c:if test="${rentContract.contractStatus!='0' && rentContract.contractStatus!='3'}">${not empty rentContract.id?'查看':'添加'}</c:if></shiro:hasPermission><shiro:lacksPermission name="contract:rentContract:edit">查看</shiro:lacksPermission>
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
		<form:hidden path="oriEndDate"/><!-- 如是续签，则原合同的结束日期 -->
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
			<label class="control-label">合同编号：</label>
			<div class="controls">
				<form:input path="contractCode" htmlEscape="false" maxlength="100" class="input-xlarge" readonly="true"/>
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
				<shiro:hasPermission name="contract:rentContract:edit"><a href="#" onclick="addProject()">添加物业项目</a></shiro:hasPermission>
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
				<shiro:hasPermission name="contract:rentContract:edit"><a href="#" onclick="addBuilding()">添加楼宇</a></shiro:hasPermission>
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
				<shiro:hasPermission name="contract:rentContract:edit"><a href="#" onclick="addHouse()">添加房屋</a></shiro:hasPermission>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房间：</label>
			<div class="controls">
				<form:select path="room.id" class="input-xlarge">
					<form:option value="" label="请选择..."/>
					<form:options items="${roomList}" itemLabel="roomNo" itemValue="id" htmlEscape="false"/>
				</form:select>
				<shiro:hasPermission name="contract:rentContract:edit"><a href="#" onclick="addRoom()">添加房间</a></shiro:hasPermission>
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
			<label class="control-label"><c:if test="${empty renew}">水电押金金额：</c:if><c:if test="${not empty renew}">水电押金差额：</c:if></label>
			<div class="controls">
				<form:input path="depositElectricAmount" htmlEscape="false" class="input-xlarge  number required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><c:if test="${empty renew}">房租押金金额：</c:if><c:if test="${not empty renew}">房租押金差额：</c:if></label>
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
				<shiro:hasPermission name="contract:rentContract:edit"><a href="#" onclick="addPartner()">添加合作人</a></shiro:hasPermission>
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
			<label class="control-label">出租合同：</label>
			<div class="controls">
				<form:hidden id="rentContractFile" path="rentContractFile" htmlEscape="false" maxlength="4000" class="input-xlarge"/>
				<sys:ckfinder input="rentContractFile" type="files" uploadPath="/出租合同" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">出租合同收据：</label>
			<div class="controls">
				<form:hidden id="rentContractReceiptFile" path="rentContractReceiptFile" htmlEscape="false" maxlength="4000" class="input-xlarge"/>
				<sys:ckfinder input="rentContractReceiptFile" type="files" uploadPath="/出租合同收据" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">租客身份证：</label>
			<div class="controls">
				<form:hidden id="rentContractCusIDFile" path="rentContractCusIDFile" htmlEscape="false" maxlength="4000" class="input-xlarge"/>
				<sys:ckfinder input="rentContractCusIDFile" type="files" uploadPath="/租客身份证" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">出租合同其他附件：</label>
			<div class="controls">
				<form:hidden id="rentContractOtherFile" path="rentContractOtherFile" htmlEscape="false" maxlength="4000" class="input-xlarge"/>
				<sys:ckfinder input="rentContractOtherFile" type="files" uploadPath="/出租合同其他附件" selectMultiple="true"/>
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
				<c:if test="${ rentContract.contractStatus=='0' || rentContract.contractStatus=='3'|| rentContract.contractStatus=='1' || empty rentContract.id}">
					<input id="saveBtn" class="btn btn-primary" type="button" value="暂 存" onclick="saveData()"/>&nbsp;
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" onclick="submitData()"/>&nbsp;
				</c:if>
			</shiro:hasPermission>
			<shiro:hasPermission name="contract:rentContract:audit">
			<c:if test="${rentContract.contractStatus=='2'}">
  				<input id="btnCancel" class="btn btn-primary" type="button" value="审 核" onclick="toAudit('${rentContract.id}','1')"/>
			</c:if>
			<c:if test="${rentContract.contractBusiStatus=='17'}">
  				<input id="btnCancel" class="btn btn-primary" type="button" value="审 核" onclick="toAudit('${rentContract.id}','2')"/>
			</c:if>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>