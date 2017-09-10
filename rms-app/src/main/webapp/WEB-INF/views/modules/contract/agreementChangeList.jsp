<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>协议变更管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		function auditHis(id) {
			$.jBox.open("iframe:${ctx}/contract/leaseContract/auditHis?objectId="+id,'审核记录',650,400,{buttons:{'关闭':true}});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/contract/agreementChange/">协议变更列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="agreementChange"
		action="${ctx}/contract/agreementChange/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<ul class="ul-form">
			<li><label style="width: 120px;">出租合同：</label> <form:input
					path="rentContractName" htmlEscape="false" maxlength="64"
					class="input-medium" style="width:195px;" /></li>
			<li><label style="width: 120px;">合同变更协议名称：</label> <form:input
					path="agreementChangeName" htmlEscape="false" maxlength="64"
					class="input-medium" style="width:195px;" /></li>
			<li><label style="width: 120px;">协议生效时间：</label> <input
				name="startDate" type="text" readonly="readonly" maxlength="20"
				class="input-medium Wdate"
				value="<fmt:formatDate value="${agreementChange.startDate}" pattern="yyyy-MM-dd"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
				style="width: 196px;" /></li>
			<li><label style="width: 120px;">协议审核状态：</label> <form:select
					path="agreementStatus" class="input-medium" style="width:210px;">
					<form:option value="" label="全部" />
					<form:options items="${fns:getDictList('contract_status')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select></li>

			<shiro:hasPermission name="contract:agreementChange:view">
				<li class="btns"><input id="btnSubmit" class="btn btn-primary"
					type="submit" value="查询" /></li>
			</shiro:hasPermission>

			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" type="${messageType}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>合同变更协议名称</th>
				<th>出租合同</th>
				<th>协议生效时间</th>
				<th>首付房租月数</th>
				<th>房租押金月数</th>
				<th>协议审核状态</th>
				<th>申请人</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="agreementChange">
				<tr>
					<td><a
						href="${ctx}/contract/agreementChange/form?id=${agreementChange.id}">
							${agreementChange.agreementChangeName} </a></td>
					<td>${agreementChange.rentContractName}</td>
					<td><fmt:formatDate value="${agreementChange.startDate}"
							pattern="yyyy-MM-dd" /></td>
					<td>${agreementChange.renMonths}</td>
					<td>${agreementChange.depositMonths}</td>
					<td>${fns:getDictLabel(agreementChange.agreementStatus, 'contract_status', '')}
					</td>
					<td>${agreementChange.createUserName}</td>
					<td><fmt:formatDate value="${agreementChange.updateDate}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${agreementChange.remarks}</td>
					<td><shiro:hasPermission name="contract:agreementChange:edit">
							<c:if
								test="${agreementChange.agreementStatus=='0'||agreementChange.agreementStatus=='2'}">
								<a
									href="${ctx}/contract/agreementChange/form?id=${agreementChange.id}">修改</a>
							</c:if>
						</shiro:hasPermission> <c:if
							test="${agreementChange.agreementStatus=='1'||agreementChange.agreementStatus=='2'}">
							<a href="javascript:void(0);"
								onclick="auditHis('${agreementChange.id}')">审核记录</a>
						</c:if></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>