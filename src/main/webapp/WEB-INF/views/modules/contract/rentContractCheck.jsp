<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>退租核算</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function submitData() {
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		}
		function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
		}
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
			}else if(delFlag.val() == "1"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/contract/rentContract/">出租合同列表</a></li>
		<shiro:hasPermission name="contract:rentContract:edit"><li><a href="${ctx}/contract/rentContract/form">出租合同添加</a></li></shiro:hasPermission>
		<li class="active"><a href="javascript:void(0);">退租核算</a></li>
	</ul>
	<form:form id="inputForm" modelAttribute="rentContract" action="${ctx}/contract/rentContract/returnCheck" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="tradeType"/>
		<form:hidden path="isSpecial"/>
		<div class="control-group">
			<label class="control-label">房屋是否损坏：</label>
			<div class="controls">
				&nbsp;<input type="checkbox" name="breakDown" value="1"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">应收费用：</label>
			<div class="controls">
				<table id="contentTable" class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th class="hide"></th>
							<th>费用类型</th>
							<th>费用金额</th>
							<th width="10">&nbsp;</th>
						</tr>
					</thead>
					<tbody id="accountList">
						<c:forEach items="${accountList}" var="outItem" varStatus="status">
						<tr id="accountList${status.index}">
							<td class="hide">
								<input id="accountList${status.index}_id" name="accountList[${status.index}].id" type="hidden"/>
								<input id="accountList${status.index}_delFlag" name="accountList[${status.index}].delFlag" type="hidden" value="0"/>
							</td>
							<td>
								<select id="accountList${status.index}_feeType" name="accountList[${status.index}].feeType" class="required" style="width:220px;">
									<option value="${outItem.feeType}">${fns:getDictLabel(outItem.feeType, 'fee_type', '')}</option>
								</select>
								<span class="help-inline"><font color="red">*</font> </span>
							</td>
							<td>
								<input id="accountList${status.index}_feeAmount" name="accountList[${status.index}].feeAmount" type="text" value="${outItem.feeAmount}" maxlength="255" class="input-small required number"/>
								<span class="help-inline"><font color="red">*</font> </span>
							</td>
							<td class="text-center" width="10">
								<span class="close" onclick="delRow(this, '#accountList${status.index}')" title="删除">&times;</span>
							</td>
						</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr><td colspan="4"><a href="javascript:" onclick="addRow('#accountList', accountRowIdx, accountTpl);accountRowIdx = accountRowIdx + 1;" class="btn">新增</a></td></tr>
					</tfoot>
				</table>
				<script type="text/template" id="accountTpl">//<!--
						<tr id="accountList{{idx}}">
							<td class="hide">
								<input id="accountList{{idx}}_id" name="accountList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
								<input id="accountList{{idx}}_delFlag" name="accountList[{{idx}}].delFlag" type="hidden" value="0"/>
							</td>
							<td>
								<select id="accountList{{idx}}_feeType" name="accountList[{{idx}}].feeType" class="required">
									<option value="">请选择</option>
									<c:forEach items="${fns:getDictList('fee_type')}" var="item">
										<option value="${item.value}">${item.label}</option>
									</c:forEach>
								</select>
								<span class="help-inline"><font color="red">*</font> </span>
							</td>
							<td>
								<input id="accountList{{idx}}_feeAmount" name="accountList[{{idx}}].feeAmount" type="text" value="{{row.feeAmount}}" maxlength="255" class="input-small required number"/>
								<span class="help-inline"><font color="red">*</font> </span>
							</td>
							<td class="text-center" width="10">
								{{#delBtn}}<span class="close" onclick="delRow(this, '#accountList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
							</td>
						</tr>//-->
					</script>
				<script type="text/javascript">
					var accountRowIdx = ${accountSize}, accountTpl = $("#accountTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
					$(document).ready(function() {
						var data = ${fns:toJson(rentContract.accountList)};
						for (var i=0; i<data.length; i++){
							addRow('#accountList', accountRowIdx, accountTpl, data[i]);
							accountRowIdx = accountRowIdx + 1;
						}
					});
				</script>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">应出费用：</label>
			<div class="controls">
				<table id="contentTable" class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th class="hide"></th>
							<th>费用类型</th>
							<th>费用金额</th>
							<th width="10">&nbsp;</th>
						</tr>
					</thead>
					<tbody id="outAccountList">
						<c:forEach items="${outAccountList}" var="outItem" varStatus="status">
						<tr id="outAccountList${status.index}">
							<td class="hide">
								<input id="outAccountList${status.index}_id" name="outAccountList[${status.index}].id" type="hidden"/>
								<input id="outAccountList${status.index}_delFlag" name="outAccountList[${status.index}].delFlag" type="hidden" value="0"/>
							</td>
							<td>
								<select id="outAccountList${status.index}_feeType" name="outAccountList[${status.index}].feeType" class="required" style="width:220px;">
									<option value="${outItem.feeType}">${fns:getDictLabel(outItem.feeType, 'fee_type', '')}</option>
								</select>
								<span class="help-inline"><font color="red">*</font> </span>
							</td>
							<td>
								<input id="outAccountList${status.index}_feeAmount" name="outAccountList[${status.index}].feeAmount" type="text" value="${outItem.feeAmount}" maxlength="255" class="input-small required number"/>
								<span class="help-inline"><font color="red">*</font> </span>
							</td>
							<td class="text-center" width="10">
								<span class="close" onclick="delRow(this, '#outAccountList${status.index}')" title="删除">&times;</span>
							</td>
						</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr><td colspan="4"><a href="javascript:" onclick="addRow('#outAccountList', outAccountRowIdx, outAccountTpl);outAccountRowIdx = outAccountRowIdx + 1;" class="btn">新增</a></td></tr>
					</tfoot>
				</table>
				<script type="text/template" id="outAccountTpl">//<!--
						<tr id="outAccountList{{idx}}">
							<td class="hide">
								<input id="outAccountList{{idx}}_id" name="outAccountList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
								<input id="outAccountList{{idx}}_delFlag" name="outAccountList[{{idx}}].delFlag" type="hidden" value="0"/>
							</td>
							<td>
								<select id="outAccountList{{idx}}_feeType" name="outAccountList[{{idx}}].feeType" class="required">
									<option value="">请选择</option>
									<c:forEach items="${fns:getDictList('fee_type')}" var="item">
										<option value="${item.value}">${item.label}</option>
									</c:forEach>
								</select>
								<span class="help-inline"><font color="red">*</font> </span>
							</td>
							<td>
								<input id="outAccountList{{idx}}_feeAmount" name="outAccountList[{{idx}}].feeAmount" type="text" value="{{row.feeAmount}}" maxlength="255" class="input-small required number"/>
								<span class="help-inline"><font color="red">*</font> </span>
							</td>
							<td class="text-center" width="10">
								{{#delBtn}}<span class="close" onclick="delRow(this, '#outAccountList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
							</td>
						</tr>//-->
					</script>
				<script type="text/javascript">
					var outAccountRowIdx = ${outAccountSize}, outAccountTpl = $("#outAccountTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
					$(document).ready(function() {
						var data = ${fns:toJson(rentContract.outAccountList)};
						for (var i=0; i<data.length; i++){
							addRow('#outAccountList', outAccountRowIdx, outAccountTpl, data[i]);
							outAccountRowIdx = outAccountRowIdx + 1;
						}
					});
				</script>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" onclick="submitData()"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>