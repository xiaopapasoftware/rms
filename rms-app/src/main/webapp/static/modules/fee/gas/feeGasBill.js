layui.use(['form', 'table', 'layer', 'laydate', 'laytpl'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        laydate = layui.laydate,
        laytpl = layui.laytpl;

    var feeGasBillIndex;

    var feeGasBill = {
        init: function () {
            feeGasBillMVC.View.initDate();
            feeGasBillMVC.View.bindEvent();
            feeGasBillMVC.Controller.getAreaFun();
            feeGasBillMVC.View.renderTable();
            feeGasBillMVC.Controller.getTotalAmountFun();

            form.on('select(area)', function (data) {
                feeGasBillMVC.Controller.selectItemFun("project", "PROJECT", data.value);
                $("#project option").remove();
                $("#project").append('<option value="">物业项目</option>');

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');
                form.render('select');
            });
            form.on('select(project)', function (data) {
                feeGasBillMVC.Controller.selectItemFun("building", "BUILDING", data.value);

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('select(building)', function (data) {
                feeGasBillMVC.Controller.selectItemFun("house", "HOUSE", data.value);

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('submit(addGasBill)', function () {
                feeGasBillMVC.Controller.saveFun();
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

            $('#houseGasNum').autocomplete({
                serviceUrl: feeGasBillMVC.URLs.getHouseInfo.url +"?type=1",
                dataType: 'json',
                paramName: "accountNum",
                zIndex: 999999999,
                width: "350px",
                onHint: function () {
                    $("#houseId").val("");
                    $("#houseAddress").val("");
                },
                transformResult: function (response) {
                    var result = {};
                    result.suggestions = [];
                    if (response.code == "200") {
                        $.each(response.data, function (index, obj) {
                            result.suggestions.push({
                                "data": obj.gasAccountNum,
                                "value": obj.projectAddr + "(" + obj.gasAccountNum + ")",
                                "id": obj.id,
                                "address": obj.projectAddr
                            });
                        });
                    }
                    return result;
                },
                onSelect: function (data) {
                    $("#eleDegree").focus();
                    $("#houseGasNum").val(data.data);
                    $("#houseId").val(data.id);
                    $("#houseAddress").val(data.address);
                }
            });
        }
    };

    var feeGasBillCommon = {
        baseUrl: ctx + "/fee/gas/bill"
    };

    var feeGasBillMVC = {
        URLs: {
            query: {
                url: feeGasBillCommon.baseUrl + "/list",
                method: "GET"
            },
            save: {
                url: feeGasBillCommon.baseUrl + "/save",
                method: "POST"
            },
            delete: {
                url: feeGasBillCommon.baseUrl + "/delete",
                method: "GET"
            },
            print: {
                url: feeGasBillCommon.baseUrl + "/print",
                method: "GET"
            },
            audit: {
                url: feeGasBillCommon.baseUrl + "/audit",
                method: "GET"
            },
            selectItem: {
                url: feeGasBillCommon.baseUrl + "/getSubOrgList",
                method: "GET"
            },
            selectArea: {
                url: feeGasBillCommon.baseUrl + "/getArea",
                method: "GET"
            },
            getTotalAmount: {
                url: feeGasBillCommon.baseUrl + "/getTotalAmount",
                method: "GET"
            },
            getHouseInfo: {
                url: feeGasBillCommon.baseUrl + "/houseInfo",
                method: "GET"
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
                    elem: '#gasBillDate',
                    type: 'month',
                    btns: ['confirm'],
                    value: new Date()
                });
            },
            bindEvent: function () {
                $("#btn-add").on("click", feeGasBillMVC.Controller.feeGasBillFun);
                $("#btn-search").on("click", feeGasBillMVC.Controller.queryFun);
                $("#btn-undo").on("click", feeGasBillMVC.Controller.undoFun);
                $("#btn-pass").on("click", feeGasBillMVC.Controller.passFun);
                $("#btn-reject").on("click", feeGasBillMVC.Controller.rejectFun);
                $("#btn-commit").on("click", feeGasBillMVC.Controller.commitFun);
                $("#btn-print").on("click", feeGasBillMVC.Controller.printFun);
                $("#btn-view").on("click", feeGasBillMVC.Controller.viewFun);
            },
            renderTable: function () {
                table.render({
                    elem: '#gasBillTable',
                    url: feeGasBillMVC.URLs.query.url,
                    limits: [20, 30, 60, 90, 150, 300],
                    limit: 20,
                    cols: [[
                        {checkbox: true, width: 20, fixed: true},
                        {field: 'areaName', align: 'center', title: '区域', width: 100},
                        {field: 'projectName', align: 'center', title: '物业项目', width: 120},
                        {field: 'projectAddress', align: 'center', title: '地址', width: 180},
                        {field: 'buildingName', align: 'center', title: '楼宇', width: 80},
                        {field: 'houseNo', align: 'center', title: '房号', sort: true, width: 80},
                        {field: 'houseGasNum', align: 'center', title: '户号', width: 140},
                        {field: 'gasBillDate', align: 'center', title: '账期', width: 100},
                        {field: 'batchNo', align: 'center', title: '审核批次号', width: 140},
                        {
                            field: 'gasDegree', align: 'right', title: '仪表数', width: 120,
                            templet: '<div>{{ layui.laytpl.NumberFormat(d.gasDegree) }}</div>'
                        },
                        {
                            field: 'gasBillAmount', align: 'right', title: '金额', width: 120,
                            templet: '<div>{{ layui.laytpl.amountFormat(d.gasBillAmount) }}</div>'
                        },
                        {field: 'billStatusName', align: 'center', title: '状态', width: 100},
                        {align: 'center', title: '操作', toolbar: '#toolBar', width: 120}
                    ]],
                    id: 'gasBillTable',
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

                table.on('tool(gasBill)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
                    var data = obj.data; //获得当前行数据
                    var layEvent = obj.event; //获得 lay-event 对应的值
                    if (layEvent === 'edit') { //编辑
                        if(data.billStatus != null && data.billStatus != "0" && data.billStatus != "3"){
                            layer.msg("当前账单已提交,不可修改", {icon: 5, offset: 100, time: 1000, shift: 6});
                            return;
                        }
                        $("#gasBillDate").val(data.gasBillDate);
                        $("#houseGasNum").val(data.houseGasNum);
                        $("#houseAddress").val(data.projectAddress + data.buildingName + "号" + data.houseNo + "室");
                        $("#houseId").val(data.houseId);
                        $("#gasDegree").val(data.gasDegree);
                        $("#gasBillAmount").val(data.gasBillAmount);
                        feeGasBillMVC.Controller.feeGasBillFun();
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
                            $.post(feeGasBillMVC.URLs.delete.url, {id: data.id}, function (data) {
                                if (data.code == "200") {
                                    feeGasBillMVC.Controller.queryFun();
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
                    "feeDate": ($("#feeDate").val() == "" || $("#feeDate").val() == null) ? (new Date().getFullYear() + "-" + (new Date().getMonth() < 9 ? "0" + (new Date().getMonth() + 1) : new Date().getMonth() + 1)) : $("#feeDate").val(),
                    "keyWord":$("#keyWord").val(),
                    "areaId": $("#area").val(),
                    "propertyId": $("#project").val(),
                    "buildId": $("#building").val(),
                    "houseId": $("#house").val(),
                    "status": $("#status").val(),
                    "isRecord": $("#isRecord").val()
                };
                return where;
            },
            feeGasBillFun: function () {
                feeGasBillIndex = layer.open({
                    title: "燃气账单录入",
                    type: 1,
                    resize: true,
                    offset: 'rt',
                    anim: 2,
                    area: ['350px', '380px'],
                    content: $('#addDiv') //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                });
            },
            queryFun: function () {
                table.reload('gasBillTable', {
                    where: feeGasBillMVC.Controller.getWhereFun()
                });
                feeGasBillMVC.Controller.getTotalAmountFun();
            },
            undoFun: function () {
                $("#keyWord").val("");
                $("#area").val("");
                $("#project").val("");
                $("#building").val("");
                $("#house").val("");
                $("#status").val("");
                $("#isRecord").val("");
                form.render("select");
            },
            passFun: function () {
                var selectRows = table.checkStatus('gasBillTable');
                if (selectRows.data.length == 0) {
                    layer.msg("请选择处理的数据", {icon: 5, offset: 100, time: 1000, shift: 6});
                    return;
                }
                feeGasBillMVC.Controller.auditFun("2");
            },
            auditFun: function (status) {
                var selectRows = table.checkStatus('gasBillTable');
                var data = "status="+status;
                $.each(selectRows.data, function (index, object) {
                    data += "&id=" + object.id;
                });
                $.post(feeGasBillMVC.URLs.audit.url, data, function (data) {
                    if (data.code == "200") {
                        feeGasBillMVC.Controller.queryFun();
                        layer.msg(data.msg, {icon: 1, offset: 100, time: 1000, shift: 6});
                    } else {
                        layer.msg(data.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                    }
                });
            },
            rejectFun: function () {
                var selectRows = table.checkStatus('gasBillTable');
                if (selectRows.data.length == 0) {
                    layer.msg("请选择处理的数据", {icon: 5, offset: 100, time: 1000, shift: 6});
                    return;
                }
                feeGasBillMVC.Controller.auditFun("3");
            },
            commitFun: function () {
                var selectRows = table.checkStatus('gasBillTable');
                if (selectRows.data.length == 0) {
                    layer.msg("请选择处理的数据", {icon: 5, offset: 100, time: 1000, shift: 6});
                    return;
                }

                var flag = true;
                $.each(selectRows.data, function (index, obj) {
                    if (obj.gasDegree == null || obj.gasDegree == 0
                        || obj.gasBillAmount == null || obj.gasBillAmount == 0) {
                        layer.msg("户号[" + obj.houseGasNum + "]没有录入完,不能提交", {icon: 5, offset: 100, time: 5000, shift: 6});
                        flag = false;
                        return false;
                    }
                    if (obj.billStatus != "0" && obj.billStatus != "3") {
                        layer.msg("户号[" + obj.houseGasNum + "]已经提交,不能重复提交", {icon: 5, offset: 100, time: 5000, shift: 6});
                        flag = false;
                        return false;
                    }
                });
                if(flag){
                    feeGasBillMVC.Controller.auditFun("1");
                }
            },
            printFun: function () {
                var where = feeGasBillMVC.Controller.getWhereFun();
                where.isRecord="0";

                $.post(feeGasBillMVC.URLs.print.url, where, function (resp) {
                    if (resp.code == "200") {
                        if(resp.data.length > 0){
                            var printHtml="";
                            laytpl(printTableTpl.innerHTML).render(resp, function (html) {
                                printHtml = html;
                            });
                            var LODOP=getLodop();
                            LODOP.PRINT_INIT("账单打印");
                            LODOP.ADD_PRINT_TABLE(0,0,2000,20000,printHtml);
                            LODOP.PREVIEW();
                        }else{
                            layer.msg("没有已录的数据,不能打印", {icon: 5, offset: 100, time: 1000, shift: 6});
                        }
                    } else {
                        layer.msg(resp.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                    }
                });
            },
            saveFun: function () {
                var data = {
                    "gasBillDate": $("#gasBillDate").val(),
                    "houseGasNum": $("#houseGasNum").val(),
                    "houseId": $("#houseId").val(),
                    "gasDegree": $("#gasDegree").val(),
                    "gasBillAmount": $("#gasBillAmount").val()
                };
                $.post(feeGasBillMVC.URLs.save.url, data, function (data) {
                    if (data.code == "200") {
                        feeGasBillMVC.Controller.cleanValue();
                        $("#houseGasNum").focus();
                        layer.msg('保存成功', {icon: 1, offset: 100, time: 1000, shift: 6});
                    } else {
                        layer.msg(data.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                    }
                });
            },
            viewFun: function () {
                layer.close(feeGasBillIndex);
                feeGasBillMVC.Controller.queryFun();
                feeGasBillMVC.Controller.cleanValue();
            },
            cleanValue: function () {
                $("#houseGasNum").val("");
                $("#houseAddress").val("");
                $("#houseId").val("");
                $("#gasDegree").val("");
                $("#gasBillAmount").val("")
            },
            getAreaFun: function () {
                $.getJSON(feeGasBillMVC.URLs.selectArea.url, "", function (data) {
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
                $.getJSON(feeGasBillMVC.URLs.selectItem.url, {
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
            },
            getTotalAmountFun: function () {
                $.getJSON(feeGasBillMVC.URLs.getTotalAmount.url,
                    feeGasBillMVC.Controller.getWhereFun(),
                    function (data) {
                        if (data.code == "200") {
                            $("#totalAmount").html(layui.laytpl.amountFormat(data.data));
                        }
                    });
            }
        }
    };
    feeGasBill.init();
});






