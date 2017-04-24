/**
 * Created by wangganggang on 17/4/22.
 */
layui.use(['form', 'laypage', 'layer', 'laydate', 'laytpl'], function () {
    var form = layui.form(),
        laypage = layui.laypage,
        layer = layui.layer,
        laydate = layui.laydate,
        laytpl = layui.laytpl;

    var FinanceReport = {
        init: function () {
            FinanceReportMVC.View.initControl();
            FinanceReportMVC.View.bindEvent();
            FinanceReportMVC.Controller.loadProject();
        }
    };

    var FinanceReportCommon = {
        baseUrl: ctx + "/a/report/",
        pageNum: 1,
        pageSize: 50,
        exportNum : 110000
    };

    var FinanceReportMVC = {
        URLs: {
            export: {
                url: FinanceReportCommon.baseUrl + "finance/export",
                method: "POST"
            },
            query: {
                url: FinanceReportCommon.baseUrl + "finance/query",
                method: "GET"
            },
            project :{
                url: FinanceReportCommon.baseUrl + "component/project",
                method: "GET"
            }
        },
        View: {
            initControl: function () {
                FinanceReportMVC.View.intDate('receiptDateBegin', 'receiptDateEnd');
            },
            intDate: function (_ele1, _ele2) {
                var start = _ele1 + "_sart", end = _ele2 + "_end";
                var startObj = $("#"+_ele1),endObj = $("#"+_ele2);
                startObj.val(moment().subtract(30,'days').format('YYYY-MM-DD'));
                endObj.val(moment().format('YYYY-MM-DD'));
                start = {
                    max: '2099-06-16 23:59:59',
                    choose: function (datas) {
                        end.min = datas; //开始日选好后，重置结束日的最小日期
                        end.start = datas;//将结束日的初始值设定为开始日
                        if(moment().diff(datas) > 30){
                            end.max = moment(datas).add(30,"days");
                        }
                    }
                };

                end = {
                    max: laydate.now(),
                    choose: function (datas) {
                        start.min = moment(datas).subtract(30,'days').format('YYYY-MM-DD');
                        start.max = datas; //结束日选好后，重置开始日的最大日期
                    }
                };

                laydate(end);
                document.getElementById(_ele1).onclick = function () {
                    start.elem = this;
                    laydate(start);
                };
                document.getElementById(_ele2).onclick = function () {
                    end.elem = this;
                    laydate(end);
                };
            },
            bindEvent: function () {
                $("#btn-export").on("click", FinanceReportMVC.Controller.export);
                $("#btn-search").on("click", FinanceReportMVC.Controller.query);
                $("#btn-undo").on("click", FinanceReportMVC.Controller.undo);
            }
        },
        Controller: {
            export: function (){
                layer.alert("导出比较慢,请耐心等待，如果失败5分钟之后再试",{offset: 100},function(index){
                    $("#queryFrom").attr("action", FinanceReportMVC.URLs.export.url).attr("method", FinanceReportMVC.URLs.export.method).submit();
                    layer.close(index);
                });
            },
            undo: function () {
                $("#queryFrom input").val("");
                $("#queryFrom select option:first").prop("selected", 'selected');
            },
            query: function () {
                if($("#receiptDateBegin").val() == "" || $("#receiptDateEnd").val() == ""){
                    layer.alert("收据结束日期和开始日期不能空!");
                    return;
                }

                var day = moment($("#receiptDateEnd").val()).diff(moment($("#receiptDateBegin").val()),'days');
                if(day > 30){
                    layer.alert("收据结束日期和开始日期之差不能超过30天!");
                    return;
                }

                var index = layer.load(0, {shade: [0.1, '#000'], time: 5000});
                var url = FinanceReportMVC.URLs.query.url + "?pageNum=" + FinanceReportCommon.pageNum + "&pageSize=" + FinanceReportCommon.pageSize;
                $.getJSON(url, FinanceReportMVC.Controller.params(), function (rep) {
                    layer.close(index);
                    var getTpl = financeTpl.innerHTML;
                    laytpl(getTpl).render(rep.data, function (html) {
                        financeContent.innerHTML = html;
                    });
                    laypage({
                        cont : 'financePage',
                        pages : rep.totalPage,
                        curr : rep.pageNum,
                        groups : 5,
                        skip : true,
                        jump : function (obj, first) {
                            if (!first) {
                                FinanceReportCommon.pageNum = obj.curr;
                                FinanceReportMVC.Controller.query();
                            }
                        }
                    });

                    $("#totalAmount").html(rep.data.totalAmount.sumTotalAmount);
                    $("#houseAmount").html(rep.data.totalAmount.sumHouseAmount);
                    $("#houseDeposit").html(rep.data.totalAmount.sumHouseDeposit);
                    $("#waterDeposit").html(rep.data.totalAmount.sumWaterDeposit);
                    $("#agreeAmount").html(rep.data.totalAmount.sumAgreeAmount);
                    $("#firstEleAmount").html(rep.data.totalAmount.sumFirstEleAmount);
                    $("#serviceAmount").html(rep.data.totalAmount.sumServiceAmount);
                    $("#waterAmount").html(rep.data.totalAmount.sumWaterAmount);
                    $("#netAmount").html(rep.data.totalAmount.sumNetAmount);
                    $("#tvAmount").html(rep.data.totalAmount.sumTvAmount);
                });
            },
            loadProject : function(){
                var index = layer.load(0, {shade: [0.1, '#000'], time: 5000});
                $.getJSON(FinanceReportMVC.URLs.project.url, "", function (data) {
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

    FinanceReport.init();
});


function a(a){
    alert(a);
}






