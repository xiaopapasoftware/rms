<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>工作记录管理</title>
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
		
		function review(id) {
			var html = "<table style='margin:20px;'><tr><td><label>批阅备注：</label></td><td><textarea id='reviewMsg'></textarea></td></tr></table>";
			var content = {
		    	state1:{
					content: html,
				    buttons: { '保存': 1, '取消': 0 },
				    buttonsFocus: 0,
				    submit: function (v, h, f) {
				    	if (v == 0) {
				        	return true;
				        } else if(v==1){
				        	saveReview(id);
				        }
				        return false;
				    }
				}
			};
			$.jBox.open(content,"审阅",350,220,{});
		}
		
		function saveReview(id) {
			loading('正在提交，请稍等...');
			var msg = $("#reviewMsg").val();
			window.location.href="${ctx}/company/workRecord/review?id="+id+"&reviewRemarks="+msg;
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/company/workRecord/">工作记录列表</a></li>
		<shiro:hasPermission name="company:workRecord:edit"><li><a href="${ctx}/company/workRecord/form">工作记录添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="workRecord" action="${ctx}/company/workRecord/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>记录类型：</label>
				<form:select path="recordType" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('work_record_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>记录标题：</label>
				<form:input path="recordTitle" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>状态：</label>
				<form:select path="recordStatus" class="input-medium">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('work_record_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>批阅人：</label>
				<sys:treeselect id="user" name="user.id" value="${workRecord.user.id}" labelName="user.name" labelValue="${workRecord.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" type="${messageType}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>记录类型</th>
				<th>记录标题</th>
				<th>状态</th>
				<th>批阅人</th>
				<th>批阅时间</th>
				<th>批阅备注</th>
				<th>记录时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="company:workRecord:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="workRecord">
			<tr>
				<td><a href="${ctx}/company/workRecord/form?id=${workRecord.id}">
					${fns:getDictLabel(workRecord.recordType, 'work_record_type', '')}
				</a></td>
				<td>
					${workRecord.recordTitle}
				</td>
				<td>
					${fns:getDictLabel(workRecord.recordStatus, 'work_record_status', '')}
				</td>
				<td>
					${workRecord.user.name}
				</td>
				<td>
					<fmt:formatDate value="${workRecord.reviewDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${workRecord.reviewRemarks}
				</td>
				<td>
					<fmt:formatDate value="${workRecord.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${workRecord.remarks}
				</td>
				<td>
					<c:if test="${workRecord.recordStatus=='1'}">
					<shiro:hasPermission name="company:workRecord:edit">
    					<a href="${ctx}/company/workRecord/form?id=${workRecord.id}">修改</a>
					</shiro:hasPermission>
					<!--<a href="${ctx}/company/workRecord/delete?id=${workRecord.id}" onclick="return confirmx('确认要删除该工作记录吗？', this.href)">删除</a>-->
					<shiro:hasPermission name="company:workRecord:review">
					<a href="javascript:void(0);" onclick="review('${workRecord.id}')">审阅</a>
					</shiro:hasPermission>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>