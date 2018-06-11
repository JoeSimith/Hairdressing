<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">

<head>
	<title>${pd.SYSNAME}</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="X-UA-Compatible" content="ie=edge">
	<!-- jquery -->
	<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
	<!-- vue -->
	<script src="https://cdn.bootcss.com/vue/2.4.4/vue.js"></script>
	<!-- element -->
	<script src="https://cdn.bootcss.com/element-ui/1.4.6/index.js"></script>
	<link href="https://cdn.bootcss.com/element-ui/1.4.6/theme-default/index.css" rel="stylesheet">
	<!-- bootstrap -->
	<link href="static/assets/css/bootstrap.min.css" rel="stylesheet" />
	<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" rel="stylesheet">
	<link rel="stylesheet" href="static/css/home/login.css">
	<title>登录</title>
</head>

<body>
	<div class="stage" id="app" v-cloak>
		<div class="back_box">
			<img src="static/img/home/bg.png" alt="">
		</div>
		<div class="login_box" v-loading="loading">
				<div class="login_in_header"></div>
				<div class="qr_box">
					<div class="qr_out">
						<div class="qr" @click="isUserMode=!isUserMode">
							<img src="static/img/home/qr.png" alt="" v-if="isUserMode">
							<img src="static/img/home/lock.png" alt="" v-if="!isUserMode">
						</div>
					</div>
				</div>
				<div class="login_mode" v-show="isUserMode">
					<div class="input_box">
						<div class="input_ico">
							<span class="glyphicon glyphicon-user"></span>
						</div>
						<div class="input_text">
							<input v-model="username" @keyup.enter="changeEnter" id="loginname" name="loginname" type="text" class="itt" v-focus>
						</div>
					</div>
					<div class="input_box">
						<div class="input_ico">
							<span class="glyphicon glyphicon-lock"></span>
						</div>
						<div class="input_text">
							<input id="password" name="password" v-model="password"  @keyup.enter="login"  type="password" class="itt">
						</div>
					</div>
					<div class="remember_box">
						<input type="checkbox" name=""  id="saveid" class="remember" v-model="isRemember">
						<div class="rtb">
							<label for="remember" class="rmember_text">记住密码</label>
						</div>
					</div>
					<div class="btn_box">
						<button class="btn_login" @click="login"> 登&nbsp;&nbsp;&nbsp;&nbsp;录</button>
					</div>
				</div>
				<div class="qr_mode" v-show="!isUserMode">
					<div>
						<img src="static/img/home/qr.jpg" alt="">
					</div>
					<hr>
					<div class="text-center qr_text">
						扫描二维码登录
					</div>
				</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	Vue.directive('focus', {
		inserted: function (el) {
			el.focus();
		}
	});
	var app = new Vue({
		el: "#app",
		data: {
			loginUrl: "<%=basePath%>login_login",
			isUserMode: true,//登录模式
			isRemember: false,//是否记住密码
			username:"",
			password:"",
			loading: false,
			showLoginResult: false,
			loginType: "false",
			loginText: "0"
		},
		watch: {
			isRemember: function () {
				if (this.isRemember) {
					localStorage.setItem("isRemember", "1");
				} else {
					localStorage.setItem("isRemember", "0");
				}
				if (this.isRemember && this.username == "") {
					this.username = localStorage.getItem("username");
					this.password = localStorage.getItem("password");
				}
			}
		},
		mounted: function () {
			if (localStorage.getItem("isRemember") == "1") {
				this.isRemember = true;
			}
		},
		methods: {
			//提交登录信息
			login: function () {
				this.loading = true;
				if (this.isRemember) {
					localStorage.setItem("username", this.username);
					localStorage.setItem("password", this.password);
				}
				$.ajax({
					url: this.loginUrl,
					type: "post",
					data: {
						USERNAME: this.username,
						PASSWORD: this.password,
						KEYDATA:this.username+",&,"+this.password+",&,1",
					},
					success: function (res) {
						app.loading = false;
						if(res.state=="success"){
							app.$notify({
								title: '登录成功',
								message: '正在进入首页，请稍候……',
								type: 'success'
							});
							/*window.location.href="<%=basePath%>main/index";*/
							window.location.href="<%=basePath%>createCode/list.do"
						}else{
							app.loading = false;
							app.$notify({
								title: '登录失败',
								message: '请确认用户名密码是否正确。',
								type: 'error'
							});
							$("#password").focus().select();
						}
					},
					error: function (e) {
						app.loading = false;
						app.$notify({
							title: '登录失败',
							message: '请确认网络是否通畅。',
							type: 'error'
						});
						$("#password").focus().select();
					},

				});
			},
			changeEnter:function(){
				$("#password").focus().select();
			}
		}

	});
</script>
</html>