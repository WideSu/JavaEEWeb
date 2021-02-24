<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE">
	<title>当前用户</title>
	<%pageContext.setAttribute("APP_PATH",request.getContextPath());%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/jquery-easyui-1.7.0/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/jquery-easyui-1.7.0/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/jquery-easyui-1.7.0/demo/demo.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/js/jquery.uploadfile.js">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-easyui-1.7.0/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-easyui-1.7.0/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.websocket-0.0.1.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/date.format.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/server/globe.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-easyui-1.7.0/locale/easyui-lang-zh_CN.js"></script>
    </head>
<body>
	<!-- 用户表维护的表单 -->
	<div id="userFormforEdit" style="position: absolute; left:100px; top: 100px;">
		<form:form id="user" modelAttribute="user" action="/user/update" method="post">
		    <table style="front-size:12px;">
		    <tr>
		    	<form:hidden path="userId"/>
		    	<form:hidden path="userState"/>
				<td width="200px"><label>账号：</label></td>
		    	<td><form:input path="userName"/></td>
		    	<td><a href="javascript:void(0)" class="easyui-linkbutton" onclick="validate_userName()">修改</a></td>
		    </tr>
		    <tr></tr>
		    <tr></tr>
		    <tr>
		    	<td>密码</td>
		    	<td><form:input path="userPassword"/></td>
		    	<td><a href="javascript:void(0)" class="easyui-linkbutton" onclick="validate_userPassWord()">修改</a></td>
		    </tr>
		    </table>
		</form:form>
	</div>
	<script type="text/javascript">

	function validate_userName(){
		 //检验新的用户名是否合法（验证用户名唯一性）
			var newName=$("#userName").val();
			console.log(newName);
			if(newName.length==0){
				$.messager.alert('提示','还未填写账号!');
				return false;
			}else{
				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/user/check",
					dataType:"text",
				    async:false, 
				    cache: false,
			        data: {'userName':newName},
					error:function(data){
					   $.messager.alert('提示','数据传输错误!');
					},success:function(data) {
					   if(data=="false"){
					   	  $.messager.alert("提示","用户名已在数据库中，请重新输入");
					   }
					   else if(data=="true"){
					      submitUserNameEdit();
					   }
				    }
				})
			}
		}
	function validate_userPassWord(){
		 //检验密码是否合法
		var newPsw=$("#userPassword").val();
		if(newPsw.length==0){
			$.messager.alert('提示','还未填写密码!');
		}else{
			submitUserPassWordEdit();
		}
	}
	function submitUserNameEdit(){
		//提交更改的用户名信息
		var newName=$("#userName").val();
		var user = $("#user").serialize();
		$.ajax({
			type: "POST",
			url: "${pageContext.request.contextPath}/user/updateUserName",
			dataType:"json",
			async:false,
			cache: false,
			data: user,
			error:function(){
				$.messager.alert("提示","数据传输错误");
			},success:function() {
				$.messager.alert('提示', '保存成功！');
			}
		})
	}
	function submitUserPassWordEdit(){
		//提交更改的密码信息
		$.ajax({
			 type: "POST",
			 dataType: 'json',
			 url: "${pageContext.request.contextPath}/user/updateUserPassword",
			 async:false,
			 cache: false,
			 data: $("#user").serialize(),
			 dataType:"json",
			 error:function(){
			 	alert("数据传输错误");
			 },success: function (data) {
				$.messager.alert('提示', '保存成功！');
			 }
		})
	}
    </script>
</body>
</html>