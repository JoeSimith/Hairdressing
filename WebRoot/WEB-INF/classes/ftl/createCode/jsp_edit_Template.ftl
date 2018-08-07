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
	var ${primaryKey} = $.request("${primaryKey}");
	 $(function () {
         <!--下拉选范例-->
         initSelect();
         <!--下拉选范例 END-->
        if (!!${primaryKey}) {
        	$.ajax({
                url: "<%=basePath%>${objectNameFirstLower}/getFormJson",
                data: {${primaryKey}: ${primaryKey}},
                dataType: "json",
                success: function (data) {                    
                	$("#form").formSerialize(data);  
                }
            });
        }
    });
    function submitForm() {
    	if (!$('#form').formValid()) {
            return false;
        }
        $.submitForm({
            url: "<%=basePath%>${objectNameFirstLower}/saveOrUpdate.do",
            param: $("#form").formSerialize(),
            success: function () {
            	$.currentWindow().$("#gridList").trigger("reloadGrid");
            }
        })
    }
    <!--下拉选控件范例-->
    function initSelect() {
        $("#SELECT_DEMO").bindSelect({
            url: "<%=basePath%>${objectNameFirstLower}/getSelectJson.do"
        });
	}
	<!--下拉选控件范例 END-->
</script>

<form id="form">
    <div style="margin-top: 10px; margin-left: 10px; margin-right: 10px;">
        <div style="padding-top: 20px; margin-right: 30px;">
            <table class="form">
            	<input id="${primaryKey}" name="${primaryKey}" type="hidden"/>
            	<#list fieldList as var>
							<#if var[3] == "是">
				<tr>
                    <th class="formTitle">${var[2]}</th>
                    <td class="formValue">
                        <input id="${var[0]}" name="${var[0]}" type="text" class="form-control required" />
                    </td>
                </tr>
							</#if>
				</#list>
                <!--下拉选控件范例-->
                <tr>
                    <th class="formTitle">下拉选范例</th>
                    <td class="formValue">
                        <select id="SELECT_DEMO" name="SELECT_DEMO" class="form-control required">
                            <option value="">-请选择-</option>
                        </select>
                    </td>
                </tr>
                <!--下拉选控件范例 END-->
                <!--时间选择控件范例-->
                <tr>
                    <th class="formTitle">时间选择范例</th>
                    <td class="formValue">
                        <input id="TIME_DEMO" name="TIME_DEMO" type="text" class="form-control input-wdatepicker required" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
                    </td>
                </tr>
                <!--时间选择控件范例 END-->
            </table>

        </div>
    </div>
</form>

</body>
</html>