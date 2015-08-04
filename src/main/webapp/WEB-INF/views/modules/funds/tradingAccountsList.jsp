<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>账务交易管理</title>
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
		function toAudit(id) {
			var html = "<table style='margin:20px;'><tr><td><label>审核意见：</label></td><td><textarea id='auditMsg'></textarea></td></tr></table>";
			var content = {
		    	state1:{
					content: html,
				    buttons: { '同意': 1, '拒绝':2, '取消': 0 },
				    buttonsFocus: 0,
				    submit: function (v, h, f) {
				    	if (v == 0) {
				        	return true; // close the window
				        } else if(v==1){
				        	saveAudit(id,'1');
				        } else if(v==2){
				        	saveAudit(id,'2');
				        }
				        return false;
				    }
				}
			};
			$.jBox.open(content,"审核",350,220,{});
		}
		function saveAudit(id,status) {
			loading('正在提交，请稍等...');
			var msg = $("#auditMsg").val();
			window.location.href="${ctx}/funds/tradingAccounts/audit?objectId="+id+"&auditMsg="+msg+"&auditStatus="+status;
		}
		
		function auditHis(id) {
			$.jBox.open("iframe:${ctx}/contract/leaseContract/auditHis?objectId="+id,'审核记录',650,400,{buttons:{'关闭':true}});
		}
		
		function viewReceipt(id) {
			$.jBox.open("iframe:${ctx}/funds/receipt/viewReceipt?tradingAccounts.id="+id,'收据信息',800,400,{buttons:{'关闭':true}});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/funds/tradingAccounts/">账务交易列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="tradingAccounts" action="${ctx}/funds/tradingAccounts/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="width:120px;">账务交易对象：</label>
				<form:input path="tradeName" htmlEscape="false" maxlength="64" class="input-medium" style="width:185px;"/>
			</li>
			<li><label style="width:120px;">账务交易类型：</label>
				<form:select path="tradeType" class="input-medium" style="width:200px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('trans_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">账务交易方向：</label>
				<form:select path="tradeDirection" class="input-medium" style="width:200px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('trans_dirction')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">账务状态：</label>
				<form:select path="tradeStatus" class="input-medium" style="width:200px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('trading_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>账务交易对象</th>
				<th>账务交易类型</th>
				<th>账务交易方向</th>
				<th>交易金额</th>
				<th>交易方名称</th>
				<th>交易方类型</th>
				<th>账务状态</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="funds:tradingAccounts:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tradingAccounts">
			<tr>
				<td>
					${tradingAccounts.tradeName}
				</td>
				<td>
					${fns:getDictLabel(tradingAccounts.tradeType, 'trans_type', '')}
				</td>
				<td>
					${fns:getDictLabel(tradingAccounts.tradeDirection, 'trans_dirction', '')}
				</td>
				<td>
					${tradingAccounts.tradeAmount}
				</td>
				<td>
					${tradingAccounts.payeeName}
				</td>
				<td>
					${fns:getDictLabel(tradingAccounts.payeeType, 'receive_type', '')}
				</td>
				<td>
					${fns:getDictLabel(tradingAccounts.tradeStatus, 'trading_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${tradingAccounts.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${tradingAccounts.remarks}
				</td>
				<td>
				<a href="javascript:void(0);" onclick="viewReceipt('${tradingAccounts.id}')">查看收据</a>
				<shiro:hasPermission name="funds:tradingAccounts:edit">
					<c:if test="${tradingAccounts.tradeStatus=='2'}">
    					<a href="${ctx}/funds/tradingAccounts/edit?id=${tradingAccounts.id}">修改</a>
    				</c:if>
				</shiro:hasPermission>
					<!--<c:if test="${tradingAccounts.tradeType=='0' && tradingAccounts.transStatus=='1' && tradingAccounts.tradeStatus=='0'}">
						<a href="javascript:void(0);" onclick="toAudit('${tradingAccounts.id}')">审核</a>
					</c:if>-->
					<shiro:hasPermission name="funds:tradingAccounts:audit">
					<c:if test="${tradingAccounts.tradeType=='1' && tradingAccounts.transStatus=='3' && tradingAccounts.tradeStatus=='0'}">
						<a href="javascript:void(0);" onclick="toAudit('${tradingAccounts.id}')">审核</a>
					</c:if>
					<c:if test="${(tradingAccounts.tradeType!='1') && ((tradingAccounts.transStatus=='4'||tradingAccounts.transStatus=='6') && tradingAccounts.tradeStatus=='0')}">
						<a href="javascript:void(0);" onclick="toAudit('${tradingAccounts.id}')">审核</a>
					</c:if>
					<c:if test="${tradingAccounts.tradeType=='1' && tradingAccounts.transBusiStatus=='5' && tradingAccounts.tradeStatus=='0'}">
						<a href="javascript:void(0);" onclick="toAudit('${tradingAccounts.id}')">审核</a>
					</c:if>
					<c:if test="${tradingAccounts.transBusiStatus=='11' && tradingAccounts.tradeStatus=='0'}">
						<a href="javascript:void(0);" onclick="toAudit('${tradingAccounts.id}')">审核</a>
					</c:if>
					</shiro:hasPermission>
					<shiro:hasPermission name="funds:tradingAccounts:receipt">
					<c:if test="${tradingAccounts.tradeStatus=='1'}">
						<a href="${ctx}/funds/invoice/form?tradingAccountsId=${tradingAccounts.id}">开具发票</a>
					</c:if>
					</shiro:hasPermission>
					<c:if test="${tradingAccounts.tradeStatus=='1' || tradingAccounts.tradeStatus=='2' || tradingAccounts.tradeStatus=='3'}">
						<a href="javascript:void(0);" onclick="auditHis('${tradingAccounts.id}')">审核记录</a>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>