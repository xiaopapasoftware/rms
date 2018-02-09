<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>承租入住人管理列表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
            top.$.jBox.tip.mess = null;
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
		 
		function resetForm() {
			$("#searchForm").find("select").each(function(index){
				$(this).val("");
				$(this).prev("div").find(".select2-chosen").html("全部");
				
				$(this).find("option").each(function(){
					$(this).removeAttr("selected");
				});
			});
			$("#searchForm").find("input").each(function(index){
				if($(this).attr("type")=="text")
					$(this).val("");
			});
			
			var html = '<option value="">全部</option>';
			$("[id='building.id']").html(html);
			$("[id='house.id']").html(html);
			$("[id='room.id']").html(html);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/person/tanentLiveMgt/">出租合同列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="rentContract" action="${ctx}/person/tanentLiveMgt/list" method="post" class="breadcrumb form-search">
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
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:196px;"/>
			</li>
			<li><label style="width:120px;">合同过期时间：</label>
				<input name="expiredDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${rentContract.expiredDate}" pattern="yyyy-MM-dd、"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:196px;"/>
			</li>
			<li><label style="width:120px;">合同签订时间：</label>
				<input name="signDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${rentContract.signDate}" pattern="yyyy-MM-dd、"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:196px;"/>
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
			<li><label style="width:120px;">租客姓名：</label>
				<form:input path="tenantName" htmlEscape="false" maxlength="100" class="input-medium" style="width:195px;"/>
			</li>
			<li><label style="width:120px;">租客手机号：</label>
				<form:input path="tenantMobileNo" htmlEscape="false" maxlength="100" class="input-medium" style="width:195px;"/>
			</li>
			<li><label style="width:120px;">租客身份证：</label>
				<form:input path="tenantIdNo" htmlEscape="false" maxlength="100" class="input-medium" style="width:195px;"/>
			</li>
			<li class="btns">
				<shiro:hasPermission name="person:tanentLiveMgt:view">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
					<input type="button" class="btn btn-primary" value="重置" onclick="resetForm()"/>
				</shiro:hasPermission>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" type="${messageType}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="width:3000px;">
		<thead>
			<tr>
				<th>合同编号</th>
				<th>合同名称</th>
				<th>物业项目</th>
				<th>楼宇</th>
				<th>房屋号</th>
				<th>房间号</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="rentContract">
			<tr <c:if test="${rentContract.contractBusiStatus=='7'||rentContract.contractBusiStatus=='8'||rentContract.contractBusiStatus=='9'||rentContract.contractBusiStatus=='16'}">style="background-color:#f1f2f2;"</c:if>>
				<td>
					${rentContract.contractCode}
				</td>
				<td>
					${rentContract.contractName}
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
					<shiro:hasPermission name="person:tanentLiveMgt:edit">
     					<a href="${ctx}/person/tanentLiveMgt/form?id=${rentContract.id}">修改承租人/入住人</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>