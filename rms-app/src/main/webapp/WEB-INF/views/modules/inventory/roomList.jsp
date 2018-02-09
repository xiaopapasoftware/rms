<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>房间信息管理</title>
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

        function finishDirect(roomId) {
            $.get("${ctx}/inventory/room/finishDirect?id=" + roomId, function (data) {
                if ("SUCCESS" == data) {
                    alertx("操作成功！");
                } else {
                    alertx("操作失败！");
                }
                $("#searchForm").submit();
            });
        }

    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/inventory/room/">房间信息列表</a></li>
    <shiro:hasPermission name="inventory:room:edit">
        <li><a href="${ctx}/inventory/room/form">房间信息添加</a></li>
    </shiro:hasPermission>
</ul>
<form:form id="searchForm" modelAttribute="room" action="${ctx}/inventory/room/list" method="post"
           class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <ul class="ul-form">
        <li><label>物业项目：</label>
            <form:select path="propertyProject.id" class="input-medium" onchange="changeProject()">
                <form:option value="" label="请选择..."/>
                <form:options items="${listPropertyProject}" itemLabel="projectName" itemValue="id" htmlEscape="false"/>
            </form:select>
        </li>
        <li><label>楼宇：</label>
            <form:select path="building.id" class="input-medium" onchange="buildingChange()">
                <form:option value="" label="请选择..."/>
                <form:options items="${listBuilding}" itemLabel="buildingName" itemValue="id" htmlEscape="false"/>
            </form:select>
        </li>
        <li><label>房屋号：</label>
            <form:select path="house.id" class="input-medium">
                <form:option value="" label="请选择..."/>
                <form:options items="${listHouse}" itemLabel="houseNo" itemValue="id" htmlEscape="false"/>
            </form:select>
        </li>
        <li><label>房间号：</label>
            <form:input path="roomNo" htmlEscape="false" maxlength="100" class="input-medium"/>
        </li>
        <li><label>房间状态：</label>
            <form:select path="roomStatus" class="input-medium">
                <form:option value="" label="请选择..."/>
                <form:options items="${fns:getDictList('room_status')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
        </li>
        <li>
            <label>精选房源：</label>
            <form:select path="isFeature" class="input-medium">
                <form:option value="" label="请选择..."/>
                <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
        </li>
        <li>
            <label>支付间隔：</label>
            <form:input path="rentMonthGap" htmlEscape="false" maxlength="100" class="input-medium"/>
        </li>
        <li>
            <label>押金月数：</label>
            <form:input path="deposMonthCount" htmlEscape="false" maxlength="100" class="input-medium"/>
        </li>
        <li>
            <label>预约热线：</label>
            <form:input path="reservationPhone" htmlEscape="false" maxlength="100" class="input-medium"/>
        </li>
        <li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
        <li class="clearfix"></li>
    </ul>
</form:form>
<sys:message content="${message}" type="${messageType}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>物业项目</th>
        <th>楼宇</th>
        <th>房屋号</th>
        <th>房间号</th>
        <th>预约热线电话</th>
        <th>精选房源</th>
        <th>房间状态</th>
        <th>电表号</th>
        <th>支付间隔月数</th>
        <th>押金月数</th>
        <th>房间面积</th>
        <th>支付宝同步状态</th>
        <th>支付宝上下架状态</th>
        <th>朝向</th>
        <th>创建时间</th>
        <th>修改时间</th>
        <th>创建人</th>
        <th>修改人</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="room">
        <tr>
            <td>${room.propertyProject.projectName}</td>
            <td>${room.building.buildingName}</td>
            <td>${room.house.houseNo}</td>
            <td><a href="${ctx}/inventory/room/form?id=${room.id}">${room.roomNo}</a></td>
            <td>${room.reservationPhone}</td>
            <td>${fns:getDictLabel(room.isFeature, 'yes_no', '')}</td>
            <td>${fns:getDictLabel(room.roomStatus, 'room_status', '')}</td>
            <td>${room.meterNo}</td>
            <td>${room.rentMonthGap}</td>
            <td>${room.deposMonthCount}</td>
            <td>${room.roomSpace}</td>
            <td>
                <c:if test="${room.alipayStatus eq '1'}">
                    已同步
                </c:if>
                <c:if test="${room.alipayStatus != '1'}">
                    未同步
                </c:if>
            </td>
            <td>
                <c:if test="${room.alipayStatus eq '1' and room.up eq '1'}">
                    已上架
                </c:if>
                <c:if test="${room.alipayStatus eq '1' and room.up eq '0'}">
                    已下架
                </c:if>
            </td>
            <td>
                    ${fns:getDictLabels(room.orientation, 'orientation', '')}
            </td>
            <td><fmt:formatDate value="${room.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td><fmt:formatDate value="${room.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td>${room.createBy.loginName}</td>
            <td>${room.updateBy.loginName}</td>
            <shiro:hasPermission name="inventory:room:edit">
                <td><a href="${ctx}/inventory/room/form?id=${room.id}">修改</a></td>
            </shiro:hasPermission>
            <shiro:hasPermission name="inventory:room:del">
                <td><a href="${ctx}/inventory/room/delete?id=${room.id}"
                       onclick="return confirmx('确认要删除该房间及图片信息吗？', this.href)">删除</a></td>
            </shiro:hasPermission>
            <shiro:hasPermission name="device:room:done">
                <td><c:if test="${room.roomStatus eq '0'}"><a href="#"
                                                              onclick="finishDirect('${room.id}');">装修完成</a></c:if></td>
            </shiro:hasPermission>
            <shiro:hasPermission name="device:roomDevices:edit">
                <td><a href="${ctx}/device/roomDevices/maintainDevices?roomId=${room.id}">设备维护</a></td>
            </shiro:hasPermission>
            <shiro:hasPermission name="device:roomDevices:view">
                <td><a href="${ctx}/device/roomDevices/maintainDevices?roomId=${room.id}">设备查看</a></td>
            </shiro:hasPermission>
            <shiro:hasPermission name="alipay:houseAndRoom:sync">
                <c:if test="${room.building.type eq '1' and room.house.intentMode eq '1' and (room.roomStatus eq '1' or room.roomStatus eq '2' or room.roomStatus eq '3')}">
                    <td><a href="${ctx}/app/alipay/syncRoom/${room.id}">分散式同步支付宝</a></td>
                </c:if>
                <c:if test="${room.building.type eq '2' and room.house.intentMode eq '1' and (room.roomStatus eq '1' or room.roomStatus eq '2' or room.roomStatus eq '3')}">
                    <td><a href="${ctx}/app/alipay/syncRoom/${room.id}">集中式同步支付宝</a></td>
                </c:if>
                <c:if test="${room.alipayStatus eq '1' and room.up eq '0'}">
                    <td><a href="${ctx}/app/alipay/upRoom/${room.id}">上架</a></td>
                </c:if>
                <c:if test="${room.alipayStatus eq '1' and room.up eq '1'}">
                    <td><a href="${ctx}/app/alipay/downRoom/${room.id}">下架</a></td>
                </c:if>
            </shiro:hasPermission>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>