layui.use(['form', 'table', 'layer', 'laydate', 'laytpl'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        laydate = layui.laydate;


    var feeGasScopeIndex;

    var generateType;

    var feeGasChargeFlow = {
        init: function () {
            feeGasChargeFlowMVC.View.initDate();
            feeGasChargeFlowMVC.View.bindEvent();
            feeGasChargeFlowMVC.Controller.getAreaFun();
            feeGasChargeFlowMVC.View.renderTable();

            form.on('select(area)', function (data) {
                feeGasChargeFlowMVC.Controller.selectItemFun("project", "PROJECT", data.value);
                $("#project option").remove();
                $("#project").append('<option value="">物业项目</option>');

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');
                form.render('select');
            });
            form.on('select(project)', function (data) {
                feeGasChargeFlowMVC.Controller.selectItemFun("building", "BUILDING", data.value);

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('select(building)', function (data) {
                feeGasChargeFlowMVC.Controller.selectItemFun("house", "HOUSE", data.value);

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('submit(addGasBill)', function () {
                feeGasChargeFlowMVC.Controller.saveFun();
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
                feeGasChargeFlowMVC.Controller.selectItemFun("projectId", "PROJECT", data.value);
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
                feeGasChargeFlowMVC.Controller.selectItemFun("buildingId", "BUILDING", data.value);

                $("#buildingId option").remove();
                $("#buildingId").append('<option value="">楼宇</option>');

                $("#houseId option").remove();
                $("#houseId").append('<option value="">房屋</option>');

                $("#roomId option").remove();
                $("#roomId").append('<option value="">房间</option>');

                form.render('select');
            });
            form.on('select(buildingId)', function (data) {
                feeGasChargeFlowMVC.Controller.selectItemFun("houseId", "HOUSE", data.value);

                $("#houseId option").remove();
                $("#houseId").append('<option value="">房屋</option>');

                $("#roomId option").remove();
                $("#roomId").append('<option value="">房间</option>');

                form.render('select');
            });
            form.on('select(houseId)', function (data) {
                feeGasChargeFlowMVC.Controller.selectItemFun("roomId", "ROOM", data.value);

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
        }
    };

    var feeGasChargeFlowCommon = {
        baseUrl: ctx + "/fee/gas/charged/flow"
    };

    var feeGasChargeFlowMVC = {
        URLs: {
            query: {
                url: feeGasChargeFlowCommon.baseUrl + "/list",
                method: "GET"
            },
            generateFlow: {
                url: feeGasChargeFlowCommon.baseUrl + "/generateFlow",
                method: "POST"
            },
            generateOrder: {
                url: feeGasChargeFlowCommon.baseUrl + "/generateOrder",
                method: "GET"
            },
            selectItem: {
                url: feeGasChargeFlowCommon.baseUrl + "/getSubOrgList",
                method: "GET"
            },
            selectArea: {
                url: feeGasChargeFlowCommon.baseUrl + "/getArea",
                method: "GET"
            },
            getHouseInfo: {
                url: feeGasChargeFlowCommon.baseUrl + "/houseInfo",
                method: "GET"
            },
            getRoomInfo: {
                url: feeGasChargeFlowCommon.baseUrl + "/roomInfo",
                method: "GET"
            }
        },
        View: {
            initDate: function () {
                laydate.render({
                    elem: '#gasReadDates',
                    type: 'date',
                    range: '~',
                    format: 'yyyy-MM-dd'
                });
            },
            bindEvent: function () {
                $("#btn-generateFlow").on("click", function () {
                    generateType = "flow";
                    feeGasChargeFlowMVC.Controller.showScopeWinFun();
                });
                $("#btn-generateOrder").on("click", function () {
                    generateType = "order";
                    feeGasChargeFlowMVC.Controller.showScopeWinFun();
                });
                $("#btn-cancel").on("click", feeGasChargeFlowMVC.Controller.scopeWinCloseFun);
                $("#btn-generate").on("click", feeGasChargeFlowMVC.Controller.generateFun);

                $("#btn-search").on("click", feeGasChargeFlowMVC.Controller.queryFun);
                $("#btn-undo").on("click", feeGasChargeFlowMVC.Controller.undoFun);
            },
            renderTable: function () {
                table.render({
                    elem: '#gasChargeFlowTable',
                    url: feeGasChargeFlowMVC.URLs.query.url,
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
                        {field: 'gasCalculateDate', align: 'center', title: '计算日期', width: 120},
                        {field: 'intentModeName', align: 'center', title: '出租类型', width: 100},
                        {
                            field: 'gasAmount', align: 'right', title: '金额（元）', width: 120,
                            templet: '<div>{{ layui.laytpl.amountFormat(d.gasAmount) }}</div>'
                        },
                        {
                            field: 'generateOrder', align: 'center', title: '是否生成账单', width: 120,
                            templet: '<div>{{ layui.laytpl.generateOrderFormat(d.generateOrder) }}</div>'
                        }
                    ]],
                    id: 'gasChargeFlowTable',
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
                    //"startTime": $("#gasReadDates").val().split("~")[0],
                    //"endTime": $("#gasReadDates").val().split("~")[1],
                    "areaId": $("#area").val(),
                    "propertyId": $("#project").val(),
                    "buildId": $("#building").val(),
                    "houseId": $("#house").val()
                };
                return where;
            },
            showScopeWinFun: function () {
                feeGasScopeIndex = layer.open({
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
                layer.close(feeGasScopeIndex);
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
                    url = feeGasChargeFlowMVC.URLs.generateFlow.url;
                } else if (generateType == "order") {
                    url = feeGasChargeFlowMVC.URLs.generateOrder.url;
                } else {
                    return;
                }
                $.getJSON(url, {
                    "scope": scope,
                    "businessId": businessId
                }, function (data) {
                    if (data.code == "200") {
                        feeGasChargeFlowMVC.Controller.queryFun();
                        layer.close(feeGasScopeIndex);
                        layer.msg(data.msg, {icon: 1, offset: 100, time: 1000, shift: 6});
                    } else {
                        layer.msg(data.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                    }
                });
            },
            queryFun: function () {
                table.reload('gasChargeFlowTable', {
                    where: feeGasChargeFlowMVC.Controller.getWhereFun()
                });
            },
            undoFun: function () {
                $("#area").val("");
                $("#project").val("");
                $("#building").val("");
                $("#house").val("");
                $("#gasReadDates").val("");
                form.render("select");
            },
            getAreaFun: function () {
                $.getJSON(feeGasChargeFlowMVC.URLs.selectArea.url, "", function (data) {
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
                $.getJSON(feeGasChargeFlowMVC.URLs.selectItem.url, {
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
    feeGasChargeFlow.init();
});






