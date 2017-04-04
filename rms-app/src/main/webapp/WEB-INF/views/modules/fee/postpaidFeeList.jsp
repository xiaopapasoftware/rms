<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>后付费查询</title>
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
		<li><a href="${ctx}/contract/rentContract/queryPublicBasicFeeInfo">合同初始事业费管理</a></li>
		<li class="active"><a href="${ctx}/fee/electricFee/postpaidFeeList">后付费查询</a></li>
		<li><a href="${ctx}/fee/electricFee/postpaidFeeForm">后付费付款</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="postpaidFee" action="${ctx}/fee/electricFee/postpaidFeeList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="width:120px;">出租合同：</label>
				<form:input path="contractName" htmlEscape="false" maxlength="64" class="input-medium" style="width:195px;"/>
			</li>
			<li><label style="width:120px;">费用类型：</label>
				<form:select path="publicFeeType" class="input-medium" style="width:210px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('public_fee_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">付费状态：</label>
				<form:select path="payStatus" class="input-medium" style="width:210px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('public_fee_pay_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">结算状态：</label>
				<form:select path="settleStatus" class="input-medium" style="width:210px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('settle_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">付费开始日期：</label>
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${postpaidFee.startDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:196px;"/>
			</li>
			<li><label style="width:120px;">付费结束日期：</label>
				<input name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${postpaidFee.endDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:196px;"/>
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
				<th>付费类型</th>
				<th>付费状态</th>
				<th>结算状态</th>
				<th>付费金额</th>
				<th>付费日期</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<th>创建人</th>
				<th>更新人</th>
				<th>备注信息</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="postpaidFee">
			<tr>
				<td>
					${postpaidFee.contractName}
				</td>
				<td>
					${fns:getDictLabel(postpaidFee.publicFeeType, 'public_fee_type', '')}
				</td>
				<td>
					${fns:getDictLabel(postpaidFee.payStatus, 'public_fee_pay_status', '')}
				</td>
				<td>
					${fns:getDictLabel(postpaidFee.settleStatus, 'settle_status', '')}
				</td>
				<td>
					${postpaidFee.payAmount}
				</td>
				<td>
					<fmt:formatDate value="${postpaidFee.payDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${postpaidFee.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${postpaidFee.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>${postpaidFee.createBy.loginName}</td>
				<td>${postpaidFee.updateBy.loginName}</td>
				<td>${postpaidFee.remarks}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>