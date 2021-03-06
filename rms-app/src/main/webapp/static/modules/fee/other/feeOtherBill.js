layui.use(['form', 'table', 'layer', 'laydate', 'laytpl'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        laydate = layui.laydate,
        laytpl = layui.laytpl;

    var feeOtherBillBillIndex;

    var feeOtherBillBill = {
        init: function () {
            feeOtherBillBillMVC.View.initDate();
            feeOtherBillBillMVC.View.bindEvent();
            feeOtherBillBillMVC.Controller.getAreaFun();
            feeOtherBillBillMVC.View.renderTable();
            feeOtherBillBillMVC.Controller.getTotalAmountFun();

            //查询条件
            form.on('select(area)', function (data) {
                feeOtherBillBillMVC.Controller.selectItemFun("project", "PROJECT", data.value);
                $("#project option").remove();
                $("#project").append('<option value="">物业项目</option>');

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');
                form.render('select');
            });
            form.on('select(project)', function (data) {
                feeOtherBillBillMVC.Controller.selectItemFun("building", "BUILDING", data.value);

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('select(building)', function (data) {
                feeOtherBillBillMVC.Controller.selectItemFun("house", "HOUSE", data.value);

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('submit(addOtherBill)', function () {
                feeOtherBillBillMVC.Controller.saveFun();
            });

            //新增修改
            form.on('select(areaId)', function (data) {
                feeOtherBillBillMVC.Controller.selectItemFun("projectId", "PROJECT", data.value);
                $("#projectId option").remove();
                $("#projectId").append('<option value="">物业项目</option>');

                $("#buildingId option").remove();
                $("#buildingId").append('<option value="">楼宇</option>');

                $("#houseId option").remove();
                $("#houseId").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('select(projectId)', function (data) {
                feeOtherBillBillMVC.Controller.selectItemFun("buildingId", "BUILDING", data.value);

                $("#buildingId option").remove();
                $("#buildingId").append('<option value="">楼宇</option>');

                $("#houseId option").remove();
                $("#houseId").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('select(buildingId)', function (data) {
                feeOtherBillBillMVC.Controller.selectItemFun("houseId", "HOUSE", data.value);

                $("#houseId option").remove();
                $("#houseId").append('<option value="">房屋</option>');

                form.render('select');
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

    var feeOtherBillBillCommon = {
        baseUrl: ctx + "/fee/other/bill"
    };

    var feeOtherBillBillMVC = {
        URLs: {
            query: {
                url: feeOtherBillBillCommon.baseUrl + "/list",
                method: "GET"
            },
            save: {
                url: feeOtherBillBillCommon.baseUrl + "/save",
                method: "POST"
            },
            delete: {
                url: feeOtherBillBillCommon.baseUrl + "/delete",
                method: "GET"
            },
            print: {
                url: feeOtherBillBillCommon.baseUrl + "/print",
                method: "GET"
            },
            audit: {
                url: feeOtherBillBillCommon.baseUrl + "/audit",
                method: "GET"
            },
            selectItem: {
                url: feeOtherBillBillCommon.baseUrl + "/getSubOrgList",
                method: "GET"
            },
            selectArea: {
                url: feeOtherBillBillCommon.baseUrl + "/getArea",
                method: "GET"
            },
            getTotalAmount: {
                url: feeOtherBillBillCommon.baseUrl + "/getTotalAmount",
                method: "GET"
            },
            getHouseInfo: {
                url: feeOtherBillBillCommon.baseUrl + "/houseInfo",
                method: "GET"
            }
        },
        View: {
            initDate: function () {
                laydate.render({
                    elem: '#feeDate',
                    type: 'date',
                    range: '~',
                    format: 'yyyy-MM-dd'
                });
                laydate.render({
                    elem: '#billDate',
                    type: 'date',
                    format: 'yyyy-MM-dd',
                    value: new Date()
                });
            },
            bindEvent: function () {
                $("#btn-add").on("click", feeOtherBillBillMVC.Controller.feeOtherBillFun);
                $("#btn-search").on("click", feeOtherBillBillMVC.Controller.queryFun);
                $("#btn-undo").on("click", feeOtherBillBillMVC.Controller.undoFun);
                $("#btn-pass").on("click", feeOtherBillBillMVC.Controller.passFun);
                $("#btn-reject").on("click", feeOtherBillBillMVC.Controller.rejectFun);
                $("#btn-commit").on("click", feeOtherBillBillMVC.Controller.commitFun);
                $("#btn-print").on("click", feeOtherBillBillMVC.Controller.printFun);
                $("#btn-view").on("click", feeOtherBillBillMVC.Controller.viewFun);
            },
            renderTable: function () {
                table.render({
                    elem: '#otherBillTable',
                    url: feeOtherBillBillMVC.URLs.query.url,
                    limits: [20, 30, 60, 90, 150, 300],
                    limit: 20,
                    cols: [[
                        {checkbox: true, width: 20, fixed: true},
                        {field: 'areaName', align: 'center', title: '区域', width: 100},
                        {field: 'projectName', align: 'center', title: '物业项目', width: 120},
                        {field: 'projectAddress', align: 'center', title: '地址', width: 180},
                        {field: 'buildingName', align: 'center', title: '楼宇', width: 80},
                        {field: 'houseNo', align: 'center', title: '房号', sort: true, width: 80},
                        {field: 'billDate', align: 'center', title: '账期日期', width: 120},
                        {field: 'batchNo', align: 'center', title: '审核批次号', width: 140},
                        {field: 'billTypeName', align: 'center', title: '账单类型', width: 120},
                        {
                            field: 'billAmount', align: 'right', title: '金额', width: 120,
                            templet: '<div>{{ layui.laytpl.amountFormat(d.billAmount) }}</div>'
                        },
                        {field: 'billStatusName', align: 'center', title: '状态', width: 100},
                        {align: 'center', title: '操作', toolbar: '#toolBar', width: 120}
                    ]],
                    id: 'otherBillTable',
                    page: true,
                    request: {
                        pageName: 'pageNum',
                        limitName: 'pageSize'
                    },
                    where: {
                        "feeDate": ($("#feeDate").val() == "" || $("#feeDate").val() == null) ? (new Date().getFullYear() + "-" + (new Date().getMonth() < 9 ? "0" + (new Date().getMonth() + 1) : new Date().getMonth() + 1)) : $("#feeDate").val()
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

                table.on('tool(otherBill)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
                    var data = obj.data; //获得当前行数据
                    var layEvent = obj.event; //获得 lay-event 对应的值
                    if (layEvent === 'edit') { //编辑
                        if (data.billStatus != null && data.billStatus != "0" && data.billStatus != "3") {
                            layer.msg("当前账单已提交,不可修改", {icon: 5, offset: 100, time: 1000, shift: 6});
                            return;
                        }
                        $("#otherBillId").val(data.id);
                        $("#billDate").val(data.billDate);
                        $("#billType").val(data.billType);
                        $("#billUnit").val(data.billUnit);
                        $("#billAmount").val(data.billAmount);
                        feeOtherBillBillMVC.Controller.selectItemFun("projectId", "PROJECT", data.areaId, function () {
                            feeOtherBillBillMVC.Controller.selectItemFun("buildingId", "BUILDING", data.propertyId, function () {
                                feeOtherBillBillMVC.Controller.selectItemFun("houseId", "HOUSE", data.buildingId, function () {
                                    $("#areaId").val(data.areaId);
                                    $("#projectId").val(data.propertyId);
                                    $("#buildingId").val(data.buildingId);
                                    $("#houseId").val(data.houseId);
                                    form.render('select');
                                });
                            });
                        });
                        form.render('select');
                        feeOtherBillBillMVC.Controller.feeOtherBillFun();
                    } else if (layEvent === 'del') { //删除
                        if (data.billStatus != null && data.billStatus != "0" && data.billStatus != "3") {
                            layer.msg("当前账单已提交,不可删除", {icon: 5, offset: 100, time: 1000, shift: 6});
                            return;
                        }
                        layer.confirm('确认删除吗?', {offset: '100px', icon: 3, title: '提示'}, function (index) {
                            $.post(feeOtherBillBillMVC.URLs.delete.url, {id: data.id}, function (data) {
                                if (data.code == "200") {
                                    feeOtherBillBillMVC.Controller.queryFun();
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
                    "startTime": $("#feeDate").val().split("~")[0],
                    "endTime": $("#feeDate").val().split("~")[1],
                    "areaId": $("#area").val(),
                    "propertyId": $("#project").val(),
                    "buildId": $("#building").val(),
                    "houseId": $("#house").val(),
                    "keyWord":$("#keyWord").val(),
                    "status": $("#status").val()
                };
                return where;
            },
            feeOtherBillFun: function () {
                feeOtherBillBillIndex = layer.open({
                    title: "账单录入",
                    type: 1,
                    resize: true,
                    offset: 'rt',
                    anim: 2,
                    area: ['350px', '380px'],
                    content: $('#addDiv') //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                });
            },
            queryFun: function () {
                table.reload('otherBillTable', {
                    where: feeOtherBillBillMVC.Controller.getWhereFun()
                });
                feeOtherBillBillMVC.Controller.getTotalAmountFun();
            },
            undoFun: function () {
                $("#keyWord").val("");
                $("#feeDate").val("");
                $("#area").val("");
                $("#project").val("");
                $("#building").val("");
                $("#house").val("");
                $("#status").val("");
                form.render("select");
            },
            passFun: function () {
                var selectRows = table.checkStatus('otherBillTable');
                if (selectRows.data.length == 0) {
                    layer.msg("请选择处理的数据", {icon: 5, offset: 100, time: 1000, shift: 6});
                    return;
                }
                feeOtherBillBillMVC.Controller.auditFun("2");
            },
            auditFun: function (status) {
                var selectRows = table.checkStatus('otherBillTable');
                var data = "status=" + status;
                $.each(selectRows.data, function (index, object) {
                    data += "&id=" + object.id;
                });
                $.post(feeOtherBillBillMVC.URLs.audit.url, data, function (data) {
                    if (data.code == "200") {
                        feeOtherBillBillMVC.Controller.queryFun();
                        layer.msg(data.msg, {icon: 1, offset: 100, time: 1000, shift: 6});
                    } else {
                        layer.msg(data.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                    }
                });
            },
            rejectFun: function () {
                var selectRows = table.checkStatus('otherBillTable');
                if (selectRows.data.length == 0) {
                    layer.msg("请选择处理的数据", {icon: 5, offset: 100, time: 1000, shift: 6});
                    return;
                }
                feeOtherBillBillMVC.Controller.auditFun("3");
            },
            commitFun: function () {
                var selectRows = table.checkStatus('otherBillTable');
                if (selectRows.data.length == 0) {
                    layer.msg("请选择处理的数据", {icon: 5, offset: 100, time: 1000, shift: 6});
                    return;
                }

                feeOtherBillBillMVC.Controller.auditFun("1");
            },
            printFun: function () {
                var where = feeOtherBillBillMVC.Controller.getWhereFun();

                $.post(feeOtherBillBillMVC.URLs.print.url, where, function (resp) {
                    if (resp.code == "200") {
                        if (resp.data.length > 0) {
                            var printHtml = "";
                            laytpl(printTableTpl.innerHTML).render(resp, function (html) {
                                printHtml = html;
                            });
                            var LODOP = getLodop();
                            LODOP.PRINT_INIT("账单打印");
                            LODOP.ADD_PRINT_TABLE(0, 0, 2000, 20000, printHtml);
                            LODOP.PREVIEW();
                        } else {
                            layer.msg("没有已录的数据,不能打印", {icon: 5, offset: 100, time: 1000, shift: 6});
                        }
                    } else {
                        layer.msg(resp.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                    }
                });
            },
            saveFun: function () {
                var data = {
                    "id": $("#otherBillId").val(),
                    "billDate": $("#billDate").val(),
                    "houseId": $("#houseId").val(),
                    "billType": $("#billType").val(),
                    "billUnit": $("#billUnit").val(),
                    "billAmount": $("#billAmount").val()
                };
                $.post(feeOtherBillBillMVC.URLs.save.url, data, function (data) {
                    if (data.code == "200") {
                        feeOtherBillBillMVC.Controller.viewFun();
                        feeOtherBillBillMVC.Controller.queryFun();
                        layer.msg('保存成功', {icon: 1, offset: 100, time: 1000, shift: 6});
                    } else {
                        layer.msg(data.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                    }
                });
            },
            viewFun: function () {
                layer.close(feeOtherBillBillIndex);
                feeOtherBillBillMVC.Controller.cleanValue();
            },
            cleanValue: function () {
                $("#billDate").val("");
                $("#billType").val("");
                $("#billUnit").val("");
                $("#billAmount").val("");
                $("#areaId").val("");
                $("#projectId").val("");
                $("#buildingId").val("");
                $("#houseId").val("");
                $("#houseSelect").show();
                form.render('select');
            },
            getAreaFun: function () {
                $.getJSON(feeOtherBillBillMVC.URLs.selectArea.url, "", function (data) {
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
                $.getJSON(feeOtherBillBillMVC.URLs.selectItem.url, {
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

                    if (callbackFun != undefined && callbackFun != null) {
                        callbackFun(data);
                    }
                });
            },
            getTotalAmountFun: function () {
                $.getJSON(feeOtherBillBillMVC.URLs.getTotalAmount.url,
                    feeOtherBillBillMVC.Controller.getWhereFun(),
                    function (data) {
                        if (data.code == "200") {
                            $("#totalAmount").html(layui.laytpl.amountFormat(data.data));
                        }
                    });
            }
        }
    };
    feeOtherBillBill.init();
});






