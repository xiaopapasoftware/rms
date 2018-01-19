<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>定金协议转合同</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
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

			$("#contractName, #rental, #depositAmount, #renMonths, #depositMonths, #depositElectricAmount, #tvFee, #netFee, #waterFee, #serviceFee, #meterValue, #totalMeterValue, #peakMeterValue, #valleyMeterValue, #freeMonths,#coalValue, #waterValue, #userName").keypress(function(event) {
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
					var freeMonths = $('#freeMonths').val();
					if($("#hasFree").attr('checked')&&(freeMonths==null||freeMonths==""||freeMonths==undefined)){
						top.$.jBox.tip('减免房租不可为空！','warning');
						return;
					}
					var number = parseFloat(freeMonths);
					if($("#hasFree").attr('checked') && (number>10 || number <= 0)){
						top.$.jBox.tip('减免房租至少为1个月且不可超过10个月！','warning');
						return;
					}
					var rental = $("#rental").val();
					if(rental!=null && rental!=undefined && rental!=""){
						var rentalNum = parseFloat(rental);
						if(rentalNum < 0){
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
					if($("#rentMode").val()=="0") {//整租
						if($("#eleRechargeAmount").val().length != 0 ){
							top.$.jBox.tip('整租房源不能进行电费充值！','warning');
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
			if($("#rentMode").val()=="0") {//整租
				$("[id='room.id']").attr("disabled","disabled");
				$("[id='room.id']").val("");
				$("[id='room.id']").prev("[id='s2id_room.id']").find(".select2-chosen").html("请选择...");
				$("[id='room.id']").next("a").hide();
			} else {//单间
				$("[id='room.id']").removeAttr("disabled");
				$("[id='room.id']").next("a").show();
			}
			changeContractName();
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
            calculateRange();
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
		//function changeLiveList(rentContractId){//更改入住人列表
		//    $.post("${ctx}/contract/rentContract/changeLiveList", {'rentContractId':rentContractId, 'liveIds':$.makeArray($("#liveList").val()).join()});
		//}
		//function changeTenantList(rentContractId){//更改承租人列表
		//    $.post("${ctx}/contract/rentContract/changeTenantList", {'rentContractId':rentContractId, 'tenantIds':$.makeArray($("#tenantList").val()).join()});
		//}
	     function isHasFree(dom) {
    	    var flag = dom.checked ? false:true;
			$('#freeMonths').attr('disabled',flag);
			if(!flag){
			  $('#freeMonths').addClass("required");
			}else{
			  $('#freeMonths').val("0");
			  $('#freeMonths').removeClass("required");
			}
         }

        function calculateRange() {
            var value = $('#leaseMonth').val();
            var startDate = $("#startDate").val();
            var expireDate = nextMonthsDate(startDate,value);
            var refetCallDate = nextMonthsDate(startDate,value-1);
            expireDate = new Date(expireDate);
            refetCallDate = new Date(refetCallDate);
            expireDate.setDate(expireDate.getDate()-1);
            refetCallDate.setDate(refetCallDate.getDate()-1);
            $('#expireDate').val(formatDate(expireDate));
            $('#remindTime').val(formatDate(refetCallDate));
        }

        function nextMonthsDate (date, monthNum)
        {
            var dateArr = date.split('-');
            var year = dateArr[0]; //获取当前日期的年份
            var month = dateArr[1]; //获取当前日期的月份
            var day = dateArr[2]; //获取当前日期的日
            var days = new Date(year, month, 0);
            var year2 = year;
            var month2 = parseInt(month) + parseInt(monthNum);
            if (month2 >12) {
                year2 = parseInt(year2) + parseInt((parseInt(month2) / 12 == 0 ? 1 : parseInt(month2) / 12));
                month2 = parseInt(month2) % 12;
            }
            var day2 = day;
            var days2 = new Date(year2, month2, 0);
            days2 = days2.getDate();
            if (day2 > days2) {
                day2 = days2;
            }
            if (month2 < 10) {
                month2 = '0' + month2;
            }
            return year2 + '-' + month2 + '-' + day2;
        }
        function formatDate(date){
            var year = date.getFullYear();
            var month = date.getMonth()+1;
            var day = date.getDate();
            month = month<10?'0'+month:month;
            day = day<10?'0'+day:day;
            return year+'-'+month+'-'+day;
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
		<form:hidden path="validatorFlag" value="1"/><!-- 正常保存，而非暂存 -->
		<form:hidden path="contractId"/>
		<form:hidden path="agreementId"/><!-- 定金转合同，保存定金ID -->
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
				<form:input type = "number" placeholder="请填写正整数" id = "renMonths"  path="renMonths" htmlEscape="false" maxlength="2" class="input-xlarge digits required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房租押金月数：</label>
			<div class="controls">
				<form:input type = "number" placeholder="请填写正整数" id = "depositMonths"  path="depositMonths" htmlEscape="false" maxlength="2" class="input-xlarge digits required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">智能电表首充金额</label>
			<div class="controls">
				<form:input path="eleRechargeAmount" htmlEscape="false" class="input-xlarge number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">承租人：</label>
			<div class="controls">
				<!-- onchange="changeTenantList('${rentContract.id}');" -->
				<form:select path="tenantList" style="width:450px;" class="input-xlarge required tanants" multiple="true">
					<option></option>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
				<shiro:hasPermission name="contract:rentContract:edit"><a href="#" onclick="addTenant()">添加承租人</a></shiro:hasPermission>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">入住人：</label>
			<div class="controls">
				<!-- onchange="changeLiveList('${rentContract.id}');" -->
				<form:select path="liveList" style="width:450px;" class="input-xlarge required tanants" multiple="true">
				    <option></option>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
	 			<shiro:hasPermission name="contract:rentContract:edit"><a href="#" onclick="addLive()">添加入住人</a></shiro:hasPermission>
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
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合同生效时间：</label>
			<div class="controls">
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${rentContract.startDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" onchange="startDateChange()"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">
				合同租期：
			</label>
			<div class="controls">
				<input type = "number" id = "leaseMonth" placeholder="请输入1-24之间的正整数" class="input-medium required" onblur="calculateRange()" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合同过期时间：</label>
			<div class="controls">
				<input name="expiredDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${rentContract.expiredDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"  onchange="endDateChange()"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">续租提醒时间：</label>
			<div class="controls">
				<input name="remindTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${rentContract.remindTime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否返租促销：</label>
			<div class="controls">
				<form:checkbox path="hasFree"  value="1" style="line-height: 30px; height: 30px;" onclick="isHasFree(this)"  />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">优惠促销房租月数：</label>
			<div class="controls">
				<form:input type = "number" placeholder="请填写正整数" id = "freeMonths"  path="freeMonths" htmlEscape="false" maxlength="2" class="input-xlarge  digits"  disabled="true" />
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
					title="用户" url="/sys/office/treeData?type=3" cssClass="required" allowClear="true" notAllowSelectParent="true"/>
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否开通有线电视：</label>
			<div class="controls">
				<form:select path="hasTv" class="input-xlarge required">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font></span>
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
				<form:select path="hasNet" class="input-xlarge required">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font></span>
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
			<label class="control-label">每月燃气费：</label>
			<div class="controls">
				<form:input path="gasFee" htmlEscape="false" class="input-xlarge  number"/>
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
				<c:if test="${'2'==rentContract.dataSource && rentContract.contractStatus != '0'}">
					<a target="_blank" href="http://rms.tangroom.com:12302/rms-api/rentContractAgreement.html?id=${rentContract.id}">合同附件</a>
				</c:if>
				<c:if test="${'2'!=rentContract.dataSource}">
					<form:hidden id="rentContractFile" path="rentContractFile" htmlEscape="false" maxlength="4000" class="input-xlarge"/>
					<sys:ckfinder input="rentContractFile" type="files" uploadPath="/出租合同" selectMultiple="true"/>
				</c:if>
				<span class="help-inline"><font color="red">附件最大不能超过10M</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">租客身份证：</label>
			<div class="controls">
				<form:hidden id="rentContractCusIDFile" path="rentContractCusIDFile" htmlEscape="false" maxlength="4000" class="input-xlarge"/>
				<sys:ckfinder input="rentContractCusIDFile" type="files" uploadPath="/租客身份证" selectMultiple="true"/>
				<span class="help-inline"><font color="red">附件最大不能超过10M</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">出租合同其他附件：</label>
			<div class="controls">
				<form:hidden id="rentContractOtherFile" path="rentContractOtherFile" htmlEscape="false" maxlength="4000" class="input-xlarge"/>
				<sys:ckfinder input="rentContractOtherFile" type="files" uploadPath="/出租合同其他附件" selectMultiple="true"/>
				<span class="help-inline"><font color="red">附件最大不能超过10M</font> </span>
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