<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>物业项目管理</title>
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
		<li class="active"><a href="${ctx}/inventory/propertyProject/">物业项目列表</a></li>
		<shiro:hasPermission name="inventory:propertyProject:edit"><li><a href="${ctx}/inventory/propertyProject/form">物业项目添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="propertyProject" action="${ctx}/inventory/propertyProject/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="width:120px;">居委会：</label>
				<form:select path="neighborhood.id" class="input-medium" style="width:195px;">
					<form:option value="" label="请选择..."/>
					<form:options items="${listNeighborhood}" itemLabel="neighborhoodName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">物业公司：</label>
				<form:select path="managementCompany.id" class="input-medium" style="width:195px;">
					<form:option value="" label="请选择..."/>
					<form:options items="${listManagementCompany}" itemLabel="companyName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="clearfix"></li>
			<li><label style="width:120px;">物业项目名称：</label>
				<form:input path="projectName" htmlEscape="false" maxlength="100" class="input-medium" style="width:180px;"/>
			</li>
			<li><label style="width:120px;">项目拼音首字母：</label>
				<form:input path="projectSimpleName" htmlEscape="false" maxlength="100" class="input-medium" style="width:180px;"/>
			</li>

			<li><label style="width:120px;">城市名称：</label>
				<form:input path="cityName" htmlEscape="false" maxlength="100" class="input-medium" style="width:180px;"/>
			</li>
			<li><label style="width:120px;">区县名称：</label>
				<form:input path="districtName" htmlEscape="false" maxlength="100" class="input-medium" style="width:180px;"/>
			</li>
			<li><label style="width:120px;">小区同步状态：</label>
				<form:select path="alipayStatus"  class="input-medium" style="width:195px;">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('alipay_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>

			<li><label style="width:120px;">物业项目地址：</label>
				<form:input path="projectAddr" htmlEscape="false" maxlength="300" class="input-medium" style="width:180px;"/>
			</li>
			<shiro:hasPermission name="inventory:propertyProject:view">
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			</shiro:hasPermission>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" type="${messageType}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>居委会</th>
				<th>物业公司</th>
				<th>物业项目名称</th>
				<th>物业项目拼音首字母</th>
				<th>物业项目地址</th>
				<th>城市编码</th>
				<th>城市名称</th>
				<th>区县编码</th>
				<th>区县名称</th>
				<th>坐标系</th>
				<th>同步请求号</th>
				<th>同步状态</th>
				<th>创建时间</th>
				<th>修改时间</th>
				<th>创建人</th>
				<th>修改人</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="propertyProject">
			<tr>
				<td>
					${propertyProject.neighborhood.neighborhoodName}
				</td>
				<td>
					${propertyProject.managementCompany.companyName}
				</td>
				<td>
					<a href="${ctx}/inventory/propertyProject/form?id=${propertyProject.id}">
					${propertyProject.projectName}
					</a>
				</td>
				<td>
					${propertyProject.projectSimpleName}
				</td>
				<td>
					${propertyProject.projectAddr}
				</td>
				<td>
						${propertyProject.cityCode}
				</td>
				<td>
						${propertyProject.cityName}
				</td>
				<td>
						${propertyProject.districtCode}
				</td>
				<td>
						${propertyProject.districtName}
				</td>
				<td>
						${fns:getDictLabel(propertyProject.coordsys, 'coordsys', '')}
				</td>
				<td>
						${propertyProject.commReqId}
				</td>
				<td>
						${fns:getDictLabel(propertyProject.coordsys, 'alipay_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${propertyProject.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${propertyProject.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
				 	${propertyProject.createBy.loginName}
				</td>
				<td>
				 	${propertyProject.updateBy.loginName}
				</td>
				<td>
					${propertyProject.remarks}
				</td>
				<td>
				<shiro:hasPermission name="inventory:propertyProject:edit">
    				<a href="${ctx}/inventory/propertyProject/form?id=${propertyProject.id}">修改</a>
    			</shiro:hasPermission>
    			<shiro:hasPermission name="inventory:propertyProject:del">
					<a href="${ctx}/inventory/propertyProject/delete?id=${propertyProject.id}" onclick="return confirmx('确认要删除该物业项目和图片、其下楼宇和图片、其下房屋和图片、其下房间和图片吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>