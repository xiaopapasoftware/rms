<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="sitemesh"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="overflow-x: auto; overflow-y: auto;">
<head>
<title><sitemesh:title /></title>
<!--  - Powered By 小爬爬软件工作室 -->
<%@include file="/WEB-INF/views/include/head.jsp"%>
<!-- Baidu tongji analytics -->
<script>
	var _hmt = _hmt || [];
	(function() {
		var hm = document.createElement("script");
		hm.src = "//hm.baidu.com/hm.js?82116c626a8d504a5c0675073362ef6f";
		var s = document.getElementsByTagName("script")[0];
		s.parentNode.insertBefore(hm, s);
	})();
</script>
<sitemesh:head />
</head>
<body>
	<sitemesh:body />
</body>
</html>