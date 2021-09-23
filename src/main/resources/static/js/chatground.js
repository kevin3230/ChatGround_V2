        var webSocket;

        //建立websockt連線
        function connect() {
            webSocket = new WebSocket(endPointURL);

            webSocket.onopen = function(event) {
                console.log("Connect Success!");
                $("#area").html(""); //清空對話框
                $("#area").append($("<li>").text("歡迎來到聊天廣場，在這裡可以暢所欲言"));
                $("#text_submit").prop("disabled", false);
            }

            webSocket.onmessage = function(event) {
                var jsonObj = JSON.parse(event.data);
                if ("chat" === jsonObj.type) {
                    $("#area").append($("<li>").text(jsonObj.sender + ":  " + jsonObj.message));
                    $("#onlineCounter").text(jsonObj.onlineCounter);
                    scrollBottom();
                }else if("open" === jsonObj.type) {
                	$("#onlineCounter").text(jsonObj.onlineCounter);
                	scrollBottom();
                }
            }

            webSocket.onclose = function(event) {
                console.log("disconnect");
            }

            webSocket.onerror = function(event) {
                $("#area").append($("<li>").text("連線失敗"));
                scrollBottom();
            }
        }

        function sendMessage() {
            var textarea = $("textarea");
            var receiver = "chatground";
            var message = textarea.val().trim();

            if ("" === message) {
                textarea.focus();
            } else {
                var jsonObj = {
                    "type": "chat",
                    "sender": self,
                    "message": message
                };
                webSocket.send(JSON.stringify(jsonObj));

            }
            textarea.val("");
            textarea.focus();

        }

        function scrollBottom() {
            //對話框滾軸置底
            var messagesArea = document.getElementById('messagesArea');
            messagesArea.scrollTop = messagesArea.scrollHeight;
        }

        function disconnect() {
            webSocket.close();
            $("#text_submit").prop("disabled", true);
        }

        function addListener() {
            $("#text_submit").click(sendMessage);
            $("textarea").keyup(function(event) {
                //判斷組合鍵,不是shift+enter就改寫enter的默認功能
                if (!event.shiftKey && event.keyCode === 13) {
                    sendMessage();
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
        window.onunload = function() {
            disconnect();
        }
