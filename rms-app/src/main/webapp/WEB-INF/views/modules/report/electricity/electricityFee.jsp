<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title></title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

        $(document).ready(function() {
        	$("#btnSubmit").click(function () {
                var building = $("#BUILDING").val();
                var house = $("#HOUSE").val();
                var room = $("#ROOM").val();
                if (building === "" && house === "" && room === "") {
                    alert("选择范围必须为楼宇、房间、房间号三者之一");
                    return false;
                }
				$.post("${ctx}/report/electricity/list", {
                    building:building,
					house:house,
					room:room,
                    minValue:$("#minValue").val(),
                    maxValue:$("#maxValue").val()
				}, function (data, status) {
                    $("#viewTbody").html("");
                   if(!data || !data.length) return;
                   	var html = "";
					var trs = "";
					$.each(data,function (index,item) {
						var tds = "<td>"+item.name+"</td><td>"+item.fee+"</td><td>"+item.updateDate+"</td><td><a id=" + item.id + " onclick='sendMessage(this)'>发送</a></td>";
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
                $.get("${ctx}/report/gross/getSubOrgList?business=ELECTRICITY&id=" + id + "&type="+type, function(data){
                    for(var i=0;i<data.length;i++) {
                        html += "<option value='"+data[i].id+"'>"+data[i].name+"</option>";
                    }
                    $("[id="+type+"]").html(html);
                });
            } else {
                $("[id="+type+"]").html(html);
            }
        }
        function sendMessage(obj) {
            $.get("${ctx}/report/electricity/sendSms?id="+obj.id,function (data) {
			    if(data){
			        alert('发送成功');
				}else{
			        alert('发送失败');
				}

            })
        }
	</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="houseRoomReport" action="${ctx}/report/electricity/list" method="post" class="breadcrumb form-search">
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
				<select id="PROJECT" name="project" class="input-medium selectDom"  onchange="changeSelect(this.options[this.options.selectedIndex].value,'BUILDING')">
					<option value="">请选择...</option>
				</select>
			</li>
			<li><label>楼宇：</label>
				<select id="BUILDING" name="building" class="input-medium selectDom"  onchange="changeSelect(this.options[this.options.selectedIndex].value,'HOUSE')">
					<option value="">请选择...</option>
				</select>
			</li>
			<li><label>房屋：</label>
				<select id="HOUSE" name="house" class="input-medium selectDom" onchange="changeSelect(this.options[this.options.selectedIndex].value,'ROOM')">
					<option value="">请选择...</option>
				</select>
			</li>
			<li><label>房间号：</label>
				<select id="ROOM" name="romm" class="input-medium selectDom">
					<option value="">请选择...</option>
				</select>
			</li>
			<li><label>剩余金额范围：</label>
				<input id="minValue" maxlength="64" class="input-medium" style="width:100px;margin-right:20px" placeholder="最小值"/> 至 <input id="maxValue" maxlength="64" class="input-medium" style="width:100px;margin-left:20px" placeholder="最大值"/>元
			</li>
			<li class="btns" ><input id="btnSubmit" class="btn btn-primary" type="button"  value="查询"/></li>
		</ul>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>名称</th>
				<th>剩余电费</th>
				<th>更新时间</th>
				<th>短信提醒</th>
			</tr>
		</thead>
		<tbody id = "viewTbody">

		</tbody>
	</table>
</body>
</html>