<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>承租合同管理</title>
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
		<li class="active"><a href="${ctx}/contract/leaseContract/">承租合同列表</a></li>
		<shiro:hasPermission name="contract:leaseContract:edit"><li><a href="${ctx}/contract/leaseContract/form">承租合同添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="leaseContract" action="${ctx}/contract/leaseContract/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>物业项目：</label>
				<form:select path="propertyProject.id" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>楼宇：</label>
				<form:select path="building.id" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>房屋：</label>
				<form:select path="house.id" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>汇款人：</label>
				<form:input path="remittancer.id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>承租合同名称：</label>
				<form:input path="contractName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>合同生效时间：</label>
				<input name="effectiveDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${leaseContract.effectiveDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>首次打款日期：</label>
				<input name="firstRemittanceDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${leaseContract.firstRemittanceDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>打款日期：</label>
				<form:select path="remittanceDate" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>合同过期时间：</label>
				<input name="expiredDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${leaseContract.expiredDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>合同签订时间：</label>
				<input name="contractDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${leaseContract.contractDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>承租押金：</label>
				<form:input path="deposit" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>合同审核状态：</label>
				<form:select path="contractStatus" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>房屋</th>
				<th>汇款人</th>
				<th>承租合同名称</th>
				<th>合同生效时间</th>
				<th>首次打款日期</th>
				<th>打款日期</th>
				<th>合同过期时间</th>
				<th>合同签订时间</th>
				<th>承租押金</th>
				<th>合同审核状态</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="contract:leaseContract:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="leaseContract">
			<tr>
				<td><a href="${ctx}/contract/leaseContract/form?id=${leaseContract.id}">
					${fns:getDictLabel(leaseContract.propertyProject.id, '', '')}
				</a></td>
				<td>
					${fns:getDictLabel(leaseContract.building.id, '', '')}
				</td>
				<td>
					${fns:getDictLabel(leaseContract.house.id, '', '')}
				</td>
				<td>
					${leaseContract.remittancer.id}
				</td>
				<td>
					${leaseContract.contractName}
				</td>
				<td>
					<fmt:formatDate value="${leaseContract.effectiveDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${leaseContract.firstRemittanceDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(leaseContract.remittanceDate, '', '')}
				</td>
				<td>
					<fmt:formatDate value="${leaseContract.expiredDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${leaseContract.contractDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${leaseContract.deposit}
				</td>
				<td>
					${fns:getDictLabel(leaseContract.contractStatus, '', '')}
				</td>
				<td>
					<fmt:formatDate value="${leaseContract.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${leaseContract.remarks}
				</td>
				<shiro:hasPermission name="contract:leaseContract:edit"><td>
    				<a href="${ctx}/contract/leaseContract/form?id=${leaseContract.id}">修改</a>
					<a href="${ctx}/contract/leaseContract/delete?id=${leaseContract.id}" onclick="return confirmx('确认要删除该承租合同吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>