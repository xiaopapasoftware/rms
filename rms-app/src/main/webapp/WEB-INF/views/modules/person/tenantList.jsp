<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>租客信息管理</title>
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/person/tenant/">租客信息列表</a></li>
		<shiro:hasPermission name="person:tenant:edit"><li><a href="${ctx}/person/tenant/form">租客信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="tenant" action="${ctx}/person/tenant/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="width:100px;">姓名：</label>
				<form:input path="tenantName" htmlEscape="false" maxlength="100" class="input-medium" style="width:162px;"/>
			</li>
			<li><label style="width:100px;">性别：</label>
				<form:select path="gender" class="input-medium" style="width:177px;">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:100px;">租客类型：</label>
				<form:select path="tenantType" class="input-medium" style="width:177px;">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('tenant_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:100px;">证件类型：</label>
				<form:select path="idType" class="input-medium" style="width:177px;">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('id_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:100px;">证件号码：</label>
				<form:input path="idNo" htmlEscape="false" maxlength="100" class="input-medium" style="width:162px;"/>
			</li>
			<li><label style="width:100px;">手机号码：</label>
				<form:input path="cellPhone" htmlEscape="false" maxlength="64" class="input-medium" style="width:162px;"/>
			</li>
			<li><label style="width:100px;">出生日期：</label>
				<input name="birthday" type="text" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${tenant.birthday}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:162px;"/>
			</li>
			<li><label style="width:100px;">学历：</label>
				<form:select path="degrees" class="input-medium" style="width:177px;">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('degrees')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:100px;">所属企业：</label>
				<form:select path="company.id" class="input-xlarge" style="width:177px;">
					<form:option value="" label="请选择..."/>
					<form:options items="${listCompany}" itemLabel="companyName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:100px;">电子邮箱：</label>
				<form:input path="email" htmlEscape="false" maxlength="64" class="input-medium" style="width:162px;"/>
			</li>
			<li><label style="width:100px;">户籍所在地：</label>
				<form:input path="houseRegister" htmlEscape="false" maxlength="64" class="input-medium" style="width:162px;"/>
			</li>
			<li><label style="width:100px;">职位：</label>
				<form:input path="position" htmlEscape="false" maxlength="64" class="input-medium" style="width:162px;"/>
			</li>
			<li><label style="width:100px;">跟进销售：</label>
				<form:select path="user.id" class="input-xlarge" style="width:177px;">
					<form:option value="" label="请选择..."/>
					<form:options items="${listUser}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:100px;">出租合同号：</label>
				<form:input path="contractCode" htmlEscape="false" maxlength="100" class="input-medium" style="width:162px;"/>
			</li>
			<li><label style="width:100px;">出租合同名称：</label>
				<form:input path="contractName" htmlEscape="false" maxlength="64" class="input-medium" style="width:162px;"/>
			</li>
			<li><label style="width:100px;">物业项目：</label>
				<form:select path="propertyProject.id" class="input-medium" style="width:177px;" onchange="changeProject()">
					<form:option value="" label="全部"/>
					<form:options items="${projectList}" itemLabel="projectName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:100px;">楼宇：</label>
				<form:select path="building.id" class="input-medium" style="width:177px;" onchange="buildingChange()">
					<form:option value="" label="全部"/>
					<form:options items="${buildingList}" itemLabel="buildingName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:100px;">房屋：</label>
				<form:select path="house.id" class="input-medium" style="width:177px;" onchange="houseChange()">
					<form:option value="" label="全部"/>
					<form:options items="${houseList}" itemLabel="houseNo" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:100px;">房间：</label>
				<form:select path="room.id" class="input-medium" style="width:177px;">
					<form:option value="" label="全部"/>
					<form:options items="${roomList}" itemLabel="roomNo" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns">
				<label style="width:100px;"></label>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" style="width:135px;"/>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" type="${messageType}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>姓名</th>
				<th>性别</th>
				<th>租客类型</th>
				<th>所属企业</th>
				<th>证件类型</th>
				<th>证件号码</th>
				<th>出生日期</th>
				<th>学历</th>
				<th>手机号码</th>
				<th>电子邮箱</th>
				<th>户籍所在地</th>
				<th>职位</th>
				<th>跟进销售</th>
				<th>创建时间</th>
				<th>修改时间</th>
				<th>创建人</th>
				<th>修改人</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tenant">
			<tr>
				<td><a href="${ctx}/person/tenant/form?id=${tenant.id}">
					 ${tenant.tenantName}
				</a></td>
				<td>
					 ${fns:getDictLabel(tenant.gender, 'sex', '')}
				</td>
				<td>
					 ${fns:getDictLabel(tenant.tenantType, 'tenant_type', '')}
				</td>
				<td>
					${tenant.company.companyName}
				</td>
				<td>
					${fns:getDictLabel(tenant.idType, 'id_type', '')}
				</td>
				<td>
					${tenant.idNo}
				</td>
				<td>
					<fmt:formatDate value="${tenant.birthday}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					 ${fns:getDictLabel(tenant.degrees, 'degrees', '')}
				</td>
				<td>
					${tenant.cellPhone}
				</td>
				<td>
					${tenant.email}
				</td>
				<td>
					${tenant.houseRegister}
				</td>
				<td>
					${tenant.position}
				</td>
				<td>
					${tenant.user.name}
				</td>
				<td>
					<fmt:formatDate value="${tenant.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${tenant.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
				 	${tenant.createBy.loginName}
				</td>
				<td>
				 	${tenant.updateBy.loginName}
				</td>
				<td>
					${tenant.remarks}
				</td>
				<td>
				<shiro:hasPermission name="person:tenant:edit">
    				<a href="${ctx}/person/tenant/form?id=${tenant.id}">修改</a>
    			</shiro:hasPermission>
    			<shiro:hasPermission name="person:tenant:del">
					<a href="${ctx}/person/tenant/delete?id=${tenant.id}" onclick="return confirmx('确认要删除该租客信息吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>