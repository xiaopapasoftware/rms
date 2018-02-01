<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>拨号记录管理</title>
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
			var project = $("[id='projectId']").val();
			var html = "<option value='' selected='selected'>全部</option>";
			if("" != project) {
				$.ajaxSetup({ cache: false });
				$.get("${ctx}/inventory/building/findList?id=" + project, function(data){
					for(var i=0;i<data.length;i++) {
						html += "<option value='"+data[i].id+"'>"+data[i].buildingName+"</option>";
					}
					$("[id='buildingId']").html(html);
				});
			} else {
				$("[id='buildingId']").html(html);
			}
			$("[id='buildingId']").val("");
			$("[id='buildingId']").prev("[id='s2id_building.id']").find(".select2-chosen").html("全部");
			
			$("[id='houseId']").html(html);
			$("[id='houseId']").val("");
			$("[id='houseId']").prev("[id='s2id_house.id']").find(".select2-chosen").html("全部");
			
			$("[id='roomId']").html(html);
			$("[id='roomId']").val("");
			$("[id='roomId']").prev("[id='s2id_room.id']").find(".select2-chosen").html("全部");
		}
		
		function buildingChange() {
			var building = $("[id='buildingId']").val();
			var html = "<option value='' selected='selected'>全部</option>";
			if("" != building) {
				$.ajaxSetup({ cache: false });
				$.get("${ctx}/inventory/house/findList?id=" + building, function(data){
					for(var i=0;i<data.length;i++) {
						html += "<option value='"+data[i].id+"'>"+data[i].houseNo+"</option>";
					}
					$("[id='houseId']").html(html);
				});
			} else {
				$("[id='houseId']").html(html);
			}
			$("[id='houseId']").val("");
			$("[id='houseId']").prev("[id='s2id_house.id']").find(".select2-chosen").html("全部");
			
			$("[id='roomId']").html(html);
			$("[id='roomId']").val("");
			$("[id='roomId']").prev("[id='s2id_room.id']").find(".select2-chosen").html("全部");
		}
		
		function houseChange() {
			var room = $("[id='houseId']").val();
			var html = "<option value='' selected='selected'>全部</option>";
			if("" != room) {
				$.ajaxSetup({ cache: false });
				$.get("${ctx}/inventory/room/findList?id=" + room, function(data){
					for(var i=0;i<data.length;i++) {
						html += "<option value='"+data[i].id+"'>"+data[i].roomNo+"</option>";
					}
					$("[id='roomId']").html(html);
				});
			} else {
				$("[id='roomId']").html(html);
			}
			$("[id='roomId']").val("");
			$("[id='roomId']").prev("[id='s2id_room.id']").find(".select2-chosen").html("全部");
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
			$("[id='buildingId']").html(html);
			$("[id='houseId']").html(html);
			$("[id='roomId']").html(html);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/contract/phoneRecord/">拨号记录列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="phoneRecord" action="${ctx}/contract/phoneRecord/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="width:120px;">物业项目：</label>
				<form:select path="projectId" class="input-medium" style="width:210px;" onchange="changeProject()">
					<form:option value="" label="全部"/>
					<form:options items="${projectList}" itemLabel="projectName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">楼宇：</label>
				<form:select path="buildingId" class="input-medium" style="width:210px;" onchange="buildingChange()">
					<form:option value="" label="全部"/>
					<form:options items="${buildingList}" itemLabel="buildingName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">房屋：</label>
				<form:select path="houseId" class="input-medium" style="width:210px;" onchange="houseChange()">
					<form:option value="" label="全部"/>
					<form:options items="${houseList}" itemLabel="houseNo" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">房间：</label>
				<form:select path="roomId" class="input-medium" style="width:210px;">
					<form:option value="" label="全部"/>
					<form:options items="${roomList}" itemLabel="roomNo" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">电话记录时间：</label>
				<input name="recordTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${phoneRecord.recordTime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:196px;"/>
			</li>
			<li><label style="width:120px;">支付宝用户Id：</label>
				<form:input path="aliUserId" htmlEscape="false" maxlength="100" class="input-medium" style="width:195px;"/>
			</li>
			<li class="btns">
				<shiro:hasPermission name="contract:phoneRecord:view">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
					<input type="button" class="btn btn-primary" value="重置" onclick="resetForm()"/>
				</shiro:hasPermission>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" type="${messageType}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>物业项目</th>
				<th>楼宇</th>
				<th>房屋号</th>
				<th>房间号</th>
				<th>房源类型</th>
				<th>支付宝用户Id</th>
				<th>电话记录时间</th>
				<th>房源编号</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="phoneRecord">
			<tr>
				<td>
					${phoneRecord.projectName}
				</td>
				<td>
					${phoneRecord.buildingName}
				</td>
				<td>
					${phoneRecord.houseCode}
				</td>
				<td>
					${phoneRecord.roomNo}
				</td>
				<td>
					${fns:getDictLabel(phoneRecord.flatsTag, 'housing_type', '')}
				</td>
				<td>
					${phoneRecord.aliUserId}
				</td>
				<td>
					<fmt:formatDate value="${phoneRecord.recordTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${phoneRecord.roomCode}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>