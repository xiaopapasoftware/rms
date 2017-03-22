/**
 * Created by wangganggang on 17/2/27.
 */

layui.use(['form', 'laypage', 'layer', 'laydate', 'laytpl'], function () {
    var form = layui.form(),
        laypage = layui.laypage,
        layer = layui.layer,
        laydate = layui.laydate,
        laytpl = layui.laytpl;


    var ContractReport = {
        init: function () {
            ContractReportMVC.View.initControl();
            ContractReportMVC.View.bindEvent();
            ContractReportMVC.Controller.loadDict();
        }
    }

    var ContractReportCommon = {
        baseUrl: ctx + "/a/report/",
        pageNum: 1,
        pageSize: 50,
        exportNum : 110000
    };

    var ContractReportMVC = {
        URLs: {
            export: {
                url: ContractReportCommon.baseUrl + "contract/export",
                method: "POST"
            },
            query: {
                url: ContractReportCommon.baseUrl + "contract/query",
                method: "GET"
            },
            dict :{
                url: ContractReportCommon.baseUrl + "component/dict",
                method: "GET"
            }
        },
        View: {
            initControl: function () {
                ContractReportMVC.View.intDate('signDateBegin', 'signDateEnd');
                ContractReportMVC.View.intDate('startDateBegin', 'startDateEnd');
                ContractReportMVC.View.intDate('expiredDateBegin', 'expiredDateEnd');
            },
            intDate: function (_ele1, _ele2) {
                var start = _ele1 + "_sart", end = _ele2 + "_end";
                start = {
                    //min: laydate.now(),
                    max: '2099-06-16 23:59:59',
                    istoday: false,
                    choose: function (datas) {
                        end.min = datas; //开始日选好后，重置结束日的最小日期
                        end.start = datas //将结束日的初始值设定为开始日
                    }
                };

                end = {
                    //min: laydate.now(),
                    max: '2099-06-16 23:59:59',
                    istoday: false,
                    choose: function (datas) {
                        start.max = datas; //结束日选好后，重置开始日的最大日期
                    }
                };

                document.getElementById(_ele1).onclick = function () {
                    start.elem = this;
                    laydate(start);
                };
                document.getElementById(_ele2).onclick = function () {
                    end.elem = this
                    laydate(end);
                };
            },
            bindEvent: function () {
                $("#btn-export").on("click", ContractReportMVC.Controller.export);
                $("#btn-search").on("click", ContractReportMVC.Controller.query);
                $("#btn-undo").on("click", ContractReportMVC.Controller.undo);
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
                    $("#queryFrom").attr("action", ContractReportMVC.URLs.export.url).attr("method", ContractReportMVC.URLs.export.method).submit();
                    layer.close(index);
                });

               /* layer.prompt({
                    formType: 3,
                    value: ContractReportCommon.exportNum,
                    title: '导出条数',
                    area: ['100px', '30px'] //自定义文本域宽高
                }, function(value, index, elem){
                    if(isNaN(value)) return;
                    /!*if(value > ContractReportCommon.exportNum){
                        layer.alert("导出条数不能大于:" + ContractReportCommon.exportNum);
                        return;
                    }*!/

                    var url = ContractReportMVC.URLs.export.url + "?pageSize=" + value;
                    $("#queryFrom").attr("action", url).attr("method", ContractReportMVC.URLs.export.method).submit();
                    layer.close(index);
                });*/
            },
            undo: function () {
                $("#queryFrom input").val("");
                $("#queryFrom select option:first").prop("selected", 'selected');
            },
            query: function () {
                var index = layer.load(0, {shade: [0.1, '#000'], time: 5000});
                var url = ContractReportMVC.URLs.query.url + "?pageNum=" + ContractReportCommon.pageNum + "&pageSize=" + ContractReportCommon.pageSize;
                $.getJSON(url, ContractReportMVC.Controller.params(), function (data) {
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
                                ContractReportCommon.pageNum = obj.curr;
                                ContractReportMVC.Controller.query();
                            }
                        }
                    });
                });
            },
            loadDict : function(){
                var index = layer.load(0, {shade: [0.1, '#000'], time: 5000});
                $.getJSON(ContractReportMVC.URLs.dict.url, "type=rent_contract_busi_status", function (data) {
                    layer.close(index);
                    var getTpl = dictValueTpl.innerHTML;
                    laytpl(getTpl).render(data, function (html) {
                        dictValue.innerHTML = html;
                        form.render('select');
                    });
                });
            },
            params: function () {
                return $("#queryFrom").serialize();
            }
        }
    };

    ContractReport.init();
});






