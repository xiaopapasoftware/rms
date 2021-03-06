<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>电费充值管理</title>
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
		<li class="active"><a href="${ctx}/fee/electricFee">电费充值列表</a></li>
		<li><a href="${ctx}/fee/electricFee/form">电费充值</a></li>
		<li><a href="${ctx}/fee/electricFee/viewUseInfo">电费使用查询</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="electricFee" action="${ctx}/fee/electricFee/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="width:120px;">出租合同名称：</label>
				<form:input path="contractName" htmlEscape="false" maxlength="64" class="input-medium" style="width:195px;"/>
			</li>
			<li><label style="width:120px;">出租合同编号：</label>
				<form:input path="contractCode" htmlEscape="false" maxlength="64" class="input-medium" style="width:195px;"/>
			</li>
			<li><label style="width:120px;">电费充值开始时间：</label>
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${electricFee.startDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:196px;"/>
			</li>
			<li><label style="width:120px;">电费充值结束时间：</label>
				<input name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${electricFee.endDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:196px;"/>
			</li>
			<li><label style="width:120px;">充值状态：</label>
				<form:select path="chargeStatus" class="input-medium" style="width:210px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('charge_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">结算状态：</label>
				<form:select path="settleStatus" class="input-medium" style="width:210px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('settle_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<shiro:hasPermission name="fee:electricFee:view">
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			</shiro:hasPermission>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" type="${messageType}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>合同名称</th>
				<th>合同编号</th>
				<th>充值时间</th>
				<th>充值金额</th>
				<th>充值状态</th>
				<th>结算状态</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<th>创建人</th>
				<th>更新人</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="electricFee">
			<tr>
				<td>
					${electricFee.contractName}
				</td>
				<td>
					${electricFee.contractCode}
				</td>
				<td>
					<fmt:formatDate value="${electricFee.chargeDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${electricFee.chargeAmount}
				</td>
			 	<td>
					${fns:getDictLabel(electricFee.chargeStatus, 'charge_status', '')}
				</td>
				<td>
					${fns:getDictLabel(electricFee.settleStatus, 'settle_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${electricFee.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${electricFee.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>${electricFee.createBy.loginName}</td>
				<td>${electricFee.updateBy.loginName}</td>
				<td>${electricFee.remarks}</td>
				<td>
					<shiro:hasPermission name="fee:electricFee:retryFail">
						<c:if test="${electricFee.chargeStatus=='2' && electricFee.settleStatus=='2' && electricFee.tradingAccountStatus =='1'}">
							 <a href="${ctx}/fee/electricFee/retryFail?id=${electricFee.id}">失败重试</a>
						</c:if>
					</shiro:hasPermission>
					<shiro:hasPermission name="fee:electricFee:remove">
						<c:if test="${(electricFee.chargeStatus=='0' && electricFee.settleStatus=='0') || (electricFee.chargeStatus=='1' && electricFee.settleStatus=='3'&& electricFee.tradeType=='11' && electricFee.tradingAccountStatus =='1')}">
							 <a href="${ctx}/fee/electricFee/removeEletricFee?id=${electricFee.id}">删除</a>
						</c:if>
						<c:if test="${(electricFee.tradeType=='11' || electricFee.tradeType=='' || electricFee.tradeType == null) && (electricFee.chargeStatus=='0' || electricFee.chargeStatus=='2')}">
							 <a href="${ctx}/fee/electricFee/form?id=${electricFee.id}">修改</a>
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