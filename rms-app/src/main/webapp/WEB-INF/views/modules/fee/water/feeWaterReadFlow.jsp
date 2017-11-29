<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="/WEB-INF/tlds/shiros.tld" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static"/>
<script type="text/javascript">var ctx = '${ctx}', ctxStatic = '${ctxStatic}';</script>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>水抄表流水</title>
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>

    <link rel="stylesheet" href="${ctxStatic}/layui/css/layui.css"/>
    <link rel="stylesheet" href="${ctxStatic}/xqsight/widget/font-awesome.css"/>
    <link rel="stylesheet" href="${ctxStatic}/xqsight/widget/widget.css"/>

    <style>
        .layui-table-cell .layui-form-checkbox {
            top: 5px !important;
        }

        .tangchao .layui-form-item {
            height: 30px !important;
        }

        .tangchao .layui-input, .layui-select, .layui-textarea {
            height: 30px;
        }

        .tangchao .button {
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
                    <div class="layui-input-inline m-large" style="width: 200px;">
                        <input type="text" id="waterReadDates" name="waterReadDates" placeholder="抄表日期" readonly
                               class="layui-input">
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="widget-box transparent widget-container-col">
    <div class="widget-header tangchao">
        <shiro:hasPermission name="fee:water:read:add">
        <div class="widget-toolbar no-border pull-left">
            <a href="javascript:void(0);" id="btn-add" class="button">
                录入
            </a>
        </div>
        </shiro:hasPermission>
    </div>
    <div class="widget-body">
        <div class="widget-main padding-6 no-padding-left no-padding-right">
            <table class="layui-hide" id="waterReadFlowTable" lay-filter="waterReadFlow">
            </table>
        </div>
    </div>
</div>


<div id="addDiv" class="tangchao" hidden>
    <form class="layui-form" id="addFeeGasBillForm" action="">
        <div class="layui-form-item" style="margin-top: 15px;">
            <label class="layui-form-label">抄表日期</label>
            <div class="layui-input-inline">
                <input type="text" id="waterReadDate" required lay-verify="required" readonly placeholder="抄表日期"
                       class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">区域项目</label>
                <div class="layui-input-inline" style="width: 110px;">
                    <select id="areaId" name="areaId" lay-filter="areaId" placeholder="区域" lay-search>
                        <option value="">区域</option>
                    </select>
                </div>
                <div class="layui-input-inline" style="width: 90px;">
                    <select id="projectId" name="projectId" lay-filter="projectId" placeholder="物业项目" lay-search>
                        <option value="">物业项目</option>
                    </select>
                </div>
            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">楼宇房屋</label>
                <div class="layui-input-inline" style="width: 100px;">
                    <select id="buildingId" name="buildingId" lay-filter="buildingId" placeholder="楼宇" lay-search>
                        <option value="">楼宇</option>
                    </select>
                </div>
                <div class="layui-input-inline" style="width: 100px;">
                    <select id="houseId" name="houseId" lay-filter="houseId" placeholder="房屋" lay-search>
                        <option value="">房屋</option>
                    </select>
                </div>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">水抄表数</label>
            <div class="layui-input-inline">
                <input type="number" id="waterDegree" placeholder="抄表数"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <a href="javascript:void(0);" id="btn-save" lay-filter="addWaterBill" lay-submit class="button">保存并继续</a>
                <a href="javascript:void(0);" id="btn-view" class="button">
                    统计查看
                </a>
            </div>
        </div>
    </form>
</div>

<script type="text/html" id="toolBar">
    <shiro:hasPermission name="fee:water:read:add">
    <a class="layui-btn layui-btn-mini" lay-event="edit">编辑</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="fee:water:read:delete">
    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="del">删除</a>
    </shiro:hasPermission>
</script>

<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
<script src="${ctxStatic}/layui/layui.js"></script>
<script src="${ctxStatic}/xqsight/widget/widgets.js"></script>
<script src="${ctxStatic}/modules/fee/water/feeWaterReadFlow.js"></script>
</body>
</html>