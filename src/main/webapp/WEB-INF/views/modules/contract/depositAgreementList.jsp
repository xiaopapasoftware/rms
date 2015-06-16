<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>定金协议管理</title>
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/contract/depositAgreement/">定金协议列表</a></li>
		<shiro:hasPermission name="contract:depositAgreement:edit"><li><a href="${ctx}/contract/depositAgreement/form">定金协议添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="depositAgreement" action="${ctx}/contract/depositAgreement/" method="post" class="breadcrumb form-search"
		cssStyle="width:1335px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form" style="width:1200px;">
			<li><label style="width:120px;">物业项目：</label>
				<form:select path="propertyProject.id" class="input-medium" style="width:200px;">
					<form:option value="" label=""/>
					<form:options items="${projectList}" itemLabel="projectName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">楼宇：</label>
				<form:select path="building.id" class="input-medium" style="width:200px;">
					<form:option value="" label=""/>
					<form:options items="${buildingList}" itemLabel="buildingName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">房屋：</label>
				<form:select path="house.id" class="input-medium" style="width:200px;">
					<form:option value="" label=""/>
					<form:options items="${houseList}" itemLabel="houseNo" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">房间：</label>
				<form:select path="room.id" class="input-medium" style="width:200px;">
					<form:option value="" label=""/>
					<form:options items="${roomList}" itemLabel="roomNo" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">出租方式：</label>
				<form:select path="rentMode" class="input-medium" style="width:200px;">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('rent_mode')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">销售：</label>
				<sys:treeselect id="user" name="user.id" value="${depositAgreement.user.id}" labelName="user.name" labelValue="${depositAgreement.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true" cssStyle="width:140px;"/>
			</li>
			<li><label style="width:120px;">定金协议名称：</label>
				<form:input path="agreementName" htmlEscape="false" maxlength="100" class="input-medium" style="width:185px;"/>
			</li>
			<li><label style="width:120px;">协议开始时间：</label>
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${depositAgreement.startDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width:185px;"/>
			</li>
			<li><label style="width:120px;">协议结束时间：</label>
				<input name="expiredDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${depositAgreement.expiredDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width:185px;"/>
			</li>
			<li><label style="width:120px;">协议签订时间：</label>
				<input name="signDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${depositAgreement.signDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width:185px;"/>
			</li>
			<li><label style="width:120px;">首付房租月数：</label>
				<form:input path="renMonths" htmlEscape="false" maxlength="11" class="input-medium" style="width:185px;"/>
			</li>
			<li><label style="width:120px;">房租押金月数：</label>
				<form:input path="depositMonths" htmlEscape="false" maxlength="11" class="input-medium" style="width:185px;"/>
			</li>
			<li><label style="width:120px;">约定合同签约时间：</label>
				<input name="agreementDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${depositAgreement.agreementDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width:185px;"/>
			</li>
			<li><label style="width:120px;">定金协议审核状态：</label>
				<form:select path="agreementStatus" class="input-medium" style="width:200px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('deposit_agreement_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">定金协议业务状态：</label>
				<form:select path="agreementBusiStatus" class="input-medium" style="width:200px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('deposit_agreement_busi_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="width:1370px;">
		<thead>
			<tr>
				<th>定金协议名称</th>
				<th>物业项目</th>
				<th>楼宇</th>
				<th>房屋</th>
				<th>房间</th>
				<th>出租方式</th>
				<!--<th>销售</th>-->
				<th>协议开始时间</th>
				<th>协议结束时间</th>
				<th>协议签订时间</th>
				<th>首付房租月数</th>
				<th>房租押金月数</th>
				<th>约定合同签约时间</th>
				<th>定金协议审核状态</th>
				<th>定金协议业务状态</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<th width="115">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="depositAgreement">
			<tr>
				<td><a href="${ctx}/contract/depositAgreement/form?id=${depositAgreement.id}">
					${depositAgreement.agreementName}
				</a></td>
				<td>
					${depositAgreement.projectName}
				</td>
				<td>
					${depositAgreement.buildingBame}
				</td>
				<td>
					${depositAgreement.houseNo}
				</td>
				<td>
					${depositAgreement.roomNo}
				</td>
				<td>
					${fns:getDictLabel(depositAgreement.rentMode, 'rent_mode', '')}
				</td>
				<!--<td>
					${depositAgreement.user.name}
				</td>-->
				<td>
					<fmt:formatDate value="${depositAgreement.startDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${depositAgreement.expiredDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${depositAgreement.signDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${depositAgreement.renMonths}
				</td>
				<td>
					${depositAgreement.depositMonths}
				</td>
				<td>
					<fmt:formatDate value="${depositAgreement.agreementDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${fns:getDictLabel(depositAgreement.agreementStatus, 'deposit_agreement_status', '')}
				</td>
				<td>
					${fns:getDictLabel(depositAgreement.agreementBusiStatus, 'deposit_agreement_busi_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${depositAgreement.updateDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${depositAgreement.remarks}
				</td>
				<td>
					<shiro:hasPermission name="contract:depositAgreement:edit">
						<c:if test="${depositAgreement.agreementStatus=='1' || depositAgreement.agreementStatus=='2'}">
	    					<a href="${ctx}/contract/depositAgreement/form?id=${depositAgreement.id}">修改</a>
	    				</c:if>
						<!--<a href="${ctx}/contract/depositAgreement/delete?id=${depositAgreement.id}" onclick="return confirmx('确认要删除该定金协议吗？', this.href)">删除</a>-->
					</shiro:hasPermission>
					<c:if test="${depositAgreement.agreementStatus=='1'}">
						<a href="javascript:void(0);" onclick="toAudit('${depositAgreement.id}')">审核</a>
					</c:if>
					<c:if test="${depositAgreement.agreementStatus=='2' || depositAgreement.agreementStatus=='3'}">
						<a href="javascript:void(0);" onclick="auditHis('${depositAgreement.id}')">审核记录</a>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>