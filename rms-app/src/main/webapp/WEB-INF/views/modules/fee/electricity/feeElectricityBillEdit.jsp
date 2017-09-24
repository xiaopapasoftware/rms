<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static"/>
<script type="text/javascript">var ctx = '${ctx}', ctxStatic = '${ctxStatic}';</script>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>电费账单录入</title>
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>

    <link rel="stylesheet" href="${ctxStatic}/layui/css/layui.css"/>
    <link rel="stylesheet" href="${ctxStatic}/xqsight/widget/font-awesome.css"/>
    <link rel="stylesheet" href="${ctxStatic}/xqsight/widget/widget.css"/>
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
                    <label class="layui-form-label">缴费年月</label>
                    <div class="layui-input-inline date" style="width: 100px;">
                        <input type="text" id="feeDate" name="feeDate" placeholder="yyyy-MM"
                               class="layui-input">
                    </div>
                </div>

                <div class="layui-inline">
                    <label class="layui-form-label">区域选择</label>
                    <div class="layui-input-inline" style="width: 100px;">
                        <input type="text" id="areaId" name="areaId" placeholder="区域选择"
                               class="layui-input">
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">小区选择</label>
                    <div class="layui-input-inline m-large">
                        <select id="propertyId" name="propertyId" lay-search="" placeholder="小区选择">
                            <option value="">请选择</option>
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">楼宇选择</label>
                    <div class="layui-input-inline m-large">
                        <select id="buildId" name="buildId" lay-search="" placeholder="楼宇选择">
                            <option value="">请选择</option>
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">房号</label>
                    <div class="layui-input-inline m-large">
                        <input type="text" name="houseNo" placeholder="房号" class="layui-input">
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">审核状态</label>
                    <div class="layui-input-inline m-large">
                        <select id="status" name="status" lay-search="" placeholder="审核状态">
                            <option value="">请选择</option>
                            <option value="0">待提交</option>
                            <option value="1">待审核</option>
                            <option value="2">审核通过</option>
                            <option value="3">审核驳回</option>
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">是否已录</label>
                    <div class="layui-input-inline m-large">
                        <select id="isRecord" name="isRecord" lay-search="" placeholder="是否已录">
                            <option value="">请选择</option>
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
                    <col width="120">
                    <col width="60">
                    <col width="60">
                    <col width="60">
                    <col width="110">
                    <col width="110">
                    <col width="110">
                    <col width="100">
                    <col width="100">
                    <col width="100">
                    <col width="100">
                    <col width="100">
                    <col width="120">
                </colgroup>
                <thead>
                <tr>
                    <th>合同编号</th>
                    <th>合同名称</th>
                    <th>装修结构</th>
                    <th>楼号</th>
                    <th>房号</th>
                    <th>室号</th>
                    <th>签订日期</th>
                    <th>生效日期</th>
                    <th>到期日期</th>
                    <th>付费方式</th>
                    <th>月租金</th>
                    <th>押金</th>
                    <th>水电押金</th>
                    <th>智表首付金</th>
                    <th>合同业务状态</th>
                    <!--<th>承租人</th>
                    <th>承租人电话</th>
                    <th>入住人</th>
                    <th>入住人联系电话</th>
                    <th>入住人身份证号码</th>
                    <th>租赁顾问</th>
                    <th>服务管家</th>
                    <th>合同来源</th>
                    <th>公共事业费付费方式</th>
                    <th>水费(元/每月)</th>
                    <th>宽带费(元/每月)</th>
                    <th>有线月租(元/每月)</th>
                    <th>入住水表数</th>
                    <th>入住峰电数</th>
                    <th>入住谷电数</th>
                    <th>入住总电数</th>
                    <th>入住分电表数</th>
                    <th>入住煤总表数</th>
                    <th>备注</th>-->
                </tr>
                </thead>
                <tbody id="contractContent">
                </tbody>
            </table>
            <div id="contractPage"></div>
        </div>
    </div>
</div>

<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
<script src="${ctxStatic}/layui/layui.js"></script>
<script src="${ctxStatic}/xqsight/widget/widgets.js"></script>
<script src="${ctxStatic}/modules/report/contractReport.js"></script>

<script id="contractTpl" type="text/html">
    {{#  layui.each(d.data, function(index, item){ }}
    <tr>
        <td>{{ item.contractCode }}</td>
        <td>{{ item.contractName }}</td>
        <td>{{ item.houseStyle }}</td>
        <td>{{ item.buildingName || '' }}</td>
        <td>{{ item.houseNo || '' }}</td>
        <td>{{ item.roomNo || '' }}</td>
        <td>{{ item.signDate || '' }}</td>
        <td>{{ item.startDate || '' }}</td>
        <td>{{ item.expiredDate || '' }}</td>
        <td>{{ item.payType }}</td>
        <td>{{ item.rental || '' }}</td>
        <td>{{ item.depositAmount || '' }}</td>
        <td>{{ item.depositElectricAmount || '' }}</td>
        <td>{{ item.eleRechargeAmount || '' }}</td>
        <td>{{ item.contractBusiStatusName || ''}}</td>
        <!--<td>{{ item.tenantNameLead || '' }}</td>
        <td>{{ item.cellPhoneLead || '' }}</td>-->
    </tr>
    {{#  }); }}
    {{#  if(d.data.length === 0){ }}
    无数据
    {{#  } }}
</script>

<script id="dictValueTpl" type="text/html">
    <option value="">合同业务状态</option>
    {{#  layui.each(d.data, function(index, item){ }}
    <option value="{{ item.value }}">{{ item.label }}</option>
    {{#  }); }}
</script>
<script id="projectValueTpl" type="text/html">
    <option value="">物业项目</option>
    {{#  layui.each(d.data, function(index, item){ }}
    <option value="{{ item.id }}">{{ item.projectName }}</option>
    {{#  }); }}
</script>
</body>
</html>