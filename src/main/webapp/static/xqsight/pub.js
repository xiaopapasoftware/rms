/**
 * Created by wangganggang on 2017/2/28.
 */

var xqsight = xqsight || {};

xqsight.utils = {

    /** 创建from表单并提交，一般用于下载 */
    createFromAndSubmit: function (url, data, method) {
        if (url && data) {
            data = typeof data == "string" ? data : jQuery.param(data);
            var inputs = "";
            $.each(data.split("&"), function () {
                var pair = this.split("=");
                inputs += "<input type=\"hidden\" name=\"" + pair[0] + "\" value=\"" + pair[1] + "\" />";
            });
            $("<form action=\"" + url + "\" method=\"" + (method || "post") + "\">" + inputs + "</form>").appendTo("body").submit().remove();
        };
    },

    /** 获取请求的服务 **/
    getServerPath: function (reqType) {
        var serverPath = "";
        switch (reqType) {
            case "rms" :
                serverPath = "/rms/a";//
                break;
            default:
                serverPath = xqsight.utils.getContextPath();
        }
        return serverPath;
    },

    /** 获取服务的根 **/
    getContextPath: function () {
        var contextPath = document.location.pathname;
        var index = contextPath.substr(1).indexOf("/"); //这个地方可能有问题，要根据具体项目适当修改
        contextPath = contextPath.substr(0, index + 1);
        return contextPath == "/page" ? "" : contextPath;
    }

};