<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE">
	<title>试验参数表</title>
    <%
	pageContext.setAttribute("APP_PATH",request.getContextPath());
	%>

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
<div id="expParamSearchDiv" style="width: 100%;height: 100%;">
	<div id="searchbar">
		<input id="expName" class="easyui-textbox" prompt='试验名'/>
		<input id="expClass" class="easyui-combobox"  data-options="
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
		<input id="algorithm" class="easyui-textbox" prompt='试验参数值'/>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="searchExpP()">搜索</a>
	</div>
	<div style="margin-top: 1%">
		<table id="expParamList" title="试验参数表" class="easyui-datagrid" style="width:100%"></table>
	</div>
</div>
<script type="text/javascript">
	$("#dataStorList").datagrid({
		onSelect: function (rowIndex, rowData) {
			$(this).datagrid("unselectRow",rowIndex);
		}
	});

  $('#expParamList').datagrid({
	  url:"${APP_PATH}/expPs",
	  type:"POST",
	  //fitColumns:true,
	  //fit:true,
      scrollbarSize:0,
      //显示分页
      pagination:true,
      //显示行号
      rownumbers:true,
      columns:[[
          {field:'expId',title:'试验id',width:270},
          {field:'expName',title:'试验名称',width:200},
          {field:'featureName',title:'试验参数名',width:200,
              formatter:function(value,row,index){
                  var expParameter = row.expParameter;
                  if(expParameter==null){
                      return '';
                  }
                  var word=expParameter.featureName;
				  switch(word){
					  case 'LP_Predict':
						  return '上传预测点';
					  case 'freqRangeMin':
						  return '频率范围下界';
					  case 'freqRangeMax':
						  return '频率范围上界';
					  case 'algorithm':
						  return '使用算法';
					  case 'knownLoad':
						  return '已知载荷数据（响应预测）';
					  case 'samplingFreq':
						  return '采样频率';
					  case 'data2Predict':
						  return '未知测点数据（响应预测）';
					  case 'knownResponse':
						  return '已知响应信号数据(响应预测)';
					  case 'p2Predict':
						  return '待预测的检测点';
					  case 'LP_KnownResponse':
						  return '已知响应信号数据(载荷识别)';
					  case 'LP_Source':
						  return '已知源信号数据(载荷识别)';
					  case 'unKnownPoint':
						  return '预测数据';
					  case 'knownPoint':
						  return '训练数据';
					  default:
						  return '';
				  }
				}
		  },
          {field:'featureValue',title:'试验参数值',width:200,
              formatter:function(value,row,index){
                  var expParameter = row.expParameter;
                  if(expParameter==null){
                      return '';
                  }
                  return expParameter.featureValue;}}
	  ]]
  });
  

  function searchExpP(){
	  var expName = $('#expName').val();
	  var expClass = $('#expClass').val();
	  var algorithm = $('#algorithm').val();
	  $('#expParamList').datagrid({
				url:'${APP_PATH}/expP/search?'+encodeURI('expName='+expName+'&expClass='+expClass+'&algorithm='+algorithm),
				type:"POST",
			  //fitColumns:true,
		      scrollbarSize:0,
		      //显示分页
		      pagination:true,
		      //显示行号
		      rownumbers:true,
		      columns:[[
				  {field:'expId',title:'试验id',width:270},
				  {field:'expName',title:'试验名称',width:200},
				  {field:'featureName',title:'试验参数名',width:200,
                      formatter:function(value,row,index){
                          var expParameter = row.expParameter;
                          if(expParameter==null){
                              return '';
                          }
                          var word=expParameter.featureName;
                          switch(word){
                              case 'LP_Predict':
                                  return '上传预测点';
                              case 'freqRangeMin':
                                  return '频率范围下界';
                              case 'freqRangeMax':
                                  return '频率范围上界';
                              case 'algorithm':
                                  return '使用算法';
                              case 'knownLoad':
                                  return '已知载荷数据（响应预测）';
                              case 'samplingFreq':
                                  return '采样频率';
                              case 'data2Predict':
                                  return '未知测点数据（响应预测）';
                              case 'knownResponse':
                                  return '已知响应信号数据(响应预测)';
                              case 'p2Predict':
                                  return '待预测的检测点';
                              case 'LP_KnownResponse':
                                  return '已知响应信号数据(载荷识别)';
                              case 'LP_Source':
                                  return '已知源信号数据(载荷识别)';
                              case 'unKnownPoint':
                                  return '预测数据';
                              case 'knownPoint':
                                  return '训练数据';
                              default:
                                  return '';
                          }
                      }
				  },
				  {field:'featureValue',title:'试验参数值',width:200,
					  formatter:function(value,row,index){
						  var expParameter = row.expParameter;
                          if(expParameter==null){
                              return '';
                          }
						  return expParameter.featureValue;}}
			  ]]
			});
  }
</script>
</body>

</html>