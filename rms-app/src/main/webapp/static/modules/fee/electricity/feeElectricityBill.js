layui.use(['form', 'table', 'layer', 'laydate', 'laytpl'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        laydate = layui.laydate,
        laytpl = layui.laytpl;


    var feeEleBill = {
        init: function () {
            feeEleBillMVC.View.initDate();
            feeEleBillMVC.View.bindEvent();
            //eeEleBillMVC.Controller.getCounty();
            feeEleBillMVC.View.renderTable();
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
            edit :{
                url: feeEleBillCommon.baseUrl + "/edit",
                method: "GET"
            },
            selectItem :{
                url: feeEleBillCommon.baseUrl + "/getSubOrgList",
                method: "GET"
            }
        },
        View: {
            initDate: function () {
                laydate.render({
                    elem: '#feeDate',
                    type: 'month',
                    btns: ['confirm']
                    //value: new Date()
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
                $("#btn-search").on("click", feeEleBillMVC.Controller.query);
                $("#btn-undo").on("click", feeEleBillMVC.Controller.undo);
            },
            renderTable:function(){
                table.render({
                    elem: '#electricityBillTable',
                    url: feeEleBillMVC.URLs.query.url,
                    cols: [[
                        {checkbox: true, width:20,fixed: true},
                        {field:'areaName', align:'center', title: '区域', width:120},
                        {field:'buildingName', align:'center',title: '小区', width:120},
                        {field:'houseNo', align:'center',title: '房号', width:80},
                        {field:'houseEleNum', align:'center',title: '户号', width:100},
                        {field:'eleBillDate', align:'center',title: '账期', width:80},
                        {field:'batchNo', align:'center',title: '审核批次号',width:100},
                        {field:'elePeakDegree', align:'center',title: '谷值',width:80},
                        {field:'eleValleyDegree', align:'center',title: '峰值',width:80},
                        {field:'eleBillAmount', align:'center',title: '金额',width:100},
                        {field:'billStatus', align:'center',title: '状态', width:100},
                        {align:'center',title: '操作', toolbar: '#toolBar',width:120}
                    ]],
                    id: 'electricityBillTable',
                    page: true,
                    request: {
                        pageName: 'pageNum',
                        limitName: 'pageSize'
                    },
                    where:{
                        "feeDate" : $("#feeDate").val()
                    },
                    done: function(res, curr, count){
                        //如果是异步请求数据方式，res即为你接口返回的信息。
                        //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                        console.log(res);

                        //得到当前页码
                        console.log(curr);

                        //得到数据总量
                        console.log(count);
                    }
                });

                table.on('tool(electricityBill)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
                    var data = obj.data; //获得当前行数据
                    var layEvent = obj.event; //获得 lay-event 对应的值

                    if(layEvent === '保存'){ //编辑
                        console.log(data);
                        //do somehing
                        obj.update({
                            username: '123'
                            ,title: 'xxx'
                        });

                    } else if(layEvent === 'del'){ //删除
                        layer.confirm('真的删除行么', function(index){
                            obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                            layer.close(index);
                            //向服务端发送删除指令
                        });
                    }
                });
            }
        },
        Controller: {
            addEleFun:function () {
                layer.open({
                    title:"电费账单录入",
                    type: 1,
                    resize: true,
                    offset: 'r',
                    anim: 2,
                    area: ['350px', '380px'],
                    content: $('#addDiv') //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                });
            },
            query: function () {

            },

            getCounty : function(){
                var index = layer.load(0, {shade: [0.1, '#000'], time: 5000});
                $.getJSON(feeEleBillMVC.URLs.selectItem, {"business":"org","type":"county"}, function (data) {
                    layer.close(index);
                    var getTpl = projectValueTpl.innerHTML;
                    laytpl(getTpl).render(data, function (html) {
                        projectValue.innerHTML = html;
                        form.render('select');
                    });
                });
            },

            commitEleBill : function(){
                form.on('submit(addEleBill)', function(data){
                    layer.msg(JSON.stringify(data.field));
                    return false;
                });
            }
        }
    };

    feeEleBill.init();
});






