<%@ page language="java" contentType="text/html; charset=utf-8"
pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Websocket Demo</title>
</head>
<body>
Websocket Demo
<hr>
<button id="ws">使用ws创建连接</button>
<%--<button id="sockjs">使用sockjs创建连接</button>--%>
<button id="close">关闭websocket连接</button>

<br/><br/><br/>
请输入：<textarea rows="5" cols="10" id="inputMsg" name="inputMsg"></textarea>
<button onclick="doSend();">发送</button>

</body>
</html>
<script type="text/javascript" src="../js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="../js/sockjs.min.js"></script>
<script type="text/javascript">
    var websocket = null;
    if ('WebSocket' in window) {//判断浏览器是否支持WebSocket
        //websocket = new WebSocket("ws://localhost:8080/websocket/socketServer.do");
        websocket = new WebSocket("ws://localhost:8080/websocket/socketServer.do");
    }
    else if ('MozWebSocket' in window) {
        //websocket = new MozWebSocket("ws://localhost:8080/websocket/socketServer.do");
        websocket = new MozWebSocket("ws://localhost:8080/websocket/socketServer.do");
    }
    else {
        //websocket = new SockJS("http://localhost:8080/sockjs/socketServer.do");
        websocket = new SockJS("http://localhost:8080/sockjs/socketServer.do");
    }


    websocket.onopen = onOpen;
    websocket.onmessage = onMessage;
    websocket.onerror = onError;
    websocket.onclose = onClose;

    function onOpen(result) {
        alert("连接建立时触发:"+result.data);
    }

    function onMessage(result) {
        alert("客户端接收服务端数据时触发:"+result.data);
    }
    function onError(result) {
        alert("通信发生错误时触发:"+result.data);
    }
    function onClose(result) {
        alert("连接关闭时触发:"+result.data);
    }
    //使用连接发送数据
    function doSend() {
        if (websocket.readyState == websocket.OPEN) {
            var msg = document.getElementById("inputMsg").value;
            websocket.send(msg);//调用后台handleTextMessage方法
            alert("发送成功!");
        } else {
            alert("连接已关闭!");
        }
    }
    //窗口关闭时,将websocket连接关闭
    window.close=function()
    {
        websocket.onclose();
    }


    $(function () {
        $("#ws").click(function () {
            //ws_connect();
            connect();
        });
        /*$("#sockjs").click(function () {
            sockjs_connect();
        });*/
        $("#close").click(function () {
            websocket.close();
        });
    });
    /*function ws_connect() {
        websocket = new WebSocket("ws://localhost:8080/websocket/socketServer.do");
    }
    function sockjs_connect() {
        websocket = new SockJS("http://localhost:8080/sockjs/socketServer.do");
    }*/
    function connect() {

    }

</script>