<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>科技侠门锁钥匙管理</title>
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
		<li class="active"><a href="${ctx}/lock/scienerLockKey/">钥匙列表</a></li>
		<shiro:hasPermission name="person:neighborhoodContact:edit"><li><a href="${ctx}/lock/scienerLockKey/form">分配钥匙</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="key" action="${ctx}/lock/scienerLockKey/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
            <li><label>门锁：</label>
                <form:select path="lockId" class="input-medium">
                    <form:option value="" label="请选择..."/>
                    <c:forEach items="${lockList}" var="item">
                        <form:option value="${item.room_id}" label="${item.lock_alias}"/>
                    </c:forEach>
                </form:select>
            </li>
            <li><label>用户：</label>
                <form:select path="username" class="input-medium">
                    <form:option value="" label="请选择..."/>
                    <form:options items="${users}" itemLabel="phoneAndName" itemValue="scienerUserName" />
                </form:select>
            </li>
            <li><label>钥匙状态：</label>
                <form:select path="keyStatus" class="input-medium" style="width:200px;">
                <form:option value="" label="全部"/>
                <form:options items="${fns:getDictList('sciener_key_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
                <th>门锁名称</th>
                <th>钥匙编号</th>
                <th>用户</th>
                <th>用户科技侠账号</th>
				<th>钥匙类型</th>
                <th>生效时间</th>
				<th>结束时间</th>
				<th>钥匙状态</th>
                <th>备注</th>
				<th>分配时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${keys}" var="key">
			<tr>
				<td>
				    ${key.lock_alias}
				</td>
				<td>
                    ${key.keyId}
				</td>
                <td>
                        ${key.appUserName}
                </td>
				<td>
				    ${key.username}
				</td>
				<td>
                    <c:if test="${key.keyType=='0'}">
                        永久
                    </c:if>
                    <c:if test="${key.keyType=='1'}">
                        限时
                    </c:if>
				</td>
				<td>
                    <c:if test="${key.keyType!='0'}">
                        <fmt:formatDate value="${key.startDate}" pattern="yyyy-MM-dd HH:mm"/>
                    </c:if>

				</td>
				<td>
                    <c:if test="${key.keyType!='0'}">
                        <fmt:formatDate value="${key.endDate}" pattern="yyyy-MM-dd HH:mm"/>
                    </c:if>
				</td>
				<td>
                    ${fns:getDictLabels(key.keyStatus, 'sciener_key_status', '')}
				</td>
                <td>
                        ${key.remarks}
                </td>
				<td>
                    <fmt:formatDate value="${key.date}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>

				<td>
					<a href="${ctx}/lock/scienerLockKey/delete?keyId=${key.keyId}&lockId=${key.lockId}&openid=${key.openid}" onclick="return confirmx('确认要删除该钥匙吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>