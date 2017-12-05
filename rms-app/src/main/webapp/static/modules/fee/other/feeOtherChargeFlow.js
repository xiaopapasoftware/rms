layui.use(['form', 'table', 'layer', 'laydate', 'laytpl'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        laydate = layui.laydate;

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
                } else if (value == 1) {
                    value = "否";
                } else if (value == 0) {
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
                }else if (value == 9) {
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
                $("#btn-generateFlow").on("click", feeOtherChargeFlowMVC.Controller.generateFlowFun);
                $("#btn-generateOrder").on("click", feeOtherChargeFlowMVC.Controller.generateOrderFun);
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
                        {field: 'areaName', align: 'center', title: '区域', width: 140},
                        {field: 'projectName', align: 'center', title: '物业项目', width: 140},
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
            generateFlowFun: function () {
                $.getJSON(feeOtherChargeFlowMVC.URLs.generateFlow.url, "", function (data) {
                    if (data.code == "200") {
                        layer.msg(data.msg, {icon: 1, offset: 100, time: 1000, shift: 6});
                    } else {
                        layer.msg(data.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                    }
                });
            },
            generateOrderFun: function () {
                $.getJSON(feeOtherChargeFlowMVC.URLs.generateOrder.url, "", function (data) {
                    if (data.code == "200") {
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






