/**
 * Created by wangganggang on 17/2/27.
 */

layui.use('upload', function () {

    layui.upload({
        url: '/test/upload.json',
        elem: '#test' ,//指定原始元素，默认直接查找class="layui-upload-file"
        method: 'post', //上传接口的http类型
        success: function(res){
           alert();
        }
    });

    var FileUpload = {
        init: function () {
            FileUploadMVC.View.initControl();
            FileUploadMVC.View.bindEvent();
        }
    }

    var FileUploadCommon = {
        baseUrl: ctx + "/a/file",
        exportNum: 110000
    };

    var FileUploadMVC = {
        URLs: {
            upload: {
                url: FileUploadCommon.baseUrl + "/upload",
                method: "POST"
            },
            download: {
                url: FileUploadCommon.baseUrl + "/download",
                method: "GET"
            }
        },
        View: {
            initControl: function () {
            },
            bindEvent: function () {
                $("#btn-export").on("click", FileUploadMVC.Controller.export);
            }
        },
        Controller: {
            upload: function () {

            },
            query: function () {

            }
        }
    };

    FileUpload.init();
});






