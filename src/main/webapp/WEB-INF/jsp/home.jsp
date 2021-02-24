<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE">
	<title>机械振动信号分析系统</title>
	<link rel="stylesheet" type="text/css" href="${ctx}/js/jquery-easyui-1.7.0/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/js/jquery-easyui-1.7.0/themes/icon.css">
	<%--<link rel="stylesheet" type="text/css" href="${ctx}/js/jquery-easyui-1.7.0/demo/demo.css">--%>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/styleExp.css">
	<%--<link rel="stylesheet" type="text/css" href="${ctx}/css/sidemenu.css">--%>
	<script type="text/javascript" src="${ctx}/js/jquery-easyui-1.7.0/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/jquery-easyui-1.7.0/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/date.format.js"></script>
	<script type="text/javascript" src="${ctx}/js/server/globe.js"></script>
	<script type="text/javascript" src="${ctx}/js/jquery-easyui-1.7.0/locale/easyui-lang-zh_CN.js"></script>
	<c:set var="windowsProperties" value="collapsible:false,minimizable:false,maximizable:false,closable:false"/>

	<style type="text/css">
		body {
			text-align: center;
			<%--background: #006eb0 url('${ctx}/image/lgbg.png') center top no-repeat;clear: both;margin: 0 auto;width: 100%;min-width: 960px;--%>
		}
	</style>
</head>

<body class="easyui-layout">
<%--<div id="layout" class="easyui-layout" style="width:100%;height:100%;">--%>
<div data-options="region:'west'" style="width:20.3%;height: 100%;background: url('${ctx}/image/lgbgnoborderleft.jpg') center top no-repeat;clear: both;margin: 0 auto;background-size: cover"></div>
<div data-options="region:'east'" style="width:20.3%;height: 100%;background: url('${ctx}/image/lgbgnoborderleft.jpg') center top no-repeat;clear: both;margin: 0 auto;background-size: cover"></div>


<div id="center" data-options="region:'center'" style="width:59.4%;height: 100%;">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north'" style="overflow:hidden;width: 62%;height:15.5%;border-color: #95b8e7">
			<div id="imageTitle" class="im">
				<ul>
					<li>
						<label><%=session.getAttribute("userName")%></label>
						<a href="javascript:jump2Login(this)">退出</a>
					</li>
				</ul>
			</div>
		</div>
		<div id="west" data-options="region:'west'" style="width:224px;height: 100%;">
			<!--sideMenu-->
			<div id="sm" class="easyui-sidemenu" style="width: 99.5%">
			</div>
		</div>
		<div id="innercenter" data-options="region:'center'" style="width:80%;">
			<!--Tabs-->
			<div id="tabs" class="easyui-tabs" data-options="fit:true"></div>
		</div>

	</div>
</div>

<div id="cover" style="display: none">
	<div id="enterExpName" class="easyui-window" title="请输入试验名" data-options="modal:true,iconCls:'icon-save',closed:true,${windowsProperties}" style="width:380px;height:200px;padding:10px;">
		<table cellpadding="15" style = "margin-left: 3%;">
			<tr>
				<td>
					<label>请输入试验名：<input id = "typeinExpName"  type="text" name="typeinExpName" maxlength="20"/></label>
				</td>
			</tr>
			<tr>
				<td align="right">
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openExpTab()">确定</a>
					<a style="margin-left: 20px" href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#enterExpName').window('close')">取消</a>
				</td>
			</tr>
		</table>
	</div>
</div>
<div id="choseExp" class="easyui-window" title="请选择需要打开的试验" data-options="modal:true,iconCls:'icon-save',closed:true,${windowsProperties}" style="width:800px;height:600px;padding:10px;">
	<table id="expTable" style="width: 100%; height: 90%"></table>
	<div align="right" style="margin-top: 1%">
		<a href="javascript:void(0)" class="easyui-linkbutton" style="margin-right: 50px" onclick="getSelectedExp()" text="打开"></a>

		<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="$('#choseExp').window('close')" text="取消"></a>
	</div>
</div>

<div id="authorizeExp" class="easyui-window" title="请选择需要授权的试验" data-options="modal:true,iconCls:'icon-save',closed:true,${windowsProperties}" style="width:800px;height:600px;padding:10px;">
	<table id="expTableAu" style="width: 100%; height: 90%"></table>
	<div align="right" style="margin-top: 1%">
		<a href="javascript:void(0)" class="easyui-linkbutton" style="margin-right: 50px" onclick="getExp2Authorize()" text="授权"></a>

		<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="$('#authorizeExp').window('close')" text="取消"></a>
	</div>
</div>

<div id="chooseUser2Au" class="easyui-window" title="请选择被授权的用户" data-options="modal:true,iconCls:'icon-save',closed:true,${windowsProperties}" style="width:400px;height:500px;padding:10px;">
	<table id="userTable" style="width: 100%; height: 90%"></table>
	<div align="right" style="margin-top: 1%">
		<a href="javascript:void(0)" class="easyui-linkbutton" style="margin-right: 50px" onclick="authorizeUser()" text="授权"></a>

		<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="$('#chooseUser2Au').window('close')" text="取消"></a>
	</div>
</div>

<div id="delExp" class="easyui-window" title="删除试验" data-options="modal:true,iconCls:'icon-save',closed:true,${windowsProperties}" style="width:800px;height:600px;padding:10px;">
	<table id="delExpTable" style="width: 100%; height: 90%"></table>
	<div align="right" style="margin-top: 1%">
		<a href="javascript:void(0)" class="easyui-linkbutton" style="margin-right: 50px" onclick="confirmDelete()" text="删除"></a>

		<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="$('#delExp').window('close')" text="关闭"></a>
	</div>
</div>

<div id="coverColapse" style="display: none">
	<div id="enterDistinctExpName" class="easyui-window" title="请输入试验名" data-options="modal:true,iconCls:'icon-save',closed:true,${windowsProperties}" style="width:380px;height:200px;padding:10px;">
		<table cellpadding="15" style = "margin-left: 3%;">
			<tr>
				<td>
					<label>请输入试验名：<input id = "typeinDistinctExpName" name = "typeinDistinctExpName" maxlength="20"/></label>
				</td>
			</tr>
			<tr>
				<td align="right">
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reauthorizeUser()">确定</a>
					<a style="margin-left: 20px" href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#enterDistinctExpName').window('close')">取消</a>
				</td>
			</tr>
		</table>
	</div>
</div>





<script type="text/javascript">

    //itemGlobal是全局寄存的data树形json数据
    var itemGlobal;
    var selectedExp2Au;
    var expNames;
    //打开试验中避免打开相同的试验
	var selectedExp;
    //var openedTabs = new Map();
	//var openedClusters = [];
    //菜单数据
    var data = [
        {
        id:'unknownLoadPredict',
        text: '未知载荷的响应预测',
        state: 'open',
        children: [{
            id:'uLPTest',
            text: '模型精度测试',
            state: 'open',
            children: [{
                id:'uLPMExpAdd',
                isAdd: true,
                text: '新建试验',
                expClass:'ULPM',
                url:'${ctx}/uLPMExpAdd',
                state: 'open'
            },{
                id:'openULPMExp',
                isExpOpen:true,
                expClass:'ULPM',
                url:'${ctx}/openULPMExp',
                text: '打开试验'
            },{
                id:'delULPMExp',
                isExpDel:true,
                expClass:'ULPM',
                text: '删除试验'
            },{
                id:'authorizeUlpm',
                isExpAu:true,
                expClass:'ULPM',
                url:'${ctx}/openULPMExp',
                text: '试验授权'
            }]
        },{
            id:'unknownPointPredict',
            text: '未知测点预测',
            children:[{
                id:'uPPAdd',
                isAdd: true,
                text: '新建试验',
                url:'${ctx}/uLPExpAdd',
                expClass:"ULP"
            },{
                id:'openULPExp',
                isExpOpen:true,
                expClass:'ULP',
                url:'${ctx}/openULPExp',
                text: '打开试验'
            },{
                id:'delULPExp',
                isExpDel:true,
                expClass:'ULP',
                text: '删除试验'
            },{
                id:'authorizeUlp',
                isExpAu:true,
                expClass:'ULP',
                url:'${ctx}/openULPExp',
                text: '试验授权'
            }]
        }]
    },{
        id:'KnownLoadPredict',
        text: '已知载荷的响应预测',
        children: [{
            id:'KLPTest',
            text: '模型精度测试',
            children: [{
                id:'kLPMExpAdd',
                isAdd: true,
                text: '新建试验',
                expClass:'KLPM',
                url:'${ctx}/kLPMExpAdd'
            },{
                id:'openKLPMExp',
                isExpOpen:true,
                expClass:'KLPM',
                url:'${ctx}/openKLPMExp',
                text: '打开试验'
            },{
                id:'delKLPMExp',
                isExpDel:true,
                expClass:'KLPM',
                text: '删除试验'
            },{
                id:'authorizeKlpm',
                isExpAu:true,
                expClass:'KLPM',
                url:'${ctx}/openKLPMExp',
                text: '试验授权'
            }]
        },{
            id:'RSPredict',
            text: '响应信号预测',
            children:[{
                id:'kLPAdd',
                isAdd: true,
                text: '新建试验',
                url:'${ctx}/kLPExpAdd',
                expClass:"KLP"
            },{
                id:'openKLPExp',
                isExpOpen:true,
                expClass:'KLP',
                url:'${ctx}/openKLPExp',
                text: '打开试验'
            },{
                id:'delKLPExp',
                isExpDel:true,
                expClass:'KLP',
                text: '删除试验'
            },{
                id:'authorizeKlp',
                isExpAu:true,
                expClass:'KLP',
                url:'${ctx}/openKLPExp',
                text: '试验授权'
            }]
        }]
    },
        {
            id:'LoadRecognition',
            text: '载荷识别',
            children: [{
                id:'LoadModelTest',
                text: '模型精度测试',
                children: [{
                    id:'addExp_Load',
                    isAdd: true,
                    text: '新建试验',
                    url:'${ctx}/addLrExp',
                    expClass:'UKLT'
                    //未知载荷测试
                },{
                    id:'openLRExp',
                    text: '打开试验',
                    isExpOpen:true,
                    expClass:'UKLT',
                    url:'${ctx}/getExpByExpId'
                },{
                    id:'delLRExp',
                    text: '删除试验',
                    isExpDel:true,
                    expClass:'UKLT'
                },{
                    id:'authorizeUklt',
                    isExpAu:true,
                    expClass:'UKLT',
                    url:'${ctx}/getExpByExpId',
                    text: '试验授权'
                }]
            },{
                id:'LoadDataPredict',
                text: '未知载荷识别',
                children: [{
                    id:'addExp_LoadPred',
                    isAdd: true,
                    text: '新建试验',
                    url:'${ctx}/addLrpExp',
                    expClass:'UKLP'
                },{
                    id:'openLRPExp',
                    isExpOpen:true,
                    text: '打开试验',
                    expClass:'UKLP',
                    //未知载荷预测
                    url:'${ctx}/getExpByExpId'
                },
                    {
                        id:'delLRPExp',
                        text: '删除试验',
                        isExpDel:true,
                        expClass:'UKLP'
                    },{
                        id:'authorizeUklp',
                        isExpAu:true,
                        expClass:'UKLP',
                        url:'${ctx}/getExpByExpId',
                        text: '试验授权'
                    }]
            }]
        },
        {
            id:'maintain',
            text: '维护',
            children: [{
                id:'usrLMaintain',
                text: '用户表维护',
                ismaintain:true,
                url:'${ctx}/showUserInfo'
            },{
                id:'expLMaintain',
                text: '工程表维护',
                ismaintain:true,
                url:'${ctx}/showExpInfo'
            },{
                id:'exParaSearch',
                text:'试验参数查询',
                ismaintain:true,
                url:'${ctx}/showExpParaInfo'
            },{
                id:'dataStorage',
                text:'数据表维护',
                ismaintain:true,
                url:'${ctx}/showDataInfo'
            }]

        },
        {
            id:'timeSeriesBehavAnalysis',
            text: '时间序列数据分析',
            children: [{
                id:'behavAnalysis',
                text:'行为分析(层次聚类)',
                children:[{
                    id:'VDSI',
                    isNotExp:true,
                    text: '层次聚类(VDSI)',
                    url:'${ctx}/VDSI',
                    expClass:'VDSI'
                }]
            },
				{
                id:'correlationAnalysis',
                text:'相关性分析(层次聚类)',
                children:[
                    {
                        id:'tsCorAnalysisPos',
                        isNotExp:true,
                        text: '正相关',
                        url:'${ctx}/tsCorAnalysisPos',
                        expClass:'HCP'
                    },
                    {
                        id:'tsCorAnalysisNeg',
                        isNotExp:true,
                        text: '负相关',
                        url:'${ctx}/tsCorAnalysisNeg',
                        expClass:'HCN'
                    }
                ]
            },{
                id:'correlationAnalysisD',
                text:'相关性分析(密度聚类)',
                children:[{
                    id:'tsCorAnalysisDPos',
                    isNotExp:true,
                    text: ' 正相关 ',
                    url:'${ctx}/tsCorAnalysisDPos',
                    expClass:'DCP'
                },{
                    id:'tsCorAnalysisDNeg',
                    isNotExp:true,
                    text: ' 负相关 ',
                    url:'${ctx}/tsCorAnalysisDNeg',
                    expClass:'DCN'
                }]
            }]
        }
    ];

    $(window).resize(function() {
    });

    function openExpTable(expClass){
        //打开试验的表格
        $('#expTable').datagrid({
            url:'${ctx}/getExpList?expClass='+expClass,
            //去除最右侧空白
            fitColumns:true,
            scrollbarSize:0,
            //显示分页
            pagination:true,
            //显示行号
            rownumbers:true,
            singleSelect:true,
            columns:[[
                {field:'checkBox',checkbox:true},
                {field:'expName',title:'试验名称',width:100},
                {field:'expParameter',title:'使用方法',width:100,
                    formatter:function(value){
                        //datagrid初始化的时候不知什么原因会请求两次，第一次是null，
                        // 因此在这里做判断，请求到数据的时候获取value的属性，不然js报错，数据无法加载。
                        if(value!=null){
                            return value.algorithm;
                        }
                    }},
                {
                    field:'expUpdateDate',
                    title:'试验更新时间',
                    width:150,
                    formatter:function(value){
                        var myDate = new Date(value);
                        return myDate.format('Y-m-d H:i:s');
                    }
                },
                {
                    field:'userName',title:'创建人',width:100
                },
                {
                    field:'dataId',
                    title:'数据id',
                    hidden:true
                },
                {
                    field:'expId',
                    title:'试验id',
                    hidden:true
                }
            ]],
            onLoadSuccess:function(data){

            },
            onLoadError:function(){
                $.messager.alert('提示','数据载入失败')
            }
        });
    }

    function openExpTableAu(expClass){
        //打开试验的表格
        $('#expTableAu').datagrid({
            url:'${ctx}/getExpList?expClass='+expClass,
            //去除最右侧空白
            fitColumns:true,
            scrollbarSize:0,
            //显示分页
            pagination:true,
            //显示行号
            rownumbers:true,
            singleSelect:true,
            columns:[[
                {field:'checkBox',checkbox:true},
                {field:'expName',title:'试验名称',width:100},
                {field:'expParameter',title:'使用方法',width:100,
                    formatter:function(value){
                        //datagrid初始化的时候不知什么原因会请求两次，第一次是null，
                        // 因此在这里做判断，请求到数据的时候获取value的属性，不然js报错，数据无法加载。
                        if(value!=null){
                            return value.algorithm;
                        }
                    }},
                {
                    field:'expUpdateDate',
                    title:'试验更新时间',
                    width:150,
                    formatter:function(value){
                        var myDate = new Date(value);
                        return myDate.format('Y-m-d H:i:s');
                    }
                },
                {
                    field:'userName',title:'创建人',width:100
                },
                {
                    field:'dataId',
                    title:'数据id',
                    hidden:true
                },
                {
                    field:'expId',
                    title:'试验id',
                    hidden:true
                }
            ]],
            onLoadSuccess:function(data){

            },
            onLoadError:function(){
                $.messager.alert('提示','数据载入失败')
            }
        });
    }

    function openUserTable(){
        //打开试验的表格
        $('#userTable').datagrid({
            url:'${ctx}/getUserList',
            //去除最右侧空白
            fitColumns:true,
            scrollbarSize:0,
            //显示分页
            pagination:true,
            //显示行号
            rownumbers:true,
            singleSelect:true,
            columns:[[
                {field:'checkBox',checkbox:true},
                {field:'userId',title:'用户id',width:100},
                {field:'userName',title:'用户名称',width:100}
            ]],
            onLoadSuccess:function(data){

            },
            onLoadError:function(){
                $.messager.alert('提示','数据载入失败')
            }
        });
    }

    function delExpTable(expClass){
        //打开试验的表格
        $('#delExpTable').datagrid({
            url:'${ctx}/getExpList?expClass='+expClass,
            //去除最右侧空白
            fitColumns:true,
            scrollbarSize:0,
            //显示分页
            pagination:true,
            //显示行号
            rownumbers:true,
            columns:[[
                {field:'checkBox',checkbox:true},
                {field:'expName',title:'试验名称',width:100},
                {field:'expParameter',title:'使用方法',width:100,
                    formatter:function(value){
                        //datagrid初始化的时候不知什么原因会请求两次，第一次是null，
                        // 因此在这里做判断，请求到数据的时候获取value的属性，不然js报错，数据无法加载。
                        if(value!=null){
                            return value.algorithm;
                        }
                    }},
                {
                    field:'expUpdateDate',
                    title:'试验更新时间',
                    width:150,
                    formatter:function(value){
                        var myDate = new Date(value);
                        return myDate.format('Y-m-d H:i:s');
                    }
                },
                {
                    field:'userName',title:'创建人',width:100
                },
                {
                    field:'dataId',
                    title:'数据id',
                    hidden:true
                },
                {
                    field:'expId',
                    title:'试验id',
                    hidden:true
                }
            ]]
        });
    }

    function reauthorizeUser(){
        expNames[0] = $("#typeinDistinctExpName").val();
        authorizeUser();
	}

    function authorizeUser(){
        var users = $('#userTable').datagrid('getSelections');
        if(users.length===0){
            $.messager.alert('提示','请选择被授权人！');
            return;
        }
        var userIds = [];
        for(var i = 0; i<users.length; i++){
            userIds[i] = users[i].userId;
        }

        for(var i = 0; i<expNames.length; i++){
            for(var j = 0; j<userIds.length; j++){
                if(!checkAuUserExpName(expNames[i],userIds[j])){
                    alert('被授权用户存在该试验名的试验，请重新输入试验名');
                    $("#enterDistinctExpName").window('open');
                    return;
                }
			}
		}

		var expName = $("#typeinDistinctExpName").val();
        var dataPara;
        if(expName!==null&&expName!==undefined&&expName.length!==0){
            dataPara = {
                expId:selectedExp2Au,
				userId:userIds,
				newExpName:expName
            }
		}else{
            dataPara = {
                expId:selectedExp2Au,
                userId:userIds
            }
		}
        $.ajax({
            url: '${ctx}/authorizeExp',
            type: 'POST',
			data:dataPara,
            async:false,
			traditional:true,
			success:function(){
                $.messager.alert('提示','授权成功！');
                $('#chooseUser2Au').window('close');
                $('#enterDistinctExpName').window('close');
            },
			error:function(){
                $.messager.alert('提示','授权失败！');
            }
		})

	}

    function getSelectedExp(){
        var exp = $('#expTable').datagrid('getSelected');
        if(exp === null){
            $.messager.alert("提示","请选择需要打开的试验！");
            return;
		}
        selectedExp = exp;
        var title = exp.expName;
        var expId = exp.expId;
        var href = itemGlobal.url + '?expId=' + expId;
        // //判断该试验是否已经打开，如果已经打开，则选中该tab，如果没有则打开该试验的tab并将该试验加入已打开的tab列表
        // var flag = true;
        // openedTabs.forEach(function (item) {
        // 	if(exp.expId == item){
        // 	    flag = false;
        // 	}
        // })
        // if(!flag){
        //     return;
        // }
        openTabDistinctExp(title,href);
        $('#choseExp').window('close');
    }

    function getExp2Authorize(){
        expNames = [];
		selectedExp2Au = [];
       var selectedExps = $('#expTableAu').datagrid('getSelections');
       for(var i = 0; i<selectedExps.length; i++){
           selectedExp2Au[i] = selectedExps[i].expId;
           expNames[i] = selectedExps[i].expName;
	   }

	   if(selectedExp2Au.length!==0){
           openUserTable();
           $('#chooseUser2Au').window('open');
	   }else{
	       $.messager.alert("提示","请选择需要授权的试验！");
	   }
    }

    function confirmDelete(){
        $.messager.confirm('提示','确定删除选中的试验吗？',function(r){
            if (!r){
                return;
            }else{
                delSelectedExp();
            }
        });
	}

    function delSelectedExp(){
        var delExpList = $('#delExpTable').datagrid('getSelections');
        var expId = [];
        if(delExpList.length===0||delExpList===undefined){
            $.messager.alert("提示","请选择需要删除数据！");
            return;
		}
        for(var i =0; i< delExpList.length;i++){
            expId.push(delExpList[i].expId);
        }

        $.ajax({
            //删除选中的数据
            url: '${ctx}/delExpList',
            type: 'POST',
            data: {expId:expId},
            dataType:'text',
            async:false,
            traditional:true,
            success:function (data) {
                $.messager.alert('提示','删除成功！');
            },
            error:function (data) {
                console.log(data);
                $.messager.alert('提示','删除失败！');
            }
        })
        //datagrid刷新
        $('#delExpTable').datagrid('reload');
    }


    //菜单选择事件
    $('#sm').sidemenu({
        border:0,
        data: data,
        onSelect: function (item) {
            if($('#unknownLoadPredict').tree("isLeaf", item.target)){
                var tabs1 = $("#tabs");
                var tab1 = tabs1.tabs("getTab", item.text);
                //打开试验先弹出窗口，填入试验名称之后再open tab
                //判断是不是新建试验菜单项，是则打开试验名弹窗
                if(item.isAdd == true){
                    $("#typeinExpName").val("");
                    $('#enterExpName').window('open');
                    itemGlobal = item;
                }
                // else if (tab1) {
                //     tabs1.tabs("select", item.text);
                // }
                //判断是不是打开试验菜单项，是则打开试验列表弹窗
                else if(item.isExpOpen){
                    $('#choseExp').window('open');
                    openExpTable(item.expClass);
                    itemGlobal = item;
                } else if(item.isExpDel){
                    $('#delExp').window('open');
                    delExpTable(item.expClass);
                    itemGlobal = item;
                }else if(item.isNotExp){
                    itemGlobal = item;
                    openClusteringTab(item);
                }else if(item.isExpAu){
                    $('#authorizeExp').window('open');
                    openExpTableAu(item.expClass);
                    itemGlobal = item;
				}else if(item.ismaintain){
                    //打开维护表
                    var tabs1 = $("#tabs");
                    itemGlobal = item;
                    if(tabs1.tabs('exists',item.text)){
                        //如果该tab已经存在，选中并刷新该tab
                        tabs1.tabs('select',item.text);
                        //refreshTab({tabTitle:item.text,url:href});
                    }else {
                        var content = '<iframe scrolling="auto" frameborder="0"  src="'+item.url+'" style="width:100%;height:100%;"></iframe>';
                        tabs1.tabs('add', {
                            title: item.text,
                            content:content,
                            closable: true
                        });
                    }
                }
            }
        }
    });

    function openExpTab(){
        var title =$('#typeinExpName').val();
        if(title==null||title.length==0){
            $.messager.alert("提示","试验名称不能为空！");
            return;
		}

		if(!checkExpName(title,itemGlobal)){
            $.messager.alert("提示","该试验名已存在，请重新输入！");
            return;
		}
        var href = encodeURI(itemGlobal.url + '?'+'expName=' +title);
        openTab(title,href);
        $('#enterExpName').window('close');
    }

    function openClusteringTab(item){
        var title = item.text;
        var href =item.url;
        // for(var i in openedClusters){
         //    if(item.expClass===openedClusters[i]){
         //        return;
		// 	}
		// }
		// openedClusters.push(item.expClass);
        openTabDistinct(title,href);
    }

    function openTab(title,href){
        var tabs1 = $("#tabs");
        var content = '<iframe scrolling="auto" frameborder="0"  src="'+href+'" style="width:100%;height:100%;"></iframe>';
        tabs1.tabs('add', {
            title: title,
            content:content,
            closable: true
        });

        // if(tabs1.tabs('exists',title)){
        //     //如果该tab已经存在，选中并刷新该tab
        //     tabs1.tabs('select',title);
        //     refreshTab({tabTitle:title,url:href});
        // }else {
        // }
    }

    function openTabDistinct(title,href){
        var tabs1 = $("#tabs");
        if(tabs1.tabs('exists',title)){
            //如果该tab已经存在，选中并刷新该tab
            tabs1.tabs('select',title);
            //refreshTab({tabTitle:title,url:href});
        }else {
            var content = '<iframe scrolling="auto" frameborder="0"  src="'+href+'" style="width:100%;height:100%;"></iframe>';
            tabs1.tabs('add', {
                title: title,
                content:content,
                closable: true
            });
        }
    }

    function openTabDistinctExp(title,href){
        var tabs1 = $("#tabs");
        var content = '<iframe scrolling="auto" frameborder="0"  src="'+href+'" style="width:100%;height:100%;"></iframe>';
        if(tabs1.tabs('exists',title)) {
            //如果该tab已经存在，关闭并重新打开
            var opts = tabs1.tabs('options');
            var bc = opts.onBeforeClose;
            opts.onBeforeClose = function(){};
            tabs1.tabs('close',title);
            opts.onBeforeClose = bc;
        }
            tabs1.tabs('add', {
                title: title,
                content:content,
                closable: true
            });
    }

    $('#tabs').tabs({
		onAdd:function(title,index){
		},
        onBeforeClose: function(title,index){
            var target = this;
            $.messager.confirm('提示','确定关闭 '+title + ' 吗？',function(r){
                if (r){
                    var opts = $(target).tabs('options');
                    var bc = opts.onBeforeClose;
                    opts.onBeforeClose = function(){};
                    $(target).tabs('close',index);
                    opts.onBeforeClose = bc;
                }
            });
            return false;	// prevent from closing
        },
		onClose:function(title,index){
            //openedTabs.delete(index);
            // var expClass = itemGlobal.expClass;
            // for(var i in openedClusters){
			 //    if(expClass===openedClusters[i]){
			 //        delete openedClusters[i];
				// }
            // }
		}
    });


    function refreshTab(cfg){
        var refresh_tab = cfg.tabTitle ? $('#center_tab').tabs('getTab',cfg.tabTitle) : $('#center_tab').tabs('getSelected');
        if(refresh_tab && refresh_tab.find('iframe').length > 0){
            var _refresh_ifram = refresh_tab.find('iframe')[0];
            var refresh_url = cfg.url?cfg.url:_refresh_ifram.src;
            _refresh_ifram.contentWindow.location.href=refresh_url;
        }
    }

    //方法写法resize
    // $('#sm').sidemenu('resize', {
    //     width: opts.collapsed ? 60 : 200
    // })
	
	function checkExpName(title, itemGlobal) {
        var expName = title;
        var flag = false;
		$.ajax({
			url:'${ctx}/checkExpName',
            type: 'POST',
			data:{
			    expName:expName
			},
			async:false,
			success:function (data) {
			    flag = data;
            },
			error:function () {
                $.messager.alert("提示","后台校验异常！");
                flag = null;
            }
		});
        return flag;
    }

    function checkAuUserExpName(expName, userId) {
        var flag = false;
        $.ajax({
            url:'${ctx}/checkExpName',
            type: 'POST',
            data:{
                expName:expName,
				userId:userId
            },
            async:false,
            success:function (data) {
                flag = data;
            },
            error:function () {
                $.messager.alert("提示","后台校验异常！");
                flag = null;
            }
        });
        return flag;
    }

    function jump2Login(aClass){
        $.messager.confirm('提示','确定退出系统吗？',function(r){
            if (!r){
                return;
            }else{
                window.location.href='${ctx}/';
            }
        });
	}


</script>

<script type="text/javascript">
</script>
</body>
</html>