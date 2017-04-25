<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static"/>
<script type="text/javascript">var ctx = '${ctx}', ctxStatic='${ctxStatic}';</script>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>出账财务报表</title>
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
                <input type="hidden" name="tradeDirection" value="0" title="出账">
                <div class="layui-inline">
                    <label class="layui-form-label">收据日期</label>
                    <div class="layui-input-inline date" style="width: 100px;">
                        <input type="text" id="receiptDateBegin" name="filter_GES_receipt_date" placeholder="开始日期"
                               class="layui-input">
                    </div>
                    <div class="layui-form-mid">-</div>
                    <div class="layui-input-inline" style="width: 100px;">
                        <input type="text" id="receiptDateEnd" name="filter_LES_receipt_date" placeholder="结束日期" class="layui-input">
                    </div>
                </div>

                <div class="layui-inline">
                    <label class="layui-form-label">物业项目</label>
                    <div class="layui-input-inline m-large">
                        <select id="projectValue" name="filter_EQS_tpp.id" lay-search="" placeholder="合同业务状态">
                            <option value="">物业项目</option>
                        </select>
                    </div>
                </div>

                <div class="layui-inline">
                    <label class="layui-form-label">楼号</label>
                    <div class="layui-input-inline m-large">
                        <input type="text" name="filter_LIKES_tb.building_name" placeholder="楼号" class="layui-input">
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">房号</label>
                    <div class="layui-input-inline m-large">
                        <input type="text" name="filter_LIKES_th.house_no" placeholder="房号" class="layui-input">
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">室号</label>
                    <div class="layui-input-inline m-large">
                        <input type="text" name="filter_LIKES_tr.room_no" placeholder="室号" class="layui-input">
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>


<div class="widget-box transparent widget-container-col">
    <div class="widget-header">
        <h4 class="widget-title lighter"><i class="ace-icon fa fa-th"></i>出账流水列表 (单位:元)</h4>
        <div class="widget-toolbar no-border">
            <a href="javascript:void(0);" id="btn-in-export" class="white">
                <i class="ace-icon fa fa-download"></i> 导出
            </a>
        </div>
    </div>
    <div class="widget-body">
        <div class="widget-main padding-6 no-padding-left no-padding-right">
            <table id="in-table" class="layui-table" lay-even>
                <colgroup>
                    <col width="120">
                    <col width="120">
                    <col width="80">
                    <col width="80">
                    <col width="80">
                    <col width="100">
                    <col width="100">
                    <col width="100">
                    <col width="100">
                    <col width="100">
                    <col width="100">
                    <col width="100">
                    <col width="100">
                    <col width="100">
                    <col width="100">
                </colgroup>
                <thead>
                <tr>
                    <th>收据日期</th>
                    <th>物业项目</th>
                    <th>楼号</th>
                    <th>房号</th>
                    <th>室号</th>
                    <th>总金额</th>
                    <th>应退房租押金</th>
                    <th>应退水电费押金</th>
                    <th>应退房租金额</th>
                    <th>应退电费余额</th>
                    <th>应退水费余额</th>
                    <th>应退宽带费余额</th>
                    <th>应退有线电视费余额</th>
                    <th>早退违约金</th>
                    <th>服务费</th>
                    <th>其他</th>
                    <th>备注</th>
                </tr>
                </thead>
                <tbody id="financeContent">
                </tbody>
                <tfoot>
                    <tr>
                        <th colspan="5">合计</th>
                        <th id="outTotalAmount"></th>
                        <th id="houseDepositRefund"></th>
                        <th id="waterDepositRefund"></th>
                        <th id="houseRefund"></th>
                        <th id="eleRefund"></th>
                        <th id="waterRefund"></th>
                        <th id="netRefund"></th>
                        <th id="tvRefund"></th>
                        <th id="unagreeRefund"></th>
                        <th id="serviceRefund"></th>
                        <th id="otherRefund"></th>
                        <th ></th>
                    </tr>
                </tfoot>
            </table>
            <div id="financePage"></div>
        </div>
    </div>
</div>

<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
<script src="${ctxStatic}/layui/layui.js"></script>
<script src="${ctxStatic}/xqsight/widget/widgets.js"></script>
<script src="${ctxStatic}/xqsight/moment.js"></script>
<script src="${ctxStatic}/modules/report/financeOutReport.js"></script>

<script id="financeTpl" type="text/html">
    {{#  layui.each(d.dataList, function(index, item){ }}
    <tr>
        <td>{{ item.receiptDate }}</td>
        <td>{{ item.projectName }}</td>
        <td>{{ item.buildingName || '' }}</td>
        <td>{{ item.houseNo || '' }}</td>
        <td>{{ item.roomNo || '' }}</td>
        <td>{{ item.totalAmount }}</td>
        <td>{{ item.houseDepositRefund || '' }}</td>
        <td>{{ item.waterDepositRefund || '' }}</td>
        <td>{{ item.houseRefund || ''}}</td>
        <td>{{ item.eleRefund || '' }}</td>
        <td>{{ item.waterRefund || '' }}</td>
        <td>{{ item.netRefund || '' }}</td>
        <td>{{ item.tvRefund || '' }}</td>
        <td>{{ item.unagreeRefund || '' }}</td>
        <td>{{ item.serviceRefund || '' }}</td>
        <td>{{ item.otherRefund || '' }}</td>
        <td>{{ item.remark || '' }}</td>
    </tr>
    {{#  }); }}
    {{#  if(d.dataList.length === 0){ }}
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