<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>模板引擎代码生成</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta charset="UTF-8"/>
	
	<link href="static/framework-font.css" rel="stylesheet" />
    <link href="static/framework-theme.css?v=1.0" rel="stylesheet" />
    <script src="static/jquery-2.1.1.min.js"></script>
    <script src="static/bootstrap.js"></script>
	<link href="static/bootstrap.min.css" rel="stylesheet" />
    <link href="static/framework-ui.css" rel="stylesheet" />
    <script src="static/framework-ui.js?v=1.0"></script>
    <script src="static/validate/jquery.validate.min.js"></script>
	
    <link data-jsfiddle="common" rel="stylesheet" media="screen" href="static/handsontable/dist/handsontable.css">
    <link data-jsfiddle="common" rel="stylesheet" media="screen" href="static/handsontable/dist/pikaday/pikaday.css">
    <script data-jsfiddle="common" src="static/handsontable/dist/pikaday/pikaday.js"></script>
    <script data-jsfiddle="common" src="static/handsontable/dist/moment/moment.js"></script>
    <script data-jsfiddle="common" src="static/handsontable/dist/zeroclipboard/ZeroClipboard.js"></script>
    <script data-jsfiddle="common" src="static/handsontable/dist/numbro/numbro.js"></script>
    <script data-jsfiddle="common" src="static/handsontable/dist/numbro/languages.js"></script>
    <script data-jsfiddle="common" src="static/handsontable/dist/handsontable.js"></script>
	
  </head>
  
  <body>
  
  <script>
  var data = [
      ['', '', '', '', '']
  ];
	$(function () {
	    	initControl();
    });
    function initControl() {        
        var isEdit = ['是','否'];
        var isPK = ['是','否'];
         $("#fieldList").handsontable({
             data: data,                   
             minSpareRows: 1,
             colHeaders: ["字段", "数据类型", "字段名称", "是否可修改", "是否主键"],
             colWidths: [200, 200, 200, 200, 200],
             stretchH: 'none',
             columns: [
                 { data: 'field' },
                 {   data: 'dataType',
                     type: 'dropdown',
                     source: ['String', 'Integer'] },
                 { data: 'fieldName' },
                 {
                     data: 'isEdit',
                     type: 'dropdown',
                     source: isEdit
                 },
                 {
                     data: 'isPK',
                     type: 'dropdown',
                     source: isPK
                 }
             ]
         });
    }
    function submitForm() {
    	var $container = $("#fieldList");
        var tableData = $container.data('handsontable').getData();
        temp = "";
        for (var i = 0; i < tableData.length; i++) {
            if (tableData[i][0] != null && tableData[i][0] != "") {
            	temp+=tableData[i][0]+","+tableData[i][1]+","+tableData[i][2]+","+tableData[i][3]+","+tableData[i][4]+"|"
            }
        }
        
        $("#fieldListData").val(temp);
        if (!$('#form1').formValid()) {
            return false;
        }
        $.submitForm({
            url: "/createCode/submit",
            param: $("#form1").formSerialize(),
            success: function (data) {
            	window.location.href="<%=basePath%>admin/ftl/code.zip";
            }
        })
    }
</script>
	<div class="ui-layout" id="layout" style="height: 600px; width: 100%;">
	    <div class="ui-layout-west">
	        <div id="itemTree"></div>
	    </div>
	    <div class="ui-layout-center">
	        <div class="topPanel">
	        
	            <div class="toolbar">
	            		<div class="btn-group">
				            <a class="btn btn-primary " onclick="$.reload()"><span class="glyphicon glyphicon-refresh"></span></a>
				        </div>
				        <div class="btn-group">
				            <a id="NF-create" authorize="yes" class="btn btn-primary dropdown-text" onclick="submitForm()"><i class="fa fa-plus"></i>生成代码</a>            
				        </div>
	            </div>
	        </div>
	        <div class="gridPanel">
	        	<div style="padding-top: 20px; margin-right: 30px;">
	        		<form id="form1">
				        <table class="form">
				            <tr>
				                <th class="formTitle">包名</th>
				                <td class="formValue">
				                    <input id="packageName" name="packageName" type="text" class="form-control required" />
				                </td>    
				                <th class="formTitle">类名</th>
				                <td class="formValue">
				                    <input id="objectName" name="objectName" type="text" class="form-control required" />
				                </td> 
				                <th class="formTitle">表开头字段</th>
				                <td class="formValue">
				                     <input id="tabletop" name="tabletop" type="text" class="form-control required" /> 
				                     <input id="fieldListData" name="fieldListData" type="hidden"/>                  
				                </td>           
				            </tr>
				        </table>
			        </form>
					<table align="center">
						<tr>
							<td>
								<div id="fieldList"></div>
							</td>
						</tr>
					</table>
				</div>
	        </div>
	    </div>
	</div>
  </body>
</html>
