<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>聊天廣場</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/utility/css/bootstrap.min.css">
<link rel="shortcut icon" href="<%= request.getContextPath()%>/favicon.ico" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/front-end/chatground/css/chatground.css">
</head>
<body>
	<jsp:include page="/front-end/index/NavBar.jsp" flush="true" />
	<div class="container">
        <div class="row justify-content-center">
            <div class="col-12 px-0 py-0 row justify-content-center">
                <div class=" col-10 bg-dark text-white my-0" id="title">
                    	聊天廣場
                </div>
                <div class="col-10 overflow-auto mx-0 my-0 px-0 py-0" id="messagesArea">
                    <ul id="area">
                    </ul>
                </div>
                <div class="col-10 px-0">
                    <textarea class="form-control col-9 float-left" rows="1" placeholder="請輸入文字"></textarea>
                    <button class="btn btn-secondary col-auto mx-1 my-0" id="text_submit">發送</button>
                </div>
                <div class="col-10 row px-0">
                    <div class="col-4 mx-0 px-0">
                        <h6>現在線上人數：<span id="onlineCounter"></span></h6>
                    </div>
                </div>
            </div>
        </div>
    </div>
	<script src="<%= request.getContextPath()%>/utility/js/jquery-3.5.1.min.js"></script>
    <script src="<%= request.getContextPath()%>/utility/js/popper.min.js"></script>
    <script src="<%= request.getContextPath()%>/utility/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		var myPoint = "/ChatGroundWS/${sessionScope.memberVO_memNo}";
	    var host = window.location.host;
	    var path = window.location.pathname;
	    var webCtx = path.substring(0, path.indexOf("/", 1));
	    var endPointURL = "ws://" + host + webCtx + myPoint;
	    // console.log(host); //協定名稱+port阜號
	    // console.log(path); //context到之後的url
	    // console.log(webCtx); //substing取出context名稱
	
	    var self = "${sessionScope.memberVO_memNo}";
	    
	</script>
    <script src="<%= request.getContextPath()%>/front-end/chatground/js/chatground.js"></script>

</body>
</html>