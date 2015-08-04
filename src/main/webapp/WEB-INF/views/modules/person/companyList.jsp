<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>企业信息管理</title>
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
		<li class="active"><a href="${ctx}/person/company/">企业信息列表</a></li>
		<shiro:hasPermission name="person:company:edit"><li><a href="${ctx}/person/company/form">企业信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="company" action="${ctx}/person/company/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="width:100px;">企业名称：</label>
				<form:input path="companyName" htmlEscape="false" maxlength="100" class="input-medium" style="width:177px;"/>
			</li>
			<li><label style="width:100px;">企业证件类型：</label>
				<form:select path="idType" class="input-medium" style="width:190px;">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('comp_idtype')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:100px;">企业证件号码：</label>
				<form:input path="idNo" htmlEscape="false" maxlength="100" class="input-medium" style="width:177px;"/>
			</li>
			<li><label style="width:100px;">企业电话：</label>
				<form:input path="tellPhone" htmlEscape="false" maxlength="100" class="input-medium" style="width:177px;"/>
			</li>
			<li><label style="width:100px;">企业注册地址：</label>
				<form:input path="companyAdress" htmlEscape="false" maxlength="100" class="input-medium" style="width:177px;"/>
			</li>
			<li><label style="width:100px;">企业营业地址：</label>
				<form:input path="businessAdress" htmlEscape="false" maxlength="100" class="input-medium" style="width:177px;"/>
			</li>
			<li><label style="width:100px;">开户行名称：</label>
				<form:input path="bankName" htmlEscape="false" maxlength="100" class="input-medium" style="width:177px;"/>
			</li>
			<li><label style="width:100px;">开户行账号：</label>
				<form:input path="bankAccount" htmlEscape="false" maxlength="100" class="input-medium" style="width:177px;"/>
			</li>
			<li class="btns" style="width:100px;"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>企业名称</th>
				<th>企业证件类型</th>
				<th>企业证件号码</th>
				<th>企业电话</th>
				<th>企业注册地址</th>
				<th>企业营业地址</th>
				<th>开户行名称</th>
				<th>开户行账号</th>
				<th>创建时间</th>
				<th>修改时间</th>
				<th>创建人</th>
				<th>修改人</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="company">
			<tr>
				<td><a href="${ctx}/person/company/form?id=${company.id}">
					${company.companyName}
				</a></td>
				<td>
					${fns:getDictLabel(company.idType, 'comp_idtype', '')}
				</td>
				<td>
					${company.idNo}
				</td>
				<td>
					${company.tellPhone}
				</td>
				<td>
					${company.companyAdress}
				</td>
				<td>
					${company.businessAdress}
				</td>
				<td>
					${company.bankName}
				</td>
				<td>
					${company.bankAccount}
				</td>
				<td>
					<fmt:formatDate value="${company.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${company.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
				 	${company.createBy.loginName}
				</td>
				<td>
				 	${company.updateBy.loginName}
				</td>
				<td>
					${company.remarks}
				</td>
				<td>
				<shiro:hasPermission name="person:company:edit">
    				<a href="${ctx}/person/company/form?id=${company.id}">修改</a>
    			</shiro:hasPermission>
    			<shiro:hasPermission name="person:company:del">
					<a href="${ctx}/person/company/delete?id=${company.id}" onclick="return confirmx('确认要删除该企业及其联系人信息吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>