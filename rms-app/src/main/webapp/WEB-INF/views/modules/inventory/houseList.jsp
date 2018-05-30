<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>房屋信息管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {
            top.$.jBox.tip.mess = null;

            $("#btnExport").click(function() {
                $("#searchForm").attr("action", "${ctx}/inventory/house/exportHouse");
                $("#searchForm").submit();
                $("#searchForm").attr("action", "${ctx}/inventory/house/list");
            });
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
        }

        function finishDirect(houseId) {
            $.get("${ctx}/inventory/house/finishDirect?id=" + houseId, function (data) {
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
    <li class="active"><a href="${ctx}/inventory/house/">房屋信息列表</a></li>
    <shiro:hasPermission name="inventory:house:edit">
        <li><a href="${ctx}/inventory/house/form">房屋信息添加</a></li>
    </shiro:hasPermission>
</ul>
<form:form id="searchForm" modelAttribute="house"
           action="${ctx}/inventory/house/list" method="post"
           class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden"
           value="${page.pageSize}"/>
    <ul class="ul-form">
        <li>
            <label>物业项目：</label>
            <form:select path="propertyProject.id" class="input-medium" onchange="changeProject()">
                <form:option value="" label="请选择..."/>
                <form:options items="${listPropertyProject}" itemLabel="projectName" itemValue="id" htmlEscape="false"/>
            </form:select>
        </li>
        <li>
            <label>楼宇：</label>
            <form:select path="building.id" class="input-medium">
                <form:option value="" label="请选择..."/>
                <form:options items="${listBuilding}" itemLabel="buildingName" itemValue="id" htmlEscape="false"/>
            </form:select>
        </li>
        <li>
            <label>业主：</label>
            <form:select path="owner.id" class="input-medium">
                <form:option value="" label="请选择..."/>
                <form:options items="${listOwner}" itemLabel="name" itemValue="id" htmlEscape="false"/>
            </form:select>
        </li>
        <li>
            <label>房屋编码：</label>
            <form:input path="houseCode" htmlEscape="false" maxlength="100" class="input-medium"/>
        </li>
        <li>
            <label>房屋号：</label>
            <form:input path="houseNo" htmlEscape="false" maxlength="100" class="input-medium"/>
        </li>
        <li>
            <label>电户号：</label>
            <form:input path="eleAccountNum" htmlEscape="false" maxlength="100" class="input-medium"/>
        </li>
        <li>
            <label>水户号：</label>
            <form:input path="waterAccountNum" htmlEscape="false" maxlength="100" class="input-medium"/>
        </li>
        <li>
            <label>煤气户号：</label>
            <form:input path="gasAccountNum" htmlEscape="false" maxlength="100" class="input-medium"/>
        </li>
        <li>
            <label>预约热线：</label>
            <form:input path="reservationPhone" htmlEscape="false" maxlength="100" class="input-medium"/>
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
            <label>房屋状态：</label>
            <form:select path="houseStatus" class="input-medium">
                <form:option value="" label="请选择..."/>
                <form:options items="${fns:getDictList('house_status')}" itemLabel="label" itemValue="value"
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
            <label>公寓类型：</label>
            <form:select path="type" class="input-medium">
                <form:option value="" label="请选择..."/>
                <form:options items="${fns:getDictList('building_type')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
        </li>
        <li>
            <label style="width:104px;">支付宝同步状态：</label>
            <form:select path="alipayStatus" class="input-medium">
                <form:option value="" label="请选择..."/>
                <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
        </li>
        <li>
            <label style="width:117px;">支付宝上下架状态：</label>
            <form:select path="up" class="input-medium">
                <form:option value="" label="请选择..."/>
                <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
        </li>
        <shiro:hasPermission name="inventory:house:view">
            <li class="btns"> <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
            <li class="btns"> <input id="btnExport" class="btn btn-primary" type="button" value="导出"/></li>
            <li class="clearfix"> </li>
        </shiro:hasPermission>
    </ul>
</form:form>
<sys:message content="${message}" type="${messageType}"/>
<table id="contentTable"
       class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>物业项目</th>
        <th>楼宇</th>
        <th>业主</th>
        <th>房屋编码</th>
        <th>房屋号</th>
        <th>预约热线电话</th>
        <th>精选房源</th>
        <th>公寓类型</th>
        <th>房屋状态</th>
        <th>楼层</th>
        <th>原始面积</th>
        <th>装修面积</th>
        <th>原始结构</th>
        <th>装修结构</th>
        <th>产权证号</th>
        <th>电户号</th>
        <th>水户号</th>
        <th>煤气户号</th>
        <th>支付间隔月数</th>
        <th>押金月数</th>
        <th>支付宝同步状态</th>
        <th>支付宝上下架状态</th>
        <th>创建时间</th>
        <th>修改时间</th>
        <th>创建人</th>
        <th>修改人</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="house">
        <tr>
            <td>${house.propertyProject.projectName}</td>
            <td>${house.building.buildingName}</td>
            <td>${house.ownerNamesOfHouse}</td>
            <td>${house.houseCode}</td>
            <td><a href="${ctx}/inventory/house/form?id=${house.id}">${house.houseNo}</a></td>
            <td>${house.reservationPhone}</td>
            <td>${fns:getDictLabel(house.isFeature, 'yes_no', '')}</td>
            <td>${fns:getDictLabel(house.building.type, 'building_type', '')}</td>
            <td>${fns:getDictLabel(house.houseStatus, 'house_status', '')}</td>
            <td>${house.houseFloor}</td>
            <td>${house.houseSpace}</td>
            <td>${house.decorationSpance}</td>
            <td>${house.oriStrucRoomNum}房${house.oriStrucCusspacNum}厅${house.oriStrucWashroNum}卫</td>
            <td>${house.decoraStrucRoomNum}房${house.decoraStrucCusspacNum}厅${house.decoraStrucWashroNum}卫</td>
            <td>${house.certificateNo}</td>
            <td>${house.eleAccountNum}</td>
            <td>${house.waterAccountNum}</td>
            <td>${house.gasAccountNum}</td>
            <td>${house.rentMonthGap}</td>
            <td>${house.deposMonthCount}</td>
            <td>
                <c:if test="${house.alipayStatus eq '1'}">
                    已同步
                </c:if>
                <c:if test="${house.alipayStatus != '1'}">
                    未同步
                </c:if>
            </td>
            <td>
                <c:if test="${house.alipayStatus eq '1' and house.up eq '1'}">
                    已上架
                </c:if>
                <c:if test="${house.alipayStatus eq '1' and house.up eq '0'}">
                    已下架
                </c:if>
            </td>
            <td><fmt:formatDate value="${house.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td><fmt:formatDate value="${house.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td>${house.createBy.loginName}</td>
            <td>${house.updateBy.loginName}</td>
            <td>
                <shiro:hasPermission name="inventory:house:edit">
            <td><a href="${ctx}/inventory/house/form?id=${house.id}">修改</a></td>
            </shiro:hasPermission>
            <shiro:hasPermission name="inventory:house:del">
                <td><a href="${ctx}/inventory/house/delete?id=${house.id}"
                       onclick="return confirmx('确认要删除该房屋、图片及其所有房间和图片的信息吗？', this.href)">删除</a></td>
            </shiro:hasPermission>
            <shiro:hasPermission name="device:house:done">
                <c:if test="${house.houseStatus eq '0'}">
                    <td><a href="#" onclick="finishDirect('${house.id}');">装修完成</a></td>
                </c:if>
            </shiro:hasPermission>
            <shiro:hasPermission name="device:roomDevices:view">
                <td><a href="${ctx}/device/roomDevices/viewHouseDevices?houseId=${house.id}">查看设备</a></td>
            </shiro:hasPermission>

            <shiro:hasPermission name="alipay:houseAndRoom:sync">
                <c:if test="${house.building.type eq '1' and house.intentMode eq '0' and house.houseStatus eq '1'}">
                    <td><a href="${ctx}/app/alipay/syncHouse/${house.id}">分散式同步支付宝</a></td>
                </c:if>
                <c:if test="${house.building.type eq '2' and house.intentMode eq '0' and house.houseStatus eq '1'}">
                    <td><a href="${ctx}/app/alipay/syncHouse/${house.id}">集中式同步支付宝</a></td>
                </c:if>
                <c:if test="${house.alipayStatus eq '1' and house.up eq '0'}">
                    <td><a href="${ctx}/app/alipay/upHouse/${house.id}">上架</a></td>
                </c:if>
                <c:if test="${house.alipayStatus eq '1' and house.up eq '1'}">
                    <td><a href="${ctx}/app/alipay/downHouse/${house.id}">下架</a></td>
                </c:if>
            </shiro:hasPermission>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>