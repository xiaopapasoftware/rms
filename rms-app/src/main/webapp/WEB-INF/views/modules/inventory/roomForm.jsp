<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>房间信息管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#roomNo, #meterNo, #roomSpace,#orientation").keypress(function (event) {
                if (event.keyCode == 13) {
                    event.preventDefault();
                }
            });
            $("#btnSubmit").click(function () {
                if (!$("#attachmentPath").val()) {
                    alert('请先上传房间图片！');
                    return false;
                }
            });
            $("#inputForm").validate({
                submitHandler: function (form) {
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

        function changeProject() {
            var project = $("[id='propertyProject.id']").val();
            var html = "<option value='' selected='selected'>请选择...</option>";
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
            $("[id='building.id']").prev("[id='s2id_building.id']").find(".select2-chosen").html("请选择...");

            $("[id='house.id']").html(html);
            $("[id='house.id']").val("");
            $("[id='house.id']").prev("[id='s2id_house.id']").find(".select2-chosen").html("请选择...");
        }

        function buildingChange() {
            var building = $("[id='building.id']").val();
            var html = "<option value='' selected='selected'>请选择...</option>";
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
            $("[id='house.id']").prev("[id='s2id_house.id']").find(".select2-chosen").html("请选择...");
        }

        function isFeatureChange() {
            var isFeature = $("#isFeature").val();
            if ("1" == isFeature) {
                $("#shortDesc").addClass("required");
                $("#shortDesc").next("label").remove();
                $("#shortDesc").next("span").show();
                $("#shortLocation").addClass("required");
                $("#shortLocation").next("label").remove();
                $("#shortLocation").next("span").show();

            } else {
                $("#shortDesc").removeClass("required");
                $("#shortDesc").next("label").remove();
                $("#shortDesc").next("span").hide();
                $("#shortLocation").removeClass("required");
                $("#shortLocation").next("label").remove();
                $("#shortLocation").next("span").hide();
            }
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/inventory/room/">房间信息列表</a></li>
    <li class="active"><a href="${ctx}/inventory/room/form?id=${room.id}">房间信息<shiro:hasPermission
            name="inventory:room:edit">${not empty room.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission
            name="inventory:room:edit">查看</shiro:lacksPermission></a></li>
</ul>
<br/>
<form:form id="inputForm" modelAttribute="room" action="${ctx}/inventory/room/save" method="post"
           class="form-horizontal">
    <form:hidden path="id"/>
    <sys:message content="${message}" type="${messageType}"/>
    <div class="control-group">
        <label class="control-label">物业项目：</label>
        <div class="controls">
            <form:select path="propertyProject.id" class="input-xlarge required" onchange="changeProject()">
                <form:option value="" label="请选择..."/>
                <form:options items="${listPropertyProject}" itemLabel="projectName" itemValue="id" htmlEscape="false"/>
            </form:select>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">楼宇：</label>
        <div class="controls">
            <form:select path="building.id" class="input-xlarge required" onchange="buildingChange()">
                <form:option value="" label="请选择..."/>
                <form:options items="${listBuilding}" itemLabel="buildingName" itemValue="id" htmlEscape="false"/>
            </form:select>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">房屋号：</label>
        <div class="controls">
            <form:select path="house.id" class="input-xlarge required">
                <form:option value="" label="请选择..."/>
                <form:options items="${listHouse}" itemLabel="houseNo" itemValue="id" htmlEscape="false"/>
            </form:select>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">房间号（0:公共区域）：</label>
        <div class="controls">
            <form:input path="roomNo" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">预约热线电话：</label>
        <div class="controls">
            <form:input path="reservationPhone" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">智能电表号：</label>
        <div class="controls">
            <form:input path="meterNo" htmlEscape="false" maxlength="100" class="input-xlarge"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">支付间隔月数：</label>
        <div class="controls">
            <form:input type="number" placeholder="请填写正整数" id="rentMonthGap" path="rentMonthGap" htmlEscape="false"
                        maxlength="2" class="input-xlarge digits required"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">押金月数：</label>
        <div class="controls">
            <form:input type="number" placeholder="请填写正整数" id="deposMonthCount" path="deposMonthCount"
                        htmlEscape="false" maxlength="2" class="input-xlarge digits required"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">房间面积（平方米）：</label>
        <div class="controls">
            <form:input path="roomSpace" htmlEscape="false" class="input-xlarge number  required"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">朝向：</label>
        <div class="controls">
            <form:radiobuttons name="orientation" path="orientation" items="${fns:getDictList('orientation')}"
                               itemLabel="label"
                               itemValue="value" htmlEscape="false" class="required"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">物品配置：</label>
        <div class="controls">
            <form:checkboxes path="roomConfigList" items="${fns:getDictList('room_config')}" itemLabel="label"
                             itemValue="value" htmlEscape="false" class=""/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">房间图片（仅支持JPG、PNG、JPEG格式）：</label>
        <div class="controls">
            <form:hidden id="attachmentPath" path="attachmentPath" htmlEscape="false" maxlength="4000"
                         class="input-xlarge"/>
            <sys:ckfinder input="attachmentPath" type="files" uploadPath="/房间图片" selectMultiple="true"/>
            <span class="help-inline" style="display:none;"><font color="red">*</font></span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">是否精选房源：</label>
        <div class="controls">
            <form:select path="isFeature" class="input-xlarge required" onchange="isFeatureChange()">
                <form:option value="" label="请选择..."/>
                <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">意向租金：</label>
        <div class="controls">
            <form:input path="rental" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
            <span class="help-inline" style="display:none;"><font color="red">*</font></span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">房源描述：</label>
        <div class="controls">
            <form:input path="shortDesc" htmlEscape="false" maxlength="100" class="input-xlarge"/>
            <span class="help-inline" style="display:none;"><font color="red">*</font></span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">房源地址描述：</label>
        <div class="controls">
            <form:input path="shortLocation" htmlEscape="false" maxlength="100" class="input-xlarge"/>
            <span class="help-inline" style="display:none;"><font color="red">*</font></span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">跟进销售：</label>
        <div class="controls">
            <sys:treeselect id="salesUser" name="salesUser" value="${room.salesUser.id}"
                            labelName="salesUser.name" labelValue="${room.salesUser.name}"
                            title="跟进销售" url="/sys/office/treeData?type=3" cssClass="required" allowClear="true"
                            notAllowSelectParent="true"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">备注信息：</label>
        <div class="controls">
            <form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">（1）费用描述：</label>
        <div class="controls">
            <form:input path="feeDesc1" htmlEscape="false" class="input-xlarge"/>&nbsp;&nbsp;&nbsp;
        </div>
        <label class="control-label">（1）费用金额（元）：</label>
        <div class="controls">
            <form:input path="feeAmt1" htmlEscape="false" class="input-xlarge number"/>&nbsp;&nbsp;&nbsp;
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">（2）费用描述：</label>
        <div class="controls"><form:input path="feeDesc2" htmlEscape="false" class="input-xlarge"/>&nbsp;&nbsp;&nbsp;
        </div>
        <label class="control-label">（2）费用金额（元）：</label>
        <div class="controls"><form:input path="feeAmt2" htmlEscape="false" class="input-xlarge number"/>&nbsp;&nbsp;&nbsp;</div>
    </div>
    <div class="control-group">
        <label class="control-label">（3）费用描述：</label>
        <div class="controls"><form:input path="feeDesc3" htmlEscape="false" class="input-xlarge"/>&nbsp;&nbsp;&nbsp;
        </div>
        <label class="control-label">（3）费用金额（元）：</label>
        <div class="controls"><form:input path="feeAmt3" htmlEscape="false" class="input-xlarge number"/>&nbsp;&nbsp;&nbsp;</div>
    </div>
    <div class="control-group">
        <label class="control-label">（4）费用描述：</label>
        <div class="controls"><form:input path="feeDesc4" htmlEscape="false" class="input-xlarge"/>&nbsp;&nbsp;&nbsp;
        </div>
        <label class="control-label">（4）费用金额（元）：</label>
        <div class="controls"><form:input path="feeAmt4" htmlEscape="false" class="input-xlarge number"/>&nbsp;&nbsp;&nbsp;</div>
    </div>
    <div class="control-group">
        <label class="control-label">（5）费用描述：</label>
        <div class="controls"><form:input path="feeDesc5" htmlEscape="false" class="input-xlarge"/>&nbsp;&nbsp;&nbsp;
        </div>
        <label class="control-label">（5）费用金额（元）：</label>
        <div class="controls"><form:input path="feeAmt5" htmlEscape="false" class="input-xlarge number"/>&nbsp;&nbsp;&nbsp;</div>
    </div>
    <div class="form-actions">
        <shiro:hasPermission name="inventory:room:edit"><input id="btnSubmit" class="btn btn-primary" type="submit"
                                                               value="保 存"/>&nbsp;</shiro:hasPermission>
        <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form:form>
</body>
</html>