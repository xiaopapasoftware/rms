<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>常见问题管理</title>
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
		<li class="active"><a href="${ctx}/app/questions/">常见问题列表</a></li>
		<li><a href="${ctx}/app/questions/form">常见问题添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="questions" action="${ctx}/app/questions/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>问题：</label>
				<form:input path="question" htmlEscape="false" maxlength="2000" class="input-medium"/>
			</li>
			<li><label>回答：</label>
				<form:input path="answer" htmlEscape="false" maxlength="2000" class="input-medium"/>
			</li>
			<li><label>排序：</label>
				<form:input path="sort" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>备注信息：</label>
				<form:input path="remarks" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>问题</th>
				<th>回答</th>
				<th>排序</th>
				<th>创建者</th>
				<th>创建时间</th>
				<th>更新者</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="questions">
			<tr>
				<td><a href="${ctx}/app/questions/form?id=${questions.id}">
					${questions.question}
				</a></td>
				<td>
					${questions.answer}
				</td>
				<td>
					${questions.sort}
				</td>
				<td>
					${questions.createBy.id}
				</td>
				<td>
					<fmt:formatDate value="${questions.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${questions.updateBy.id}
				</td>
				<td>
					<fmt:formatDate value="${questions.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${questions.remarks}
				</td>
				<td>
    				<a href="${ctx}/app/questions/form?id=${questions.id}">修改</a>
					<a href="${ctx}/app/questions/delete?id=${questions.id}" onclick="return confirmx('确认要删除该常见问题吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>