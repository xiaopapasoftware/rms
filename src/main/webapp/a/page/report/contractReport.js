/**
 * Created by wangganggang on 17/2/27.
 */
layui.use(['laypage', 'layer','laytpl'], function() {
    var laypage = layui.laypage
        , layer = layui.layer
        ,laytpl = layui.laytpl;

    var ctxData = xqsight.utils.getServerPath("rms");


    var loadDataFun = function(pageNum,pageSize){
        $.ajax({
            url : ctxData + "/report/contract/query",
            data : {"pageNum" : pageNum,"pageSize" :  pageSize},
            success : function(retData){
                console.debug(retData);
                renderDataFun(retData);
                renderPageFun(retData);
            }
        })
    };

    var renderDataFun = function(data){
        var getTpl = contractTpl.innerHTML;
        laytpl(getTpl).render(data, function(html){
            contractContent.innerHTML = html;
        });
    };

    var renderPageFun = function(data){
        laypage({
            cont: 'contractPage',
            pages: data.totalPage,
            curr : data.pageSize,
            groups : 0,
            skip: true,
            jump : function(obj, first){
                console.debug("test");
                if(!first){
                    loadData(obj.curr,obj.groups);
                }
            }
        });
    }

    var initFun = function(){
        loadDataFun(1,15);

        $("#btn-export").on("click",exportFun);
    }

    var exportFun = function(){
        xqsight.utils.createFromAndSubmit(ctxData + "/report/contract/export","&date=a","post");
    }

    initFun();

})

