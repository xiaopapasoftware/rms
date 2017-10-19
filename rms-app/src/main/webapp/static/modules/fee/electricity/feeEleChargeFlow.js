layui.use(['form', 'table', 'layer', 'laydate', 'laytpl'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        laydate = layui.laydate;

    var feeEleChargeFlow = {
        init: function () {
            feeEleChargeFlowMVC.View.initDate();
            feeEleChargeFlowMVC.View.bindEvent();
            feeEleChargeFlowMVC.Controller.getAreaFun();
            feeEleChargeFlowMVC.View.renderTable();

            form.on('select(area)', function (data) {
                feeEleChargeFlowMVC.Controller.selectItemFun("project", "PROJECT", data.value);
                $("#project option").remove();
                $("#project").append('<option value="">物业项目</option>');

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');
                form.render('select');
            });
            form.on('select(project)', function (data) {
                feeEleChargeFlowMVC.Controller.selectItemFun("building", "BUILDING", data.value);

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('select(building)', function (data) {
                feeEleChargeFlowMVC.Controller.selectItemFun("house", "HOUSE", data.value);

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('submit(addEleBill)', function () {
                feeEleChargeFlowMVC.Controller.saveFun();
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
            layui.laytpl.dateFormat = function (value) {
                //json日期格式转换为正常格式
                var date = new Date(value);
                var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
                var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
                return date.getFullYear() + "-" + month + "-" + day;
            };
        }
    };

    var feeEleChargeFlowCommon = {
        baseUrl: ctx + "/fee/ele/charged/flow"
    };

    var feeEleChargeFlowMVC = {
        URLs: {
            query: {
                url: feeEleChargeFlowCommon.baseUrl + "/list",
                method: "GET"
            },
            save: {
                url: feeEleChargeFlowCommon.baseUrl + "/save",
                method: "POST"
            },
            delete: {
                url: feeEleChargeFlowCommon.baseUrl + "/delete",
                method: "GET"
            },
            selectItem: {
                url: feeEleChargeFlowCommon.baseUrl + "/getSubOrgList",
                method: "GET"
            },
            selectArea: {
                url: feeEleChargeFlowCommon.baseUrl + "/getArea",
                method: "GET"
            },
            getHouseInfo: {
                url: feeEleChargeFlowCommon.baseUrl + "/houseInfo",
                method: "GET"
            },
            getRoomInfo: {
                url: feeEleChargeFlowCommon.baseUrl + "/roomInfo",
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
                $("#btn-add").on("click", feeEleChargeFlowMVC.Controller.addEleFun);
                $("#btn-search").on("click", feeEleChargeFlowMVC.Controller.queryFun);
                $("#btn-undo").on("click", feeEleChargeFlowMVC.Controller.undoFun);
            },
            renderTable: function () {
                table.render({
                    elem: '#eleChargeFlowTable',
                    url: feeEleChargeFlowMVC.URLs.query.url,
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
                        {
                            field: 'eleCalculateDate', align: 'center', title: '计算日期', width: 120,
                            templet: '<div>{{ layui.laytpl.dateFormat(d.eleCalculateDate) }}</div>'
                        },
                        {field: 'intentModeName', align: 'center', title: '出租类型', width: 100},
                        {
                            field: 'elePeakAmount', align: 'right', title: '峰金额（元）', width: 120,
                            templet: '<div>{{ layui.laytpl.NumberFormat(d.elePeakAmount) }}</div>'
                        },
                        {
                            field: 'eleValleyAmount', align: 'right', title: '谷金额（元）', width: 120,
                            templet: '<div>{{ layui.laytpl.NumberFormat(d.eleValleyAmount) }}</div>'
                        },
                        {
                            field: 'eleAmount', align: 'right', title: '金额（元）', width: 120,
                            templet: '<div>{{ layui.laytpl.NumberFormat(d.eleAmount) }}</div>'
                        },
                        {
                            field: 'generateOrder', align: 'center', title: '是否生成账单', width: 120,
                            templet: '<div>{{ layui.laytpl.generateOrderFormat(d.generateOrder) }}</div>'
                        }
                    ]],
                    id: 'eleChargeFlowTable',
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

            },
            queryFun: function () {
                table.reload('eleChargeFlowTable', {
                    where: feeEleChargeFlowMVC.Controller.getWhereFun()
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
                $.post(feeEleChargeFlowMVC.URLs.save.url, data, function (data) {
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
            getAreaFun: function () {
                $.getJSON(feeEleChargeFlowMVC.URLs.selectArea.url, "", function (data) {
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
                $.getJSON(feeEleChargeFlowMVC.URLs.selectItem.url, {
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
    feeEleChargeFlow.init();
});






