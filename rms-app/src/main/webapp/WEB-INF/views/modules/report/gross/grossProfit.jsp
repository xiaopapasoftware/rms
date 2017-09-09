<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title></title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function changeSelect(value,type) {
            var id = value;
			var curParentNextAll = $("#"+type).parent().prev().nextAll();
            var html = "<option value='' selected='selected'>请选择...</option>";
			curParentNextAll.each(function (index,parent) {   //清空当前选项之后的所有级联select
				var child = $(this).children(".input-medium");
				child.empty();
				child.append(html);
            });
            if("" != id) {
                $.ajaxSetup({ cache: false });
                $.get("${ctx}/report/gross/getSubOrgList?business=org&id=" + id + "&type="+type, function(data){
                    for(var i=0;i<data.length;i++) {
                        html += "<option value='"+data[i].id+"'>"+data[i].name+"</option>";
                    }
                    $("[id="+type+"]").html(html);
                });
            } else {
                $("[id="+type+"]").html(html);
            }
        }
	</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="houseRoomReport" action="${ctx}/report/gross/listGrossProfit" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
			<li><label>公司：</label>
				<select id="company" name="company" class="input-medium selectDom" onchange="changeSelect(this.options[this.options.selectedIndex].value,'center')">
					<option value="">请选择...</option>
					<c:forEach items="${companyList}" var="company">
						<option value="${company.id}">${company.name}</option>
					</c:forEach>
				</select>
			</li>
			<li><label>服务中心：</label>
				<select id="center" name="center" class="input-medium selectDom" onchange="changeSelect(this.options[this.options.selectedIndex].value,'area')">
					<option value="">请选择...</option>
				</select>
			</li>
			<li><label>区域：</label>
				<select name="area" id="area" class="input-medium selectDom" onchange="changeSelect(this.options[this.options.selectedIndex].value,'project')">
					<option value="">请选择...</option>
				</select>
			</li>
			<li><label>物业项目：</label>
				<select id="project" name="project" class="input-medium selectDom"  onchange="changeSelect(this.options[this.options.selectedIndex].value,'house')">
					<option value="">请选择...</option>
				</select>
			</li>
			<li><label>房屋：</label>
				<select id="house" name="house" class="input-medium selectDom">
					<option value="">请选择...</option>
				</select>
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
			<c:if test="${report!=null}">
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
			</c:if>
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