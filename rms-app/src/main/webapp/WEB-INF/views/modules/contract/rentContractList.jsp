<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>出租合同管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {
            top.$.jBox.tip.mess = null;
        });

        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }

        function changeProject() {
            var project = $("[id='propertyProject.id']").val();
            var html = "<option value='' selected='selected'>全部</option>";
            if ("" != project) {
                $.ajaxSetup({cache: false});
                $.get("${ctx}/inventory/building/findList?id=" + project, function (data) {
                    for (var i = 0; i < data.length; i++) {
                        html += "<option value='" + data[i].id + "'>" + data[i].buildingName + "</option>";
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
            if ("" != building) {
                $.ajaxSetup({cache: false});
                $.get("${ctx}/inventory/house/findList?id=" + building, function (data) {
                    for (var i = 0; i < data.length; i++) {
                        html += "<option value='" + data[i].id + "'>" + data[i].houseNo + "</option>";
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
            if ("" != room) {
                $.ajaxSetup({cache: false});
                $.get("${ctx}/inventory/room/findList?id=" + room, function (data) {
                    for (var i = 0; i < data.length; i++) {
                        html += "<option value='" + data[i].id + "'>" + data[i].roomNo + "</option>";
                    }
                    $("[id='room.id']").html(html);
                });
            } else {
                $("[id='room.id']").html(html);
            }
            $("[id='room.id']").val("");
            $("[id='room.id']").prev("[id='s2id_room.id']").find(".select2-chosen").html("全部");
        }

        function toAudit(id, type) {
            var html = "<table style='margin:20px;'><tr><td><label>审核意见：</label></td><td><textarea id='auditMsg'></textarea></td></tr></table>";
            var content = {
                state1: {
                    content: html,
                    buttons: {'同意': 1, '拒绝': 2, '取消': 0},
                    buttonsFocus: 0,
                    submit: function (v, h, f) {
                        if (v == 0) {
                            return true; // close the window
                        } else if (v == 1) {
                            saveAudit(id, '1', type);
                        } else if (v == 2) {
                            saveAudit(id, '2', type);
                        }
                        return false;
                    }
                }
            };
            $.jBox.open(content, "审核", 350, 220, {});
        }

        function saveAudit(id, status, type) {
            loading('正在提交，请稍等...');
            var msg = $("#auditMsg").val();
            window.location.href = "${ctx}/contract/rentContract/audit?objectId=" + id + "&auditMsg=" + msg + "&auditStatus=" + status + "&type=" + type;
        }

        function auditHis(id) {
            $.jBox.open("iframe:${ctx}/contract/leaseContract/auditHis?objectId=" + id, '审核记录', 650, 400, {buttons: {'关闭': true}});
        }

        function processReturn(id, type) {
            confirmx("确认要发起退租吗?", function () {
                var curDate = new Date();
                var year = curDate.getFullYear();
                var month = curDate.getMonth() + 1;
                if (parseFloat(month) < 10) {
                    month = "0" + "" + month;
                }
                var day = curDate.getDate();
                if (parseFloat(day) < 10) {
                    day = "0" + "" + day;
                }
                var curDateStyle = year + "-" + month + "-" + day;
                var html = '<label style="width:120px;">实际退房日期：</label>';
                html += '<input name="returnDateStr" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"';
                html += 'value="' + curDateStyle + '"';
                html += 'onclick="WdatePicker({dateFmt:\'yyyy-MM-dd\',isShowClear:true});" style="width:196px;"/>';
                var submit = function (v, h, f) {
                    if (type == 'special') {//特殊退租
                        window.location.href = "${ctx}/contract/rentContract/specialReturnContract?id=" + id + "&returnDateStr=" + f.returnDateStr;
                    }
                    if (type == 'normal') {//正常退租
                        window.location.href = "${ctx}/contract/rentContract/returnContract?id=" + id + "&returnDateStr=" + f.returnDateStr;
                    }
                    if (type == 'early') {//提前退租
                        window.location.href = "${ctx}/contract/rentContract/earlyReturnContract?id=" + id + "&returnDateStr=" + f.returnDateStr;
                    }
                    if (type == 'late') {//逾期退租
                        window.location.href = "${ctx}/contract/rentContract/lateReturnContract?id=" + id + "&returnDateStr=" + f.returnDateStr;
                    }
                    return true;
                };
                $.jBox(html, {title: "选择实际退租日期", submit: submit});
            });
        }

        function resetForm() {
            $("#searchForm").find("select").each(function (index) {
                $(this).val("");
                $(this).prev("div").find(".select2-chosen").html("全部");

                $(this).find("option").each(function () {
                    $(this).removeAttr("selected");
                });
            });
            $("#searchForm").find("input").each(function (index) {
                if ($(this).attr("type") == "text")
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
    <li class="active"><a href="${ctx}/contract/rentContract/">出租合同列表</a></li>
    <shiro:hasPermission name="contract:rentContract:edit">
        <li><a href="${ctx}/contract/rentContract/form">出租合同添加</a></li>
    </shiro:hasPermission>
</ul>
<form:form id="searchForm" modelAttribute="rentContract" action="${ctx}/contract/rentContract/list" method="post"
           class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <ul class="ul-form">
        <li><label style="width:120px;">合同来源：</label>
            <form:select path="contractSource" class="input-medium" style="width:210px;">
                <form:option value="" label="全部"/>
                <form:options items="${fns:getDictList('contract_source')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
        </li>
        <li><label style="width:120px;">合同签约类型：</label>
            <form:select path="signType" class="input-medium" style="width:210px;">
                <form:option value="" label="全部"/>
                <form:options items="${fns:getDictList('contract_sign_type')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
        </li>
        <li><label style="width:120px;">合同编号：</label>
            <form:input path="contractCode" htmlEscape="false" maxlength="100" class="input-medium"
                        style="width:195px;"/>
        </li>
        <li><label style="width:120px;">合同名称：</label>
            <form:input path="contractName" htmlEscape="false" maxlength="100" class="input-medium"
                        style="width:195px;"/>
        </li>
        <li><label style="width:120px;">出租方式：</label>
            <form:select path="rentMode" class="input-medium" style="width:210px;">
                <form:option value="" label="全部"/>
                <form:options items="${fns:getDictList('rent_mode')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
        </li>
        <li><label style="width:120px;">物业项目：</label>
            <form:select path="propertyProject.id" class="input-medium" style="width:210px;" onchange="changeProject()">
                <form:option value="" label="全部"/>
                <form:options items="${projectList}" itemLabel="projectName" itemValue="id" htmlEscape="false"/>
            </form:select>
        </li>
        <li><label style="width:120px;">楼宇：</label>
            <form:select path="building.id" class="input-medium" style="width:210px;" onchange="buildingChange()">
                <form:option value="" label="全部"/>
                <form:options items="${buildingList}" itemLabel="buildingName" itemValue="id" htmlEscape="false"/>
            </form:select>
        </li>
        <li><label style="width:120px;">房屋：</label>
            <form:select path="house.id" class="input-medium" style="width:210px;" onchange="houseChange()">
                <form:option value="" label="全部"/>
                <form:options items="${houseList}" itemLabel="houseNo" itemValue="id" htmlEscape="false"/>
            </form:select>
        </li>
        <li><label style="width:120px;">房间：</label>
            <form:select path="room.id" class="input-medium" style="width:210px;">
                <form:option value="" label="全部"/>
                <form:options items="${roomList}" itemLabel="roomNo" itemValue="id" htmlEscape="false"/>
            </form:select>
        </li>
        <li><label style="width:120px;">合同生效时间：</label>
            <input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
                   value="<fmt:formatDate value="${rentContract.startDate}" pattern="yyyy-MM-dd"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:196px;"/>
        </li>
        <li><label style="width:120px;">合同过期时间：</label>
            <input name="expiredDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
                   value="<fmt:formatDate value="${rentContract.expiredDate}" pattern="yyyy-MM-dd"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:196px;"/>
        </li>
        <li><label style="width:120px;">合同签订时间：</label>
            <input name="signDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
                   value="<fmt:formatDate value="${rentContract.signDate}" pattern="yyyy-MM-dd"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:196px;"/>
        </li>
        <li><label style="width:120px;">实际退房日期：</label>
            <input name="signDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
                   value="<fmt:formatDate value="${rentContract.returnDate}" pattern="yyyy-MM-dd"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:196px;"/>
        </li>
        <li><label style="width:120px;">合同审核状态：</label>
            <form:select path="contractStatus" class="input-medium" style="width:210px;">
                <form:option value="" label="全部"/>
                <form:options items="${fns:getDictList('rent_contract_status')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
        </li>
        <li><label style="width:120px;">合同业务状态：</label>
            <form:select path="contractBusiStatus" class="input-medium" style="width:210px;">
                <form:option value="" label="全部"/>
                <form:options items="${fns:getDictList('rent_contract_busi_status')}" itemLabel="label"
                              itemValue="value" htmlEscape="false"/>
            </form:select>
        </li>
        <li><label style="width:120px;">租客姓名：</label>
            <form:input path="tenantName" htmlEscape="false" maxlength="100" class="input-medium" style="width:195px;"/>
        </li>
        <li><label style="width:120px;">租客手机号：</label>
            <form:input path="tenantMobileNo" htmlEscape="false" maxlength="100" class="input-medium"
                        style="width:195px;"/>
        </li>
        <li><label style="width:120px;">租客身份证：</label>
            <form:input path="tenantIdNo" htmlEscape="false" maxlength="100" class="input-medium" style="width:195px;"/>
        </li>
        <li class="btns">
            <shiro:hasPermission name="contract:rentContract:view">
                <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
                <!--<shiro:hasPermission name="contract:superRentContract:edit">
                <a href="${ctx}/contract/rentContract/testProcess">TestProcess</a>
            </shiro:hasPermission>-->
                <input type="button" class="btn btn-primary" value="重置" onclick="resetForm()"/>
            </shiro:hasPermission>
        </li>
        <li class="clearfix"></li>
    </ul>
</form:form>
<sys:message content="${message}" type="${messageType}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed" style="width:3000px;">
    <thead>
    <tr>
        <th>合同编号/名称</th>
        <th>出租方式</th>
        <th>合同来源</th>
        <th>数据来源</th>
        <th>付费方式</th>
        <th>月租金额</th>
        <th>签订类型</th>
        <th>签订时间</th>
        <th>生效时间</th>
        <th>过期时间</th>
        <th>租赁周期（月）</th>
        <th>审核状态</th>
        <th>业务状态</th>
        <th>是否返租促销</th>
        <th>是否房租全免</th>
        <th>原定金编号/名称</th>
        <th>原合同编号/名称</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="rentContract">
        <tr
                <c:if test="${rentContract.contractBusiStatus=='7'||rentContract.contractBusiStatus=='8'||rentContract.contractBusiStatus=='9'||rentContract.contractBusiStatus=='16'}">style="background-color:#f1f2f2;"</c:if>>
            <td>
                <a href="${ctx}/contract/rentContract/form?id=${rentContract.id}">
                        ${rentContract.contractCode} - ${rentContract.contractName}
                </a>
            </td>
            <td>
                    ${fns:getDictLabel(rentContract.rentMode, 'rent_mode', '')}
            </td>
            <td>
                    ${fns:getDictLabel(rentContract.contractSource, 'contract_source', '')}
            </td>
            <td>
                    ${fns:getDictLabel(rentContract.dataSource, 'data_source', '管理系统')}
            </td>
            <td>
                    ${fns:getDictLabel(rentContract.chargeType, 'charge_type', '')}
            </td>
            <td>
                    ${rentContract.rental}
            </td>
            <td>
                    ${fns:getDictLabel(rentContract.signType, 'contract_sign_type', '')}
            </td>
            <td>
                <fmt:formatDate value="${rentContract.signDate}" pattern="yyyy-MM-dd"/>
            </td>
            <td>
                <fmt:formatDate value="${rentContract.startDate}" pattern="yyyy-MM-dd"/>
            </td>
            <td>
                <fmt:formatDate value="${rentContract.expiredDate}" pattern="yyyy-MM-dd"/>
            </td>
            <td>
                    ${rentContract.leaseTermMonths}
            </td>
            <td>
                    ${fns:getDictLabel(rentContract.contractStatus, 'rent_contract_status', '')}
            </td>
            <td>
                    ${fns:getDictLabel(rentContract.contractBusiStatus, 'rent_contract_busi_status', '')}
            </td>
            <td>
                    ${fns:getDictLabel(rentContract.hasFree, 'yes_no', '否')}
            </td>
            <td>
                    ${fns:getDictLabel(rentContract.derateRentFlag, 'yes_no', '否')}
            </td>
            <td>
                    ${rentContract.refAgreementNo} - ${rentContract.refAgreementName}
            </td>
            <td>
                    ${rentContract.refContractNo} - ${rentContract.refContractName}
            </td>
            <td>
                <shiro:hasPermission name="contract:rentContract:edit">
                    <c:if test="${rentContract.dataSource=='2' && rentContract.contractStatus=='0'}">
                        <a href="${ctx}/contract/rentContract/cancel?objectId=${rentContract.id}"
                           onclick="return confirmx('确认要取消吗?', this.href)">取消</a>
                    </c:if>
                    <c:if test="${rentContract.contractStatus=='3'||rentContract.contractStatus=='0'||rentContract.contractStatus=='1'}">
                        <a href="${ctx}/contract/rentContract/form?id=${rentContract.id}">修改</a>
                    </c:if>
                    <c:if test="${rentContract.contractStatus=='6' && rentContract.contractBusiStatus=='0'}">
                        <a href="${ctx}/contract/rentContract/renewContract?id=${rentContract.id}"
                           onclick="return confirmx('确认要续签吗?', this.href)">续签</a>
                    </c:if>
                </shiro:hasPermission>
                <shiro:hasPermission name="contract:superRentContract:edit">
                    <!-- 出租合同已经被审核通过，且有效的情况下的后门修改 -->
                    <c:if test="${rentContract.contractStatus=='6' && rentContract.contractBusiStatus=='0'}">
                        <a href="${ctx}/contract/rentContract/form?id=${rentContract.id}">【后门修改】</a>
                    </c:if>
                    <!-- 出租合同账务收据审核拒绝，后门修改 -->
                    <c:if test="${rentContract.contractStatus=='5' && (rentContract.contractBusiStatus==''||rentContract.contractBusiStatus==null)}">
                        <a href="${ctx}/contract/rentContract/form?id=${rentContract.id}">【后门修改】</a>
                    </c:if>
                    <!-- 出租合同的审核状态为审核通过，同时出租合同业务状态为【退租核算完成到账收据待登记4、退租款项待审核5、退租款项审核拒绝6】，后门撤销 -->
                    <c:if test="${rentContract.contractStatus=='6' && (rentContract.contractBusiStatus=='4' || rentContract.contractBusiStatus=='5' || rentContract.contractBusiStatus=='6')}">
                        <a href="${ctx}/contract/rentContract/backCancelRetreat?id=${rentContract.id}"
                           onclick="return confirmx('确认要退租核算撤销吗?', this.href)">【后门退租核算撤销】</a>
                    </c:if>
                </shiro:hasPermission>
                <shiro:hasPermission name="contract:rentContract:return">
                    <c:if test="${rentContract.contractStatus=='6' && rentContract.contractBusiStatus=='0'}">
                        <a href="javascript:void(0);"
                           onclick="javascript:processReturn('${rentContract.id}','normal');">正常退租</a>
                        <a href="javascript:void(0);"
                           onclick="javascript:processReturn('${rentContract.id}','early');">提前退租</a>
                        <a href="javascript:void(0);"
                           onclick="javascript:processReturn('${rentContract.id}','late');">逾期退租</a>
                    </c:if>
                    <c:if test="${rentContract.contractStatus=='6' && rentContract.contractBusiStatus=='1'}">
                        <a href="${ctx}/contract/rentContract/rollbackFromPreturnToNormal?id=${rentContract.id}"
                           onclick="return confirmx('确认要把提前退租合同恢复为有效合同吗?', this.href)">恢复有效</a>
                        <a href="${ctx}/contract/rentContract/toEarlyReturnCheck?id=${rentContract.id}"
                           onclick="$('#messageBox').remove();">提前退租核算</a>
                    </c:if>
                    <c:if test="${rentContract.contractStatus=='6' && rentContract.contractBusiStatus=='2'}">
                        <a href="${ctx}/contract/rentContract/rollbackToNormal?id=${rentContract.id}"
                           onclick="return confirmx('确认要把正常退租合同恢复为有效合同吗?', this.href)">恢复有效</a>
                        <a href="${ctx}/contract/rentContract/toReturnCheck?id=${rentContract.id}"
                           onclick="$('#messageBox').remove();">正常退租核算</a>
                    </c:if>
                    <c:if test="${rentContract.contractStatus=='6' && rentContract.contractBusiStatus=='3'}">
                        <a href="${ctx}/contract/rentContract/toLateReturnCheck?id=${rentContract.id}"
                           onclick="$('#messageBox').remove();">逾期退租核算</a>
                    </c:if>
                </shiro:hasPermission>
                <shiro:hasPermission name="contract:rentContract:specialreturn">
                    <c:if test="${rentContract.contractStatus=='6' && (rentContract.contractBusiStatus=='0'||rentContract.contractBusiStatus=='18')}">
                        <a href="javascript:void(0);"
                           onclick="javascript:processReturn('${rentContract.id}','special');">特殊退租</a>
                    </c:if>
                </shiro:hasPermission><br/>
                <shiro:hasPermission name="contract:rentContract:change">
                    <c:if test="${rentContract.contractStatus=='6' && rentContract.contractBusiStatus=='0'}">
                        <a href="${ctx}/contract/rentContract/changeContract?id=${rentContract.id}"
                           onclick="return confirmx('确认要协议变更吗?', this.href)">协议变更</a>
                    </c:if>
                </shiro:hasPermission>
                <c:if test="${rentContract.contractStatus!='0' && rentContract.contractStatus!='1'}">
                    <a href="javascript:void(0);" onclick="auditHis('${rentContract.id}')">审核记录</a>
                </c:if>
                <shiro:hasPermission name="contract:rentContract:deleteContract">
                    <a href="${ctx}/contract/rentContract/deleteContract?id=${rentContract.id}"
                       onclick="return confirmx('确认要删除合同吗?', this.href)">删除合同</a>
                </shiro:hasPermission>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>