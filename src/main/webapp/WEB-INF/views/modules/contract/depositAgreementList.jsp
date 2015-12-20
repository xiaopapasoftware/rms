<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>定金协议管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			top.$.jBox.tip.mess = null;
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
			
			$("[id='room.id']").html(html);
			$("[id='room.id']").val("");
			$("[id='room.id']").prev("[id='s2id_room.id']").find(".select2-chosen").html("全部");
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
			
			$("[id='room.id']").html(html);
			$("[id='room.id']").val("");
			$("[id='room.id']").prev("[id='s2id_room.id']").find(".select2-chosen").html("全部");
		}
		
		function houseChange() {
			var room = $("[id='house.id']").val();
			var html = "<option value='' selected='selected'>全部</option>";
			if("" != room) {
				$.ajaxSetup({ cache: false });
				$.get("${ctx}/inventory/room/findList?id=" + room, function(data){
					for(var i=0;i<data.length;i++) {
						html += "<option value='"+data[i].id+"'>"+data[i].roomNo+"</option>";
					}
					$("[id='room.id']").html(html);
				});
			} else {
				$("[id='room.id']").html(html);
			}
			$("[id='room.id']").val("");
			$("[id='room.id']").prev("[id='s2id_room.id']").find(".select2-chosen").html("全部");
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
			window.location.href="${ctx}/contract/depositAgreement/audit?objectId="+id+"&auditMsg="+msg+"&auditStatus="+status;
		}
		
		function auditHis(id) {
			$.jBox.open("iframe:${ctx}/contract/leaseContract/auditHis?objectId="+id,'审核记录',650,400,{buttons:{'关闭':true}});
		}
		
		function breakContract(id){
				  var r = confirm("是否要退费？");
				  if (r==true){
						var html = '<label>转违约退费金额：</label>';
						html += '<input name="refundAmount" type="text" maxlength="20" class="input-medium number"';
						html += 'style="width:196px;"/>';
						var submit = function (v, h, f) {
							var isNum = /^\d+(\.\d+)?$/;
							if(!isNum.test(f.refundAmount)){
								alertx("转违约退费金额输入不合法！");
								return false;
							}else{
								if(parseFloat(f.refundAmount) <=0 ){
									alertx("转违约退费金额必须大于0！");
									return false;
								}else{
								 	window.location.href="${ctx}/contract/depositAgreement/breakContract?id="+id+"&refundAmount="+f.refundAmount;
									return true;
								}
							}
						};
						$.jBox(html,{title:"定金转违约退费金额",submit:submit});
				  }else{
					  var j = confirm("确定定金全额转违约金？注意：此操作不可逆，请慎重操作！");
					  if (j==true){
						  window.location.href="${ctx}/contract/depositAgreement/breakContract?id="+id;
						  return true;
					  }else{
						 return false;
					  }
				  }
		}
		
		function resetForm() {
			$("#searchForm").find("select").each(function(index){
				$(this).val("");
				$(this).prev("div").find(".select2-chosen").html("全部");
				
				$(this).find("option").each(function(){
					$(this).removeAttr("selected");
				});
			});
			$("#searchForm").find("input").each(function(index){
				if($(this).attr("type")=="text")
					$(this).val("");
			});
			
			var html = '<option value="">全部</option>';
			$("[id='building.id']").html(html);
			$("[id='house.id']").html(html);
			$("[id='room.id']").html(html);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/contract/depositAgreement/">定金协议列表</a></li>
		<shiro:hasPermission name="contract:depositAgreement:edit"><li><a href="${ctx}/contract/depositAgreement/form">定金协议添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="depositAgreement" action="${ctx}/contract/depositAgreement/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="width:120px;">出租方式：</label>
				<form:select path="rentMode" class="input-medium" style="width:200px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('rent_mode')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">物业项目：</label>
				<form:select path="propertyProject.id" class="input-medium" style="width:200px;" onchange="changeProject()">
					<form:option value="" label="全部"/>
					<form:options items="${projectList}" itemLabel="projectName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li>
				<label style="width:120px;">楼宇：</label>
				<form:select path="building.id" class="input-medium" style="width:200px;" onchange="buildingChange()">
					<form:option value="" label="全部"/>
					<form:options items="${buildingList}" itemLabel="buildingName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
					
			<li><label style="width:120px;">房屋：</label>
				<form:select path="house.id" class="input-medium" style="width:200px;" onchange="houseChange()">
					<form:option value="" label="全部"/>
					<form:options items="${houseList}" itemLabel="houseNo" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">房间：</label>
				<form:select path="room.id" class="input-medium" style="width:200px;">
					<form:option value="" label="全部"/>
					<form:options items="${roomList}" itemLabel="roomNo" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">定金协议编号：</label>
				<form:input path="agreementCode" htmlEscape="false" maxlength="100" class="input-medium" style="width:185px;"/>
			</li>
			<li><label style="width:120px;">定金协议名称：</label>
				<form:input path="agreementName" htmlEscape="false" maxlength="100" class="input-medium" style="width:185px;"/>
			</li>
			<li><label style="width:120px;">定金协议审核状态：</label>
				<form:select path="agreementStatus" class="input-medium" style="width:200px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('deposit_agreement_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="width:120px;">定金协议业务状态：</label>
				<form:select path="agreementBusiStatus" class="input-medium" style="width:200px;">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('deposit_agreement_busi_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><label style="width:60px;"></label>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input type="button" class="btn btn-primary" value="重置" onclick="resetForm()"/>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="width:2200px">
		<thead>
			<tr>
				<th>定金协议编号</th>
				<th>定金协议名称</th>
				<th style="width:40px;">出租方式</th>
				<th>物业项目</th>
				<th>楼宇</th>
				<th>房屋</th>
				<th style="width:40px;">房间</th>
				<th style="width:80px;">合同开始时间</th>
				<th style="width:80px;">合同结束时间</th>
				<th style="width:80px;">协议签订时间</th>
				<th style="width:40px;">首付房租月数</th>
				<th style="width:40px;">房租押金月数</th>
				<th style="width:80px;">约定合同签约时间</th>
				<th>定金金额</th>
				<th>房屋租金</th>
				<th style="width:40px;">销售</th>
				<th style="width:150px;">定金协议审核状态</th>
				<th style="width:40px;">定金协议业务状态</th>
				<th style="width:130px;">更新时间</th>
				<th style="width:150px;">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="depositAgreement">
			<tr>
				<td>
					${depositAgreement.agreementCode}
				</a>
				<td><a href="${ctx}/contract/depositAgreement/form?id=${depositAgreement.id}">
					${depositAgreement.agreementName}
				</a></td>
				<td>
					${fns:getDictLabel(depositAgreement.rentMode, 'rent_mode', '')}
				</td>
				<td>
					${depositAgreement.projectName}
				</td>
				<td>
					${depositAgreement.buildingBame}
				</td>
				<td>
					${depositAgreement.houseNo}
				</td>
				<td>
					${depositAgreement.roomNo}
				</td>
				<td>
					<fmt:formatDate value="${depositAgreement.startDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${depositAgreement.expiredDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${depositAgreement.signDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${depositAgreement.renMonths}
				</td>
				<td>
					${depositAgreement.depositMonths}
				</td>
				<td>
					<fmt:formatDate value="${depositAgreement.agreementDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${depositAgreement.depositAmount}
				</td>
				<td>
					${depositAgreement.housingRent}
				</td>
				<td>
					${depositAgreement.user.name}
				</td>
				<td>
					${fns:getDictLabel(depositAgreement.agreementStatus, 'deposit_agreement_status', '')}
				</td>
				<td>
					${fns:getDictLabel(depositAgreement.agreementBusiStatus, 'deposit_agreement_busi_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${depositAgreement.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<shiro:hasPermission name="contract:depositAgreement:edit">
						<c:if test="${depositAgreement.agreementStatus=='0' || depositAgreement.agreementStatus=='2' || depositAgreement.agreementStatus=='6'}">
	    					<a href="${ctx}/contract/depositAgreement/form?id=${depositAgreement.id}">修改</a>
	    				</c:if>
					</shiro:hasPermission>
					<shiro:hasPermission name="contract:depositAgreement:return">
						<c:if test="${depositAgreement.agreementStatus=='5' && depositAgreement.agreementBusiStatus=='0'}">
						 	<a href="javascript:void(0);" onclick="javascript:breakContract('${depositAgreement.id}');">转违约</a>
						</c:if>
						<c:if test="${depositAgreement.agreementStatus=='5' && depositAgreement.agreementBusiStatus=='0'}">
							<a href="${ctx}/contract/depositAgreement/intoContract?id=${depositAgreement.id}" onclick="return confirmx('确认要转合同吗?', this.href)">转合同</a>
						</c:if>
					</shiro:hasPermission>
					<c:if test="${depositAgreement.agreementStatus!='6' &&depositAgreement.agreementStatus!='0' && depositAgreement.agreementStatus!='1'}">
						<a href="javascript:void(0);" onclick="auditHis('${depositAgreement.id}')">审核记录</a></td>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>