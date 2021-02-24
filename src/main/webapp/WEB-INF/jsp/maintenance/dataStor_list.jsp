<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE">
	<title>数据表</title>
	<%pageContext.setAttribute("APP_PATH",request.getContextPath());%>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/jquery-easyui-1.7.0/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/jquery-easyui-1.7.0/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/sidemenu.css">
	<%--<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/jquery-easyui-1.7.0/demo/demo.css">--%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-easyui-1.7.0/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-easyui-1.7.0/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/date.format.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/server/globe.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-easyui-1.7.0/locale/easyui-lang-zh_CN.js"></script>
	</head>
<body>
	<!-- 页面上方搜索栏 -->
	<div id="searchbar">
		<input id="dataName" class="easyui-textbox" prompt='数据名'/>
		<input id="dataApplicationRange" class="easyui-combobox" data-options="
		prompt:'数据适用范围',
		panelHeight:'auto',
		textField: 'label',
		valueField: 'value',
		data: [{
			label: '未知载荷响应预测',
			value: '1'
		},{
			label: '已知载荷响应预测',
			value: '2'
		},{
			label: '载荷识别',
			value: '3'
		}]" />
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="searchDatStr()">搜索</a>
	</div>
	<div id="toolbar">
		<%--<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editDatStr()">更改名称</a>--%>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="confirmDestroyData()">删除数据</a>
	</div>
	<!-- 936 -->
	<!-- 显示数据表信息的datagrid -->
	<table id="dataStorList" title="数据表维护" class="easyui-datagrid" style="width:100%"></table>
	<!-- 保存数据表的模态框 -->
	<div id="edit-datastorage" class="easyui-dialog" style="width:240px;height:170px;padding:10px;"
			closed="true" buttons="#edit-datastorag-buttons">
	<div id="edit-datastorag-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveData()">保存</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#edit-datastorage').dialog('close')">取消</a>
	</div>
	<!-- 更改数据表的表单 -->
	<div class="ftitle">更改数据名称</div>
		<form id="editName" method="post">
			<div class="fitem">
				<label>新的数据名</label>
				<input id="newName" class="easyui-validatebox" required="true"/>
			</div>
		</form>
	</div>
	<!-- 删除确认对话框 -->
	<div id="confirm-destroy-storage" class="easyui-dialog" style="width:240px;height:130px;padding:10px 20px"
		closed="true" buttons="#confirm-destroy-storage-buttons">
		<div>确认删除选中的数据吗？</div>
	<div>
		<div id="confirm-destroy-storage-buttons">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="destroyData()">确认删除</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#confirm-destroy-storage').dialog('close')">取消</a>
		</div>
<script type="text/javascript">
	var isCheckedFlag = true;
	var isUnCheckedFlag = true;
	$("#dataStorList").datagrid({
		onSelect: function (rowIndex, rowData) {
			if(isCheckedFlag){
				rows = $(this).datagrid('getSelections');
				var dataIds = [];
				for (var i = 0; i < rows.length; i++) {
					var dataId = rows[i].dataId;
					for (var j = 0; j < dataIds.length; j++) {
						if (dataId === dataIds[j]) {
							dataId = null;
							break;
						}
					}
					dataIds.push(dataId);
				}
				var currentRows = $(this).datagrid("getRows");
				for (var i = 0; i < currentRows.length; i++) {
					for (var j = 0; j < dataIds.length; j++) {
						if (currentRows[i].dataId === dataIds[j] && i!==rowIndex) {
							isCheckedFlag = false;
							$(this).datagrid("selectRow", i);
						}
					}
				}
				isCheckedFlag = true;
			}
		},
		onUnselect:function(rowIndex,rowData){
			if(isUnCheckedFlag){
				var dataId = rowData.dataId;
				var currentRows = $(this).datagrid("getRows");
				for(var i = 0; i<currentRows.length; i++){
					if(currentRows[i].dataId===dataId && i!=rowIndex){
						isUnCheckedFlag = false;
						$(this).datagrid("unselectRow",i);
					}
				}
				isUnCheckedFlag = true;
			}
		}
	});
    var rows;
    $('#dataStorList').datagrid({
        url:"${APP_PATH}/datStrs",
        type:"POST",
        //fitColumns:true,
        scrollbarSize:0,
        //显示分页
        pagination:true,
        //显示行号
        rownumbers:true,
        singleSelect:false,
        columns:[[
            {field:'checkBox',checkbox:true},
            {field:'dataId',title:'数据Id',width:180},
            {field:'dataName',title:'数据名',width:180},
            {field:'dataApplicationRange',title:'数据适用范围',width:160,
                formatter:function(value){
                    switch(value){
                        case '1':
                            return '未知载荷的响应预测';
                        case '2':
                            return '已知载荷的响应预测';
                        case '3':
                            return '载荷识别';
                        default:
                            return '';
                    }
                }},
            {field:'dataClass',title:'数据类型',width:80,
                formatter:function(value){
                    switch(value){
                        case 'Pdata':
                            return '训练数据';
                        case 'Tdata':
                            return '训练数据';
                        case 'Udata':
                            return '预测数据';
                        case 'LP_Tdata':
                            return '训练数据';
                        case 'LP_Pdata':
                            return '预测数据';
                        case 'LP_Sdata':
                            return '训练数据';
                        default:
                            return '';
                    }
                }},
            {field:'uploadDate',title:'上传时间',width:190,
                formatter:function(value){
                    var myDate = new Date(value);
                    return myDate.format('Y-m-d H:i:s');
                }}
        ]]
    });

  function searchDatStr(){
	  var dataName = $('#dataName').val();
	  var dARange = $('#dataApplicationRange').val();
		  $('#dataStorList').datagrid({
			  url:'${APP_PATH}/datStrs/search?dataName='+dataName+'&dARange='+dARange,
		  	  type:"GET",
		  	  //fitColumns:true,
		      scrollbarSize:0,
		      //显示分页
		      pagination:true,
		      //显示行号
		      rownumbers:true,
		      singleSelect:false,
		      columns:[[
		    	  {field:'checkBox',checkbox:true},
		          {field:'dataId',title:'数据Id', width:200},
		          {field:'dataName',title:'数据名',width:200},
		          {field:'dataApplicationRange',title:'数据适用范围',width:180,
                      formatter:function(value){
                          switch(value){
                              case '1':
                                  return '未知载荷的响应预测';
                              case '2':
                                  return '已知载荷的响应预测';
                              case '3':
                                  return '载荷识别';
                              default:
                                  return '';
                          }
                      }},
		          {field:'dataClass',title:'数据类型',width:100,
					  formatter:function(value){
		              	switch(value){
							case 'Pdata':
							    return '训练数据';
							case 'Tdata':
							    return '训练数据';
                            case 'Udata':
                                return '预测数据';
                            case 'LP_Tdata':
                                return '训练数据';
                            case 'LP_Pdata':
                                return '预测数据';
                            case 'LP_Sdata':
                                return '训练数据';
							default:
							    return '';
						}
					  }},
		          {field:'uploadDate',title:'上传时间',width:336,
		              formatter:function(value){
		                  var myDate = new Date(value);
		                  return myDate.format('Y-m-d H:i:s');
		              }}
		          ]]
		  });
  }

  function editDatStr(){
	  rows = $('#dataStorList').datagrid('getSelections');
	  if(rows.length != 1)  $.messager.alert("只能选中一行进行编辑！");
	  else{
	  	$('#edit-datastorage').dialog('open').dialog('setTitle','编辑数据表');
	  	$('#editName').form('load',rows[0]);
	  }
  }
  function saveData(){
	 	$.ajax({
			type: "POST",      //data 传送数据类型。post 传递
		      url: "${pageContext.request.contextPath}/datStr/update", // 控制器方法
		      dataType:"json",
		      cache: false,      
		      data: {'dataId':rows[0].dataId,'dataName':$("#newName").val()}, //传送的数据
		      error:function(){
		    	  $.messager.alert("数据传输错误");
		      },success:function() {
		    	  $.messager.alert('提示', '保存成功！');
		    	  $('#edit-datastorage').dialog('close');
		    	  $('#dataStorList').datagrid('reload');
		      }
		})
}
function confirmDestroyData(){
	rows = $('#dataStorList').datagrid('getSelections');
	//$('#dataStorList').datagrid('selectRecord','rows.');
	//$('#dataStorList').datagrid('reload');
	if(rows.length>0){
	  $('#confirm-destroy-storage').dialog('open').dialog('setTitle','确认删除');
 	}
}
function destroyData(){
	rows = $('#dataStorList').datagrid('getSelections');
	var dataId = [];
	for(var i=0; i<rows.length; i++){
		dataId[i] = rows[i].dataId;
	}
	$.ajax({
		  url:"${pageContext.request.contextPath}/datStr/delete",
		  type: "POST",
		  async:false, 
		  cache: false,  
		  traditional:true,
		  dataType:"text",
		  data: {dataId:dataId},
		  error:function(data){
			  $.messager.alert('提示', '删除失败！');
		  },success:function(data) {
			  $.messager.alert('提示', '删除成功！');
			  $('#confirm-destroy-storage').dialog('close');
		  	  $('#edit-datastorage').dialog('close');
		  	  $('#dataStorList').datagrid('reload');
		  }	  
	})	
}
  </script>
</body>
</html>