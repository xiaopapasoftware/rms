<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>账务交易管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#inputForm").validate({
                submitHandler: function (form) {
                    var tradeTypeVar = $("#tradeType").val();
                    //当交易类型为定金转违约("2")时，不能人为添加收据记录（收据记录集合必须为空，收据总金额必须为0）
                    if (tradeTypeVar == "2") {
                        if ($('input[id*="receiptList"]').size() > 0) {
                            top.$.jBox.tip('定金转违约账务交易类型，不能添加收据！', 'warning');
                            return;
                        }
                    }
                    //当交易类型为预约定金("1")、新签合同("3")、续签合同("4")、电费充值("11")、公共事业费后付("12")，则收据金额之和必须与账务交易金额一致；
                    if (tradeTypeVar == "1" || tradeTypeVar == "3" || tradeTypeVar == "4" || tradeTypeVar == "11" || tradeTypeVar == "12") {
                        var preTradeAmount = parseFloat($("#tradeAmount").val());
                        var calculatedAmount = 0;
                        $("input[id^='receiptList'][id$='_receiptAmount']").each(function () {
                            calculatedAmount = calculatedAmount + parseFloat($(this).val());
                        });
                        if (preTradeAmount.toFixed(2) != calculatedAmount.toFixed(2)) {
                            top.$.jBox.tip('账务交易总金额与收据总金额不相等！', 'warning');
                            return;
                        }
                    }
                    //当交易类型为提前退租("6")、正常退租("7")、逾期退租("8")、特殊退租("9")时;
                    //如果账务交易方向为应付（金额为0也是应付）， 则收据记录集合必须为空;
                    //如果账务交易方向为应收， 则收据金额必须与账务交易金额一致;
                    if (tradeTypeVar == "6" || tradeTypeVar == "7" || tradeTypeVar == "8" || tradeTypeVar == "9") {
                        var tradeDirect = $("#tradeDirection").val();
                        if ("0" == tradeDirect) {//应付
                            if ($('input[id*="receiptList"]').size() > 0) {
                                top.$.jBox.tip('当前的账务交易类型，不能添加收据！', 'warning');
                                return;
                            }
                        }
                        if ("1" == tradeDirect) {//应收
                            var preTradeAmount = parseFloat($("#tradeAmount").val());
                            var calculatedAmount = 0;
                            $("input[id^='receiptList'][id$='_receiptAmount']").each(function () {
                                calculatedAmount = calculatedAmount + Number($(this).val());
                            });
                            if (preTradeAmount.toFixed(2) != calculatedAmount.toFixed(2)) {
                                top.$.jBox.tip('账务交易总金额与收据总金额不相等！', 'warning');
                                return;
                            }
                        }
                    }
                    //本次页面上的收据号码列表不能重复
                    var repeatFlag = false;
                    var receiptNos = new Array();
                    $("input[id^='receiptList'][id$='_receiptNo']").each(function () {
                        receiptNos.push($(this).val());
                    });
                    var nary = receiptNos.sort();
                    for (var i = 0; i < nary.length - 1; i++) {
                        if (nary[i] == nary[i + 1]) {
                            repeatFlag = true;
                        }
                    }
                    if (repeatFlag) {
                        top.$.jBox.tip('录入的收据编号有重复！', 'warning');
                        return;
                    }
                    loading('正在提交，请稍等...');
                    $("#btnSubmit").attr("disabled", true);
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        });

        function addRow(list, idx, tpl, row) {
            $(list).append(Mustache.render(tpl, {
                idx: idx, delBtn: true, row: row
            }));
            $(list + idx).find("select").each(function () {
                $(this).val($(this).attr("data-value"));
            });
            $(list + idx).find("input[type='checkbox'], input[type='radio']").each(function () {
                var ss = $(this).attr("data-value").split(',');
                for (var i = 0; i < ss.length; i++) {
                    if ($(this).val() == ss[i]) {
                        $(this).attr("checked", "checked");
                    }
                }
            });
        }

        function delRow(obj, prefix) {
            var id = $(prefix + "_id");
            var delFlag = $(prefix + "_delFlag");
            if (id.val() == "") {
                $(obj).parent().parent().remove();
            } else if (delFlag.val() == "0") {
                delFlag.val("1");
                $(obj).html("&divide;").attr("title", "撤销删除");
                $(obj).parent().parent().addClass("error");
            } else if (delFlag.val() == "1") {
                delFlag.val("0");
                $(obj).html("&times;").attr("title", "删除");
                $(obj).parent().parent().removeClass("error");
            }
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/funds/paymentTrans/">款项交易列表</a></li>
    <li class="active">
        <a href="javascript:void(0);">到账登记</a>
    </li>
</ul>
<br/>
<form:form id="inputForm" modelAttribute="tradingAccounts" action="${ctx}/funds/tradingAccounts/save" method="post"
           class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="tradeId"/>
    <form:hidden path="transIds"/>
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
            <form:input path="tradeTypeDesc" htmlEscape="false" class="input-xlarge" readonly="true"/>
            <form:hidden path="tradeType" htmlEscape="false" class="input-xlarge" readonly="true"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">账务交易方向：</label>
        <div class="controls">
            <form:input path="tradeDirectionDesc" htmlEscape="false" class="input-xlarge" readonly="true"/>
            <form:hidden path="tradeDirection" htmlEscape="false" class="input-xlarge" readonly="true"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">账务交易总金额：</label>
        <div class="controls">
            <form:input path="tradeAmount" htmlEscape="false" readonly="true" class="input-xlarge"/>
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
                <form:options items="${fns:getDictList('receive_type')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
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
                    <th>收据日期</th>
                    <th>款项开始日期</th>
                    <th>款项结束日期</th>
                    <th>备注</th>
                    <th width="10">&nbsp;</th>
                </tr>
                </thead>
                <tbody id="receiptList">
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="6"><a href="javascript:"
                                       onclick="addRow('#receiptList', receiptRowIdx, receiptTpl);receiptRowIdx = receiptRowIdx + 1;"
                                       class="btn">新增</a></td>
                </tr>
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
								<input id="receiptList{{idx}}_transBeginDateDesc" name="receiptList[{{idx}}].transBeginDateDesc" type="text" readonly="readonly" value="{{row.transBeginDateDesc}}" class="input-small" style="width:100px;"/>
							</td>
							<td>
								<input id="receiptList{{idx}}_transEndDateDesc" name="receiptList[{{idx}}].transEndDateDesc" type="text" readonly="readonly" value="{{row.transEndDateDesc}}" class="input-small" style="width:100px;"/>
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
                var receiptRowIdx = 0, receiptTpl = $("#receiptTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
                $(document).ready(function () {
                    var data = ${fns:toJson(tradingAccounts.receiptList)};
                    for (var i = 0; i < data.length; i++) {
                        addRow('#receiptList', receiptRowIdx, receiptTpl, data[i]);
                        receiptRowIdx = receiptRowIdx + 1;
                    }
                });
            </script>
        </div>
    </div>
    <!-- 新签合同/续签合同/提前退租/正常退租/逾期退租/特殊退租--入账 -->
    <c:if test="${tradingAccounts.tradeDirection=='1' && (tradingAccounts.tradeType == '3'|| tradingAccounts.tradeType == '4'|| tradingAccounts.tradeType == '6'|| tradingAccounts.tradeType == '7'|| tradingAccounts.tradeType == '8'|| tradingAccounts.tradeType == '9')}">
        <div class="control-group">
            <label class="control-label">出租合同收据：</label>
            <div class="controls">
                <form:hidden id="rentContractReceiptFile" path="rentContractReceiptFile" htmlEscape="false"
                             maxlength="4000" class="input-xlarge"/>
                <sys:ckfinder input="rentContractReceiptFile" type="files" uploadPath="/出租合同收据" selectMultiple="true"/>
            </div>
        </div>
    </c:if>
    <c:if test="${tradingAccounts.tradeDirection=='1' && tradingAccounts.tradeType == '1'}"><!-- 预约定金--入账 -->
        <div class="control-group">
            <label class="control-label">定金协议收据：</label>
            <div class="controls">
                <form:hidden id="depositReceiptFile" path="depositReceiptFile" htmlEscape="false" maxlength="4000"
                             class="input-xlarge"/>
                <sys:ckfinder input="depositReceiptFile" type="files" uploadPath="/定金收据" selectMultiple="true"/>
            </div>
        </div>
    </c:if>
    <div class="control-group">
        <label class="control-label">备注信息：</label>
        <div class="controls">
            <form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
        </div>
    </div>
    <div class="form-actions">
        <shiro:hasPermission name="funds:tradingAccounts:edit"><input id="btnSubmit" class="btn btn-primary"
                                                                      type="submit"
                                                                      value="保 存"/>&nbsp;</shiro:hasPermission>
        <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form:form>
</body>
</html>