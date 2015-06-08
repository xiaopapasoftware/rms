<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>定金协议管理</title>
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
		<li class="active"><a href="${ctx}/contract/depositAgreement/">定金协议列表</a></li>
		<shiro:hasPermission name="contract:depositAgreement:edit"><li><a href="${ctx}/contract/depositAgreement/form">定金协议添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="depositAgreement" action="${ctx}/contract/depositAgreement/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>物业项目：</label>
				<form:select path="propertyProject.id" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>楼宇：</label>
				<form:select path="building.id" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>房屋：</label>
				<form:select path="house.id" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>房间：</label>
				<form:select path="room.id" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>出租方式：</label>
				<form:select path="rentMode" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>销售：</label>
				<sys:treeselect id="user" name="user.id" value="${depositAgreement.user.id}" labelName="user.name" labelValue="${depositAgreement.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>定金协议名称：</label>
				<form:input path="agreementName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>协议开始时间：</label>
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${depositAgreement.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>协议结束时间：</label>
				<input name="expiredDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${depositAgreement.expiredDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>协议签订时间：</label>
				<input name="signDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${depositAgreement.signDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>首付房租月数：</label>
				<form:input path="renMonths" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>房租押金月数：</label>
				<form:input path="depositMonths" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>约定合同签约时间：</label>
				<input name="agreementDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${depositAgreement.agreementDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>定金协议审核状态：</label>
				<form:select path="agreementStatus" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>定金协议业务状态：</label>
				<form:select path="agreementBusiStatus" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>物业项目</th>
				<th>楼宇</th>
				<th>房屋</th>
				<th>房间</th>
				<th>出租方式</th>
				<th>销售</th>
				<th>定金协议名称</th>
				<th>协议开始时间</th>
				<th>协议结束时间</th>
				<th>协议签订时间</th>
				<th>首付房租月数</th>
				<th>房租押金月数</th>
				<th>约定合同签约时间</th>
				<th>定金协议审核状态</th>
				<th>定金协议业务状态</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="contract:depositAgreement:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="depositAgreement">
			<tr>
				<td><a href="${ctx}/contract/depositAgreement/form?id=${depositAgreement.id}">
					${fns:getDictLabel(depositAgreement.propertyProject.id, '', '')}
				</a></td>
				<td>
					${fns:getDictLabel(depositAgreement.building.id, '', '')}
				</td>
				<td>
					${fns:getDictLabel(depositAgreement.house.id, '', '')}
				</td>
				<td>
					${fns:getDictLabel(depositAgreement.room.id, '', '')}
				</td>
				<td>
					${fns:getDictLabel(depositAgreement.rentMode, '', '')}
				</td>
				<td>
					${depositAgreement.user.name}
				</td>
				<td>
					${depositAgreement.agreementName}
				</td>
				<td>
					<fmt:formatDate value="${depositAgreement.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${depositAgreement.expiredDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${depositAgreement.signDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${depositAgreement.renMonths}
				</td>
				<td>
					${depositAgreement.depositMonths}
				</td>
				<td>
					<fmt:formatDate value="${depositAgreement.agreementDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(depositAgreement.agreementStatus, '', '')}
				</td>
				<td>
					${fns:getDictLabel(depositAgreement.agreementBusiStatus, '', '')}
				</td>
				<td>
					<fmt:formatDate value="${depositAgreement.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${depositAgreement.remarks}
				</td>
				<shiro:hasPermission name="contract:depositAgreement:edit"><td>
    				<a href="${ctx}/contract/depositAgreement/form?id=${depositAgreement.id}">修改</a>
					<a href="${ctx}/contract/depositAgreement/delete?id=${depositAgreement.id}" onclick="return confirmx('确认要删除该定金协议吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>