<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Insert title here</title>
</head>
<body>
<script type="text/javascript" src="http://cdn.bootcss.com/jquery/3.1.0/jquery.min.js"></script>
<script type="text/javascript" src="http://cdn.bootcss.com/sockjs-client/1.1.1/sockjs.js"></script>
<script type="text/javascript">
    var websocket = null;
    if ('WebSocket' in window) {//判断浏览器是否支持WebSocket
        websocket = new WebSocket("ws://172.16.16.33:8080/websocket/socketServer.do");
    }
    else if ('MozWebSocket' in window) {
        websocket = new MozWebSocket("ws://172.16.16.33:8080/websocket/socketServer.do");
    }
    else {
        websocket = new SockJS("http://172.16.16.33:8080/sockjs/socketServer.do");
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
            alert("连接失败!");
        }
    }
    //窗口关闭时,将websocket连接关闭
    window.close=function()
    {
        websocket.onclose();
    }
</script>
请输入：<textarea rows="5" cols="10" id="inputMsg" name="inputMsg"></textarea>
<button onclick="doSend();">发送</button>
</body>
</html>