layui.use(['form', 'table', 'layer', 'laydate', 'laytpl'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        laydate = layui.laydate;

    var feeWaterChargeFlow = {
        init: function () {
            feeWaterChargeFlowMVC.View.initDate();
            feeWaterChargeFlowMVC.View.bindEvent();
            feeWaterChargeFlowMVC.Controller.getAreaFun();
            feeWaterChargeFlowMVC.View.renderTable();

            form.on('select(area)', function (data) {
                feeWaterChargeFlowMVC.Controller.selectItemFun("project", "PROJECT", data.value);
                $("#project option").remove();
                $("#project").append('<option value="">物业项目</option>');

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');
                form.render('select');
            });
            form.on('select(project)', function (data) {
                feeWaterChargeFlowMVC.Controller.selectItemFun("building", "BUILDING", data.value);

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('select(building)', function (data) {
                feeWaterChargeFlowMVC.Controller.selectItemFun("house", "HOUSE", data.value);

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('submit(addWaterBill)', function () {
                feeWaterChargeFlowMVC.Controller.saveFun();
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
                }else if (value == 0) {
                    value = "总表";
                }
                return value;
            };
            layui.laytpl.generateOrderFormat = function (value) {
                if (value == null) {
                    value = "";
                }else if (value == 1) {
                    value = "否";
                }else if (value == 0) {
                    value = "是";
                }
                return value;
            };
        }
    };

    var feeWaterChargeFlowCommon = {
        baseUrl: ctx + "/fee/water/charged/flow"
    };

    var feeWaterChargeFlowMVC = {
        URLs: {
            query: {
                url: feeWaterChargeFlowCommon.baseUrl + "/list",
                method: "GET"
            },
            generateFlow: {
                url: feeWaterChargeFlowCommon.baseUrl + "/generateFlow",
                method: "POST"
            },
            generateOrder: {
                url: feeWaterChargeFlowCommon.baseUrl + "/generateOrder",
                method: "GET"
            },
            selectItem: {
                url: feeWaterChargeFlowCommon.baseUrl + "/getSubOrgList",
                method: "GET"
            },
            selectArea: {
                url: feeWaterChargeFlowCommon.baseUrl + "/getArea",
                method: "GET"
            },
            getHouseInfo: {
                url: feeWaterChargeFlowCommon.baseUrl + "/houseInfo",
                method: "GET"
            },
            getRoomInfo: {
                url: feeWaterChargeFlowCommon.baseUrl + "/roomInfo",
                method: "GET"
            }
        },
        View: {
            initDate: function () {
                laydate.render({
                    elem: '#waterReadDates',
                    type: 'date',
                    range: '~',
                    format: 'yyyy-MM-dd'
                });
            },
            bindEvent: function () {
                $("#btn-generateFlow").on("click", feeWaterChargeFlowMVC.Controller.generateFlowFun);
                $("#btn-generateOrder").on("click", feeWaterChargeFlowMVC.Controller.generateOrderFun);
                $("#btn-search").on("click", feeWaterChargeFlowMVC.Controller.queryFun);
                $("#btn-undo").on("click", feeWaterChargeFlowMVC.Controller.undoFun);
            },
            renderTable: function () {
                table.render({
                    elem: '#waterChargeFlowTable',
                    url: feeWaterChargeFlowMVC.URLs.query.url,
                    limits: [20, 30, 60, 90, 150, 300],
                    limit: 20,
                    cols: [[
                        {field: 'areaName', align: 'center', title: '区域', width: 140},
                        {field: 'projectName', align: 'center', title: '物业项目', width: 140},
                        {field: 'buildingName', align: 'center', title: '楼宇', width: 80},
                        {field: 'houseNo', align: 'center', title: '房号', width: 80},
                        {
                            field: 'roomNo', align: 'center', title: '房号', width: 80,
                            templet: '<div>{{ layui.laytpl.roomNoFormat(d.roomNo) }}</div>'
                        },
                        {field: 'orderNo', align: 'center', title: '订单号', width: 120},
                        {field: 'waterCalculateDate', align: 'center', title: '计算日期', width: 120},
                        {field: 'intentModeName', align: 'center', title: '出租类型', width: 100},
                        {
                            field: 'waterAmount', align: 'right', title: '金额（元）', width: 120,
                            templet: '<div>{{ layui.laytpl.NumberFormat(d.waterAmount) }}</div>'
                        },
                        {
                            field: 'generateOrder', align: 'center', title: '是否生成账单', width: 120,
                            templet: '<div>{{ layui.laytpl.generateOrderFormat(d.generateOrder) }}</div>'
                        }
                    ]],
                    id: 'waterChargeFlowTable',
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
                    "startTime": $("#waterReadDates").val().split("~")[0],
                    "endTime": $("#waterReadDates").val().split("~")[1],
                    "areaId": $("#area").val(),
                    "propertyId": $("#project").val(),
                    "buildId": $("#building").val(),
                    "houseId": $("#house").val()
                };
                return where;
            },
            generateFlowFun: function () {
                $.getJSON(feeWaterChargeFlowMVC.URLs.generateFlow.url, "", function (data) {
                    if (data.code == "200") {
                        layer.msg(data.msg, {icon: 1, offset: 100, time: 1000, shift: 6});
                    } else {
                        layer.msg(data.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                    }
                });
            },
            generateOrderFun: function () {
                $.getJSON(feeWaterChargeFlowMVC.URLs.generateOrder.url, "", function (data) {
                    if (data.code == "200") {
                        layer.msg(data.msg, {icon: 1, offset: 100, time: 1000, shift: 6});
                    } else {
                        layer.msg(data.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                    }
                });
            },
            queryFun: function () {
                table.reload('waterChargeFlowTable', {
                    where: feeWaterChargeFlowMVC.Controller.getWhereFun()
                });
            },
            undoFun: function () {
                $("#area").val("");
                $("#project").val("");
                $("#building").val("");
                $("#house").val("");
                $("#waterReadDates").val("");
                form.render("select");
            },
            getAreaFun: function () {
                $.getJSON(feeWaterChargeFlowMVC.URLs.selectArea.url, "", function (data) {
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
                $.getJSON(feeWaterChargeFlowMVC.URLs.selectItem.url, {
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
    feeWaterChargeFlow.init();
});






