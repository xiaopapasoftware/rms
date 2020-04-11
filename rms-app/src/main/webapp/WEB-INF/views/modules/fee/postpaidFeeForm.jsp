<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>后付费付款</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					if(""==$("#contractName").val()) {
						top.$.jBox.tip('请选择合同.','warning');
						return;
					}
					if(""==$("#electricSelfAmt").val()
						    && ""==$("#electricShareAmt").val()
						    && ""==$("#waterAmt").val()
							&& ""==$("#gasAmt").val()
							&& ""==$("#tvAmt").val()
							&& ""==$("#netAmt").val()
							&& ""==$("#serviceAmt").val()) {
							   top.$.jBox.tip('后付费费用不能全部为空值！','warning')
							   return;
					}
					if(""!=$("input[name='electricSelfAmtStartDate']").val() && ""!=$("input[name='electricSelfAmtEndDate']").val() && !dateCompare($("input[name='electricSelfAmtStartDate']").val(),$("input[name='electricSelfAmtEndDate']").val())){
						  top.$.jBox.tip('自用电费开始日期不能超过结束日期！','warning');
						  return;
					}
					if(""!=$("input[name='electricShareAmtStartDate']").val() && ""!=$("input[name='electricShareAmtEndDate']").val() && !dateCompare($("input[name='electricShareAmtStartDate']").val(),$("input[name='electricShareAmtEndDate']").val())){
						top.$.jBox.tip('分摊电费开始日期不能超过结束日期！','warning');
						  return;				
					}
					if(""!=$("input[name='waterAmtStartDate']").val() && ""!=$("input[name='waterAmtEndDate']").val() && !dateCompare($("input[name='waterAmtStartDate']").val(),$("input[name='waterAmtEndDate']").val())){
						top.$.jBox.tip('水费开始日期不能超过结束日期！','warning');
						  return;
					}
					if(""!=$("input[name='gasAmtStartDate']").val() && ""!=$("input[name='gasAmtEndDate']").val() && !dateCompare($("input[name='gasAmtStartDate']").val(),$("input[name='gasAmtEndDate']").val())){
						top.$.jBox.tip('燃气费开始日期不能超过结束日期！','warning');
						  return;
					}
					if(""!=$("input[name='tvAmtStartDate']").val() && ""!=$("input[name='tvAmtEndDate']").val() && !dateCompare($("input[name='tvAmtStartDate']").val(),$("input[name='tvAmtEndDate']").val())){
						top.$.jBox.tip('电视费开始日期不能超过结束日期！','warning');
						  return;
					}
					if(""!=$("input[name='netAmtStartDate']").val() && ""!=$("input[name='netAmtEndDate']").val() && !dateCompare($("input[name='netAmtStartDate']").val(),$("input[name='netAmtEndDate']").val())){
						top.$.jBox.tip('宽带费开始日期不能超过结束日期！','warning');
						  return;
					}
					if(""!=$("input[name='serviceAmtStartDate']").val() && ""!=$("input[name='serviceAmtEndDate']").val() && !dateCompare($("input[name='serviceAmtStartDate']").val(),$("input[name='serviceAmtEndDate']").val())){
						top.$.jBox.tip('服务费开始日期不能超过结束日期！','warning');
						  return;
					}
					loading('正在提交，请稍等...');
					$("#btnSubmit").attr("disabled",true);
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
		
		function dateCompare(startdate,enddate){   
			var arr=startdate.split("-");    
			var starttime= new Date(arr[0],arr[1],arr[2]);    
			var arrs=enddate.split("-");    
			var endtime=new Date(arrs[0],arrs[1],arrs[2]);    
			if(starttime>=endtime){   
				return false;   
			}else{
				return true;		
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/contract/rentContract/queryPublicBasicFeeInfo">合同初始事业费管理</a></li>
		<li><a href="${ctx}/fee/electricFee/initpostpaidFeeList">后付费查询</a></li>
		<li class="active"><a href="${ctx}/fee/electricFee/postpaidFeeForm">后付费付款</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="postpaidFee" action="${ctx}/fee/electricFee/postpaidFeeSave" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}" type="${messageType}"/>
		<div class="control-group">
			<label class="control-label">出租合同</label>
			<c:if test="${postpaidFee.id!=''&& postpaidFee.id!=null && postpaidFee.id!=undefined}"><!-- 修改 -->
				 <div class="controls">
				 	<form:input path="contractName" readonly="true"/>
				 </div>
			</c:if>
			<c:if test="${postpaidFee.id==''||postpaidFee.id==null||postpaidFee.id==undefined}"><!-- 新增 -->
					<div class="controls">
						<div class="input-append">
							<input id="rentContractId" name="rentContractId" type="hidden" value="${rentContractId}">
							<input id="contractName" readonly="readonly" type="text" value="${contractName}">
							<a id="rentContractButton" href="javascript:" class="btn" style="">&nbsp;<i class="icon-search"></i>&nbsp;</a>&nbsp;&nbsp;
						</div>
						<span class="help-inline"><font color="red">*</font></span>
						<script type="text/javascript">
							$("#rentContractButton, #contractName").click(function(){
								if ($("#rentContractButton").hasClass("disabled")){
									return true;
								}
								top.$.jBox.open("iframe:${ctx}/contract/rentContract/rentContractDialog", "选择合同", 1000, 520, {
									buttons:{"确定":"ok", "关闭":true}, submit:function(v, h, f){
										if (v=="ok"){
											var checkedId = h.find("iframe")[0].contentWindow.$("input[name='rentContractId']:checked");
											$("#rentContractId").val($(checkedId).val());
											$("#contractName").val($(checkedId).attr("attr-name"));
										}
									}
								});
							});
						</script>
					</div>
			</c:if>
		</div>
		<div class="control-group">
			<label class="control-label">自用电费</label>
			<div class="controls">
				<form:input path="electricSelfAmt" htmlEscape="false" class="input-xlarge number"/>&nbsp;&nbsp;&nbsp;
				从&nbsp;<input name="electricSelfAmtStartDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${postpaidFee.electricSelfAmtStartDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
				到&nbsp;<input name="electricSelfAmtEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${postpaidFee.electricSelfAmtEndDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">分摊电费</label>
			<div class="controls">
				<form:input path="electricShareAmt" htmlEscape="false" class="input-xlarge number"/>&nbsp;&nbsp;&nbsp;
				从&nbsp;<input name="electricShareAmtStartDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${postpaidFee.electricShareAmtStartDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
				到&nbsp;<input name=electricShareAmtEndDate type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${postpaidFee.electricShareAmtEndDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">水费</label>
			<div class="controls">
				<form:input path="waterAmt" htmlEscape="false" class="input-xlarge number"/>&nbsp;&nbsp;&nbsp;
				从&nbsp;<input name="waterAmtStartDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${postpaidFee.waterAmtStartDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
				到&nbsp;<input name="waterAmtEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${postpaidFee.waterAmtEndDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">燃气费</label>
			<div class="controls">
				<form:input path="gasAmt" htmlEscape="false" class="input-xlarge number"/>&nbsp;&nbsp;&nbsp;
				从&nbsp;<input name="gasAmtStartDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${postpaidFee.gasAmtStartDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
				到&nbsp;<input name="gasAmtEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${postpaidFee.gasAmtEndDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">电视费</label>
			<div class="controls">
				<form:input path="tvAmt" htmlEscape="false" class="input-xlarge number"/>&nbsp;&nbsp;&nbsp;
				从&nbsp;<input name="tvAmtStartDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${postpaidFee.tvAmtStartDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
				到&nbsp;<input name="tvAmtEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${postpaidFee.tvAmtEndDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">宽带费</label>
			<div class="controls">
				<form:input path="netAmt" htmlEscape="false" class="input-xlarge number"/>&nbsp;&nbsp;&nbsp;
				从&nbsp;<input name="netAmtStartDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${postpaidFee.netAmtStartDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
				到&nbsp;<input name="netAmtEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${postpaidFee.netAmtEndDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">服务费</label>
			<div class="controls">
				<form:input path="serviceAmt" htmlEscape="false" class="input-xlarge number"/>&nbsp;&nbsp;&nbsp;
				从&nbsp;<input name="serviceAmtStartDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${postpaidFee.serviceAmtStartDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
				到&nbsp;<input name="serviceAmtEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${postpaidFee.serviceAmtEndDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">房租</label>
			<div class="controls">
				<form:input path="houseRentAmt" htmlEscape="false" class="input-xlarge number"/>&nbsp;&nbsp;&nbsp;
				从&nbsp;<input name="houseRentAmtStartDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${postpaidFee.houseRentAmtStartDate}" pattern="yyyy-MM-dd"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
				到&nbsp;<input name="houseRentAmtEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${postpaidFee.houseRentAmtEndDate}" pattern="yyyy-MM-dd"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="fee:postpaidFee:edit">
				<c:if test="${postpaidFee.id==''||postpaidFee.id==null}">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
				</c:if>
				<c:if test="${postpaidFee.id!=''&& postpaidFee.id!=null && (postpaidFee.payStatus=='1'||postpaidFee.payStatus=='5')}">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
				</c:if>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>