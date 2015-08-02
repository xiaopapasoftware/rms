<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>电费结算管理</title>
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
		<li class="active"><a href="${ctx}/fee/electricFee/">电费管理列表</a></li>
		<li><a href="${ctx}/fee/electricFee/form">电费充值</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="electricFee" action="${ctx}/fee/electricFee/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="width:120px;">出租合同：</label>
				<form:input path="contractName" htmlEscape="false" maxlength="64" class="input-medium" style="width:195px;"/>
			</li>
			<li><label style="width:120px;">电费缴纳开始时间：</label>
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${electricFee.startDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width:196px;"/>
			</li>
			<li><label style="width:120px;">电费缴纳结束时间：</label>
				<input name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${electricFee.endDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width:196px;"/>
			</li>
			<li><label style="width:120px;">结算状态：</label>
				<form:select path="settleStatus" class="input-medium" style="width:210px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('settle_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>出租合同</th>
				<th>结算类型</th>
				<th>电费缴纳开始时间</th>
				<th>电费缴纳结束时间</th>
				<th>入住电表系数</th>
				<th>充值金额</th>
				<th>结算状态</th>
				<th>更新时间</th>
				<th>备注信息</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="electricFee">
			<tr>
				<td>
					${electricFee.contractName}
				</td>
				<td>
					${fns:getDictLabel(electricFee.settleType, 'electric_settle_type', '')}
				</td>
				<td>
					<fmt:formatDate value="${electricFee.startDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${electricFee.endDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${electricFee.meterValue}
				</td>
				<td>
					${electricFee.personFee}
				</td>
				<td>
					${fns:getDictLabel(electricFee.settleStatus, 'settle_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${electricFee.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${electricFee.remarks}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>