<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>协议变更管理</title>
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
			window.location.href="${ctx}/contract/agreementChange/audit?objectId="+id+"&auditMsg="+msg+"&auditStatus="+status;
		}
		
		function auditHis(id) {
			$.jBox.open("iframe:${ctx}/contract/leaseContract/auditHis?objectId="+id,'审核记录',650,400,{buttons:{'关闭':true}});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/contract/agreementChange/">协议变更列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="agreementChange" action="${ctx}/contract/agreementChange/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="width:120px;">出租合同：</label>
				<form:input path="rentContractName" htmlEscape="false" maxlength="64" class="input-medium" style="width:195px;"/>
			</li>
			<li><label style="width:120px;">合同变更协议名称：</label>
				<form:input path="agreementChangeName" htmlEscape="false" maxlength="64" class="input-medium" style="width:195px;"/>
			</li>
			<li><label style="width:120px;">协议生效时间：</label>
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${agreementChange.startDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width:196px;"/>
			</li>
			<li><label style="width:120px;">协议审核状态：</label>
				<form:select path="agreementStatus" class="input-medium" style="width:210px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('contract_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>合同变更协议名称</th>
				<th>出租合同</th>
				<th>协议生效时间</th>
				<th>首付房租月数</th>
				<th>房租押金月数</th>
				<th>协议审核状态</th>
				<th>申请人</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="contract:agreementChange:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="agreementChange">
			<tr>
				<td>
					<a href="${ctx}/contract/agreementChange/form?id=${agreementChange.id}">
					${agreementChange.agreementChangeName}
					</a>
				</td>
				<td>
					${agreementChange.rentContractName}
				</td>
				<td>
					<fmt:formatDate value="${agreementChange.startDate}" pattern="yyyy-MM-dd"/>
				</td>
			 	<td>
					${agreementChange.renMonths}
				</td>
				<td>
					${agreementChange.depositMonths}
				</td>
				<td>
					${fns:getDictLabel(agreementChange.agreementStatus, 'contract_status', '')}
				</td>
				<td>
					${agreementChange.createUserName}
				</td>
				<td>
					<fmt:formatDate value="${agreementChange.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${agreementChange.remarks}
				</td>
				<td>
					<shiro:hasPermission name="contract:agreementChange:edit">
					<c:if test="${agreementChange.agreementStatus=='0'||agreementChange.agreementStatus=='2'}">
	   					<a href="${ctx}/contract/agreementChange/form?id=${agreementChange.id}">修改</a>
					</c:if>
					</shiro:hasPermission>
					<!--<shiro:hasPermission name="contract:agreementChange:audit">
					<c:if test="${agreementChange.agreementStatus=='0'}">
	   					<a href="javascript:void(0);" onclick="toAudit('${agreementChange.id}')">审核</a>
					</c:if>
					</shiro:hasPermission>-->
					<c:if test="${agreementChange.agreementStatus=='1'||agreementChange.agreementStatus=='2'}">
	   					<a href="javascript:void(0);" onclick="auditHis('${agreementChange.id}')">审核记录</a>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>