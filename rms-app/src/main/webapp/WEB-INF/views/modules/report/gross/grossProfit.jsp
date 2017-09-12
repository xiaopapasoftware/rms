<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title></title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

        $(document).ready(function() {
        	$("#btnSubmit").click(function () {
                var startDate = $("#startDate").val();
                var endDate = $("#endDate").val();
                if (startDate == "" || endDate == "") {
                    alert("请选择具体的时间范围");
                    return false;
				}
				$.post("${ctx}/report/gross/listGrossProfit", {
                    company:$("#company").val(),
                    center:$("#center").val(),
                    area:$("#area").val(),
                    project:$("#project").val(),
					house:$("#house").val(),
                    startDate:startDate,
                    endDate:endDate
				}, function (data, status) {
                    $("#viewTbody").html("");
                   if(!data || !data.length) return;
                   	var html = "";
					var trs = "";
					$.each(data,function (index,item) {
						var tds = "<td>"+item.name+"</td><td>"+item.income+"</td><td>"+item.cost+"</td><td>"+item.totalProfit+"</td><td>"+
						item.profitPercent+"</td>";
						trs += "<tr>"+tds+"</tr>";
					});
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
			<li><label>区县：</label>
				<select id="county" name="county" class="input-medium selectDom" onchange="changeSelect(this.options[this.options.selectedIndex].value,'center')">
					<option value="">请选择...</option>
					<c:forEach items="${countyList}" var="county">
						<option value="${county.id}">${county.name}</option>
					</c:forEach>
				</select>
			</li>
			<li><label>服务中心：</label>
				<select id="center" name="center" class="input-medium selectDom" onchange="changeSelect(this.options[this.options.selectedIndex].value,'area')">
					<option value="">请选择...</option>
				</select>
			</li>
			<li><label>营运区域：</label>
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

			<li><label>起始年月：</label>
				<input name="startDate" id="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					   onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:true});"  />
			</li>

			<li><label>截止年月：</label>
				<input name="endDate" id="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					   onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:true});" />
			</li>

			<li class="btns" ><input id="btnSubmit" class="btn btn-primary" type="button"  value="查询"/></li>
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
		<tbody id = "viewTbody">

		</tbody>
	</table>
</body>
</html>