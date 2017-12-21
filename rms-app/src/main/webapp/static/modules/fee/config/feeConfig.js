layui.use(['form', 'table', 'layer', 'laydate', 'laytpl'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        laydate = layui.laydate;

    var addFeeConfigIndex;

    var feeConfig = {
        init: function () {
            feeConfigMVC.View.bindEvent();
            feeConfigMVC.View.renderTable();

            form.on('select(area)', function (data) {
                $("#areaId").val(data.value);
                feeConfigMVC.Controller.selectItemFun("project", "PROJECT", data.value);
                $("#project option").remove();
                $("#project").append('<option value="">物业项目</option>');

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                $("#room option").remove();
                $("#room").append('<option value="">房间</option>');
                form.render('select');
            });

            form.on('select(project)', function (data) {
                feeConfigMVC.Controller.selectItemFun("building", "BUILDING", data.value);

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                $("#room option").remove();
                $("#room").append('<option value="">房间</option>');

                form.render('select');
            });

            form.on('select(building)', function (data) {
                feeConfigMVC.Controller.selectItemFun("house", "HOUSE", data.value);

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                $("#room option").remove();
                $("#room").append('<option value="">房间</option>');

                form.render('select');
            });

            form.on('select(house)', function (data) {
                feeConfigMVC.Controller.selectItemFun("room", "ROOM", data.value);

                $("#room option").remove();
                $("#room").append('<option value="">房间</option>');

                form.render('select');
            });

            form.on('submit(addFeeConfig)', function () {
                feeConfigMVC.Controller.saveFun();
            });

            form.on('radio(selChargeMethod)', function (data) {
                $("#chargeMethod").val(data.value);
            });

            form.on('radio(selRentMethod)', function (data) {
                $("#rentMethod").val(data.value);
            });

            form.on('select(configType)', function (data) {
                $("#project option").remove();
                $("#project").append('<option value="">物业项目</option>');

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                $("#room option").remove();
                $("#room").append('<option value="">房间</option>');
                form.render('select');

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
                if (value > 6) {
                    value = 6;
                }
                feeConfigMVC.Controller.getAreaFun(value);
            });

            layui.laytpl.feeTypeFormat = function (value) {
                if (value == 0) {
                    value = "电费单价";
                } else if (value == 1) {
                    value = "电费谷单价";
                } else if (value == 2) {
                    value = "电费峰单价";
                } else if (value == 3) {
                    value = "宽带单价";
                } else if (value == 4) {
                    value = "有线电视费单价";
                } else if (value == 5) {
                    value = "水费单价";
                } else if (value == 6) {
                    value = "燃气费单价";
                }
                return value;
            };

            layui.laytpl.configTypeFormat = function (value) {
                if (value == 0) {
                    value = "默认";
                } else if (value == 1) {
                    value = "公司";
                } else if (value == 2) {
                    value = "省份";
                } else if (value == 3) {
                    value = "地市";
                } else if (value == 4) {
                    value = "区县";
                } else if (value == 5) {
                    value = "服务中心";
                } else if (value == 6) {
                    value = "运营区域";
                } else if (value == 7) {
                    value = "小区";
                } else if (value == 8) {
                    value = "楼宇";
                } else if (value == 9) {
                    value = "房屋";
                } else if (value == 10) {
                    value = "房间";
                }
                return value;
            };

            layui.laytpl.chargeMethodFormat = function (value) {
                if (value == 0) {
                    value = "固定模式";
                } else if (value == 1) {
                    value = "账单模式";
                }
                return value;
            };

            layui.laytpl.configStatusFormat = function (value) {
                if (value == 0) {
                    value = "启用";
                } else if (value == 1) {
                    value = "停用";
                }
                return value;
            };
        }
    };

    var feeConfigCommon = {
        baseUrl: ctx + "/fee/config"
    };

    var feeConfigMVC = {
        URLs: {
            query: {
                url: feeConfigCommon.baseUrl + "/list",
                method: "GET"
            },
            save: {
                url: feeConfigCommon.baseUrl + "/save",
                method: "POST"
            },
            delete: {
                url: feeConfigCommon.baseUrl + "/delete",
                method: "GET"
            },
            selectItem: {
                url: feeConfigCommon.baseUrl + "/getSubOrgList",
                method: "GET"
            },
            selectArea: {
                url: feeConfigCommon.baseUrl + "/getArea",
                method: "GET"
            },
            changeConfigStatus: {
                url: feeConfigCommon.baseUrl + "/changeConfigStatus",
                method: "POST"
            }
        },
        View: {
            initDate: function () {
                laydate.render({
                    elem: '#feeDate',
                    type: 'month',
                    btns: ['confirm'],
                    value: new Date()
                });
                laydate.render({
                    elem: '#eleBillDate',
                    type: 'month',
                    btns: ['confirm'],
                    value: new Date()
                });
            },
            bindEvent: function () {
                $("#btn-add").on("click", feeConfigMVC.Controller.addFeeConfigFun);
                $("#btn-search").on("click", feeConfigMVC.Controller.queryFun);
                $("#btn-undo").on("click", feeConfigMVC.Controller.undoFun);
                $("#btn-cancel").on("click", feeConfigMVC.Controller.cancelFun);
            },
            renderTable: function () {
                table.render({
                    elem: '#feeConfigTable',
                    url: feeConfigMVC.URLs.query.url,
                    limits: [20, 30, 60, 90, 150, 300],
                    limit: 20,
                    cols: [[
                        {checkbox: true, width: 20, fixed: true},
                        {
                            field: 'feeType', align: 'center', title: '费用类型', width: 140,
                            templet: '<div>{{ layui.laytpl.feeTypeFormat(d.feeType) }}</div>'
                        },
                        {
                            field: 'configType', align: 'center', title: '费用范围', width: 140,
                            templet: '<div>{{ layui.laytpl.configTypeFormat(d.configType) }}</div>'
                        },
                        {field: 'configTypeName', align: 'center', title: '范围名称', width: 180},
                        {
                            field: 'chargeMethod', align: 'center', title: '收取方式', width: 100,
                            templet: '<div>{{ layui.laytpl.chargeMethodFormat(d.chargeMethod) }}</div>'
                        },
                        {field: 'configValue', align: 'center', title: '配置值', width: 100},
                        {
                            field: 'configStatus', align: 'center', title: '是否启用', width: 100,
                            templet: '<div>{{ layui.laytpl.configStatusFormat(d.configStatus) }}</div>'
                        },
                        {align: 'center', title: '操作', toolbar: '#toolBar', width: 120}
                    ]],
                    id: 'feeConfigTable',
                    page: true,
                    request: {
                        pageName: 'pageNum',
                        limitName: 'pageSize'
                    },
                    done: function (res, curr, count) {
                        //如果是异步请求数据方式，res即为你接口返回的信息。
                        console.log(res);
                        //得到当前页码
                        console.log(curr);
                        //得到数据总量
                        console.log(count);
                    }
                });

                table.on('tool(feeConfig)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
                    var data = obj.data; //获得当前行数据
                    var layEvent = obj.event; //获得 lay-event 对应的值
                    if (layEvent === 'stop') { //编辑
                        $.post(feeConfigMVC.URLs.changeConfigStatus.url, {
                            id: data.id,
                            configStatus: "1"
                        }, function (data) {
                            if (data.code == "200") {
                                feeConfigMVC.Controller.queryFun();
                                layer.msg('操作成功', {icon: 1, offset: 100, time: 1000, shift: 6});
                            } else {
                                layer.msg('操作失败', {icon: 5, offset: 100, time: 1000, shift: 6});
                            }
                        });
                    } else if (layEvent === 'start') { //编辑
                        $.post(feeConfigMVC.URLs.changeConfigStatus.url, {
                            id: data.id,
                            configStatus: "0"
                        }, function (data) {
                            if (data.code == "200") {
                                feeConfigMVC.Controller.queryFun();
                                layer.msg('操作成功', {icon: 1, offset: 100, time: 1000, shift: 6});
                            } else {
                                layer.msg('操作失败', {icon: 5, offset: 100, time: 1000, shift: 6});
                            }
                        });
                    } else if (layEvent === 'del') { //删除
                        layer.confirm('确认删除吗?', {offset: '100px', icon: 3, title: '提示'}, function (index) {
                            $.post(feeConfigMVC.URLs.delete.url, {id: data.id}, function (data) {
                                if (data.code == "200") {
                                    feeConfigMVC.Controller.queryFun();
                                    layer.msg('删除成功', {icon: 1, offset: 100, time: 1000, shift: 6});
                                } else {
                                    layer.msg('删除失败', {icon: 5, offset: 100, time: 1000, shift: 6});
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
                    "feeType": $("#selFeeType").val(),
                    "configType": $("#selConfigType").val(),
                    "chargeMethod": $("#selChargeMethod").val()
                };
                return where;
            },
            addFeeConfigFun: function () {
                addFeeConfigIndex = layer.open({
                    title: "费用配置录入",
                    type: 1,
                    resize: true,
                    offset: 'rt',
                    anim: 2,
                    area: ['350px', '520px'],
                    content: $('#addDiv') //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                });
            },
            queryFun: function () {
                table.reload('feeConfigTable', {
                    where: feeConfigMVC.Controller.getWhereFun()
                });
            },
            undoFun: function () {
                $("#selFeeType").val("");
                $("#selConfigType").val("");
                form.render("select");
            },
            saveFun: function () {
                var configType = $("#configType").val();
                var businessId = 0;
                if (configType == 0) {
                    businessId = 0;
                } else if (configType < 7) {
                    businessId = $("#area").val();
                } else if (configType == 7) {
                    businessId = $("#project").val();
                } else if (configType == 8) {
                    businessId = $("#building").val();
                } else if (configType == 9) {
                    businessId = $("#house").val();
                } else if (configType == 10) {
                    businessId = $("#room").val();
                }

                var data = {
                    "id": $("#id").val(),
                    "feeType": $("#feeType").val(),
                    "configType": configType,
                    "chargeMethod": $("#chargeMethod").val(),
                    "rentMethod": $("#rentMethod").val(),
                    "configValue": $("#configValue").val(),
                    "businessId": businessId
                };
                $.post(feeConfigMVC.URLs.save.url, data, function (data) {
                    if (data.code == "200") {
                        layer.close(addFeeConfigIndex);
                        feeConfigMVC.Controller.queryFun();
                        layer.msg("保存成功", {icon: 1, offset: 100, time: 1000, shift: 6});
                    } else {
                        layer.msg(data.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                    }
                });
            },
            cancelFun: function () {
                layer.close(addFeeConfigIndex);
            },
            cleanValue: function () {
                $("#feeType").val("");
                $("#configType").val("");
                $("#configValue").val("");
                form.render("select");
            },
            getAreaFun: function (type) {
                $.getJSON(feeConfigMVC.URLs.selectArea.url, {"type": type}, function (data) {
                    $("#area option").remove();
                    $('#area').append($('<option>', {
                        value: "",
                        text: "请选择"
                    }));
                    $.each(data.data, function (index, object) {
                        $('#area').append($('<option>', {
                            value: object.id,
                            text: object.name
                        }));
                    });
                    form.render('select');
                });
            },
            selectItemFun: function (id, type, value) {
                $.getJSON(feeConfigMVC.URLs.selectItem.url, {
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
    feeConfig.init();
});






