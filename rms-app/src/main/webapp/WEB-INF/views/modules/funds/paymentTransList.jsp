<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>款项交易管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
            top.$.jBox.tip.mess = null;
			$("#btnExport").click(function(){
				$("#searchForm").attr("action","${ctx}/funds/paymentTrans/exportPaymentTrans");
				$("#searchForm").submit();
				$("#searchForm").attr("action","${ctx}/funds/paymentTrans/");
			});
			
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
					if($("input[name='transIds']:checked").eq(i).attr("transId") != $("input[name='transIds']:checked").eq(i-1).attr("transId")
							&& $("input[name='transIds']:checked").eq(i).attr("tradeType")!='0') {
						check = false;
						break;
					}
				}
			}
			if(check) {
				//到账登记时，如果有后付费款项，则不能跟别的交易类型一起混合到账登记，必须只能是后付费这一种账务交易类型。
				var postpaidTransCount = 0;
				for(var i=0;i<transIds;i++) {
					if($("input[name='transIds']:checked").eq(i).attr("tradetype")=='12'){
						postpaidTransCount = postpaidTransCount + 1;
					}
				}
				if(postpaidTransCount>0 && postpaidTransCount < transIds){
					top.$.jBox.tip('后付费款项不能与其他交易类型一起到账！','warning');
					return;
				}
				//到账登记的款项必须是 同一笔后付费交易，不能跨两笔后付费交易去登记
				for(var i=1;i<transIds;i++) {
					if($("input[name='transIds']:checked").eq(i).attr("postpaidFeeId") != $("input[name='transIds']:checked").eq(i-1).attr("postpaidFeeId")
							&& $("input[name='transIds']:checked").eq(i).attr("postpaidFeeId")!='' && $("input[name='transIds']:checked").eq(i).attr("postpaidFeeId")!=null
							&& $("input[name='transIds']:checked").eq(i-1).attr("postpaidFeeId")!='' && $("input[name='transIds']:checked").eq(i-1).attr("postpaidFeeId")!=null) {
						top.$.jBox.tip('后付费款项只能在同一笔后付费交易下一并到账登记，不可跨不同后付费交易混合登记！','warning');
						return;
					}
				}
				
				var transId=new Array();
				for(var i=0;i<transIds;i++) {
					transId.push($("input[name='transIds']:checked").eq(i).val());
				}
				window.location.href="${ctx}/funds/tradingAccounts/form?transIds="+transId.join(",")+"&tradeId="+$("input[name='transIds']:checked").eq(0).attr("transId")
						+"&tradeName="+$("input[name='transIds']:checked").eq(0).attr("transName")+"&tradeType="+$("input[name='transIds']:checked").eq(0).attr("tradeType");
			} else {
				top.$.jBox.tip('勾选的款项来自不同的合同,不能一并到账登记.','warning');
			}
		}
		
		function chooseAllTransId(target) {
			$("input[name='transIds']").each(function(index){
				if(!$(this).prop("disabled")) {
					if($(target).is(":checked")) {
						$(this).attr("checked",true);
					} else {
						$(this).attr("checked",false);
					}
				}
			});
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
		<li class="active"><a href="${ctx}/funds/paymentTrans/">款项交易列表</a></li>
		<shiro:hasPermission name="funds:paymentTrans:edit">
			<li><a href="javascript:void(0);" onclick="register()">到账登记</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="paymentTrans" action="${ctx}/funds/paymentTrans/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="250"/>
		<ul class="ul-form">
			<li><label style="width:500px;">定金协议名称/出租合同名称/承租合同名称/物业项目名称/楼宇名称/房屋号/房间号：</label>
				<form:input path="transName" htmlEscape="false" maxlength="64" class="input-medium" style="width:450px;"/>
			</li><br/>
			<li><label style="width:500px;">出租合同编号/承租合同编号/定金协议编号：</label>
				<form:input path="transObjectNo" htmlEscape="false" maxlength="64" class="input-medium" style="width:450px;"/>
			</li><br/>
			<li>
				<label style="width:120px;">交易款项开始时间：</label>
				<input name="startDate_begin" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${paymentTrans.startDate_begin}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:185px;"/>至
				<input name="startDate_end" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${paymentTrans.startDate_end}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:185px;"/>
			</li><br/>
			<li><label style="width:120px;">交易款项到期时间：</label>
				<input name="expiredDate_begin" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${paymentTrans.expiredDate_begin}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:185px;"/>
				<input name="expiredDate_end" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${paymentTrans.expiredDate_end}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:185px;"/>
			</li><br/>
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
			<li>
				<label style="width:120px;" class="control-label">打款日期：</label>
				<form:select path="remittanceDate" class="input-xlarge"  style="width:200px;">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('remittance_date')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
			<li class="btns">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnExport" class="btn btn-primary" type="button" value="导出"/>
				<input type="button" class="btn btn-primary" value="重置" onclick="resetForm()"/>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" type="${messageType}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th width="15"><input type="checkbox" onclick="chooseAllTransId(this)" title="全选"/></th>
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
				<th>款项付费周期（月数）</th>
				<th>更新时间</th>
				<th>备注信息</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="paymentTrans">
			<tr>
				<td>
					<input ${paymentTrans.transStatus =='2' ? 'disabled="disabled"' : ""} name="transIds" tradeType="${paymentTrans.tradeType}" transId="${paymentTrans.transId}" postpaidFeeId="${paymentTrans.postpaidFeeId}" transName="${paymentTrans.transName}" type="checkbox" value="${paymentTrans.id}"/>
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
					${paymentTrans.splitPaidMonths}
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