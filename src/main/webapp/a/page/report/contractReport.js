/**
 * Created by wangganggang on 17/2/27.
 */

layui.use(['laypage', 'layer', 'laydate', 'laytpl'], function () {
    var laypage = layui.laypage,
        layer = layui.layer,
        laydate = layui.laydate,
        laytpl = layui.laytpl;


    var ContractReport = {
        init: function () {
            ContractReportMVC.View.initControl();
            ContractReportMVC.View.bindEvent();
        }
    }

    var ContractReportCommon = {
        baseUrl: "/rms/a/report/contract/",
        pageNo: 1,
        pageSize: 15
    };

    var ContractReportMVC = {
        URLs: {
            export: {
                url: ContractReportCommon.baseUrl + "export",
                method: "POST"
            },
            query: {
                url: ContractReportCommon.baseUrl + "query?pageNo=" + ContractReportCommon.pageNo + "&pageSize=" + ContractReportCommon.pageSize,
                method: "GET"
            }
        },
        View: {
            initControl: function () {
                ContractReportMVC.View.intDate('signDateBegin','signDateEnd');
                ContractReportMVC.View.intDate('startDateBegin','startDateEnd');
                ContractReportMVC.View.intDate('expiredDateBegin','expiredDateEnd');
            },
            intDate: function (_ele1, _ele2) {
                var start  = _ele1 + "_sart",end = _ele2 + "_end";
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

                document.getElementById(_ele1).onclick = function(){
                    start.elem = this;
                    laydate(start);
                };
                document.getElementById(_ele2).onclick = function(){
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
            export: function () {
                $("#queryFrom").attr("action", ContractReportMVC.URLs.export.url).attr("method", ContractReportMVC.URLs.export.method).submit();
            },
            undo: function () {
                $("#queryFrom input").val("");
                $("#queryFrom select option:first").prop("selected", 'selected');
            },
            query: function () {
                var index = layer.load(0, {shade: [0.1, '#000'], time: 5000});
                $.getJSON(ContractReportMVC.URLs.query.url, ContractReportMVC.Controller.params(), function (data) {
                    layer.close(index);

                    var getTpl = contractTpl.innerHTML;
                    laytpl(getTpl).render(data, function (html) {
                        contractContent.innerHTML = html;
                    });

                    laypage({
                        cont: 'contractPage',
                        pages: data.totalPage,
                        curr: data.pageSize,
                        groups: 0,
                        skip: true,
                        jump: function (obj, first) {
                            if (!first) {
                                ContractReportCommon.pageNo = obj.curr;
                                ContractReportMVC.Controller.load();
                            }
                        }
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

Render = {
    houseStyle: function (item) {

        return item.contractCode;
    },
    payType: function (item) {
        var renMonths = item.renMonths == undefined ? "0" : item.renMonths, depositMonths = item.depositMonths == undefined ? "0" : item.depositMonths;
        return "付" + renMonths + "押" + depositMonths;
    }
}






