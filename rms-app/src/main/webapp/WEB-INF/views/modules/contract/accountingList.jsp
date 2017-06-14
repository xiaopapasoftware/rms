<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>退租核算管理</title>
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
		<li class="active"><a href="${ctx}/contract/accounting/">退租核算列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="accounting" action="${ctx}/contract/accounting/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li>
				<label style="width:120px;">出租合同名称：</label>
				<form:input path="rentContractName" htmlEscape="false" class="input-medium" style="width:195px;"/>
			</li>
			<li>
				<label style="width:120px;">出租合同编号：</label>
				<form:input path="rentContractCode" htmlEscape="false" class="input-medium" style="width:195px;"/>
			</li>
			<li><label style="width:120px;">核算类型：</label>
				<form:select path="accountingType" class="input-medium" style="width:210px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('accounting_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">核算费用方向：</label>
				<form:select path="feeDirection" class="input-medium" style="width:210px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('fee_dirction')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">核算费用类别：</label>
				<form:select path="feeType" class="input-medium" style="width:210px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('fee_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">核算人：</label>
				<sys:treeselect id="user" name="user.id" value="${accounting.user.id}" labelName="user.name" labelValue="${accounting.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true" cssStyle="width:150px;"/>
			</li>
			<li><label style="width:120px;">核算时间：</label>
				<input name="feeDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${accounting.feeDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:192px;"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" type="${messageType}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>出租合同</th>
				<th>核算类型</th>
				<th>核算费用方向</th>
				<th>核算费用类别</th>
				<th>核算金额</th>
				<th>核算人</th>
				<th>核算时间</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="accounting">
			<tr>
				<td>
					${accounting.rentContractName}
				</td>
				<td>
					${fns:getDictLabel(accounting.accountingType, 'accounting_type', '')}
				</td>
				<td>
					${fns:getDictLabel(accounting.feeDirection, 'fee_dirction', '')}
				</td>
				<td>
					${fns:getDictLabel(accounting.feeType, 'fee_type', '')}
				</td>
				<td>
					${accounting.feeAmount}
				</td>
				<td>
					${accounting.user.name}
				</td>
				<td>
					<fmt:formatDate value="${accounting.feeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${accounting.remarks}
				</td>
				<td>
					<shiro:hasPermission name="contract:accounting:adminDelete">
    					 <c:if test="${accounting.transStatus=='0'}">
     					 	<a href="${ctx}/contract/accounting/deleteAccountingAndTrans?id=${accounting.id}" onclick="return confirmx('确认要删除核算记录及其款项吗?', this.href)">删除核算记录及其款项</a>
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