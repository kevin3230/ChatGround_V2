    var stompClient = null;

    function connect(){
        var socket = new SockJS('/endpointChatground');
        stompClient = Stomp.over(socket);
        stompClient.debug = null;   //不顯示SockJS互相傳送的heartBeat訊息log,debug時才開啟
        stompClient.connect({},function(data){
            // console.log("Connected success: " + data);
            $("#area").html(""); //清空對話框
            $("#area").append($("<li>").text("歡迎來到聊天廣場，在這裡可以暢所欲言"));
            $("#text_submit").prop("disabled", false);
            stompClient.subscribe('/topic/getResponse', function(response){
                // console.log("response success: " + response);
                showMessage(response);
            });
            //取得線上人數onlinecounter
            let jsonObj = {
                "type": "onOpen",
                "sender": self
            };
            stompClient.send("/messageControl", {}, JSON.stringify(jsonObj));
        });
    }

    function showMessage(event) {
        var jsonObj = JSON.parse(event.body);
        if ("chat" === jsonObj.type) {
            $("#area").append($("<li>").text(jsonObj.senderNickname + ":  " + jsonObj.message));
            $("#onlineCounter").text(jsonObj.onlineCounter);
            scrollBottom();
        }else if("onOpen" === jsonObj.type){
            $("#onlineCounter").text(jsonObj.onlineCounter);
        }else if("onClose"){}
    }

    function sendMessage() {
        var textarea = $("textarea");
        var message = textarea.val().trim();
        // console.log("message: " + message);
        // console.log("stompClient: " + stompClient);

        if ("" === message) {
            textarea.focus();
        } else {
            var jsonObj = {
                "type": "chat",
                "sender": self,
                "senderNickname": selfNickname,
                "message": message
            };
            stompClient.send("/messageControl", {}, JSON.stringify(jsonObj));
        }
        textarea.val("");
        textarea.focus();
    }

    function disconnect() {
        if(stompClient != null){
            let jsonObj = {
                "type": "onClose",
                "sender": self
            };
            stompClient.send("/messageControl", {}, JSON.stringify(jsonObj));
            stompClient.disconnect();
        }
        $("#text_submit").prop("disabled", true);
        console.log("stompClient disconnect");
    }

    function scrollBottom() {
        //對話框滾軸置底
        var messagesArea = document.getElementById('messagesArea');
        messagesArea.scrollTop = messagesArea.scrollHeight;
    }

    function addListener() {
        $("#text_submit").click(sendMessage);
        $("textarea").keyup(function(event) {
            //判斷組合鍵,不是shift+enter就改寫enter的默認功能
            if (!event.shiftKey && event.keyCode === 13) {
                sendMessage();
            }
        });

        //限制輸入框字數400字以內
        $("textarea").bind('input propertychange', function(){
            let textarea = $("textarea");
            if(textarea.val().length >400){
                textarea.val(textarea.val().substring(0, 400));
            }
        });
    }

    $(document).ready(function() {
        $("#text_submit").prop("disabled", true);
        $("#area").append($("<li>").text("連線中..."));
        scrollBottom();
        addListener();
        connect();
    });

    window.onbeforeunload = function() {
        disconnect();
    }