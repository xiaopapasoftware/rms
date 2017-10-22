layui.use(['form', 'table', 'layer', 'laydate', 'laytpl'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        laydate = layui.laydate;

    var addGasReadIndex;
    var operType, operId, intentMode;

    var feeGasReadFlow = {
        init: function () {
            feeGasReadFlowMVC.View.initDate();
            feeGasReadFlowMVC.View.bindEvent();
            feeGasReadFlowMVC.Controller.getAreaFun();
            feeGasReadFlowMVC.View.renderTable();

            form.on('select(area)', function (data) {
                feeGasReadFlowMVC.Controller.selectItemFun("project", "PROJECT", data.value);
                $("#project option").remove();
                $("#project").append('<option value="">物业项目</option>');

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');
                form.render('select');
            });
            form.on('select(project)', function (data) {
                feeGasReadFlowMVC.Controller.selectItemFun("building", "BUILDING", data.value);

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('select(building)', function (data) {
                feeGasReadFlowMVC.Controller.selectItemFun("house", "HOUSE", data.value);

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('submit(addGasBill)', function () {
                feeGasReadFlowMVC.Controller.saveFun();
            });

            form.on('select(areaId)', function (data) {
                feeGasReadFlowMVC.Controller.selectItemFun("projectId", "PROJECT", data.value);
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
                feeGasReadFlowMVC.Controller.selectItemFun("buildingId", "BUILDING", data.value);

                $("#buildingId option").remove();
                $("#buildingId").append('<option value="">楼宇</option>');

                $("#houseId option").remove();
                $("#houseId").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('select(buildingId)', function (data) {
                feeGasReadFlowMVC.Controller.selectItemFun("houseId", "HOUSE", data.value);

                $("#houseId option").remove();
                $("#houseId").append('<option value="">房屋</option>');

                $("#separateRentShowDiv").html("");
                form.render('select');
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
        }
    };

    var feeGasReadFlowCommon = {
        baseUrl: ctx + "/fee/gas/read/flow"
    };

    var feeGasReadFlowMVC = {
        URLs: {
            query: {
                url: feeGasReadFlowCommon.baseUrl + "/list",
                method: "GET"
            },
            save: {
                url: feeGasReadFlowCommon.baseUrl + "/save",
                method: "POST"
            },
            delete: {
                url: feeGasReadFlowCommon.baseUrl + "/delete",
                method: "GET"
            },
            selectItem: {
                url: feeGasReadFlowCommon.baseUrl + "/getSubOrgList",
                method: "GET"
            },
            selectArea: {
                url: feeGasReadFlowCommon.baseUrl + "/getArea",
                method: "GET"
            },
            getHouseInfo: {
                url: feeGasReadFlowCommon.baseUrl + "/houseInfo",
                method: "GET"
            },
            getRoomInfo: {
                url: feeGasReadFlowCommon.baseUrl + "/roomInfo",
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
                $("#btn-add").on("click", feeGasReadFlowMVC.Controller.addGasFun);
                $("#btn-search").on("click", feeGasReadFlowMVC.Controller.queryFun);
                $("#btn-undo").on("click", feeGasReadFlowMVC.Controller.undoFun);
                $("#btn-view").on("click", feeGasReadFlowMVC.Controller.viewFun);
            },
            renderTable: function () {
                table.render({
                    elem: '#gasReadFlowTable',
                    url: feeGasReadFlowMVC.URLs.query.url,
                    limits: [20, 30, 60, 90, 150, 300],
                    limit: 20,
                    cols: [[
                        {field: 'areaName', align: 'center', title: '区域', width: 140},
                        {field: 'projectName', align: 'center', title: '物业项目', width: 140},
                        {field: 'projectAddress', align: 'center', title: '地址', width: 180},
                        {field: 'buildingName', align: 'center', title: '楼宇', width: 80},
                        {field: 'houseNo', align: 'center', title: '房号', sort: true, width: 80},
                        {field: 'intentModeName', align: 'center', title: '出租类型', width: 100},
                        {field: 'gasReadDate', align: 'center', title: '抄表日期', width: 120},
                        {
                            field: 'gasDegree', align: 'right', title: '抄表数', width: 120,
                            templet: '<div>{{ layui.laytpl.NumberFormat(d.gasDegree) }}</div>'
                        },
                        {align: 'center', title: '操作', toolbar: '#toolBar', width: 120}
                    ]],
                    id: 'gasReadFlowTable',
                    page: true,
                    request: {
                        pageName: 'pageNum',
                        limitName: 'pageSize'
                    }
                });

                table.on('tool(gasReadFlow)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
                    var data = obj.data; //获得当前行数据
                    var layEvent = obj.event; //获得 lay-event 对应的值
                    if (layEvent === 'edit') { //编辑
                        console.log(data);
                        if (data.fromSource != null && data.fromSource == "1") {
                            layer.msg("当前记录为账单录入生成,不可修改", {icon: 5, offset: 100, time: 1000, shift: 6});
                            return;
                        }
                        $("#houseId").val(data.houseId);
                        operType = "edit";
                        $("#gasDegree").val(data.gasDegree);
                        laydate.render({
                            elem: '#gasReadDate',
                            type: 'date',
                            format: 'yyyy-MM-dd',
                            value: new Date(data.gasReadDate)
                        });
                        feeGasReadFlowMVC.Controller.addGasFun();
                    } else if (layEvent === 'del') { //删除
                        if (data.fromSource != null && data.fromSource == "1") {
                            layer.msg("当前记录为账单录入生成,不可删除", {icon: 5, offset: 100, time: 1000, shift: 6});
                            return;
                        }
                        layer.confirm('确认删除吗?', {offset: '100px', icon: 3, title: '提示'}, function (index) {
                            $.post(feeGasReadFlowMVC.URLs.delete.url, {id: data.id}, function (data) {
                                if (data.code == "200") {
                                    feeGasReadFlowMVC.Controller.queryFun();
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
                    "startTime": $("#gasReadDates").val().split("~")[0],
                    "endTime": $("#gasReadDates").val().split("~")[1],
                    "areaId": $("#area").val(),
                    "propertyId": $("#project").val(),
                    "buildId": $("#building").val(),
                    "houseId": $("#house").val()
                };
                return where;
            },
            addGasFun: function () {
                laydate.render({
                    elem: '#gasReadDate',
                    type: 'date',
                    format: 'yyyy-MM-dd',
                    value: new Date()
                });
                addGasReadIndex = layer.open({
                    title: "燃气抄表录入",
                    type: 1,
                    resize: true,
                    offset: 'rt',
                    anim: 2,
                    area: ['370px', '450px'],
                    content: $('#addDiv') //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                });
            },
            queryFun: function () {
                table.reload('gasReadFlowTable', {
                    where: feeGasReadFlowMVC.Controller.getWhereFun()
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
            saveFun: function () {
                var data = "gasReadDate=" + $("#gasReadDate").val();
                data += "&houseId=" + $("#houseId").val();
                data += "&gasDegree=" + $("#gasDegree").val();

                $.post(feeGasReadFlowMVC.URLs.save.url, data, function (data) {
                    if (data.code == "200") {
                        $("#houseId").val("");
                        $("#gasDegree").val("");
                        form.render('select');
                        layer.msg('保存成功', {icon: 1, offset: 100, time: 1000, shift: 6});
                    } else {
                        layer.msg(data.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                    }
                });
            },
            viewFun: function () {
                layer.close(addGasReadIndex);
                feeGasReadFlowMVC.Controller.queryFun();
                feeGasReadFlowMVC.Controller.cleanValue();
            },
            cleanValue: function () {
                $("#gasReadDate").val("");
                $("#areaId").val("");
                $("#projectId").val("");
                $("#buildingId").val("");
                $("#houseId").val("");
                $("#gasDegree").val("");
                form.render('select');
            },
            getAreaFun: function () {
                $.getJSON(feeGasReadFlowMVC.URLs.selectArea.url, "", function (data) {
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
                $.getJSON(feeGasReadFlowMVC.URLs.selectItem.url, {
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
    feeGasReadFlow.init();
});






