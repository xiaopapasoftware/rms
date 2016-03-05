<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>预约信息管理</title>
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
		<li class="active"><a href="${ctx}/contract/book/">预约信息列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="contractBook" action="${ctx}/contract/book/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>预约姓名：</label>
				<form:input path="userName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>预约电话：</label>
				<form:input path="userPhone" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>预约时间：</label>
				<input name="bookDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${contractBook.bookDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width:196px;"/>
			</li>
			<li><label>预约状态：</label>
				<form:select path="bookStatus" class="input-medium" style="width:210px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('book_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>房屋号</th>
				<th>房间号</th>
				<th>预约姓名</th>
				<th>预约电话</th>
				<th>预约时间</th>
				<th>预约状态</th>
				<th>房屋管家</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="contractBook">
			<tr>
				<td>
					${contractBook.projectName}
				</td>
				<td>
					${contractBook.buildingName}
				</td>
				<td>
					${contractBook.houseCode}
				</td>
				<td>
					${contractBook.roomNo}
				</td>
				<td>
					${contractBook.userName}
				</td>
				<td>
					${contractBook.userPhone}
				</td>
				<td>
					<fmt:formatDate value="${contractBook.bookDate}" pattern="yyyy-MM-dd HH:mm"/>
				</td>
				<td>
					${fns:getDictLabel(contractBook.bookStatus, 'book_status', '')}
				</td>
				<td>
					${contractBook.salesName}
				</td>
				<td>
					${contractBook.remarks}
				</td>
				<td>
				<shiro:hasPermission name="contract:contractBook:edit">
				  <c:if test="${contractBook.bookStatus=='0'}">
					<a href="${ctx}/contract/book/confirm?id=${contractBook.id}&userId=${contractBook.userId}" onclick="return confirm('确认该预约信息吗？', this.href)">确认</a>
					<a href="${ctx}/contract/book/cancel?id=${contractBook.id}&userId=${contractBook.userId}" onclick="return confirm('取消该预约信息吗？', this.href)">取消</a>
				  </c:if>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>