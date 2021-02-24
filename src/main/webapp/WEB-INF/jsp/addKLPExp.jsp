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

<form:form id="experiment" modelAttribute="experiment" action="${ctx}/save" method="post" target="hidden_frame_add">
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
    <form:hidden id="knownLoad"  path="expParameter.knownLoad"/>
    <form:hidden id="knownResponse" path="expParameter.knownResponse"/>
    <form:hidden id="data2Predict" path="expParameter.data2Predict"/>
    <form:hidden id="algorithm" path="expParameter.algorithm"/>
    <div id="center">
        <div id="chooseUploadData" class="easyui-window" title="选择上传文件" data-options="modal:true,iconCls:'icon-save',closed:true,${windowsProperties}" style="overflow:hidden;width:366px;height:273px;padding:10px;">
            <input type="file" id="knownPointFile"  name="upload" onchange="validDateFileNew(this,'#fileName1')" style="display: none"/>
            <input type="file" id="unKnownPointFile" name="upload" onchange="validDateFileNew(this,'#fileName2')" style="display: none"/>
            <table cellpadding="20" class="fileUploadTable">
                <tr>
                    <td>
                        <label>载荷信号数据</label>
                    </td>
                    <td>
                        <a class="easyui-linkbutton" onclick="$('#knownPointFile').click()">上传</a>
                        <label id="fileName1">请选择文件</label>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label>响应信号数据</label>
                    </td>
                    <td>
                        <a class="easyui-linkbutton" onclick="$('#unKnownPointFile').click()">上传</a>
                        <label id="fileName2">请选择文件</label>
                    </td>
                </tr>
            </table>
            <div id="buttons" style="align:center" class="button-arrange">
                <button id="buttonSave" type="button" class="easyui-linkbutton" onclick="butonSaveUpload()" style="margin-right: 37px;">保存</button>
                <button id="buttonClose" type="button" class="easyui-linkbutton" onclick="$('#chooseUploadData').window('close')">取消</button>
            </div>
        </div>

        <div id="chooseUploadedData" class="easyui-window" title="选择已上传文件" data-options="modal:true,iconCls:'icon-save',closed:true,${windowsProperties}" style="width:600px;height:380px;padding:10px;">
            <table id="dataTable" class="easyui-datagrid" style="width: 100%; height: 85%"></table>
            <div align="right" style="margin-top: 1%">
                <a href="javascript:void(0)" class="easyui-linkbutton" style="" onclick="getSelect()" text="确定"></a>

                <a href="javascript:void(0)" class="easyui-linkbutton"  onclick="$('#chooseUploadedData').window('close')" text="取消"></a>
            </div>
        </div>

        <div id="resultPic"  style="overflow: hidden;">
            <div style="width: 700px;">
                <div id="buttonSwitch" class="div-inline">
                    <a id="switch" class="easyui-linkbutton" onclick="switchPic() ">切换</a>
                </div>
                <div>
                    <img id="img" alt="" class="div-inline">
                </div>
            </div>
        </div>

        <div id="resultData"  style="padding:10px">
            <div id="resultDataList">
            </div>
            <div align="right">
                <a href="javascript:void(0)" class="easyui-linkbutton" style="margin-top: 20px;" onclick="downloadFile()" text="下载"></a>
            </div>
        </div>

        <table id="expTableforPara" cellpadding="5" class="expTableforPara">
            <tr>
                <td>
                    <label>选择训练数据： </label>
                </td>
                <td>
                    <div id="radioDiv1" style="">
                        <label><input id="uploadNew" name="radio1Name" type="radio" onclick="radioControl(this.name)">上传新训练数据</label>
                        <a href="javascript:void(0)" style="margin-left: 85px" class="easyui-linkbutton" onclick="chooseFiles()">确定</a>
                    </div>
                    <div id="textDiv" style="">
                        <label>载荷信号数据：<div id = 'kText' style="display: table-row"></div></label>
                        <br/>
                        <div style="margin-top: 5px">
                            <label>响应信号数据：<div id = 'uText' style="display: table-row"></div></label>
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
                        <label><input id="chooseUsed" name="radio1Name" type="radio" onclick="radioControl(this.name)">选择已有训练数据</label>
                    </div>
                </td>
            </tr>
            <tr valign="center">
                <td>
                    <div style="margin-top: 20px">
                        <label>工况载荷信号数据： </label>
                    </div>
                </td>
                <td valign="center">
                    <div style="margin-top: 20px">
                        <input type="file" id="predictUpload"  name="predictUpload" value="上传" style="display: none" onchange="showPredictData(this)"/>
                        <a id="uploadPredict" href="javascript:void(0)" style="width: 45px;height: 30px" class="easyui-linkbutton" onclick="predictUploadOnclick()">上传</a>
                        <div id="predictDiv" style="">
                            <label id="pText"></label>
                            <br/>
                            <a href="javascript:void(0)" style="margin-top: 5px" class="easyui-linkbutton" onclick="resetItem1()">重新选择</a>
                        </div>
                    </div>
                </td>
            </tr>
            <tr align="left">
                <td colspan="3">
                    <div style="margin-top: 15px;margin-bottom: 20px;border-bottom: 1px solid #999999">
                    </div>
                </td>
            </tr>
            <tr>
                <td style="padding-top: 15px;padding-bottom: 20px;">
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
            <tr style="display: none">
                <td style="padding-top: 25px;padding-bottom: 25px;">
                    <label>待预测的检测点：</label>
                </td>
                <td>
                    <label><form:input id="p2Predict" cssClass="textbox" path="expParameter.p2Predict" name="param" onkeyup="onlyNum(this)" cssStyle="width: 15%;height:25px;font-size: 15px"/></label>
                </td>
            </tr>
            <tr>
                <td style="padding-top: 25px;padding-bottom: 25px;">
                    <label>响应预测方法：</label>
                </td>
                <td colspan="2">
                    <label style="padding-right: 18%"><input id = "SVM" name="radio2Name" type="radio" onclick="radioControl(this.name);$('#algorithm').val('SVM')">SVM</label>
                    <label style="padding-right: 18%"><input id = "LR" name="radio2Name" type="radio" onclick="radioControl(this.name);$('#algorithm').val('线性回归')">线性回归</label>
                    <label style=""><input id = "Dtree" name="radio2Name" type="radio" onclick="radioControl(this.name);$('#algorithm').val('决策树')">决策树</label>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center" valign="middle" style="padding-top: 15px;padding-bottom: 10px;">
                    <a id="train" href="javascript:void(0)" class="easyui-linkbutton" onclick="runAlgorithms()" style="border:transparent;background:#439eb7;color: #faf4ff;height: 40px;width: 100px" text="开始训练"></a>
                    <a href="javascript:void(0)" class="easyui-linkbutton" style="margin-left:50px;border:transparent;background:#439eb7;color: #faf4ff;height: 40px;width: 100px" onclick="saveExp()" text="保存试验"></a>
                </td>
            </tr>
            <tr align="left">
                <td colspan="3">
                    <div style="margin-top: 0px;margin-bottom: 5px;border-bottom: 1px solid #999999">
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
                        <a id="checkPic" href="javascript:void(0)" class="easyui-linkbutton" style="color: #439eb7" onclick="$('#resultPic').dialog('open');" text="&nbsp查看结果图&nbsp"></a>
                        <a id="checkData" href="javascript:void(0)"  style="margin-left: 50px;;color: #439eb7;" class="easyui-linkbutton" onclick="$('#resultData').dialog('open');" text="查看结果数据"></a>
                    </div>
                </td>
            </tr>
        </table>
    </div>
    <script>

        /*
        * 初始化参数信息在表单中的显示
        */
        //--------------------------------------------------------------------------------------------//
        $(document).ready(function(){
            //判断试验是否已经选择了数据,从而选择不同的控件显示
            var dataId = $('#dataId').val();
            if(dataId!==null&&dataId.length!==0){
                $('#kText').html($('#knownLoad').val());
                $('#uText').html($('#knownResponse').val());
                var data2Predict = $('#data2Predict').val();
                if(data2Predict!==null&&data2Predict.length!==0){
                    $('#uploadPredict').css('display','none');
                    $('#pText').text(data2Predict);
                }else{
                    $('#predictDiv').css('display','none');
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
                $('#predictDiv').css('display','none');
            }

            var algo = $('#algorithm').val();
            if(algo!=null&&algo!==''&&algo!==undefined){
                switch (algo){
                    case 'SVM':
                        $("#SVM").prop("checked",true);
                        console.log("1");
                        break;
                    case '线性回归':
                        $("#LR").prop("checked",true);
                        break;
                    case '决策树':
                        $("#Dtree").prop("checked",true);
                        break;
                    default:
                        console.log("switch step into default");
                }
            }
        });
        //--------------------------------------------------------------------------------------------//


        var textflow = [];
        document.getElementById("checkPic").style.display='none';
        document.getElementById("checkData").style.display='none';

        $("#resultPic").dialog({
        	modal:true,
            title: '试验结果图',
            closed: true,
            cache: false,
            closable: true,
            width:680,
            height:580,
            padding:10,
            onClose: function () {
                $('#img').removeAttr("src");
            },
            onOpen:function(){
                //这个部分只是用来增加一个ajax，使其走进setup过滤
                var expId = $('#expId').val();
                var hasResult = hasExpResult('${ctx}/hasResult',expId);
                //增加random使得每次请求的url都不一样，不会读取图片缓存
                $('#img').attr("src",'${ctx}/getImg?expId='+$('#expId').val() +'.'+Math.random());
            }
        });

        $("#resultData").dialog({
        	modal:true,
            title: '试验结果',
            closed: true,
            cache: false,
            closable: true,
            width:600,
            height:330,
            padding:10,
            onClose: function () {
            },
            onOpen:function (){
                $('#resultDataList').datagrid({
                    url:'${ctx}/getPredictedDataList?expId='+$("#expId").val(),
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
                        {field:'resultIndex',title:'数据索引',hidden:true}
                    ]]
                });
            }
        });

        var isLog = true;
        function switchPic(){
            var img = $('#img');
            var expId = $('#expId').val();
            img.attr("src",'${ctx}/getImg?expId='+expId +'.'+Math.random()+'&isSwitch='+isLog);
            isLog = !isLog;
        }

        //控制radio单选
        function radioControl(name) {
            var radios = document.getElementsByName(name);
            for(var i = 0; i < radios.length; i++){
                if(radios[i].checked){
                    for(var j = 0; j < radios.length; j++){
                        if(i!==j){
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
            for(var i = 1;document.getElementsByName('radio'+i+'Name').length!==0;i++){
                flag = checkSelection(document.getElementsByName('radio'+i+'Name'));
                if(!flag){
                    return;
                }
            }

            var param1 = $("#freqRangeMin").val();
            var param2 = $("#freqRangeMax").val();
            var param3 = $("#samplingFreq").val();
            var expId = $("#expId").val();
            var dataId = $("#dataId").val();
            var algorithm = $('#algorithm').val();
            var expClass = $("#expClass").val();
            var data2Predict = $("#data2Predict").val();
            //校验试验参数是否填写
            if(param1.length===0||param2.length===0||param3.length===0){
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

            if(data2Predict===null||data2Predict===''){
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
                var str = 'expClass='+expClass
                    +'&freqRangeMin='+param1+'&freqRangeMax='+param2+'&samplingFreq='+param3+'&p2Predict='+'null'
                    +'&expId='+expId+'&dataId='+dataId
                    +'&algorithm='+algorithm
                    +'&data2Predict='+data2Predict;
                socket.send(str);
                //清空流动文本
                textflow=[];
            };

            socket.onclose = function(event){
                $('#train').linkbutton('enable');
                if(event.code!=1007){
                    document.getElementById("checkPic").style.display='';
                    document.getElementById("checkData").style.display='';
                }
            };

            socket.onerror = function(event){

            };

            socket.onmessage = function(event){
                getMessageFlow(event.data);
            }
        }

        function onlyNum(that){
            that.value=that.value.replace(/\D/g,"");
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
                        if(dataId!==undefined&&dataId!=null&&dataId.length!==0){
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
        
        function chooseFiles(){
            var flag = checkSelection(document.getElementsByName('radio1Name'));
            if(!flag){
                return;
            }else{
                if($('#uploadNew')[0].checked){
                    $('#chooseUploadData').window('open');
                }else if($('#chooseUsed')[0].checked){
                    $('#chooseUploadedData').window('open');
                    openDataTable();
                }
            }

        }

        function showPredictData(self){
            validDateFile(self);
            var file = $("#predictUpload").prop('files');
            $("#data2Predict").val(file[0].name);
            console.log($("#data2Predict").val());
            $("#uploadPredict").css('display','none');
            $('#predictDiv').css('display','block');

            $("#pText").text(file[0].name);

            //上传数据
            data2PredictUpload();
        }

        function validDateFile(self){
            //返回String对象中子字符串最后出现的位置.
            var seat=self.value.lastIndexOf(".");
            //返回位于String对象中指定位置的子字符串并转换为小写.
            var extension=self.value.substring(seat).toLowerCase();
            if(extension===""||extension==null||extension===undefined){
                return false;
            }else if(extension!==".txt"){
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

        function butonSaveUpload(){
            //这个部分只是用来增加一个ajax，使其走进setup过滤
            var expId = $('#expId').val();
            var hasResult = hasExpResult('${ctx}/hasResult',expId);

            var file1 = $('#knownPointFile').prop('files');
            var file2 = $('#unKnownPointFile').prop('files');
            var file3 = $('#predictUpload').prop('files');

            if(file1.length===0||file2.length===0){
                $.messager.alert("提示","请上传完整所需数据！");
                return;
            }

            var data = new FormData();
            //训练数据;未知测点数据；待预测数据
            var str = ['knownLoad=Tdata', 'knownResponse=Pdata', 'data2Predict=Udata'];
            data.append(str[0], file1[0]);data.append(str[1], file2[0]);data.append(str[2], file3[0]);
            //将文件名存于参数属性
            $('#knownLoad').val(file1[0].name);$('#knownResponse').val(file2[0].name);
            /*$("#data2Predict").val(file[0].name);*/ //在predictUpload的click事件中已经赋值

            var dataId = $('#dataId').val();
            var url = '${ctx}/uploadData' + '?param1=' + str[0] + '&param2='+ str[1] + 
            '&expId=' + $('#expId').val() + '&expClass=' + $('#expClass').val();
            if(dataId!==undefined&&dataId.length!==0){
                url = url + '&dataId=' + dataId;
            }
            $.ajax({
                //传回dataId以确定更新数据还是插入数据
                url: url,
                type: 'POST',
                data: data,
                dataType:'text',
                async:false,
                cache: false,
                processData: false,
                contentType: false,
                success:function (data) {
                    //上传成功则赋值dataId
                    $('#dataId').val(data);
                    $.messager.alert('提示','文件上传成功！');
                    showUpload();
                    $('#chooseUploadData').window('close');
                },
                error:function (data) {
                    console.log(data);
                    $.messager.alert('提示','文件上传失败！');
                }
            });
        }

        function data2PredictUpload(){
            var file1 = $('#predictUpload').prop('files');
            var data = new FormData();
            //训练数据;未知测点数据；待预测数据
            var str = ['data2Predict=Udata'];
            data.append(str[0], file1[0]);
            //将文件名存于参数属性
            /*$("#data2Predict").val(file[0].name);*/ //在predictUpload的click事件中已经赋值
            var url = '${ctx}/uploadData'+'?param1='+str[0] + 
            '&expId=' + $('#expId').val()+ '&expClass=' + $('#expClass').val();
            
            var dataId = $("#dataId").val();
            if(dataId!=undefined&&dataId.length!=0){
                url = url + '&dataId=' + dataId;
            }
            $.ajax({
                //传回dataId以确定更新数据还是插入数据
                url: url,
                type: 'POST',
                data: data,
                dataType:'text',
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
            console.log($("#experiment").serialize());
            $.ajax({
                url:'${ctx}/save',
                dataType:"json",
                data:$("#experiment").serialize(),
                type:"POST",
                success:function(data) {
                    if(data===true){
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

		var submitFlag;
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

        function openDataTable(){
            $('#dataTable').datagrid({
                url:'${ctx}/getUploadedDataList' + '?expClass=' + $('#expClass').val(),
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
                    {field:'knownPoint',title:'载荷信号数据',width:100},
                    {field:'unKnownPoint',title:'响应信号数据',width:100},
                    {
                        field:'uploadDate',
                        title:'上传时间',
                        width:150,
                        formatter:function(value){
                            if(value!==''&&value!==undefined&&value!=null) {
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


        function getSelect(){
            var selectedRow = $('#dataTable').datagrid('getSelected');
            //将选择的数据的id赋值给当前实验的dataId
            console.log(selectedRow);
            var dataId = $('#dataId').val();
            //将选择的数据的id赋值给当前实验的dataId
            console.log(dataId);
            if(dataId===undefined||dataId.length===0){
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

        function downloadFile(){
            var resultIndex = $('#resultDataList').datagrid('getSelected').resultIndex;
            //var resultMeaning = $('#resultDataList').datagrid('getSelected').resultMeaning;
            var url = "${ctx}/downloadFile?path="+resultIndex;

            var $eleForm = $("<form method='post'></form>");

            console.log(url);

            $eleForm.attr("action",url);

            $(document.body).append($eleForm);

            $eleForm.submit();
        }

        function showUpload(){
            $('#radioDiv1').css('display','none');
            $('#radioDiv2').css('display','none');
            $('#textDiv').css('display','block');
            $('#kText').html($('#knownLoad').val());
            $('#uText').html($('#knownResponse').val());
        }

        function showUploaded(selectedRow){
            $('#chooseUploadedData').window('close');
            $('#radioDiv1').css('display','none');
            $('#radioDiv2').css('display','none');
            $('#textDiv').css('display','block');
            $('#kText').html(selectedRow.knownPoint);
            $('#uText').html(selectedRow.unKnownPoint);
            $('#knownLoad').val(selectedRow.knownPoint);
            $('#knownResponse').val(selectedRow.unKnownPoint);
        }

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
            var file = document.getElementById('predictUpload');
            //虽然file的value值不能设为有内容的字符，但是可以设置为空字符
            file.value = '';
            //或者
            file.outerHTML = file.outerHTML;
            $("#data2Predict").val('');
            $('#predictDiv').css('display','none');
            $('#uploadPredict').css('display','block');
        }

        function predictUploadOnclick(){
            var dataId = $("#dataId").val();
            if(dataId===undefined || dataId.length === 0){
                $.messager.alert('提示','请先上传训练数据！');
                return;
            }
            $('#predictUpload').click();
        }
    </script>
</form:form>

</body>
</html>
