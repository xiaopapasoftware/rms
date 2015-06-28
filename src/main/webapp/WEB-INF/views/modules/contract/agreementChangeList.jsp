<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>协议变更管理</title>
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
		<li class="active"><a href="${ctx}/contract/agreementChange/">协议变更列表</a></li>
		<shiro:hasPermission name="contract:agreementChange:edit"><li><a href="${ctx}/contract/agreementChange/form">协议变更添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="agreementChange" action="${ctx}/contract/agreementChange/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>出租合同：</label>
				<form:select path="rentContract.id" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>合同变更协议名称：</label>
				<form:input path="agreementChangeName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>协议生效时间：</label>
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${agreementChange.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>出租方式：</label>
				<form:select path="rentMode" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>协议审核状态：</label>
				<form:select path="agreementStatus" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>核算人：</label>
				<sys:treeselect id="user" name="user.id" value="${agreementChange.user.id}" labelName="user.name" labelValue="${agreementChange.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>出租合同</th>
				<th>合同变更协议名称</th>
				<th>协议生效时间</th>
				<th>出租方式</th>
				<th>协议审核状态</th>
				<th>核算人</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="contract:agreementChange:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="agreementChange">
			<tr>
				<td><a href="${ctx}/contract/agreementChange/form?id=${agreementChange.id}">
					${fns:getDictLabel(agreementChange.rentContract.id, '', '')}
				</a></td>
				<td>
					${agreementChange.agreementChangeName}
				</td>
				<td>
					<fmt:formatDate value="${agreementChange.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(agreementChange.rentMode, '', '')}
				</td>
				<td>
					${fns:getDictLabel(agreementChange.agreementStatus, '', '')}
				</td>
				<td>
					${agreementChange.user.name}
				</td>
				<td>
					<fmt:formatDate value="${agreementChange.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${agreementChange.remarks}
				</td>
				<shiro:hasPermission name="contract:agreementChange:edit"><td>
    				<a href="${ctx}/contract/agreementChange/form?id=${agreementChange.id}">修改</a>
					<a href="${ctx}/contract/agreementChange/delete?id=${agreementChange.id}" onclick="return confirmx('确认要删除该协议变更吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>