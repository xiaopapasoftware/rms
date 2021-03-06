layui.use(['form', 'table', 'layer', 'laydate', 'laytpl'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        laydate = layui.laydate;

    var addEleReadIndex;
    var operType;

    var feeEleReadFlow = {
        init: function () {
            feeEleReadFlowMVC.View.initDate();
            feeEleReadFlowMVC.View.bindEvent();
            feeEleReadFlowMVC.Controller.getAreaFun();
            feeEleReadFlowMVC.View.renderTable();

            form.on('select(area)', function (data) {
                feeEleReadFlowMVC.Controller.selectItemFun("project", "PROJECT", data.value);
                $("#project option").remove();
                $("#project").append('<option value="">物业项目</option>');

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');
                form.render('select');
            });
            form.on('select(project)', function (data) {
                feeEleReadFlowMVC.Controller.selectItemFun("building", "BUILDING", data.value);

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('select(building)', function (data) {
                feeEleReadFlowMVC.Controller.selectItemFun("house", "HOUSE", data.value);

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('submit(addEleBill)', function () {
                feeEleReadFlowMVC.Controller.saveFun();
            });

            form.on('select(areaId)', function (data) {
                feeEleReadFlowMVC.Controller.selectItemFun("projectId", "PROJECT", data.value);
                $("#projectId option").remove();
                $("#projectId").append('<option value="">物业项目</option>');

                $("#buildingId option").remove();
                $("#buildingId").append('<option value="">楼宇</option>');

                $("#houseId option").remove();
                $("#houseId").append('<option value="">房屋</option>');

                $("#separateRentShowDiv").html("");
                form.render('select');
            });
            form.on('select(projectId)', function (data) {
                feeEleReadFlowMVC.Controller.selectItemFun("buildingId", "BUILDING", data.value);

                $("#buildingId option").remove();
                $("#buildingId").append('<option value="">楼宇</option>');

                $("#houseId option").remove();
                $("#houseId").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('select(buildingId)', function (data) {
                feeEleReadFlowMVC.Controller.selectItemFun("houseId", "HOUSE", data.value);

                $("#houseId option").remove();
                $("#houseId").append('<option value="">房屋</option>');

                $("#separateRentShowDiv").html("");
                form.render('select');
            });
            form.on('select(houseId)', function (data) {
                feeEleReadFlowMVC.Controller.renderRoom(data.value);
            });

            layui.laytpl.NumberFormat = function (value) {
                if (value == null) {
                    value = 0;
                }
                return (value).toLocaleString();
            };
            layui.laytpl.roomNoFormat = function (value) {
                if (value == null) {
                    value = "";
                } else if (value == 0) {
                    value = "总表";
                }
                return value;
            };
        }
    };

    var feeEleReadFlowCommon = {
        baseUrl: ctx + "/fee/ele/read/flow"
    };

    var feeEleReadFlowMVC = {
        URLs: {
            query: {
                url: feeEleReadFlowCommon.baseUrl + "/list",
                method: "GET"
            },
            save: {
                url: feeEleReadFlowCommon.baseUrl + "/save",
                method: "POST"
            },
            delete: {
                url: feeEleReadFlowCommon.baseUrl + "/delete",
                method: "GET"
            },
            selectItem: {
                url: feeEleReadFlowCommon.baseUrl + "/getSubOrgList",
                method: "GET"
            },
            selectArea: {
                url: feeEleReadFlowCommon.baseUrl + "/getArea",
                method: "GET"
            },
            getHouseInfo: {
                url: feeEleReadFlowCommon.baseUrl + "/houseInfo",
                method: "GET"
            },
            getRoomInfo: {
                url: feeEleReadFlowCommon.baseUrl + "/roomInfo",
                method: "GET"
            }
        },
        View: {
            initDate: function () {
                laydate.render({
                    elem: '#eleReadDates',
                    type: 'date',
                    range: '~',
                    format: 'yyyy-MM-dd'
                });
            },
            bindEvent: function () {
                $("#btn-add").on("click", feeEleReadFlowMVC.Controller.addEleFun);
                $("#btn-search").on("click", feeEleReadFlowMVC.Controller.queryFun);
                $("#btn-undo").on("click", feeEleReadFlowMVC.Controller.undoFun);
                $("#btn-view").on("click", feeEleReadFlowMVC.Controller.viewFun);
            },
            renderTable: function () {
                table.render({
                    elem: '#eleReadFlowTable',
                    url: feeEleReadFlowMVC.URLs.query.url,
                    limits: [20, 30, 60, 90, 150, 300],
                    limit: 20,
                    cols: [[
                        {field: 'areaName', align: 'center', title: '区域', width: 100},
                        {field: 'projectName', align: 'center', title: '物业项目', width: 120},
                        {field: 'projectAddress', align: 'center', title: '地址', width: 180},
                        {field: 'buildingName', align: 'center', title: '楼宇', width: 80},
                        {field: 'houseNo', align: 'center', title: '房号', sort: true, width: 80},
                        {
                            field: 'roomNo', align: 'center', title: '房号', width: 80,
                            templet: '<div>{{ layui.laytpl.roomNoFormat(d.roomNo) }}</div>'
                        },
                        {field: 'intentModeName', align: 'center', title: '出租类型', width: 100},
                        {field: 'eleReadDate', align: 'center', title: '抄表日期', width: 120},
                        {
                            field: 'eleDegree', align: 'right', title: '抄表数', width: 120,
                            templet: '<div>{{ layui.laytpl.NumberFormat(d.eleDegree) }}</div>'
                        },
                        {
                            field: 'elePeakDegree', align: 'right', title: '谷值', width: 120,
                            templet: '<div>{{ layui.laytpl.NumberFormat(d.elePeakDegree) }}</div>'
                        },
                        {
                            field: 'eleValleyDegree', align: 'right', title: '峰值', width: 120,
                            templet: '<div>{{ layui.laytpl.NumberFormat(d.eleValleyDegree) }}</div>'
                        },
                        {align: 'center', title: '操作', toolbar: '#toolBar', width: 120}
                    ]],
                    id: 'eleReadFlowTable',
                    page: true,
                    request: {
                        pageName: 'pageNum',
                        limitName: 'pageSize'
                    }
                });

                table.on('tool(eleReadFlow)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
                    var data = obj.data; //获得当前行数据
                    var layEvent = obj.event; //获得 lay-event 对应的值
                    if (layEvent === 'edit') { //编辑
                        console.log(data);
                        if (data.fromSource != null && data.fromSource == "1") {
                            layer.msg("当前记录为账单录入生成,不可修改", {icon: 5, offset: 100, time: 1000, shift: 6});
                            return;
                        }
                        feeEleReadFlowMVC.Controller.selectItemFun("projectId", "PROJECT", data.areaId, function () {
                            feeEleReadFlowMVC.Controller.selectItemFun("buildingId", "BUILDING", data.propertyId, function () {
                                feeEleReadFlowMVC.Controller.selectItemFun("houseId", "HOUSE", data.buildingId, function () {
                                    $("#areaId").val(data.areaId);
                                    $("#projectId").val(data.propertyId);
                                    $("#buildingId").val(data.buildingId);
                                    $("#houseId").val(data.houseId);
                                    $("#areaId").prop('disabled', true);
                                    $("#projectId").prop('disabled', true);
                                    $("#buildingId").prop('disabled', true);
                                    $("#houseId").prop('disabled', true);
                                    form.render('select');
                                });
                            });
                        });

                        $("#elePeakDegree").val(data.elePeakDegree);
                        $("#eleValleyDegree").val(data.eleValleyDegree);
                        $("#eleDegree").val(data.eleDegree);


                        $("#roomId").val(data.roomId);
                        $("#roomNo").html(layui.laytpl.roomNoFormat(data.roomNo));
                        $("#eleReadDate").val(data.eleReadDate);
                        $("#eleReadDate").prop('readonly', true);

                        if (data.intentMode == "0") {
                            $("#editShowDiv").hide();
                            $("#separateRentShowDiv").hide();
                            $("#wholeRentShowDiv").show()
                        } else {
                            $("#editShowDiv").show();
                            $("#separateRentShowDiv").hide();
                            $("#wholeRentShowDiv").hide()
                        }

                        operType = "edit";
                        addEleReadIndex = layer.open({
                            title: "电费账单录入",
                            type: 1,
                            resize: true,
                            offset: 'rt',
                            anim: 2,
                            area: ['370px', '450px'],
                            content: $('#addDiv') //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                        });
                    } else if (layEvent === 'del') { //删除
                        if (data.fromSource != null && data.fromSource == "1") {
                            layer.msg("当前记录为账单录入生成,不可删除", {icon: 5, offset: 100, time: 1000, shift: 6});
                            return;
                        }
                        layer.confirm('确认删除吗?', {offset: '100px', icon: 3, title: '提示'}, function (index) {
                            $.post(feeEleReadFlowMVC.URLs.delete.url, {id: data.id}, function (data) {
                                if (data.code == "200") {
                                    feeEleReadFlowMVC.Controller.queryFun();
                                    layer.msg('删除成功', {icon: 1, offset: 100, time: 1000, shift: 6});
                                } else {
                                    layer.msg(data.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                                }
                            });
                            layer.close(index);
                        });
                    }
                });
            }
        },
        Controller: {
            getWhereFun: function () {
                var where = {
                    "startTime": $("#eleReadDates").val().split("~")[0],
                    "endTime": $("#eleReadDates").val().split("~")[1],
                    "areaId": $("#area").val(),
                    "propertyId": $("#project").val(),
                    "buildId": $("#building").val(),
                    "houseId": $("#house").val()
                };
                return where;
            },
            addEleFun: function () {
                $("#areaId").prop('disabled', false);
                $("#projectId").prop('disabled', false);
                $("#buildingId").prop('disabled', false);
                $("#houseId").prop('disabled', false);
                $("#eleReadDate").prop('readonly', false);
                form.render('select');
                laydate.render({
                    elem: '#eleReadDate',
                    type: 'date',
                    format: 'yyyy-MM-dd',
                    value: new Date()
                });

                addEleReadIndex = layer.open({
                    title: "电费账单录入",
                    type: 1,
                    resize: true,
                    offset: 'rt',
                    anim: 2,
                    area: ['370px', '450px'],
                    content: $('#addDiv') //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                });
            },
            queryFun: function () {
                table.reload('eleReadFlowTable', {
                    where: feeEleReadFlowMVC.Controller.getWhereFun()
                });
            },
            undoFun: function () {
                $("#area").val("");
                $("#project").val("");
                $("#building").val("");
                $("#house").val("");
                $("#eleReadDates").val("");
                form.render("select");
            },
            saveFun: function () {
                var data = "eleReadDate=" + $("#eleReadDate").val();
                if ($("#separateRentShowDiv").is(":visible")) {
                    data += "&houseId=" + $("#houseId").val();
                    $.each($("#addDiv input[name='roomIds']"), function (index, obj) {
                        data += "&roomIds=" + obj.value;
                    });
                    $.each($("#addDiv input[name='eleDegrees']"), function (index, obj) {
                        data += "&eleDegrees=" + obj.value;
                    });
                }
                if ($("#wholeRentShowDiv").is(":visible")) {
                    data += "&houseId=" + $("#houseId").val();
                    data += "&elePeakDegree=" + $("#elePeakDegree").val();
                    data += "&eleValleyDegree=" + $("#eleValleyDegree").val();
                }
                if ($("#editShowDiv").is(":visible")) {
                    data += "&houseId=" + $("#houseId").val();
                    data += "&roomId=" + $("#roomId").val();
                    data += "&eleDegree=" + $("#eleDegree").val();
                }
                $.post(feeEleReadFlowMVC.URLs.save.url, data, function (data) {
                    if (data.code == "200") {
                        $("#separateRentShowDiv").html("");
                        if (operType == "edit") {
                            layer.close(addEleReadIndex);
                        } else {
                            $("#houseId").val("");
                            $("#elePeakDegree").val("");
                            $("#eleValleyDegree").val("");
                            $("#eleDegree").val("");
                            form.render('select');
                        }
                        layer.msg('保存成功', {icon: 1, offset: 100, time: 1000, shift: 6});
                    } else {
                        layer.msg(data.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                    }
                });
            },
            viewFun: function () {
                layer.close(addEleReadIndex);
                feeEleReadFlowMVC.Controller.queryFun();
                feeEleReadFlowMVC.Controller.cleanValue();
            },
            cleanValue: function () {
                $("#eleReadDate").val("");
                $("#areaId").val("");
                $("#projectId").val("");
                $("#buildingId").val("");
                $("#houseId").val("");
                $("#elePeakDegree").val("");
                $("#eleValleyDegree").val("");
                $("#eleDegree").val("");
                $("#roomId").val("");
                $("#roomNo").html("");
                form.render('select');
            },
            renderRoom: function (value) {
                $.post(feeEleReadFlowMVC.URLs.getRoomInfo.url, {"houseId": value}, function (data) {
                    if (data.code == "200") {
                        if (data.data.length == 0) {
                            $("#editShowDiv").hide();
                            $("#separateRentShowDiv").hide();
                            $("#wholeRentShowDiv").show()
                        } else {
                            $("#separateRentShowDiv").html("");
                            var separateRentHtml = "";
                            $.each(data.data, function (index, obj) {
                                separateRentHtml += '<div class="layui-form-item">';
                                separateRentHtml += '<label class="layui-form-label">';
                                separateRentHtml += obj.roomNo + '</label>';
                                separateRentHtml += '<input name="roomIds" value="' + obj.id;
                                separateRentHtml += '" hidden /> <div class="layui-input-inline">';
                                separateRentHtml += '<input type="number" name="eleDegrees" required lay-verify="required"';
                                separateRentHtml += ' placeholder="' + obj.roomNo + '电表度数" class="layui-input">';
                                separateRentHtml += '</div></div>';
                            });

                            separateRentHtml += '<div class="layui-form-item">';
                            separateRentHtml += '<label class="layui-form-label">总表</label>';
                            separateRentHtml += '<input name="roomIds" value="0" hidden /> <div class="layui-input-inline">';
                            separateRentHtml += '<input type="number" name="eleDegrees" required lay-verify="required"';
                            separateRentHtml += ' placeholder="总表电表度数" class="layui-input">';
                            separateRentHtml += '</div></div>';

                            $("#separateRentShowDiv").html(separateRentHtml);
                            $("#editShowDiv").hide();
                            $("#separateRentShowDiv").show();
                            $("#wholeRentShowDiv").hide()
                        }
                    } else {
                        layer.msg(data.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                    }
                });
            },
            getAreaFun: function () {
                $.getJSON(feeEleReadFlowMVC.URLs.selectArea.url, "", function (data) {

                    $.each(data.data, function (index, object) {
                        $('#area').append($('<option>', {
                            value: object.id,
                            text: object.name
                        }));
                    });

                    $.each(data.data, function (index, object) {
                        $('#areaId').append($('<option>', {
                            value: object.id,
                            text: object.name
                        }));
                    });
                    form.render('select');
                });
            },
            selectItemFun: function (id, type, value, callbackFun) {
                $.getJSON(feeEleReadFlowMVC.URLs.selectItem.url, {
                    "business": "ORG",
                    "type": type,
                    "id": value
                }, function (data) {
                    var obj = $("#" + id);
                    if (data.data == null) {
                        return;
                    }
                    $(obj).find('option').remove();
                    obj.append($('<option>', {
                        value: "",
                        text: "请选择"
                    }));
                    $.each(data.data, function (index, object) {
                        obj.append($('<option>', {
                            value: object.id,
                            text: object.name
                        }));
                    });

                    form.render('select');

                    if (callbackFun != undefined && callbackFun != null) {
                        callbackFun(data);
                    }
                });
            }
        }
    };
    feeEleReadFlow.init();
});






