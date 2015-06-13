<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>租客信息管理</title>
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
		<li class="active"><a href="${ctx}/person/tenant/">租客信息列表</a></li>
		<shiro:hasPermission name="person:tenant:edit"><li><a href="${ctx}/person/tenant/form">租客信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="tenant" action="${ctx}/person/tenant/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>定金协议：</label>
				<form:input path="depositAgreement.id" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>承租合同：</label>
				<form:input path="leaseContract.id" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>出租合同：</label>
				<form:input path="rentContract.id" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>承租合同变更协议：</label>
				<form:input path="leaseContractChangeId" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>出租合同变更协议：</label>
				<form:input path="rentContractChangeId" htmlEscape="false" maxlength="300" class="input-medium"/>
			</li>
			<li><label>租客类型：</label>
				<form:select path="tenantType" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>企业：</label>
				<form:input path="companyId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>姓名：</label>
				<form:input path="tenantName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>性别：</label>
				<form:select path="gender" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>证件类型：</label>
				<form:select path="idType" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>证件号码：</label>
				<form:input path="idNo" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>出生日期：</label>
				<input name="birthday" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${tenant.birthday}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>学历：</label>
				<form:select path="degrees" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>手机号码：</label>
				<form:input path="cellPhone" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>电子邮箱：</label>
				<form:input path="email" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>户籍所在地：</label>
				<form:input path="houseRegister" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>职位：</label>
				<form:input path="position" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>定金协议</th>
				<th>承租合同</th>
				<th>出租合同</th>
				<th>承租合同变更协议</th>
				<th>出租合同变更协议</th>
				<th>租客类型</th>
				<th>企业</th>
				<th>姓名</th>
				<th>性别</th>
				<th>证件类型</th>
				<th>证件号码</th>
				<th>出生日期</th>
				<th>学历</th>
				<th>手机号码</th>
				<th>电子邮箱</th>
				<th>户籍所在地</th>
				<th>职位</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="person:tenant:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tenant">
			<tr>
				<td><a href="${ctx}/person/tenant/form?id=${tenant.id}">
					${tenant.depositAgreement.id}
				</a></td>
				<td>
					${tenant.leaseContract.id}
				</td>
				<td>
					${tenant.rentContract.id}
				</td>
				<td>
					${tenant.leaseContractChangeId}
				</td>
				<td>
					${tenant.rentContractChangeId}
				</td>
				<td>
					${fns:getDictLabel(tenant.tenantType, '', '')}
				</td>
				<td>
					${tenant.companyId}
				</td>
				<td>
					${tenant.tenantName}
				</td>
				<td>
					${fns:getDictLabel(tenant.gender, '', '')}
				</td>
				<td>
					${fns:getDictLabel(tenant.idType, '', '')}
				</td>
				<td>
					${tenant.idNo}
				</td>
				<td>
					<fmt:formatDate value="${tenant.birthday}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(tenant.degrees, '', '')}
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
					<fmt:formatDate value="${tenant.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${tenant.remarks}
				</td>
				<shiro:hasPermission name="person:tenant:edit"><td>
    				<a href="${ctx}/person/tenant/form?id=${tenant.id}">修改</a>
					<a href="${ctx}/person/tenant/delete?id=${tenant.id}" onclick="return confirmx('确认要删除该租客信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>