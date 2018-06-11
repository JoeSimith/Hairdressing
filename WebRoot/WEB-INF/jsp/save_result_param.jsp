<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>保存结果</title>
<base href="<%=basePath%>">
<meta name="description" content="overview & stats" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<%@ include file="system/admin/top.jsp"%> 
<style type="text/css">
	.dialog_size{width:400px;height:400px}
</style>
<script src="static/js/bootstrap.min.js"></script>
<script type="text/javascript" src="static/js/bootbox.min.js"></script><!-- 确认窗口 -->
</head>
<body>
	<div id="zhongxin"></div>
	<script type="text/javascript">
	    var XL_ID='${pd.XL_ID}';
	    var BD_ID='${pd.BD_ID}';
	    var GD_ID='${pd.GD_ID}';
	    var CZM='${pd.CZM}';
	    var JCXM='${pd.JCXM}';
	    var BCCSSJBEGIN='${pd.BCCSSJBEGIN}';
	    var BCCSSJEND='${pd.BCCSSJEND}';
 		var msg = '${msg}';
		var gourl = '${gourl}';
		if(msg=="success" || msg==""){
			if (gourl != "") {
				location.href = '<%=basePath%>' + gourl+"?XL_ID="+XL_ID+"&BD_ID="+BD_ID+"&GD_ID="+GD_ID+"&CZMC="+CZM+"&JCXMLX_ID="+JCXM+"&BCCSSJBEGIN="+BCCSSJBEGIN+"&BCCSSJEND="+BCCSSJEND;
			} else {
				document.getElementById('zhongxin').style.display = 'none';
				window.parent.Dialog.close();
			}
		}else{
			var html = '<div style="background-color: #fff;font-size:14px;width:450px;height:150px;overflow-y:auto">'
				+'<div style="display:table;height:120px;"><div style="vertical-align:middle;display:table-cell;padding: 10px;">'
				+msg+'</div></div></div>';
			var diag = new Dialog();
 			 diag.Drag=false;
 			 diag.Title ="错误消息";
 			 diag.InnerHtml = html;
 			 diag.Width = 450;
 			 diag.Height = 150;
 			 diag.OKEvent = function(){ //关闭事件
 				diag.close();
 				document.getElementById('zhongxin').style.display = 'none';
				 if (gourl != "") {
					location.href = '<%=basePath%>' + gourl+"?XL_ID="+XL_ID+"&BD_ID="+BD_ID+"&GD_ID="+GD_ID+"&CZMC="+CZM+"&JCXMLX_ID="+JCXM+"&BCCSSJBEGIN="+BCCSSJBEGIN+"&BCCSSJEND="+BCCSSJEND;
				 }
 			 };
			diag.show();
			Dialog.close();
			diag.show();


		}
	</script>
</body>
</html>