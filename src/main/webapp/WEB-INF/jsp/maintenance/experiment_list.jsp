<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE">
	<title>工程表</title>
    <%
	pageContext.setAttribute("APP_PATH",request.getContextPath());
	%>
<!-- Annie 2019/05/15 -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/jquery-easyui-1.7.0/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/jquery-easyui-1.7.0/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/sidemenu.css">
<%--<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/jquery-easyui-1.7.0/demo/demo.css">--%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-easyui-1.7.0/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-easyui-1.7.0/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/date.format.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/server/globe.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-easyui-1.7.0/locale/easyui-lang-zh_CN.js"></script>
	<style>
		.fitem{
			margin-top: 5%;
			margin-left: 5%;
		}
	</style>
</head>

<body>
<div class="panel">
	<div id="searchbar">
		<input id="expName" class="easyui-textbox" prompt='试验名'/>
		<input id="expClass" class="easyui-combobox" data-options="
		prompt:'试验类别',
		panelHeight:'auto',
		textField: 'label',
		valueField: 'value',
		data: [{
			label: '模型精度测试(未知载荷)',
			value: 'ULPM'
		},{
			label: '未知测点预测',
			value: 'ULP'
		},{
			label: '模型精度测试(已知载荷)',
			value: 'KLPM'
		},{
			label: '响应信号预测',
			value: 'KLP'
		},{
			label: '模型精度测试(载荷识别)',
			value: 'UKLT'
		},{
			label: '未知载荷识别',
			value: 'UKLP'
		}]" />
		<input id="userName" class="easyui-textbox" prompt='用户名'/>
		<input id="algorithm" class="easyui-textbox" prompt='算法名'/>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="searchExp()">搜索</a>
	</div>
	<div id="toolbar">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editExp()">更改名称</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="confirmDestroy()">删除试验</a>
	</div>
	
	<table id="experimentList" title="工程表" class="easyui-datagrid" style="width:100%"></table>
	
	<div id="edit-experiment" class="easyui-dialog" style="width:240px;height:170px;padding:1px 1px"
			closed="true" buttons="#edit-experiment-buttons">
		<form id="experiemt-info" method="post">
			<div class="fitem">
				<label>请输入新的试验名：</label>
				<input id="newName" class="easyui-textbox" />
			</div>
		</form>
	</div>
	<div id="edit-experiment-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveExp()">保存</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#edit-experiment').dialog('close')">取消</a>
	</div>

	<div id="delete-experiment" class="easyui-dialog" style="width:240px;height:130px;padding:10px 20px" closed="true" buttons="#delete-experiment-buttons">
		<div>确认删除选中的试验吗？</div>
	</div>
	<div id="delete-experiment-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="destroyExp()">确认删除</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#delete-experiment').dialog('close')">取消</a>
	</div>
</div>
<script type="text/javascript">
var rows;
  $('#experimentList').datagrid({
		url:"${APP_PATH}/exps",
		type:"POSTs",
	  fitColumns:true,
      scrollbarSize:0,
      //显示分页
      pagination:true,
      //显示行号
      rownumbers:true,
      singleSelect:false,
      columns:[[
          {field:'checkBox',checkbox:true},
          {field:'expName',title:'试验名称'},
          {field:'expId',title:'试验id'},
          {
        	  field:'expClass',
        	  title:'试验类别',
        	  formatter:function(value){
        		  switch(value){
                      case 'ULPM':
                          return '模型精度测试(未知载荷)';
                      case 'KLPM':
                          return '模型精度测试(已知载荷)';
                      case 'ULP':
                          return '未知测点预测(未知载荷)';
                      case 'KLP':
                          return '响应信号预测(已知载荷)';
                      case 'UKLT':
                          return '模型精度测试(载荷识别)';
                      case 'UKLP':
                          return '未知载荷识别(载荷识别)';
                      default:
                          return '';
				}
        	  }
          },
          {field:'algorithm',title:'使用算法'},
          {field:'userName',title:'试验创建用户'},
          {
              field:'expCreateDate',
			  title:'试验创建时间',
			  formatter:function(value){
                  var myDate = new Date(value);
                  return myDate.format('Y-m-d H:i:s');
              }
		  },
          {
              field:'expUpdateDate',
			  title:'试验更新时间',
              formatter:function(value){
                  var myDate = new Date(value);
                  return myDate.format('Y-m-d H:i:s');
              }
		  }
          ]]
	});
  
  function editExp(){
	  rows = $('#experimentList').datagrid('getSelections');
	  if(rows.length === 0){
          $.messager.alert('提示', '请选中一个试验！');
	  }else if (rows.length > 1){
		  $.messager.alert('提示', '只能选中一个进行编辑！');
	  }else{
	  	$('#edit-experiment').dialog('open').dialog('setTitle','编辑试验');
	  	$('#experiemt-info').form('load',rows);
	  }
  }
  function saveExp(){
      var expName = $("#newName").val();
      console.log(expName);
      if(expName.length===0){
          $.messager.alert('提示','试验名不能为空！');
          return;
	  }
      if(checkExpName(expName)!==true){
          $.messager.alert('提示','该试验名已存在！');
	  }else{
          $.ajax({
              type: "POST",      //data 传送数据类型。post 传递
              url: "${pageContext.request.contextPath}/exp/update", // 控制器方法
              dataType:"json",
              cache: false,
              data: {'expId':rows[0].expId,'expName':expName}, //传送的数据
              error:function(){
                  $.messager.alert("提示", "更改失败！");
              },success:function(data) {
                  if(data===true) {
                      $.messager.alert('提示', '保存成功！');
                      $("#newName").val('');
                      $('#edit-experiment').dialog('close');
                      $('#experimentList').datagrid('reload');
                  }else{
                      $.messager.alert('提示', '该试验名已存在，请重新输入！');
                  }
              }
          })
      }
  }
  function confirmDestroy(){
	  rows = $('#experimentList').datagrid('getSelections');
      if(rows.length === 0){
          $.messager.alert('提示','请选择需要删除的试验！');
          return;
      }
		$('#delete-experiment').dialog('open').dialog('setTitle','确认删除');
  }

  function destroyExp(){
	  rows = $('#experimentList').datagrid('getSelections');
	  var expId = [];
      for(var i = 0; i<rows.length; i++){
          expId[i] = rows[i].expId;
		}
      $.ajax({
			 url:"${pageContext.request.contextPath}/exp/delete",
			 async:false, 
		  	 cache: false,  
		  	 traditional:true,
			 type: "POST",
			 dataType:"text",
			 data: {expId:expId},
			 error:function(data){
			    $.messager.alert('提示', '删除失败！');
			 },success:function(data) {
			    $.messager.alert('提示', '删除成功！');
			    $('#delete-experiment').dialog('close');
			    $('#experimentList').datagrid('reload');
			 }
		})  
  }
  function searchExp(){
	  var expName = $('#expName').val();
	  var expClass = $('#expClass').val();
	  var userName = $('#userName').val();
	  var algorithm = $('#algorithm').val();
	  console.log(expName,expClass,userName,algorithm);

		  $('#experimentList').datagrid({
				url:'${APP_PATH}/exp/search?'+encodeURI('expName='+expName+'&expClass='+expClass+'&userName='+userName+'&algorithm='+algorithm),
				type:"GET",
			  fitColumns:true,
		      scrollbarSize:0,
		      //显示分页
		      pagination:true,
		      //显示行号
		      rownumbers:true,
		      singleSelect:true,
		      columns:[[
		          {field:'checkBox',checkbox:true},
		          {field:'expName',title:'试验名称'},
		          {field:'expId',title:'试验id'},
		          {
		        	  field:'expClass',
		        	  title:'试验类别',
		        	  formatter:function(value){
		        		  switch(value){
							case 'ULPM':
							    return '模型精度测试(未知载荷)';
							case 'KLPM':
							    return '模型精度测试(已知载荷)';
							case 'ULP':
								return '未知测点预测(未知载荷)';
							case 'KLP':
								return '响应信号预测(已知载荷)';
							case 'UKLT':
								return '模型精度测试(载荷识别)';
							case 'UKLP':
								return '未知载荷识别(载荷识别)';
							default:
							    return '';
						}
		        	  }
		          },
		          {field:'algorithm',title:'使用算法'},
		          {field:'userName',title:'试验创建用户'},
		          {
		              field:'expCreateDate',
					  title:'试验创建时间',
					  formatter:function(value){
		                  var myDate = new Date(value);
		                  return myDate.format('Y-m-d H:i:s');
		              }
				  },
		          {
		              field:'expUpdateDate',
					  title:'试验更新时间',
		              formatter:function(value){
		                  var myDate = new Date(value);
		                  return myDate.format('Y-m-d H:i:s');
		              }
				  }
		          ]]
			});
  }

function checkExpName(title) {
    var expName = title;
    var flag = false;
    $.ajax({
        url:'${pageContext.request.contextPath}/checkExpName',
        type: 'POST',
        data:{
            expName:expName
        },
        async:false,
        success:function (data) {
            flag = data;
            console.log(flag);
        },
        error:function () {
            $.messager.alert("提示","后台校验异常！");
            flag = null;
        }
    });
    return flag;
}

function ifEmptyGiveEmpty(str){
    console.log(this);
    if(str.val()==='&nbsp;'||str.val()===" "){
        $(str).val('');
        console.log($(str).val());
    }
}
</script>
</body>
</html>