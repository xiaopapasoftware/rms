<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static"/>
<script type="text/javascript">var ctx = '${ctx}', ctxStatic='${ctxStatic}';</script>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>合同报表</title>
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>

    <link rel="stylesheet" href="${ctxStatic}/layui/css/layui.css"/>
    <link rel="stylesheet" href="${ctxStatic}/xqsight/widget/font-awesome.css"/>
    <link rel="stylesheet" href="${ctxStatic}/xqsight/widget/widget.css"/>

    <style>
        .layui-form-item .m-large {
            width: 224px !important;
        }
        table {
          table-layout:fixed;
        }

        table td{
            word-wrap:break-word;
            word-break:break-all;
            align-content: center;
        }
        table thead td{
            align-content: center;
        }
    </style>
</head>

<body>

<div class="widget-box transparent widget-container-col">
    <div class="widget-header">
        <h4 class="widget-title lighter">
            <i class="ace-icon fa fa-filter"></i>查询条件</h4>
        <div class="widget-toolbar">
            <a href="javascript:void(0);" data-action="collapse">
                <i class="ace-icon fa fa-chevron-up"></i>
            </a>
        </div>
        <div class="widget-toolbar">
            <a href="javascript:void(0);" id="btn-undo" class="white">
                <i class="ace-icon fa fa-undo"></i> 重置
            </a>
        </div>
        <div class="widget-toolbar no-border">
            <a href="javascript:void(0);" id="btn-search" class="white btn-search">
                <i class="ace-icon fa fa-search"></i> 查询
            </a>
        </div>

    </div>
    <div class="widget-body">
        <div class="widget-main padding-6 no-padding-left no-padding-right">
            <form id="queryFrom" class="layui-form layui-form-item layui-form-pane">

                <div class="layui-inline">
                    <label class="layui-form-label">到期范围(天)</label>
                    <div class="layui-input-inline" style="width: 100px;">
                        <input type="number" id="freeDayBegin" name="freeDayBegin" value="" class="layui-input">
                    </div>
                    <div class="layui-form-mid">-</div>
                    <div class="layui-input-inline" style="width: 100px;">
                        <input type="number" id="freeDayEnd" name="freeDayEnd" value="15" class="layui-input">
                    </div>
                </div>

                <div class="layui-inline">
                    <label class="layui-form-label">合同编号</label>
                    <div class="layui-input-inline m-large">
                        <input type="text" name="contractCode" placeholder="合同编号" class="layui-input">
                    </div>
                </div>

                <div class="layui-inline">
                    <label class="layui-form-label">物业项目</label>
                    <div class="layui-input-inline m-large">
                        <select id="projectValue" name="projectValue" lay-search="" placeholder="合同业务状态">
                            <option value="">物业项目</option>
                        </select>
                    </div>
                </div>

                <div class="layui-inline">
                    <label class="layui-form-label">服务管家</label>
                    <div class="layui-input-inline m-large">
                        <input type="text" name="serverName" placeholder="服务管家" class="layui-input">
                    </div>
                </div>

                <div class="layui-inline">
                    <label class="layui-form-label">楼号</label>
                    <div class="layui-input-inline m-large">
                        <input type="text" name="buildingName" placeholder="楼号" class="layui-input">
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">房号</label>
                    <div class="layui-input-inline m-large">
                        <input type="text" name="houseNo" placeholder="房号" class="layui-input">
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">室号</label>
                    <div class="layui-input-inline m-large">
                        <input type="text" name="roomNo" placeholder="室号" class="layui-input">
                    </div>
                </div>

            </form>
        </div>
    </div>
</div>


<div class="widget-box transparent widget-container-col">
    <div class="widget-header">
        <h4 class="widget-title lighter"><i class="ace-icon fa fa-th"></i>合同列表 (单位:元)</h4>
        <div class="widget-toolbar no-border">
            <a href="javascript:void(0);" id="btn-export" class="white">
                <i class="ace-icon fa fa-download"></i> 导出
            </a>
        </div>
    </div>
    <div class="widget-body">
        <div class="widget-main padding-6 no-padding-left no-padding-right">
            <table class="layui-table" lay-even>
                <colgroup>
                    <col width="120">
                    <col width="120">
                    <col width="80">
                    <col width="100">
                    <col width="60">
                    <col width="60">
                    <col width="60">
                    <col width="80">
                    <col width="100">
                    <col width="120">
                    <col width="120">
                    <col width="120">
                    <col width="110">
                    <col width="110">
                    <col width="100">
                    <col width="100">
                    <col width="110">
                    <col width="60">
                </colgroup>
                <thead>
                    <tr>
                        <th>合同编号</th>
                        <th>合同名称</th>
                        <th>出租方式</th>
                        <th>物业项目</th>
                        <th>楼号</th>
                        <th>房号</th>
                        <th>室号</th>
                        <th>服务管家</th>
                        <th>承租人</th>
                        <th>承租人电话</th>
                        <th>入住人</th>
                        <th>入住人电话</th>
                        <th>生效日期</th>
                        <th>到期日期</th>
                        <th>付费方式</th>
                        <th>月租金</th>
                        <th>上次支付到期日</th>
                        <th>到期剩余天数</th>
                    </tr>
                </thead>
                <tbody id="rentDueUrgeContent">
                </tbody>
            </table>
            <div id="rentDueUrgePage"></div>
        </div>
    </div>
</div>

<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
<script src="${ctxStatic}/layui/layui.js"></script>
<script src="${ctxStatic}/xqsight/widget/widgets.js"></script>
<script src="${ctxStatic}/modules/report/rentDueUrgeReport.js"></script>

<script id="rentDueUrgeTpl" type="text/html">
    {{#  layui.each(d.data, function(index, item){ }}
    <tr>
        <td>{{ item.contractCode }}</td>
        <td>{{ item.contractName }}</td>
        <td>{{ item.rentMode }}</td>
        <td>{{ item.projectName }}</td>
        <td>{{ item.buildingName || '' }}</td>
        <td>{{ item.houseNo || '' }}</td>
        <td>{{ item.roomNo || '' }}</td>
        <td>{{ item.serverName || '' }}</td>
        <td>{{ item.tenantNameLead || '' }}</td>
        <td>{{ item.cellPhoneLead || '' }}</td>
        <td>{{ item.tenantName || '' }}</td>
        <td>{{ item.cellPhone || '' }}</td>
        <td>{{ item.startDate || '' }}</td>
        <td>{{ item.expiredDate || '' }}</td>
        <td>{{ item.payType }}</td>
        <td>{{ item.rental || '' }}</td>
        <td>{{ item.prePayDate || '' }}</td>
        <td>{{ item.freeDay || '' }}</td>
    </tr>
    {{#  }); }}
    {{#  if(d.data.length === 0){ }}
    无数据
    {{#  } }}
</script>

<script id="projectValueTpl" type="text/html">
    <option value="">物业项目</option>
    {{#  layui.each(d.data, function(index, item){ }}
    <option value="{{ item.id }}">{{ item.projectName }}</option>
    {{#  }); }}
</script>
</body>
</html>