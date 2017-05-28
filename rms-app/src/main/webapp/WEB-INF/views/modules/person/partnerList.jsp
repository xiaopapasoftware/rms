<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合作人信息管理</title>
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
		<li class="active"><a href="${ctx}/person/partner/">合作人信息列表</a></li>
		<shiro:hasPermission name="person:partner:edit"><li><a href="${ctx}/person/partner/form">合作人信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="partner" action="${ctx}/person/partner/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>姓名：</label>
				<form:input path="partnerName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="cellPhone" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>座机号：</label>
				<form:input path="deskPhone" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>合作人类型：</label>
				<form:select path="partnerType" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('partner_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>开户人姓名：</label>
				<form:input path="userName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>开户行名称：</label>
				<form:input path="bankName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>开户行账号：</label>
				<form:input path="bankAccount" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" type="${messageType}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				
				<th>姓名</th>
				<th>手机号</th>
				<th>座机号</th>
				<th>合作人类型</th>
				<th>开户人姓名</th>
				<th>开户行名称</th>
				<th>开户行账号</th>
				<th>佣金百分比</th>
				<th>创建时间</th>
				<th>修改时间</th>
				<th>创建人</th>
				<th>修改人</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="partner">
			<tr>
				<td><a href="${ctx}/person/partner/form?id=${partner.id}">
					${partner.partnerName}
				</a></td>
				<td>
					${partner.cellPhone}
				</td>
				<td>
					${partner.deskPhone}
				</td>
				<td>
					${fns:getDictLabel(partner.partnerType, 'partner_type', '')}
				</td>
				<td>
					${partner.userName}
				</td>
				<td>
					${partner.bankName}
				</td>
				<td>
					${partner.bankAccount}
				</td>
				<td>
					${partner.commissionPercent}
				</td>
				<td>
					<fmt:formatDate value="${partner.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${partner.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
				 	${partner.createBy.loginName}
				</td>
				<td>
				 	${partner.updateBy.loginName}
				</td>
				<td>
					${partner.remarks}
				</td>
				<td>
				<shiro:hasPermission name="person:partner:edit">
    				<a href="${ctx}/person/partner/form?id=${partner.id}">修改</a>
    			</shiro:hasPermission>
    			<shiro:hasPermission name="person:partner:del">
					<a href="${ctx}/person/partner/delete?id=${partner.id}" onclick="return confirmx('确认要删除该合作人信息吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>