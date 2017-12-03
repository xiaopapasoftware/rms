<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title></title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

        $(document).ready(function() {
            $("#btnExport").click(function() {
                $("#searchForm").attr("action", "${ctx}/report/lease/exportLeaseStatistics");
                $("#searchForm").submit();
                $("#searchForm").attr("action", "${ctx}/report/lease/listLeaseStatistics");
            });

        	$("#btnSubmit").click(function () {
                var startDate = $("#startDate").val();
                var endDate = $("#endDate").val();
                if (startDate === ""  || endDate === "") {
                    alert("请选择具体的时间");
                    return false;
				}
				$.post("${ctx}/report/lease/listLeaseStatistics", {
                    county:$("#COUNTY").val(),
                    center:$("#CENTER").val(),
                    area:$("#AREA").val(),
                    project:$("#PROJECT").val(),
                    startDate:startDate,
                    endDate:endDate
				}, function (data, status) {
                    $("#viewTbody").html("");
                   if(!data) return;
                   	var html = "";
					var trs = "";
					var tds = "<td>"+data.name+"</td><td>"+data.totalRooms+"</td><td>"+data.leasedRooms+"</td><td>"+data.leasedPercent+"</td><td>"+
                        data.rentSum+"</td><td>"+data.rentAvg+"</td>";
					trs += "<tr>"+tds+"</tr>";
					html +=trs;
                    $('#viewTbody').append(html);
                })
            })
        });
		
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
                $.get("${ctx}/report/gross/getSubOrgList?business=ORG&id=" + id + "&type="+type, function(data){
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
	<form:form id="searchForm" modelAttribute="leaseReport" action="${ctx}/report/lease/listLeaseStatistics" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
			<li><label>区县：</label>
				<select id="COUNTY" name="county" class="input-medium selectDom" onchange="changeSelect(this.options[this.options.selectedIndex].value,'CENTER')">
					<option value="">请选择...</option>
					<c:forEach items="${countyList}" var="county">
						<option value="${county.id}">${county.name}</option>
					</c:forEach>
				</select>
			</li>
			<li><label>服务中心：</label>
				<select id="CENTER" name="center" class="input-medium selectDom" onchange="changeSelect(this.options[this.options.selectedIndex].value,'AREA')">
					<option value="">请选择...</option>
				</select>
			</li>
			<li><label>营运区域：</label>
				<select id="AREA" name="area" class="input-medium selectDom" onchange="changeSelect(this.options[this.options.selectedIndex].value,'PROJECT')">
					<option value="">请选择...</option>
				</select>
			</li>
			<li><label>物业项目：</label>
				<select id="PROJECT" name="project" class="input-medium selectDom">
					<option value="">请选择...</option>
				</select>
			</li>

			<li><label>起始日期：</label>
				<input name="startDate" id="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"  />
			</li>

			<li><label>截止日期：</label>
				<input name="endDate" id="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"  />
			</li>

			<shiro:hasPermission name="report:lease:view">
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="button" value="查询"/></li>
				<li class="btns"><input id="btnExport" class="btn btn-primary" type="button" value="导出"/></li>
			</shiro:hasPermission>
		</ul>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>维度名称</th>
				<th>房间总数(间)</th>
				<th>已出租数(间)</th>
				<th>出租率</th>
				<th>房租总计(元)</th>
				<th>月房租均价(元)</th>
			</tr>
		</thead>
		<tbody id = "viewTbody">

		</tbody>
	</table>
</body>
</html>