layui.use(['form', 'table', 'layer', 'laydate', 'laytpl'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        laydate = layui.laydate;

    var addWaterReadIndex;

    var feeWaterReadFlow = {
        init: function () {
            feeWaterReadFlowMVC.View.initDate();
            feeWaterReadFlowMVC.View.bindEvent();
            feeWaterReadFlowMVC.Controller.getAreaFun();
            feeWaterReadFlowMVC.View.renderTable();

            form.on('select(area)', function (data) {
                feeWaterReadFlowMVC.Controller.selectItemFun("project", "PROJECT", data.value);
                $("#project option").remove();
                $("#project").append('<option value="">物业项目</option>');

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');
                form.render('select');
            });
            form.on('select(project)', function (data) {
                feeWaterReadFlowMVC.Controller.selectItemFun("building", "BUILDING", data.value);

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('select(building)', function (data) {
                feeWaterReadFlowMVC.Controller.selectItemFun("house", "HOUSE", data.value);

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('submit(addWaterBill)', function () {
                feeWaterReadFlowMVC.Controller.saveFun();
            });

            form.on('select(areaId)', function (data) {
                feeWaterReadFlowMVC.Controller.selectItemFun("projectId", "PROJECT", data.value);
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
                feeWaterReadFlowMVC.Controller.selectItemFun("buildingId", "BUILDING", data.value);

                $("#buildingId option").remove();
                $("#buildingId").append('<option value="">楼宇</option>');

                $("#houseId option").remove();
                $("#houseId").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('select(buildingId)', function (data) {
                feeWaterReadFlowMVC.Controller.selectItemFun("houseId", "HOUSE", data.value);

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

    var feeWaterReadFlowCommon = {
        baseUrl: ctx + "/fee/water/read/flow"
    };

    var feeWaterReadFlowMVC = {
        URLs: {
            query: {
                url: feeWaterReadFlowCommon.baseUrl + "/list",
                method: "GET"
            },
            save: {
                url: feeWaterReadFlowCommon.baseUrl + "/save",
                method: "POST"
            },
            delete: {
                url: feeWaterReadFlowCommon.baseUrl + "/delete",
                method: "GET"
            },
            selectItem: {
                url: feeWaterReadFlowCommon.baseUrl + "/getSubOrgList",
                method: "GET"
            },
            selectArea: {
                url: feeWaterReadFlowCommon.baseUrl + "/getArea",
                method: "GET"
            },
            getHouseInfo: {
                url: feeWaterReadFlowCommon.baseUrl + "/houseInfo",
                method: "GET"
            },
            getRoomInfo: {
                url: feeWaterReadFlowCommon.baseUrl + "/roomInfo",
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
                $("#btn-add").on("click", feeWaterReadFlowMVC.Controller.addWaterFun);
                $("#btn-search").on("click", feeWaterReadFlowMVC.Controller.queryFun);
                $("#btn-undo").on("click", feeWaterReadFlowMVC.Controller.undoFun);
                $("#btn-view").on("click", feeWaterReadFlowMVC.Controller.viewFun);
            },
            renderTable: function () {
                table.render({
                    elem: '#waterReadFlowTable',
                    url: feeWaterReadFlowMVC.URLs.query.url,
                    limits: [20, 30, 60, 90, 150, 300],
                    limit: 20,
                    cols: [[
                        {field: 'areaName', align: 'center', title: '区域', width: 140},
                        {field: 'projectName', align: 'center', title: '物业项目', width: 140},
                        {field: 'projectAddress', align: 'center', title: '地址', width: 180},
                        {field: 'buildingName', align: 'center', title: '楼宇', width: 80},
                        {field: 'houseNo', align: 'center', title: '房号', sort: true, width: 80},
                        {field: 'intentModeName', align: 'center', title: '出租类型', width: 100},
                        {field: 'waterReadDate', align: 'center', title: '抄表日期', width: 120},
                        {
                            field: 'waterDegree', align: 'right', title: '抄表数', width: 120,
                            templet: '<div>{{ layui.laytpl.NumberFormat(d.waterDegree) }}</div>'
                        },
                        {align: 'center', title: '操作', toolbar: '#toolBar', width: 120}
                    ]],
                    id: 'waterReadFlowTable',
                    page: true,
                    request: {
                        pageName: 'pageNum',
                        limitName: 'pageSize'
                    }
                });

                table.on('tool(waterReadFlow)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
                    var data = obj.data; //获得当前行数据
                    var layEvent = obj.event; //获得 lay-event 对应的值
                    if (layEvent === 'edit') { //编辑
                        console.log(data);
                        if (data.fromSource != null && data.fromSource == "1") {
                            layer.msg("当前记录为账单录入生成,不可修改", {icon: 5, offset: 100, time: 1000, shift: 6});
                            return;
                        }
                        feeWaterReadFlowMVC.Controller.selectItemFun("projectId", "PROJECT", data.areaId,function(){
                            feeWaterReadFlowMVC.Controller.selectItemFun("buildingId", "BUILDING", data.propertyId,function(){
                                feeWaterReadFlowMVC.Controller.selectItemFun("houseId", "HOUSE", data.buildingId,function(){
                                    $("#areaId").val(data.areaId);
                                    $("#projectId").val(data.propertyId);
                                    $("#buildingId").val(data.buildingId);
                                    $("#houseId").val(data.houseId);
                                    form.render('select');
                                });
                            });
                        });

                        $("#waterDegree").val(data.waterDegree);
                        laydate.render({
                            elem: '#waterReadDate',
                            type: 'date',
                            format: 'yyyy-MM-dd',
                            value: new Date(data.waterReadDate)
                        });
                        feeWaterReadFlowMVC.Controller.addWaterFun();
                    } else if (layEvent === 'del') { //删除
                        if (data.fromSource != null && data.fromSource == "1") {
                            layer.msg("当前记录为账单录入生成,不可删除", {icon: 5, offset: 100, time: 1000, shift: 6});
                            return;
                        }
                        layer.confirm('确认删除吗?', {offset: '100px', icon: 3, title: '提示'}, function (index) {
                            $.post(feeWaterReadFlowMVC.URLs.delete.url, {id: data.id}, function (data) {
                                if (data.code == "200") {
                                    feeWaterReadFlowMVC.Controller.queryFun();
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
                    "startTime": $("#waterReadDates").val().split("~")[0],
                    "endTime": $("#waterReadDates").val().split("~")[1],
                    "areaId": $("#area").val(),
                    "propertyId": $("#project").val(),
                    "buildId": $("#building").val(),
                    "houseId": $("#house").val()
                };
                return where;
            },
            addWaterFun: function () {
                laydate.render({
                    elem: '#waterReadDate',
                    type: 'date',
                    format: 'yyyy-MM-dd',
                    value: new Date()
                });
                addWaterReadIndex = layer.open({
                    title: "水抄表录入",
                    type: 1,
                    resize: true,
                    offset: 'rt',
                    anim: 2,
                    area: ['370px', '450px'],
                    content: $('#addDiv') //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                });
            },
            queryFun: function () {
                table.reload('waterReadFlowTable', {
                    where: feeWaterReadFlowMVC.Controller.getWhereFun()
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
            saveFun: function () {
                var data = "waterReadDate=" + $("#waterReadDate").val();
                data += "&houseId=" + $("#houseId").val();
                data += "&waterDegree=" + $("#waterDegree").val();

                $.post(feeWaterReadFlowMVC.URLs.save.url, data, function (data) {
                    if (data.code == "200") {
                        $("#houseId").val("");
                        $("#waterDegree").val("");
                        form.render('select');
                        layer.msg('保存成功', {icon: 1, offset: 100, time: 1000, shift: 6});
                    } else {
                        layer.msg(data.msg, {icon: 5, offset: 100, time: 1000, shift: 6});
                    }
                });
            },
            viewFun: function () {
                layer.close(addWaterReadIndex);
                feeWaterReadFlowMVC.Controller.queryFun();
                feeWaterReadFlowMVC.Controller.cleanValue();
            },
            cleanValue: function () {
                $("#waterReadDate").val("");
                $("#areaId").val("");
                $("#projectId").val("");
                $("#buildingId").val("");
                $("#houseId").val("");
                $("#waterDegree").val("");
                form.render('select');
            },
            getAreaFun: function () {
                $.getJSON(feeWaterReadFlowMVC.URLs.selectArea.url, "", function (data) {
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
            selectItemFun: function (id, type, value,callbackFun) {
                $.getJSON(feeWaterReadFlowMVC.URLs.selectItem.url, {
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

                    if(callbackFun!=undefined && callbackFun !=null){
                        callbackFun(data);
                    }
                });
            }
        }
    };
    feeWaterReadFlow.init();
});






