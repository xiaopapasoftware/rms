layui.use(['form', 'table', 'layer', 'laydate', 'laytpl'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        laydate = layui.laydate;

    var feeOtherScopeIndex;

    var generateType;

    var feeOtherChargeFlow = {
        init: function () {
            feeOtherChargeFlowMVC.View.initDate();
            feeOtherChargeFlowMVC.View.bindEvent();
            feeOtherChargeFlowMVC.Controller.getAreaFun();
            feeOtherChargeFlowMVC.View.renderTable();

            form.on('select(area)', function (data) {
                feeOtherChargeFlowMVC.Controller.selectItemFun("project", "PROJECT", data.value);
                $("#project option").remove();
                $("#project").append('<option value="">物业项目</option>');

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');
                form.render('select');
            });
            form.on('select(project)', function (data) {
                feeOtherChargeFlowMVC.Controller.selectItemFun("building", "BUILDING", data.value);

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('select(building)', function (data) {
                feeOtherChargeFlowMVC.Controller.selectItemFun("house", "HOUSE", data.value);

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('submit(addOtherBill)', function () {
                feeOtherChargeFlowMVC.Controller.saveFun();
            });

            form.on('select(scope)', function (data) {
                var value = data.value;
                if (value > 0) {
                    $("#businessDiv").show();
                    if (value == 7) {
                        $("#projectDiv").show();
                        $("#buildingDiv").hide();
                        $("#houseDiv").hide();
                        $("#roomDiv").hide();
                    } else if (value == 8) {
                        $("#projectDiv").show();
                        $("#buildingDiv").show();
                        $("#houseDiv").hide();
                        $("#roomDiv").hide();
                    } else if (value == 9) {
                        $("#projectDiv").show();
                        $("#buildingDiv").show();
                        $("#houseDiv").show();
                        $("#roomDiv").hide();
                    } else if (value == 10) {
                        $("#projectDiv").show();
                        $("#buildingDiv").show();
                        $("#houseDiv").show();
                        $("#roomDiv").show();
                    } else {
                        $("#projectDiv").hide();
                        $("#buildingDiv").hide();
                        $("#houseDiv").hide();
                        $("#roomDiv").hide();
                    }
                } else {
                    $("#businessDiv").hide();
                }
            });
            form.on('select(areaId)', function (data) {
                $("#areaId").val(data.value);
                feeOtherChargeFlowMVC.Controller.selectItemFun("projectId", "PROJECT", data.value);
                $("#projectId option").remove();
                $("#projectId").append('<option value="">物业项目</option>');

                $("#buildingId option").remove();
                $("#buildingId").append('<option value="">楼宇</option>');

                $("#houseId option").remove();
                $("#houseId").append('<option value="">房屋</option>');

                $("#roomId option").remove();
                $("#roomId").append('<option value="">房间</option>');
                form.render('select');
            });
            form.on('select(projectId)', function (data) {
                feeOtherChargeFlowMVC.Controller.selectItemFun("buildingId", "BUILDING", data.value);

                $("#buildingId option").remove();
                $("#buildingId").append('<option value="">楼宇</option>');

                $("#houseId option").remove();
                $("#houseId").append('<option value="">房屋</option>');

                $("#roomId option").remove();
                $("#roomId").append('<option value="">房间</option>');

                form.render('select');
            });
            form.on('select(buildingId)', function (data) {
                feeOtherChargeFlowMVC.Controller.selectItemFun("houseId", "HOUSE", data.value);

                $("#houseId option").remove();
                $("#houseId").append('<option value="">房屋</option>');

                $("#roomId option").remove();
                $("#roomId").append('<option value="">房间</option>');

                form.render('select');
            });
            form.on('select(houseId)', function (data) {
                feeOtherChargeFlowMVC.Controller.selectItemFun("roomId", "ROOM", data.value);

                $("#roomId option").remove();
                $("#roomId").append('<option value="">房间</option>');

                form.render('select');
            });

            layui.laytpl.amountFormat = function (value) {
                if (value == null) {
                    value = 0;
                }
                return (value).toLocaleString('zh-Hans-CN', {style: 'currency', currency: 'CNY'});
            };
            layui.laytpl.roomNoFormat = function (value) {
                if (value == null) {
                    value = "";
                } else if (value == 0) {
                    value = "总表";
                }
                return value;
            };
            layui.laytpl.generateOrderFormat = function (value) {
                if (value == null) {
                    value = "";
                } else if (value == 0) {
                    value = "否";
                } else if (value == 1) {
                    value = "是";
                }
                return value;
            };
            layui.laytpl.typeFormat = function (value) {
                if (value == null) {
                    value = "";
                } else if (value == 3) {
                    value = "宽带";
                } else if (value == 4) {
                    value = "电视";
                } else if (value == 9) {
                    value = "其他";
                }
                return value;
            };
        }
    };

    var feeOtherChargeFlowCommon = {
        baseUrl: ctx + "/fee/other/charged/flow"
    };

    var feeOtherChargeFlowMVC = {
        URLs: {
            query: {
                url: feeOtherChargeFlowCommon.baseUrl + "/list",
                method: "GET"
            },
            generateFlow: {
                url: feeOtherChargeFlowCommon.baseUrl + "/generateFlow",
                method: "POST"
            },
            generateOrder: {
                url: feeOtherChargeFlowCommon.baseUrl + "/generateOrder",
                method: "GET"
            },
            selectItem: {
                url: feeOtherChargeFlowCommon.baseUrl + "/getSubOrgList",
                method: "GET"
            },
            selectArea: {
                url: feeOtherChargeFlowCommon.baseUrl + "/getArea",
                method: "GET"
            },
            getHouseInfo: {
                url: feeOtherChargeFlowCommon.baseUrl + "/houseInfo",
                method: "GET"
            },
            getRoomInfo: {
                url: feeOtherChargeFlowCommon.baseUrl + "/roomInfo",
                method: "GET"
            }
        },
        View: {
            initDate: function () {
                laydate.render({
                    elem: '#otherReadDates',
                    type: 'date',
                    range: '~',
                    format: 'yyyy-MM-dd'
                });
            },
            bindEvent: function () {
                $("#btn-generateFlow").on("click", function () {
                    generateType = "flow";
                    feeOtherChargeFlowMVC.Controller.showScopeWinFun();
                });
                $("#btn-generateOrder").on("click", function () {
                    generateType = "order";
                    feeOtherChargeFlowMVC.Controller.showScopeWinFun();
                });
                $("#btn-cancel").on("click", feeOtherChargeFlowMVC.Controller.scopeWinCloseFun);
                $("#btn-generate").on("click", feeOtherChargeFlowMVC.Controller.generateFun);

                $("#btn-search").on("click", feeOtherChargeFlowMVC.Controller.queryFun);
                $("#btn-undo").on("click", feeOtherChargeFlowMVC.Controller.undoFun);
            },
            renderTable: function () {
                table.render({
                    elem: '#otherChargeFlowTable',
                    url: feeOtherChargeFlowMVC.URLs.query.url,
                    limits: [20, 30, 60, 90, 150, 300],
                    limit: 20,
                    cols: [[
                        {field: 'areaName', align: 'center', title: '区域', width: 100},
                        {field: 'projectName', align: 'center', title: '物业项目', width: 120},
                        {field: 'buildingName', align: 'center', title: '楼宇', width: 80},
                        {field: 'houseNo', align: 'center', title: '房号', width: 80},
                        {
                            field: 'roomNo', align: 'center', title: '房号', width: 80,
                            templet: '<div>{{ layui.laytpl.roomNoFormat(d.roomNo) }}</div>'
                        },
                        {field: 'orderNo', align: 'center', title: '订单号', width: 200},
                        {field: 'calculateDate', align: 'center', title: '计算日期', width: 120},
                        {field: 'intentModeName', align: 'center', title: '出租类型', width: 100},
                        {
                            field: 'type', align: 'center', title: '费用类型', width: 100,
                            templet: '<div>{{ layui.laytpl.typeFormat(d.type) }}</div>'
                        },
                        {
                            field: 'amount', align: 'right', title: '金额（元）', width: 120,
                            templet: '<div>{{ layui.laytpl.amountFormat(d.amount) }}</div>'
                        },
                        {
                            field: 'generateOrder', align: 'center', title: '是否生成账单', width: 120,
                            templet: '<div>{{ layui.laytpl.generateOrderFormat(d.generateOrder) }}</div>'
                        }
                    ]],
                    id: 'otherChargeFlowTable',
                    page: true,
                    request: {
                        pageName: 'pageNum',
                        limitName: 'pageSize'
                    }
                });
            }
        },
        Controller: {
            getWhereFun: function () {
                var where = {
                    "startTime": $("#otherReadDates").val().split("~")[0],
                    "endTime": $("#otherReadDates").val().split("~")[1],
                    "areaId": $("#area").val(),
                    "propertyId": $("#project").val(),
                    "buildId": $("#building").val(),
                    "houseId": $("#house").val()
                };
                return where;
            },
            showScopeWinFun: function () {
                feeOtherScopeIndex = layer.open({
                    title: "范围选择",
                    type: 1,
                    resize: true,
                    offset: '100',
                    anim: 2,
                    area: ['350px', '380px'],
                    content: $('#generateDiv') //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                });
            },
            scopeWinCloseFun: function () {
                layer.close(feeOtherScopeIndex);
            },
            generateFun: function () {
                var scope = $("#scope").val();
                var businessId = 0;
                if (scope == 0) {
                    businessId = 0;
                } else if (scope < 7) {
                    businessId = $("#areaId").val();
                } else if (scope == 7) {
                    businessId = $("#projectId").val();
                } else if (scope == 8) {
                    businessId = $("#buildingId").val();
                } else if (scope == 9) {
                    businessId = $("#houseId").val();
                } else if (scope == 10) {
                    businessId = $("#roomId").val();
                }
                var url = "";
                if (generateType == "flow") {
                    url = feeOtherChargeFlowMVC.URLs.generateFlow.url;
                } else if (generateType == "flow") {
                    url = feeOtherChargeFlowMVC.URLs.generateFlow.url;
                } else {
                    return;
                }
                $.getJSON(url, {
                    "scope": scope,
                    "businessId": businessId
                }, function (data) {
                    if (data.code == "200") {
                        feeOtherChargeFlowMVC.Controller.queryFun();
                        layer.close(feeOtherScopeIndex);
                        layer.msg(data.msg, {icon: 1, offset: 100, time: 1000, shift: 6});
                    } else {
                        layer.msg(data.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                    }
                });
            },
            queryFun: function () {
                table.reload('otherChargeFlowTable', {
                    where: feeOtherChargeFlowMVC.Controller.getWhereFun()
                });
            },
            undoFun: function () {
                $("#area").val("");
                $("#project").val("");
                $("#building").val("");
                $("#house").val("");
                $("#OtherReadDates").val("");
                form.render("select");
            },
            getAreaFun: function () {
                $.getJSON(feeOtherChargeFlowMVC.URLs.selectArea.url, "", function (data) {
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
            selectItemFun: function (id, type, value) {
                $.getJSON(feeOtherChargeFlowMVC.URLs.selectItem.url, {
                    "business": "ORG",
                    "type": type,
                    "id": value
                }, function (data) {
                    var obj = $("#" + id);
                    if (data.data == null) {
                        return;
                    }
                    $.each(data.data, function (index, object) {
                        obj.append($('<option>', {
                            value: object.id,
                            text: object.name
                        }));
                    });
                    form.render('select');
                });
            }
        }
    };
    feeOtherChargeFlow.init();
});






