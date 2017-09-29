layui.use(['form', 'table', 'layer', 'laydate', 'laytpl'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        laydate = layui.laydate;

    var addEleBillIndex;

    var feeEleReadFlow = {
        init: function () {
            feeEleReadFlowMVC.View.initDate();
            feeEleReadFlowMVC.View.bindEvent();
            feeEleReadFlowMVC.Controller.getAreaFun();
            feeEleReadFlowMVC.View.renderTable();

            form.on('select(area)', function (data) {
                feeEleReadFlowMVC.Controller.selectItemFun("project", "project", data.value);
                $("#project option").remove();
                $("#project").append('<option value="">物业项目</option>');

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');
                form.render('select');
            });
            form.on('select(project)', function (data) {
                feeEleReadFlowMVC.Controller.selectItemFun("building", "building", data.value);

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('select(building)', function (data) {
                feeEleReadFlowMVC.Controller.selectItemFun("house", "house", data.value);

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('submit(addEleBill)', function () {
                feeEleReadFlowMVC.Controller.saveFun();
            });

            layui.laytpl.NumberFormat = function (value) {
                if (value == null) {
                    value = 0;
                }
                return (value).toLocaleString();
            };

            layui.laytpl.amountFormat = function (value) {
                if (value == null) {
                    value = 0;
                }
                return (value).toLocaleString('zh-Hans-CN', {style: 'currency', currency: 'CNY'});
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
            }
        },
        View: {
            initDate: function () {
                laydate.render({
                    elem: '#eleReadDate',
                    type: 'date',
                    range: '~',
                    format:'yyyy-MM-dd'
                });
            },
            bindEvent: function () {
                $("#btn-add").on("click", feeEleReadFlowMVC.Controller.addEleFun);
                $("#btn-search").on("click", feeEleReadFlowMVC.Controller.queryFun);
                $("#btn-undo").on("click", feeEleReadFlowMVC.Controller.undoFun);
            },
            renderTable: function () {
                table.render({
                    elem: '#eleReadFlowTable',
                    url: feeEleReadFlowMVC.URLs.query.url,
                    limits: [20, 30, 60, 90, 150, 300],
                    limit: 20,
                    cols: [[
                        {field: 'areaName', align: 'center', title: '区域', width: 140},
                        {field: 'projectName', align: 'center', title: '物业项目', width: 140},
                        {field: 'projectAddress', align: 'center', title: '地址', width: 180},
                        {field: 'buildingName', align: 'center', title: '楼宇', width: 80},
                        {field: 'houseNo', align: 'center', title: '房号', sort: true, width: 80},
                        {field: 'roomNo', align: 'center', title: '房号', width: 80},
                        {field: 'intentModeName', align: 'center', title: '出租类型', width: 100},
                        {field: 'eleReadDate', align: 'center', title: '抄表日期',sort: true, width: 100},
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
                    },
                    where: {
                        "feeDate": ""
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

                table.on('tool(eleReadFlow)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
                    var data = obj.data; //获得当前行数据
                    var layEvent = obj.event; //获得 lay-event 对应的值
                    if (layEvent === 'edit') { //编辑
                        if(data.billStatus != null && data.billStatus != "0" && data.billStatus != "3"){
                            layer.msg("当前账单已提交,不可修改", {icon: 5, offset: 100, time: 1000, shift: 6});
                            return;
                        }
                        $("#houseEleNum").val(data.houseEleNum);
                        $("#houseAddress").val(data.projectAddress + data.buildingName + "号" + data.houseNo + "室");
                        $("#houseId").val(data.houseId);
                        $("#elePeakDegree").val(data.elePeakDegree);
                        $("#eleValleyDegree").val(data.eleValleyDegree);
                        $("#eleBillAmount").val(data.eleBillAmount)
                        feeEleReadFlowMVC.Controller.addEleFun();
                    } else if (layEvent === 'del') { //删除
                        if (data.id == null) {
                            layer.msg("当前账单没有录入,不可删除", {icon: 5, offset: 100, time: 1000, shift: 6});
                            return;
                        }
                        if(data.billStatus != null && data.billStatus != "0" &&data.billStatus != "3"){
                            layer.msg("当前账单已提交,不可删除", {icon: 5, offset: 100, time: 1000, shift: 6});
                            return;
                        }
                        layer.confirm('确认删除吗?', {offset: '100px', icon: 3, title: '提示'}, function (index) {
                            $.post(feeEleReadFlowMVC.URLs.delete.url, {id: data.id}, function (data) {
                                if (data.code == "200") {
                                    feeEleReadFlowMVC.Controller.queryFun();
                                } else {
                                    layer.msg('删除成功', {icon: 5, offset: 100, time: 1000, shift: 6});
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
                    "startTime": $("#eleReadDate").val().split("~")[0],
                    "endTime": $("#eleReadDate").val().split("~")[1],
                    "areaId": $("#area").val(),
                    "propertyId": $("#project").val(),
                    "buildId": $("#building").val(),
                    "houseId": $("#house").val()
                };
                return where;
            },
            addEleFun: function () {
                addEleBillIndex = layer.open({
                    title: "电费账单录入",
                    type: 1,
                    resize: true,
                    offset: 'rt',
                    anim: 2,
                    area: ['350px', '380px'],
                    content: $('#addDiv') //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                });
                $("#houseEleNum").focus();
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
                $("#eleReadDate").val("");
                form.render("select");
            },
            saveFun: function () {
                var data = {
                    "eleBillDate": $("#eleBillDate").val(),
                    "houseEleNum": $("#houseEleNum").val(),
                    "houseId": $("#houseId").val(),
                    "elePeakDegree": $("#elePeakDegree").val(),
                    "eleValleyDegree": $("#eleValleyDegree").val(),
                    "eleBillAmount": $("#eleBillAmount").val()
                };
                $.post(feeEleReadFlowMVC.URLs.save.url, data, function (data) {
                    if (data.code == "200") {
                        feeEleReadFlowMVC.Controller.cleanValue();
                        $("#houseEleNum").focus();
                    } else {
                        layer.msg(data.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                    }
                });
            },
            viewFun: function () {
                layer.close(addEleBillIndex);
                feeEleReadFlowMVC.Controller.queryFun();
                feeEleReadFlowMVC.Controller.cleanValue();
            },
            cleanValue: function () {
                $("#houseEleNum").val("");
                $("#houseAddress").val("");
                $("#houseId").val("");
                $("#elePeakDegree").val("");
                $("#eleValleyDegree").val("");
                $("#eleBillAmount").val("")
            },
            getAreaFun: function () {
                $.getJSON(feeEleReadFlowMVC.URLs.selectArea.url, "", function (data) {
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
                $.getJSON(feeEleReadFlowMVC.URLs.selectItem.url, {
                    "business": "org",
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
    feeEleReadFlow.init();
});






