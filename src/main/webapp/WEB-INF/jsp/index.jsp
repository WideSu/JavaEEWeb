<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:font-family="http://www.w3.org/1999/xhtml">
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE">
    <title>机械振动信号分析系统</title>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/style.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/js/jquery-easyui-1.7.0/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/js/jquery-easyui-1.7.0/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/css/sidemenu.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/css/styleExp.css">
    <script type="text/javascript" src="${ctx}/js/jquery-easyui-1.7.0/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-easyui-1.7.0/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/date.format.js"></script>
    <script type="text/javascript" src="${ctx}/js/server/globe.js"></script>

</head>
<body>
<form:form id="user" modelAttribute="user" action="${ctx}/home" method="post" onsubmit="return checkFlag();">
    <div class="main">
    	<div class="mainin">
        	<h1>机械振动信号分析系统</h1>
            <div class="mainin1">
            	<ul>
                	<li><span>用户名：</span><form:input path="userName" cssClass="SearchKeyword" type = "text" onkeydown="" required="required" maxlength="18"/></li>
                    <li><span>密码：</span><form:input path="userPassword" cssClass="SearchKeyword2" type = "password" required="required" maxlength="18"/></li>
                    <li style="padding-bottom: 0px"><a href="${ctx}/register" onclick="" target="_blank">注册账号</a></li>
                    <li><input type="submit" class="submitte" onclick="validateUser()" value="登录"></li>
                </ul>
            </div>
        </div>
    </div>
</form:form>
    <div id="loading" style="text-align: center">
        <img id="loadingImg" src="${ctx}/image/loading.gif" style="display:none;position:absolute;" />
    </div>
<div id="POPLoading" class="cssPOPLoading">
    <div style=" height:110px; border-bottom:1px solid #9a9a9a">
        <div class="showMessge"></div>
    </div>
    <div style=" line-height:40px; font-size:14px; letter-spacing:1px;">
        <a onclick="puc()">确定</a>
    </div>
</div>
<div style="text-align:center;">
</div>

<script type="text/javascript">
    var flag = false;
    $(document).ready(function(){
        $('#userName').val('');
        $('#userPassword').val('');
    });

    // function loadgif() {
    //    $('#loadingImg').css('display','block');
    // }

    function checkFlag(){
        return flag;
    }

    function validateUser(){
        var userName = $("#userName").val();
        var userPassword = $("#userPassword").val();
        if(userName.length==0||userPassword.length==0) return flag;
        $.ajax({
            url: '${ctx}/validateUser',
            type: 'POST',
            data: $('#user').serialize(),
            dataType:'json',
            async:false,
            traditional:true,
            success:function (data) {
                if(data==true){
                    flag = true;
                }else{
                    flag = false;
                    $.messager.alert('提示','请输入正确的账号密码！');
                }
            },
            error:function () {
                flag = false;
                $.messager.alert('提示','登陆失败，后台异常！');
            }
        });
        return flag;
    }

</script>
<%--从iframe中跳转登录--%>
<script type="text/javascript">
    if (top.location != location){
        top.location.href = location.href;
    }
</script>
</body>
</html>
