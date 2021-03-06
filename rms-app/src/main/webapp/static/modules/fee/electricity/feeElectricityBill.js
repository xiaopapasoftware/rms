layui.use(['form', 'table', 'layer', 'laydate', 'laytpl'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        laydate = layui.laydate,
        laytpl = layui.laytpl;

    var addEleBillIndex;

    var feeEleBill = {
        init: function () {
            feeEleBillMVC.View.initDate();
            feeEleBillMVC.View.bindEvent();
            feeEleBillMVC.Controller.getAreaFun();
            feeEleBillMVC.View.renderTable();
            feeEleBillMVC.Controller.getTotalAmountFun();

            form.on('select(area)', function (data) {
                feeEleBillMVC.Controller.selectItemFun("project", "PROJECT", data.value);
                $("#project option").remove();
                $("#project").append('<option value="">物业项目</option>');

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');
                form.render('select');
            });
            form.on('select(project)', function (data) {
                feeEleBillMVC.Controller.selectItemFun("building", "BUILDING", data.value);

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('select(building)', function (data) {
                feeEleBillMVC.Controller.selectItemFun("house", "HOUSE", data.value);

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('submit(addEleBill)', function () {
                feeEleBillMVC.Controller.saveFun();
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

            $('#houseEleNum').autocomplete({
                serviceUrl: feeEleBillMVC.URLs.getHouseInfo.url +"?type=0",
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
                                "data": obj.eleAccountNum,
                                "value": obj.projectAddr + "(" + obj.eleAccountNum + ")",
                                "id": obj.id,
                                "address": obj.projectAddr
                            });
                        });
                    }
                    return result;
                },
                onSelect: function (data) {
                    $("#elePeakDegree").focus();
                    $("#houseEleNum").val(data.data);
                    $("#houseId").val(data.id);
                    $("#houseAddress").val(data.address);
                }
            });
        }
    };

    var feeEleBillCommon = {
        baseUrl: ctx + "/fee/electricity/bill"
    };

    var feeEleBillMVC = {
        URLs: {
            query: {
                url: feeEleBillCommon.baseUrl + "/list",
                method: "GET"
            },
            save: {
                url: feeEleBillCommon.baseUrl + "/save",
                method: "POST"
            },
            delete: {
                url: feeEleBillCommon.baseUrl + "/delete",
                method: "GET"
            },
            print: {
                url: feeEleBillCommon.baseUrl + "/print",
                method: "GET"
            },
            audit: {
                url: feeEleBillCommon.baseUrl + "/audit",
                method: "GET"
            },
            selectItem: {
                url: feeEleBillCommon.baseUrl + "/getSubOrgList",
                method: "GET"
            },
            selectArea: {
                url: feeEleBillCommon.baseUrl + "/getArea",
                method: "GET"
            },
            getTotalAmount: {
                url: feeEleBillCommon.baseUrl + "/getTotalAmount",
                method: "GET"
            },
            getHouseInfo: {
                url: feeEleBillCommon.baseUrl + "/houseInfo",
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
                    elem: '#eleBillDate',
                    type: 'month',
                    btns: ['confirm'],
                    value: new Date()
                });
            },
            bindEvent: function () {
                $("#btn-add").on("click", feeEleBillMVC.Controller.addEleFun);
                $("#btn-search").on("click", feeEleBillMVC.Controller.queryFun);
                $("#btn-undo").on("click", feeEleBillMVC.Controller.undoFun);
                $("#btn-pass").on("click", feeEleBillMVC.Controller.passFun);
                $("#btn-reject").on("click", feeEleBillMVC.Controller.rejectFun);
                $("#btn-commit").on("click", feeEleBillMVC.Controller.commitFun);
                $("#btn-print").on("click", feeEleBillMVC.Controller.printFun);
                //$("#btn-save").on("click", feeEleBillMVC.Controller.saveFun);
                $("#btn-view").on("click", feeEleBillMVC.Controller.viewFun);
            },
            renderTable: function () {
                table.render({
                    elem: '#electricityBillTable',
                    url: feeEleBillMVC.URLs.query.url,
                    limits: [20, 30, 60, 90, 150, 300],
                    limit: 20,
                    cols: [[
                        {checkbox: true, width: 20, fixed: true},
                        {field: 'areaName', align: 'center', title: '区域', width: 100},
                        {field: 'projectName', align: 'center', title: '物业项目', width: 120},
                        {field: 'projectAddress', align: 'center', title: '地址', width: 180},
                        {field: 'buildingName', align: 'center', title: '楼宇', width: 80},
                        {field: 'houseNo', align: 'center', title: '房号', sort: true, width: 80},
                        {field: 'houseEleNum', align: 'center', title: '户号', width: 140},
                        {field: 'eleBillDate', align: 'center', title: '账期', width: 100},
                        {field: 'batchNo', align: 'center', title: '审核批次号', width: 120},
                        {
                            field: 'elePeakDegree', align: 'right', title: '谷值', width: 120,
                            templet: '<div>{{ layui.laytpl.NumberFormat(d.elePeakDegree) }}</div>'
                        },
                        {
                            field: 'eleValleyDegree', align: 'right', title: '峰值', width: 120,
                            templet: '<div>{{ layui.laytpl.NumberFormat(d.eleValleyDegree) }}</div>'
                        },
                        {
                            field: 'eleBillAmount', align: 'right', title: '金额', width: 120,
                            templet: '<div>{{ layui.laytpl.amountFormat(d.eleBillAmount) }}</div>'
                        },
                        {field: 'billStatusName', align: 'center', title: '状态', width: 100},
                        {align: 'center', title: '操作', toolbar: '#toolBar', width: 120}
                    ]],
                    id: 'electricityBillTable',
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

                table.on('tool(electricityBill)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
                    var data = obj.data; //获得当前行数据
                    var layEvent = obj.event; //获得 lay-event 对应的值
                    if (layEvent === 'edit') { //编辑
                        if(data.billStatus != null && data.billStatus != "0" && data.billStatus != "3"){
                            layer.msg("当前账单已提交,不可修改", {icon: 5, offset: 100, time: 1000, shift: 6});
                            return;
                        }
                        $("#eleBillDate").val(data.eleBillDate);
                        $("#houseEleNum").val(data.houseEleNum);
                        $("#houseAddress").val(data.projectAddress + data.buildingName + "号" + data.houseNo + "室");
                        $("#houseId").val(data.houseId);
                        $("#elePeakDegree").val(data.elePeakDegree);
                        $("#eleValleyDegree").val(data.eleValleyDegree);
                        $("#eleBillAmount").val(data.eleBillAmount)
                        feeEleBillMVC.Controller.addEleFun();
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
                            $.post(feeEleBillMVC.URLs.delete.url, {id: data.id}, function (data) {
                                if (data.code == "200") {
                                    feeEleBillMVC.Controller.queryFun();
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
                // $("#houseEleNum").focus();
            },
            queryFun: function () {
                table.reload('electricityBillTable', {
                    where: feeEleBillMVC.Controller.getWhereFun()
                });
                feeEleBillMVC.Controller.getTotalAmountFun();
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
                var selectRows = table.checkStatus('electricityBillTable');
                if (selectRows.data.length == 0) {
                    layer.msg("请选择处理的数据", {icon: 5, offset: 100, time: 1000, shift: 6});
                    return;
                }
                feeEleBillMVC.Controller.auditFun("2");
            },
            auditFun: function (status) {
                var selectRows = table.checkStatus('electricityBillTable');
                var data = "status="+status;
                $.each(selectRows.data, function (index, object) {
                    data += "&id=" + object.id;
                });
                $.post(feeEleBillMVC.URLs.audit.url, data, function (data) {
                    if (data.code == "200") {
                        feeEleBillMVC.Controller.queryFun();
                        layer.msg(data.msg, {icon: 1, offset: 100, time: 1000, shift: 6});
                    } else {
                        layer.msg(data.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                    }
                });
            },
            rejectFun: function () {
                var selectRows = table.checkStatus('electricityBillTable');
                if (selectRows.data.length == 0) {
                    layer.msg("请选择处理的数据", {icon: 5, offset: 100, time: 1000, shift: 6});
                    return;
                }
                feeEleBillMVC.Controller.auditFun("3");
            },
            commitFun: function () {
                var selectRows = table.checkStatus('electricityBillTable');
                if (selectRows.data.length == 0) {
                    layer.msg("请选择处理的数据", {icon: 5, offset: 100, time: 1000, shift: 6});
                    return;
                }

                var flag = true;
                $.each(selectRows.data, function (index, obj) {
                    if (obj.elePeakDegree == null || obj.elePeakDegree == 0
                        || obj.eleValleyDegree == null || obj.eleValleyDegree == 0
                        || obj.eleBillAmount == null || obj.eleBillAmount == 0) {
                        layer.msg("户号[" + obj.houseEleNum + "]没有录入完,不能提交", {icon: 5, offset: 100, time: 5000, shift: 6});
                        flag = false;
                        return false;
                    }
                    if (obj.billStatus != "0" && obj.billStatus != "3") {
                        layer.msg("户号[" + obj.houseEleNum + "]已经提交,不能重复提交", {icon: 5, offset: 100, time: 5000, shift: 6});
                        flag = false;
                        return false;
                    }
                });
                if(flag){
                    feeEleBillMVC.Controller.auditFun("1");
                }
            },
            printFun: function () {
                var where = feeEleBillMVC.Controller.getWhereFun();
                where.isRecord="0";

                $.post(feeEleBillMVC.URLs.print.url, where, function (resp) {
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
                    "eleBillDate": $("#eleBillDate").val(),
                    "houseEleNum": $("#houseEleNum").val(),
                    "houseId": $("#houseId").val(),
                    "elePeakDegree": $("#elePeakDegree").val(),
                    "eleValleyDegree": $("#eleValleyDegree").val(),
                    "eleBillAmount": $("#eleBillAmount").val()
                };
                $.post(feeEleBillMVC.URLs.save.url, data, function (data) {
                    if (data.code == "200") {
                        feeEleBillMVC.Controller.cleanValue();
                        $("#houseEleNum").focus();
                        layer.msg('保存成功', {icon: 1, offset: 100, time: 1000, shift: 6});
                    } else {
                        layer.msg(data.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                    }
                });
            },
            viewFun: function () {
                layer.close(addEleBillIndex);
                feeEleBillMVC.Controller.queryFun();
                feeEleBillMVC.Controller.cleanValue();
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
                $.getJSON(feeEleBillMVC.URLs.selectArea.url, "", function (data) {
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
                $.getJSON(feeEleBillMVC.URLs.selectItem.url, {
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
                $.getJSON(feeEleBillMVC.URLs.getTotalAmount.url,
                    feeEleBillMVC.Controller.getWhereFun(),
                    function (data) {
                        if (data.code == "200") {
                            $("#totalAmount").html(layui.laytpl.amountFormat(data.data));
                        }
                    });
            }
        }
    };

    feeEleBill.init();
});






