<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>账务收据管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {
            top.$.jBox.tip.mess = null;
        });

        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/funds/receipt/">账务收据列表</a></li>
</ul>
<form:form id="searchForm" modelAttribute="receipt" action="${ctx}/funds/receipt/list" method="post"
           class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <ul class="ul-form">
        <li><label style="width:120px;">账务交易名称：</label>
            <form:input path="tradeName" htmlEscape="false" maxlength="64" class="input-medium" style="width:185px;"/>
        </li>
        <li><label style="width:120px;">账务交易编号：</label>
            <form:input path="tradeNo" htmlEscape="false" maxlength="64" class="input-medium" style="width:185px;"/>
        </li>
        <li><label style="width:120px;">账务交易类型：</label>
            <form:select path="tradeType" class="input-medium" style="width:200px;">
                <form:option value="" label="全部"/>
                <form:options items="${fns:getDictList('trans_type')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
        </li>
        <li><label style="width:120px;">账务交易方式：</label>
            <form:select path="tradeMode" class="input-medium" style="width:200px;">
                <form:option value="" label="全部"/>
                <form:options items="${fns:getDictList('trans_mode')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
        </li>
        <li><label style="width:120px;">收据款项类型：</label>
            <form:select path="paymentType" class="input-medium" style="width:200px;">
                <form:option value="" label="全部"/>
                <form:options items="${fns:getDictList('payment_type')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
        </li>
        <li><label style="width:120px;">收据号码：</label>
            <form:input path="receiptNo" htmlEscape="false" maxlength="100" class="input-medium" style="width:185px;"/>
        </li>
        <li><label style="width:120px;">收据日期：</label>
            <input name="receiptDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
                   value="<fmt:formatDate value="${receiptDate}" pattern="yyyy-MM-dd"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:185px;"/>
        </li>
        <shiro:hasPermission name="funds:receipt:view">
            <li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
        </shiro:hasPermission>
        <li class="clearfix"></li>
    </ul>
</form:form>
<sys:message content="${message}" type="${messageType}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>账务交易名称</th>
        <th>账务交易编号</th>
        <th>账务交易类型</th>
        <th>账务交易方式</th>
        <th>收据款项类型</th>
        <th>收据金额</th>
        <th>收据号码</th>
        <th>收据日期</th>
        <th>更新时间</th>
        <th>备注信息</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="receipt">
        <tr>
            <td>
                    ${receipt.tradeName}
            </td>
            <td>
                    ${receipt.tradeNo}
            </td>
            <td>
                    ${fns:getDictLabel(receipt.tradeType, 'trans_type', '')}
            </td>
            <td>
                    ${fns:getDictLabel(receipt.tradeMode, 'trans_mode', '')}
            </td>
            <td>
                    ${fns:getDictLabel(receipt.paymentType, 'payment_type', '')}
            </td>
            <td>
                    ${receipt.receiptAmount}
            </td>
            <td>
                    ${receipt.receiptNo}
            </td>
            <td>
                <fmt:formatDate value="${receipt.receiptDate}" pattern="yyyy-MM-dd"/>
            </td>
            <td>
                <fmt:formatDate value="${receipt.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>
            <td>
                    ${receipt.remarks}
            </td>
            <td>
                <shiro:hasPermission name="funds:receipt:adminBackDoorModify">
                    <a href="${ctx}/funds/receipt/form?id=${receipt.id}">【后门修改】</a>
                </shiro:hasPermission>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>