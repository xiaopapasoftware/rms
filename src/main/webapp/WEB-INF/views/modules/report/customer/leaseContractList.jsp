<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title></title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function(){
				$("#searchForm").attr("action","${ctx}/report/customer/export");
				$("#searchForm").submit();
				$("#searchForm").attr("action","${ctx}/report/customer/leaseContract");
			});
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
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
	<form:form id="searchForm" modelAttribute="leaseContractReport" action="${ctx}/report/customer/leaseContract" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:message content="${message}" type="${messageType}"/>
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
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="btns"><input id="btnExport" class="btn btn-primary" type="button" value="导出"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>业主姓名</th>
				<th>业主手机号码</th>
				<th>物业项目</th>
				<th>楼宇</th>
				<th>房屋</th>
				<th>房屋原始结构</th>
				<th>房屋原始面积</th>
				<th>承租合同开始日期</th>
				<th>承租合同结束日期</th>
				<th>承租合同备注</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="leaseContractReport">
			<tr>
				<td>
					${leaseContractReport.ownerName}
				</td>
				<td>
					${leaseContractReport.ownerCellPhone}
				</td>
				<td>
					${leaseContractReport.projectName}
				</td>
				<td>
					${leaseContractReport.buildingBame}
				</td>
				<td>
					${leaseContractReport.houseNo}
				</td>
				<td>
					${leaseContractReport.houseStructure}
				</td>
				<td>
					${leaseContractReport.houseSpace}
				</td>
				<td>
					<fmt:formatDate value="${leaseContractReport.effectiveDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${leaseContractReport.expiredDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${leaseContractReport.remarks}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>