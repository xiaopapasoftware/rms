layui.use(['form', 'table', 'layer', 'laydate', 'laytpl'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        laydate = layui.laydate;

    var feeEleBill = {
        init: function () {
            feeEleBillMVC.View.initDate();
            feeEleBillMVC.View.bindEvent();
            feeEleBillMVC.Controller.getAreaFun();
            feeEleBillMVC.View.renderTable();

            form.on('select(area)', function(data){
                console.log(data.value); //得到被选中的值
                feeEleBillMVC.Controller.selectItemFun("project","project",data.value);
                $("#project option").remove();
                $("#project").append('<option value="">物业项目</option>');

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('select(project)', function(data){
                console.log(data.value); //得到被选中的值
                feeEleBillMVC.Controller.selectItemFun("building","building",data.value);

                $("#building option").remove();
                $("#building").append('<option value="">楼宇</option>');

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
            form.on('select(building)', function(data){
                console.log(data.value); //得到被选中的值
                feeEleBillMVC.Controller.selectItemFun("house","house",data.value);

                $("#house option").remove();
                $("#house").append('<option value="">房屋</option>');

                form.render('select');
            });
        }
    };

    var feeEleBillCommon = {
        baseUrl: ctx + "/fee/electricity/bill",
    };

    var feeEleBillMVC = {
        URLs: {
            query: {
                url: feeEleBillCommon.baseUrl + "/list",
                method: "GET"
            },
            edit: {
                url: feeEleBillCommon.baseUrl + "/edit",
                method: "GET"
            },
            selectItem: {
                url: feeEleBillCommon.baseUrl + "/getSubOrgList",
                method: "GET"
            },
            selectArea: {
                url: feeEleBillCommon.baseUrl + "/getArea",
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
                $("#tn-reject").on("click", feeEleBillMVC.Controller.rejectFun);
                $("#btn-commit").on("click", feeEleBillMVC.Controller.commitFun);
                $("#btn-print").on("click", feeEleBillMVC.Controller.printFun);
                $("#btn-save").on("click", feeEleBillMVC.Controller.saveFun);
                $("#btn-view").on("click", feeEleBillMVC.Controller.viewFun);
            },
            renderTable: function () {
                table.render({
                    elem: '#electricityBillTable',
                    url: feeEleBillMVC.URLs.query.url,
                    cols: [[
                        {checkbox: true, width: 20, fixed: true},
                        {field: 'areaName', align: 'center', title: '区域', width: 120},
                        {field: 'buildingName', align: 'center', title: '小区', width: 120},
                        {field: 'houseNo', align: 'center', title: '房号', width: 80},
                        {field: 'houseEleNum', align: 'center', title: '户号', width: 100},
                        {field: 'eleBillDate', align: 'center', title: '账期', width: 80},
                        {field: 'batchNo', align: 'center', title: '审核批次号', width: 100},
                        {field: 'elePeakDegree', align: 'center', title: '谷值', width: 80},
                        {field: 'eleValleyDegree', align: 'center', title: '峰值', width: 80},
                        {field: 'eleBillAmount', align: 'center', title: '金额', width: 100},
                        {field: 'billStatus', align: 'center', title: '状态', width: 100},
                        {align: 'center', title: '操作', toolbar: '#toolBar', width: 120}
                    ]],
                    id: 'electricityBillTable',
                    page: true,
                    request: {
                        pageName: 'pageNum',
                        limitName: 'pageSize'
                    },
                    where: {
                        "feeDate": $("#feeDate").val()
                    },
                    done: function (res, curr, count) {
                        //如果是异步请求数据方式，res即为你接口返回的信息。
                        //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
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
                        console.log(data);
                        //do somehing
                        obj.update({
                            username: '123'
                            , title: 'xxx'
                        });

                    } else if (layEvent === 'del') { //删除
                        layer.confirm('真的删除行么', function (index) {
                            obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                            layer.close(index);
                            //向服务端发送删除指令
                        });
                    }
                });
            },
            setValueFun:function(object){
                $("#eleBillDate").val();
                $("#houseEleNum").val();
                $("#houseId").val();
                $("#houseAddress").val();
                $("#elePeakDegree").val();
                $("#eleValleyDegree").val();
                $("#eleBillAmount").val();
            }
        },
        Controller: {
            addEleFun: function () {
                layer.open({
                    title: "电费账单录入",
                    type: 1,
                    resize: true,
                    offset: 'r',
                    anim: 2,
                    area: ['350px', '380px'],
                    content: $('#addDiv') //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                });
            },
            queryFun: function () {
                table.reload('electricityBillTable', {
                    where: {
                        "feeDate": $("#feeDate").val(),
                        "areaId": $("#area").val(),
                        "propertyId": $("#project").val(),
                        "buildId": $("#building").val(),
                        "houseId": $("#house").val(),
                        "status": $("#status").val(),
                        "isRecord": $("#isRecord").val()
                    }
                });
            },
            undoFun:function(){

            },
            passFun:function(){
                var selectRows = table.checkStatus('electricityBillTable');
                if(selectRows.data.length == 0){
                    layer.msg("请选择处理的数据", {icon: 5, offset: 100,time: 1000, shift: 6});
                    return;
                }
                console.log(selectRows);
            },
            rejectFun:function(){
                var selectRows = table.checkStatus('electricityBillTable');
                if(selectRows.data.length == 0){
                    layer.msg("请选择处理的数据", {icon: 5, offset: 100,time: 1000, shift: 6});
                    return;
                }
                console.log(selectRows);
            },
            commitFun:function(){
                var selectRows = table.checkStatus('electricityBillTable');
                if(selectRows.data.length == 0){
                    layer.msg("请选择处理的数据", {icon: 5, offset: 100,time: 1000, shift: 6});
                    return;
                }
                console.log(selectRows);
            },
            printFun:function(){},
            saveFun:function(){},
            viewFun:function(){
                layer.close(layer.index);
                feeEleBillMVC.Controller.queryFun();
            },
            getAreaFun: function () {
                $.getJSON(feeEleBillMVC.URLs.selectArea.url, "", function (data) {
                    $.each(data.data,function(index,object){
                        $('#area').append($('<option>', {
                            value: object.id,
                            text : object.name
                        }));
                    });
                    form.render('select');
                });
            },
            selectItemFun: function(id,type,value){
                $.getJSON(feeEleBillMVC.URLs.selectItem.url,{"business":"org","type":type,"id":value},function (data) {
                    var obj = $("#" + id);
                    if(data.data == null){
                        return;
                    }
                    $.each(data.data,function(index,object){
                        obj.append($('<option>', {
                            value: object.id,
                            text : object.name
                        }));
                    });
                    form.render('select');
                });
            }
        }
    };

    feeEleBill.init();
});






