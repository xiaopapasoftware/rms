<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>款项交易管理</title>
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
		function register() {
			var transIds = $("input[name='transIds']:checked").length;
			var check = true;
			if(transIds < 1) {
				top.$.jBox.tip('请选择款项.','warning');
				return;
			} else if(transIds>1){
				for(var i=1;i<transIds;i++) {
					if($("input[name='transIds']:checked").eq(i).attr("transId") != $("input[name='transIds']:checked").eq(i-1).attr("transId")) {
						check = false;
						break;
					}
				}
			}
			if(check) {
				var transId=new Array();
				for(var i=0;i<transIds;i++) {
					transId.push($("input[name='transIds']:checked").eq(i).val());
				}
				window.location.href="${ctx}/funds/tradingAccounts/form?transIds="+transId.join(",")+"&tradeId="+$("input[name='transIds']:checked").eq(0).attr("transId")
						+"&tradeName="+$("input[name='transIds']:checked").eq(0).attr("transName");
			} else {
				top.$.jBox.tip('勾选的款项来自不同的合同,不能一并到账登记.','warning');
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/funds/paymentTrans/">款项交易列表</a></li>
		<shiro:hasPermission name="funds:paymentTrans:edit">
			<li><a href="javascript:void(0);" onclick="register()">到账登记</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="paymentTrans" action="${ctx}/funds/paymentTrans/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="width:120px;">交易对象：</label>
				<form:input path="transName" htmlEscape="false" maxlength="64" class="input-medium" style="width:185px;"/>
			</li>
			<li><label style="width:120px;">交易类型：</label>
				<form:select path="tradeType" class="input-medium" style="width:200px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('trans_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">款项类型：</label>
				<form:select path="paymentType" class="input-medium" style="width:200px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('payment_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">交易款项方向：</label>
				<form:select path="tradeDirection" class="input-medium" style="width:200px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('fee_dirction')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">交易款项状态：</label>
				<form:select path="transStatus" class="input-medium" style="width:200px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('trade_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th width="15"></th>
				<th>交易对象</th>
				<th>交易类型</th>
				<th>款项类型</th>
				<th>交易款项方向</th>
				<th>交易款项开始时间</th>
				<th>交易款项到期时间</th>
				<th>应该交易金额</th>
				<th>实际交易金额</th>
				<th>剩余交易金额</th>
				<th>交易款项状态</th>
				<th>更新时间</th>
				<th>备注信息</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="paymentTrans">
			<tr>
				<td>
					<input ${paymentTrans.transStatus =='2' ? 'disabled="disabled"' : ""} name="transIds" transId="${paymentTrans.transId}" transName="${paymentTrans.transName}" type="checkbox" value="${paymentTrans.id}"/>
				</td>
				<td>
					${paymentTrans.transName}
				</td>
				<td>
					${fns:getDictLabel(paymentTrans.tradeType, 'trans_type', '')}
				</td>
				<td>
					${fns:getDictLabel(paymentTrans.paymentType, 'payment_type', '')}
				</td>
				<td>
					${fns:getDictLabel(paymentTrans.tradeDirection, 'fee_dirction', '')}
				</td>
				<td>
					<fmt:formatDate value="${paymentTrans.startDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${paymentTrans.expiredDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${paymentTrans.tradeAmount}
					<c:if test="${paymentTrans.tradeDirection=='0'}">
					<c:set var="sumTradeAmount" value="${sumTradeAmount-paymentTrans.tradeAmount}"></c:set>
					</c:if>
					<c:if test="${paymentTrans.tradeDirection=='1'}">
					<c:set var="sumTradeAmount" value="${sumTradeAmount+paymentTrans.tradeAmount}"></c:set>
					</c:if>
				</td>
				<td>
					${paymentTrans.transAmount}
					<c:if test="${paymentTrans.tradeDirection=='0'}">
					<c:set var="sumTransAmount" value="${sumTransAmount-paymentTrans.transAmount}"></c:set>
					</c:if>
					<c:if test="${paymentTrans.tradeDirection=='1'}">
					<c:set var="sumTransAmount" value="${sumTransAmount+paymentTrans.transAmount}"></c:set>
					</c:if>
				</td>
				<td>
					${paymentTrans.lastAmount}
					<c:if test="${paymentTrans.tradeDirection=='0'}">
					<c:set var="sumLastAmount" value="${sumLastAmount-paymentTrans.lastAmount}"></c:set>
					</c:if>
					<c:if test="${paymentTrans.tradeDirection=='1'}">
					<c:set var="sumLastAmount" value="${sumLastAmount+paymentTrans.lastAmount}"></c:set>
					</c:if>
				</td>
				<td>
					${fns:getDictLabel(paymentTrans.transStatus, 'trade_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${paymentTrans.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${paymentTrans.remarks}
				</td>
			</tr>
		</c:forEach>
		<tr>
				<td colspan="7">
				</td>
				<td>
					总计:<fmt:formatNumber type="number" value="${sumTradeAmount}" pattern="0.00" maxFractionDigits="2"/> 
				</td>
				<td>
					总计:<fmt:formatNumber type="number" value="${sumTransAmount}" pattern="0.00" maxFractionDigits="2"/> 
				</td>
				<td>
					总计:<fmt:formatNumber type="number" value="${sumLastAmount}" pattern="0.00" maxFractionDigits="2"/> 
				</td>
				<td colspan="3">
				</td>
			</tr>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>