<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>账务交易管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function(){
				$("#searchForm").attr("action","${ctx}/funds/tradingAccounts/exportTradingAccounts");
				$("#searchForm").submit();
				$("#searchForm").attr("action","${ctx}/funds/tradingAccounts/");
			});
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
		
		function viewReceiptAttachmentFiles(id) {
			$.jBox.open("iframe:${ctx}/funds/tradingAccounts/viewReceiptAttachmentFiles?id="+id,'收据凭单',1000,460,{buttons:{'关闭':true}});
		}
		
		function resetForm() {
			$("#searchForm").find("select").each(function(index){
				$(this).val("");
				$(this).prev("div").find(".select2-chosen").html("全部");
				
				$(this).find("option").each(function(){
					$(this).removeAttr("selected");
				});
			});
			$("#searchForm").find("input").each(function(index){
				if($(this).attr("type")=="text")
					$(this).val("");
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/funds/tradingAccounts/">账务交易列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="tradingAccounts" action="${ctx}/funds/tradingAccounts/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="width:500px;">定金协议名称/出租合同名称/承租合同名称/物业项目名称/楼宇名称/房屋号/房间号：</label>
				<form:input path="tradeName" htmlEscape="false" maxlength="64" class="input-medium" style="width:450px;"/>
			</li><br/>
			<li><label style="width:500px;">出租合同编号/承租合同编号/定金协议编号：</label>
				<form:input path="tradeObjectNo" htmlEscape="false" maxlength="64" class="input-medium" style="width:450px;"/>
			</li><br/>
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
			<li class="btns">
				<shiro:hasPermission name="funds:tradingAccounts:view">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
					<input id="btnExport" class="btn btn-primary" type="button" value="导出"/>
					<input type="button" class="btn btn-primary" value="重置" onclick="resetForm()"/>
				</shiro:hasPermission>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" type="${messageType}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>账务交易对象名称</th>
				<th>账务交易对象编号</th>
				<th>账务交易类型</th>
				<th>账务交易方向</th>
				<th>账务交易金额</th>
				<th>账务交易状态</th>
				<th>账务交易参与个体名称</th>
				<th>账务交易参与个体类型</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tradingAccounts">
			<tr>
				<td>
					${tradingAccounts.tradeName}
				</td>
				<td>
					${tradingAccounts.tradeObjectNo}
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
					${fns:getDictLabel(tradingAccounts.tradeStatus, 'trading_status', '')}
				</td>
				<td>
					${tradingAccounts.payeeName}
				</td>
				<td>
					${fns:getDictLabel(tradingAccounts.payeeType, 'receive_type', '')}
				</td>
				<td>
					<fmt:formatDate value="${tradingAccounts.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${tradingAccounts.remarks}
				</td>
				<td>
				<shiro:hasPermission name="funds:tradingAccounts:edit">
					<a href="javascript:void(0);" onclick="viewReceipt('${tradingAccounts.id}')">查看收据</a>
					<c:if test="${tradingAccounts.tradeStatus=='2'}">
    					<a href="${ctx}/funds/tradingAccounts/edit?id=${tradingAccounts.id}">修改</a>
    				</c:if>
    				<c:if test="${tradingAccounts.tradeType=='1' || tradingAccounts.tradeType=='3' || tradingAccounts.tradeType=='4' || tradingAccounts.tradeType=='5' || tradingAccounts.tradeType=='6' || tradingAccounts.tradeType=='7' || tradingAccounts.tradeType=='8' || tradingAccounts.tradeType=='9' || tradingAccounts.tradeType == '10' || tradingAccounts.tradeType == '11' || tradingAccounts.tradeType == '12' || tradingAccounts.tradeType == '13' || tradingAccounts.tradeType == '14' || tradingAccounts.tradeType == '15'}">
						<a href="javascript:void(0);" onclick="viewReceiptAttachmentFiles('${tradingAccounts.id}')">收据凭单</a>
					</c:if>
				</shiro:hasPermission>
				
					<!--tradeType 账务交易类型  -->
					<!--transStatus 定金协议审核状态/出租合同审核状态/承租合同审核状态-->
					<!--tradeStatus 账务记录审核状态 -->
					<!--transBusiStatus 定金协议业务状态/出租合同业务状态-->
					<shiro:hasPermission name="funds:tradingAccounts:audit">
						<c:if test="${tradingAccounts.tradeType=='1' && tradingAccounts.transStatus=='3' && tradingAccounts.tradeStatus=='0'}">
							<a href="javascript:void(0);" onclick="toAudit('${tradingAccounts.id}')">审核</a>
						</c:if><!-- 账务交易类型为“预约定金”，定金协议审核状态为“内容审核通过到账收据待审核” ，账务记录审核状态为“待审核”-->
						
						<!-- APP前端的定金，从后台直接审核 -->
						<c:if test="${tradingAccounts.agreementDataSource == '2'}">
							<c:if test="${tradingAccounts.tradeType=='1' && tradingAccounts.transStatus=='1' && tradingAccounts.tradeStatus=='0'}">
								<a href="javascript:void(0);" onclick="toAudit('${tradingAccounts.id}')">审核</a>
							</c:if><!-- 账务交易类型为“预约定金”，定金协议审核状态为“到账收据登记完成内容待审核” ，账务记录审核状态为“待审核”-->
						</c:if>
						
						<c:if test="${tradingAccounts.tradeType=='2' && tradingAccounts.transStatus=='5' && tradingAccounts.transBusiStatus =='4' && tradingAccounts.tradeStatus=='0'}">
							<a href="javascript:void(0);" onclick="toAudit('${tradingAccounts.id}')">审核</a>
						</c:if><!-- 账务交易类型为“定金转违约”，定金协议审核状态为“到账收据审核通过” ，定金协议业务状态为'定金转违约到账待审核' 账务记录审核状态为“待审核”-->
						
						<c:if test="${(tradingAccounts.tradeType!='1' && tradingAccounts.tradeType!='2' && (tradingAccounts.transStatus=='4' || tradingAccounts.transStatus=='6') && tradingAccounts.tradeStatus=='0')}">
							<a href="javascript:void(0);" onclick="toAudit('${tradingAccounts.id}')">审核</a>
						</c:if><!-- 账务交易类型不为“预约定金”和“定金转违约“，出租合同审核状态为“内容审核通过到账收据待审核”或“到账收据审核通过” ，账务记录审核状态为“待审核”-->
						
						<!-- APP前端的合同，从后台直接审核 -->
						<c:if test="${tradingAccounts.contractDataSource == '2'}">
							<c:if test="${(tradingAccounts.tradeType!='1' && tradingAccounts.tradeType!='2' && tradingAccounts.transStatus=='2' && tradingAccounts.tradeStatus=='0')}">
								<a href="javascript:void(0);" onclick="toAudit('${tradingAccounts.id}')">审核</a>
							</c:if><!-- 账务交易类型不为“预约定金”和“定金转违约“，出租合同审核状态为“到账收据完成合同内容待审核	” ，账务记录审核状态为“待审核”-->
						</c:if>
						
						<c:if test="${tradingAccounts.transBusiStatus=='11' && tradingAccounts.tradeStatus=='0' && tradingAccounts.transStatus=='6'}">
							<a href="javascript:void(0);" onclick="toAudit('${tradingAccounts.id}')">审核</a>
						</c:if><!--出租合同业务状态为：特殊退租结算待审核 ，账务记录审核状态为“待审核” ，出租合同审核状态为"到账收据审核通过"-->
						
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