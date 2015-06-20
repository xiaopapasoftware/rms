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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/contract/rentContract/">出租合同列表</a></li>
		<shiro:hasPermission name="contract:rentContract:edit"><li><a href="${ctx}/contract/rentContract/form">出租合同添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="rentContract" action="${ctx}/contract/rentContract/" method="post" class="breadcrumb form-search"
		cssStyle="width:1145px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<!-- <li><label>原出租合同：</label>
				<form:input path="contractId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li> -->
			<li><label style="width:120px;">合同名称：</label>
				<form:input path="contractName" htmlEscape="false" maxlength="100" class="input-medium" style="width:185px;"/>
			</li>
			<li><label style="width:120px;">出租方式：</label>
				<form:select path="rentMode" class="input-medium" style="width:210px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('rent_mode')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">物业项目：</label>
				<form:select path="propertyProject.id" class="input-medium" style="width:200px;">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">楼宇：</label>
				<form:select path="building.id" class="input-medium" style="width:200px;">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">房屋：</label>
				<form:select path="house.id" class="input-medium" style="width:210px;">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">房间：</label>
				<form:select path="room.id" class="input-medium" style="width:200px;">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">销售：</label>
				<sys:treeselect id="user" name="user.id" value="${rentContract.user.id}" labelName="user.name" labelValue="${rentContract.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true" cssStyle="width:140px;"/>
			</li>
			<li><label style="width:120px;">合同来源：</label>
				<form:select path="contractSource" class="input-medium" style="width:210px;">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">月租金：</label>
				<form:input path="rental" htmlEscape="false" class="input-medium" style="width:185px;"/>
			</li>
			<li><label style="width:120px;">合同生效时间：</label>
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${rentContract.startDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd、',isShowClear:false});" style="width:186px;"/>
			</li>
			<li><label style="width:120px;">合同过期时间：</label>
				<input name="expiredDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${rentContract.expiredDate}" pattern="yyyy-MM-dd、"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd、',isShowClear:false});" style="width:196px;"/>
			</li>
			<li><label style="width:120px;">合同签订时间：</label>
				<input name="signDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${rentContract.signDate}" pattern="yyyy-MM-dd、"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd、',isShowClear:false});" style="width:186px;"/>
			</li>
			<li><label style="width:120px;">合同状态：</label>
				<form:select path="contractStatus" class="input-medium" style="width:200px;">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('rent_contract_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">合同业务状态：</label>
				<form:select path="contractBusiStatus" class="input-medium" style="width:210px;">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('rent_contract_busi_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="width:1180px;">
		<thead>
			<tr>
				<!--<th>原出租合同</th>-->
				<th>合同名称</th>
				<th>出租方式</th>
				<th>物业项目</th>
				<th>楼宇</th>
				<th>房屋</th>
				<th>房间</th>
				<th>销售</th>
				<th>合同来源</th>
				<th>月租金</th>
				<th>合同生效时间</th>
				<th>合同过期时间</th>
				<th>合同签订时间</th>
				<th>合同状态</th>
				<th>合同业务状态</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="contract:rentContract:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="rentContract">
			<tr>
				<!--<td><a href="${ctx}/contract/rentContract/form?id=${rentContract.id}">
					${rentContract.contractId}
				</a></td>-->
				<td>
					<a href="${ctx}/contract/rentContract/form?id=${rentContract.id}">
					${rentContract.contractName}
					</a>
				</td>
				<td>
					${fns:getDictLabel(rentContract.rentMode, 'rent_mode', '')}
				</td>
				<td>
					${fns:getDictLabel(rentContract.propertyProject.id, '', '')}
				</td>
				<td>
					${fns:getDictLabel(rentContract.building.id, '', '')}
				</td>
				<td>
					${fns:getDictLabel(rentContract.house.id, '', '')}
				</td>
				<td>
					${fns:getDictLabel(rentContract.room.id, '', '')}
				</td>
				<td>
					${rentContract.user.name}
				</td>
				<td>
					${fns:getDictLabel(rentContract.contractSource, '', '')}
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
					${fns:getDictLabel(rentContract.contractStatus, '', '')}
				</td>
				<td>
					${fns:getDictLabel(rentContract.contractBusiStatus, '', '')}
				</td>
				<td>
					<fmt:formatDate value="${rentContract.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${rentContract.remarks}
				</td>
				<shiro:hasPermission name="contract:rentContract:edit"><td>
    				<a href="${ctx}/contract/rentContract/form?id=${rentContract.id}">修改</a>
					<a href="${ctx}/contract/rentContract/delete?id=${rentContract.id}" onclick="return confirmx('确认要删除该出租合同吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>