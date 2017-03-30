<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="input" type="java.lang.String" required="true" description="输入框"%>
<%@ attribute name="type" type="java.lang.String" required="true" description="files、images、flash、thumb"%>
<%@ attribute name="uploadPath" type="java.lang.String" required="true" description="打开文件管理的上传路径"%>
<%@ attribute name="selectMultiple" type="java.lang.Boolean" required="false" description="是否允许多选"%>
<%@ attribute name="readonly" type="java.lang.Boolean" required="false" description="是否查看模式"%>
<%@ attribute name="maxWidth" type="java.lang.String" required="false" description="最大宽度"%>
<%@ attribute name="maxHeight" type="java.lang.String" required="false" description="最大高度"%>
<ol id="${input}Preview"></ol><c:if test="${!readonly}">
    <div class="layui-box layui-upload-button">
       <input type="file" id="${input}_upload_file" name="file" class="layui-upload-file" lay-title="添加" onchange="${input}UploadFile();">
     <span class="layui-upload-icon"><i class="layui-icon"></i>添加</span></div>
    <%--<input type="file" id="_btn_upload" name="file" class="layui-upload-file" lay-title="添加">--%>
    <%--<a href="javascript:" id="_btn_upload" class="btn">${selectMultiple?'添加':'选择'}</a>--%>&nbsp;
    <a href="javascript:" onclick="${input}DelAll();" class="layui-btn layui-btn-primary">清除</a></c:if>
<script type="text/javascript">
	function ${input}FinderOpen(){
	    //<c:if test="${type eq 'thumb'}"><c:set var="ctype" value="images"/></c:if><c:if test="${type ne 'thumb'}"><c:set var="ctype" value="${type}"/></c:if>
		var date = new Date(), year = date.getFullYear(), month = (date.getMonth()+1)>9?date.getMonth()+1:"0"+(date.getMonth()+1);
		var url = "${ctxStatic}/ckfinder/ckfinder.html?type=${ctype}&start=${ctype}:${uploadPath}/"+year+"/"+month+
			"/&action=js&func=${input}SelectAction&thumbFunc=${input}ThumbSelectAction&cb=${input}Callback&dts=${type eq 'thumb'?'1':'0'}&sm=${selectMultiple?1:0}";
		windowOpen(url,"文件管理",1000,700);
		//top.$.jBox("iframe:"+url+"&pwMf=1", {title: "文件管理", width: 1000, height: 500, buttons:{'关闭': true}});
	}

	function ${input}SelectAction(fileUrl, data, allFiles){
		var url="", files=ckfinderAPI.getSelectedFiles();
		for(var i=0; i<files.length; i++){//<c:if test="${type eq 'thumb'}">
			url += files[i].getThumbnailUrl();//</c:if><c:if test="${type ne 'thumb'}">
			url += files[i].getUrl();//</c:if>
			if (i<files.length-1) url+="|";
		}//<c:if test="${selectMultiple}">
		$("#${input}").val($("#${input}").val()+($("#${input}").val(url)==""?url:"|"+url));//</c:if><c:if test="${!selectMultiple}">
		$("#${input}").val(url);//</c:if>
		${input}Preview();
		//top.$.jBox.close();
	}
	function ${input}ThumbSelectAction(fileUrl, data, allFiles){
		var url="", files=ckfinderAPI.getSelectedFiles();
		for(var i=0; i<files.length; i++){
			url += files[i].getThumbnailUrl();
			if (i<files.length-1) url+="|";
		}//<c:if test="${selectMultiple}">
		$("#${input}").val($("#${input}").val()+($("#${input}").val(url)==""?url:"|"+url));//</c:if><c:if test="${!selectMultiple}">
		$("#${input}").val(url);//</c:if>
		${input}Preview();
		//top.$.jBox.close();
	}

	function ${input}Callback(url){
        <c:if test="${selectMultiple}">
            $("#${input}").val($("#${input}").val()+($("#${input}").val(url)==""?url:"|"+url));
        </c:if>
        <c:if test="${!selectMultiple}">
            $("#${input}").val(url);
        </c:if>
        ${input}Preview();
	}

	function ${input}Del(obj){
		var url = $(obj).prev().attr("url");
		$("#${input}").val($("#${input}").val().replace("|"+url,"","").replace(url+"|","","").replace(url,"",""));
		${input}Preview();
	}
	function ${input}DelAll(){
		$("#${input}").val("");
		${input}Preview();
	}
	function ${input}Preview(){
		var li, urls = $("#${input}").val().split("|");
		$("#${input}Preview").children().remove();
		for (var i=0; i<urls.length; i++){
			if (urls[i]!=""){
                <c:if test="${type eq 'thumb' || type eq 'images'}">
				li = "<li><img src=\""+ctxFile+urls[i]+"\" url=\""+urls[i]+"\" style=\"max-width:${empty maxWidth ? 200 : maxWidth}px;max-height:${empty maxHeight ? 200 : maxHeight}px;_height:${empty maxHeight ? 200 : maxHeight}px;border:0;padding:3px;\">";
                </c:if>
                <c:if test="${type ne 'thumb' && type ne 'images'}">
				li = "<li><a href=\""+ctxFile+urls[i]+"\" url=\""+urls[i]+"\" target=\"_blank\">"+decodeURIComponent(urls[i].substring(urls[i].lastIndexOf("/")+1))+"</a>";
                </c:if>
				li += "&nbsp;&nbsp;<c:if test="${!readonly}"><a href=\"javascript:\" onclick=\"${input}Del(this);\">×</a></c:if></li>";
				$("#${input}Preview").append(li);
			}
		}
		if ($("#${input}Preview").text() == ""){
			$("#${input}Preview").html("<li style='list-style:none;padding-top:5px;'>无</li>");
		}
	}

	${input}Preview();

    function ${input}UploadFile(){
        var formData = new FormData();
        formData.append("uploadFile", $("#${input}_upload_file")[0].files[0]);
        $.support.cors = true;
        $.ajax({
            type: "POST",
            cache: false,
            dataType: "text",
            data: formData,
            processData: false,
            contentType: false,
            url: ctx + "/file/upload?date=" + new Date().getTime(),  //这里是网址
            timeout: 5000, //超时时间
            success: function (data) {
                try {
                    var obj = JSON.parse(data);
                    if (obj.status == "0") {
                        ${input}Callback(obj.data);
                    }
                } catch (e) {
                    console.log(e.message);
                }
            },
            error: function (err) {
                alert(err);
            }
        });
    }
</script>