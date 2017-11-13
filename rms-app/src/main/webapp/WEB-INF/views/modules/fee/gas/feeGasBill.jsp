<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static"/>
<script type="text/javascript">var ctx = '${ctx}', ctxStatic = '${ctxStatic}';</script>

<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>煤气账单录入</title>
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>

    <link rel="stylesheet" href="${ctxStatic}/layui/css/layui.css"/>
    <link rel="stylesheet" href="${ctxStatic}/xqsight/widget/font-awesome.css"/>
    <link rel="stylesheet" href="${ctxStatic}/xqsight/widget/widget.css"/>

    <script src="${ctxStatic}/lodop6/LodopFuncs.js"></script>
    <object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>
        <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
    </object>

    <style>
        .layui-table-cell .layui-form-checkbox {
            top: 5px !important;
        }
        .tangchao .layui-form-item{
            height: 30px !important;
        }
        .tangchao .layui-input, .layui-select, .layui-textarea {
            height: 30px;
        }
        .tangchao .button{
            border: 1px solid #009688;
            height: 25px;
            line-height: 25px;
            min-width: 50px;
            padding: 0px 10px;
            display: inline-block;
            font-size: 12px;
            text-align: center;
            border-radius: 6px;
            color: #009688;
            background-color: #FFFFFF;
        }
        .widget-body .layui-input, .layui-select, .layui-textarea {
            height: 30px;
        }
        .widget-body .layui-form-item .layui-input-inline {
            width: 150px;
        }
        .widget-body .layui-form-item .layui-inline {
            margin-right: 0px;
        }

        .autocomplete-suggestions { border: 1px solid #999; background: #FFF; overflow: auto; }
        .autocomplete-suggestion { padding: 2px 5px; white-space: nowrap; overflow: hidden; }
        .autocomplete-selected { background: #F0F0F0; }
        .autocomplete-suggestions strong { font-weight: normal; color: #3399FF; }
        .autocomplete-group { padding: 2px 5px; }
        .autocomplete-group strong { display: block; border-bottom: 1px solid #000; }
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
                    <div class="layui-input-inline m-large">
                        <input type="text" id="feeDate" name="feeDate" placeholder="缴费年月" readonly
                               class="layui-input">
                    </div>
                </div>

                <div class="layui-inline">
                    <div class="layui-input-inline m-large">
                        <input type="text" id="houseNum" name="houseNum" placeholder="户号"
                               class="layui-input">
                    </div>
                </div>

                <div class="layui-inline">
                    <div class="layui-input-inline m-large">
                        <select id="area" name="area" lay-filter="area" placeholder="区域" lay-search>
                            <option value="">区域</option>
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <div class="layui-input-inline m-large">
                        <select id="project" name="project" lay-filter="project" placeholder="物业项目" lay-search>
                            <option value="">物业项目</option>
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <div class="layui-input-inline m-large">
                        <select id="building" name="building" lay-filter="building" placeholder="楼宇" lay-search>
                            <option value="">楼宇</option>
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <div class="layui-input-inline m-large">
                        <select id="house" name="house" lay-filter="house" placeholder="房屋" lay-search>
                            <option value="">房屋</option>
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <div class="layui-input-inline m-large">
                        <select id="status" name="status" placeholder="审核状态">
                            <option value="">审核状态</option>
                            <option value="0">待提交</option>
                            <option value="1">待审核</option>
                            <option value="2">审核通过</option>
                            <option value="3">审核驳回</option>
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <div class="layui-input-inline m-large">
                        <select id="isRecord" name="isRecord" placeholder="是否已录">
                            <option value="">是否已录</option>
                            <option value="0">已录</option>
                            <option value="1">未录</option>
                        </select>
                    </div>
                </div>

            </form>
        </div>
    </div>
</div>

<div class="widget-box transparent widget-container-col">
    <div class="widget-header tangchao">
        <div class="widget-toolbar no-border pull-left">
            <a href="javascript:void(0);" id="btn-add" class="button">
                录入
            </a>
        </div>
        <div class="widget-toolbar no-border pull-left">
            <a href="javascript:void(0);" id="btn-commit" class="button">
                提交审批
            </a>
        </div>
        <div class="widget-toolbar no-border pull-left">
            <a href="javascript:void(0);" id="btn-pass" class="button">
                同意
            </a>
        </div>
        <div class="widget-toolbar no-border pull-left">
            <a href="javascript:void(0);" id="btn-reject" class="button">
                驳回
            </a>
        </div>
        <div class="widget-toolbar no-border pull-left">
            <a href="javascript:void(0);" id="btn-print" class="button">
                打印
            </a>
        </div>

        <div class="widget-toolbar no-border">
            总额:<span id="totalAmount">0.00</span>元
        </div>
    </div>
    <div class="widget-body">
        <div class="widget-main padding-6 no-padding-left no-padding-right">
            <table class="layui-hide" id="gasBillTable" lay-filter="gasBill">
            </table>
        </div>
    </div>
</div>


<div id="addDiv" class="tangchao" hidden >
    <form class="layui-form" id="addFeeEleBillForm" action="">
        <div class="layui-form-item" style="margin-top: 15px;">
            <label class="layui-form-label">账期年月</label>
            <div class="layui-input-inline">
                <input type="text" id="gasBillDate" required lay-verify="required" readonly placeholder="账期年月" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址</label>
            <div class="layui-input-inline">
                <input type="text" id="houseAddress" readonly placeholder="地址" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">户&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</label>
            <div class="layui-input-inline">
                <input type="number" id="houseGasNum" required lay-verify="required" placeholder="户号" class="layui-input">
                <input type="text" id="houseId" name="houseId" hidden>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">账单度数</label>
            <div class="layui-input-inline">
                <input type="number" id="gasDegree" required lay-verify="required" placeholder="账单度数" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">账单金额</label>
            <div class="layui-input-inline">
                <input type="number" id="gasBillAmount" required lay-verify="required" placeholder="账单金额" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <a href="javascript:void(0);" id="btn-save" lay-filter="addGasBill" lay-submit class="button">保存并继续</a>
                <a href="javascript:void(0);" id="btn-view" class="button">
                    统计查看
                </a>
            </div>
        </div>
    </form>
</div>

<script type="text/html" id="toolBar">
    {{# var status=d.billStatus }}
    {{# if (status==null || status=='0' || status=='3'){ }}
    <a class="layui-btn layui-btn-mini" lay-event="edit">编辑</a>
    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="del">删除</a>
    {{# } }}
</script>


<script id="printTableTpl" type="text/html">
    <style type="text/css">
        table.gridtable {
            font-family: verdana,arial,sans-serif;
            font-size:11px;
            color:#333333;
            border-width: 1px;
            border-color: #666666;
            border-collapse: collapse;
        }
        table.gridtable th {
            border-width: 1px;
            padding: 8px;
            border-style: solid;
            border-color: #666666;
            background-color: #dedede;
        }
        table.gridtable td {
            border-width: 1px;
            padding: 8px;
            border-style: solid;
            border-color: #666666;
            background-color: #ffffff;
        }
    </style>

    <table class="gridtable">
        <tr>
            <td>区域</td>
            <td>物业项目</td>
            <td>地址</td>
            <td>楼宇</td>
            <td>房号</td>
            <td>户号</td>
            <td>账期</td>
            <td>仪表数</td>
            <td>金额</td>
            <td>状态</td>
        </tr>
        {{# layui.each(d.data, function(index, item){ }}
        <tr>
            <td>{{ item.areaName }}</td>
            <td>{{ item.projectName }}</td>
            <td>{{ item.projectAddress }}</td>
            <td>{{ item.buildingName || '' }}</td>
            <td>{{ item.houseNo || '' }}</td>
            <td>{{ item.houseEleNum || '' }}</td>
            <td>{{ item.gasBillDate || '' }}</td>
            <td>{{ item.gasDegree || '' }}</td>
            <td>{{ item.gasBillAmount || '' }}</td>
            <td>{{ item.billStatusName || ''}}</td>
        </tr>
        {{# }); }}
        {{# if(d.data.length === 0){ }}
        无数据
        {{# } }}
    </table>
</script>

<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
<script src="${ctxStatic}/layui/layui.js"></script>
<script src="${ctxStatic}/xqsight/widget/widgets.js"></script>
<script src="${ctxStatic}/jquery-autocomplete/jquery.autocomplete.js"></script>
<script src="${ctxStatic}/modules/fee/gas/feeGasBill.js"></script>
</body>
</html>