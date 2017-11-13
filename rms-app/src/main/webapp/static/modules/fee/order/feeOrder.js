layui.use(['form', 'table', 'layer', 'laydate', 'laytpl'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        laydate = layui.laydate;

    var addEleReadIndex;

    var feeOrder = {
        init: function () {
            feeOrderMVC.View.bindEvent();
            feeOrderMVC.Controller.getAreaFun();
            feeOrderMVC.View.renderTable();

            form.on('select(area)', function (data) {
                feeOrderMVC.Controller.selectItemFun("project", "PROJECT", data.value);
                $("#project option").remove();
                $("#project").append('<option value="">物业项目</option>');

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');
                form.render('select');
            });
            form.on('select(project)', function (data) {
                feeOrderMVC.Controller.selectItemFun("building", "BUILDING", data.value);

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('select(building)', function (data) {
                feeOrderMVC.Controller.selectItemFun("house", "HOUSE", data.value);

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('submit(addEleBill)', function () {
                feeOrderMVC.Controller.saveFun();
            });

            form.on('select(areaId)', function (data) {
                feeOrderMVC.Controller.selectItemFun("projectId", "PROJECT", data.value);
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
                feeOrderMVC.Controller.selectItemFun("buildingId", "BUILDING", data.value);

                $("#buildingId option").remove();
                $("#buildingId").append('<option value="">楼宇</option>');

                $("#houseId option").remove();
                $("#houseId").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('select(buildingId)', function (data) {
                feeOrderMVC.Controller.selectItemFun("houseId", "HOUSE", data.value);

                $("#houseId option").remove();
                $("#houseId").append('<option value="">房屋</option>');

                $("#separateRentShowDiv").html("");
                form.render('select');
            });
            form.on('select(houseId)', function (data) {
                feeOrderMVC.Controller.renderRoom(data.value);
            });

            layui.laytpl.amountFormat = function (value) {
                if (value == null) {
                    value = 0;
                }
                return (value).toLocaleString('zh-Hans-CN', {style: 'currency', currency: 'CNY'});
            };

            layui.laytpl.orderTypeFormat = function (value) {
                switch (value){
                    case 0 :
                        return "电费";
                    case 1 :
                        return "水费";
                    case 2:
                        return "燃气费";
                    case 3:
                        return "宽带";
                    case 4:
                        return "电视";
                    case 5:
                        return "房租";
                    case 6:
                        return "房租押金";
                    case 7:
                        return "定金";
                    case 8:
                        return "违约金";
                }
            };

            layui.laytpl.orderStatusFormat = function (value) {
                switch (value){
                    case 0:
                        return "待审核";
                    case 1:
                        return "待缴费";
                    case 2:
                        return "已缴";
                    case 3:
                        return "驳回";
                }
            };

        }
    };

    var feeOrderCommon = {
        baseUrl: ctx + "/fee/order"
    };

    var feeOrderMVC = {
        URLs: {
            query: {
                url: feeOrderCommon.baseUrl + "/list",
                method: "GET"
            },
            payed: {
                url: feeOrderCommon.baseUrl + "/payed",
                method: "POST"
            },
            repay: {
                url: feeOrderCommon.baseUrl + "/repay",
                method: "POST"
            },
            selectItem: {
                url: feeOrderCommon.baseUrl + "/getSubOrgList",
                method: "GET"
            },
            selectArea: {
                url: feeOrderCommon.baseUrl + "/getArea",
                method: "GET"
            },
            getHouseInfo: {
                url: feeOrderCommon.baseUrl + "/houseInfo",
                method: "GET"
            },
            getRoomInfo: {
                url: feeOrderCommon.baseUrl + "/roomInfo",
                method: "GET"
            }
        },
        View: {
            bindEvent: function () {
                $("#btn-payed").on("click", feeOrderMVC.Controller.payedFun);
                $("#btn-repay").on("click", feeOrderMVC.Controller.repayFun);
                $("#btn-search").on("click", feeOrderMVC.Controller.queryFun);
                $("#btn-undo").on("click", feeOrderMVC.Controller.undoFun);
                $("#btn-view").on("click", feeOrderMVC.Controller.viewFun);
            },
            renderTable: function () {
                table.render({
                    elem: '#feeOrderTable',
                    url: feeOrderMVC.URLs.query.url,
                    limits: [20, 30, 60, 90, 150, 300],
                    limit: 20,
                    cols: [[
                        {checkbox: true, width: 20, fixed: true},
                        {field: 'areaName', align: 'center', title: '区域', width: 140},
                        {field: 'projectName', align: 'center', title: '物业项目', width: 140},
                        {field: 'projectAddress', align: 'center', title: '地址', width: 180},
                        {field: 'buildingName', align: 'center', title: '楼宇', width: 80},
                        {field: 'houseNo', align: 'center', title: '房号', sort: true, width: 80},
                        {field: 'roomNo', align: 'center', title: '房屋', width: 80},
                        {
                            field: 'orderType', align: 'center', title: '费用类型', width: 100,
                            templet: '<div>{{ layui.laytpl.orderTypeFormat(d.orderType) }}</div>'
                        },
                        {field: 'orderDate', align: 'center', title: '订单日期', width: 120},
                        {
                            field: 'amount', align: 'right', title: '金额', width: 120,
                            templet: '<div>{{ layui.laytpl.amountFormat(d.amount) }}</div>'
                        },
                        {
                            field: 'orderStatus', align: 'right', title: '订单状态', width: 120,
                            templet: '<div>{{ layui.laytpl.orderStatusFormat(d.orderStatus) }}</div>'
                        }
                    ]],
                    id: 'feeOrderTable',
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
                    "areaId": $("#area").val(),
                    "propertyId": $("#project").val(),
                    "buildId": $("#building").val(),
                    "houseId": $("#house").val(),
                    "type": $("#orderType").val(),
                    "status": $("#orderStatus").val()
                };
                return where;
            },
            queryFun: function () {
                table.reload('feeOrderTable', {
                    where: feeOrderMVC.Controller.getWhereFun()
                });
            },
            undoFun: function () {
                $("#area").val("");
                $("#project").val("");
                $("#building").val("");
                $("#house").val("");
                $("#orderType").val("");
                $("#orderStatus").val("");
                form.render("select");
            },
            payedFun: function () {
                var selectRows = table.checkStatus('feeOrderTable');
                if (selectRows.data.length == 0) {
                    layer.msg("请选择处理的数据", {icon: 5, offset: 100, time: 1000, shift: 6});
                    return;
                }

                var data = "";
                $.each(selectRows.data, function (index, object) {
                    data += "&id=" + object.id;
                });
                $.post(feeOrderMVC.URLs.payed.url, data, function (data) {
                    if (data.code == "200") {
                        feeOrderMVC.Controller.queryFun();
                        layer.msg('保存成功', {icon: 1, offset: 100, time: 1000, shift: 6});
                    } else {
                        layer.msg(data.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                    }
                });
            },
            repayFun: function () {
                var selectRows = table.checkStatus('feeOrderTable');
                if (selectRows.data.length == 0) {
                    layer.msg("请选择处理的数据", {icon: 5, offset: 100, time: 1000, shift: 6});
                    return;
                }

                var data = "";
                $.each(selectRows.data, function (index, object) {
                    data += "&id=" + object.id;
                });
                $.post(feeOrderMVC.URLs.repay.url, data, function (data) {
                    feeOrderMVC.Controller.queryFun();
                    if (data.code == "200") {
                        layer.msg('保存成功', {icon: 1, offset: 100, time: 1000, shift: 6});
                    } else {
                        layer.msg(data.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                    }
                });
            },
            viewFun: function () {
                layer.close(addEleReadIndex);
                feeOrderMVC.Controller.queryFun();
                feeOrderMVC.Controller.cleanValue();
            },
            getAreaFun: function () {
                $.getJSON(feeOrderMVC.URLs.selectArea.url, "", function (data) {

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
                $.getJSON(feeOrderMVC.URLs.selectItem.url, {
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
    feeOrder.init();
});






