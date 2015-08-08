<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>承租合同管理</title>
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
			window.location.href="${ctx}/contract/leaseContract/audit?objectId="+id+"&auditMsg="+msg+"&auditStatus="+status;
		}
		
		function auditHis(id) {
			$.jBox.open("iframe:${ctx}/contract/leaseContract/auditHis?objectId="+id,'审核记录',650,400,{buttons:{'关闭':true}});
		}
		
		function changeProject() {
			var project = $("[id='propertyProject.id']").val();
			var html = "<option value='' selected='selected'>全部</option>";
			if("" != project) {
				$.ajaxSetup({ cache: false });
				$.get("${ctx}/inventory/building/findList?id=" + project, function(data){
					for(var i=0;i<data.length;i++) {
						html += "<option value='"+data[i].id+"'>"+data[i].buildingName+"</option>";
					}
					$("[id='building.id']").html(html);
				});
			} else {
				$("[id='building.id']").html(html);
			}
			$("[id='building.id']").val("");
			$("[id='building.id']").prev("[id='s2id_building.id']").find(".select2-chosen").html("全部");
			
			$("[id='house.id']").html(html);
			$("[id='house.id']").val("");
			$("[id='house.id']").prev("[id='s2id_house.id']").find(".select2-chosen").html("全部");
		}
		
		function buildingChange() {
			var building = $("[id='building.id']").val();
			var html = "<option value='' selected='selected'>全部</option>";
			if("" != building) {
				$.ajaxSetup({ cache: false });
				$.get("${ctx}/inventory/house/findList?id=" + building, function(data){
					for(var i=0;i<data.length;i++) {
						html += "<option value='"+data[i].id+"'>"+data[i].houseNo+"</option>";
					}
					$("[id='house.id']").html(html);
				});
			} else {
				$("[id='house.id']").html(html);
			}
			$("[id='house.id']").val("");
			$("[id='house.id']").prev("[id='s2id_house.id']").find(".select2-chosen").html("全部");
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/contract/leaseContract/">承租合同列表</a></li>
		<shiro:hasPermission name="contract:leaseContract:edit"><li><a href="${ctx}/contract/leaseContract/form?type=add">承租合同添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="leaseContract" action="${ctx}/contract/leaseContract/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="width:100px;">物业项目：</label>
				<form:select path="propertyProject.id" class="input-medium" style="width:177px;" onchange="changeProject()">
					<form:option value="" label="全部"/>
					<form:options items="${projectList}" itemLabel="projectName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:100px;">楼宇：</label>
				<form:select path="building.id" class="input-medium" style="width:177px;" onchange="buildingChange()">
					<form:option value="" label="全部"/>
					<form:options items="${buildingList}" itemLabel="buildingName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:100px;">房屋：</label>
				<form:select path="house.id" class="input-medium" style="width:177px;">
					<form:option value="" label="全部"/>
					<form:options items="${houseList}" itemLabel="houseNo" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:100px;">承租合同编号：</label>
				<form:input path="contractCode" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label style="width:100px;">承租合同名称：</label>
				<form:input path="contractName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label style="width:100px;">打款日期：</label>
				<form:select path="remittanceDate" class="input-medium" style="width:177px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('remittance_date')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:100px;">合同审核状态：</label>
				<form:select path="contractStatus" class="input-medium" style="width:177px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('contract_status')}" itemLabel="label" itemValue="value" htmlEscape="true"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="width:2000px;">
		<thead>
			<tr>
				<th style="width:40px;">承租合同编号</th>
				<th>承租合同名称</th>
				<th>物业项目</th>
				<th>楼宇</th>
				<th>房屋</th>
				<th style="width:40px;">汇款人</th>
				<th style="width:80px;">合同生效时间</th>
				<th style="width:80px;">首次打款日期</th>
				<th style="width:40px;">打款日期</th>
				<th style="width:80px;">合同过期时间</th>
				<th style="width:80px;">合同签订时间</th>
				<th>承租押金</th>
				<th style="width:40px;">合同审核状态</th>
				<th style="width:130px;">更新时间</th>
				<th>备注信息</th>
				<th style="width:100px;">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="leaseContract">
			<tr>
				<td>
					${leaseContract.contractCode}
				</td>
				<td><a title="查看详细" href="${ctx}/contract/leaseContract/form?id=${leaseContract.id}">
					${leaseContract.contractName}
				</a></td>
				<td>
					${leaseContract.projectName}
				</td>
				<td>
					${leaseContract.buildingBame}
				</td>
				<td>
					${leaseContract.houseNo}
				</td>
				<td>
					${leaseContract.remittancerName}
				</td>
				<td>
					<fmt:formatDate value="${leaseContract.effectiveDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${leaseContract.firstRemittanceDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${fns:getDictLabel(leaseContract.remittanceDate, 'remittance_date', '')}
				</td>
				<td>
					<fmt:formatDate value="${leaseContract.expiredDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${leaseContract.contractDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${leaseContract.deposit}
				</td>
				<td>
					${fns:getDictLabel(leaseContract.contractStatus, 'contract_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${leaseContract.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${leaseContract.remarks}
				</td>
				<td>
				<shiro:hasPermission name="contract:leaseContract:edit">
					<c:if test="${leaseContract.contractStatus=='2'}">
    					<a href="${ctx}/contract/leaseContract/form?id=${leaseContract.id}">修改</a>
    				</c:if>
					<!--<a href="${ctx}/contract/leaseContract/delete?id=${leaseContract.id}" onclick="return confirmx('确认要删除该承租合同吗？', this.href)">删除</a>-->
				</shiro:hasPermission>
				<shiro:hasPermission name="contract:leaseContract:audit">
				<!--<c:if test="${leaseContract.contractStatus=='0'}">
					<a href="javascript:void(0);" onclick="toAudit('${leaseContract.id}')">审核</a>
				</c:if>-->
				</shiro:hasPermission>
				<c:if test="${leaseContract.contractStatus=='1' || leaseContract.contractStatus=='2'}">
					<a href="javascript:void(0);" onclick="auditHis('${leaseContract.id}')">审核记录</a>
				</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>