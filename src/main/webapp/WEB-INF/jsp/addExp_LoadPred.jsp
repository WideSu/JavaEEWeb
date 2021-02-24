<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
    <link rel="stylesheet" type="text/css" href="${ctx}/js/jquery-easyui-1.7.0/demo/demo.css">
    <link rel="stylesheet" href="${ctx}/js/jquery.uploadfile.js">
    <link rel="stylesheet" type="text/css" href="${ctx}/css/styleExp.css">
    <script type="text/javascript" src="${ctx}/js/jquery-easyui-1.7.0/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-easyui-1.7.0/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery.websocket-0.0.1.js"></script>
    <script type="text/javascript" src="${ctx}/js/date.format.js"></script>
    <script type="text/javascript" src="${ctx}/js/server/serverinfo.js"></script>
    <script type="text/javascript" src="${ctx}/js/server/globe.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-easyui-1.7.0/locale/easyui-lang-zh_CN.js"></script>
    <c:set var="windowsProperties" value="collapsible:false,minimizable:false,maximizable:false,closable:false"/>
    <style type="text/css">
        body,td,th {font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 16px;color: #182b35; line-height:10px}
    </style>
</head>
<body>

<form:form id="experiment" modelAttribute="experiment" action="${ctx}/save" method="post" target="hidden_frame">
    <form:hidden path="UUID"/>
    <form:hidden path="expId"/>
    <form:hidden path="expName"/>
    <form:hidden path="userId"/>
    <form:hidden path="authorizedUserId"/>
    <form:hidden path="expClass"/>
    <form:hidden id="dataId" path="data.dataId"/>
    <form:hidden path="expCreateDate"/>
    <form:hidden path="expUpdateDate"/>
    <form:hidden path="expState"/>
    <form:hidden path="delFlag"/>
    <form:hidden id="LP_KnownResponse"  path="expParameter.LP_KnownResponse"/>
    <form:hidden id="LP_Source" path="expParameter.LP_Source"/>
    <form:hidden id="LP_Predict" path="expParameter.LP_Predict" />
    <form:hidden id="algorithm" path="expParameter.algorithm"/>
    
    <div id="center" data-options="region:'center',split:true">
    
    <!-- 上传响应信号数据和源信号数据 默认隐藏，点击上传数据后开启-->
        <div id="LRP_chooseUpload" class="easyui-window" title="选择上传文件" data-options="modal:true,iconCls:'icon-save',closed:true,${windowsProperties}" style="overflow:hidden;width:366px;height:273px;padding:10px;">
            <input type="file" id="LP_KnownResponseFile"  name="upload" onchange="validDateFileNew(this,'#fileName1')" style="display: none"/>
            <input type="file" id="LP_SourceFile" name="upload" onchange="validDateFileNew(this,'#fileName2')" style="display: none"/>
            <table cellpadding="20" class="fileUploadTable">
                <tr>
                    <td>
                        <label>已知测点数据</label>
                    </td>
                    <td>
                        <a class="easyui-linkbutton" onclick="$('#LP_KnownResponseFile').click()">上传</a>
                        <label id="fileName1">请选择文件</label>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label>未知测点数据</label>
                    </td>
                    <td>
                        <a class="easyui-linkbutton" onclick="$('#LP_SourceFile').click()">上传</a>
                        <label id="fileName2">请选择文件</label>
                    </td>
                </tr>
            </table>
            <div id="buttons" style="align:center" class="button-arrange">
                <button id="buttonSave" type="button" class="easyui-linkbutton" style="margin-right: 37px;">保存</button>
                <button id="buttonClose" type="button" class="easyui-linkbutton" onclick="$('#LRP_chooseUpload').window('close')">取消</button>
            </div>
        </div>

        <!-- 选择已上传文件列表 -->
		<div id="LRP_chooseUploadedData" class="easyui-window" title="选择已上传文件" data-options="modal:true,iconCls:'icon-save',closed:true,${windowsProperties}" style="width:600px;height:380px;padding:10px;">
            <table id="UploaddataTable" class="easyui-datagrid" style="width: 100%; height: 85%"></table>
            <div align="right" style="margin-top: 1%">
                <a href="javascript:void(0)" class="easyui-linkbutton" style="" onclick="getSelect()" text="确定"></a>

                <a href="javascript:void(0)" class="easyui-linkbutton"  onclick="$('#LRP_chooseUploadedData').window('close')" text="取消"></a>
            </div>
        </div>
        
         <!-- 查看试验结果图 使用datagrid加载多张图片，默认隐藏，点击查看图片后开启 -->
        <div id="LRP_resultPic" class="easyui-dialog" title="试验结果图" data-options="modal:true,iconCls:'icon-save',closed:true,closable:true" style="width:720px;height:580px;padding:10px">
				<table id="LRP_grid" >
		
				</table>
				
				
		<!-- 下载结果数据表，默认隐藏，点击下载数据后开启 -->		
		<div id="resultData" class="easyui-dialog" title="试验结果"  style="padding:10px">
			<table id="ResData_Grid">
			</table>
			<div align="right">
				<a style="display:block; float:right;text-align:right; margin-right:10px" class="easyui-linkbutton" onclick="downloadData()">下载</a>
			</div>
		</div>
		
	
	 <!-- 定义了显示图片，下载数据，日期转换等函数. -->
    <script type="text/javascript">
    
    $(function (){
    	$("#LRP_grid").datagrid({
    		url:'${ctx}/getMultipleImgURL?expId='+$("#expId").val(),
    		width: 680,
    		height:500,
    		collapsible:true,
    		showHeader:false, 
    		columns : [[
                {field:'changePic',formatter:showButton,width:'9%'},
    			{field:'url',align:center, formatter:show, width:'90%', height:'500'}
    		]],
    	})
    })
    //将获取到的src传给后台输出图片
    	function show(value,row,index){
    		return "<img src='${ctx}/getSingleImg?path="+value+"'></img>"
    	}
        function showButton(value,row,index){
            return "<input name='changePic' onclick='changePic("+index+")' type='button' value='切换'/>"
        }

        function changePic(index){
            //获取该行的数据
            var data = $('#LRP_grid').datagrid("getRows")[index];
            //获取该行图片的url
            var oldUrl = data.url;
            //按'/'切割为字符数组 并对倒数第二个元素 也就是末级目录进行判断并替换
            var char = oldUrl.split('/');
            var length = char.length;
            if(char[length-2] =='log'){char[length-2] = 'nature'}
            else if(char[length-2] == 'nature'){char[length-2] = 'log'}
            else {return ;}
            var newUrl = char.join('/');
            //刷新当前行
            $('#LRP_grid').datagrid('updateRow',{
                index: index,
                row:{
                    "url":newUrl
                }
            });

        }
    	
    //将选中的expResultId传给后台进行数据下载
    	function downloadData(){
    	var checkedRow = $("#ResData_Grid").datagrid("getChecked");
    	var expResultId = "" ;
		expResultId = String(checkedRow[0].expResultId);
				
			 window.location.href = "${ctx }/downloadText?expResultId="+expResultId;			
			}
    	
    	
    	//使用js方法加载ResData_Grid
    	  $("#resultData").dialog({
            title: '试验结果',
            modal:true,
            closed: true,
            cache: false,
            closable: true,
            width:600,
            height:330,
            padding:10,
            onClose: function () {
                $("#result")
            },
    	   onOpen:function (){
    		$("#ResData_Grid").datagrid({
    			singleSelect:true,
    			url:'${ctx}/GetDataTable?expId='+$("#expId").val(),
    			height:200,
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
                        {field:'resultMeaning',title:'数据名称',width:90},
                        {
                            field:'resultCreateDate',
                            title:'数据生成日期',
                            width:90,
                            formatter:function(value){
                                var myDate = new Date(value);
                                return myDate.format('Y-m-d H:i:s');
                            }
                        },
                        {field:'expId', title:'试验ID',width:'150',formatter:getExpId,hidden:true},
					 	{field:'expName', title:'试验名称',width:'150',formatter:getExpName,hidden:true},
                    ]],
                })
       	 },
    	});
    	
    	function getExpId(value,row,index){
    		exp = row.experiment;
    		return exp.expId; 
    	}
    	function getExpName(value,row,index){
    		exp = row.experiment;
    		return exp.expName;
    	}
    	
    	//日期转换
    	function date(value) {
            var date = new Date(value);//long转换成date
            var year = date.getFullYear().toString();
            var month = (date.getMonth() + 1);
            var day = date.getDate().toString();
            var hours = date.getHours().toString();
            var minute = date.getMinutes().toString();
            var second = date.getSeconds().toString();
            if (month < 10) {
                month = "0" + month;
            }
            if (day < 10) {
                day = "0" + day;
            }
            if (hours < 10) {
                hours = "0" + hours;
            }
            if (minute < 10) {
                minute = "0" + minute;
            }
            if (second < 10) {
                second = "0" + second;
            }
            return year + "-" + month + "-" + day +" "+hours +":"+minute +":"+second ;
        }
        
	 function getSelect(){
            //将选择的数据的id赋值给当前实验的dataId
            var selectedRow = $('#UploaddataTable').datagrid('getSelected');
            console.log(selectedRow);
            var dataId = $('#dataId').val();
            console.log(dataId);
            //将选择的数据的id赋值给当前实验的dataId
            if(dataId==undefined||dataId.length==0){
                $('#dataId').val(selectedRow.dataId);
         	     showUploaded(selectedRow);
            }else{
            	updateData(selectedRow);
            	
            }

        }
       function updateData(selectedRow){
            var dataId = $('#dataId').val();
        	var selectDataId = selectedRow.dataId;
            	var url = '${ctx}/updateData'
            	 + '?dataId='+dataId+'&selectDataId='+selectDataId;
            	$.ajax({
                url: url,
                type: 'POST',              
                cache: false,
                success:function (data) {
               		showUploaded(selectedRow);
                },
                error:function (data) {
               		 console.log(data);
                    $.messager.alert('提示','文件选择失败！');
                }
            });
        }
    </script>
        </div>
    </div>
    

	<!-- 中心页面，默认开启，不可隐藏 -->
     <table id="expTableforPara" cellpadding="5" class="expTableforPara">
            <tr>
                <td>
                    <label>选择训练数据： </label>
                </td>
                <td>
                    <div id="radioDiv1" style="">
                        <label><input id="uploadNew" name="radio1Name" type="radio" onclick="radioControl(this.name)">上传新数据</label>
                        <a href="javascript:void(0)" style="margin-left: 85px" class="easyui-linkbutton" onclick="chooseFiles()">确定</a>
                    </div>
                    <div id="textDiv" style="">
                        <label>响应信号数据：<div id = 'rText' style="display: table-row"></div></label>
                        <br/>
                        <div style="margin-top: 5px">
                            <label>源信号数据：<div id = 'sText' style="display: table-row"></div></label>
                        </div>
                        
                        <a href="javascript:void(0)" style="margin-top: 5px" class="easyui-linkbutton" onclick="resetItemwithNodataIdReset()">重新选择</a>
                    </div>
                </td>
            </tr>
            <tr valign="top">
                <td>

                </td>
                <td>
                    <div id="radioDiv2" style="">
                        <label><input id="chooseUsed" name="radio1Name" type="radio" onclick="radioControl(this.name)">选择已有数据</label>
                    </div>
                </td>
            </tr>
            <tr valign="center">
            	<td>
                    <div style="margin-top: 20px">
                        <label>工况响应信号数据： </label>
                    </div>
                </td>
            	<td valign="center">
            		<div style="margin-top: 20px">
            			<input type="file" id="LP_PredictFile"  name="LP_PredictFile" value="上传" style="display: none" onchange="showPredictData(this)"/>
            			<a id="radioDiv3" href="javascript:void(0)" class="easyui-linkbutton" style="width: 45px;height: 30px" onclick="predictLRPOnclick()">上传</a>
            			<div id="textDiv2" style="">
                            <label id="pText"></label>
                            <br/>
                            <a href="javascript:void(0)" style="margin-top: 5px" class="easyui-linkbutton" onclick="resetItem1()">重新选择</a>
                        </div>
            		</div>
            	</td>
            </tr>
 
            <tr align="left">
                <td colspan="3">
                    <div style="margin-top: 20px;margin-bottom: 20px;border-bottom: 1px solid #999999">
                    </div>
                </td>
            </tr>
            <tr>
                <td style="padding-top: 20px;padding-bottom: 20px;">
                    <div style="">
                        <label>频率范围：</label>
                    </div>
                </td>
                <td>
                    <label><form:input id="freqRangeMin" cssClass="textbox" path="expParameter.freqRangeMin" name="param" onkeyup="onlyNum(this)" cssStyle="width: 15%;height:25px;font-size: 15px"/>Hz</label>
                    <label style="margin-left: 20px;padding-right: 20px">-</label>
                    <label><form:input id="freqRangeMax" cssClass="textbox" path="expParameter.freqRangeMax" name="param" onkeyup="onlyNum(this)" cssStyle="width: 15%;height:25px;font-size: 15px"/>Hz</label>
                </td>
            </tr>
            <tr>
                <td style="padding-top: 25px;padding-bottom: 25px;">
                    <label style="">采样频率：</label>
                </td>
                <td>
                    <label><form:input id="samplingFreq" cssClass="textbox" path="expParameter.samplingFreq" name="param" onkeyup="onlyNum(this)" cssStyle="width: 15%;height:25px;font-size: 15px"/>Hz</label>
                </td>
            </tr>
            <tr>
                <td style="padding-top: 25px;padding-bottom: 25px;">
                    <label>载荷识别预测方法：</label>
                </td>
                <td colspan="2">
                    <label style="padding-right: 18%"><input id = "SVM" name="radio2Name" type="radio" onclick="radioControl(this.name);$('#algorithm').val('SVM')">SVM</label>
                    <label style="padding-right: 18%"><input id = "LR" name="radio2Name" type="radio" onclick="radioControl(this.name);$('#algorithm').val('线性回归')">线性回归</label>
                    <label style=""><input id = "KNN" name="radio2Name" type="radio" onclick="radioControl(this.name);$('#algorithm').val('KNN')">KNN</label>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center" valign="middle" style="padding-top: 20px;padding-bottom: 10px;">
                    <a id="train" href="javascript:void(0)" class="easyui-linkbutton" onclick="runAlgorithms()" style="border:transparent;background:#439eb7;color: #faf4ff;height: 40px;width: 100px" text="开始训练"></a>
                    <a href="javascript:void(0)" class="easyui-linkbutton" style="margin-left:50px;border:transparent;background:#439eb7;color: #faf4ff;height: 40px;width: 100px" onclick="saveExp()" text="保存试验"></a>
                </td>
            </tr>
            <tr align="left">
                <td colspan="3">
                    <div style="margin-top: 0px;margin-bottom: 10px;border-bottom: 1px solid #999999">
                    </div>
                </td>
            </tr>
            <tr>
                <td style="width: 20%">
                    <label>
                        试验输出：
                    </label>
                </td>
                <td>
                    <textarea id="alOutput" rows="6" cols="20" class="textAreaCss" readonly="true"></textarea>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <div style="">
                        <a id="checkPic" href="javascript:void(0)" class="easyui-linkbutton" style="color: #439eb7" onclick="opendialog()" text="&nbsp查看结果图&nbsp"></a>
                        <a id="checkData" href="javascript:void(0)"  style="margin-left:50px;color: #439eb7;" class="easyui-linkbutton" onclick="opendialog2()" text="查看结果数据"></a>
                    </div>
                </td>
            </tr>
        </table>
    
    <!-- 用来判断查看对比图和下载数据列表的按钮是否显示 -->
	<input type="hidden" name="OpenPic" id="OpenPic" value="${OpenPic }">
    <script>
    /*
        * 初始化参数信息在表单中的显示
        */
        //--------------------------------------------------------------------------------------------//
        $(document).ready(function(){
            //判断试验是否已经选择了数据,从而选择不同的控件显示
            var dataId = $('#dataId').val();
            console.log(dataId);
            if(dataId!=null&&dataId.length!==0){
                $('#rText').html($('#LP_KnownResponse').val());
                $('#sText').html($('#LP_Source').val());
                var LP_Predict = $('#LP_Predict').val();
                if(LP_Predict!==null&&LP_Predict.length!==0){
                    $('#radioDiv3').css('display','none');
                    $('#pText').text(LP_Predict);
                }else{
                    $('#textDiv2').css('display','none');
                }

                $('#radioDiv1').css('display','none');
                $('#radioDiv2').css('display','none');

                var expId = $('#expId').val();
                var hasResult = hasExpResult('${ctx}/hasResult',expId);
                if(hasResult){
                    $('#checkPic').css('display','');
                    $('#checkData').css('display','');
                }
            }else{
                $('#textDiv').css('display','none');
                $('#textDiv2').css('display','none');
                document.getElementById("checkPic").style.display='none';
                document.getElementById("checkData").style.display='none';
            }
       var algo = $('#algorithm').val();
            if(algo!=null&&algo!=''&&algo!=undefined){
                switch (algo){
                    case 'SVM':
                        $("#SVM").prop("checked",true);
                        console.log("1");
                        break;
                    case '线性回归':
                        $("#LR").prop("checked",true);
                        break;
                    case 'KNN':
                        $("#KNN").prop("checked",true);
                        break;
                    default:
                        console.log("switch step into default");
                }
            }
        })
    
    //打开查看对比图的div 并且重新加载datagrid
    	function opendialog(){
    	$("#LRP_grid").datagrid({url:"${ctx}/getMultipleImgURL?expId="+$("#expId").val()});
    	$("#LRP_grid").datagrid("reload");
    	$('#LRP_resultPic').dialog('open');
    	}
    	
    //打开下载数据的div 并且重新加载datagrid	
    	function opendialog2(){
    	$('#resultData').dialog('open');
    	}
    	
    	//默认将查看图片和下载数据按钮隐藏
    	document.getElementById("checkPic").style.display='none' 
    	
    	document.getElementById("checkData").style.display='none' 
    	
    	//若是打开试验，那么会传一个OpenPic的判断值，如果为1 则说明是打开试验而不是新建试验，则将查看图片和下载数据的按钮显示
    	//否则，只会在点击开始训练后在与websocket的连接中开启
        // $(function(){
			// if($("#OpenPic").val()=="1"){
			// document.getElementById("checkPic").style.display='' ;
			// document.getElementById("checkData").style.display='';
			// }
        // })
		
		function resetItem(){
            $("#dataId").val("");
            $('#textDiv').css('display','none');
            $('#radioDiv1').css('display','block');
            $('#radioDiv2').css('display','block');
        }

        function resetItemwithNodataIdReset(){
            $('#textDiv').css('display','none');
            $('#radioDiv1').css('display','block');
            $('#radioDiv2').css('display','block');
        }
        
        function resetItem1(){
            var file = document.getElementById('LP_PredictFile');
            //虽然file的value值不能设为有内容的字符，但是可以设置为空字符
            file.value = '';
            //或者
            file.outerHTML = file.outerHTML;
            $("#LP_Predict").val('');
        	$('#radioDiv3').css('display','block');
        	$('#textDiv2').css('display','none');
        }
        
        function predictLRPOnclick(){
            var dataId = $("#dataId").val();
            if(dataId==undefined || dataId.length == 0){
                $.messager.alert('提示','请先上传训练数据！');
                return;
            }
            $('#LP_PredictFile').click();
        }
        
        function showPredictData(self){
            validDateFile(self);
            var file = $("#LP_PredictFile").prop('files');
            $("#LP_Predict").val(file[0].name);
            console.log($("#LP_Predict").val());
            $("#radioDiv3").css('display','none');
            $('#textDiv2').css('display','block');

            $("#pText").text(file[0].name);

            //上传数据
            LP_PredictUpload();
        }
        
        function validDateFile(self){
            //返回String对象中子字符串最后出现的位置.
            var seat=self.value.lastIndexOf(".");
            //返回位于String对象中指定位置的子字符串并转换为小写.
            var extension=self.value.substring(seat).toLowerCase();
            if(extension==""||extension==null||extension==undefined){
                return false;
            }else if(extension!=".txt" && extension!=".csv"){
                $.messager.alert('提示','请上传txt格式的数据！');
                self.outerHTML = self.outerHTML;
                return false;
            }
            return true;
        }

    function validDateFileNew(self,spanName){
        if(validDateFile(self)){
            var i = self.value.lastIndexOf("\\");
            var fileName = self.value.slice(i+1);
            if (fileName.length > 10)
                fileName = fileName.substring(0, 15) + "...";
            $(spanName).text(fileName);
        }else{
            return;
        }
    }

        //控制radio单选
        function radioControl(name) {
            var radios = document.getElementsByName(name);
            for(var i = 0; i < radios.length; i++){
                if(radios[i].checked){
                    for(var j = 0; j < radios.length; j++){
                        if(i!=j){
                            radios[j].checked = false;
                        }
                    }
                }
            }
        }

        function runAlgorithms(){
            $('#checkPic').css('display','none');
            $('#checkData').css('display','none');
            //校验单选按钮是否点击
            $("#alOutput").val("");
            var flag = true;
            for(var i = 1;document.getElementsByName('radio'+i+'Name').length!=0;i++){
                flag = checkSelection(document.getElementsByName('radio'+i+'Name'));
                if(!flag){
                    return;
                }
            }
            
            var param1 = $("#freqRangeMin").val();
            var param2 = $("#freqRangeMax").val();
            var param3 = $("#samplingFreq").val();
            var param4 = 'none';
            var expId = $("#expId").val();
            var dataId = $("#dataId").val();
            var algorithm = $('#algorithm').val();
            var expClass = $("#expClass").val();
            var LP_Predict = $("#LP_Predict").val();
            //校验试验参数是否填写
            if(param1.length===0||param2.length===0||param3.length===0||param4.length===0){
                $.messager.alert('提示','请填写完整试验参数！');
                return;
            }
            
            if(!checkParam()){
            	return ;
            }

            if(dataId===undefined||dataId.length===0){
                $.messager.alert('提示','请选择上传训练数据！');
                return;
            }

            if(LP_Predict===null||LP_Predict===''){
                $.messager.alert('提示','请选择上传待预测数据！');
                return;
            }



			saveExp2();
            if(!submitFlag){
            	$("#alOutput").val('数据上传失败！');
            	return ;
            }

            //运行前先删除已有的实验结果，避免之后运行失败导致查看结果异常
            delPreExpResult('${ctx}/delPreExpResult', expId);


            $('#train').linkbutton('disable');
            var ip = getServerIP('${ctx}/getServerIP');
            var socket = new WebSocket('ws://'+ip+'/vibsignal_analysis/websocket');
            socket.onopen = function(event){
                var str = 'expClass='+expClass+
                '&freqRangeMin='+param1+
                '&freqRangeMax='+param2+
                '&samplingFreq='+param3+
                '&p2Predict='+param4+
                '&expId='+expId+
                '&dataId='+dataId+
                '&algorithm='+algorithm;
                socket.send(str);
                //清空流动文本
                textflow=[];
            }

            socket.onclose = function(event){
                $('#train').linkbutton('enable');
                if(event.code!=1007){
                    document.getElementById("checkPic").style.display='';
                    document.getElementById("checkData").style.display='';
                }
            }

            socket.onerror = function(event){

            }

            socket.onmessage = function(event){
                getMessageFlow(event.data);
            }
        }

        function getMessageFlow(data){
            textflow.push('\n' + data);
            if(textflow.length > 7){
                textflow = textflow.slice(textflow.length-7,textflow.length)
            }
            $("#alOutput").val(textflow);
        }

        //校验单选完整性
        function checkSelection(inputElement){
            var flag = false;
            for(var i = 0; i < inputElement.length; i++){
                if(inputElement[i].checked){
                    flag = true;
                }
            }
            if(!flag){
                switch(inputElement[0].name){
                    case 'radio1Name':
                        var dataId = $('#dataId').val();
                        if(dataId!=undefined&&dataId!=null&&dataId.length!=0){
                            flag = true;
                        }else{
                            $.messager.alert('提示','请上传或选择数据！');
                        }
                        break;
                    case 'radio2Name':
                        $.messager.alert('提示','请选择需要使用的算法！');
                        break;
                    default:
                        $.messager.alert('提示','异常！');
                }
                return flag;
            }
            return flag;

        }
        //传输数据判断
        function checkParam(){
        	var fMin = $("#freqRangeMin").val();
            var fMax = $("#freqRangeMax").val();
            var sFreq = $("#samplingFreq").val();
            if(1*fMin>1*fMax){
          	  $.messager.alert('提示','请正确填写频率范围');
          	  return false;
            }
            if(sFreq == '0'){
             $.messager.alert('提示','采样频率不能为0');
             return false;
            }
            return true;
        } 
        
		//打开数据上传的div
        function chooseFiles(){
            var flag = checkSelection(document.getElementsByName('radio1Name'));
            if(!flag){
                return;
            }
            else{
                if($('#uploadNew')[0].checked){
                    $('#LRP_chooseUpload').window('open');
                }else if($('#chooseUsed')[0].checked){
                    $('#LRP_chooseUploadedData').window('open');
                    openDataTable();
                }
            }

        }

        function onlyNum(that){
            that.value=that.value.replace(/\D/g,"");
        }

        function validDataFile(self){
            //返回String对象中子字符串最后出现的位置.
            var seat=self.value.lastIndexOf(".");
            //返回位于String对象中指定位置的子字符串并转换为小写.
            var extension=self.value.substring(seat).toLowerCase();
            if(extension==""||extension==null||extension==undefined){
                return false;
            }else if(extension!=".txt"){
                $.messager.alert('提示','请上传txt格式的数据！');
                self.outerHTML = self.outerHTML;
                return false;
            }
            return true;
        }
		
		//上传数据
        $('#buttonSave').click(function(){
            //这个部分只是用来增加一个ajax，使其走进setup过滤
            var expId = $('#expId').val();
            var hasResult = hasExpResult('${ctx}/hasResult',expId);

            var file1 = $('#LP_KnownResponseFile').prop('files');
            var file2 = $('#LP_SourceFile').prop('files');

            if(file1.length==0||file2.length==0){
                $.messager.alert("提示","请上传完整所需数据！");
                return;
            }

            var data = new FormData();
            var str = ['LP_KnownResponse=LP_Tdata', 'LP_Source=LP_Sdata'];
            data.append(str[0], file1[0]);
            data.append(str[1], file2[0]);
			$('#LP_KnownResponse').val(file1[0].name);$('#LP_Source').val(file2[0].name);
			
			var dataId = $('#dataId').val();
            var url = '${ctx}/uploadData' + '?param1=' + str[0] + 
            '&param2='+ str[1] + 
            '&expId=' + $('#expId').val() + '&expClass=' + $('#expClass').val();
            if(dataId!=undefined&&dataId.length!=0){
                url = url + '&dataId=' + dataId;
            }
            $.ajax({
                url: url,
                type: 'POST',
                data: data,
                async:false,
                cache: false,
                processData: false,
                contentType: false,
                success:function (data) {
                	
               		 //上传成功则赋值dataId
                    $('#dataId').val(data);
                    $.messager.alert('提示','文件上传成功！');
                    showUpload();
                    $('#LRP_chooseUpload').window('close');
                },
                error:function (data) {
               		 console.log(data);
                    $.messager.alert('提示','文件上传失败！');
                }
            });
        });
        
        //上传工况响应数据
         function LP_PredictUpload(){
             //这个部分只是用来增加一个ajax，使其走进setup过滤
             var expId = $('#expId').val();
             var hasResult = hasExpResult('${ctx}/hasResult',expId);

        	var file =  $('#LP_PredictFile').prop('files');
        	var data = new FormData();
        	var str = ['LP_Predict=LP_Pdata'];
        	data.append(str[0], file[0]);
        	$('#LP_Predict').val(file[0].name);
        	
        	var url = '${ctx}/uploadData' + '?param1='+str[0] + 
        	'&expId=' + $('#expId').val() + '&expClass=' + $('#expClass').val();
            var dataId = $("#dataId").val();
            if(dataId!=undefined&&dataId.length!=0){
                url = url + '&dataId=' + dataId;
            }
        	 $.ajax({
                url: url,
                type: 'POST',
                data: data,
                cache: false,
                processData: false,
                contentType: false,
                success:function (data) {
                	
               		 //上传成功则赋值dataId
                    $('#dataId').val(data);
                    $.messager.alert('提示','文件上传成功！');
                },
                error:function (data) {
               		 console.log(data);
                    $.messager.alert('提示','文件上传失败！');
                }
            });
        }

        //保存，提交表单
        function saveExp(){
            $.ajax({
                url:'${ctx}/save',
                dataType:"text",
                data:$("#experiment").serialize(),
                type:"POST",
                success:function(data) {
                    if(data==='true'){
                        $.messager.alert('提示', '保存成功！');
                    }else if(data === 'dataNotFound'){
                        $.messager.alert('提示','该上传数据已被删除，请重新上传！');
                        resetItem();
                    }else{
                        $.messager.alert('提示', '保存失败！');
                    }
                },
                error:function(data){
                    $.messager.alert('提示', '后台错误！');
                }
            })

        }
        
		var submitFlag ;
        function saveExp2(){
            $.ajax({
                url:'${ctx}/save',
                dataType:"text",
                data:$("#experiment").serialize(),
                type:"POST",
                async:false,
                success:function(data) {
                    if(data === 'true'){
                        submitFlag = true;
                    }else if(data === 'dataNotFound'){
                        submitFlag = false;
                        $.messager.alert('提示','该上传数据已被删除，请重新上传！');
                        resetItem();
                    }else{
                        submitFlag = false;
                    }
                },
                error:function(data){
                    submitFlag = false;
                }
            })

        }
         function showUpload(){
            $('#radioDiv1').css('display','none');
            $('#radioDiv2').css('display','none');
            $('#textDiv').css('display','block');
            $('#rText').html($('#LP_KnownResponse').val());
            $('#sText').html($('#LP_Source').val());
        }
		
		
        function showUploaded(selectedRow){
            $('#LP_KnownResponse').val(selectedRow.lp_KnownResponse);
            $('#LP_Source').val(selectedRow.lp_Source);
            $('#LRP_chooseUploadedData').window('close');
            $('#radioDiv1').css('display','none');
            $('#radioDiv2').css('display','none');
            $('#textDiv').css('display','block');
            
            $('#rText').html(selectedRow.lp_KnownResponse);
            $('#sText').html(selectedRow.lp_Source);
        }
        
        function openDataTable(){
            $('#UploaddataTable').datagrid({
                url:'${ctx}/getLpUploadedDataList'  + '?expClass=' + $('#expClass').val(),
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
                    {field:'lp_KnownResponse',title:'已知响应数据',width:100},
                    {field:'lp_Source',title:'源信号数据',width:100},
                    {field:'p2Predict',title:'所选组',width:40},
                    {
                        field:'uploadDate',
                        title:'上传时间',
                        width:150,
                        formatter:function(value){
                            if(value!=''&&value!=undefined&&value!=null) {
                                return value.split(".")[0];
                            }
                            return '';
                        }
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
    </script>
</form:form>
<iframe name='hidden_frame' id="hidden_frame" style="display:none"></iframe>

</body>
</html>
