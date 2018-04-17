<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title></title>
	<meta name="decorator" content="default"/>
</head>
<body>
<form:form id="inputForm" modelAttribute="tradingAccounts" action="${ctx}/contract/rentContract/save" method="post" class="form-horizontal">
	<sys:message content="${message}" type="${messageType}"/>
	<form:hidden id="commonPostFeeFile" path="commonPostFeeFile" htmlEscape="false" maxlength="4000" class="input-xlarge"/>
	<sys:ckfinder maxHeight="100%" maxWidth="100%" readonly="true" input="commonPostFeeFile" type="files" uploadPath="/公共事业费后付收据" selectMultiple="false"/>
</form:form>
</body>
</html>