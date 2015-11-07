<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title></title>
	<meta name="decorator" content="default"/>
</head>
<body>
<form:form id="inputForm" modelAttribute="tradingAccounts" action="${ctx}/contract/depositAgreement/save" method="post" class="form-horizontal">
	<sys:message content="${message}"/>
	<form:hidden id="depositReceiptFile" path="depositReceiptFile" htmlEscape="false" maxlength="4000" class="input-xlarge"/>
	<sys:ckfinder maxHeight="100%" maxWidth="100%" readonly="true" input="depositReceiptFile" type="files" uploadPath="/定金收据凭证" selectMultiple="false"/>
</form:form>
</body>
</html>