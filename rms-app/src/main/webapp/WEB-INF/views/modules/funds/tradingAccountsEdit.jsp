<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>账务交易管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
            top.$.jBox.tip.mess = null;
			$("#inputForm").validate({
				submitHandler: function(form){
					if($("#receiptList").find("tr").length>0) {
						var totalReceiptAmount = 0;
						$("input[id$='receiptAmount']").each(function(){
							totalReceiptAmount = totalReceiptAmount + parseFloat($(this).val());
						});
						if(parseFloat(totalReceiptAmount) != parseFloat($("#tradeAmount").val())){
							top.$.jBox.tip('账务交易总金额与收据总金额不相等,请重新到账.','warning');
							return false;
						}
					}
					loading('正在提交，请稍等...');
					$("#btnSubmit").attr("disabled",true);
					$("#delSubmit").attr("disabled",true);
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
		});
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
		
		function delData() {
			if(confirm("确定要撤销到账?")) {
				var id = $("#id").val();
				window.location.href="${ctx}/funds/tradingAccounts/revoke?id="+id;
				$("#btnSubmit").attr("disabled",true);
				$("#delSubmit").attr("disabled",true);
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/funds/tradingAccounts/">账务交易列表</a></li>
		<li class="active">
			<a href="javascript:void(0);">账务交易修改</a>
		</li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="tradingAccounts" action="${ctx}/funds/tradingAccounts/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="tradeId"/>
		<sys:message content="${message}" type="${messageType}"/>
		<div class="control-group">
			<label class="control-label">账务交易对象：</label>
			<div class="controls">
				<form:input path="tradeName" htmlEscape="false" class="input-xlarge" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">账务交易类型：</label>
			<div class="controls">
				<form:select path="tradeType" class="input-xlarge">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('trans_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">账务交易方向：</label>
			<div class="controls">
				<form:select path="tradeDirection" class="input-xlarge">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('trans_dirction')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">账务交易总金额：</label>
			<div class="controls">
				<form:input path="tradeAmount" htmlEscape="false" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">交易方名称：</label>
			<div class="controls">
				<form:input path="payeeName" htmlEscape="false" maxlength="100" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">交易方类型：</label>
			<div class="controls">
				<form:select path="payeeType" class="input-xlarge">
					<form:option value="" label="请选择..."/>
					<form:options items="${fns:getDictList('receive_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
				<label class="control-label">收据信息：</label>
				<div class="controls">
					<table id="contentTable" class="table table-striped table-bordered table-condensed">
						<thead>
							<tr>
								<th class="hide"></th>
								<th>交易方式</th>
								<th>款项类型</th>
								<th>收据金额</th>
								<th>收据号码</th>
								<th>交易日期</th>
								<th>备注</th>
								<th width="10">&nbsp;</th>
							</tr>
						</thead>
						<tbody id="receiptList">
						</tbody>
						<tfoot>
							<tr><td colspan="6"><a href="javascript:" onclick="addRow('#receiptList', receiptRowIdx, receiptTpl);receiptRowIdx = receiptRowIdx + 1;" class="btn">新增</a></td></tr>
						</tfoot>
					</table>
					<script type="text/template" id="receiptTpl">//<!--
						<tr id="receiptList{{idx}}">
							<td class="hide">
								<input id="receiptList{{idx}}_id" name="receiptList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
								<input id="receiptList{{idx}}_delFlag" name="receiptList[{{idx}}].delFlag" type="hidden" value="0"/>
							</td>
							<td>
								<select data-value="{{row.tradeMode}}" id="receiptList{{idx}}_tradeMode" name="receiptList[{{idx}}].tradeMode" class="required" style="width:100px;">
									<option value="">请选择</option>
									<c:forEach items="${fns:getDictList('trans_mode')}" var="item">
										<option value="${item.value}">${item.label}</option>
									</c:forEach>
								</select>
								<span class="help-inline"><font color="red">*</font> </span>
							</td>
							<td>
								<select data-value="{{row.paymentType}}" id="receiptList{{idx}}_paymentType" name="receiptList[{{idx}}].paymentType" class="required" style="width:100px;">
									<option value="">请选择</option>
									<c:forEach items="${fns:getDictList('payment_type')}" var="item">
										<option value="${item.value}">${item.label}</option>
									</c:forEach>
								</select>
								<span class="help-inline"><font color="red">*</font> </span>
							</td>
							<td>
								<input id="receiptList{{idx}}_receiptAmount" name="receiptList[{{idx}}].receiptAmount" type="text" value="{{row.receiptAmount}}" maxlength="255" class="input-small required number"/>
								<span class="help-inline"><font color="red">*</font> </span>
							</td>
							<td>
								<input id="receiptList{{idx}}_receiptNo" name="receiptList[{{idx}}].receiptNo" type="text" value="{{row.receiptNo}}" maxlength="255" class="input-small required"/>
								<span class="help-inline"><font color="red">*</font> </span>
							</td>
							<td>
								<input id="receiptList{{idx}}_receiptDate" name="receiptList[{{idx}}].receiptDate" type="text" readonly="readonly" value="{{row.receiptDate}}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" class="input-medium Wdate required" style="width:100px;"/>
								<span class="help-inline"><font color="red">*</font> </span>
							</td>
							<td>
								<input id="receiptList{{idx}}_remarks" name="receiptList[{{idx}}].remarks" type="text" value="{{row.remarks}}" maxlength="255" class="input-small"/>
							</td>
							<td class="text-center" width="10">
								{{#delBtn}}<span class="close" onclick="delRow(this, '#receiptList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
							</td>
						</tr>//-->
					</script>
					<script type="text/javascript">
						var receiptRowIdx = 0, receiptTpl = $("#receiptTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
						$(document).ready(function() {
							var data = ${fns:toJson(tradingAccounts.receiptList)};
							for (var i=0; i<data.length; i++){
								addRow('#receiptList', receiptRowIdx, receiptTpl, data[i]);
								receiptRowIdx = receiptRowIdx + 1;
							}
						});
					</script>
				</div>
			</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="funds:tradingAccounts:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
				<input id="delSubmit" onclick="delData()" class="btn btn-primary" type="button" value="撤 销"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>