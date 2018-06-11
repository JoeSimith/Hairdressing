<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    <meta name="viewport" content="width=device-width" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta name="viewport" content="width=device-width" />
    <title>Form</title>
    <link href="static/NIFine/framework-font.css" rel="stylesheet" />
    <link href="static/NIFine/framework-theme.css" rel="stylesheet" />
	<script src="static/NIFine/jquery-2.1.1.min.js"></script>
    <script src="static/NIFine/bootstrap.js"></script>
    <link href="static/NIFine/bootstrap.min.css" rel="stylesheet" />
    <script src="static/NIFine/wdtree/tree.js"></script>
    <link href="static/NIFine/wdtree/tree.css" rel="stylesheet" />
    <link href="static/NIFine/select2/select2.min.css" rel="stylesheet" />
    <script src="static/NIFine/select2/select2.min.js"></script>
    <link href="static/NIFine/wizard/wizard.css" rel="stylesheet" />
    <script src="static/NIFine/wizard/wizard.js"></script>
    <script src="static/NIFine/validate/jquery.validate.min.js"></script>
    <script src="static/NIFine/datepicker/WdatePicker.js"></script>
    <link href="static/NIFine/framework-ui.css" rel="stylesheet" />
    <script src="static/NIFine/framework-ui.js"></script>
  </head>
<body>
<script>
	var ID = $.request("ID");
	 $(function () {
        if (!!ID) {
        	$.ajax({
                url: "<%=basePath%>broadcast/getFormJson",
                data: {ID: ID},
                dataType: "json",
                success: function (data) {                    
                	$("#form").formSerialize(data);  
                }
            });
        }
    });
</script>

<form id="form">
    <div style="margin-top: 10px; margin-left: 10px; margin-right: 10px;">
        <div style="padding-top: 20px; margin-right: 30px;">
            <table class="form">
            	<input id="ID" name="ID" type="hidden"/>
				<tr>
                    <th class="formTitle">轮播图片</th>
                    <td class="formValue">
                        <input id="picpath" name="picpath" type="text" class="form-control required" readonly/>
                    </td>
                </tr>
				<tr>
                    <th class="formTitle">轮播图片标题</th>
                    <td class="formValue">
                        <input id="pictitle" name="pictitle" type="text" class="form-control required" readonly/>
                    </td>
                </tr>
				<tr>
                    <th class="formTitle">轮播图片内容</th>
                    <td class="formValue">
                        <input id="piccontent" name="piccontent" type="text" class="form-control required" readonly/>
                    </td>
                </tr>
				<tr>
                    <th class="formTitle">轮播是否有效</th>
                    <td class="formValue">
                        <input id="available" name="available" type="text" class="form-control required" readonly/>
                    </td>
                </tr>
				<tr>
                    <th class="formTitle">轮播的顺序号</th>
                    <td class="formValue">
                        <input id="sort" name="sort" type="text" class="form-control required" readonly/>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</form>

</body>
</html>