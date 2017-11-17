/**
 * Created by wangganggang on 17/2/27.
 */

layui.use(['form', 'laypage', 'layer', 'laytpl'], function () {
    var form = layui.form,
        laypage = layui.laypage,
        layer = layui.layer,
        laytpl = layui.laytpl;


    var RentDueUrgeReport = {
        init: function () {
            RentDueUrgeReportMVC.View.initControl();
            RentDueUrgeReportMVC.View.bindEvent();
            RentDueUrgeReportMVC.Controller.loadProject();
        }
    }

    var RentDueUrgeReportCommon = {
        baseUrl: ctx + "/a/report/",
        pageNum: 1,
        pageSize: 50,
        exportNum : 1000
    };

    var RentDueUrgeReportMVC = {
        URLs: {
            export: {
                url: RentDueUrgeReportCommon.baseUrl + "rentdueurge/export",
                method: "POST"
            },
            query: {
                url: RentDueUrgeReportCommon.baseUrl + "rentdueurge/query",
                method: "GET"
            },
            project :{
                url: RentDueUrgeReportCommon.baseUrl + "component/project",
                method: "GET"
            }
        },
        View: {
            initControl: function () {
            },
            bindEvent: function () {
                $("#btn-export").on("click", RentDueUrgeReportMVC.Controller.export);
                $("#btn-search").on("click", RentDueUrgeReportMVC.Controller.query);
                $("#btn-undo").on("click", RentDueUrgeReportMVC.Controller.undo);
            }
        },
        Controller: {
            export: function () {
                layer.alert("导出比较慢,请耐心等待，如果失败5分钟之后再试",{offset: 100},function(index){
                    $("#queryFrom").attr("action", RentDueUrgeReportMVC.URLs.export.url).attr("method", RentDueUrgeReportMVC.URLs.export.method).submit();
                    layer.close(index);
                });
                /*layer.prompt({
                    formType: 3,
                    value: RentDueUrgeReportCommon.exportNum,
                    title: '导出条数',
                    area: ['100px', '30px'] //自定义文本域宽高
                }, function(value, index, elem){
                    if(isNaN(value)) return;
                    if(value > RentDueUrgeReportCommon.exportNum){
                        layer.alert("导出条数不能大于:" + ContractReportCommon.exportNum);
                        return;
                    }
                    var url = RentDueUrgeReportMVC.URLs.export.url + "?pageSize=" + value;
                    $("#queryFrom").attr("action", url).attr("method", RentDueUrgeReportMVC.URLs.export.method).submit();
                    layer.close(index);
                });*/
            },
            undo: function () {
                $("#queryFrom input").val("");
                $("#queryFrom select option:first").prop("selected", 'selected');
            },
            query: function () {
                var index = layer.load(0, {shade: [0.1, '#000'], time: 5000});
                var url = RentDueUrgeReportMVC.URLs.query.url + "?pageNum=" + RentDueUrgeReportCommon.pageNum + "&pageSize=" + RentDueUrgeReportCommon.pageSize;
                $.getJSON(url, RentDueUrgeReportMVC.Controller.params(), function (data) {
                    layer.close(index);

                    var getTpl = rentDueUrgeTpl.innerHTML;
                    laytpl(getTpl).render(data, function (html) {
                        rentDueUrgeContent.innerHTML = html;
                    });

                    laypage.render({
                        elem : 'rentDueUrgePage',
                        count : data.totalPage,
                        curr : data.pageNum,
                        groups : 5,
                        skip : true,
                        jump : function (obj, first) {
                            if (!first) {
                                RentDueUrgeReportCommon.pageNum = obj.curr;
                                RentDueUrgeReportMVC.Controller.query();
                            }
                        }
                    });
                });
            },
            loadProject : function(){
                var index = layer.load(0, {shade: [0.1, '#000'], time: 5000});
                $.getJSON(RentDueUrgeReportMVC.URLs.project.url, "", function (data) {
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

    RentDueUrgeReport.init();
});






