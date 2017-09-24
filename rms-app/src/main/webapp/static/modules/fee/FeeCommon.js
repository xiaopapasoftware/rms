layui.use(['form', 'layer', 'laydate', 'laytpl'], function () {
    var form = layui.form,
        layer = layui.layer,
        laydate = layui.laydate,
        laytpl = layui.laytpl;


    var feeEleBill = {
        init: function () {
            feeEleBillMVC.View.initControl();
            feeEleBillMVC.View.bindEvent();
            feeEleBillMVC.Controller.loadDict();
            feeEleBillMVC.Controller.loadProject();
        }
    }

    var feeEleBillCommon = {
        baseUrl: ctx + "/a/report/",
        pageNum: 1,
        pageSize: 50,
        exportNum : 110000
    };

    var feeEleBillMVC = {
        URLs: {
            export: {
                url: feeEleBillCommon.baseUrl + "contract/export",
                method: "POST"
            },
            query: {
                url: feeEleBillCommon.baseUrl + "contract/query",
                method: "GET"
            },
            dict :{
                url: feeEleBillCommon.baseUrl + "component/dict",
                method: "GET"
            },
            project :{
                url: feeEleBillCommon.baseUrl + "component/project",
                method: "GET"
            }
        },
        View: {
            initControl: function () {
                feeEleBillMVC.View.intDate('signDateBegin', 'signDateEnd');
                feeEleBillMVC.View.intDate('startDateBegin', 'startDateEnd');
                feeEleBillMVC.View.intDate('expiredDateBegin', 'expiredDateEnd');
            },
            intDate: function (_ele1, _ele2) {
                laydate.render({
                    elem: '#'+_ele1,
                    eventElem: '#'+_ele1,
                    trigger: 'click'
                });

                laydate.render({
                    elem: '#'+_ele2,
                    eventElem: '#'+_ele2,
                    trigger: 'click'
                });
            },
            bindEvent: function () {
                $("#btn-export").on("click", feeEleBillMVC.Controller.export);
                $("#btn-search").on("click", feeEleBillMVC.Controller.query);
                $("#btn-undo").on("click", feeEleBillMVC.Controller.undo);
            }
        },
        Controller: {
            export: function (){
                layer.open({
                    content: '测试回调',
                    success: function(layero, index){
                        console.log(layero, index);
                    }
                });
                layer.alert("导出比较慢,请耐心等待，如果失败5分钟之后再试",{offset: 100},function(index){
                    $("#queryFrom").attr("action", feeEleBillMVC.URLs.export.url).attr("method", feeEleBillMVC.URLs.export.method).submit();
                    layer.close(index);
                });
            },
            undo: function () {
                $("#queryFrom input").val("");
                $("#queryFrom select option:first").prop("selected", 'selected');
            },
            query: function () {
                var index = layer.load(0, {shade: [0.1, '#000'], time: 5000});
                var url = feeEleBillMVC.URLs.query.url + "?pageNum=" + feeEleBillCommon.pageNum + "&pageSize=" + feeEleBillCommon.pageSize;
                $.getJSON(url, feeEleBillMVC.Controller.params(), function (data) {
                    layer.close(index);

                    var getTpl = contractTpl.innerHTML;
                    laytpl(getTpl).render(data, function (html) {
                        contractContent.innerHTML = html;
                    });

                    laypage({
                        cont : 'contractPage',
                        pages : data.totalPage,
                        curr : data.pageNum,
                        groups : 5,
                        skip : true,
                        jump : function (obj, first) {
                            if (!first) {
                                feeEleBillCommon.pageNum = obj.curr;
                                feeEleBillMVC.Controller.query();
                            }
                        }
                    });
                });
            },
            loadDict : function(){
                var index = layer.load(0, {shade: [0.1, '#000'], time: 5000});
                $.getJSON(feeEleBillMVC.URLs.dict.url, "type=rent_contract_busi_status", function (data) {
                    layer.close(index);
                    var getTpl = dictValueTpl.innerHTML;
                    laytpl(getTpl).render(data, function (html) {
                        dictValue.innerHTML = html;
                        form.render('select');
                    });
                });
            },
            loadProject : function(){
                var index = layer.load(0, {shade: [0.1, '#000'], time: 5000});
                $.getJSON(feeEleBillMVC.URLs.project.url, "", function (data) {
                    layer.close(index);
                    var getTpl = projectValueTpl.innerHTML;
                    laytpl(getTpl).render(data, function (html) {
                        projectValue.innerHTML = html;
                        form.render('select');
                    });
                });
            },
            params: function () {
                return $("#queryFrom").serialize();
            }
        }
    };

    feeEleBill.init();
});






