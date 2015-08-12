<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>出租合同管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		function changeProject() {
			var project = $("[id='propertyProject.id']").val();
			var html = "<option value='' selected='selected'>全部</option>";
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
			$("[id='building.id']").prev("[id='s2id_building.id']").find(".select2-chosen").html("全部");
			
			$("[id='house.id']").html(html);
			$("[id='house.id']").val("");
			$("[id='house.id']").prev("[id='s2id_house.id']").find(".select2-chosen").html("全部");
			
			$("[id='room.id']").html(html);
			$("[id='room.id']").val("");
			$("[id='room.id']").prev("[id='s2id_room.id']").find(".select2-chosen").html("全部");
		}
		
		function buildingChange() {
			var building = $("[id='building.id']").val();
			var html = "<option value='' selected='selected'>全部</option>";
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
			$("[id='house.id']").prev("[id='s2id_house.id']").find(".select2-chosen").html("全部");
			
			$("[id='room.id']").html(html);
			$("[id='room.id']").val("");
			$("[id='room.id']").prev("[id='s2id_room.id']").find(".select2-chosen").html("全部");
		}
		
		function houseChange() {
			var room = $("[id='house.id']").val();
			var html = "<option value='' selected='selected'>全部</option>";
			if("" != room) {
				$.ajaxSetup({ cache: false });
				$.get("${ctx}/inventory/room/findList?id=" + room, function(data){
					for(var i=0;i<data.length;i++) {
						html += "<option value='"+data[i].id+"'>"+data[i].roomNo+"</option>";
					}
					$("[id='room.id']").html(html);
				});
			} else {
				$("[id='room.id']").html(html);
			}
			$("[id='room.id']").val("");
			$("[id='room.id']").prev("[id='s2id_room.id']").find(".select2-chosen").html("全部");
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
		
		function auditHis(id) {
			$.jBox.open("iframe:${ctx}/contract/leaseContract/auditHis?objectId="+id,'审核记录',650,400,{buttons:{'关闭':true}});
		}
		
		function specialReturn(id) {
			confirmx("确认要特殊退租吗?",function(){
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
				var curDateStyle = year + "-" + month + "-" + day;
				
				var html = '<label style="width:120px;">实际退房日期：</label>';
				html += '<input name="returnDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"';
				html += 'value="'+curDateStyle+'"';
				html += 'onclick="WdatePicker({dateFmt:\'yyyy-MM-dd\',isShowClear:false});" style="width:196px;"/>';
				
				var submit = function (v, h, f) {
				    window.location.href="${ctx}/contract/rentContract/specialReturnContract?id="+id+"&returnDate="+f.returnDate;
				    return true;
				};
				
				$.jBox(html,{title:"选择退租日期",submit:submit});
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/contract/rentContract/">出租合同列表</a></li>
		<shiro:hasPermission name="contract:rentContract:edit"><li><a href="${ctx}/contract/rentContract/form">出租合同添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="rentContract" action="${ctx}/contract/rentContract/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="width:120px;">合同来源：</label>
				<form:select path="contractSource" class="input-medium" style="width:210px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('contract_source')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">合同签约类型：</label>
				<form:select path="signType" class="input-medium" style="width:210px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('contract_sign_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">合同编号：</label>
				<form:input path="contractCode" htmlEscape="false" maxlength="100" class="input-medium" style="width:195px;"/>
			</li>
			<li><label style="width:120px;">合同名称：</label>
				<form:input path="contractName" htmlEscape="false" maxlength="100" class="input-medium" style="width:195px;"/>
			</li>
			<li><label style="width:120px;">出租方式：</label>
				<form:select path="rentMode" class="input-medium" style="width:210px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('rent_mode')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">物业项目：</label>
				<form:select path="propertyProject.id" class="input-medium" style="width:210px;" onchange="changeProject()">
					<form:option value="" label="全部"/>
					<form:options items="${projectList}" itemLabel="projectName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">楼宇：</label>
				<form:select path="building.id" class="input-medium" style="width:210px;" onchange="buildingChange()">
					<form:option value="" label="全部"/>
					<form:options items="${buildingList}" itemLabel="buildingName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">房屋：</label>
				<form:select path="house.id" class="input-medium" style="width:210px;" onchange="houseChange()">
					<form:option value="" label="全部"/>
					<form:options items="${houseList}" itemLabel="houseNo" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">房间：</label>
				<form:select path="room.id" class="input-medium" style="width:210px;">
					<form:option value="" label="全部"/>
					<form:options items="${roomList}" itemLabel="roomNo" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">合同生效时间：</label>
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${rentContract.startDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width:196px;"/>
			</li>
			<li><label style="width:120px;">合同过期时间：</label>
				<input name="expiredDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${rentContract.expiredDate}" pattern="yyyy-MM-dd、"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width:196px;"/>
			</li>
			<li><label style="width:120px;">合同签订时间：</label>
				<input name="signDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${rentContract.signDate}" pattern="yyyy-MM-dd、"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width:196px;"/>
			</li>
			<li><label style="width:120px;">合同审核状态：</label>
				<form:select path="contractStatus" class="input-medium" style="width:210px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('rent_contract_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">合同业务状态：</label>
				<form:select path="contractBusiStatus" class="input-medium" style="width:210px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('rent_contract_busi_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="width:3000px;">
		<thead>
			<tr>
				<th>原出租合同名称</th>
				<th>原定金协议名称</th>
				<th style="width:20px;">合同来源</th>
				<th style="width:20px;">合同签订类型</th>
				<th>合同编号</th>
				<th>合同名称</th>
				<th style="width:20px;">出租方式</th>
				<th style="width:120px;">物业项目</th>
				<th style="width:120px;">楼宇</th>
				<th style="width:80px;">房屋号</th>
				<th>房间号</th>
				<th style="width:120px;">月租金</th>
				<th style="width:80px;">合同生效时间</th>
				<th style="width:80px;">合同过期时间</th>
				<th style="width:80px;">合同签订时间</th>
				<th style="width:80px;">续租提醒时间</th>
				<th style="width:150px;">合同审核状态</th>
				<th style="width:180px;">合同业务状态</th>
				<th style="width:20px;">销售姓名</th>
				<th style="width:20px;">合作人姓名</th>
				<th style="width:130px;">创建时间</th>
				<th style="width:130px;">修改时间</th>
				<th style="width:20px;">创建人</th>
				<th style="width:20px;">修改人</th>
				<th style="width:480px;">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="rentContract">
			<tr <c:if test="${rentContract.contractBusiStatus=='7'||rentContract.contractBusiStatus=='8'||rentContract.contractBusiStatus=='9'||rentContract.contractBusiStatus=='16'}">style="background-color:#f1f2f2;"</c:if>>
				<td>
					${rentContract.refContractName}
				</td>
				<td>
					${rentContract.refAgreementName}
				</td>
				<td>
					${fns:getDictLabel(rentContract.contractSource, 'contract_source', '')}
				</td>
				<td>
					${fns:getDictLabel(rentContract.signType, 'contract_sign_type', '')}
				</td>
				<td>
					${rentContract.contractCode}
				</td>
				<td>
					<a href="${ctx}/contract/rentContract/form?id=${rentContract.id}">
						${rentContract.contractName}
					</a>
				</td>
				<td>
					${fns:getDictLabel(rentContract.rentMode, 'rent_mode', '')}
				</td>
				<td>
					${rentContract.projectName}
				</td>
				<td>
					${rentContract.buildingBame}
				</td>
				<td>
					${rentContract.houseNo}
				</td>
				<td>
					${rentContract.roomNo}
				</td>
				<td>
					${rentContract.rental}
				</td>
				<td>
					<fmt:formatDate value="${rentContract.startDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${rentContract.expiredDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${rentContract.signDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${rentContract.remindTime}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${fns:getDictLabel(rentContract.contractStatus, 'rent_contract_status', '')}
				</td>
				<td>
					${fns:getDictLabel(rentContract.contractBusiStatus, 'rent_contract_busi_status', '')}
				</td>
				<td>
					${rentContract.user.name}
				</td>
				<td>
					${rentContract.partner.partnerName}
				</td>
				<td><fmt:formatDate value="${rentContract.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td><fmt:formatDate value="${rentContract.updateDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td>${rentContract.createBy.loginName}</td>
				<td>${rentContract.updateBy.loginName}</td>			
				<td>
					<shiro:hasPermission name="contract:rentContract:edit">
						<c:if test="${rentContract.contractStatus=='3'||rentContract.contractStatus=='0'||rentContract.contractStatus=='1'}">
    						<a href="${ctx}/contract/rentContract/form?id=${rentContract.id}">修改</a>
						</c:if>
					</shiro:hasPermission>
					<shiro:hasPermission name="contract:rentContract:return">
						<c:if test="${rentContract.contractStatus=='6' && rentContract.contractBusiStatus=='0'}">
	    				 	<a href="${ctx}/contract/rentContract/returnContract?id=${rentContract.id}" onclick="return confirmx('确认要正常退租吗?', this.href)">正常退租</a>
	    				</c:if>
	    				<c:if test="${rentContract.contractStatus=='6' && rentContract.contractBusiStatus=='0'}">
	    					<a href="${ctx}/contract/rentContract/earlyReturnContract?id=${rentContract.id}" onclick="return confirmx('确认要提前退租吗,提前退租将删除未到账款项?', this.href)">提前退租</a>
	    				</c:if>
    					 <c:if test="${rentContract.contractStatus=='6' && rentContract.contractBusiStatus=='0'}">		
    						<a href="${ctx}/contract/rentContract/lateReturnContract?id=${rentContract.id}" onclick="return confirmx('确认要逾期退租吗?', this.href)">逾期退租</a>
    					 </c:if>  
   					</shiro:hasPermission>
    				<shiro:hasPermission name="contract:rentContract:specialreturn">
    					 <c:if test="${rentContract.contractStatus=='6' && rentContract.contractBusiStatus=='0'}">
    					    <a href="javascript:void(0);" onclick="javascript:specialReturn('${rentContract.id}');">特殊退租</a>
    					 </c:if> 
   					 </shiro:hasPermission><br/>
    				<shiro:hasPermission name="contract:rentContract:change">
    				 	 <c:if test="${rentContract.contractStatus=='6' && rentContract.contractBusiStatus=='0'}">
    					 	<a href="${ctx}/contract/rentContract/changeContract?id=${rentContract.id}" onclick="return confirmx('确认要协议变更吗?', this.href)">协议变更</a>
    					 </c:if> 
   					 </shiro:hasPermission>
   					<shiro:hasPermission name="contract:rentContract:edit">
    					 <c:if test="${rentContract.contractStatus=='6' && rentContract.contractBusiStatus=='0'}">
    					    <a href="${ctx}/contract/rentContract/renewContract?id=${rentContract.id}" onclick="return confirmx('确认要人工续签吗?', this.href)">人工续签</a>
    					 </c:if> 
    					 <c:if test="${rentContract.contractStatus=='6' && rentContract.contractBusiStatus=='0'}">
    					 	<a href="${ctx}/contract/rentContract/autoRenewContract?id=${rentContract.id}" onclick="return confirmx('确认要逾期自动续签吗?', this.href)">逾期自动续签</a>
    					 </c:if> 
	    			</shiro:hasPermission>
					<shiro:hasPermission name="contract:rentContract:return">
						<c:if test="${rentContract.contractStatus=='6' && rentContract.contractBusiStatus=='2'}">
    						<a href="${ctx}/contract/rentContract/toReturnCheck?id=${rentContract.id}" onclick="$('#messageBox').remove();">正常退租核算</a>
						</c:if>
						<c:if test="${rentContract.contractStatus=='6' && rentContract.contractBusiStatus=='1'}">
    						<a href="${ctx}/contract/rentContract/toEarlyReturnCheck?id=${rentContract.id}" onclick="$('#messageBox').remove();">提前退租核算</a>
						</c:if>
						<c:if test="${rentContract.contractStatus=='6' && rentContract.contractBusiStatus=='3'}">
    						<a href="${ctx}/contract/rentContract/toLateReturnCheck?id=${rentContract.id}" onclick="$('#messageBox').remove();">逾期退租核算</a>
						</c:if>
					</shiro:hasPermission>
	 					<c:if test="${rentContract.contractStatus!='0' && rentContract.contractStatus!='1'}">
	    					<a href="javascript:void(0);" onclick="auditHis('${rentContract.id}')">审核记录</a>
						</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>