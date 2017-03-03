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
                laydate({
                    elem: "#signDateBegin",
                    istoday: false
                });
                /*$('#signDateBegin').on("click", function () {
                    laydate({
                        elem: this,
                        istoday: false
                    });
                });*/
                $('#signDateEnd').on("click", function () {
                    laydate({
                        elem: this,
                        istoday: false
                    });
                });

                $('#startDateBegin').on("click", function () {
                    laydate({
                        elem: this,
                        istoday: false
                    });
                });
                $('#startDateEnd').on("click", function () {
                    laydate({
                        elem: this,
                        istoday: false
                    });
                });

                $('#expiredDateBegin').on("click", function () {
                    laydate({
                        elem: this,
                        istoday: false
                    });
                });
                $('#expiredDateEnd').on("click", function () {
                    laydate({
                        elem: this,
                        istoday: false
                    });
                });
            },
            bindEvent: function () {
                $("#btn-export").on("click", ContractReportMVC.Controller.export);
                $("#btn-search").on("click", ContractReportMVC.Controller.query);
                $("#btn-undo").on("click", ContractReportMVC.Controller.undo);
            }
        },
        Controller: {
            export: function () {
                $("#queryFrom").attr("action",ContractReportMVC.URLs.export.url).attr("method",ContractReportMVC.URLs.export.method).submit();
            },
            undo: function () {
                $("#queryFrom input").val("");
                $("#queryFrom select option:first").prop("selected", 'selected');
            },
            query : function () {
                var index = layer.load(0, { shade: [0.1, '#000'], time: 5000 });
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
    houseStyle : function(item) {

        return item.contractCode;
    },
    payType : function (item) {
        var renMonths = item.renMonths == undefined ? "0" : item.renMonths,depositMonths = item.depositMonths == undefined ? "0" : item.depositMonths;
        return "付" + renMonths + "押" +  depositMonths;
    }
}






