<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title></title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        $(document).ready(function() {
            var html = $("#conditions").html();
            $("#addSubmit").click(function () {
                $("#conditions").append(html);
                $("#conditions .selectDom").each(function () {
                    $(this).removeClass('select2-hidden-accessible');
                });
                $('.select2-container').css('display','none');
                $("#conditions .type").each(function (index) {
					$(this).addClass('type'+index);
				});
                $("#conditions .detail").each(function (index) {
                    $(this).addClass('detail'+index);
                });
            });
            $("#conditions .selectDom").each(function () {
				$(this).removeClass('select2-hidden-accessible');
				$('.select2-container').css('display','none');
            });

            $("#btnSubmit").click(function () {
                var startDate = $("#startDate").val();
                var endDate = $("#endDate").val();
                var compareData = [];
                $('#conditions .type').each(function (index) {
                    var detail = $('.detail'+index);
					compareData.push({type:$(this).val(),id:detail.val()})
                });

                if (startDate == "" || endDate == "") {
                    alert("请选择具体的时间范围");
                    return false;
                }
                $.post("${ctx}/report/gross/listGrossProfitCompare", {
					compareData:JSON.stringify(compareData),
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

        function changeSelect(_this,type) {

            var curParentNextAll = $("#type").parent().prev().nextAll();
            var html = "<option value='' selected='selected'>请选择...</option>";
            curParentNextAll.each(function (index,parent) {   //清空当前选项之后的所有级联select
                var child = $(this).children(".input-medium");
                child.empty();
                child.append(html);
            });
            $.ajaxSetup({ cache: false });
            $.get("${ctx}/report/gross/getSubOrgList?business=CATEGORY&type="+type, function(data){
                for(var i=0;i<data.length;i++) {
                    html += "<option value='"+data[i].id+"'>"+data[i].name+"</option>";
                }
                $(_this).parent().next().children('.detail').html(html);
            });
        }

	</script>
</head>
<body>
<form:form id="searchForm" modelAttribute="houseRoomReport" action="${ctx}/report/gross/listGrossProfit" method="post" class="breadcrumb form-search">
	<div id="conditions">
		<ul class="ul-form">
			<li><label>类别：</label>
				<select id="type" name="type" class="input-medium selectDom type" onchange="changeSelect(this,this.options[this.options.selectedIndex].value)">
					<option value="">请选择...</option>
					<c:forEach items="${categoryList}" var="category">
						<option value="${category.id}">${category.name}</option>
					</c:forEach>
				</select>
			</li>
			<li><label>明细：</label>
				<select id="id" name="id" class="input-medium selectDom detail">
					<option value="">请选择...</option>
				</select>
			</li>
		</ul>
	</div>
		<ul class="ul-form">
			<li><label>起始年月：</label>
				<input name="startDate" id="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					   onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:true});"  />
			</li>
			<li><label>截止年月：</label>
				<input name="endDate" id="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					   onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:true});" />
			</li>
		</ul>

	<div style = "text-align: center;margin-top:20px;">
		<input id="addSubmit" class="btn btn-primary" type="button"  value="新增" style = "margin-right:20px;" />
		<input id="btnSubmit" class="btn btn-primary" type="button"  value="查询"/>
	</div>
</form:form>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<thead>
	<tr>
		<th>名称</th>
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