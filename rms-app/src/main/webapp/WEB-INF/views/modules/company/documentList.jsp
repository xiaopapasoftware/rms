<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>办公文件管理管理</title>
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/company/document/">办公文件管理列表</a></li>
		<shiro:hasPermission name="company:document:edit"><li><a href="${ctx}/company/document/form">办公文件管理添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="document" action="${ctx}/company/document/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>附件名称：</label>
				<form:input path="attachmentName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>附件类型：</label>
				<form:select path="attachmentType" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('document_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" type="${messageType}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>附件名称</th>
				<th>附件类型</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="company:document:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="document">
			<tr>
				<td><a href="${ctx}/company/document/form?id=${document.id}">
					${document.attachmentName}
				</a></td>
				<td>
					${fns:getDictLabel(document.attachmentType, 'document_type', '')}
				</td>
				<td>
					<fmt:formatDate value="${document.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${document.remarks}
				</td>
				<shiro:hasPermission name="company:document:edit"><td>
    				<a href="${ctx}/company/document/form?id=${document.id}">修改</a>
					<!--<a href="${ctx}/company/document/delete?id=${document.id}" onclick="return confirmx('确认要删除该办公文件管理吗？', this.href)">删除</a>-->
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>