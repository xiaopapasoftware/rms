<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title></title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<form:form id="searchForm" modelAttribute="houseRoomReport" action="${ctx}/report/sales/roomsCount" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
			<li><label>公司：</label>
				<form:select path="propertyProject.id" class="input-medium" onchange="changeProject()">
					<form:option value="" label="请选择..."/>
					<form:options items="${listPropertyProject}" itemLabel="projectName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>服务中心：</label>
				<form:select path="building.id" class="input-medium" onchange="buildingChange()">
					<form:option value="" label="请选择..."/>
					<form:options items="${listBuilding}" itemLabel="buildingName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>区域：</label>
				<form:select path="house.id" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${listHouse}" itemLabel="houseNo" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>物业项目：</label>
				<form:input path="roomNo" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>房屋：</label>
				<form:select path="roomStatus" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('room_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
		</ul>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>项目</th>
				<th>收入</th>
				<th>支出</th>
				<th>毛利</th>
				<th>毛利率</th>
			</tr>
		</thead>
		<tbody>
				<tr>
					<td>
						${report.parent.name}
					</td>
					<td>
						${report.parent.income}
					</td>
					<td>
						${report.parent.cost}
					</td>
					<td>
						${report.parent.totalProfit}
					</td>
					<td>
						${report.parent.profitPercent}
					</td>
				</tr>
				<c:forEach items="${report.childReportList}" var="child">
					<tr>
						<td>
							${child.name}
						</td>
						<td>
							${child.income}
						</td>
						<td>
							${child.cost}
						</td>
						<td>
							${child.totalProfit}
						</td>
						<td>
							${child.profitPercent}
						</td>
					</tr>
				</c:forEach>
		</tbody>
	</table>
</body>
</html>