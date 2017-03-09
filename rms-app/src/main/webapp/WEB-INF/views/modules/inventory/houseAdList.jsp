<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>广告管理管理</title>
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
		<li class="active"><a href="#">广告管理列表</a></li>
		<shiro:hasPermission name="inventory:ad:edit"><li><a href="${ctx}/inventory/ad/form">广告管理添加</a></li></shiro:hasPermission>
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>广告名称</th>
				<th>广告类型</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="inventory:ad:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="houseAd">
			<tr>
				<td>
				  <a href="${ctx}/inventory/ad/form?id=${houseAd.id}">${houseAd.adName}</a>
				</td>
				<td>${fns:getDictLabel(houseAd.adType, 'ad_type', '')}</td>
				<td>
					<fmt:formatDate value="${houseAd.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${houseAd.remarks}
				</td>
				<shiro:hasPermission name="inventory:ad:edit"><td>
    				<a href="${ctx}/inventory/ad/form?id=${houseAd.id}">修改</a>
					<a href="${ctx}/inventory/ad/delete?id=${houseAd.id}" onclick="return confirmx('确认要删除该广告吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>