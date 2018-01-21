<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>预约信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var scope = {};
		getSalesmanList();
		$(document).ready(function() {

            $('#allCheck').click(function () {
                var chk = document.getElementsByName("chk");
                for(var i=0;i<chk.length;i++){
                    chk[i].checked = true;
                }
            });

		    $('#allocation').click(function () {

                var result = "";
                $(".allocationCheck").each(function () {
                    if($(this).attr("checked")){
                        result = result + $(this).val() + "|";
                    }
                });
                if(result === ""){
                    return alert("请选择需要分配的单据");
                }

                var options = optionCreator();
                var selectHtml = "<select id = 'selectSalesman' name = 'selectSalesman' >"+options+"</select>"
                var html =  "<div style='padding:10px;'>输入姓名："+selectHtml+"</div>";

                var submit = function (v, h, f) {
                    if (f.selectSalesman == '') {
                        $.jBox.tip("请输入您的姓名。", 'error', { focusId: "selectSalesman" }); // 关闭设置 yourname 为焦点
                        return false;
                    }
					$.post('${ctx}/contract/book/distribution',{
                        customerIdList:result,
                        salesId:f.selectSalesman
					},function (data,status) {
                        $.jBox.tip("设置成功");
                        window.location.href = "${ctx}/contract/book";
                    });
                    return true;
                };
                $.jBox(html, { title: "工单分配", submit: submit });
            });

		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
        function optionCreator() {
		    var data = scope.salsemanList;
            var html = "<option value=''>请选择</option>";
            if(!data || !data.length) return;
            $.each(data,function (index,item) {
                html += "<option value = "+item.id+">"+item.name+"</option>";
            });
            return html;
        }
        function  getSalesmanList() {
            $.post("${ctx}/sys/user/saleUser",{
            },function (data,status) {
                scope.salsemanList =  data;
            });
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/contract/book/">预约信息列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="contractBook" action="${ctx}/contract/book/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>预约姓名：</label>
				<form:input path="customer.trueName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>预约电话：</label>
				<form:input path="bookPhone" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>预约时间：</label>
				<input name="bookDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${contractBook.bookDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:196px;"/>
			</li>
			<li><label>预约状态：</label>
				<form:select path="bookStatus" class="input-medium" style="width:210px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('book_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<shiro:hasPermission name="contract:contractBook:view">
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			</shiro:hasPermission>
			<li class="btns"><input id="allCheck" class="btn btn-primary" type="button" value="全选"/></li>
			<shiro:hasPermission name="contract:contractBook:edit">
				<li class="btns">
					<input id = "allocation" class="btn btn-primary" type = "button"  value = "工单分配"  />
				</li>
			</shiro:hasPermission>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" type="${messageType}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>选择</th>
				<th>物业项目</th>
				<th>楼宇</th>
				<th>房屋号</th>
				<th>房间号</th>
				<th>预约姓名</th>
				<th>预约电话</th>
				<th>预约时间</th>
				<th>预约状态</th>
				<th>跟进销售</th>
				<th>来源</th>
				<th>房源编号</th>
				<th>房源类型</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="contractBook">
			<tr>
				<td><input type = "checkbox" class="allocationCheck" value = "${contractBook.id}"  name="chk" /></td>
				<td>
					${contractBook.projectName}
				</td>
				<td>
					${contractBook.buildingName}
				</td>
				<td>
					${contractBook.houseNo}
				</td>
				<td>
					${contractBook.roomNo}
				</td>
				<td>
					${contractBook.customer.trueName}
				</td>
				<td>
					${contractBook.bookPhone}
				</td>
				<td>
					<fmt:formatDate value="${contractBook.bookDate}" pattern="yyyy-MM-dd HH:mm"/>
				</td>
				<td>
					${fns:getDictLabel(contractBook.bookStatus, 'book_status', '')}
				</td>
				<td>
					${contractBook.salesName}
				</td>
				<td>
					${fns:getDictLabel(contractBook.source, 'book_source', '')}
				</td>
				<td>
					${contractBook.housingCode}
				</td>
				<td>
					${fns:getDictLabel(contractBook.housingType, 'house_type', '')}
				</td>
				<td>
				<shiro:hasPermission name="contract:contractBook:edit">
				  <c:if test="${contractBook.bookStatus=='0'}">
					<a href="${ctx}/contract/book/cancel?id=${contractBook.id}&userId=${contractBook.userId}" onclick="return confirm('取消该预约信息吗？', this.href)">取消</a>
				  </c:if>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>