<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<meta name="viewport" content="width=device-width" />
		<title>列表</title>
		<meta charset="UTF-8" />
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<link href="static/NIFine/bootstrap.min.css" rel="stylesheet" />
		<link href="static/NIFine/framework-font.css" rel="stylesheet" />
		<link href="static/NIFine/framework-theme.css?v=1.0" rel="stylesheet" />
		<link href="static/NIFine/jqgrid.css" rel="stylesheet" />
		<link href="static/NIFine/framework-ui.css" rel="stylesheet" />
		<link href="static/NIFine/dialog/dialog.css" rel="stylesheet" />
		<link href="static/NIFine/dialog/layer.css" rel="stylesheet" />
		<script src="static/NIFine/jquery-2.1.1.min.js"></script>
		<script src="static/NIFine/bootstrap.js"></script>
		<script src="static/NIFine/jqgrid.min.js"></script>
		<script src="static/NIFine/grid.locale-cn.js"></script>
		<script src="static/NIFine/datepicker/WdatePicker.js"></script>
		<script src="static/NIFine/framework-ui.js?v=1.0"></script>
	</head>
<body>
	<script type="text/javascript">
		$(function () {
	        gridList();
	    });
	    function gridList() {
	        var $gridList = $("#gridList");
	        $gridList.dataGrid({
	            url: "<%=basePath%>broadcast/getGridListJson.do",
	            height: $(window).height() - 96,
	            colModel: [
					{ label: '轮播图片', name: 'picpath', width: 150, align: 'left'},
					{ label: '轮播图片标题', name: 'pictitle', width: 150, align: 'left'},
					{ label: '轮播图片内容', name: 'piccontent', width: 150, align: 'left'},
					{ label: '轮播是否有效', name: 'available', width: 150, align: 'left'},
					{ label: '轮播的顺序号', name: 'sort', width: 150, align: 'left'},
	            	{ label: "主键", name: "ID", hidden: true, key: true }
	            ],
		        pager: "#gridPager",
	            viewrecords: true
	        });
		   $("#btn_search").click(function () {
	            $gridList.jqGrid('setGridParam', {
	                postData: { "BROADCAST_NAME": $("#txt_keyword").val()},
	            }).trigger('reloadGrid');
	        });
	    }
	    
	    function btn_add() {
			$.modalOpen({
				id: "Form",
				title: "新增",
				url: "<%=basePath%>broadcast/toAdd.do",
				width: "600px",
				height: "450px",
				callBack: function (iframeId) {
				    top.frames[iframeId].submitForm();
				}
	    	});
		}
		function btn_edit() {
		    if(!$("#gridList").jqGridRowValue().ID){
		    	$.modalMsg("请选择数据", "warning");
		    }else{
		    	var ID = $("#gridList").jqGridRowValue().ID;
		     	$.modalOpen({
					id: "Form",
					title: "编辑",
					url: "<%=basePath%>broadcast/toEdit.do?ID=" + ID,
					width: "600px",
					height: "450px",
					callBack: function (iframeId) {
					    top.frames[iframeId].submitForm();
					}
		     	});
		    }
		}
	    function btn_delete() {
	        if(!$("#gridList").jqGridRowValue().ID){
	        	$.modalMsg("请选择数据", "warning");
	        }else{
	        	var ID = $("#gridList").jqGridRowValue().ID;
		        $.deleteForm({
		            url: "<%=basePath%>broadcast/delete.do",
		            param: { ID: ID },
		            success: function () {
		            	$.currentWindow().$("#gridList").trigger("reloadGrid");
		            }
		        })
	        }
	    }
	    function btn_details() {
	    	if(!$("#gridList").jqGridRowValue().ID){
	        	$.modalMsg("请选择数据", "warning");
	        }else{
	        	var ID=$("#gridList").jqGridRowValue().ID;
		        $.modalOpen({
		            id: "Form",
		            title: "查看",
		            url: "<%=basePath%>broadcast/toDetail.do?ID=" + ID ,
		            width: "600px",
		            height: "400px",
		            callBack: function (iframeId) {
		                top.frames[iframeId].submitForm();
		            },
		            btn:null
		        });
	        }
	    }
	</script>

	<div class="topPanel">
		<div class="toolbar">
			<div class="btn-group">
				<a class="btn btn-primary " onclick="$.reload()"><span class="glyphicon glyphicon-refresh"></span></a>
			</div>
			<div class="btn-group">           
	            <a id="NF-add" authorize="yes" class="btn btn-primary dropdown-text" onclick="btn_add()"><i class="fa fa-plus"></i>添加</a>
	        </div>
	        <div class="btn-group">           
	            <a id="NF-edit" authorize="yes" class="btn btn-primary dropdown-text" onclick="btn_edit()"><i class="fa fa-pencil-square-o"></i>编辑</a>
	        </div>
	        <div class="btn-group">           
	            <a id="NF-delete" authorize="yes" class="btn btn-primary dropdown-text" onclick="btn_delete()"><i class="fa fa-trash-o"></i>删除</a>
	        </div>
	        <div class="btn-group">           
	            <a id="NF-details" authorize="yes" class="btn btn-primary dropdown-text" onclick="btn_details()"><i class="fa fa-search-plus"></i>查看</a>
	        </div>
			<script>$('.toolbar').authorizeButton()</script>
		</div>
		<div class="search">
			<table>
				<tbody>
					<tr>
						<td>
							<div class="input-group">
								<input id="txt_keyword" type="text" class="form-control" placeholder="名称" style="width: 200px;">
								<span class="input-group-btn">
									<button id="btn_search" type="button" class="btn  btn-primary">
										<i class="fa fa-search"></i>
									</button>
								</span>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<div class="gridPanel">
		<table id="gridList"></table>
		<div id="gridPager"></div>
	</div>
</body>
</html>

