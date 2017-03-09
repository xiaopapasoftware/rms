<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>发票信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnImport").click(function(){
				$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
					bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
			});
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
	<div id="importBox" class="hide">
		<form id="importForm" action="${ctx}/funds/invoice/import" method="post" enctype="multipart/form-data"
			class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading('正在导入，请稍等...');"><br/>
			<input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>　　
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
			<a href="${ctx}/funds/invoice/import/template">下载模板</a>
		</form>
	</div>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/funds/invoice/">发票信息列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="invoice" action="${ctx}/funds/invoice/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="width:120px;">账务交易对象：</label>
				<form:input path="tradingAccounts.id" htmlEscape="false" maxlength="64" class="input-medium" style="width:185px;"/>
			</li>
			<li><label style="width:120px;">开票类型：</label>
				<form:select path="invoiceType" class="input-medium" style="width:200px;">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('invoice_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">发票号码：</label>
				<form:input path="invoiceNo" htmlEscape="false" maxlength="64" class="input-medium" style="width:185px;"/>
			</li>
			<li><label style="width:120px;">发票抬头：</label>
				<form:input path="invoiceTitle" htmlEscape="false" maxlength="64" class="input-medium" style="width:185px;"/>
			</li>
			<li><label style="width:120px;">开票日期：</label>
				<input name="invoiceDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${invoice.invoiceDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:185px;"/>
			</li>
			<li><label style="width:120px;">发票金额：</label>
				<form:input path="invoiceAmount" htmlEscape="false" class="input-medium" style="width:185px;"/>
			</li>
			<li class="btns">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<shiro:hasPermission name="funds:invoice:edit">
				<input id="btnImport" class="btn btn-primary" type="button" value="导入"/>
				</shiro:hasPermission>
			</li>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>账务交易对象</th>
				<th>账务交易类型</th>
				<th>开票类型</th>
				<th>发票号码</th>
				<th>发票抬头</th>
				<th>开票日期</th>
				<th>发票金额</th>
				<th>更新时间</th>
				<th>备注信息</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="invoice">
			<tr>
				<td>
					${invoice.tradeName}
				</td>
				<td>
					${fns:getDictLabel(invoice.tradeType, 'trans_type', '')}
				</td>
				<td>
					${fns:getDictLabel(invoice.invoiceType, 'invoice_type', '')}
				</td>
				<td>
					${invoice.invoiceNo}
				</td>
				<td>
					${invoice.invoiceTitle}
				</td>
				<td>
					<fmt:formatDate value="${invoice.invoiceDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${invoice.invoiceAmount}
				</td>
				<td>
					<fmt:formatDate value="${invoice.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${invoice.remarks}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>