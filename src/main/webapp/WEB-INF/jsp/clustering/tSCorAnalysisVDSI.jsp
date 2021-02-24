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
    <script type="text/javascript" src="${ctx}/js/server/globe.js"></script>
    <script type="text/javascript" src="${ctx}/js/echarts.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/server/serverinfo.js"></script>
    <c:set var="windowsProperties" value="collapsible:false,minimizable:false,maximizable:false,closable:false"/>
    <style type="text/css">
        body,td,th {font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 16px;color: #182b35; line-height:10px;}
    </style>
</head>
<body>
<form id="timeSeries" method="post" target="hidden_frame_add">
    <div id="center">
        <%--<div id="chooseUploadData" class="easyui-window" title="选择上传文件" data-options="modal:true,iconCls:'icon-save',closed:true,${windowsProperties}" style="width:500px;height:300px;padding:10px;">--%>
            <%--<table cellpadding="20" style = "margin-left: 3%;">--%>
                <%--<tr>--%>
                    <%--<td align="right">--%>
                        <%--<label>时间序列1</label>--%>
                    <%--</td>--%>
                    <%--<td>--%>
                        <%--<input type="file" id="knownPointFile"  name="upload" onchange="validDateFile(this)"/>--%>
                        <%--<input id="knownPoint" type="hidden" name="knownPoint"/>--%>
                    <%--</td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                    <%--<td align="right">--%>
                        <%--<label>时间序列2</label>--%>
                    <%--</td>--%>
                    <%--<td align="right">--%>
                        <%--<input type="file" id="unKnownPointFile" name="upload" onchange="validDateFile(this)">--%>
                        <%--<input id="unKnownPoint" type="hidden" name="unKnownPoint">--%>
                    <%--</td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                    <%--<td align="right">--%>
                        <%--<button id="buttonSave" type="button">保存</button>--%>
                    <%--</td>--%>
                    <%--<td align="center">--%>
                        <%--<button id="buttonClose" type="button" onclick="$('#chooseUploadData').window('close')">取消</button>--%>
                    <%--</td>--%>
                <%--</tr>--%>
            <%--</table>--%>
        <%--</div>--%>

        <%--<div id="chooseUploadedData" class="easyui-window" title="选择已上传文件" data-options="modal:true,iconCls:'icon-save',closed:true,${windowsProperties}" style="width:600px;height:380px;padding:10px;">--%>
            <%--<table id="dataTable" class="easyui-datagrid" style="width: 100%; height: 85%"></table>--%>
            <%--<div align="right" style="margin-top: 1%">--%>
                <%--<a href="javascript:void(0)" class="easyui-linkbutton" style="" onclick="getSelect()" text="确定"></a>--%>

                <%--<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="$('#chooseUploadedData').window('close')" text="取消"></a>--%>
            <%--</div>--%>
        <%--</div>--%>
        <div id="resultPic"  >
            <div id = "chartDiv" style="width: 800px;height: 600px"></div>
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
                    <label>选择数据： </label>
                    <input id="dataId" type="hidden" name="dataId"/>
                </td>
                <td>
                    <div id="radioDiv1" style="">
                        <input type="file" id="knownPointFile1"  name="upload" onchange="uploadTSfiles(this)" style="opacity:0;width:0%;height:0%;position:absolute;top:0;left:0">
                        <a href="javascript:void(0)" style="" class="easyui-linkbutton" onclick="$('#knownPointFile1').click();">上传</a>
                    </div>
                    <div id="textDiv" style="">
                        <label>时间序列1：<div id = 'kText' style="display: table-row"></div></label>
                        <br/>
                        <a href="javascript:void(0)" style="margin-top: 5px" class="easyui-linkbutton" onclick="resetItem()">重新选择</a>
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
                        <label>方差距离阈值：</label>
                    </div>
                </td>
                <td>
                    <input id="corThreshold" cssClass="textbox" name="corThreshold" class="textbox" onkeyup="onlyNum1(this)" style="width: 15%;height:25px;font-size: 15px"/>
                </td>
            </tr>
            <tr>
                <td style="padding-top: 25px;padding-bottom: 25px;">
                    <label style="">时间窗口长度：</label>
                </td>
                <td>
                    <input id="windowSize" cssClass="textbox" name="windowSize" class="textbox" onkeyup="onlyNum1(this)" style="width: 15%;height:25px;font-size: 15px"/>
                </td>
            </tr>
            <tr>
                <td style="padding-top: 25px;padding-bottom: 25px;">
                    <label style="">可视化簇个数：</label>
                </td>
                <td>
                    <input id="clusterSize" cssClass="textbox" name="clusterSize" class="textbox" onkeyup="onlyNum1(this)" style="width: 15%;height:25px;font-size: 15px"/>
                </td>
            </tr>
            <tr>
                <td>
                    <div id="empty"></div>
                </td>
                <td align="" valign="middle" style="padding-top: 20px;padding-bottom: 10px;">
                    <input id="algorithm" type="hidden" name="algorithm">
                    <a id="train" href="javascript:void(0)" class="easyui-linkbutton" onclick="runAlgorithms()" style="border:transparent;background:#439eb7;color: #faf4ff;height: 40px;width: 100px" text="开始聚类"></a>
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
                        聚类进度输出：
                    </label>
                </td>
                <td>
                    <textarea id="alOutput" rows="6" cols="20" readonly="true"></textarea>
                </td>
            </tr>
            <tr>
                <td>

                </td>
                <td>
                    <div style="">
                        <a id="checkPic" href="javascript:void(0)" class="easyui-linkbutton" style="color: #439eb7" onclick="$('#resultPic').dialog('open');" text="&nbsp查看结果图&nbsp"></a>
                        <a id="checkData" href="javascript:void(0)"  style="color: #439eb7;margin-left: 109px;" class="easyui-linkbutton" onclick="downloadResult()" text="查看结果数据"></a>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</form>
    <script>

        /*
        * 初始化参数信息在表单中的显示
        */
        //--------------------------------------------------------------------------------------------//
        $(document).ready(function(){
            $('#textDiv').css('display','none');
        })
        //--------------------------------------------------------------------------------------------//


        var textflow = [];
        document.getElementById("checkPic").style.display='none';
        document.getElementById("checkData").style.display='none';
        
        var legend = new Array();
		var serise = [];
		var maxNum = 0;

        $("#resultPic").dialog({
        	modal:true,
            title: '试验结果图',
            closed: true,
            cache: false,
            closable: true,
            width:850,
            height:650,
            padding:10,
            onClose: function () {
                
            },
            onOpen:function(){
                //初始化图，并清空缓存数据
                legend = new Array();
                serise = [];
                maxNum = 0;
                var myChart = echarts.init(document.getElementById('chartDiv'));
				 $.ajax({
		        	type: 'post',
		        	url: '${pageContext.request.contextPath}/getSerial',//请求数据的地址
                    data:{algorithm:$("#algorithm").val()},
		        	dataType: "json", 
            		success: function (data){
            		//将簇中的序列依次取出
		            	getSerial(data);
					//画图操作		
						setChartOptions(myChart);
           			}
        		});
            }
        });

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
            },
        });

		function getSerial(data){
			var l = 0;
      		for (var key in data) {
                //console.log(key);
                legend[l] = key;
                l = l+1;
                list = data[key]; //获取对应的value值
                for(var i=0 ;i<list.length;i++){
                    var array = list[i];
                    //console.log(array);
                    if(maxNum*1<array.length){
                        maxNum = array.length
                    }
                    　serise.push({
                    　　　　name:key,
                    　　　　type:'line',
                    　　　　data:array,
                    　　　　smooth:true
                    　　})
                }
		    }
		}
		
		function setChartOptions(myChart){
		      var xAxisArray = new Array();
     		  for(var i =1;i<=maxNum;i++){xAxisArray.push(i); }
		       // 指定图表的配置项和数据
		      var option = {
		           title: { 
		           },
		           legend: {
		               data:legend
		           },
		           xAxis: {
		          		 type: 'category',
		      			 boundaryGap: false,
		                data: xAxisArray
		           },
		           yAxis: {},
		           series: serise
		       };
		        // 使用刚指定的配置项和数据显示图表。
		        myChart.setOption(option,true);
		}
		
        function onlyNum(that){
            if(isNaN(1*that.value)){
                that.value = '';
            }else if(1*that.value>1||1*that.value<0){
                that.value = '';
            }
        }

        function onlyNum1(that){
            if(isNaN(1*that.value)){
                that.value = '';
            }else if(1*that.value<0){
                that.value = '';
            }
        }

        function resetItem(){
            $("#dataId").val("");
            $('#textDiv').css('display','none');
            $('#radioDiv1').css('display','block');
            $('#radioDiv2').css('display','block');
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
            $("#algorithm").val("VDSI");
            var timeSeries = $("form input[type=text],input[type=hidden]").serialize();
            var flag = true;
            for(var i = 1;document.getElementsByName('radio'+i+'Name').length!=0;i++){
                flag = checkSelection(document.getElementsByName('radio'+i+'Name'));
                if(!flag){
                    return;
                }
            }


            var dataId = $("#dataId").val();
            var algorithm = $('#algorithm').val();
            var corThreshold = $("#corThreshold").val();
            var windowSize = $("#windowSize").val();
            var clusterSize = $("#clusterSize").val();
            if(dataId.length===0){
                $.messager.alert('提示','请上传试验数据！');
                return;
            }
            //校验试验参数是否填写
            if(algorithm.length===0||corThreshold.length===0||windowSize.length===0||clusterSize.length===0){
                $.messager.alert('提示','请填写完整试验参数！');
                return;
            }
            if(!checkParam()){
            	return;
            }

            
            $('#train').linkbutton('disable');

            var ip = getServerIP('${ctx}/getServerIP');
            var socket = new WebSocket('ws://'+ip+'/vibsignal_analysis/websocket');
            socket.onopen = function(event){
                var str = $('#timeSeries').serialize() + '&sessionId=<%= session.getId() %>' + '&session=<%= request.getSession() %>';
                console.log(str);
                socket.send(str);
                //清空流动文本
                textflow=[];
            }

            socket.onclose = function(event){
                $('#train').linkbutton('enable');
                if(event.code!==1007){
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
            var corThreshold = $("#samplingFreq").val();
            var windowSize = $("#p2Predict").val();
            if(corThreshold == '0'){
             $.messager.alert('提示','阈值不能为0');
             return false;
            }
            if(windowSize == '0'){
             $.messager.alert('提示','待预测点不能为0');
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


        function validDateFile(self){
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

        function uploadTSfiles(self) {
            validDateFile(self);
            var file1 = $('#knownPointFile1').prop('files');
            var data = new FormData();
            var str = 'knownPoint=Tdata';
            data.append(str, file1[0]);

            $('#kText').html(file1[0].name);

            var dataId = $('#dataId').val();
            var url = '${ctx}/uploadData' + '?param1=' + str + '&expId=' + 'clustering' + '&expClass=' + $('#expClass').val();
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
                    showUpload();
                    $('#chooseUploadData').window('close');
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
                    if(data==true){
                        $.messager.alert('提示', '保存成功！');
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
                dataType:"json",
                data:$("#experiment").serialize(),
                type:"POST",
                async:false,
                success:function(data) {
                    if(data==true){
                    console.log('提示', '上传成功！');
                        submitFlag = true;
                    }else{
                    console.log('提示', '上传失败！');
                        submitFlag = false;
                    }
                },
                error:function(data){
                   console.log('提示', '后台错误！');
                   submitFlag = false;
                }
            })

        }
        function openDataTable(){
            $('#dataTable').datagrid({
                url:'${ctx}/getUploadedDataList'+ '?expClass=' + $('#expClass').val(),
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
                    {field:'knownPoint',title:'已知测点数据',width:100},
                    {field:'unKnownPoint',title:'未知测点数据',width:100},
                    {field:'p2Predict',title:'未知测点',width:40},
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


        function getSelect(){
            var selectedRow = $('#dataTable').datagrid('getSelected');
            console.log(selectedRow);
            var dataId = $('#dataId').val();
            //将选择的数据的id赋值给当前实验的dataId
            if(dataId==undefined||dataId.length==0){
                $('#dataId').val(selectedRow.dataId);
            }
            $('#kText').val(selectedRow.knownPoint);
            $('#uText').val(selectedRow.unKnownPoint);
            $('#chooseUploadedData').window('close');
            showUploaded(selectedRow);
        }

        function downloadFile(resultIndex){

            var url = "${ctx}/downloadFile?path="+resultIndex;

            var $eleForm = $("<form method='post'></form>");


            $eleForm.attr("action",url);

            $(document.body).append($eleForm);

            $eleForm.submit();
        }

        function downloadResult(){
            var dataId = $('#dataId').val();
             $.ajax({
                 //传回dataId以确定更新数据还是插入数据
                 url: "${ctx}/findDataIndex?dataId="+dataId,
                 type: 'POST',
                 dataType:'text',
                 cache: false,
                 processData: false,
                 contentType: false,
                 success:function (data) {
                     downloadFile(data);
                     return data;
                 },
                 error:function (data) {
                     return "";
                 }
             })
        }

        function showUpload(){
            $('#radioDiv1').css('display','none');
            $('#radioDiv2').css('display','none');
            $('#textDiv').css('display','block');
        }

        function showUploaded(selectedRow){
            $('#radioDiv1').css('display','none');
            $('#radioDiv2').css('display','none');
            $('#textDiv').css('display','block');
            $('#kText').html(selectedRow.knownPoint);
            $('#uText').html(selectedRow.unKnownPoint);
            $('#knownPoint').val(selectedRow.knownPoint);
            $('#unKnownPoint').val(selectedRow.unKnownPoint);
        }
    </script>

</body>
</html>
